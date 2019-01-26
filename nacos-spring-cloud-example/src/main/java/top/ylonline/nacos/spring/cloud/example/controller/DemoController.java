package top.ylonline.nacos.spring.cloud.example.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by YL on 2018/12/24
 */
@RestController
@RefreshScope
@Slf4j
public class DemoController {
    @NacosValue(value = "${example.id}", autoRefreshed = true)
    private int id;
    @Value("${example.name}")
    private String name;
    @Value("${example.age}")
    private int age;
    @Value("${example.weight}")
    private String weight;
    @Value("${example.profile}")
    private String profile;

    @GetMapping("/demo")
    public String demo() {
        return String.format("{\"id\": %s, \"name\": \"%s\", \"age\": \"%s\", \"weight\": \"%s\", \"profile\": " +
                "\"%s\"}", id, name, age, weight, profile);
    }
}
