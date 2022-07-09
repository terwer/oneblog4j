package com.terwergreen.util;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Yaml工具类
 *
 * @name: YamlUtil
 * @author: terwer
 * @date: 2022-07-09 14:04
 **/
public class YamlUtil {
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
        Map keywordsMap = new HashMap();
        keywordsMap.put("name", "keywords");
        keywordsMap.put("content", StringUtils.join(keywords, " "));
        Map descMap = new HashMap();
        descMap.put("name", "description");
        descMap.put("content", description);
        data.put("meta", new Map[]{keywordsMap, descMap});

        // categories
        data.put("categories", cats);

        // tags
        data.put("tags", keywords);

        // author
        Map author1 = new HashMap();
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
     * @return
     */
    public static LinkedHashMap<String, Object> autoBuildMetaDataMap(String postTitle, String postContent) {
        return buildMetaDataMap(postTitle, postTitle, null, null, null);
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
