package com.iot.controllers.identities;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.constants.Endpoints;
import com.iot.model.constants.Responses;
import com.iot.model.utils.HttpClient;
import com.iot.model.utils.ServerResponse;
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

public class PasswordResetController extends AbstractAuthorizationController {
    @FXML private Button sendCodeResetPasswordBtn;
    @FXML private Button codeVerifyBtn;
    @FXML private Button passwordResetConfirmation;
    @FXML private TextField codeResetPassword;
    @FXML private TextField emailResetPassword;
    @FXML private ImageView loadingCircle;
    @FXML private Text infoTextLabel;
    @FXML private TextField newPasswordTextField;

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
                case HttpStatus.SC_OK -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    String responseMessage = resultObject.get("msg").toString();

                    if (responseMessage.equals(Responses.Authorization.VERIFICATION_CODE_SENT)) {
                        emailResetPassword.setDisable(true);
                        emailResetPassword.setOpacity(0.5);
                        setInfoTextLabelText(Responses.Authorization.RESET_CODE_WAS_SENT);
                        passwordResetConfirmation.setVisible(true);
                        passwordResetConfirmation.setOpacity(1.0);
                        passwordResetConfirmation.setDisable(false);//активна
                    } else {
                        setInfoTextLabelText(Responses.Authorization.DATA_CHANGED);
                        AuthenticateModel.getInstance().setAuthorized(true);
                    }
                }
                case HttpStatus.SC_ACCEPTED -> {
                    codeResetPassword.setDisable(true);
                    codeResetPassword.setOpacity(0.5);
                    setInfoTextLabelText(Responses.Authorization.RESET_CODE_IS_RIGHT);
                    newPasswordTextField.setVisible(true);
                    newPasswordTextField.setOpacity(1.0);
                    newPasswordTextField.setEditable(true);
                    passwordResetConfirmation.setDisable(true);
                    passwordResetConfirmation.setOpacity(0.5);
                }
                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    String message = switch (resultObject.get("code").toString()) {
                        case "EE03" -> Responses.Authorization.NO_USER;
                        case "EA04" -> Responses.Authorization.CLIENT_IS_NOT_AUTHENTICATED;
                        default -> "Неверные данные пользователя";
                    };

//                    if (message == null && resultObject.get("code").equals("ET01")) {
//                        AuthorizationModel.getInstance().updateToken();
//                    }

                    setInfoTextLabelText(message);
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void sendCodeToEmail() {
        Matcher matcherEmail = patternLogin.matcher(emailResetPassword.getText());
        try {
            if ((matcherEmail.matches())) {
                sendCodeResetPasswordBtn.setVisible(false);
                sendCodeResetPasswordBtn.setDisable(false);
                codeResetPassword.setOpacity(1.0);
                codeVerifyBtn.setOpacity(1.0);
                codeResetPassword.setEditable(true);
                codeVerifyBtn.setDisable(false);

                JSONObject obj = new JSONObject();
                obj.put("email", emailResetPassword.getText());

                String endPoint = Endpoints.SEND_CODE;
//                AuthorizationModel.getInstance().setRequest(
//                        new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//                );

                loadingCircle.setVisible(true);
                HttpClient.getInstance().post(obj, endPoint);
                checkServerResponseIs();
            } else {
                setInfoTextLabelText(Responses.Authorization.EMAIL_MESSAGE_FAILED);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void codeVerify() {
        try {
            clearErrorLabel();
            Matcher matcherLogin = patternLogin.matcher(emailResetPassword.getText());
            if ((matcherLogin.matches()) && (!codeResetPassword.getText().equals(""))) {
                JSONObject obj = new JSONObject();
                obj.put("email", emailResetPassword.getText());
                obj.put("code", codeResetPassword.getText());
                String endPoint = Endpoints.CONFIRM_CODE;
//                AuthorizationModel.getInstance().setRequest(
//                        new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//                );

                loadingCircle.setVisible(true);
                HttpClient.getInstance().post(obj, endPoint);
                checkServerResponseIs();
            } else {
                setInfoTextLabelText(Responses.Authorization.VERIFICATION_CODE_IS_NOT_VALID);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML protected void resetAccountData(){
        try {
            clearErrorLabel();
            Matcher matcherLogin = patternLogin.matcher(emailResetPassword.getText());
            Matcher matcherPassword = patternPassword.matcher(newPasswordTextField.getText());
            if ((matcherLogin.matches())&&(matcherPassword.matches())){
                JSONObject obj = new JSONObject();
                obj.put("email", emailResetPassword.getText());
                obj.put("password", newPasswordTextField.getText());
                String endPoint = Endpoints.RESET_PASSWORD;
//                AuthorizationModel.getInstance().setRequest(
//                        new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//                );
                loadingCircle.setVisible(true);
                HttpClient.getInstance().post(obj, endPoint);
                checkServerResponseIs();
            } else {
                setInfoTextLabelText(Responses.Authorization.PASSWORD_FORMAT_IS_INCORRECT);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
