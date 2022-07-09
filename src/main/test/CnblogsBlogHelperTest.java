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
 * 博客API测试类
 *
 * @name: BlogHelperTest
 * @author: terwer
 * @date: 2022-01-20 10:05
 **/
public class CnblogsBlogHelperTest {
    private static Logger logger = LoggerFactory.getLogger(CnblogsBlogHelperTest.class);

    @Test
    public void test_cnblogs_getUsersBlogs() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.CNBLOGS);
        Map<String, Object> reaultMap = blogHelper.getUsersBlogs();
        logger.info("reaultMap = " + reaultMap);
    }

    @Test
    public void test_confluence_getCategories() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.CNBLOGS);
        Map<String, Object> mappedParams = new HashMap<>();
        Object result = blogHelper.getCategories(mappedParams);

        System.out.println("result = " + JSON.toJSONString(result));
    }
}
