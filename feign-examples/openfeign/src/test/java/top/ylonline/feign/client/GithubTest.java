package top.ylonline.feign.client;

import com.alibaba.fastjson.JSON;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author YL
 */
public class GithubTest {
    
    @Test
    public void contributors() {
        Github github = Feign.builder()
                .decoder(new GsonDecoder())
                .target(Github.class, "https://api.github.com");

        List<Map<String, Object>> contributors = github.contributors("foreveryang321", "java-best-practice");
        for (Map<String, Object> contributor : contributors) {
            System.out.println(JSON.toJSONString(contributor));
        }
    }
}