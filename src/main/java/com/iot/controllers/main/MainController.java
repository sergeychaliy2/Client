package com.iot.controllers.main;

import com.iot.controllers.Controller;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.utils.HttpClient;
import com.iot.model.utils.ServerResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.iot.controllers.main.MainController.ButtonsStyle.OFF;
import static com.iot.controllers.main.MainController.ButtonsStyle.ON;

public class MainController extends Controller {
    enum ButtonsStyle
    {
        ON
                {
                    @Override
                    public String toString()
                    {
                        return "-fx-background-color:  #B2AFAF; -fx-text-fill: #000000;";
                    }
                },
        OFF
                {
                    @Override
                    public String toString()
                    {
                        return "-fx-background-color:  #D9D9D9; -fx-text-fill: #000000;";
                    }
                }
    }

    @FXML
    protected void initialize() {
        HttpClient.getInstance().getWithRefresh();
        checkServerResponseIs();
    }

    @Override
    protected void transactServerResponse(ServerResponse response) {
        if (response.responseCode() == HttpStatus.SC_OK) {
            try {
                System.out.println("1");
                JSONParser parser = new JSONParser();
                JSONObject res = (JSONObject) parser.parse(response.responseMsg());
                AuthenticateModel.getInstance().setAccessToken(res.get("accessToken").toString());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML private Button connectBtn;

    @Override
    protected Stage getThisStage() {
        return (Stage) connectBtn.getScene().getWindow();
    }

    @FXML
    protected void movedConnectBtn()
    {
        connectBtn.setStyle(ON.toString());
    }

    @FXML
    protected void exitedConnectBtn()
    {
        connectBtn.setStyle(OFF.toString());
    }

}

