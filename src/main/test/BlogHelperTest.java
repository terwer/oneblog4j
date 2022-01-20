import com.terwergreen.helper.BlogHelper;
import com.terwergreen.helper.BlogHelperFactory;
import com.terwergreen.helper.BlogTypeEnum;
import com.terwergreen.model.Post;
import org.junit.Test;

import java.util.ArrayList;
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
public class BlogHelperTest {

    @Test
    public void testMetaWeblogApi_blogger_getUsersBlogs() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.CNBLOGS);
        Map<String, Object> reaultMap = blogHelper.getUsersBlogs();
        System.out.println("reaultMap = " + reaultMap);
    }

    @Test
    public void testApi_blogger_getUsersBlogs() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.BUGUCMS);
        Map<String, Object> reaultMap = blogHelper.getUsersBlogs();

        System.out.println("reaultMap = " + reaultMap);
    }


}
