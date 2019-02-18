package top.ylonline.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author YL
 */
@SpringBootApplication
public class SentinelClusterServerApp {

    public static void main(String[] args) {
        SpringApplication.run(SentinelClusterServerApp.class, args);
        synchronized (SentinelClusterServerApp.class) {
            while (true) {
                try {
                    SentinelClusterServerApp.class.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
