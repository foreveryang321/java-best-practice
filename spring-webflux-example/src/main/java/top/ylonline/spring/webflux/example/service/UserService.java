package top.ylonline.spring.webflux.example.service;

import reactor.core.publisher.Flux;
import top.ylonline.spring.webflux.example.entity.User;

/**
 * @author Created by YL on 2019/1/2
 */
public interface UserService {
    Flux<User> findAll();
}
