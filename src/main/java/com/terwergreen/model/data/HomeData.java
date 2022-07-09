package com.terwergreen.model.data;

import com.terwergreen.controller.HomeController;

import java.util.LinkedHashMap;

/**
 * @author: terwer
 * @date: 2022/1/9 14:42
 * @description: Post
 */
public class HomeData {
    HomeController from;
    String mwebFileId;
    String postTitle;

    // 注意：如果是列表跳转，这个字段是null
    String content;

    // 如果是vuepress的文章，解析元数据
    LinkedHashMap metadata;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LinkedHashMap getMetadata() {
        return metadata;
    }

    public void setMetadata(LinkedHashMap metadata) {
        this.metadata = metadata;
    }
}
