package com.iot.controllers;
import com.iot.scenes.SceneChanger;
import com.iot.model.UserProfileModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.iot.scenes.ScenesNames.*;

public class UserController implements Initializable {
    @FXML private Button contactMenuBtn;
    @FXML private ComboBox<String> userComboBox;
    private final String str="Выход";
    private Stage getThisStage() {
        return (Stage) contactMenuBtn.getScene().getWindow();
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
    protected void homeForAuthorized() throws Exception {
        new SceneChanger(MAIN_AUTHORIZATION).start(getThisStage());
    }
    @FXML
    protected void serviceForAuthorized() throws Exception
    {
        new SceneChanger(MAIN_SERVICE).start(getThisStage());
    }
    @FXML
    protected void selectComboBox() throws Exception {
        String st=userComboBox.getSelectionModel().getSelectedItem();
        if(st.equals(str)){
            userComboBox.getItems().clear();
            new SceneChanger(MAIN).start(getThisStage());
        }else{
            System.out.println("Ошибка");
        }
    }
    public void initialize(URL url, ResourceBundle resourceBundle){
        ObservableList<String>list= FXCollections.observableArrayList(str);
        userComboBox.setItems(list);
        userComboBox.setPromptText(UserProfileModel.getInstance().getUserLogin());
    }

}
