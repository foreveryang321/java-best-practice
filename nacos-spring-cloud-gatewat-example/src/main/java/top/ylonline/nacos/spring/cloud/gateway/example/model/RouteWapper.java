package top.ylonline.nacos.spring.cloud.gateway.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by YL on 2018/12/22
 */
@Data
public class RouteWapper {
    private List<Route> routes = new ArrayList<>();
}
