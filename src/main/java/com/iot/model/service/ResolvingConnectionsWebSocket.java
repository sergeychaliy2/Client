package com.iot.model.service;

import com.iot.model.utils.AlertDialog;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ResolvingConnectionsWebSocket extends WebSocketClient {
    public ResolvingConnectionsWebSocket(String path) {
        super(URI.create(path));
    }

    public void setHeaders(HashMap<String, String> headers) {
        for (Map.Entry<String, String> node : headers.entrySet()) {
            this.addHeader(node.getKey(), node.getValue());
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        sendWithDelay("connect", null);
        sendWithDelay("update", null);
    }

    private void sendWithDelay(String msg, Long customDelay) {
        if (customDelay == null) customDelay = 1000L;

        try {
            Thread.sleep(customDelay);
            this.send(msg);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessage(String s) {
        switch(s) {
            case "1" ->  sendWithDelay("0", null);
            case "Submitting request received" -> {
                Platform.runLater(() -> {
                    Alert alert = AlertDialog.alertOf(
                            AlertDialog.CustomAlert.CONFIRMATION,
                            "Активное подключение",
                            "Ваше устройство пытается подключиться?"
                    );

                    alert.showAndWait();

                    if (alert.getResult().equals(ButtonType.YES))   sendWithDelay("submit", null);
                    else                                            sendWithDelay("decline", null);
                });
            }
            case "Device was submitted", "Device was declined" -> sendWithDelay("update", 2500L);
            case "Update is null" -> sendWithDelay("update", 5000L);
        }
    }


    @Override
    public void onClose(int i, String s, boolean b) {
        this.close();
    }

    @Override
    public void onError(Exception e) {
        this.close();
    }
}
