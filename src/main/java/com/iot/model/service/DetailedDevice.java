package com.iot.model.service;


public record DetailedDevice (long deviceId, String sensorName, String sensorState) {

    @Override
    public String toString() {
        return String.format("%s,    %s", sensorName, sensorState);
    }

}
