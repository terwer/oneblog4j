package com.terwergreen.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {

    private static Stage primaryStage;

    private void setPrimaryStage(Stage stage) {
        App.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return App.primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getResource("../resources/home.fxml");
        Parent root = FXMLLoader.load(resource);
        // root.setStyle("-fx-font-family: 'serif'");

        primaryStage.setTitle("一博");
        primaryStage.setScene(new Scene(root));
        setPrimaryStage(primaryStage);
        primaryStage.show();
    }
}
