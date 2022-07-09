package com.terwergreen.controller;

import com.alibaba.fastjson.JSON;
import com.sun.codemodel.internal.JNullType;
import com.terwergreen.model.control.KeyValueItem;
import com.terwergreen.model.data.HomeData;
import com.terwergreen.util.ResourceUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @name: SelectFileController
 * @author: terwer
 * @date: 2022-07-09 11:40
 **/
public class SelectFileController implements Initializable {

    private static Logger logger = LoggerFactory.getLogger(SelectFileController.class);

    @FXML
    private Button btnSelectFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void selectFileClicked(ActionEvent event) {
        logger.info("selectFileClicked");

        Stage stage = (Stage) btnSelectFile.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            if (!file.getName().endsWith(".md")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "文件格式错误，请重新选择！", ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
                return;
            }

            String key = file.getName();

            try {
                StringBuilder sb = new StringBuilder();
                StringBuilder metadataSb = new StringBuilder();
                List<String> allLines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
                if (CollectionUtils.isNotEmpty(allLines)) {
                    String postTitle = allLines.get(0);
                    boolean hasTitle = false;
                    for (int i = 0; i < allLines.size(); i++) {
                        if (allLines.get(i).startsWith("#")
                                && hasTitle == false) {
                            postTitle = allLines.get(i).split(" ")[1];
                            hasTitle = true;
                            logger.info("标题：" + postTitle);
                        }

                        if (hasTitle == false) {
                            metadataSb.append(allLines.get(i));
                            metadataSb.append("\n");
                        } else {
                            sb.append(allLines.get(i));
                            sb.append("\n");
                        }
                    }

                    Yaml yaml = new Yaml();
                    String input = metadataSb.toString();
                    LinkedHashMap metadatMap = new LinkedHashMap();
                    for (Object data : yaml.loadAll(input)) {
                       if(null != data){
                           metadatMap = (LinkedHashMap) data;
                           break;
                       }
                    }

                    HomeData homeData = new HomeData();
                    homeData.setMwebFileId(key);
                    homeData.setPostTitle(postTitle);
                    homeData.setFrom(null);
                    homeData.setContent(sb.toString());
                    homeData.setMetadata(metadatMap);

                    // 打开编辑器
                    FXMLLoader loader = new FXMLLoader(ResourceUtil.getResourceAsURL("write.fxml"));
                    Parent root1 = (Parent) loader.load();
                    Stage stage1 = new Stage();
                    stage1.setScene(new Scene(root1));
                    stage1.setResizable(false);

                    // 传值
                    WriteController writeController = loader.getController();
                    writeController.initData(homeData);

                    // stage.close();
                    stage1.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
