package top.ylonline.nacos.spring.cloud.gateway.example;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @author Created by YL on 2018/12/24
 */
public class NacosGatewayAppTest {

    @Test
    public void fastjson() {
        String jsonStr = "{\"id\": \"taobao\", \"predicates\": [{\"name\": \"Path\", \"args\": {\"pattern\": " +
                "\"/search\"}}], \"filters\": [], \"uri\": \"https://s.taobao.com/\", \"order\": 0}";

        RouteDefinition definition = JSON.parseObject(jsonStr, RouteDefinition.class);
        System.out.println(definition.toString());
    }
}