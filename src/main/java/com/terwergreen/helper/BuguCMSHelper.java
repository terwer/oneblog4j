package com.terwergreen.helper;

import com.terwergreen.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * BugUCMS的metaWeblogApi实现
 *
 * @name: BuguCMSHelper
 * @author: terwer
 * @date: 2022-03-07 11:26
 **/
public class BuguCMSHelper extends BlogHelper{

    private static Logger logger = LoggerFactory.getLogger(BuguCMSHelper.class);

    public BuguCMSHelper(Properties blogProps) {
        super((String) blogProps.get("blog.meteweblog.bugucms.serverUrl"), (String) blogProps.get("blog.meteweblog.bugucms.username"), (String) blogProps.get("blog.meteweblog.bugucms.password"));
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

        logger.debug("CnBlogs add Post:" + result);
        return false;
    }

    @Override
    public boolean updatePost(Post post) {
        logger.debug("CnBlogs update Post");
        return false;
    }
}
