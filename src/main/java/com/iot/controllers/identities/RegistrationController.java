package com.iot.controllers.identities;
import com.iot.model.*;
import com.iot.model.responses.AuthorizationErrors;
import com.iot.model.responses.AuthorizationSuccessResponses;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.regex.Matcher;

public class RegistrationController extends AbstractAuthorizationController{
    @FXML private Button verifyCodeActionBtn;
    @FXML private Button changeEmailBtn;
    @FXML private TextField verifyCodeTField;
    @FXML private TextField userPasswordTField;
    @FXML private TextField userEmailTField;
    @FXML private Text infoTextLabel;
    @FXML private ImageView loadingCircle;
    @FXML private TextField usersPasswordViewTField;

    @FXML
    protected void initialize() {
        setLoadingCircle(this.loadingCircle);
        setInfoTextLabel(this.infoTextLabel);
    }

    @Override
    protected void transactServerResponse(ServerResponse response) {
        try {
            JSONParser parser = new JSONParser();

            switch (response.responseCode()) {
                case HttpStatus.SC_ACCEPTED ->  {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    if (resultObject.get("msg") == null) {
                        AuthenticateModel.getInstance().setAccessToken(
                                resultObject.get("accessToken").toString()
                        );
                        AuthenticateModel.getInstance().setRefreshToken(
                                resultObject.get("refreshToken").toString()
                        );
                        infoTextLabel.setText(AuthorizationSuccessResponses.SUCCESSFULLY_REGISTRATION.toString());
//                        Platform.runLater(this::homeScene);
                    } else {
                        String responseMessage = resultObject.get("msg").toString();

                        if (responseMessage.equals(AuthorizationSuccessResponses.VERIFICATION_CODE_SENT.toString())) {
                            userEmailTField.setDisable(true);
                            changeEmailBtn.setVisible(true);
                            changeEmailBtn.setDisable(false);
                            verifyCodeTField.setEditable(true);
                            infoTextLabel.setText(AuthorizationSuccessResponses.VERIFICATION_CODE_WAS_SENT.toString());
                        }
                        else {
                            verifyCodeTField.setDisable(true);
                            verifyCodeActionBtn.setDisable(true);
                            userPasswordTField.setDisable(false);
                            infoTextLabel.setText(AuthorizationSuccessResponses.VERIFICATION_CODE_IS_RIGHT.toString());
                        }
                    }

                }
                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());

                    String message = switch (resultObject.get("code").toString()) {
                        case "EMD01" -> AuthorizationErrors.EMAIL_MESSAGE_ALREADY_SENT.toString();
                        case "EMD02" -> AuthorizationErrors.EMAIL_MESSAGE_FAILED.toString();
                        case "EA01"  -> AuthorizationErrors.VERIFICATION_CODE_IS_NOT_VALID.toString();
                        case "EA02"  -> AuthorizationErrors.CONNECTION_TIME_WAS_EXPIRED.toString();
                        case "EA03"  -> AuthorizationErrors.USER_ALREADY_EXISTS.toString();
                        case "EA04"  -> AuthorizationErrors.CLIENT_IS_NOT_AUTHENTICATED.toString();
                        default      -> null;
                    };

//                    if (message == null && resultObject.get("code").toString().equals("ET01")) {
//                        AuthorizationModel.getInstance().updateToken();
//                    }

                    infoTextLabel.setText(message);
                }
            }
        } catch (ParseException e) { infoTextLabel.setText(e.getMessage()); }

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
                    String endPoint = Endpoints.SEND_CODE.toString();
//                    AuthorizationModel.getInstance().setRequest(
//                            new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//                    );
                    loadingCircle.setVisible(true);
                    HttpClient.getInstance().post(obj, endPoint);
                    checkServerResponseIs();
                } else {
                    infoTextLabel.setText(AuthorizationErrors.EMAIL_FORMAT_IS_NOT_VALID.toString());
                }
            }
            case "Подтвердить" -> {
                Matcher matcherCode = patternCode.matcher(verifyCodeTField.getText());
                if (matcherCode.matches()) {
                    userPasswordTField.setEditable(true);
                    JSONObject obj = new JSONObject();
                    obj.put("email", userEmailTField.getText());
                    obj.put("code", Integer.parseInt(verifyCodeTField.getText()));
                    String endPoint = Endpoints.CONFIRM_CODE.toString();
//                    AuthorizationModel.getInstance().setRequest(
//                            new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//                    );
                    loadingCircle.setVisible(true);
                    HttpClient.getInstance().post(obj, endPoint);
                    checkServerResponseIs();
                } else {
                    infoTextLabel.setText(AuthorizationErrors.ERROR_VERIFICATION_CODE_LENGTH_OR_FORMAT_INCORRECT.toString());
                }
            }
        }


    }

    @FXML
    protected void register() {
        clearErrorLabel();

        Matcher matcherPassword = patternPassword.matcher(userPasswordTField.getText());
        Matcher matcherLogin = patternLogin.matcher(userEmailTField.getText());

        if ((matcherPassword.matches()) && (matcherLogin.matches())) {
            JSONObject obj = new JSONObject();
            obj.put("email", userEmailTField.getText());
            obj.put("password", userPasswordTField.getText());
            String endPoint = Endpoints.REGISTRATION.toString();
//            AuthorizationModel.getInstance().setRequest(
//                    new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//            );
            HttpClient.getInstance().post(obj, endPoint);
            checkServerResponseIs();
        } else {
            infoTextLabel.setText(AuthorizationErrors.PASSWORD_FORMAT_IS_INCORRECT.toString());
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