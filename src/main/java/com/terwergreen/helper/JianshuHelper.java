package com.terwergreen.helper;

import com.terwergreen.model.Post;

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
    public boolean addPost(Post post) {
        return false;
    }

    @Override
    public boolean updatePost(Post post) {
        return false;
    }
}
