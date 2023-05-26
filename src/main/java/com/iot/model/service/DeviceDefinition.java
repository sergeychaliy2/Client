package com.iot.model.service;


public record DeviceDefinition(long id, String name, String description, short orderedNum) {

    @Override
    public String toString() {
        return String.format("%d. %s,    %s", orderedNum, name, description);
    }
}
