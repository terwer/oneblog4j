package com.terwergreen.helper;

import com.alibaba.fastjson.JSONObject;
import com.terwergreen.model.Post;
import com.terwergreen.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author: terwer
 * @date: 2022/1/9 18:55
 * @description: BuguCMSHelper
 */
public class BuguCMSApiHelper extends BlogHelper {

    private static Logger logger = LoggerFactory.getLogger(BuguCMSApiHelper.class);

    public BuguCMSApiHelper(Properties blogProps) {
        super((String) blogProps.get("blog.jsonapi.bugucms.baseUrl"), "", "");
    }

    @Override
    public Map<String, Object> getUsersBlogs() {
        List<String> pParams = new ArrayList<>();

        // /site/config
        String url = this.getServerUrl() + "/site/config";
        String resultString = HttpUtil.get(url);

        JSONObject jsonObject = JSONObject.parseObject(resultString);
        //json对象转Map
        Map<String, Object> reaultMap = (Map<String, Object>) jsonObject;

        logger.debug("blogger.getUsersBlogs=>");
        return reaultMap;
    }

    @Override
    public boolean newPost(Map<String, Object> mappedParams) {
        logger.debug("BuguCMS addPost");
        return false;
    }

    @Override
    public boolean editPost(Map<String, Object> mappedParams) {
        logger.debug("BuguCMS updatePost");
        return false;
    }

    @Override
    public <T> T getPost(Map<String, Object> mappedParams) {
        return null;
    }

    @Override
    public <T> List<T> getRecentPosts(Map<String, Object> mappedParams) {
        return null;
    }

    @Override
    public <T> List<T> getCategories(Map<String, Object> mappedParams) {
        return null;
    }

    @Override
    public boolean newMediaObject(Map<String, Object> mappedParams) {
        return false;
    }
}
