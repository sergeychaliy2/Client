package com.iot.scenes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public final class SceneChanger extends Application
{
    private final static SceneChanger instance = new SceneChanger();

    private ScenesNames name;

    private SceneChanger () {}


    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loader = null;
        switch (name)
        {
            case MAIN ->                    loader = new FXMLLoader(SceneChanger.class.getResource("Main.fxml"));
            case CONNECTION ->              loader = new FXMLLoader(SceneChanger.class.getResource("Connection.fxml"));
            case AUTHORIZATION ->           loader = new FXMLLoader(SceneChanger.class.getResource("Authorization.fxml"));
            case RESET_PASSWORD ->          loader = new FXMLLoader(SceneChanger.class.getResource("ResetPassword.fxml"));
            case SERVICE ->                 loader = new FXMLLoader(SceneChanger.class.getResource("Service.fxml"));
            case PERSONAL_DATA ->           loader = new FXMLLoader(SceneChanger.class.getResource("PersonalData.fxml"));
        }

        assert(loader != null);

        Scene scene = new Scene(loader.load(), 1280, 720);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("IotApp");
        stage.show();
    }

    public void setName(ScenesNames name) {
        this.name = name;
    }

    public static SceneChanger getInstance() { return instance; }
}
