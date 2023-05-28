package com.iot.controllers.management;

import com.iot.controllers.Controller;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.utils.HttpClient;
import com.iot.model.utils.ServerResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;

import static com.iot.model.constants.Endpoints.CHANGE_USER_DATA;
import static com.iot.model.constants.Responses.Authorization.DATA_CHANGED;
import static com.iot.model.constants.Responses.Authorization.PASSWORD_FORMAT_IS_INCORRECT;
import static com.iot.model.constants.Responses.PersonalData.*;
import static com.iot.model.constants.Responses.Service.*;

public class PersonalDataController extends Controller {

    @FXML private TextField repeatPasswordTField;
    @FXML private TextField passwordTField;
    @FXML private Button submitBtn;
    private int tokenExpiredRequestsCounter = 0;

    @FXML
    protected void initialize() {
        setButtonsReactionOnAction(List.of(submitBtn));
    }


    @Override
    public void transactServerResponse(ServerResponse response) {
        try {
            JSONParser parser = new JSONParser();

            switch (response.responseCode()) {
                case HttpStatus.SC_OK -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    try {
                        String msg = resultObject.get("msg").toString();
                        if (msg.equals(DATA_WAS_SUCCESSFULLY_CHANGED)) {
                            infoTextLabel.setText(DATA_CHANGED);
                        }
                    } catch (NullPointerException e) {
                        String msg = resultObject.get("message").toString();
                        if (msg.equals(ACCESS_TOKEN_WAS_UPDATED)) {
                            tokenExpiredRequestsCounter = 0;
                            Platform.runLater(this::personalDataScene);
                        }
                    }
                }

                case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
                    String message = switch (resultObject.get("code").toString()) {
                        case "ET01" -> null;
                        case "EE03" -> USER_WAS_NOT_FOUND;
                        default -> throw new RuntimeException("Unknown error");
                    };

                    if (message == null) checkIsTokenExpired(tokenExpiredRequestsCounter);
                    else                 infoTextLabel.setText(message);
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void submitBtnClicked() {
        if (!passwordTField.getText().equals(repeatPasswordTField.getText())) {
            infoTextLabel.setText(PASSWORD_IS_NOT_EQUALS_TO_EACH_OTHER);
            return;
        }

        if (!isPasswordValid(passwordTField.getText())) {
            infoTextLabel.setText(PASSWORD_FORMAT_IS_INCORRECT);
            return;
        }

        JSONObject object = new JSONObject();
        object.put("email", AuthenticateModel.getInstance().getUserLogin());
        object.put("password", passwordTField.getText());

        loadingCircle.setVisible(true);

        HttpClient.execute(object, CHANGE_USER_DATA, HttpClient.HttpMethods.PUT);
        checkServerResponseIs();
    }
    @FXML
    protected void backBtnClicked() {
        serviceUser();
    }
}
