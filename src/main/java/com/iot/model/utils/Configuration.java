package com.iot.model.utils;

public class Configuration {
    private final static Configuration instance = new Configuration();
    private static final String httpConnectionType = "http://";
    private static final String wsConnectionType = "ws://";
    private static final String host = "151.248.116.208";
    private static final Integer port = 8000;

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
