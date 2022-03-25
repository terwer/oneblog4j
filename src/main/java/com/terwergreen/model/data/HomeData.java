package com.terwergreen.model.data;

import com.terwergreen.controller.HomeController;

/**
 * @author: terwer
 * @date: 2022/1/9 14:42
 * @description: Post
 */
public class HomeData {
    HomeController from;
    String mwebFileId;
    String postTitle;

    public HomeController getFrom() {
        return from;
    }

    public void setFrom(HomeController from) {
        this.from = from;
    }

    public String getMwebFileId() {
        return mwebFileId;
    }

    public void setMwebFileId(String mwebFileId) {
        this.mwebFileId = mwebFileId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }
}
