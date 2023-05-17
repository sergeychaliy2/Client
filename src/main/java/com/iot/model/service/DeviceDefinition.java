package com.iot.model.service;


public record DeviceDefinition(long id, String name, String description) {

    @Override
    public String toString() {
        int spaces = 30;
        return String.format("%s%s", name + " ".repeat(spaces - name.length()), description);
    }
}
