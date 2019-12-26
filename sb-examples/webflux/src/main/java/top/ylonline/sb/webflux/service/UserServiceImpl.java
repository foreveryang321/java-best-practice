package top.ylonline.sb.webflux.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import top.ylonline.sb.webflux.entity.User;
import top.ylonline.sb.webflux.repository.UserRepository;

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

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           @Qualifier("jdbcScheduler") Scheduler jdbcScheduler) {
        this.userRepository = userRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    @Override
    public Flux<User> findAll() {
        log.info("findAll");
        return asyncIterable(this.userRepository.findAll());
    }

    private <T> Flux<T> asyncIterable(Iterable<T> it) {
        return Flux.fromIterable(it).publishOn(this.jdbcScheduler);
    }

    @Override
    public Mono<List<User>> findAll2() {
        log.info("findAll");
        return asyncCallable(this.userRepository::findAll);
    }

    private <T> Mono<T> asyncCallable(Callable<T> cb) {
        return Mono.fromCallable(cb).publishOn(this.jdbcScheduler);
    }
}
