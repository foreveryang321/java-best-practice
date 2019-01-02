package top.ylonline.spring.webflux.example.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.ylonline.spring.webflux.example.entity.User;

import java.util.List;

/**
 * @author Created by YL on 2019/1/2
 */
public interface UserService {
    Flux<User> findAll();

    Mono<List<User>> findAll2();
}
