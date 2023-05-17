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

import java.util.regex.Matcher;

public class RegistrationController extends Controller {
    @FXML private Button verifyCodeActionBtn;
    @FXML private Button changeEmailBtn;
    @FXML private TextField verifyCodeTField;
    @FXML private TextField userPasswordTField;
    @FXML private TextField userEmailTField;
    @FXML private TextField usersPasswordViewTField;


    @Override
    protected void transactServerResponse(ServerResponse response) {
        try {
            JSONParser parser = new JSONParser();

            switch (response.responseCode()) {
                case HttpStatus.SC_OK ->  {
                    userEmailTField.setDisable(true);
                    changeEmailBtn.setVisible(true);
                    changeEmailBtn.setDisable(false);
                    verifyCodeTField.setEditable(true);
                    setInfoTextLabelText(Responses.Authorization.VERIFICATION_CODE_WAS_SENT);
                }
                case HttpStatus.SC_ACCEPTED ->  {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());

                    if (resultObject.get("msg") == null) {
                        AuthenticateModel.getInstance().setAccessToken(
                                resultObject.get("accessToken").toString()
                        );
                        AuthenticateModel.getInstance().setRefreshToken(
                                resultObject.get("refreshToken").toString()
                        );
                        setInfoTextLabelText(Responses.Authorization.SUCCESSFULLY_REGISTRATION);
                        Platform.runLater(this::serviceUser);
                    }
                    else {
                        verifyCodeTField.setDisable(true);
                        verifyCodeActionBtn.setDisable(true);
                        userPasswordTField.setDisable(false);
                        userPasswordTField.setEditable(true);
                        setInfoTextLabelText(Responses.Authorization.VERIFICATION_CODE_IS_RIGHT);
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

                    setInfoTextLabelText(message);
                }
            }
        } catch (ParseException e) { setInfoTextLabelText(e.getMessage()); }

    }

    @FXML
    protected void changeEmailBtnClicked() {
        userEmailTField.setDisable(false);
        changeEmailBtn.setVisible(false);
        changeEmailBtn.setDisable(true);
        userPasswordTField.setDisable(true);
        verifyCodeTField.setDisable(false);
        verifyCodeActionBtn.setDisable(false);


        verifyCodeTField.clear();
        changeStateOfCodeSendBtn(true);
    }

    @FXML
    protected void sendActionByVerifyCodeBtn() {
        clearErrorLabel();

        switch (verifyCodeActionBtn.getText()) {
            case "Отправить" -> {
                Matcher matcherLogin = patternLogin.matcher(userEmailTField.getText());
                if (matcherLogin.matches()) {
                    JSONObject obj = new JSONObject();
                    obj.put("email", userEmailTField.getText());
                    String endPoint = Endpoints.SEND_CODE;

                    loadingCircle.setVisible(true);
                    HttpClient.getInstance().post(obj, endPoint);
                    checkServerResponseIs();
                } else {
                    setInfoTextLabelText(Responses.Authorization.EMAIL_FORMAT_IS_NOT_VALID);
                }
            }
            case "Подтвердить" -> {
                Matcher matcherCode = patternCode.matcher(verifyCodeTField.getText());
                if (matcherCode.matches()) {
                    JSONObject obj = new JSONObject();
                    obj.put("email", userEmailTField.getText());
                    obj.put("code", Integer.parseInt(verifyCodeTField.getText()));
                    String endPoint = Endpoints.CONFIRM_CODE;
                    loadingCircle.setVisible(true);
                    HttpClient.getInstance().post(obj, endPoint);
                    checkServerResponseIs();
                } else {
                    setInfoTextLabelText(Responses.Authorization.ERROR_VERIFICATION_CODE_LENGTH_OR_FORMAT_INCORRECT);
                }
            }
        }


    }

    @FXML
    protected void register() {
        clearErrorLabel();

        Matcher matcherPassword = patternPassword.matcher(userPasswordTField.getText());
        Matcher matcherLogin = patternLogin.matcher(userEmailTField.getText());

        if (matcherLogin.matches() && matcherPassword.matches()) {
            JSONObject obj = new JSONObject();
            obj.put("email", userEmailTField.getText());
            obj.put("password", userPasswordTField.getText());
            String endPoint = Endpoints.REGISTRATION;
            HttpClient.getInstance().post(obj, endPoint);
            checkServerResponseIs();
        } else {
            setInfoTextLabelText(Responses.Authorization.FORM_IS_NOT_FILLED_OR_HAS_INCORRECT_DATA);
        }
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
    private void changeStateOfCodeSendBtn(Boolean isSend) {
        if (isSend) verifyCodeActionBtn.setText("Отправить");
        else        verifyCodeActionBtn.setText("Подтвердить");
    }
    @FXML
    protected void checkVerifyCodeTFieldPressed() {
        changeStateOfCodeSendBtn (
                verifyCodeTField.getText().equals("")
        );
    }
}