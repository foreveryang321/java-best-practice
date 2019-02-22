package top.ylonline.feign.client;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.ylonline.feign.OpenFeignApp;

import java.util.List;
import java.util.Map;

/**
 * @author YL
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OpenFeignApp.class)
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    public void contributors() {
        List<Map<String, Object>> contributors = githubClient.contributors("foreveryang321", "java-best-practice");
        for (Map<String, Object> contributor : contributors) {
            System.out.println(JSON.toJSONString(contributor));
        }
    }
}