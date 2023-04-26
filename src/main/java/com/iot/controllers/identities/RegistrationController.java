package com.iot.controllers.identities;
import com.iot.model.*;
import com.iot.model.consts.CommonErrors;
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
                        setInfoTextLabelText(CommonErrors.AuthorizationSuccessResponses.SUCCESSFULLY_REGISTRATION);
//                        Platform.runLater(this::homeScene);
                    } else {
                        String responseMessage = resultObject.get("msg").toString();

                        if (responseMessage.equals(CommonErrors.AuthorizationSuccessResponses.VERIFICATION_CODE_SENT)) {
                            userEmailTField.setDisable(true);
                            changeEmailBtn.setVisible(true);
                            changeEmailBtn.setDisable(false);
                            verifyCodeTField.setEditable(true);
                            setInfoTextLabelText(CommonErrors.AuthorizationSuccessResponses.VERIFICATION_CODE_WAS_SENT);
                        }
                        else {
                            verifyCodeTField.setDisable(true);
                            verifyCodeActionBtn.setDisable(true);
                            userPasswordTField.setDisable(false);
                            userPasswordTField.setEditable(true);
                            setInfoTextLabelText(CommonErrors.AuthorizationSuccessResponses.VERIFICATION_CODE_IS_RIGHT);
                        }
                    }

                }
                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());

                    String message = switch (resultObject.get("code").toString()) {
                        case "EMD01" -> CommonErrors.Authorization.EMAIL_MESSAGE_ALREADY_SENT;
                        case "EMD02" -> CommonErrors.Authorization.EMAIL_MESSAGE_FAILED;
                        case "EA01"  -> CommonErrors.Authorization.VERIFICATION_CODE_IS_NOT_VALID;
                        case "EA02"  -> CommonErrors.Authorization.CONNECTION_TIME_WAS_EXPIRED;
                        case "EA03"  -> CommonErrors.Authorization.USER_ALREADY_EXISTS;
                        case "EA04"  -> CommonErrors.Authorization.CLIENT_IS_NOT_AUTHENTICATED;
                        default      -> null;
                    };

//                    if (message == null && resultObject.get("code").toString().equals("ET01")) {
//                        AuthorizationModel.getInstance().updateToken();
//                    }

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
                    String endPoint = CommonErrors.Endpoints.SEND_CODE;
//                    AuthorizationModel.getInstance().setRequest(
//                            new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//                    );
                    loadingCircle.setVisible(true);
                    HttpClient.getInstance().post(obj, endPoint);
                    checkServerResponseIs();
                } else {
                    setInfoTextLabelText(CommonErrors.Authorization.EMAIL_FORMAT_IS_NOT_VALID);
                }
            }
            case "Подтвердить" -> {
                Matcher matcherCode = patternCode.matcher(verifyCodeTField.getText());
                if (matcherCode.matches()) {
                    JSONObject obj = new JSONObject();
                    obj.put("email", userEmailTField.getText());
                    obj.put("code", Integer.parseInt(verifyCodeTField.getText()));
                    String endPoint = CommonErrors.Endpoints.CONFIRM_CODE;
//                    AuthorizationModel.getInstance().setRequest(
//                            new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//                    );
                    loadingCircle.setVisible(true);
                    HttpClient.getInstance().post(obj, endPoint);
                    checkServerResponseIs();
                } else {
                    setInfoTextLabelText(CommonErrors.Authorization.ERROR_VERIFICATION_CODE_LENGTH_OR_FORMAT_INCORRECT);
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
            String endPoint = CommonErrors.Endpoints.REGISTRATION;
//            AuthorizationModel.getInstance().setRequest(
//                    new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//            );
            HttpClient.getInstance().post(obj, endPoint);
            checkServerResponseIs();
        } else {
            setInfoTextLabelText(CommonErrors.Authorization.FORM_IS_NOT_FILLED_OR_HAS_INCORRECT_DATA);
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