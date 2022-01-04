package com.terwergreen.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

/**
 * @author: terwer
 * @date: 2021/12/25 10:47
 * @description:
 */
public class HomeController{
    @FXML
    private Button btnChooseFile;

    public void chooseFileClicked(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        // fileChooser.showOpenDialog(this);
        System.out.println("点击了");
    }
}
