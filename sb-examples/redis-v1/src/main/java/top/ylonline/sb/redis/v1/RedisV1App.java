package top.ylonline.sb.redis.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.ylonline.sb.redis.v1.core.EnableTRedisConfiguration;

/**
 * Hello world!
 *
 * @author YL
 */
@SpringBootApplication
@EnableTRedisConfiguration
public class RedisV1App {

    public static void main(String[] args) {
        SpringApplication.run(RedisV1App.class, args);
    }
}
