package com.iot.model.utils;

public class Configuration {
    private final static Configuration instance = new Configuration();
    private final String httpConnectionType = "http://";
    private final String wsConnectionType = "ws://";
    private final String host = "151.248.116.208";
    private final Integer port = 8000;

    public String generate(boolean isBasicHttp, String endPoint) {
        return String.format(
                "%s%s%d%s",
                isBasicHttp ? httpConnectionType : wsConnectionType,
                host + ":",
                port,
                endPoint
        );
    }

    public static Configuration getInstance() {
        return instance;
    }
}
