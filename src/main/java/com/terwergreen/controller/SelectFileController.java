package com.terwergreen.controller;

import com.terwergreen.model.data.HomeData;
import com.terwergreen.util.DateUtil;
import com.terwergreen.util.ResourceUtil;
import com.terwergreen.util.yaml.DateTimeImplicitContructor;
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
import org.yaml.snakeyaml.nodes.Tag;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
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
                List<String> allLines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
                if (CollectionUtils.isNotEmpty(allLines)) {
                    StringBuilder sb = new StringBuilder();
                    StringBuilder metadataSb = new StringBuilder();

                    String postTitle = allLines.get(0);
                    boolean hasTitle = false;
                    boolean titleEnd = false;
                    int yamlTagCount = 0;
                    for (int i = 0; i < allLines.size(); i++) {
                        String line = allLines.get(i);
                        // 累计元数据与正文
                        if (titleEnd == true) {
                            // 正文
                            sb.append(line);
                            sb.append("\n");
                        } else {
                            // 元数据
                            metadataSb.append(line);
                            metadataSb.append("\n");
                        }

                        // 分割线
                        if (line.startsWith("---")) {
                            yamlTagCount++;
                        }

                        // 取标题
                        // 正文的第一个#号
                        if (titleEnd == true && hasTitle == false && line.startsWith("#")) {
                            if ("#".equals(line.split(" ")[0])) {
                                postTitle = line.split(" ")[1];
                                hasTitle = true;
                                logger.info("找到了标题：" + postTitle);
                            }
                        }

                        // 修复没有写标题的情况
                        if (yamlTagCount == 2) {
                            // 这里是为了防止解析不到内容
                            titleEnd = true;
                        }
                    }

                    // Yaml yaml = new Yaml();
                    Yaml yaml = new Yaml(new DateTimeImplicitContructor());
                    String input = metadataSb.toString();
                    LinkedHashMap metadatMap = new LinkedHashMap();
                    for (Object data : yaml.loadAll(input)) {
                        if (null != data) {
                            metadatMap = (LinkedHashMap) data;
                            break;
                        }
                    }

                    // 数据组装
                    String postTitleData = null == metadatMap.get("title") ? postTitle : metadatMap.get("title").toString();
                    String postContentData = null;
                    if (hasTitle == false) {
                        logger.info("修复缺失的标题：" + postTitle);
                        StringBuilder contentSb = new StringBuilder();
                        contentSb.append("# ");
                        contentSb.append(postTitleData);
                        contentSb.append("\n");
                        contentSb.append(sb.toString());
                        postContentData = contentSb.toString();
                    } else {
                        postContentData = sb.toString();
                    }

//                    Date cdate =  (Date)metadatMap.getOrDefault("date",new Date());
//                    String datestr = DateUtil.parseDatestr(cdate);
//                    metadatMap.put("date", datestr);

                    HomeData homeData = new HomeData();
                    homeData.setMwebFileId(key);
                    homeData.setPostTitle(postTitleData);
                    homeData.setFrom(null);
                    homeData.setContent(postContentData);
                    homeData.setMetadata(metadatMap);
                    homeData.setSelectedFile(file);

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
