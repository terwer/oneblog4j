package com.terwergreen.helper;

/**
 * @author: terwer
 * @date: 2022/1/9 18:53
 * @description: BlogHelperFactory
 */
public class BlogHelperFactory {
    private BlogHelperFactory(){

    }

    public static BlogHelper getBlogHelper(BlogTypeEnum blogType){
        switch (blogType){
            case BUGUCMS:{
                return new BuguCMSHelper();
            }
            case CNBLOGS:{
                return new CnblogsHelper();
            }
            default:
                return null;
        }
    }
}
