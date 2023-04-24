package com.iot.model;

public class Configuration {
    private final static Configuration instance = new Configuration();
    private final String httpConnectionType = "http://";
    private final String wsConnectionType = "ws://";
    private final String host = "151.248.116.208";
    private final Integer port = 8000;

    public String getHttpConnectionType() {
        return httpConnectionType;
    }

    public String getWsConnectionType() {
        return wsConnectionType;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public static Configuration getInstance() {
        return instance;
    }
}
