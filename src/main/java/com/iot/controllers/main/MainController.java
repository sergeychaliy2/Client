package com.iot.controllers.main;

import com.iot.controllers.Controller;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.constants.Endpoints;
import com.iot.model.utils.HttpClient;
import com.iot.model.utils.ServerResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.List;


public class MainController extends Controller {
    @FXML private Button connectBtn;
    @FXML private Button authorizationBtn;
    @FXML private Button registrationBtn;
    @FXML private Button serviceBtn;

    @FXML
    protected void initialize() {
        if (AuthenticateModel.getInstance().isAppFirstOpen()) {
            HttpClient.execute(null, Endpoints.UPDATE_TOKEN, HttpClient.HttpMethods.GET);
            checkServerResponseIs();
            AuthenticateModel.getInstance().setAppFirstOpen(false);
        }

        setButtonsReactionOnAction(List.of(connectBtn, authorizationBtn, registrationBtn, serviceBtn));
    }

    @Override
    protected void transactServerResponse(ServerResponse response) {
        if (response.responseCode() == HttpStatus.SC_OK) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject res = (JSONObject) parser.parse(response.responseMsg());
                AuthenticateModel.getInstance().setAccessToken(res.get("accessToken").toString());
                AuthenticateModel.getInstance().setIsAuthorized(true);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    protected Stage getThisStage() {
        return (Stage) connectBtn.getScene().getWindow();
    }

}

