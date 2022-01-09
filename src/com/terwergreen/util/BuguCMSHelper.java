package com.terwergreen.util;

import com.terwergreen.model.Post;

/**
 * @author: terwer
 * @date: 2022/1/9 18:55
 * @description: BuguCMSHelper
 */
public class BuguCMSHelper extends BlogHelper {
    @Override
    public boolean addPost(Post post) {
        System.out.println("BuguCMS addPost");
        return false;
    }

    @Override
    public boolean updatePost(Post post) {
        System.out.println("BuguCMS updatePost");
        return false;
    }
}
