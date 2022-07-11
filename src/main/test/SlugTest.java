import com.github.slugify.Slugify;
import org.junit.Test;

/**
 * @name: SlugTest
 * @author: terwer
 * @date: 2022-07-11 21:36
 **/
public class SlugTest {
    @Test
    public void testSlug(){
        String text = "XDG Desktop Menu = > no writable system menu directory found solution";
        String slug = new Slugify().slugify(text);
        System.out.println(slug);
    }
}
