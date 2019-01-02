package top.ylonline.spring.webflux.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import top.ylonline.spring.webflux.example.entity.User;
import top.ylonline.spring.webflux.example.repository.UserRepository;

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
        Flux<User> defer = Flux.defer(() -> Flux.fromIterable(this.userRepository.findAll()));
        return defer.subscribeOn(jdbcScheduler);
    }
}
