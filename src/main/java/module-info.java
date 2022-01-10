module com.terwergreen {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.terwergreen to javafx.fxml;
    exports com.terwergreen;
    exports com.terwergreen.controller;
    opens com.terwergreen.controller to javafx.fxml;
}
