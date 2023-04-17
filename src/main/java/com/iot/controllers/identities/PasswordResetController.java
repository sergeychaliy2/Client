package com.iot.controllers.identities;
import com.iot.controllers.UserController;
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
                    setInfoTextLabelText("Пароль был сброшен");
                    UserController.statusUser =false;
                }

                case HttpStatus.SC_ACCEPTED -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    String responseMessage = resultObject.get("msg").toString();
                    if (responseMessage.equals(AuthorizationSuccessResponses.VERIFICATION_CODE_SENT.toString())) {
                        emailResetPassword.setDisable(true);
                        emailResetPassword.setOpacity(0.5);
                        setInfoTextLabelText(AuthorizationSuccessResponses.RESET_CODE_WAS_SENT.toString());
                        passwordResetConfirmation.setVisible(true);
                        passwordResetConfirmation.setOpacity(1.0);
                        passwordResetConfirmation.setDisable(false);//активна
                    } else if (responseMessage.equals(AuthorizationSuccessResponses.DATA_CHANGED.toString())) {
                        setInfoTextLabelText(AuthorizationSuccessResponses.PASSWORD_RESET.toString());

                    } else {
                        codeResetPassword.setDisable(true);
                        codeResetPassword.setOpacity(0.5);
                        setInfoTextLabelText(AuthorizationSuccessResponses.RESET_CODE_IS_RIGHT.toString());
                        newPasswordTextField.setVisible(true);
                        newPasswordTextField.setOpacity(1.0);
                        newPasswordTextField.setEditable(true);
                        passwordResetConfirmation.setDisable(true);
                        passwordResetConfirmation.setOpacity(0.5);
                    }
                }
                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    String message = switch (resultObject.get("code").toString()) {
                        case "EE03" -> AuthorizationErrors.NO_USER.toString();
                        case "EA04" -> AuthorizationErrors.CLIENT_IS_NOT_AUTHENTICATED.toString();
                        default -> "Неверные данные пользователя";
                    };

//                    if (message == null && resultObject.get("code").equals("ET01")) {
//                        AuthorizationModel.getInstance().updateToken();
//                    }

                    setInfoTextLabelText(message);
                }
            }
        } catch (ParseException e) {setInfoTextLabelText(e.getMessage());}
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

                String endPoint = Endpoints.SEND_CODE.toString();
//                AuthorizationModel.getInstance().setRequest(
//                        new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//                );

                loadingCircle.setVisible(true);
                HttpClient.getInstance().post(obj, endPoint);
                checkServerResponseIs();
            }else {
                setInfoTextLabelText(AuthorizationErrors.EMAIL_MESSAGE_FAILED.toString());
            }
        }catch (Exception ex){
            ex.printStackTrace();
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
                String endPoint = Endpoints.CONFIRM_CODE.toString();
//                AuthorizationModel.getInstance().setRequest(
//                        new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//                );

                loadingCircle.setVisible(true);
                HttpClient.getInstance().post(obj, endPoint);
                checkServerResponseIs();
            } else {
                setInfoTextLabelText(AuthorizationErrors.VERIFICATION_CODE_IS_NOT_VALID.toString());
            }
        }catch (Exception ex){
            ex.printStackTrace();
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
                String endPoint = Endpoints.RESET_PASSWORD.toString();
//                AuthorizationModel.getInstance().setRequest(
//                        new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//                );
                loadingCircle.setVisible(true);
                HttpClient.getInstance().post(obj, endPoint);
                checkServerResponseIs();
            } else {
                setInfoTextLabelText(AuthorizationErrors.PASSWORD_FORMAT_IS_INCORRECT.toString());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
