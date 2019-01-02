package top.ylonline.spring.webflux.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import top.ylonline.spring.webflux.example.entity.User;
import top.ylonline.spring.webflux.example.repository.UserRepository;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Created by YL on 2019/1/2
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Scheduler jdbcScheduler;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           @Qualifier("jdbcScheduler") Scheduler jdbcScheduler) {
        this.userRepository = userRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    @Override
    public Flux<User> findAll() {
        return asyncIterable(this.userRepository.findAll());
    }

    private <T> Flux<T> asyncIterable(Iterable<T> it) {
        return Flux.fromIterable(it).publishOn(this.jdbcScheduler);
    }

    @Override
    public Mono<List<User>> findAll2() {
        return asyncCallable(this.userRepository::findAll);
    }

    private <T> Mono<T> asyncCallable(Callable<T> cb) {
        return Mono.fromCallable(cb).publishOn(this.jdbcScheduler);
    }
}
