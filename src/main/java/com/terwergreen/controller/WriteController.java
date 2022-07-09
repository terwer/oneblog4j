package com.terwergreen.controller;

import com.terwergreen.helper.BlogHelper;
import com.terwergreen.helper.BlogHelperFactory;
import com.terwergreen.helper.BlogTypeEnum;
import com.terwergreen.model.data.HomeData;
import com.terwergreen.util.ResourceUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * @author: terwer
 * @date: 2022/1/9 14:34
 * @description: WriteController
 */
public class WriteController implements Initializable {

    private static Logger logger = LoggerFactory.getLogger(WriteController.class);

    private static final int MAX_IMAGE_NUM = 10000;
    private HomeData homeData;

    @Deprecated
    @FXML
    private Label lblPostTitle;

    @FXML
    private TextField txtPostTitle;

    @FXML
    private TextArea txtWriteContent;

    @FXML
    private WebView webPreview;

    @FXML
    private Button btnPastePic;

    @FXML
    private TitledPane txtBlogType;

    @FXML
    private RadioButton rdCnblogs;

    @FXML
    private RadioButton rdBuguCMS;

    @FXML
    private Label lblMsg;

    @FXML
    private Label lblRightStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup group = new ToggleGroup();
        rdBuguCMS.setToggleGroup(group);
        rdCnblogs.setToggleGroup(group);

        txtWriteContent.textProperty().addListener((observableValue, oldValue, newValue) -> {
            String content = txtWriteContent.getText();

            WebEngine engine = webPreview.getEngine();
            reloadPreview(engine, content);

            logger.debug("内容已同步");
            // logger.debug("文字改变");
        });

//        txtWriteContent.addEventFilter(KEY_TYPED, event -> {
//            final String s = event.getCharacter();
//            char c = s.charAt(0);
//            if (c >= '\u0021' && c <= '\u007E') {
//                logger.debug("接收粘贴");
//                event.consume();
//            }
//        });
    }

    public void initData(HomeData homeData) {
        this.homeData = homeData;
        if (null != homeData.getFrom()) {
            txtPostTitle.setText(homeData.getMwebFileId() + "|" + homeData.getPostTitle());
        } else {
            txtPostTitle.setText(homeData.getPostTitle());
        }

        if (null != homeData.getMetadata()) {
            // 设置文章元数据
            setMetadata(homeData.getMetadata());
        }

        loadPost(homeData);
        // logger.debug(homeData.getFrom().getCurrentNoteDir());
    }

    /**
     * {
     * title=node发送邮件,
     * date=Fri Jul 08 00:09:53 CST 2022,
     * permalink=/post/node-send-mail.html,
     * meta=[{name=keywords, content=node mail}, {name=description, content=node发送邮件。}],
     * categories=[前端开发],
     * tags=[node, mail],
     * author={name=terwer, link=https://github.com/terwer}
     * }
     *
     * @param metadata
     */
    private void setMetadata(LinkedHashMap metadata) {
        logger.info("解析metadata=>" + metadata);
    }

    private void loadPost(HomeData homeData) {
        try {
            String content = "";
            if (null != homeData.getFrom()) {
                String postPath = Paths.get(homeData.getFrom().getCurrentNoteDir(), "/", homeData.getMwebFileId()).toString();
                // File file = new File(postPath);
                FileInputStream inputStream = new FileInputStream(postPath);
                content = ResourceUtil.readStream(inputStream);
            } else {
                content = homeData.getContent();
            }

            txtWriteContent.setText(content);

            WebEngine engine = webPreview.getEngine();
            // engine.loadContent(content,"text/html");

            URL url = ResourceUtil.getResourceAsURL("editor.html");
            engine.load(url.toExternalForm());

            // Document document = engine.getDocument();
            // Element mdEditor = document.getElementById("mdEditor");
            // mdEditor.setTextContent(content);

            String finalContent = content;
            engine.getLoadWorker().stateProperty().addListener((new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                    reloadPreview(engine, finalContent);
                }
            }));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void reloadPreview(WebEngine engine, String content) {
        content = content.replace("'", "@@");
        content = content.replace(System.getProperty("line.separator"), "\\n");
        content = content.replace("\n", "\\n");
        content = content.replace("\r", "\\r");

        if (null != homeData.getFrom()) {
            // 兼容自定义目录
            content = content.replace("images/", homeData.getFrom().getCurrentNoteImagesDir() + "/");

            // 兼容MWeb
            content = content.replace("media/", homeData.getFrom().getCurrentNoteDir() + "/media/");
        }

        // logger.debug("content = " + content);

        engine.executeScript("if(typeof setEditorValue != 'undefined'){window.editorContentMD='" + content + "';setEditorValue(window.editorContentMD);}");

        lblRightStatus.setText("ready");
    }

    public void contentClicked(MouseEvent mouseEvent) {
        // 文字改变触发
    }

    /**
     * 写文件
     *
     * @param newStr 新内容
     * @throws IOException
     */
    public static void writeTxtFile(String filename, String newStr) throws IOException {
        File f = new File(filename);
        FileOutputStream fos1 = new FileOutputStream(f);
        OutputStreamWriter dos1 = new OutputStreamWriter(fos1);
        dos1.write(newStr);
        dos1.close();
    }

    public void pastePic(ActionEvent event) {
        Clipboard cb = Clipboard.getSystemClipboard();
        if (cb.hasImage()) {
            logger.debug("处理粘贴图片");

            Image img = cb.getImage();
            // imageView.setImage(image);

            java.util.Date dt = new java.util.Date(System.currentTimeMillis());
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String fileName = fmt.format(dt);
            Random rnd = new Random(MAX_IMAGE_NUM);
            fileName = fileName + rnd.nextInt() + ".png";

            String fileShortName = "oneblog-image-" + fileName;
            String outPath = homeData.getFrom().getCurrentNoteImagesDir() + "/" +
                    homeData.getMwebFileId().replace(".md", "") + "/"
                    + fileShortName;
            File file = new File(outPath);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(img, null), "PNG", file);

                int caretPosition = txtWriteContent.getCaretPosition();
                txtWriteContent.insertText(caretPosition, "![](media/" +
                        homeData.getMwebFileId().replace(".md", "") + "/"
                        + fileShortName + ")");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnPublish(ActionEvent event) {
        BlogTypeEnum blogType = BlogTypeEnum.CNBLOGS;
        if (rdCnblogs.isSelected()) {
            blogType = BlogTypeEnum.CNBLOGS;
        } else {
            blogType = BlogTypeEnum.BUGUCMS;
        }
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(blogType);

        Map<String, Object> mappedParams = new HashMap<>();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("文章发布确认");
        alert.setHeaderText("是否发布文章？");
        alert.setContentText("文章信息：标题[" + txtPostTitle.getText() + "]\r\n" +
                "分类[" + "]\r\n" +
                "标签[" + "]\r\n"
        );

        ButtonType show = new ButtonType("再看看");
        alert.getButtonTypes().add(show);

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
            logger.debug("null");
        } else if (option.get() == ButtonType.OK) {

            blogHelper.newPost(mappedParams);
            logger.debug("文章已发布");

            logger.debug("ok");
        } else if (option.get() == ButtonType.CANCEL) {
            logger.debug("cancel");
        } else if (option.get() == show) {
            logger.debug("show");
        } else {
            logger.debug("else");
        }
    }

    public void saveLocal(ActionEvent event) {
        String content = txtWriteContent.getText();

        String noteDir = homeData.getFrom().getCurrentNoteDir();
        String notePath = Paths.get(noteDir, "/" + homeData.getMwebFileId()).toString();

        try {
            writeTxtFile(notePath, content);

            successMsg("保存成功：" + System.currentTimeMillis());
        } catch (Exception e) {
            errorMsg(e.getMessage());

            e.printStackTrace();
        }

        logger.debug("文件已保存到：" + notePath);
    }

    private void successMsg(String msg) {
        lblMsg.setText(msg);
        lblMsg.setTextFill(Color.color(32 / 255f, 168 / 255f, 92 / 255f));
    }


    private void errorMsg(String msg) {
        lblMsg.setText(msg);
        lblMsg.setTextFill(Color.color(1, 0, 0));
    }
}
