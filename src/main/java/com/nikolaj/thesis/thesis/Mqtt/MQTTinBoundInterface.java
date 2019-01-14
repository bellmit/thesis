package com.nikolaj.thesis.thesis.Mqtt;

public interface MQTTinBoundInterface {

    public void subscribeMessage(String topic);


    public void disconnect();
}
