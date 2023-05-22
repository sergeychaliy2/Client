package com.iot.controllers.management;

import com.iot.controllers.Controller;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.constants.Endpoints;
import com.iot.model.service.ConnectionWebSocket;
import com.iot.model.service.DetailedDevice;
import com.iot.model.service.DeviceDefinition;
import com.iot.model.service.ResolvingConnectionsWebSocket;
import com.iot.model.utils.AlertDialog;
import com.iot.model.utils.Configuration;
import com.iot.model.utils.HttpClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;

import static com.iot.model.constants.Endpoints.DEVICE_GETTING_UPDATES;
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
    protected ConnectionWebSocket connectionWS;
    protected ResolvingConnectionsWebSocket resolvingConnectionsWS;
    private boolean isConnectionWindowOpen = false;
    protected boolean isArrayWaiting = false;
    protected static final String exitFromProfileText = "Выход";

    @FXML
    protected void findDeviceBtnClicked() {
        infoTextLabel.setText("");
        setOnCloseOp();

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
    protected void findNewDevice(){

        if (resolvingConnectionsWS != null) {
            resolvingConnectionsWS.close();
            resolvingConnectionsWS = null;
        }

        String userUUID = uuidTextLabel.getText();
        try {
//            UUID.fromString(userUUID);
            setUpResolvingConnectionWebSocket();
            setUpConnectionWebSocket(userUUID);
            connectionWS.connect();

        } catch (IllegalArgumentException e) {
            infoTextLabel.setText(UUID_FORMAT_IS_NOT_CORRECT);
        }
    }

    private void setOnCloseOp() {
        getThisStage().setOnHidden(event -> shutdown());
    }



    @FXML
    protected void selectComboBox() {
        String str = userComboBox.getSelectionModel().getSelectedItem();

        if (str.equals(exitFromProfileText)) {
            Alert alert = AlertDialog.alertOf (
                    AlertDialog.CustomAlert.CONFIRMATION,
                    "Подтверждение",
                    "Вы хотите выйти?"
            );
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                AuthenticateModel.getInstance().setIsAuthorized(false);
                homeScene();
            } else {
                serviceUser();
            }
        }
    }

    protected void changeFullDescriptionPaneVisibility(boolean state) {
        fullDeviceDescriptionPane.setVisible(state);
        fullDeviceDescriptionPane.getChildren().forEach(it-> it.setVisible(state));
    }

    protected HashMap<String, String> getCommonWSHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", String.format("Bearer %s", AuthenticateModel.getInstance().getAccessToken()));
        return headers;
    }
    private void setUpConnectionWebSocket(String userUUID) {
        if (resolvingConnectionsWS == null)
            throw new RuntimeException("Web socket instance must not be null");

        connectionWS = new ConnectionWebSocket(
                Configuration.generate(false, Endpoints.APP_CONNECTION),
                userUUID,
                connectionWindowPane,
                infoTextLabel,
                loadingCircle,
                resolvingConnectionsWS
        );
        connectionWS.setHeaders(getCommonWSHeaders());
    }

    protected void setUpResolvingConnectionWebSocket() {
        resolvingConnectionsWS = new ResolvingConnectionsWebSocket(
                Configuration.generate(false, DEVICE_GETTING_UPDATES)
        );
        resolvingConnectionsWS.setHeaders(getCommonWSHeaders());
    }

    protected void setUpListViewSettings() {
        introDeviceInfo.setFixedCellSize(100.0);

        introDeviceInfo.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                isArrayWaiting = false;
                DeviceDefinition device = introDeviceInfo.getSelectionModel().getSelectedItem();
                HttpClient.execute(null, String.format(Endpoints.ONE_DEVICE, device.id()), HttpClient.HttpMethods.GET);
                checkServerResponseIs();
            }
        });

        sensorsList.setFixedCellSize(100.0);
    }

    public void shutdown() {

        System.out.println("GOODBYE");
    }

}
