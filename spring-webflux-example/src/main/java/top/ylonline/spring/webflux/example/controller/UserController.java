package top.ylonline.spring.webflux.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import top.ylonline.spring.webflux.example.entity.User;
import top.ylonline.spring.webflux.example.repository.UserRepository;
import top.ylonline.spring.webflux.example.service.UserService;

import java.util.List;

/**
 * @author Created by YL on 2019/1/2
 */
@RestController
@RequestMapping("/user")
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
        return userRepository.findAll();
    }

    @GetMapping(value = "/rx", produces = "application/stream+json")
    @ResponseBody
    public Flux<User> rx() {
        return this.userService.findAll();
    }
}
