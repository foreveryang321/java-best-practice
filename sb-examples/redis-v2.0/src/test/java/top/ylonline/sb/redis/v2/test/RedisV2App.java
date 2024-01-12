package top.ylonline.sb.redis.v2.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author YL
 */
@SpringBootApplication
public class RedisV2App {
    public static void main(String[] args) {
        SpringApplication.run(RedisV2App.class, args);
        synchronized (RedisV2App.class) {
            while (true) {
                try {
                    RedisV2App.class.wait();
                } catch (Exception e) {
                    // ignore
                    break;
                }
            }
        }
    }
}
