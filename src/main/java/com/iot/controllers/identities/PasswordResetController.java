package com.iot.controllers.identities;
import com.iot.controllers.Controller;
import com.iot.model.constants.Endpoints;
import com.iot.model.constants.Responses;
import com.iot.model.utils.HttpClient;
import com.iot.model.utils.ServerResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;

import static com.iot.model.constants.Endpoints.SEND_CODE;

public class PasswordResetController extends Controller {
    @FXML private Button sendCodeBtn;
    @FXML private Button resetConfirmationBtn;
    @FXML private Button verifyCodeBtn;
    @FXML private Button homeBtn;
    @FXML private Button changeEmailAddressBtn;
    @FXML private Button registrationBtn;
    @FXML private TextField verifyCodeTField;
    @FXML private TextField emailTField;
    @FXML private TextField passwordTField;

    @FXML
    protected void initialize() {
        setButtonsReactionOnAction(List.of(homeBtn, verifyCodeBtn,
                registrationBtn, resetConfirmationBtn, sendCodeBtn, changeEmailAddressBtn));
    }

    @FXML
    protected void changeEmailAddressBtnClicked() {
        clearInfoLabel();

        emailTField.setDisable(false);
        sendCodeBtn.setDisable(false);

        verifyCodeTField.clear();
        verifyCodeBtn.setDisable(true);
        passwordTField.setDisable(true);
        resetConfirmationBtn.setDisable(true);
    }

    @Override
    protected void transactServerResponse(ServerResponse response) {
        try {
            JSONParser parser = new JSONParser();
            switch (response.responseCode()) {
                case HttpStatus.SC_OK -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    String responseMessage = resultObject.get("msg").toString();

                    if (responseMessage.equals(Responses.Authorization.VERIFICATION_CODE_SENT)) {
                        sendCodeBtn.setDisable(true);
                        emailTField.setDisable(true);
                        verifyCodeTField.setDisable(false);
                        verifyCodeBtn.setDisable(false);
                        changeEmailAddressBtn.setDisable(false);

                        setInfoTextLabelText(Responses.Authorization.RESET_CODE_WAS_SENT);

                    } else {
                        setInfoTextLabelText(Responses.Authorization.DATA_CHANGED);
                    }
                }
                case HttpStatus.SC_ACCEPTED -> {
                    verifyCodeTField.setDisable(true);
                    verifyCodeBtn.setDisable(true);

                    passwordTField.setDisable(false);
                    resetConfirmationBtn.setDisable(false);

                    setInfoTextLabelText(Responses.Authorization.RESET_CODE_IS_RIGHT);
                }
                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    String message = switch (resultObject.get("code").toString()) {
                        case "EE03" -> Responses.Authorization.NO_USER;
                        case "EA04" -> Responses.Authorization.CLIENT_IS_NOT_AUTHENTICATED;
                        default -> "Неверные данные пользователя";
                    };

                    setInfoTextLabelText(message);
                }
            }
        } catch (ParseException e) { throw new RuntimeException(e); }
    }
    @FXML
    protected void sendCode() {
        if (!isLoginValid(emailTField.getText())) {
            setInfoTextLabelText(Responses.Authorization.EMAIL_FORMAT_IS_NOT_VALID);
            return;
        }

        JSONObject obj = new JSONObject();
        obj.put("email", emailTField.getText());

        loadingCircle.setVisible(true);
        HttpClient.execute(obj, SEND_CODE, HttpClient.HttpMethods.POST);
        checkServerResponseIs();
    }
    @FXML
    protected void verifyCode() {
        clearInfoLabel();

        if (!isCodeValid(verifyCodeTField.getText())) {
            setInfoTextLabelText(Responses.Authorization.VERIFICATION_CODE_IS_NOT_VALID);
            return;
        }

        JSONObject obj = new JSONObject();
        obj.put("email", emailTField.getText());
        obj.put("code", verifyCodeTField.getText());
        loadingCircle.setVisible(true);
        HttpClient.execute(obj, Endpoints.CONFIRM_CODE, HttpClient.HttpMethods.POST);
        checkServerResponseIs();
    }
    @FXML protected void resetAccountData(){
        clearInfoLabel();

        if (!isPasswordValid(passwordTField.getText())) {
            setInfoTextLabelText(Responses.Authorization.PASSWORD_FORMAT_IS_INCORRECT);
            return;
        }

        JSONObject obj = new JSONObject();
        obj.put("email", emailTField.getText());
        obj.put("password", passwordTField.getText());
        loadingCircle.setVisible(true);
        HttpClient.execute(obj, Endpoints.RESET_PASSWORD, HttpClient.HttpMethods.PUT);
        checkServerResponseIs();
    }
}
