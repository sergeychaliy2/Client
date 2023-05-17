package com.iot.controllers.identities;

import com.iot.controllers.Controller;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.constants.Endpoints;
import com.iot.model.constants.Responses;
import com.iot.model.utils.HttpClient;
import com.iot.model.utils.ServerResponse;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.regex.Matcher;


public class AuthorizationController extends Controller {
    @FXML private TextField emailText;
    @FXML private TextField passwordText;

    @Override
    protected void transactServerResponse(ServerResponse response) {
        try {
            JSONParser parser = new JSONParser();
            switch (response.responseCode()) {
                case HttpStatus.SC_ACCEPTED ->  {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    AuthenticateModel.getInstance().setUserLogin(emailText.getText());

                        AuthenticateModel.getInstance().setAccessToken(
                                resultObject.get("accessToken").toString());
                    AuthenticateModel.getInstance().setRefreshToken(
                                resultObject.get("refreshToken").toString());
                    setInfoTextLabelText(Responses.Authorization.AUTHORIZATION_COMPLETE);
                }
                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());

                    String message = switch (resultObject.get("code").toString()) {
                        case "EE03"  -> Responses.Authorization.NO_USER;
                        case "EA05"  -> Responses.Authorization.PASSWORD_IS_NOT_CORRECT;
                        default      -> null;
                    };

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
            JSONObject obj = new JSONObject();
            obj.put("email", emailText.getText());
            obj.put("password", passwordText.getText());
            String endPoint = Endpoints.AUTHORIZATION;
            loadingCircle.setVisible(true);
            HttpClient.getInstance().post(obj, endPoint);
            checkServerResponseIs();
        } else {
            setInfoTextLabelText((Responses.Authorization.ERROR_AUTHORIZED));
        }
    }
}