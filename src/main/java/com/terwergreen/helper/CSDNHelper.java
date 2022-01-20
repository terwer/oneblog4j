package com.terwergreen.helper;

import com.terwergreen.model.Post;

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
    public boolean addPost(Post post) {
        return false;
    }

    @Override
    public boolean updatePost(Post post) {
        return false;
    }
}
