package com.iot.controllers.management;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.constants.Endpoints;
import com.iot.model.constants.Responses;
import com.iot.model.service.DetailedDevice;
import com.iot.model.service.DeviceDefinition;
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


public class ManagementController extends Manager {

    @FXML
    protected void initialize() {
        if (!AuthenticateModel.getInstance().isAuthorized()) {
            //todo dialog window
            homeScene();
        }

        userComboBox.setPromptText(AuthenticateModel.getInstance().getUserLogin());
        userComboBox.setItems(FXCollections.singletonObservableList(exitFromProfileText));
        setUpListViewSettings();
        sensorsList.setFixedCellSize(100.0);

        isArrayWaiting = true;
        HttpClient.getInstance().get (
                Endpoints.ALL_DEVICES
        );
        loadingCircle2.setVisible(true);
        checkServerResponseIs();
    }

    private void collectDevicesToList(JSONArray arr) {
        ObservableList<DeviceDefinition> devices = FXCollections.observableArrayList();

        for (Object elem : arr) {
            JSONObject obj = (JSONObject) elem;
            devices.add(
                    new DeviceDefinition(
                            (long) obj.get("deviceId"),
                            obj.get("deviceName").toString(),
                            obj.get("deviceDescription").toString()
                    )
            );
        }

        introDeviceInfo.setItems(devices);
        loadingCircle2.setVisible(false);
    }

    protected void transactServerResponse(ServerResponse response) {
        try {
            JSONParser parser = new JSONParser();
            switch (response.responseCode()) {
                case HttpStatus.SC_OK -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    try {
                        String responseMessage = resultObject.get("msg").toString();

                        switch (responseMessage) {
                            case Responses.Service.DEVICE_STATE_HAS_BEEN_UPDATED -> {}
                            case Responses.Service.DEVICE_LISTENING_STATE_WAS_RESET -> {}
                            default -> throw new RuntimeException("Unknown message");
                        }

                    } catch (Exception e) {
                        if (isArrayWaiting) {
                            collectDevicesToList(
                                    (JSONArray) resultObject.get("deviceListInfo")
                            );
                        } else {
                            JSONObject obj = (JSONObject) resultObject.get("deviceInfo");
                            JSONObject sensors = (JSONObject) parser.parse(obj.get("sensorsState").toString());
                            Set<String> keys = sensors.keySet();

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    ObservableList<DetailedDevice> list = FXCollections.observableArrayList();
                                    keys.forEach(key->
                                            list.add(new DetailedDevice(
                                                    (long) obj.get("deviceId"),
                                                    key,
                                                    sensors.get(key).toString()
                                            )));

                                    sensorsList.setItems(list);
                                    deviceName.setText(obj.get("deviceName").toString());
                                    changeFullDescriptionPaneVisibility(true);
                                }
                            });


                        }
                    }

                }

                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    String message = switch (resultObject.get("code").toString()) {
                        case "EE03" -> Responses.Authorization.NO_USER;
                        case "EA04" -> Responses.Authorization.CLIENT_IS_NOT_AUTHENTICATED;
                        default -> "Неверные данные пользователя";
                    };

                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
