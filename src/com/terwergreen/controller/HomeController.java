package com.terwergreen.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * @author: terwer
 * @date: 2021/12/25 10:47
 * @description:
 */
public class HomeController{
    @FXML
    private Button btnChooseFile;

    public void chooseFileClicked(){
        Stage stage = (Stage) btnChooseFile.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        File file = fileChooser.showOpenDialog(stage);
        if(file != null){
            System.out.println(file.getAbsolutePath());
        }

        System.out.println("点击了");
    }
}
