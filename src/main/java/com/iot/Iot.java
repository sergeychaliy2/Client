package com.iot;

import com.iot.scenes.SceneChanger;
import javafx.application.Application;
import javafx.stage.Stage;

import static com.iot.scenes.ScenesNames.MAIN;

public class Iot extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        new SceneChanger(MAIN).start(new Stage());
    }

    public static void main(String[] args) {
        launch();
    }
}