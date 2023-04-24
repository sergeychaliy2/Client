package com.iot.model.responses;

public enum SocketResponses {

    BOARD_WAS_FOUND("Board was found"),
    CLIENTS_WAS_NOT_CONNECTED("Clients was not connected"),
    BOARD_ID_SUCCESSFULLY_RECEIVED("Board id successfully received"),
    SENDING_BOARD_ID_ACCEPTED("Sending board id accepted");
    private final String responseName;
    private SocketResponses(String responseName) {
        this.responseName = responseName;
    }

    public String getResponseName() { return responseName; }
}
