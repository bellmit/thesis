package com.nikolaj.thesis.thesis.controller;

import com.nikolaj.thesis.thesis.Mqtt.MqttInBound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MqttController {


    @Autowired
    MqttInBound subscriber;


    @RequestMapping(value = "/mqtt/settopic", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    public String index(@RequestBody  String topic) {

        subscriber.setTopics(topic);
        return "topic set";
    }
}
