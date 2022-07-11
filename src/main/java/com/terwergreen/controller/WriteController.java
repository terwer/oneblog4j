package com.terwergreen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.terwergreen.helper.BlogHelper;
import com.terwergreen.helper.BlogHelperFactory;
import com.terwergreen.helper.BlogTypeEnum;
import com.terwergreen.model.control.KeyValueItem;
import com.terwergreen.model.data.HomeData;
import com.terwergreen.util.DateUtil;
import com.terwergreen.util.HttpUtil;
import com.terwergreen.util.ResourceUtil;
import com.terwergreen.util.YamlUtil;
import com.terwergreen.util.http.HttpClientResult;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private ComboBox<KeyValueItem<BlogTypeEnum, String>> cmbBlogType;

    @FXML
    private Label lblMsg;

    @FXML
    private Label lblLeftStatus;

    @FXML
    private Label lblRightStatus;

    @FXML
    private TextArea txtCat;

    @FXML
    private TextArea txtTag;

    @FXML
    private TextArea txtDesc;

    @FXML
    private TextArea txtSlug;

    @FXML
    private Button btnMetadata;

    @FXML
    private Label lblBlogPlantform;

    @FXML
    Label lblPublishStatus;

    @FXML
    private Label lblSyncStatus;

    // 文章是否发布
    private boolean isPublish = false;
    // 是否同步文章信息
    private boolean isSync = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<KeyValueItem<BlogTypeEnum, String>> typeList = new ArrayList<>();
        typeList.add(new KeyValueItem<>(BlogTypeEnum.CNBLOGS, "博客园"));
        typeList.add(new KeyValueItem<>(BlogTypeEnum.BUGUCMS, "JVue4"));
        typeList.add(new KeyValueItem<>(BlogTypeEnum.CONFLUENCE_DELEGATE, "Confluence"));
        cmbBlogType.getItems().addAll(typeList);
        // 自动选择第一个
        cmbBlogType.getSelectionModel().select(0);
        ActionEvent event = new ActionEvent(cmbBlogType, null);
        cmbBlogTypeItemClicked(event);

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

        // 默认状态
        isPublish = false;
        lblPublishStatus.setTextFill(Color.color(1, 0, 0));
        isSync = false;
        lblSyncStatus.setTextFill(Color.color(1, 0, 0));
    }

    @FXML
    private void cmbBlogTypeItemClicked(ActionEvent event) {
        // logger.debug("目录点击事件");
        ComboBox<KeyValueItem<BlogTypeEnum, String>> item = (ComboBox<KeyValueItem<BlogTypeEnum, String>>) event.getSource();
        if (null == item.getValue()) {
            return;
        }

        lblBlogPlantform.setText("[" + item.getSelectionModel().getSelectedItem().getValue() + "]");
        logger.info("当前选择的平台是：" + item.getSelectionModel().getSelectedItem().getValue());
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

        // 分类
        if (null != metadata.get("categories")) {
            ArrayList<String> categories = (ArrayList<String>) metadata.get("categories");
            txtCat.setText(StringUtils.join(categories, "_"));
        }

        // 标签
        if (null != metadata.get("tags")) {
            ArrayList<String> tags = (ArrayList<String>) metadata.get("tags");
            txtTag.setText(StringUtils.join(tags, "_"));
        }

        // 别名
        String permalink = (String) metadata.get("permalink");
        permalink = permalink.replace("/post/", "");
        permalink = permalink.replace(".html", "");
        // permalink = permalink.replace("/pages/", "");
        // permalink = permalink.replace("/", "");
        txtSlug.setText(permalink);

        // 描述
        String desc = null;
        ArrayList<LinkedHashMap<String, String>> meta = (ArrayList<LinkedHashMap<String, String>>) metadata.get("meta");
        if (null != meta) {
            for (LinkedHashMap<String, String> m : meta) {
                if (null != m && null != m.get("name") && "description".equals(m.get("name"))) {
                    desc = m.get("content");
                    break;
                }
            }
            txtDesc.setText(desc);
        }


        lblLeftStatus.setText("ok");
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
            logger.debug("处理粘贴图片，使用PicGO");
            HttpClientResult result = HttpUtil.doPost("http://127.0.0.1:36677/upload");
            if (200 == result.getCode()) {
                JSONObject jsonObj = JSON.parseObject(result.getContent());
                if (jsonObj.getBoolean("success")) {
                    JSONArray imgArr = jsonObj.getJSONArray("result");
                    for (Object img : imgArr) {
                        String imgUrl = (String) img;
                        int caretPosition = txtWriteContent.getCaretPosition();
                        txtWriteContent.insertText(caretPosition, String.format("![](%s)", imgUrl));
                    }
                }
                logger.info("result=>", result);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误信息");
                alert.setHeaderText("图片上传错误=>" + result.getContent());
                alert.showAndWait();
                return;
            }

//            Image img = cb.getImage();
//            // imageView.setImage(image);
//
//            java.util.Date dt = new java.util.Date(System.currentTimeMillis());
//            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//            String fileName = fmt.format(dt);
//            Random rnd = new Random(MAX_IMAGE_NUM);
//            fileName = fileName + rnd.nextInt() + ".png";
//
//            String fileShortName = "oneblog-image-" + fileName;
//            String outPath = homeData.getFrom().getCurrentNoteImagesDir() + "/" +
//                    homeData.getMwebFileId().replace(".md", "") + "/"
//                    + fileShortName;
//            File file = new File(outPath);
//            try {
//                ImageIO.write(SwingFXUtils.fromFXImage(img, null), "PNG", file);
//
//                int caretPosition = txtWriteContent.getCaretPosition();
//                txtWriteContent.insertText(caretPosition, "![](media/" +
//                        homeData.getMwebFileId().replace(".md", "") + "/"
//                        + fileShortName + ")");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误信息");
            alert.setHeaderText("剪贴板中没有图片");
            alert.showAndWait();
            return;
        }
    }

    public void btnPublish(ActionEvent event) {
        BlogTypeEnum blogType = BlogTypeEnum.CNBLOGS;

        KeyValueItem<BlogTypeEnum, String> selectedItem = cmbBlogType.getSelectionModel().getSelectedItem();
        blogType = selectedItem.getKey();

        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(blogType);

        Map<String, Object> mappedParams = new HashMap<>();

        String content = txtWriteContent.getText();
        // 设置元数据
        String[] keywords = StringUtils.isEmpty(txtTag.getText()) ? null : txtTag.getText().split("_");
        String description = txtDesc.getText();
        String[] cats = StringUtils.isEmpty(txtTag.getText()) ? null : txtCat.getText().split("_");
        LinkedHashMap<String, Object> data = YamlUtil.buildMetaDataMap(txtPostTitle.getText(), txtSlug.getText(), keywords, description, cats, null);
        String metadata = YamlUtil.generateMetadata(data);
        // logger.info("new metadata=>" + metadata);
        StringBuilder sb = new StringBuilder();
        sb.append(metadata);
        sb.append("\n");
        sb.append(content);

        content = sb.toString();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("文章发布确认");
        alert.setHeaderText("是否发布文章？");
        alert.setContentText("文章信息：\r\n标题[" + txtPostTitle.getText() + "]\r\n" +
                "分类[" + StringUtils.join(cats, ",") + "]\r\n" +
                "标签[" + StringUtils.join(keywords, ",") + "]\r\n"
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

    public void saveLocal(ActionEvent event) throws Exception {
        String content = txtWriteContent.getText();

        String notePath = null;
        if (null != homeData.getFrom()) {
            String noteDir = homeData.getFrom().getCurrentNoteDir();
            notePath = Paths.get(noteDir, "/" + homeData.getMwebFileId()).toString();
        } else {
            notePath = homeData.getSelectedFile().getAbsolutePath();
            // 设置元数据
            String[] keywords = StringUtils.isEmpty(txtTag.getText()) ? null : txtTag.getText().split("_");
            String description = txtDesc.getText();
            String[] cats = StringUtils.isEmpty(txtTag.getText()) ? null : txtCat.getText().split("_");
            String oldDatestr = DateUtil.parseDatestr(homeData.getMetadata().get("date"));
            LinkedHashMap<String, Object> data = YamlUtil.buildMetaDataMap(txtPostTitle.getText(), txtSlug.getText(), keywords, description, cats, oldDatestr);
            String metadata = YamlUtil.generateMetadata(data);
            // logger.info("new metadata=>" + metadata);
            StringBuilder sb = new StringBuilder();
            sb.append(metadata);
            sb.append("\n");
            sb.append(content);

            content = sb.toString();
        }

        try {
            writeTxtFile(notePath, content);

            String hash = String.valueOf(System.currentTimeMillis());
            successMsg("成功=>" + hash.substring(8));
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

    public void createMetadata(ActionEvent event) throws Exception {
        String oldDatestr = DateUtil.parseDatestr(homeData.getMetadata().get("date"));
        if (txtSlug.getText().contains("pages")) {
            doCreateMetadata(null, null, oldDatestr);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("元数据确认");
            alert.setHeaderText("是否重新生成元数据？");
            alert.setContentText("重新生成会覆盖现有的所有元数据信息，请谨慎操作。");
            ButtonType showSlug = new ButtonType("仅生成别名");
            alert.getButtonTypes().add(showSlug);
            ButtonType showDesc = new ButtonType("仅生成备注");
            alert.getButtonTypes().add(showDesc);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                doCreateMetadata(null, null, oldDatestr);
            } else if (option.get() == showSlug) {
                doCreateMetadata(null, txtDesc.getText(), oldDatestr);
            } else if (option.get() == showDesc) {
                doCreateMetadata(txtSlug.getText(), null, oldDatestr);
            }
        }
    }

    private void doCreateMetadata(String oldSlug, String oldDesc, String oldDatestr) {
        logger.info("准备重新生成元数据");

        LinkedHashMap<String, Object> data = YamlUtil.autoBuildMetaDataMap(txtPostTitle.getText(),
                txtWriteContent.getText(), txtTag.getText().split("_"),
                txtCat.getText().split("_"),
                oldSlug, oldDesc, oldDatestr
        );
        this.setMetadata(data);

        // String metadata = YamlUtil.generateMetadata(data);
        // logger.info("new metadata=>" + metadata);
        logger.info("创建元数据完毕");
    }

    public void refreshPublishStatus(ActionEvent event) {
        logger.info("开始刷新发布状态");

        isSync = true;
        lblSyncStatus.setText("已同步");
        lblSyncStatus.setTextFill(Color.color(32 / 255f, 168 / 255f, 92 / 255f));
    }
}
