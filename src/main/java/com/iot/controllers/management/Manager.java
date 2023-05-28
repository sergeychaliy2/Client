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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;

import java.util.HashMap;

import static com.iot.model.constants.Endpoints.DEVICE_GETTING_UPDATES;
import static com.iot.model.constants.Endpoints.STATE_CHANGE;
import static com.iot.model.constants.Responses.Service.*;
import static com.iot.model.constants.Responses.Socket.UUID_FORMAT_IS_NOT_CORRECT;

public abstract class Manager extends Controller {
    @FXML
    protected Label deviceName;
    @FXML
    protected ListView<DetailedDevice> sensorsList;
    @FXML
    protected ImageView loadingCircle2;
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
    protected Pane changingStatePane;
    @FXML
    protected Pane changingStateNumberGroup;
    @FXML
    protected Pane changingStateLogicGroup;
    @FXML
    protected ListView<DeviceDefinition> introDeviceInfo;
    @FXML
    protected CheckBox sensorOnCheckBox;
    @FXML
    protected CheckBox sensorOffCheckBox;
    @FXML
    protected Label sensorLogicStateLabel;
    @FXML
    protected TextField changingNumericStateField;

    protected ConnectionWebSocket connectionWS;
    protected ResolvingConnectionsWebSocket resolvingConnectionsWS;
    private boolean isConnectionWindowOpen = false;
    protected boolean isArrayWaiting = false;
    protected static final String exitFromProfileText = "Выход";
    private static final String sensorWillOff = "Датчик будет выключен";
    private static final String sensorWillOn = "Датчик будет включен";
    protected static final String changeUserData = "Изменить свои данные";
    protected static final String settingsReset = "Сбросить настройки";

    @FXML
    protected void findDeviceBtnClicked() {
        if (fullDeviceDescriptionPane.isVisible()) return;
        if (loadingCircle.isVisible()) loadingCircle.setVisible(false);
        infoTextLabel.setText("");

        connectionWindowPane.setVisible(!isConnectionWindowOpen);
        isConnectionWindowOpen = !isConnectionWindowOpen;
    }

    @FXML
    protected void findNewDevice(){
        if (resolvingConnectionsWS.isOpen()) {
            resolvingConnectionsWS.close();
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

    @FXML
    protected void resetDeviceListening() {
        DeviceDefinition deviceDefinition = introDeviceInfo.getSelectionModel().getSelectedItem();
        HttpClient.execute(null, String.format(Endpoints.RESET_STATUS, deviceDefinition.id()), HttpClient.HttpMethods.GET);
        checkServerResponseIs();
    }


    @FXML
    protected void selectComboBox() {
        String chosenElement = userComboBox.getSelectionModel().getSelectedItem();

        switch (chosenElement) {
            case exitFromProfileText -> {
                Alert alert = AlertDialog.alertOf (
                        AlertDialog.CustomAlert.CONFIRMATION,
                        "Уведомление",
                        EXIT_SUGGESTION
                );
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    AuthenticateModel.getInstance().setIsAuthorized(false);
                    getThisStage().close();
                    homeScene();
                } else {
                    getThisStage().close();
                    serviceUser();
                }
            }
            case settingsReset -> {
                disconnectSockets();
                setUpResolvingConnectionWebSocket();
                resolvingConnectionsWS.connect();
                AlertDialog.alertOf(
                        AlertDialog.CustomAlert.INFORMATION,
                        "Уведомление",
                        SETTINGS_WAS_RESET
                ).showAndWait();
            }
            case changeUserData -> {
                getThisStage().close();
                personalDataScene();
            }

        }

    }

    @FXML
    protected void sensorOnCheckBoxClicked() {
        if (sensorOffCheckBox.isSelected()) {
            sensorOffCheckBox.setSelected(false);
        }
        sensorLogicStateLabel.setText(sensorWillOn);
    }

    @FXML
    protected void sensorOffCheckBoxClicked() {
        if (sensorOnCheckBox.isSelected()) {
            sensorOnCheckBox.setSelected(false);
        }
        sensorLogicStateLabel.setText(sensorWillOff);
    }


    @FXML
    protected void fullDescPaneBackBtnClicked() {
        setFullDescPaneVisible(false);
        updateIntroList();
    }

    @FXML
    protected void changeStateBtnClicked() {
        String state = null;

        if (changingStateLogicGroup.isVisible()) {
            if (!sensorOffCheckBox.isSelected() && !sensorOnCheckBox.isSelected()) {
                AlertDialog.alertOf(
                        AlertDialog.CustomAlert.EXCEPTION,
                        "Уведомление",
                        AT_LEAST_ONE_STATE_MUST_BE_USING
                ).showAndWait();
                return;
            }
            if (sensorOnCheckBox.isSelected())       state = "true";
            else if (sensorOffCheckBox.isSelected()) state = "false";
        } else {
            try {
                state = String.valueOf(
                        Integer.parseInt(changingNumericStateField.getText())
                );
            } catch (NumberFormatException e) {
                AlertDialog.alertOf(
                        AlertDialog.CustomAlert.EXCEPTION,
                        "Уведомление",
                        SENSOR_STATE_MUST_BE_ONLY_NUMERIC
                ).showAndWait();
                return;
            }
        }
        DetailedDevice device = sensorsList.getSelectionModel().getSelectedItem();

        JSONObject obj = new JSONObject();
        obj.put("sensor", device.sensorName());
        obj.put("state", state);

        HttpClient.execute(obj, String.format(STATE_CHANGE, device.deviceId()), HttpClient.HttpMethods.PUT);
        checkServerResponseIs();
    }

    @FXML
    protected void changingStateBackBtnClicked() {
        setChangingStatePaneVisible(false, null);
        setFullDescPaneDisable(false);

        isArrayWaiting = false;
        DeviceDefinition deviceDefinition = introDeviceInfo.getSelectionModel().getSelectedItem();
        HttpClient.execute(null, String.format(Endpoints.ONE_DEVICE, deviceDefinition.id()), HttpClient.HttpMethods.GET);
        checkServerResponseIs();
    }

    @FXML
    protected void updateIntroList() {
        isArrayWaiting = true;
        HttpClient.execute(null, Endpoints.ALL_DEVICES, HttpClient.HttpMethods.GET);
        checkServerResponseIs();
    }

    protected void setIntroDeviceDescPaneDisabled(boolean state) {
        introDeviceDescriptionPane.setDisable(state);
    }

    protected void setFullDescPaneVisible (boolean state) {
        setIntroDeviceDescPaneDisabled(state);
        fullDeviceDescriptionPane.setVisible(state);
    }

    private void setFullDescPaneDisable(boolean state) {
        fullDeviceDescriptionPane.setDisable(state);
    }

    protected void setFullDescPaneSettings() {
        sensorsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                setFullDescPaneDisable(true);
                DetailedDevice device = sensorsList.getSelectionModel().getSelectedItem();
                setChangingStatePaneVisible(true, device.sensorState());
            }
        });
    }

    protected void setChangingStatePaneVisible(boolean state, String sensorType) {
        changingStatePane.setVisible(state);

        if (sensorType == null) {
            changingStateNumberGroup.setVisible(state);
            changingStateLogicGroup.setVisible(state);
            return;
        }

        try {
            Integer.parseInt(sensorType);
            changingStateNumberGroup.setVisible(state);
        } catch (NumberFormatException e) {
            changingStateLogicGroup.setVisible(state);
        }

    }

    private HashMap<String, String> getCommonWSHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", String.format("Bearer %s", AuthenticateModel.getInstance().getAccessToken()));
        return headers;
    }

    private void setUpConnectionWebSocket(String userUUID) {
        if (resolvingConnectionsWS == null)
            throw new RuntimeException("Web socket instance must not be null");

        connectionWS = new ConnectionWebSocket(
                Configuration.generateURL(false, Endpoints.APP_CONNECTION),
                userUUID,
                connectionWindowPane,
                infoTextLabel,
                loadingCircle,
                resolvingConnectionsWS,
                getCommonWSHeaders()
        );
    }

    protected void setUpResolvingConnectionWebSocket() {
        resolvingConnectionsWS = new ResolvingConnectionsWebSocket(
                Configuration.generateURL(false, DEVICE_GETTING_UPDATES)
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
    protected String translateState(String state) {
        try {
            Integer.parseInt(state);
            return state;
        } catch (NumberFormatException e) {
            return state.equalsIgnoreCase("true") ? "Включен" : "Выключен";
        }
    }
    protected void disconnectSockets() {
        if (resolvingConnectionsWS != null) resolvingConnectionsWS.close();
        if (connectionWS != null)           connectionWS.close();
    }

}
