package com.iot.model.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import static com.iot.model.utils.AlertDialog.CustomAlert.CONFIRMATION;
import static com.iot.model.utils.AlertDialog.CustomAlert.EXCEPTION;

public final class AlertDialog {

    private final static Alert exceptionAlert = new Alert(Alert.AlertType.ERROR);
    private final static Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
    private final static Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "%s", ButtonType.YES, ButtonType.NO);

    public enum CustomAlert {INFORMATION, EXCEPTION, CONFIRMATION}

    private AlertDialog () {}

    public static Alert alertOf (CustomAlert type, String title, String body) {
        Alert alert;
        if (type.equals(EXCEPTION))         alert = exceptionAlert;
        else if (type.equals(CONFIRMATION)) alert = confirmationAlert;
        else                                alert = informationAlert;

        alert.setTitle(title);
        alert.setContentText(body);
        return alert;
    }

}
