package com.iot.model.responses;

public enum ConnectionErrors {

    UUID_FORMAT_IS_NOT_CORRECT("Формат введённого айди неправильный"),
    UNKNOWN_SOCKET_EXCEPTION("Неизвестная ошибка при подключении");

    private final String exceptionName;
    private ConnectionErrors(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getExceptionName() {
        return exceptionName;
    }
}
