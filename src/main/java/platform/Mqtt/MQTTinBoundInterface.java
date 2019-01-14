package platform.Mqtt;

public interface MQTTinBoundInterface {

    public void subscribeMessage(String topic);


    public void disconnect();
}
