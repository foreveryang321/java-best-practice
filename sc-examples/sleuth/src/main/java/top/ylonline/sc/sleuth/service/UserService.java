package top.ylonline.sc.sleuth.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.ylonline.sc.sleuth.entity.User;

import java.util.List;

/**
 * @author YL
 */
public interface UserService {
    Flux<User> findAll();

    Mono<List<User>> findAll2();
}
