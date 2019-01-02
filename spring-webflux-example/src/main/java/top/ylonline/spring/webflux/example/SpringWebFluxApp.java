package top.ylonline.spring.webflux.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringWebFluxApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebFluxApp.class, args);
    }
}
