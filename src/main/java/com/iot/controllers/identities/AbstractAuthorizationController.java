package com.iot.controllers.identities;

import com.iot.controllers.UserController;
import com.iot.model.AuthenticateModel;
import com.iot.model.ServerResponse;
import com.iot.model.responses.AuthorizationErrors;
import com.iot.scenes.SceneChanger;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.regex.Pattern;

import static com.iot.scenes.ScenesNames.*;

public class AbstractAuthorizationController {
    private ImageView loadingCircle;
    private Text infoTextLabel;

    protected final Pattern patternLogin = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    protected final Pattern patternPassword = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,49}$");
    protected final Pattern patternCode = Pattern.compile("\\d{6}$");

    protected void transactServerResponse(ServerResponse response) {
        throw new RuntimeException("Must be override");
    }

    protected void checkServerResponseIs() {
        new Thread(() -> {
            while(true) {
                ServerResponse response = AuthenticateModel.getInstance().getResponse();
                if (response != null) {
                    try {
                        transactServerResponse(response);
                        loadingCircle.setVisible(false);
                        AuthenticateModel.getInstance().setResponse(null);
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
        }).start();
    }


    public void setLoadingCircle(ImageView loadingCircle) {
        this.loadingCircle = loadingCircle;
    }
    private Stage getThisStage()
    {
        return (Stage) loadingCircle.getScene().getWindow();
    }
    @FXML
    protected void homeScene() {
        try {
            new SceneChanger(MAIN).start(getThisStage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void serviceUser() {
        if(UserController.statusUser){
            try {
                infoTextLabel.setText(AuthorizationErrors.AUTHORIZED_SERVICE.toString());
                new SceneChanger(AUTHORIZATION_MAIN_SERVICE).start(getThisStage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            infoTextLabel.setText(AuthorizationErrors.NOT_AUTHORIZED.toString());
        }
    }
    @FXML
    protected void passwordReset()
    {
        try {
            new SceneChanger(RESET_PASSWORD).start(getThisStage());
        } catch (Exception e)  {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void registerClicked()
    {
        try {
            new SceneChanger(CONNECTION).start(getThisStage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void authorizationScene() {
        try {
            new SceneChanger(AUTHORIZATION).start(getThisStage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    protected void clearErrorLabel() {
        infoTextLabel.setText("");
    }

    protected void setInfoTextLabel(Text infoTextLabel) {
        this.infoTextLabel = infoTextLabel;
    }

    protected void setInfoTextLabelText(String text) {
        this.infoTextLabel.setText(text);
    }

}
