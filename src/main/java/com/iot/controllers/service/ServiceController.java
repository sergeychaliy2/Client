package com.iot.controllers.service;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.auth.UserProfileModel;
import com.iot.model.constants.Endpoints;
import com.iot.model.service.CustomWebSocketHandler;
import com.iot.model.utils.Configuration;
import com.iot.scenes.SceneChanger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import static com.iot.model.constants.Responses.Socket.UUID_FORMAT_IS_NOT_CORRECT;
import static com.iot.scenes.ScenesNames.MAIN;

public class ServiceController {


    @FXML
    private Pane connectionWindowPane;
    @FXML
    private ComboBox<String> userComboBox;
    @FXML
    private ImageView loadingCircle;

    @FXML
    private Text infoTextLabel;

    @FXML
    private TextField uuidTextLabel;

    @FXML VBox connectionWindowVBox;

    private boolean isConnectionWindowOpen = false;
    private final String exitFromProfileText = "Выход";

    @FXML
    protected void initialize() {
        userComboBox.setPromptText(UserProfileModel.getInstance().getUserLogin());
        userComboBox.setItems(FXCollections.singletonObservableList(exitFromProfileText));
    }

    @FXML
    protected void findDeviceBtnClicked() {
        infoTextLabel.setText("");

        connectionWindowPane.setVisible(!isConnectionWindowOpen);
        connectionWindowVBox.setVisible(!isConnectionWindowOpen);
        connectionWindowVBox
                .getChildren()
                .stream()
                .filter(it-> !it.equals(loadingCircle))
                .forEach(it ->
                    {
                        it.setVisible(!isConnectionWindowOpen);
//                        it.setDisable(!isConnectionWindowOpen);
                    }
                );
        isConnectionWindowOpen = !isConnectionWindowOpen;
    }

    @FXML
    protected void findNewDevice() throws URISyntaxException {
        String userUUID = uuidTextLabel.getText();
        try {
//            UUID.fromString(userUUID);

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", String.format("Bearer %s", AuthenticateModel.getInstance().getAccessToken()));

            new Thread(() -> {
                try {
                    new CustomWebSocketHandler(
                            new URI(Configuration.getInstance().generate(false, Endpoints.APP_CONNECTION)),
                            headers,
                            userUUID,
                            infoTextLabel,
                            loadingCircle
                    ).connect();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }).start();

        } catch (IllegalArgumentException e) {
             infoTextLabel.setText(UUID_FORMAT_IS_NOT_CORRECT);
        }
    }

    public Stage getThisStage() {
        return (Stage) loadingCircle.getScene().getWindow();
    }


    @FXML
    protected void homeScene() {
        try {
            new SceneChanger(MAIN).start(getThisStage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @FXML
    protected void selectComboBox() {
        String str = userComboBox.getSelectionModel().getSelectedItem();

        if (str.equals(exitFromProfileText)) {
            AuthenticateModel.getInstance().setAuthorized(false);
            homeScene();
        }
    }
}
