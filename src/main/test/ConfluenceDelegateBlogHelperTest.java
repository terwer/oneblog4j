import com.alibaba.fastjson.JSON;
import com.terwergreen.helper.BlogHelper;
import com.terwergreen.helper.BlogHelperFactory;
import com.terwergreen.helper.BlogTypeEnum;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Confluence代理接口
 *
 * @name: ConfluenceDelegateBlogHelperTest
 * @author: terwer
 * @date: 2022-03-28 20:56
 **/
public class ConfluenceDelegateBlogHelperTest {
    private static Logger logger = LoggerFactory.getLogger(ConfluenceDelegateBlogHelperTest.class);

    @Test
    public void test_confluence_getUsersBlogs() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.CONFLUENCE_DELEGATE);
        Map<String, Object> reaultMap = blogHelper.getUsersBlogs();
        logger.info("reaultMap = " + reaultMap);
    }

    @Test
    public void test_confluence_getCategories() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.CONFLUENCE_DELEGATE);
        Map<String, Object> mappedParams = new HashMap<>();
        List<Map<String, Object>> reaultList = blogHelper.getCategories(mappedParams);

        for (Map<String, Object> result : reaultList) {
            System.out.println("result = " + result);
        }
    }

    @Test
    public void test_confluence_getPost() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.CONFLUENCE_DELEGATE);
        Map<String, Object> mappedParams = new HashMap<>();
        Object result = blogHelper.getPost(mappedParams);

        System.out.println("result = " + JSON.toJSONString(result));
    }
}
