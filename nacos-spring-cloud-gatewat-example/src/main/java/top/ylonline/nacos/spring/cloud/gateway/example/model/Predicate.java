package top.ylonline.nacos.spring.cloud.gateway.example.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Created by YL on 2018/12/22
 */
@Data
public class Predicate {
    // 断言对应的名称
    private String name;
    // 断言规则
    private Map<String, String> args = new LinkedHashMap<>();
}
