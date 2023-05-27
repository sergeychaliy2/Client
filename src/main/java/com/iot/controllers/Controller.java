package com.iot.controllers;

import com.iot.model.auth.AuthenticateModel;
import com.iot.model.constants.Endpoints;
import com.iot.model.utils.AlertDialog;
import com.iot.model.utils.HttpClient;
import com.iot.model.utils.ServerResponse;
import com.iot.scenes.SceneChanger;
import com.iot.scenes.ScenesNames;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.regex.Pattern;

import static com.iot.scenes.ScenesNames.*;

public abstract class Controller {
    @FXML
    protected Text infoTextLabel;
    @FXML
    protected ImageView loadingCircle;

    private static final Pattern patternLogin = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private static final Pattern patternPassword = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,49}$");
    private static final Pattern patternCode = Pattern.compile("\\d{6}$");

    enum ButtonsStyle
    {
        ON ("-fx-background-color:  #ffd596; -fx-border-color: gray; -fx-background-radius: 10; -fx-border-radius: 10;"),
        OFF("-fx-background-color:  #D9D9D9;  -fx-border-color: gray; -fx-background-radius: 10; -fx-border-radius: 10;");
        private final String style;

        ButtonsStyle (String style) {
            this.style = style;
        }

    }


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
    protected void personalDataScene() { changeScene(PERSONAL_DATA); }
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
                "Уведомление", "Вы не авторизованы"
        ).showAndWait();
    }

    protected void setButtonsReactionOnAction(List<Button> btns) {
        btns.forEach(btn -> {
            btn.setOnMouseMoved(event -> {
                btn.setStyle(ButtonsStyle.ON.style);
            });
            btn.setOnMouseExited(event -> {
                btn.setStyle(ButtonsStyle.OFF.style);
            });
        });
    }

    @FXML
    protected void passwordReset() { changeScene(RESET_PASSWORD); }
    @FXML
    protected void registrationScene() { changeScene(CONNECTION); }
    @FXML
    protected void authorizationScene() { changeScene(AUTHORIZATION); }

    protected void clearInfoLabel() {
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
    protected void checkIsTokenExpired(int attempts) {
        if (++attempts == 2) {
            AuthenticateModel.getInstance().setIsAuthorized(false);

            Platform.runLater(()-> {
                getThisStage().close();
                authorizationScene();
            });

            return;
        }
        HttpClient.execute(null, Endpoints.UPDATE_TOKEN, HttpClient.HttpMethods.GET);
        checkServerResponseIs();
    }

    protected static boolean isLoginValid(String login) {
        return patternLogin.matcher(login).matches();
    }

    protected static boolean isPasswordValid(String password) {
        return patternPassword.matcher(password).matches();
    }

    protected static boolean isCodeValid(String code) {
        return patternCode.matcher(code).matches();
    }
}
