package top.ylonline.feign.client;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author YL
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@EnableAutoConfiguration
@EnableFeignClients(clients = GithubClient.class)
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