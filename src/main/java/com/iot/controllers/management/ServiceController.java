package com.iot.controllers.management;
import com.iot.model.*;
import com.iot.model.consts.CommonErrors;
import com.iot.scenes.SceneChanger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.UUID;

import static com.iot.model.responses.ConnectionErrors.UUID_FORMAT_IS_NOT_CORRECT;
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
            UUID.fromString(userUUID);
            loadingCircle.setVisible(true);

            Configuration configuration = Configuration.getInstance();
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", String.format("Bearer %s", AuthenticateModel.getInstance().getAccessToken()));

            new WebSocketThread (
                    new CustomWebSocketHandler (
                            new URI(String.format("%s%s%d%s",configuration.getWsConnectionType(), configuration.getHost(), configuration.getPort(), CommonErrors.Endpoints.APP_CONNECTION)),
                            headers,
                            userUUID,
                            infoTextLabel
                    )
            ).start();

        } catch (IllegalArgumentException e) {
             infoTextLabel.setText(UUID_FORMAT_IS_NOT_CORRECT.getExceptionName());
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
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void selectComboBox() {
        String str = userComboBox.getSelectionModel().getSelectedItem();

        if (str.equals(exitFromProfileText)) {
            userComboBox.getItems().clear();
            AuthenticateModel.getInstance().setAuthorized(false);
            homeScene();
        }
    }
}
