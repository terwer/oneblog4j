package com.terwergreen.helper;

import com.terwergreen.App;
import com.terwergreen.model.Post;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * @author: terwer
 * @date: 2022/1/9 18:53
 * @description: BlogHelperFactory
 */
public class BlogHelperFactory {

    private static Properties blogProps = new Properties();

    static {
        InputStream blogPropsStream = App.class.getClassLoader().getResourceAsStream("blog-pro.properties");
        try {
            blogProps.load(blogPropsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BlogHelperFactory() {

    }

    public static BlogHelper getBlogHelper(BlogTypeEnum blogType) {
        switch (blogType) {
            case BUGUCMS: {
                return new BuguCMSHelper(blogProps);
            }
            case BUGUCMS_API: {
                return new BuguCMSApiHelper(blogProps);
            }
            case CNBLOGS: {
                return new CnblogsHelper(blogProps);
            }
            case CONFLUENCE: {
                return new ConfluenceHelper(blogProps);
            }
            case CONFLUENCE_DELEGATE: {
                return new ConfluenceDelegateHelper(blogProps);
            }
            default:
                return null;
        }
    }
}
