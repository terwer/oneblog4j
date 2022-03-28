package com.terwergreen.helper;

import java.util.Map;
import java.util.Properties;

/**
 * @name: ConfluenceDelegateHelper
 * @author: terwer
 * @date: 2022-03-28 20:58
 **/
public class ConfluenceDelegateHelper extends ConfluenceHelper {
    public ConfluenceDelegateHelper(Properties blogProps) {
        super((String) blogProps.get("blog.meteweblog.confluence.delegate.serverUrl"), (String) blogProps.get("blog.meteweblog.confluence.delegate.username"), (String) blogProps.get("blog.meteweblog.confluence.delegate.password"));
    }

    @Override
    public Map<String, Object> getUsersBlogs() {
        return super.getUsersBlogs();
    }
}
