package top.ylonline.dubbo.spring.boot.example;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ImportResource;

import java.util.Collections;

/**
 * Hello world!
 *
 * @author YL
 */
@SpringBootApplication
@ImportResource(locations = {"classpath:dubbo-consumer-${spring.profiles.active}.xml"})
@DubboComponentScan(basePackages = {"top.ylonline.dubbo.spring.boot.example"})
@Slf4j
public class DubboSpringBootConsumerExample {

    /**
     * Common
     */
    private static SpringApplicationBuilder configureSpringBuilder(SpringApplicationBuilder builder) {
        // builder.application().addListeners(new EnvironmentPreparedEventListener());
        builder.application().addPrimarySources(Collections.singletonList(DubboSpringBootConsumerExample.class));
        return builder.sources(DubboSpringBootConsumerExample.class);
    }

    public static void main(String[] args) {
        configureSpringBuilder(new SpringApplicationBuilder())
                .application()
                .run(args);
    }
}

