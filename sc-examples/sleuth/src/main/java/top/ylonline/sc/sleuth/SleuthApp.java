package top.ylonline.sc.sleuth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import top.ylonline.sc.sleuth.entity.User;
import top.ylonline.sc.sleuth.repository.UserRepository;
import top.ylonline.sc.sleuth.service.UserService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Hello world!
 *
 * @author YL
 */
@SpringBootApplication
@RestController
@Slf4j
public class SleuthApp {
    public static void main(String[] args) {
        SpringApplication.run(SleuthApp.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Resource
    private UserRepository userRepository;
    @Resource
    private UserService userService;
    @Resource
    private RestTemplate restTemplate;

    @RequestMapping("/user/list")
    public List<User> list() {
        log.info("user/list");
        return userRepository.findAll();
    }

    @RequestMapping("/user/list2")
    public Flux<User> list2() {
        log.info("user/list2");
        List<User> list = (List<User>) restTemplate.getForObject("http://localhost:8080/user/list", List.class);
        log.info("list: {}", list);
        return userService.findAll();
    }
}
