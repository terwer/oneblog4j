package com.terwergreen.helper;

import com.terwergreen.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author: terwer
 * @date: 2022/1/8 23:59
 * @description: CnblogsHelper
 */
public class CnblogsHelper extends BlogHelper {

    private static Logger logger = LoggerFactory.getLogger(CnblogsHelper.class);

    public CnblogsHelper(Properties blogProps) {
        super((String) blogProps.get("blog.meteweblog.cnblogs.serverUrl"), (String) blogProps.get("blog.meteweblog.cnblogs.username"), (String) blogProps.get("blog.meteweblog.cnblogs.password"));
    }

    @Override
    public Map<String, Object> getUsersBlogs() {
        return super.getUsersBlogs();
    }

    @Override
    public boolean newPost(Map<String, Object> mappedParams) {
        List<String> pParams = new ArrayList<>();
        pParams.add("default");
        pParams.add(super.getUsername());
        pParams.add(super.getPassword());

        Object result = super.executeMeteweblog("blogger.getUsersBlogs", pParams);

        logger.debug("CnBlogs add Post:" + result);
        return false;
    }

    @Override
    public boolean editPost(Map<String, Object> mappedParams) {
        logger.debug("CnBlogs update Post");
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
