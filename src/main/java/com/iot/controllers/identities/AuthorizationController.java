package com.iot.controllers.identities;

import com.iot.controllers.Controller;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.constants.Endpoints;
import com.iot.model.constants.Responses;
import com.iot.model.utils.HttpClient;
import com.iot.model.utils.ServerResponse;
import javafx.application.Platform;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.regex.Matcher;


public class AuthorizationController extends Controller {
    @FXML private TextField userEmailTField;
    @FXML private TextField userPasswordTField;

    @FXML
    protected void initialize() {
        AuthenticateModel model = AuthenticateModel.getInstance();
        this.userEmailTField.setText(model.getUserLogin());
        this.userPasswordTField.setText(model.getUserPassword());
    }

    @Override
    protected void transactServerResponse(ServerResponse response) {
        try {
            JSONParser parser = new JSONParser();
            switch (response.responseCode()) {
                case HttpStatus.SC_ACCEPTED ->  {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    AuthenticateModel.getInstance().updateFileData(
                            userEmailTField.getText(),
                            userPasswordTField.getText(),
                            resultObject.get("refreshToken").toString(),
                            resultObject.get("accessToken").toString()
                    );
                    Platform.runLater(this::serviceUser);
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
        Matcher matcherPassword = patternPassword.matcher(userPasswordTField.getText());
        Matcher matcherLogin = patternLogin.matcher(userEmailTField.getText());
        if ((matcherPassword.matches()) && (matcherLogin.matches())) {
            JSONObject obj = new JSONObject();
            obj.put("email", userEmailTField.getText());
            obj.put("password", userPasswordTField.getText());
            loadingCircle.setVisible(true);
            HttpClient.execute(obj, Endpoints.AUTHORIZATION, HttpClient.HttpMethods.POST);
            checkServerResponseIs();
        } else {
            setInfoTextLabelText((Responses.Authorization.ERROR_AUTHORIZED));
        }
    }
}