package com.iot.controllers;

import com.iot.controllers.identities.AbstractAuthorizationController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import static com.iot.controllers.MainController.ButtonsStyle.OFF;
import static com.iot.controllers.MainController.ButtonsStyle.ON;

public class MainController extends AbstractAuthorizationController
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
    @FXML private Text infoTextLabel;

    @FXML
    protected void initialize() {
        super.setInfoTextLabel(infoTextLabel);
    }

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

