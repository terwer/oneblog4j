import com.terwergreen.util.YamlUtil;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Yaml测试
 *
 * @name: YamlTest
 * @author: terwer
 * @date: 2022-07-09 13:39
 **/
public class YamlTest {
    @Test
    public void testDump() {
//        LinkedHashMap<String, Object> data = YamlUtil.buildMetaDataMap("node发送邮件", "node-send-mail",
//                new String[]{"node", "mail"}, "node发送邮件。", new String[]{"前端开发"});
        LinkedHashMap<String, Object> data = YamlUtil.autoBuildMetaDataMap("node发送邮件", "node发送邮件内容。",
                new String[]{"node", "mail"}, new String[]{"前端开发"}, "node-send-mail", "node发送邮件。", null);
        String metadata = YamlUtil.generateMetadata(data);
        System.out.println(metadata);
    }
}
