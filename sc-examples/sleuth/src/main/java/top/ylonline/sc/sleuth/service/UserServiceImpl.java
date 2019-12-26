package top.ylonline.sc.sleuth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import top.ylonline.sc.sleuth.entity.User;
import top.ylonline.sc.sleuth.repository.UserRepository;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author YL
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Scheduler jdbcScheduler;
    private final RestTemplate restTemplate;

    public UserServiceImpl(UserRepository userRepository,
                           @Qualifier("jdbcScheduler") Scheduler jdbcScheduler,
                           RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.jdbcScheduler = jdbcScheduler;
        this.restTemplate = restTemplate;
    }

    @Override
    public Flux<User> findAll() {
        List<User> list = (List<User>) restTemplate.getForObject("http://localhost:8080/user/list", List.class);
        log.info("list: {}", list);
        return asyncIterable(this.userRepository.findAll());
    }

    private <T> Flux<T> asyncIterable(Iterable<T> it) {
        return Flux.fromIterable(it).publishOn(this.jdbcScheduler);
    }

    @Override
    public Mono<List<User>> findAll2() {
        log.info("findAll2");
        return asyncCallable(this.userRepository::findAll);
    }

    private <T> Mono<T> asyncCallable(Callable<T> cb) {
        return Mono.fromCallable(cb).publishOn(this.jdbcScheduler);
    }
}
