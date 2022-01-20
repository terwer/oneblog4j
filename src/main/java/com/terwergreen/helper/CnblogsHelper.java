package com.terwergreen.helper;

import com.terwergreen.model.Post;

import java.util.*;

/**
 * @author: terwer
 * @date: 2022/1/8 23:59
 * @description: CnblogsHelper
 */
public class CnblogsHelper extends BlogHelper {
    public CnblogsHelper(Properties blogProps) {
        super((String) blogProps.get("blog.meteweblog.cnblogs.serverUrl"), (String) blogProps.get("blog.meteweblog.cnblogs.username"), (String) blogProps.get("blog.meteweblog.cnblogs.password"));
    }

    @Override
    public Map<String, Object> getUsersBlogs() {
        return super.getUsersBlogs();
    }

    @Override
    public boolean addPost(Post post) {
        List<String> pParams = new ArrayList<>();
        pParams.add("default");
        pParams.add(super.getUsername());
        pParams.add(super.getPassword());

        Object result = super.executeMeteweblog("blogger.getUsersBlogs", pParams);

        System.out.println("CnBlogs add Post:" + result);
        return false;
    }

    @Override
    public boolean updatePost(Post post) {
        System.out.println("CnBlogs update Post");
        return false;
    }
}
