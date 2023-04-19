package com.iot.controllers.identities;

import com.iot.controllers.UserController;
import com.iot.model.*;
import com.iot.model.responses.AuthorizationErrors;
import com.iot.model.responses.AuthorizationSuccessResponses;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.regex.Matcher;


public class AuthorizationController extends AbstractAuthorizationController {
    @FXML private TextField emailText;
    @FXML private TextField passwordText;
    @FXML private Text infoTextLabel;
    @FXML private ImageView loadingCircle;

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
                        AuthenticateModel.getInstance().setAccessToken(
                                resultObject.get("accessToken").toString());
                    AuthenticateModel.getInstance().setRefreshToken(
                                resultObject.get("refreshToken").toString());
                    setInfoTextLabelText(AuthorizationSuccessResponses.AUTHORIZATION_COMPLETE.toString());
                    AuthenticateModel.getInstance().setAuthorized(true);
                }
                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());

                    String message = switch (resultObject.get("code").toString()) {
                        case "EE03"  -> AuthorizationErrors.USER_ALREADY_EXISTS.toString();
                        case "EA05"  -> AuthorizationErrors.PASSWORD_IS_NOT_CORRECT.toString();
                        default      -> null;
                    };

//                    if (message == null && resultObject.get("code").equals("ET01")) {
//                        AuthorizationModel.getInstance().updateToken(this);
//                        return;
//                    }
                    setInfoTextLabelText((message));
                }
            }
        } catch (ParseException e) {setInfoTextLabelText((e.getMessage()));}
    }
    @FXML
    protected void userAuthorization()
    {
        clearErrorLabel();
        Matcher matcherPassword = patternPassword.matcher(passwordText.getText());
        Matcher matcherLogin = patternLogin.matcher(emailText.getText());
        if ((matcherPassword.matches()) && (matcherLogin.matches())) {
            UserProfileModel.getInstance().setUserInstance(emailText.getText());
            JSONObject obj = new JSONObject();
            obj.put("email", emailText.getText());
            obj.put("password", passwordText.getText());
            String endPoint = Endpoints.AUTHORIZATION.toString();
//                AuthorizationModel.getInstance().setRequest(
//                        new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//                );
            loadingCircle.setVisible(true);
            HttpClient.getInstance().post(obj, endPoint);
            checkServerResponseIs();
        } else {
            setInfoTextLabelText((AuthorizationErrors.ERROR_AUTHORIZED.toString()));
        }
    }
}