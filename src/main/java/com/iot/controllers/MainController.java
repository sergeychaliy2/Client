package com.iot.controllers;

import com.iot.model.AuthenticateModel;
import com.iot.model.responses.AuthorizationErrors;
import com.iot.scenes.SceneChanger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import static com.iot.controllers.MainController.ButtonsStyle.OFF;
import static com.iot.controllers.MainController.ButtonsStyle.ON;
import static com.iot.scenes.ScenesNames.*;

public class MainController
{
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

    @FXML private Button connectBtn;
    @FXML private Text mainInfo;

    private Stage getThisStage()
    {
        return (Stage) connectBtn.getScene().getWindow();
    }
    @FXML
    protected void connectionScene() throws Exception
    {
        new SceneChanger(CONNECTION).start(getThisStage());
    }
    @FXML
    protected void authorizationScene() throws Exception
    {
        new SceneChanger(AUTHORIZATION).start(getThisStage());
    }
    @FXML
    protected void connectBtnPushed() throws Exception
    {
        connectionScene();
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
    @FXML protected void serviceUser(){
        if(UserController.statusUser){
            mainInfo.setText(AuthorizationErrors.AUTHORIZED_SERVICE.toString());
        }else {
            mainInfo.setText(AuthorizationErrors.NOT_AUTHORIZED.toString());
        }
    }

}

