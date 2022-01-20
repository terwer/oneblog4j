package com.terwergreen.helper;

import com.alibaba.fastjson.JSONObject;
import com.terwergreen.model.Post;
import com.terwergreen.util.HttpUtil;

import java.util.*;

/**
 * @author: terwer
 * @date: 2022/1/9 18:55
 * @description: BuguCMSHelper
 */
public class BuguCMSHelper extends BlogHelper {
    public BuguCMSHelper(Properties blogProps) {
        super((String) blogProps.get("blog.jsonapi.bugucms.baseUrl"), "", "");
    }

    @Override
    public Map<String, Object> getUsersBlogs() {
        List<String> pParams = new ArrayList<>();

        // /site/config
        String url = this.getServerUrl() + "/site/config";
        String resultString = HttpUtil.get(url);

        JSONObject jsonObject = JSONObject.parseObject(resultString);
        //json对象转Map
        Map<String, Object> reaultMap = (Map<String, Object>) jsonObject;

        System.out.println("blogger.getUsersBlogs=>");
        return reaultMap;
    }

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
