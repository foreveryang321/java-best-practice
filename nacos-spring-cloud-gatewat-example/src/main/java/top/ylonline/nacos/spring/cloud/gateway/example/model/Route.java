package top.ylonline.nacos.spring.cloud.gateway.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by YL on 2018/12/22
 */
@Data
public class Route {
    // 路由 ID
    private String id;
    // 路由规则转发的目标 URI
    private String uri;
    // 路由执行顺序
    private int order = 0;
    // 路由断言集合
    private List<Predicate> predicates = new ArrayList<>();
    // 路由过滤器集合
    private List<Filter> filters = new ArrayList<>();
}
