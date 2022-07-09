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
public class BuguCMSApiTest {
    private static Logger logger = LoggerFactory.getLogger(BuguCMSApiTest.class);

    @Test
    public void test_bugucms_getUsersBlogs() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.BUGUCMS_API);
        Map<String, Object> reaultMap = blogHelper.getUsersBlogs();
        logger.info("reaultMap = " + reaultMap);
    }
}
