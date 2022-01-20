package com.terwergreen.helper;

import com.terwergreen.App;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: terwer
 * @date: 2022/1/9 18:53
 * @description: BlogHelperFactory
 */
public class BlogHelperFactory {

    private static Properties blogProps = new Properties();

    static {
        InputStream blogPropsStream = App.class.getClassLoader().getResourceAsStream("blog.properties");
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
            case CNBLOGS: {
                return new CnblogsHelper(blogProps);
            }
            default:
                return null;
        }
    }
}
