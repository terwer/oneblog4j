package com.terwergreen.util;

import com.terwergreen.model.Post;

/**
 * @author: terwer
 * @date: 2022/1/9 18:51
 * @description: BlogHelper
 */
public abstract class BlogHelper {
    public abstract boolean addPost(Post post);
    public abstract boolean updatePost(Post post);
}
