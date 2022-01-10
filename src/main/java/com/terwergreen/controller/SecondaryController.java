package com.terwergreen.controller;

import java.io.IOException;

import com.terwergreen.App;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}