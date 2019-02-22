package top.ylonline.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author YL
 */
@SpringBootApplication
public class SentinelClusterClientApp {

    public static void main(String[] args) {
        SpringApplication.run(SentinelClusterClientApp.class, args);
        synchronized (SentinelClusterClientApp.class) {
            while (true) {
                try {
                    SentinelClusterClientApp.class.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
