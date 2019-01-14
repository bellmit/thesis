package com.nikolaj.thesis.thesis.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class InfluxDBConfig {
    private static final Logger logger = LogManager.getLogger();
    InfluxDB influxDB;

    public InfluxDB connectToInflux() {

        try {
             influxDB = InfluxDBFactory.connect("http://localhost:8086", "nking", "junior");

        }catch (NullPointerException e){
            logger.debug(e);
        }finally {
            return influxDB;
        }

    }


}
