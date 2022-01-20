import com.terwergreen.helper.BlogHelper;
import com.terwergreen.helper.BlogHelperFactory;
import com.terwergreen.helper.BlogTypeEnum;
import com.terwergreen.model.Post;
import org.junit.Test;

/**
 * 博客API测试类
 *
 * @name: BlogHelperTest
 * @author: terwer
 * @date: 2022-01-20 10:05
 **/
public class BlogHelperTest {

    @Test
    private void testApi() {
        BlogHelper blogHelper = BlogHelperFactory.getBlogHelper(BlogTypeEnum.CNBLOGS);

        Post post = new Post();

        blogHelper.addPost(post);
    }
}
