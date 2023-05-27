package com.iot.controllers.identities;
import com.iot.controllers.Controller;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.constants.Endpoints;
import com.iot.model.constants.Responses;
import com.iot.model.utils.HttpClient;
import com.iot.model.utils.ServerResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;

public class RegistrationController extends Controller {
    @FXML private Button verifyCodeActionBtn;
    @FXML private Button changeEmailBtn;
    @FXML private TextField verifyCodeTField;
    @FXML private TextField userPasswordTField;
    @FXML private TextField userEmailTField;
    @FXML private TextField usersPasswordViewTField;
    @FXML private Button homeBtn;
    @FXML private Button authorizationBtn;
    @FXML private Button serviceBtn;
    @FXML private Button registerBtn;

    @FXML
    protected void initialize() {
        setButtonsReactionOnAction(List.of(homeBtn, authorizationBtn, serviceBtn, verifyCodeActionBtn, registerBtn, changeEmailBtn));
    }

    @Override
    protected void transactServerResponse(ServerResponse response) {
        try {
            JSONParser parser = new JSONParser();

            switch (response.responseCode()) {
                case HttpStatus.SC_OK ->  {

                    Platform.runLater(() -> {
                        userEmailTField.setDisable(true);
                        changeEmailBtn.setDisable(false);
                        verifyCodeTField.setDisable(false);

                        setInfoTextLabelText(Responses.Authorization.VERIFICATION_CODE_WAS_SENT);
                    });

                }
                case HttpStatus.SC_ACCEPTED ->  {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());

                    if (resultObject.get("msg") == null) {

                        AuthenticateModel.getInstance().updateFileData(
                                userEmailTField.getText(),
                                userPasswordTField.getText(),
                                resultObject.get("refreshToken").toString(),
                                resultObject.get("accessToken").toString()
                        );
                        Platform.runLater(this::serviceUser);
                    }
                    else {
                        Platform.runLater(() -> {
                            verifyCodeTField.setDisable(true);
                            verifyCodeActionBtn.setDisable(true);
                            userPasswordTField.setDisable(false);
                            registerBtn.setDisable(false);
                            setInfoTextLabelText(Responses.Authorization.VERIFICATION_CODE_IS_RIGHT);
                        });
                    }
                }
                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());

                    String message = switch (resultObject.get("code").toString()) {
                        case "EMD01" -> Responses.Authorization.EMAIL_MESSAGE_ALREADY_SENT;
                        case "EMD02" -> Responses.Authorization.EMAIL_MESSAGE_FAILED;
                        case "EA01"  -> Responses.Authorization.VERIFICATION_CODE_IS_NOT_VALID;
                        case "EA02"  -> Responses.Authorization.CONNECTION_TIME_WAS_EXPIRED;
                        case "EA03"  -> Responses.Authorization.USER_ALREADY_EXISTS;
                        case "EA04"  -> Responses.Authorization.CLIENT_IS_NOT_AUTHENTICATED;
                        default      -> null;
                    };

                    Platform.runLater(() -> setInfoTextLabelText(message));
                }
            }
        } catch (ParseException e) { throw new RuntimeException(e.getMessage()); }

    }

    @FXML
    protected void changeEmailBtnClicked() {
        userEmailTField.setDisable(false);
        verifyCodeActionBtn.setDisable(false);

        userPasswordTField.setDisable(true);
        verifyCodeTField.setDisable(true);
        registerBtn.setDisable(true);

        verifyCodeTField.clear();
        changeStateOfCodeSendBtn(true);
    }

    @FXML
    protected void actionSendOrVerifyBtnClicked() {
        clearInfoLabel();

        switch (verifyCodeActionBtn.getText()) {
            case "Отправить" -> {
                if (!isLoginValid(userEmailTField.getText())) {
                    setInfoTextLabelText(Responses.Authorization.EMAIL_FORMAT_IS_NOT_VALID);
                    return;
                }

                JSONObject obj = new JSONObject();
                obj.put("email", userEmailTField.getText());
                loadingCircle.setVisible(true);
                HttpClient.execute(obj, Endpoints.SEND_CODE, HttpClient.HttpMethods.POST);
                checkServerResponseIs();
            }
            case "Подтвердить" -> {
                if (!isCodeValid(verifyCodeTField.getText())) {
                    setInfoTextLabelText(Responses.Authorization.ERROR_VERIFICATION_CODE_LENGTH_OR_FORMAT_INCORRECT);
                    return;
                }

                JSONObject obj = new JSONObject();
                obj.put("email", userEmailTField.getText());
                obj.put("code", Integer.parseInt(verifyCodeTField.getText()));
                loadingCircle.setVisible(true);
                HttpClient.execute(obj, Endpoints.CONFIRM_CODE, HttpClient.HttpMethods.POST);
                checkServerResponseIs();
            }
        }
    }

    @FXML
    protected void registerBtnClicked() {
        clearInfoLabel();

        if (!isPasswordValid(userPasswordTField.getText())) {
            setInfoTextLabelText(Responses.Authorization.PASSWORD_FORMAT_IS_INCORRECT);
            return;
        }

        JSONObject obj = new JSONObject();
        obj.put("email", userEmailTField.getText());
        obj.put("password", userPasswordTField.getText());
        HttpClient.execute(obj, Endpoints.REGISTRATION, HttpClient.HttpMethods.POST);
        checkServerResponseIs();
    }
    @FXML protected void showPassword() {
        usersPasswordViewTField.setOpacity(1);
        usersPasswordViewTField.setDisable(false);
        usersPasswordViewTField.setText(userPasswordTField.getText());
    }

    @FXML protected void hidePassword() {
        usersPasswordViewTField.setOpacity(0);
        usersPasswordViewTField.setDisable(true);
    }


    /**
     * state == true, когда кнопка включена, соответственно state == false - выключена
     */
    private void changeStateOfCodeSendBtn(boolean isSend) {
        if (isSend) verifyCodeActionBtn.setText("Отправить");
        else        verifyCodeActionBtn.setText("Подтвердить");
    }
    @FXML
    protected void checkVerifyCodeTFieldHasAnyKeys() {
        changeStateOfCodeSendBtn (
                verifyCodeTField.getText().equals("")
        );
    }
}