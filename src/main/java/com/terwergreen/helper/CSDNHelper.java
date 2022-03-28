package com.terwergreen.helper;

import com.terwergreen.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * CSDN文章API接口
 *
 * @name: CSDNHelper
 * @author: terwer
 * @date: 2022-01-20 16:05
 **/
public class CSDNHelper extends BlogHelper{
    public CSDNHelper(Properties blogProps) {
        super((String) blogProps.get("blog.meteweblog.csdn.serverUrl"), (String) blogProps.get("blog.meteweblog.csdn.username"), (String) blogProps.get("blog.meteweblog.csdn.password"));
    }

    @Override
    public boolean newPost(Map<String, Object> mappedParam) {
        return false;
    }

    @Override
    public boolean editPost(Map<String, Object> mappedParam) {
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
