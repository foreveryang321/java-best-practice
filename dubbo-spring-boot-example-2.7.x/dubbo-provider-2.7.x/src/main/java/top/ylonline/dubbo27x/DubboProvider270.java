package top.ylonline.dubbo27x;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 * @author YL
 */
@SpringBootApplication
// @DubboComponentScan(basePackages = {"top.ylonline.dubbo27x"})
@Slf4j
public class DubboProvider270 {

    public static void main(String[] args) {
        SpringApplication.run(DubboProvider270.class, args);
    }
}

