package top.ylonline.dubbo27x;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;

/**
 * @author YL
 */
@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                WebSocketServletAutoConfiguration.class,
                JmxAutoConfiguration.class
        }
)
// @ImportResource(locations = {"classpath:dubbo-consumer-${spring.profiles.active}.xml"})
public class DubboConsumer270 {

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumer270.class, args);
    }
}

