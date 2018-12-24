package top.ylonline.sentinel.spring.cloud.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Slf4j
public class SentinelApp {
    public static void main(String[] args) {
        SpringApplication.run(SentinelApp.class, args);
    }

    @GetMapping("/demo")
    public String demo() {
        return "Hello world!";
    }
}
