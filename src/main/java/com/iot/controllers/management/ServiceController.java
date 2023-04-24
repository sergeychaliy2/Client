package com.iot.controllers.management;
import com.iot.controllers.identities.AbstractAuthorizationController;
import com.iot.model.*;
import com.iot.model.responses.AuthorizationErrors;
import com.iot.model.responses.AuthorizationSuccessResponses;
import com.iot.model.responses.ServiceErrors;
import com.iot.scenes.SceneChanger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.regex.Matcher;

import static com.iot.scenes.ScenesNames.MAIN;

public class ServiceController extends AbstractAuthorizationController {
    @FXML
    private Button findDevice;
    @FXML
    private ComboBox userComboBox;
    @FXML private ImageView loadingCircle;
    @FXML private TextField sensorText;
    @FXML private TextField stateText;
    @FXML
    private ListView<JSONObject> paneDevice;
    private final String str = "Выход";

    @FXML
    protected void initialize() {
        ObservableList<String> list = FXCollections.observableArrayList(str);
        userComboBox.setItems(list);
        userComboBox.setPromptText(UserProfileModel.getInstance().getUserInstance());
    }

    public Stage getThisStage() {
        return (Stage) findDevice.getScene().getWindow();
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
        String st = userComboBox.getSelectionModel().getSelectedItem().toString();
        if (st.equals(str)) {
            userComboBox.getItems().clear();
            homeScene();
            AuthenticateModel.getInstance().setAuthorized(false);
        } else {
            System.out.println("Ошибка");
        }
    }

    @FXML
    protected void findDevice() {
        try {
            AbstractAuthorizationController abstractAuthorizationController = new AbstractAuthorizationController();
            JSONObject obj = new JSONObject();
            String endPoint = Endpoints.RECEIVING_DEVICES.toString();
//        loadingCircle.setVisible(true);
            HttpClient.getInstance().get(obj, endPoint);
            abstractAuthorizationController.checkServerResponseIs();
            paneDevice.getItems().addAll(obj);
        }catch (Exception e){
            setInfoTextLabelText("ошибка");
        }
    }
    @FXML
    protected void stateChange() {
        clearErrorLabel();
        if ((sensorText.getText()!=null && stateText.getText()!=null)) {
            JSONObject obj = new JSONObject();
            obj.put("sensor", sensorText.getText());
            obj.put("state ", stateText.getText());
            String endPoint = Endpoints.REGISTRATION.toString();
//            AuthorizationModel.getInstance().setRequest(
//                    new ServerRequest(endPoint, HttpRequestTypes.POST, obj)
//            );
            HttpClient.getInstance().post(obj, endPoint);
            checkServerResponseIs();
        } else {
            setInfoTextLabelText(ServiceErrors.NO_STATE_DEVICE.toString());
        }
    }

    @Override
    protected void transactServerResponse(ServerResponse response) {
        try {
            JSONParser parser = new JSONParser();

            switch (response.responseCode()) {
                case HttpStatus.SC_ACCEPTED ->  {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    if (resultObject.get("msg")!= null) {
                        String responseMessage = resultObject.get("msg").toString();
                        if (responseMessage.equals(AuthorizationSuccessResponses.STATE_CHANGE.toString())) {
                            setInfoTextLabelText(AuthorizationSuccessResponses.STATE_CHANGE.toString());
                        }
                        else {
                            setInfoTextLabelText(AuthorizationSuccessResponses.RESET_STATUS.toString());
                        }
                    }
                }
                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    String message = switch (resultObject.get("code").toString()) {
                        case "ET01"  -> ServiceErrors.TOKEN_IS_ENDED.toString();
                        case "ET02"  -> ServiceErrors.NO_USER_ID.toString();
                        case "EE01"  -> ServiceErrors.NO_DEVICE.toString();
                        case "EE04"  -> ServiceErrors.NO_USER_DEVICE.toString();
                        case "EE06"  -> ServiceErrors.NO_STATE_DEVICE.toString();
                        case "OE01"  -> ServiceErrors.NO_LISTEN_SOCKET.toString();
                        case "OE02"  -> ServiceErrors.NO_STANDART.toString();
                        default      -> null;
                    };
//                    if (message == null && resultObject.get("code").toString().equals("ET01")) {
//                        AuthorizationModel.getInstance().updateToken();
//                    }
                    setInfoTextLabelText(message);
                }
            }
        } catch (ParseException e) { setInfoTextLabelText(e.getMessage()); }
    }
}
