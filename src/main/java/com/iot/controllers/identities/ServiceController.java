package com.iot.controllers.identities;

import com.iot.controllers.UserController;
import com.iot.scenes.SceneChanger;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import static com.iot.scenes.ScenesNames.*;

public class ServiceController {
    AbstractAuthorizationController abstractAuthorizationController=new AbstractAuthorizationController();
    @FXML private ImageView loadingCircle;
    @FXML protected void changeStatusHoll(){}
    @FXML protected void openDeviceMode(){}
    @FXML protected void closeDeviceMode(){}
    @FXML protected void autoDeviceMode(){}

    private Stage getThisStage()
    {
        return (Stage) loadingCircle.getScene().getWindow();
    }
    @FXML
    protected void homeScene() {
        try {
            UserController.statusUser=false;
            new SceneChanger(MAIN).start(getThisStage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML protected void authorizationScene(){
        try {
            UserController.statusUser=false;
            new SceneChanger(AUTHORIZATION).start(getThisStage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
