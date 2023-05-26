package com.iot.model.service;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.iot.model.constants.Responses.Socket.*;


public class ConnectionWebSocket extends WebSocketClient {
    private final static String boardIdPattern = "{ \"boardIdentificationData\" : { \"boardUUID\": \"%s\" } }";
    private final String uuid;
    private final Text textLabel;
    private final ImageView loadingCircle;
    private final ResolvingConnectionsWebSocket resolvingConnectionsWS;
    private final Pane box;

    public ConnectionWebSocket(String path, String uuid,
                               Pane box,  Text textLabel,
                               ImageView loadingCircle,
                               ResolvingConnectionsWebSocket resolvingConnectionsWS,
                               HashMap<String, String> headers)
    {
        super(URI.create(path));
        this.uuid = uuid;
        this.box = box;
        this.textLabel = textLabel;
        this.loadingCircle = loadingCircle;
        this.resolvingConnectionsWS = resolvingConnectionsWS;
        setHeaders(headers);
    }

    private void setHeaders(HashMap<String, String> headers) {
        for (Map.Entry<String, String> node : headers.entrySet()) {
            this.addHeader(node.getKey(), node.getValue());
        }
     }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        loadingCircle.setVisible(true);
        send("connect");
    }

    @Override
    public void onMessage(String message) {
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

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                box.setVisible(false);
                VBox vBox = (VBox) box.getChildren().get(0);
                vBox.setVisible(false);

                box.getChildren()
                        .stream()
                        .filter(it-> !it.equals(loadingCircle) && !it.equals(vBox))
                        .forEach(it->
                                {
                                    it.setDisable(true);
                                    it.setVisible(true);
                                }
                        );

                this.close();
            }
        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        this.close();
        resolvingConnectionsWS.connect();
    }

    @Override
    public void onError(Exception e) {
        this.close();
        resolvingConnectionsWS.connect();
        System.out.println("ВС для отслеживания включен");
    }

}
