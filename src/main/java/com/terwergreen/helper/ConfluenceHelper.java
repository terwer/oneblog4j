package com.terwergreen.helper;

import com.alibaba.fastjson.JSON;
import com.terwergreen.util.ResourceUtil;
import com.terwergreen.util.SystemUtil;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * Confluence
 *
 * @name: ConfluenceHelper
 * @author: terwer
 * @date: 2022-03-28 02:47
 **/
public class ConfluenceHelper extends BlogHelper {
    private static Logger logger = LoggerFactory.getLogger(BuguCMSHelper.class);

    public static final String CONFLUENCE_BLOG_TYPE = "blogtype";

    public static final String CONFLUENCE_POST_FILE_HASH = "filehash";
    public static final String CONFLUENCE_POST_POST_ID = "postId";
    public static final String CONFLUENCE_POST_CATEGORIES = "categories";

    // Static struct fields
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String PERMALINK = "permaLink";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORIES = "categories";
    public static final String AUTHOR = "author";
    public static final String PUBDATE = "pubDate";
    public static final String DATECREATED = "dateCreated";
    public static final String HTMLURL = "htmlUrl";
    public static final String RSSURL = "rssUrl";
    public static final String POSTID = "postid";
    public static final String BLOGID = "blogid";

    public ConfluenceHelper(Properties blogProps) {
        super((String) blogProps.get("blog.meteweblog.confluence.serverUrl"), (String) blogProps.get("blog.meteweblog.confluence.username"), (String) blogProps.get("blog.meteweblog.confluence.password"));
    }

    public ConfluenceHelper(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Map<String, Object> getUsersBlogs() {
        return super.getUsersBlogs();
    }

    /**
     * 参数：包含下列key值的Map
     * CONFLUENCE_POST_FILE_HASH
     * CONFLUENCE_POST_POST_ID
     * CONFLUENCE_POST_CATEGORIES
     * CONFLUENCE_BLOG_TYPE
     *
     * @param mappedParams
     * @return
     */
    @Override
    public boolean newPost(Map<String, Object> mappedParams) {
        boolean flag = false;
        try {
            long filehash = (long) mappedParams.get(CONFLUENCE_POST_FILE_HASH);
            String postId = (String) mappedParams.get(CONFLUENCE_POST_POST_ID);
            Vector<String> categories = (Vector<String>) mappedParams.get(CONFLUENCE_POST_CATEGORIES);

            BlogHelper blogHelper = (BlogHelper) mappedParams.get(CONFLUENCE_BLOG_TYPE);

            // 正文
            String basepath = "C:/Users/terwer/Documents/share/";
            if (SystemUtil.isLinux()) {
                basepath = "/Users/terwer/Documents/share/";
            }
            String postPath = basepath + "cross/MWeb/MWebLibrary/docs/" + filehash + ".md";
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(postPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String content = ResourceUtil.readStream(inputStream);
            // logger.info("content = " + content);

            // 文章标题
            String postTitle = content.split("")[0];
            logger.info("postTitle = " + postTitle);

            // 转换为markdown
            StringBuilder sb = new StringBuilder();
            sb.append("<ac:structured-macro ac:name=\"markdown\" ac:schema-version=\"1\" ac:macro-id=\"529e4807-3a3b-401e-a337-8cccc762b3fe\"><ac:plain-text-body><![CDATA[");
            sb.append(content);
            sb.append("]]></ac:plain-text-body></ac:structured-macro>");
            String parsedMarkdown = sb.toString();

            List<Object> pParams = new ArrayList<>();
            pParams.add("ds");
            pParams.add(blogHelper.getUsername());
            pParams.add(blogHelper.getPassword());
            Hashtable<String, Object> struct = new Hashtable<>();
            struct.put(TITLE, postTitle);
            struct.put(DESCRIPTION, parsedMarkdown);
            struct.put(DATECREATED, new Date());
            struct.put(CATEGORIES, categories);
            pParams.add(struct);// 文章信息
            pParams.add(true);// 是否发布
            Object result = blogHelper.executeMeteweblog("metaWeblog.newPost", pParams);

            logger.debug("Confluence add Post:" + JSON.toJSONString(result));
            flag = true;
        } catch (Exception e) {
            logger.error("接口异常", e);
        }

        return flag;
    }

    /**
     * 参数：包含下列key值的Map
     * CONFLUENCE_POST_FILE_HASH
     * CONFLUENCE_POST_POST_ID
     * CONFLUENCE_POST_CATEGORIES
     * CONFLUENCE_BLOG_TYPE
     *
     * @param mappedParams
     * @return
     */
    @Override
    public boolean editPost(Map<String, Object> mappedParams) {
        boolean flag = false;
        try {
            long filehash = (long) mappedParams.get(CONFLUENCE_POST_FILE_HASH);
            String postId = (String) mappedParams.get(CONFLUENCE_POST_POST_ID);
            Vector<String> categories = (Vector<String>) mappedParams.get(CONFLUENCE_POST_CATEGORIES);

            BlogHelper blogHelper = (BlogHelper) mappedParams.get(CONFLUENCE_BLOG_TYPE);

            // 正文
            String basepath = "C:/Users/terwer/Documents/share/";
            if (SystemUtil.isLinux()) {
                basepath = "/Users/terwer/Documents/share/";
            }
            String postPath = basepath + "cross/MWeb/MWebLibrary/docs/" + filehash + ".md";
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(postPath);
            String content = ResourceUtil.readStream(inputStream);
            // logger.info("content = " + content);

            // 文章标题
            String postTitle = content.split("\n")[0].replace("# ", "");
            logger.info("postTitle = " + postTitle);

            // 转换为markdown
            StringBuilder sb = new StringBuilder();
            sb.append("<ac:structured-macro ac:name=\"markdown\" ac:schema-version=\"1\" ac:macro-id=\"529e4807-3a3b-401e-a337-8cccc762b3fe\"><ac:plain-text-body><![CDATA[");
            sb.append(content);
            sb.append("]]></ac:plain-text-body></ac:structured-macro>");
            String parsedMarkdown = sb.toString();

            List<Object> pParams = new ArrayList<>();
            pParams.add(postId);
            pParams.add(blogHelper.getUsername());
            pParams.add(blogHelper.getPassword());
            Hashtable<String, Object> struct = new Hashtable<>();
            struct.put(TITLE, postTitle);
            struct.put(DESCRIPTION, parsedMarkdown);
            struct.put(DATECREATED, new Date());
            struct.put(CATEGORIES, categories);
            pParams.add(struct);// 文章信息
            pParams.add(true);// 是否发布
            Object result = blogHelper.executeMeteweblog("metaWeblog.editPost", pParams);
            logger.info("result = " + JSON.toJSONString(result));

            logger.debug("Confluence update Post");
            flag = true;
        } catch (Exception e) {
            logger.error("接口异常", e);
        }

        return flag;
    }

    @Override
    public <T> T getPost(Map<String, Object> mappedParams) {
        List<Object> pParams = new ArrayList<>();
        pParams.add("7241730");
        pParams.add(this.getUsername());
        pParams.add(this.getPassword());
        T result = (T) this.executeMeteweblog("metaWeblog.getPost", pParams);
        return result;
    }

    public <T> List<T> getRecentPosts_blogger(Map<String, Object> mappedParams) {
        List<Object> pParams = new ArrayList<>();
        pParams.add("default");
        pParams.add("ds");
        pParams.add(this.getUsername());
        pParams.add(this.getPassword());
        pParams.add(10);
        List<T> result = (List<T>) this.executeMeteweblog("blogger.getRecentPosts", pParams);
        return result;
    }

    @Override
    public <T> List<T> getRecentPosts(Map<String, Object> mappedParams) {
        List<Object> pParams = new ArrayList<>();
        pParams.add("spc");
        pParams.add(this.getUsername());
        pParams.add(this.getPassword());
        pParams.add(10);
        Object[] dataList = (Object[]) this.executeMeteweblog("metaWeblog.getRecentPosts", pParams);
        List<T> result = new ArrayList<>();
        for (Object obj : dataList) {
            T data = (T) obj;
            result.add(data);
        }
        return result;
    }

    @Override
    public <T> T getCategories(Map<String, Object> mappedParams) {
        List<Object> pParams = new ArrayList<>();
        pParams.add("ds");
        pParams.add(this.getUsername());
        pParams.add(this.getPassword());
        T result = (T) this.executeMeteweblog("metaWeblog.getCategories", pParams);
        return result;
    }

    @Override
    public boolean newMediaObject(Map<String, Object> pParams) {
        throw new NotImplementedException();
    }
}
