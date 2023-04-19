package com.iot.controllers.management;

import com.iot.controllers.UserController;
import com.iot.controllers.identities.AbstractAuthorizationController;
import com.iot.controllers.identities.AuthorizationController;
import com.iot.model.AuthenticateModel;
import com.iot.model.UserProfileModel;
import com.iot.scenes.SceneChanger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static com.iot.scenes.ScenesNames.MAIN;

public class ServiceController{
    @FXML private Button addDevice;
    @FXML private ComboBox userComboBox;
    @FXML private Pane connectionDeviceWindow;
    @FXML private VBox connectionDeviceBox;
    @FXML private Rectangle eventRect;
    @FXML private Rectangle connectionRect;

    private final String str="Выход";
    private double startEventRectY;
    private double startConnectionRectY;

    @FXML
    protected void initialize() {
        this.startEventRectY = eventRect.getY();
        this.startConnectionRectY = connectionRect.getY();
        ObservableList<String> list= FXCollections.observableArrayList(str);
        userComboBox.setItems(list);
        userComboBox.setPromptText(UserProfileModel.getInstance().getUserInstance());
    }
    private Stage getThisStage() {
        return (Stage) addDevice.getScene().getWindow();
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
    protected void selectComboBox(){
        String st=userComboBox.getSelectionModel().getSelectedItem().toString();
        if(st.equals(str)){
            userComboBox.getItems().clear();
            homeScene();
            AuthenticateModel.getInstance().setAuthorized(false);
        }else{
            System.out.println("Ошибка");
        }
    }

    @FXML
    protected void enableMouseEvent() {
        connectionDeviceBox.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                eventRect.setTranslateY(mouseEvent.getY() / 15);
                connectionRect.setHeight(mouseEvent.getY() - eventRect.getHeight());
                //connectionRect.setTranslateY(mouseEvent.getSceneY() - eventRect.getHeight());
            }
        });
        connectionDeviceBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                disableMouseEvent();
            }
        });
    }
    @FXML
    protected void disableMouseEvent() {
        connectionDeviceBox.setOnMouseMoved(null);

        eventRect.setTranslateY(startEventRectY);
        connectionRect.setHeight(startConnectionRectY);

    }



    @FXML
    protected void addNewBoard() {
//    @FXML protected void test() throws InterruptedException {
//        ScaleTransition trans = new ScaleTransition(Duration.seconds(2), circle);
//        trans.setFromX(1.0);
//        trans.setToX(0.40);
//        trans.setFromY(1.0);
//        trans.setToY(0.20);
//// Let the animation run forever
//        trans.setCycleCount(ScaleTransition.INDEFINITE);
//// Reverse direction on alternating cycles
//        trans.setAutoReverse(true);
//// Play the Animation
//        trans.play();
//        TranslateTransition trans = new TranslateTransition(Duration.seconds(12), test2);
//        trans.setFromX(test2.getWidth());
//        trans.setToX(-1.0 * test2.getLayoutBounds().getWidth());
//        // Let the animation run forever
//        trans.setCycleCount(TranslateTransition.INDEFINITE);
//        // Reverse direction on alternating cycles
//        trans.setAutoReverse(true);
//        // Play the Animation
//        trans.play();
//
//    }

        connectionRect.setHeight(100);
        eventRect.setY(103);

    }

}
