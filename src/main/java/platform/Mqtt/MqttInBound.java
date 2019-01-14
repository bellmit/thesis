package platform.Mqtt;


import platform.util.InfluxDBConfig;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.concurrent.TimeUnit;


@Configuration
public class MqttInBound {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String topic = "s121704/empatica";
    protected int qos = 0;
    protected String broker = "tcp://iot.eclipse.org:1883";

    //set up influxDB
    InfluxDBConfig influxDBConfig = new InfluxDBConfig();
    InfluxDB influxDB = influxDBConfig.connectToInflux();
    BatchPoints batchPoints = BatchPoints.database("thesisDB")
            .build();

    private MqttPahoMessageDrivenChannelAdapter adapter;


    public void setTopics(String topics) {
        adapter.addTopic(topics);
        logger.info(topics);
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }


    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setServerURIs(broker);
        return factory;
    }


    @Bean
    public IntegrationFlow mqttIntegrationflow() {
        return IntegrationFlows.from(mqttAdapter())
                .transform(p -> p  )
                .handle(handler())
                .handle(logger())
                .get();
    }
    @Bean
    public MessageProducerSupport mqttAdapter() {
        adapter = new MqttPahoMessageDrivenChannelAdapter("client1",
                mqttClientFactory(), topic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(qos);
        return adapter;
    }


    @Bean
    public MessageHandler handler() {
        return new ServiceActivatingHandler(message -> {
            String payload = message.getPayload().toString();
            String[] fields ;
            fields = payload.split(",");
            String user = fields[0];
            String field = fields[1];
            String value = fields[2];
            String eTime = fields[3];
            Point point1 = Point.measurement(user)
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addField("type", field)
                    .addField("value", value)
                    .addField("eTime", eTime)
                    .build();
            batchPoints.point(point1);
            influxDB.write(batchPoints);
            return message.getPayload();
        });
    }

    private LoggingHandler logger() {
        LoggingHandler loggingHandler = new LoggingHandler("INFO");
        loggingHandler.setLoggerName("MQTTLOGGER");
        return loggingHandler;
    }


}
