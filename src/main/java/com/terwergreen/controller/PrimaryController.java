package com.terwergreen.controller;

import java.io.IOException;

import com.terwergreen.App;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
