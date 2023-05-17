package com.iot.controllers.management;

import com.iot.controllers.Controller;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.constants.Endpoints;
import com.iot.model.service.CustomWebSocketHandler;
import com.iot.model.service.DetailedDevice;
import com.iot.model.service.DeviceDefinition;
import com.iot.model.utils.Configuration;
import com.iot.model.utils.HttpClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import static com.iot.model.constants.Responses.Socket.UUID_FORMAT_IS_NOT_CORRECT;

public abstract class Manager extends Controller {
    @FXML
    protected Label deviceName;
    @FXML
    protected ListView<DetailedDevice> sensorsList;
    @FXML
    protected ImageView loadingCircle2;
    @FXML
    protected HBox serviceBar;

    @FXML
    protected Pane connectionWindowPane;
    @FXML
    protected ComboBox<String> userComboBox;
    @FXML
    protected TextField uuidTextLabel;
    @FXML
    protected VBox connectionWindowVBox;
    @FXML
    protected Pane fullDeviceDescriptionPane;
    @FXML
    protected Pane introDeviceDescriptionPane;
    @FXML
    protected ListView<DeviceDefinition> introDeviceInfo;

    protected boolean isConnectionWindowOpen = false;
    protected boolean isArrayWaiting = false;
    protected static final String exitFromProfileText = "Выход";

    protected void setUpListViewSettings() {
        introDeviceInfo.setFixedCellSize(100.0);
        introDeviceInfo.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                isArrayWaiting = false;
                DeviceDefinition device = introDeviceInfo.getSelectionModel().getSelectedItem();
                HttpClient.getInstance().get (
                        String.format(Endpoints.ONE_DEVICE, device.id())
                );
                checkServerResponseIs();
            }
        });
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
                            it.setDisable(isConnectionWindowOpen);
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

            new CustomWebSocketHandler(
                    new URI(Configuration.getInstance().generate(false, Endpoints.APP_CONNECTION)),
                    headers,
                    userUUID,
                    infoTextLabel,
                    loadingCircle,
                    connectionWindowPane
            ).connect();

        } catch (IllegalArgumentException e) {
            infoTextLabel.setText(UUID_FORMAT_IS_NOT_CORRECT);
        }
    }



    @FXML
    protected void selectComboBox() {
        String str = userComboBox.getSelectionModel().getSelectedItem();

        if (str.equals(exitFromProfileText)) {
            //todo вы уверены, что хотите выйти
//            AuthenticateModel.getInstance().setUserLogin(null);
            homeScene();
        }
    }

    protected void changeFullDescriptionPaneVisibility(boolean state) {
        fullDeviceDescriptionPane.setVisible(state);
        fullDeviceDescriptionPane.getChildren().forEach(it-> it.setVisible(state));
    }
}
