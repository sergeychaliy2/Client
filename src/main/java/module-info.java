module com.iot.iot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires json.simple;
    requires Java.WebSocket;


    opens com.iot to javafx.fxml;
    exports com.iot;
    exports com.iot.controllers;
    opens com.iot.controllers to javafx.fxml;
    exports com.iot.model.responses;
    opens com.iot.model.responses to javafx.fxml;
    exports com.iot.scenes;
    opens com.iot.scenes to javafx.fxml;
    exports com.iot.controllers.identities;
    opens com.iot.controllers.identities to javafx.fxml;
    exports  com.iot.controllers.management;
    opens com.iot.controllers.management to javafx.fxml;
    exports com.iot.model;
    opens com.iot.model to javafx.fxml;
}