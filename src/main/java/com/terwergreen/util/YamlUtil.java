package com.terwergreen.util;

import com.terwergreen.constant.Constants;
import com.terwergreen.controller.WriteController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Yaml工具类
 *
 * @name: YamlUtil
 * @author: terwer
 * @date: 2022-07-09 14:04
 **/
public class YamlUtil {
    private static Logger logger = LoggerFactory.getLogger(YamlUtil.class);

    /**
     * 生成元数据Map
     *
     * @param postTitle
     * @param postSlug
     * @param keywords
     * @param description
     * @return
     */
    public static LinkedHashMap<String, Object> buildMetaDataMap(String postTitle, String postSlug, String[] keywords, String description, String[] cats) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String now = sdf.format(new Date());

        // 数据定义
        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("title", postTitle);
        data.put("date", now);
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
     * @return
     */
    public static LinkedHashMap<String, Object> autoBuildMetaDataMap(String postTitle, String postContent, String[] keywords, String[] cats, String oldSlug, String oldDesc) {
        String slug = null;
        String desc = null;

        if (null == oldSlug) {

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

        return buildMetaDataMap(postTitle, slug, keywords, desc, cats);
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

        // 数据导出
        Yaml yaml = new Yaml(options);
        StringBuilder yamlSb = new StringBuilder();
        yamlSb.append("---");
        yamlSb.append("\n");
        String output = yaml.dump(data);
        yamlSb.append(output);
        yamlSb.append("---");

        return yamlSb.toString();
    }
}
