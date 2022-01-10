package com.terwergreen.helper;

import com.terwergreen.model.Post;

/**
 * @author: terwer
 * @date: 2022/1/8 23:59
 * @description: CnblogsHelper
 */
public class CnblogsHelper extends BlogHelper{
    @Override
    public boolean addPost(Post post) {
        System.out.println("CnBlogs add Post");
        return false;
    }

    @Override
    public boolean updatePost(Post post) {
        System.out.println("CnBlogs update Post");
        return false;
    }
}
