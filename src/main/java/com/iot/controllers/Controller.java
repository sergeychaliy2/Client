package com.iot.controllers;

import com.iot.model.auth.AuthenticateModel;
import com.iot.model.utils.AlertDialog;
import com.iot.model.utils.ServerResponse;
import com.iot.scenes.SceneChanger;
import com.iot.scenes.ScenesNames;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.regex.Pattern;

import static com.iot.scenes.ScenesNames.*;

public abstract class Controller {
    @FXML
    protected Text infoTextLabel;
    @FXML
    protected ImageView loadingCircle;

    protected static final Pattern patternLogin = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    protected static final Pattern patternPassword = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,49}$");
    protected static final Pattern patternCode = Pattern.compile("\\d{6}$");


    protected Stage getThisStage() {
        return (Stage) loadingCircle.getScene().getWindow();
    }

    private void changeScene(ScenesNames name) {
        try {
            SceneChanger.getInstance().setName(name);
            SceneChanger.getInstance().start(getThisStage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    protected void transactServerResponse(ServerResponse response) {
        throw new RuntimeException("Must be overrided");
    }


    @FXML
    protected void homeScene() {
        changeScene(MAIN);
    }

    @FXML
    protected void serviceUser() {
        if (AuthenticateModel.getInstance().getIsAuthorized()){
            changeScene(SERVICE);
            return;
        }
        AlertDialog.alertOf (
                AlertDialog.CustomAlert.EXCEPTION,
                "Ошибка", "Вы не авторизованы"
        ).showAndWait();
    }

    @FXML
    protected void passwordReset() { changeScene(RESET_PASSWORD); }
    @FXML
    protected void registrationScene() { changeScene(CONNECTION); }
    @FXML
    protected void authorizationScene() { changeScene(AUTHORIZATION); }

    protected void clearErrorLabel() {
        setInfoTextLabelText("");
    }
    protected void setInfoTextLabelText(String text) {
        this.infoTextLabel.setText(text);
    }


    protected void checkServerResponseIs() {

        new Thread(() -> {
            while(true) {
                ServerResponse response = AuthenticateModel.getInstance().getResponse();
                if (response != null) {
                    try {
                        transactServerResponse(response);
                        if (loadingCircle != null) loadingCircle.setVisible(false);
                        AuthenticateModel.getInstance().setResponse(null);
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
        }).start();
    }
}
