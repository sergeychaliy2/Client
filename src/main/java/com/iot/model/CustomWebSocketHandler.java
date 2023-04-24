package com.iot.model;

import com.iot.model.responses.SocketResponses;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

import static com.iot.model.responses.ConnectionErrors.UNKNOWN_SOCKET_EXCEPTION;

public class CustomWebSocketHandler extends WebSocketClient {
    private final String boardIdPattern = "{ \"boardIdentificationData\" : { \"boardUUID\": \"%s\" } }";
    private final String uuid;

    private final Text textLabel;
    public CustomWebSocketHandler(URI serverUri, Map<String, String> httpHeaders, String uuid, Text textLabel) {
        super(serverUri, httpHeaders);
        this.uuid = uuid;
        this.textLabel = textLabel;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        send("connect");
    }

    @Override
    public void onMessage(String message) {
        textLabel.setText(message);
        SocketResponses response = null;

        for (SocketResponses x : SocketResponses.values()) {
            if (x.getResponseName().equals(message)) {
                response = x;
                break;
            }
        }

        if (response == null) {
            throw new RuntimeException(UNKNOWN_SOCKET_EXCEPTION.getExceptionName());
        }

        switch (response) {

            case SENDING_BOARD_ID_ACCEPTED -> send(String.format(boardIdPattern, uuid));
            case BOARD_ID_SUCCESSFULLY_RECEIVED -> {}
            case CLIENTS_WAS_NOT_CONNECTED -> {
                //todo в infoTextLabel}
            }
            case BOARD_WAS_FOUND -> {
                //todo в infoTextLabel}
            }
        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
