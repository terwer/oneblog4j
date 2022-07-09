package com.terwergreen.controller;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author: terwer
 * @date: 2021/12/25 10:47
 * @description:
 */
public class HomeController implements Initializable {

    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    // =================================
    // 自定义目录
    // private static final String DEFAULT_DIR = "/Users/terwer/Documents/workspace/lg2021/homework";
    // private String NOTE_DIR = "笔记";
    // private String NOTE_IMAGES_DIR = "images";

    // =================================
    // =================================

    // =================================
    // MWeb目录
    private static final String MWEB_DEFAULT_DIR = "/Users/terwer/同步空间/cross/MWeb/MWebLibrary";
    private static final String DEFAULT_DIR = MWEB_DEFAULT_DIR;
    private String MWEB_NOTE_DIR = "";
    private String NOTE_DIR = MWEB_NOTE_DIR;
    private String MWEB_NOTE_IMAGES_DIR = "media";
    private String NOTE_IMAGES_DIR = MWEB_NOTE_IMAGES_DIR;
    // ==================================
    // ==================================

    public String getCurrentDir() {
        return currentDir;
    }

    private String currentDir = DEFAULT_DIR;

    public String getCurrentNoteDir() {
        return currentNoteDir;
    }

    private String currentNoteDir = NOTE_DIR;

    public String getCurrentNoteImagesDir() {
        return Paths.get(getCurrentNoteDir(), NOTE_IMAGES_DIR).toString();
    }

    private String currentNoteImagesDir = NOTE_IMAGES_DIR;


    @FXML
    private Button btnChooseFile;

    @FXML
    private RadioButton btnAuto;

    @FXML
    private ComboBox<String> btnNoteDir;

    @FXML
    private TextArea txtLogTextArea;

    @FXML
    private ListView<KeyValueItem> listNoteList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log(null, "当前目录=>" + getCurrentDir());

        if (btnAuto.isSelected()) {
            // 绑定默笔记目录
            File file = new File(DEFAULT_DIR);
            bindNoteDirData(file.listFiles());
        }
        log(null, "主界面初始化完毕");
    }

    // 注意：构造函数在初始化之前，不能访问控件
    public HomeController() {
        // logger.debug("主界面构造完毕");
    }

    public void chooseFileClicked(ActionEvent event) {
        Stage stage = (Stage) btnChooseFile.getScene().getWindow();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("打开笔记目录");

        // 设置默认目录
        if (!"".equals(DEFAULT_DIR) && btnAuto.isSelected()) {
            directoryChooser.setInitialDirectory(new File(DEFAULT_DIR));
        }

        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            currentDir = file.getAbsolutePath();
            log(null, "当前目录=>" + getCurrentDir());

            File[] noteDirs = file.listFiles();
            bindNoteDirData(noteDirs);
        }
    }

    public void btnNoteDirItemClicked(ActionEvent event) {
        // logger.debug("目录点击事件");
        ComboBox<String> item = (ComboBox<String>) event.getSource();

        if (null == item.getValue()) {
            return;
        }

        Path noteDir = Paths.get(getCurrentDir(), item.getValue(), NOTE_DIR);
        this.currentNoteDir = noteDir.toString();

        if (!noteDir.toFile().exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误信息");
            alert.setHeaderText("未找到笔记目录：" + noteDir.getFileName());

            alert.showAndWait();
            return;
        }

        // 绑定笔记
        File[] notes = noteDir.toFile().listFiles();
        bindNotesData(notes);
    }

    /**
     * 绑定笔记目录
     *
     * @param noteDirs
     */
    private void bindNoteDirData(File[] noteDirs) {
        btnNoteDir.getItems().clear();

        for (int i = 0; i < noteDirs.length; i++) {
            File dir = noteDirs[i];
            if (!dir.isDirectory() || dir.getName().startsWith("$")
                    || dir.getName().startsWith("metadata")
            ) {
                continue;
            }
            // logger.debug(dir.getName());
            btnNoteDir.getItems().addAll(dir.getName());
        }

        if (btnNoteDir.getItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误信息");
            alert.setHeaderText("目录错误");
            log(null, "目录错误：" + getCurrentDir());

            alert.showAndWait();
            return;
        } else {
            // 数据绑定成功
            // 自动选择第一个
            btnNoteDir.getSelectionModel().select(0);
            ActionEvent event = new ActionEvent(btnNoteDir, null);
            btnNoteDirItemClicked(event);
        }

        log(null, "笔记目录绑定数据完成");
    }

    private void bindNotesData(File[] notes) {
        listNoteList.getItems().clear();
        log(null, "当前笔记目录=>" + getCurrentNoteDir());

        for (File noteItem : notes) {
            if (!noteItem.getName().endsWith(".md")) {
                continue;
            }

            String note = noteItem.getName();
            // String noteFile = Paths.get(getCurrentNoteDir(), note).toString();
            // String imageDir = getCurrentNoteImagesDir();

            // 目录完毕
            // log(null, "note=>" + note);
            // log(null,"noteFile " + noteFile);
            // log(null,"note dir " + getCurrentNoteDir());
            // log(null,"note image dir " + getCurrentNoteImagesDir());
            // log(null,"===============================");

            try {
                List<String> allLines = Files.readAllLines(Paths.get(getCurrentNoteDir(), note), StandardCharsets.UTF_8);
                if (CollectionUtils.isNotEmpty(allLines)) {
                    note = allLines.get(0);
                    logger.info("第一行：" + allLines.get(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 开始处理数据绑定
            KeyValueItem<String, String> item = new KeyValueItem<>(noteItem.getName(), note);
            listNoteList.getItems().add(item);
        }
        log(null, "笔记数据绑定成功");
    }


    public void btnPreCheckClicked(ActionEvent event) {

    }

    public void onListNotesClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            KeyValueItem<String, String> currentItemSelected = listNoteList.getSelectionModel()
                    .getSelectedItem();
            String noteFileId = currentItemSelected.getKey();
            // logger.debug("currentItemSelected = " + currentItemSelected);

            HomeData homeData = new HomeData();
            homeData.setMwebFileId(currentItemSelected.getKey());
            homeData.setPostTitle(currentItemSelected.getValue());
            homeData.setFrom(this);

            try {
                FXMLLoader loader = new FXMLLoader(ResourceUtil.getResourceAsURL("write.fxml"));
                Parent root1 = (Parent) loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.setResizable(false);

                // 传值
                WriteController writeController = loader.getController();
                writeController.initData(homeData);

                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearLogClicked(ActionEvent event) {
        txtLogTextArea.clear();
    }

    private void log(Object source, String msg) {
        String logText = "[" + source + "]:" + msg;
        txtLogTextArea.appendText(logText + "\r\n");

        logger.debug(logText);
    }
}
