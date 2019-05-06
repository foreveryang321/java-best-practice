package top.ylonline.sb.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WebFluxApp {

    public static void main(String[] args) {
        SpringApplication.run(WebFluxApp.class, args);
    }
}
