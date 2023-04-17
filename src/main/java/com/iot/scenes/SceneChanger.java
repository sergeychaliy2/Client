package com.iot.scenes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SceneChanger extends Application
{
    private final ScenesNames name;

    public SceneChanger (ScenesNames name)
    {
        if (name == null) throw new RuntimeException("Scene name is null");
        this.name = name;
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loader = null;
        switch (name)
        {
            case MAIN ->                    loader = new FXMLLoader(SceneChanger.class.getResource("Main.fxml"));
            case CONNECTION ->              loader = new FXMLLoader(SceneChanger.class.getResource("Connection.fxml"));
            case CONTACT ->                 loader = new FXMLLoader(SceneChanger.class.getResource("Contact.fxml"));
            case AUTHORIZATION ->           loader = new FXMLLoader(SceneChanger.class.getResource("Authorization.fxml"));
            case RESET_PASSWORD ->          loader = new FXMLLoader(SceneChanger.class.getResource("ResetPassword.fxml"));
            case CONFIRM_PASSWORD ->        loader = new FXMLLoader(SceneChanger.class.getResource("ConfirmPassword.fxml"));
            case MAIN_AUTHORIZATION ->      loader = new FXMLLoader(SceneChanger.class.getResource("MainAuthorization.fxml"));
            case AUTHORIZATION_MAIN_SERVICE ->      loader = new FXMLLoader(SceneChanger.class.getResource("Service.fxml"));
        }

        assert(loader != null);

        Scene scene = new Scene(loader.load(), 1280, 720);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("IotApp");
        stage.show();
    }

}
