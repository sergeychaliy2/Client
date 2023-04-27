package com.iot.model.service;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

import static com.iot.model.constants.Responses.Socket.*;


public class CustomWebSocketHandler extends WebSocketClient {
    private final String boardIdPattern = "{ \"boardIdentificationData\" : { \"boardUUID\": \"%s\" } }";
    private final String uuid;
    private final Text textLabel;
    private final ImageView loadingCircle;
    public CustomWebSocketHandler(URI serverUri,
                                  Map<String, String> httpHeaders,
                                  String uuid, Text textLabel,
                                  ImageView loadingCircle) {
        super(serverUri, httpHeaders);
        this.uuid = uuid;
        this.textLabel = textLabel;
        this.loadingCircle = loadingCircle;

        loadingCircle.setVisible(true);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        send("connect");
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
        switch (message) {
            case SENDING_BOARD_ID_ACCEPTED      -> send(String.format(boardIdPattern, uuid));
            case BOARD_ID_SUCCESSFULLY_RECEIVED -> textLabel.setText(CLIENT_SEARCHING);
            case CLIENTS_WAS_NOT_CONNECTED      ->  {
                textLabel.setText(BOARD_WAS_NOT_FOUND);
                loadingCircle.setVisible(false);
                this.close();
            }
            case BOARD_WAS_FOUND -> {
                textLabel.setText(BOARD_WAS_FOUND_RU);
                loadingCircle.setVisible(false);
                this.close();
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
