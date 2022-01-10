module com.terwergreen {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.web;

    opens com.terwergreen to javafx.fxml;
    opens com.terwergreen.controller to javafx.fxml;
    exports com.terwergreen;
}
