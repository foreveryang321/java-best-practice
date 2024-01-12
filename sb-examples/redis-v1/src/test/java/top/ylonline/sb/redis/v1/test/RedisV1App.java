package top.ylonline.sb.redis.v1.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 * @author YL
 */
@SpringBootApplication
public class RedisV1App {
    public static void main(String[] args) {
        SpringApplication.run(RedisV1App.class, args);
        synchronized (RedisV1App.class) {
            while (true) {
                try {
                    RedisV1App.class.wait();
                } catch (Exception e) {
                    // ignore
                    break;
                }
            }
        }
    }
}
