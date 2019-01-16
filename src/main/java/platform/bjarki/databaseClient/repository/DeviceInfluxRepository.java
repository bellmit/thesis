package platform.bjarki.databaseClient.repository;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.jboss.logging.Logger;
import platform.util.InfluxDBConfig;

import java.util.concurrent.TimeUnit;

public class DeviceInfluxRepository {

    private static final Logger logger = Logger.getLogger(platform.bjarki.databaseClient.repository.DeviceInfluxRepository.class.getName());

    private String dbName = "thesisDB";
    //set up influxDB
    InfluxDBConfig influxDBConfig = new InfluxDBConfig();
    InfluxDB influxDB = influxDBConfig.connectToInflux();

    public void insertValuesInflux(String type, String device, String attributes, String dataid, String line) {
        influxDB.write(Point.measurement("eyetracker")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("device", device)
                .addField("type", type)
                .addField("attributes", attributes)
                .addField("dataid", dataid)
                .addField("line", line)
                .build());

    }
}
