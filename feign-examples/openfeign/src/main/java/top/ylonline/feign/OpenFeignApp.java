package top.ylonline.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author YL
 */
@SpringBootApplication
@EnableFeignClients
public class OpenFeignApp {

    public static void main(String[] args) {
        SpringApplication.run(OpenFeignApp.class, args);
    }
}
