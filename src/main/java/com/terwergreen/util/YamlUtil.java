package com.terwergreen.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.slugify.Slugify;
import com.terwergreen.constant.Constants;
import com.terwergreen.util.http.HttpClientResult;
import javafx.scene.control.Alert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Yaml工具类
 *
 * @name: YamlUtil
 * @author: terwer
 * @date: 2022-07-09 14:04
 **/
public class YamlUtil {
    private static Logger logger = LoggerFactory.getLogger(YamlUtil.class);
    private static final Slugify slg = new Slugify();

    /**
     * 生成元数据Map
     *
     * @param postTitle
     * @param postSlug
     * @param keywords
     * @param description
     * @param cats
     * @param date
     * @return
     */
    public static LinkedHashMap<String, Object> buildMetaDataMap(String postTitle, String postSlug, String[] keywords, String description, String[] cats, String datestr) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_CN_24);
        String now = sdf.format(new Date());
        if (null != datestr) {
            now = datestr;
        }

        // 数据定义
        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("title", postTitle);
        try {
            data.put("date", sdf.parse(now));
        } catch (ParseException e) {
            try {
                SimpleDateFormat sdfShort = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT_CN_24);
                data.put("date", sdfShort.parse(now));
            } catch (Exception e2) {
                logger.error("日期转换错误", e);
                throw new RuntimeException(e);
            }
        }
        data.put("permalink", "/post/" + postSlug + ".html");

        // meta
        LinkedHashMap keywordsMap = new LinkedHashMap();
        keywordsMap.put("name", "keywords");
        keywordsMap.put("content", StringUtils.join(keywords, " "));
        LinkedHashMap descMap = new LinkedHashMap();
        descMap.put("name", "description");
        descMap.put("content", description);
        ArrayList metaList = new ArrayList();
        metaList.add(keywordsMap);
        metaList.add(descMap);
        data.put("meta", metaList);

        // categories
        if (null != cats) {
            data.put("categories", new ArrayList<>(Arrays.asList(cats)));
        }

        // tags
        if (null != keywords) {
            data.put("tags", new ArrayList<>(Arrays.asList(keywords)));
        }

        // author
        LinkedHashMap author1 = new LinkedHashMap();
        author1.put("name", "terwer");
        author1.put("link", "https://github.com/terwer");
        data.put("author", author1);

        return data;
    }

    /**
     * 构建元数据，自动生成slug与摘要
     *
     * @param postTitle
     * @param postContent
     * @param keywords
     * @param cats
     * @param oldSlug
     * @param oldDesc
     * @param oldDate
     * @return
     */
    public static LinkedHashMap<String, Object> autoBuildMetaDataMap(String postTitle, String postContent, String[] keywords, String[] cats, String oldSlug, String oldDesc, String oldDatestr) {
        String slug = null;
        String desc = null;

        if (null == oldSlug) {
            String q = null;
            try {
                q = URLEncoder.encode(postTitle, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            String reqUrl = "https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=auto&tl=en-US&q=" + q;
            logger.error("google reqUrl=>" + reqUrl);
            HttpClientResult result = HttpUtil.doGet(reqUrl);
            if (200 == result.getCode()) {
                logger.info("result=>", result);
                JSONArray resultArray = JSON.parseArray(result.getContent());
                JSONArray transInfo = resultArray.getJSONArray(0);
                String transText = transInfo.getString(0);

                final String newSlug = slg.slugify(transText);
                slug = newSlug;
                System.out.println("新的别名=>" + newSlug);
            } else {
                slug = oldSlug;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误信息");
                alert.setHeaderText("谷歌翻译服务出错=>" + result.getContent());
                alert.showAndWait();
            }
        } else {
            slug = oldSlug;
        }

        // markdown转换为html
        String html = MarkdownUtil.md2html(postContent);
        if (null == oldDesc) {
            // 截取摘要
            String filteredHtml = HtmlUtil.parseHtml(html, Constants.MAX_PREVIEW_LENGTH);
            desc = filteredHtml.replace("...", "");
        } else {
            desc = oldDesc;
        }

        // 解析图片
        List<String> thumbnails = ImageUtil.getImgSrc(html);
        for (String thumbnail : thumbnails) {
            logger.info("提取到缩略图=>" + thumbnail);
        }

        return buildMetaDataMap(postTitle, slug, keywords, desc, cats, oldDatestr);
    }

    /**
     * 根据Map生成yaml字符串
     *
     * @param data
     * @return
     */
    public static String generateMetadata(LinkedHashMap<String, Object> data) {
        // 导出选项
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndicatorIndent(2);
        options.setIndentWithIndicator(true);
        options.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // options.setTimeZone(TimeZone.getTimeZone("UTC"));

        // 数据导出
        Yaml yaml = new Yaml(options);
        StringBuilder yamlSb = new StringBuilder();
        yamlSb.append("---");
        yamlSb.append("\n");
        String output = yaml.dump(data);
        yamlSb.append(output);
        yamlSb.append("---");

        // 先转成小写，防止错误替换
        String result = yamlSb.toString()
                .replaceAll("T", " ")
                .replace("+08:00", "");

        return result;
    }
}
