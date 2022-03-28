import com.alibaba.fastjson.JSON;
import com.terwergreen.helper.BlogHelper;
import com.terwergreen.helper.BlogHelperFactory;
import com.terwergreen.helper.BlogTypeEnum;
import com.terwergreen.helper.ConfluenceHelper;
import com.terwergreen.model.Post;
import com.terwergreen.util.ResourceUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * 博客API测试类
 *
 * @name: BlogHelperTest
 * @author: terwer
 * @date: 2022-01-20 10:05
 **/
public class ConfluenceBlogHelperTest {
    private static Logger logger = LoggerFactory.getLogger(ConfluenceBlogHelperTest.class);

    // 获取最近博文
    // blogger.getRecentPosts
    @Test
    public void test_confluence_getUsersBlogs() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.CONFLUENCE);
        Map<String, Object> reaultMap = blogHelper.getUsersBlogs();
        logger.info("reaultMap = " + reaultMap);
    }

    @Test
    public void test_confluence_getRecentPosts() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.CONFLUENCE);
        Map<String, Object> mappedParams = new HashMap<>();
        List<Map> result = blogHelper.getRecentPosts(mappedParams);
        logger.info("result = " + JSON.toJSONString(result));
    }

    @Test
    public void test_confluence_newPost() {
        // =================================
        // 文章发布模板
        // 1、MWebId
        long FILE_HASH = 16484014983252L;
        String postId = "spc";
        // 2、标签
        Vector<String> categories = new Vector<>();
        categories.add("java2");
        // =================================

        BlogHelper blogHelper = (ConfluenceHelper) BlogHelperFactory.getBlogHelper(BlogTypeEnum.CONFLUENCE);

        Map<String, Object> mappedParams = new HashMap<>();
        mappedParams.put(ConfluenceHelper.CONFLUENCE_POST_FILE_HASH, FILE_HASH);
        mappedParams.put(ConfluenceHelper.CONFLUENCE_POST_POST_ID, postId);
        mappedParams.put(ConfluenceHelper.CONFLUENCE_POST_CATEGORIES, categories);
        mappedParams.put(ConfluenceHelper.CONFLUENCE_BLOG_TYPE, blogHelper);

        boolean b = blogHelper.newPost(mappedParams);
    }

    @Test
    public void test_confluence_editPost() {
        // =================================
        // 文章更新模板
        // 1、MWebId
        long FILE_HASH = 16484014983252L;
        // 2、标签
        Vector<String> categories = new Vector<>();
        categories.add("javase");
        // 3.2、文章ID
        String postId = "7241730";
        // =================================

        ConfluenceHelper blogHelper = (ConfluenceHelper) BlogHelperFactory.getBlogHelper(BlogTypeEnum.CONFLUENCE);

        Map<String, Object> mappedParams = new HashMap<>();
        mappedParams.put(ConfluenceHelper.CONFLUENCE_POST_FILE_HASH, FILE_HASH);
        mappedParams.put(ConfluenceHelper.CONFLUENCE_POST_POST_ID, postId);
        mappedParams.put(ConfluenceHelper.CONFLUENCE_POST_CATEGORIES, categories);
        mappedParams.put(ConfluenceHelper.CONFLUENCE_BLOG_TYPE, blogHelper);

        blogHelper.editPost(mappedParams);
    }
}
