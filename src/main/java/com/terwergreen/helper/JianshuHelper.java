package com.terwergreen.helper;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 简书文章API接口
 *
 * @name: JianshuHelper
 * @author: terwer
 * @date: 2022-01-20 16:06
 **/
public class JianshuHelper extends BlogHelper {
    public JianshuHelper(Properties blogProps) {
        super((String) blogProps.get("blog.meteweblog.jianshu.serverUrl"), (String) blogProps.get("blog.meteweblog.jianshu.username"), (String) blogProps.get("blog.meteweblog.jianshu.password"));
    }

    @Override
    public boolean newPost(Map<String, Object> mappedParams) {
        return false;
    }

    @Override
    public boolean editPost(Map<String, Object> mappedParams) {
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
    public <T> T getCategories(Map<String, Object> mappedParams) {
        return null;
    }

    @Override
    public boolean newMediaObject(Map<String, Object> mappedParams) {
        return false;
    }
}
