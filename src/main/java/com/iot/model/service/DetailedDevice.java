package com.iot.model.service;


public record DetailedDevice (long deviceId, String sensorName,  String sensorState) {

    @Override
    public String toString() {
        int spaces = 15;
        return String.format("%s - %s", sensorName + " ".repeat(spaces - sensorName.length()), sensorState);
    }

}
