package com.iot.controllers.management;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.constants.Endpoints;
import com.iot.model.service.DetailedDevice;
import com.iot.model.service.DeviceDefinition;
import com.iot.model.utils.AlertDialog;
import com.iot.model.utils.HttpClient;
import com.iot.model.utils.ServerResponse;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Set;

import static com.iot.model.constants.Responses.Service.*;
import static com.iot.model.utils.AlertDialog.CustomAlert.*;


public class ManagementController extends Manager {
    private int tokenExpiredRequestsCounter = 0;

    @FXML
    protected void initialize() {
        if (!AuthenticateModel.getInstance().getIsAuthorized()) {
            //todo dialog window
            homeScene();
        }

        setUpResolvingConnectionWebSocket();
        resolvingConnectionsWS.connect();

        userComboBox.setPromptText(AuthenticateModel.getInstance().getUserLogin());
        userComboBox.setItems(FXCollections.singletonObservableList(exitFromProfileText));

        setUpListViewSettings();
        isArrayWaiting = true;
        HttpClient.execute (null, Endpoints.ALL_DEVICES, HttpClient.HttpMethods.GET);
        loadingCircle2.setVisible(true);
        checkServerResponseIs();

        setOnCloseOperation();
    }

    private void collectDevicesToList(JSONArray arr) {
        ObservableList<DeviceDefinition> devices = FXCollections.observableArrayList();

        short counter = 0;

        for (Object elem : arr) {
            JSONObject obj = (JSONObject) elem;
            devices.add(
                    new DeviceDefinition(
                            (long) obj.get("deviceId"),
                            obj.get("deviceName").toString(),
                            obj.get("deviceDescription").toString(),
                            ++counter
                    )
            );
        }

        introDeviceInfo.setItems(devices);
        loadingCircle2.setVisible(false);
    }

    @Override
    protected void transactServerResponse(ServerResponse response) {
        try {
            JSONParser parser = new JSONParser();

            switch (response.responseCode()) {
                case HttpStatus.SC_OK -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    try {
                        String responseMessage = resultObject.get("msg").toString();

                        if (!responseMessage.equals(DEVICE_STATE_HAS_BEEN_UPDATED)
                            && !responseMessage.equals(DEVICE_LISTENING_STATE_WAS_RESET)) {
                            throw new RuntimeException("Unknown message");
                        }

                        Platform.runLater(() ->
                                AlertDialog.alertOf(INFORMATION, "Информация", responseMessage).showAndWait()
                        );

                    } catch (Exception e) {
                        try {
                            if (resultObject.get("message").toString().equals(ACCESS_TOKEN_WAS_UPDATED)) {
                                tokenExpiredRequestsCounter = 0;
                                Platform.runLater(()-> {
                                    getThisStage().close();
                                    serviceUser();
                                });
                                return;
                            }
                        } catch(Exception ignore) {}

                        if (isArrayWaiting) {
                            collectDevicesToList(
                                    (JSONArray) resultObject.get("deviceListInfo")
                            );
                        } else {
                            JSONObject obj = (JSONObject) resultObject.get("deviceInfo");
                            JSONObject sensors = (JSONObject) parser.parse(obj.get("sensorsState").toString());
                            Set<String> keys = sensors.keySet();

                            ObservableList<DetailedDevice> list = FXCollections.observableArrayList();
                            keys.forEach(key->
                                    list.add(new DetailedDevice(
                                            (long) obj.get("deviceId"),
                                            key,
                                            sensors.get(key).toString()
                                    )));

                            Platform.runLater(() -> {
                                sensorsList.setItems(list);
                                deviceName.setText(obj.get("deviceName").toString());
                                setFullDescPaneVisible(true);
                            });
                        }
                    }

                }

                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    String message = switch (resultObject.get("code").toString()) {
                        case "ET01" -> null;
                        case "EE04" -> USER_OR_DEVICE_WAS_NOT_FOUND;
                        case "EE06" -> DEVICE_STATE_WAS_NOT_UPDATED;
                        case "EO01" -> DEVICE_IS_NOT_LISTENING;
                        default -> throw new RuntimeException("Unknown error");
                    };

                    if (message == null) {
                        if (++tokenExpiredRequestsCounter == 2) {
                            AuthenticateModel.getInstance().setIsAuthorized(false);

                            Platform.runLater(()-> {
                                getThisStage().close();
                                authorizationScene();
                            });

                            return;
                        }
                        HttpClient.execute(null, Endpoints.UPDATE_TOKEN, HttpClient.HttpMethods.GET);
                        checkServerResponseIs();

                    } else {
                        Platform.runLater(() ->
                                AlertDialog.alertOf(EXCEPTION, "Ошибка", message).showAndWait()
                        );
                    }
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
