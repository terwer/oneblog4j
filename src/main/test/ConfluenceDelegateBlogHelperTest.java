import com.terwergreen.helper.BlogHelper;
import com.terwergreen.helper.BlogHelperFactory;
import com.terwergreen.helper.BlogTypeEnum;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
