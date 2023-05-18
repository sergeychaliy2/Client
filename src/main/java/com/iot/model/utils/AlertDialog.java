package com.iot.model.utils;

import javafx.scene.control.Alert;

import static com.iot.model.utils.AlertDialog.CustomAlert.EXCEPTION;

public final class AlertDialog {

    private final static Alert exceptionAlert = new Alert(Alert.AlertType.ERROR);
    private final static Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);

    public enum CustomAlert {INFORMATION, EXCEPTION}

    private AlertDialog () {}

    public static Alert alertOf (CustomAlert type, String title, String body) {
        Alert alert;
        if (type.equals(EXCEPTION)) alert = exceptionAlert;
        else                        alert = informationAlert;


        alert.setTitle(title);
        alert.setContentText(body);
        return alert;
    }

}
