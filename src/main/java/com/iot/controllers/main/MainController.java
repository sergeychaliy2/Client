package com.iot.controllers.main;

import com.iot.controllers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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

