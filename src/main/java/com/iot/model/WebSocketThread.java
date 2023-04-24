package com.iot.model;

public class WebSocketThread extends Thread {

    private final CustomWebSocketHandler webSocket;

    public WebSocketThread (CustomWebSocketHandler webSocket) {
        this.webSocket = webSocket;
    }
    @Override public void run() {
        webSocket.connect();
    }
}
