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
import javafx.scene.layout.Region;
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

    // ??????????????????
    private boolean isPublish = false;
    // ????????????????????????
    private boolean isSync = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<KeyValueItem<BlogTypeEnum, String>> typeList = new ArrayList<>();
        typeList.add(new KeyValueItem<>(BlogTypeEnum.CNBLOGS, "?????????"));
        typeList.add(new KeyValueItem<>(BlogTypeEnum.BUGUCMS, "JVue4"));
        typeList.add(new KeyValueItem<>(BlogTypeEnum.CONFLUENCE_DELEGATE, "Confluence"));
        cmbBlogType.getItems().addAll(typeList);
        // ?????????????????????
        cmbBlogType.getSelectionModel().select(0);
        ActionEvent event = new ActionEvent(cmbBlogType, null);
        cmbBlogTypeItemClicked(event);

        txtWriteContent.textProperty().addListener((observableValue, oldValue, newValue) -> {
            String content = txtWriteContent.getText();

            WebEngine engine = webPreview.getEngine();
            reloadPreview(engine, content);

            logger.debug("???????????????");
            // logger.debug("????????????");
        });

//        txtWriteContent.addEventFilter(KEY_TYPED, event -> {
//            final String s = event.getCharacter();
//            char c = s.charAt(0);
//            if (c >= '\u0021' && c <= '\u007E') {
//                logger.debug("????????????");
//                event.consume();
//            }
//        });

        // ????????????
        isPublish = false;
        lblPublishStatus.setTextFill(Color.color(1, 0, 0));
        isSync = false;
        lblSyncStatus.setTextFill(Color.color(1, 0, 0));
    }

    @FXML
    private void cmbBlogTypeItemClicked(ActionEvent event) {
        // logger.debug("??????????????????");
        ComboBox<KeyValueItem<BlogTypeEnum, String>> item = (ComboBox<KeyValueItem<BlogTypeEnum, String>>) event.getSource();
        if (null == item.getValue()) {
            return;
        }

        lblBlogPlantform.setText("[" + item.getSelectionModel().getSelectedItem().getValue() + "]");
        logger.info("???????????????????????????" + item.getSelectionModel().getSelectedItem().getValue());
    }

    public void initData(HomeData homeData) {
        this.homeData = homeData;
        if (null != homeData.getFrom()) {
            txtPostTitle.setText(homeData.getMwebFileId() + "|" + homeData.getPostTitle());
        } else {
            txtPostTitle.setText(homeData.getPostTitle());
        }

        if (null != homeData.getMetadata()) {
            // ?????????????????????
            setMetadata(homeData.getMetadata());
        }

        loadPost(homeData);
        // logger.debug(homeData.getFrom().getCurrentNoteDir());
    }

    /**
     * {
     * title=node????????????,
     * date=Fri Jul 08 00:09:53 CST 2022,
     * permalink=/post/node-send-mail.html,
     * meta=[{name=keywords, content=node mail}, {name=description, content=node???????????????}],
     * categories=[????????????],
     * tags=[node, mail],
     * author={name=terwer, link=https://github.com/terwer}
     * }
     *
     * @param metadata
     */
    private void setMetadata(LinkedHashMap metadata) {
        logger.info("??????metadata=>" + metadata);

        // ??????
        if (null != metadata.get("categories")) {
            ArrayList<String> categories = (ArrayList<String>) metadata.get("categories");
            txtCat.setText(StringUtils.join(categories, "_"));
        }

        // ??????
        if (null != metadata.get("tags")) {
            ArrayList<String> tags = (ArrayList<String>) metadata.get("tags");
            txtTag.setText(StringUtils.join(tags, "_"));
        }

        // ??????
        String permalink = (String) metadata.get("permalink");
        permalink = permalink.replace("/post/", "");
        permalink = permalink.replace(".html", "");
        // permalink = permalink.replace("/pages/", "");
        // permalink = permalink.replace("/", "");
        txtSlug.setText(permalink);

        // ??????
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
            // ?????????????????????
            content = content.replace("images/", homeData.getFrom().getCurrentNoteImagesDir() + "/");

            // ??????MWeb
            content = content.replace("media/", homeData.getFrom().getCurrentNoteDir() + "/media/");
        }

        // logger.debug("content = " + content);

        engine.executeScript("if(typeof setEditorValue != 'undefined'){window.editorContentMD='" + content + "';setEditorValue(window.editorContentMD);}");

        lblRightStatus.setText("ready");
    }

    public void contentClicked(MouseEvent mouseEvent) {
        // ??????????????????
    }

    /**
     * ?????????
     *
     * @param newStr ?????????
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
            logger.debug("???????????????????????????PicGO");
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
                String hash = String.valueOf(System.currentTimeMillis());
                successMsg("??????=>" + hash.substring(8));
                logger.info("result=>", result);
            } else {
                String hash = String.valueOf(System.currentTimeMillis());
                successMsg("??????=>" + hash.substring(8));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("????????????");
                alert.setHeaderText("??????????????????=>" + result.getContent());
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
            alert.setTitle("????????????");
            alert.setHeaderText("????????????????????????");
            alert.showAndWait();
            return;
        }
    }

    public void btnPublish(ActionEvent event) {
        String content = txtWriteContent.getText();
        // ???????????????
        String[] keywords = StringUtils.isEmpty(txtTag.getText()) ? null : txtTag.getText().split("_");
        String description = txtDesc.getText();
        String[] cats = StringUtils.isEmpty(txtTag.getText()) ? null : txtCat.getText().split("_");
        LinkedHashMap<String, Object> data = YamlUtil.buildMetaDataMap(txtPostTitle.getText().toLowerCase(), txtSlug.getText(), keywords, description, cats, null);
        String metadata = YamlUtil.generateMetadata(data);
        // logger.info("new metadata=>" + metadata);
        StringBuilder sb = new StringBuilder();
        sb.append(metadata);
        sb.append("\n");
        sb.append(content);

        content = sb.toString();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("??????????????????");
        alert.setHeaderText("?????????????????????");
        alert.setContentText("???????????????\r\n??????[" + txtPostTitle.getText() + "]\r\n" +
                "??????[" + StringUtils.join(cats, ",") + "]\r\n" +
                "??????[" + StringUtils.join(keywords, ",") + "]\r\n"
        );

        // ButtonType show = new ButtonType("?????????");
        // alert.getButtonTypes().add(show);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) {
            logger.debug("null");
        } else if (option.get() == ButtonType.OK) {
            if (!isSync) {
                Alert noSyncAlert = new Alert(Alert.AlertType.ERROR, "????????????????????????????????????????????????", ButtonType.OK);
                noSyncAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                noSyncAlert.show();
                return;
            }

            BlogTypeEnum blogType = BlogTypeEnum.CNBLOGS;
            KeyValueItem<BlogTypeEnum, String> selectedItem = cmbBlogType.getSelectionModel().getSelectedItem();
            blogType = selectedItem.getKey();
            BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(blogType);
            Map<String, Object> mappedParams = new HashMap<>();
            // blogHelper.newPost(mappedParams);
            logger.debug("???????????????");
        } else if (option.get() == ButtonType.CANCEL) {
            logger.debug("???????????????");
        }
        // else if (option.get() == show) {
        //     logger.debug("?????????");
        // }
        else {
            logger.debug("?????????");
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
            // ???????????????
            String[] keywords = StringUtils.isEmpty(txtTag.getText()) ? null : txtTag.getText().split("_");
            String description = txtDesc.getText();
            String[] cats = StringUtils.isEmpty(txtTag.getText()) ? null : txtCat.getText().split("_");
            String oldDatestr = DateUtil.parseDatestr(homeData.getMetadata().get("date"));
            LinkedHashMap<String, Object> data = YamlUtil.buildMetaDataMap(txtPostTitle.getText().toLowerCase(), txtSlug.getText(), keywords, description, cats, oldDatestr);
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
            successMsg("??????=>" + hash.substring(8));
        } catch (Exception e) {
            errorMsg(e.getMessage());

            e.printStackTrace();
        }

        logger.debug("?????????????????????" + notePath);
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
            alert.setTitle("???????????????");
            alert.setHeaderText("??????????????????????????????");
            alert.setContentText("????????????????????????????????????????????????????????????????????????");
            ButtonType showSlug = new ButtonType("???????????????");
            alert.getButtonTypes().add(showSlug);
            ButtonType showDesc = new ButtonType("???????????????");
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
        logger.info("???????????????????????????");

        LinkedHashMap<String, Object> data = YamlUtil.autoBuildMetaDataMap(txtPostTitle.getText(),
                txtWriteContent.getText(), txtTag.getText().split("_"),
                txtCat.getText().split("_"),
                oldSlug, oldDesc, oldDatestr
        );
        this.setMetadata(data);

        // String metadata = YamlUtil.generateMetadata(data);
        // logger.info("new metadata=>" + metadata);
        logger.info("?????????????????????");
        String hash = String.valueOf(System.currentTimeMillis());
        successMsg("??????=>" + hash.substring(8));
    }

    public void refreshPublishStatus(ActionEvent event) {
        logger.info("????????????????????????");

        isSync = true;
        lblSyncStatus.setText("?????????");
        lblSyncStatus.setTextFill(Color.color(32 / 255f, 168 / 255f, 92 / 255f));
    }
}
