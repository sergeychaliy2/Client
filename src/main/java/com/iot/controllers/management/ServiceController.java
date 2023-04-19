package com.iot.controllers.management;

import com.iot.controllers.UserController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class ServiceController {

    @FXML private Pane connectionDeviceWindow;
    @FXML private VBox connectionDeviceBox;
    @FXML private Rectangle eventRect;
    @FXML private Rectangle connectionRect;

    private double startEventRectY;
    private double startConnectionRectY;

    @FXML
    protected void initialize() {
        this.startEventRectY = eventRect.getY();
        this.startConnectionRectY = connectionRect.getY();
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
