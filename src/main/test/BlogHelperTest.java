import com.terwergreen.helper.BlogHelper;
import com.terwergreen.helper.BlogHelperFactory;
import com.terwergreen.helper.BlogTypeEnum;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 博客API测试类
 *
 * @name: BlogHelperTest
 * @author: terwer
 * @date: 2022-01-20 10:05
 **/
public class BlogHelperTest {
    private static Logger logger = LoggerFactory.getLogger(BlogHelperTest.class);

    @Test
    public void test_cnblogs_getUsersBlogs() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.CNBLOGS);
        Map<String, Object> reaultMap = blogHelper.getUsersBlogs();
        logger.info("reaultMap = " + reaultMap);
    }

    @Test
    public void test_bugucms_getUsersBlogs() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.BUGUCMS);
        Map<String, Object> reaultMap = blogHelper.getUsersBlogs();
        logger.info("reaultMap = " + reaultMap);
    }

    @Test
    public void test_confluence_getUsersBlogs() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.CONFLUENCE);
        Map<String, Object> reaultMap = blogHelper.getUsersBlogs();
        logger.info("reaultMap = " + reaultMap);
    }
}
