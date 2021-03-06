package top.ylonline.sb.redis.v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.ylonline.sb.redis.v2.anno.EnableTRedisConfiguration;

/**
 * Hello world!
 *
 * @author YL
 */
@SpringBootApplication
@EnableTRedisConfiguration
public class RedisV2App {

    public static void main(String[] args) {
        SpringApplication.run(RedisV2App.class, args);
    }
}
