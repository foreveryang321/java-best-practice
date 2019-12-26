package top.ylonline.sb.webflux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.ylonline.sb.webflux.entity.User;
import top.ylonline.sb.webflux.repository.UserRepository;
import top.ylonline.sb.webflux.service.UserService;

import java.util.List;

/**
 * @author YL
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping(value = "/block")
    @ResponseBody
    public List<User> blocking() {
        log.info("block");
        return userRepository.findAll();
    }

    @GetMapping(value = "/rx", produces = "application/stream+json")
    @ResponseBody
    public Flux<User> rx() {
        log.info("rx");
        return this.userService.findAll();
    }

    @GetMapping(value = "/rx2")
    @ResponseBody
    public Mono<List<User>> rx2() {
        log.info("rx2");
        return this.userService.findAll2();
    }
}
