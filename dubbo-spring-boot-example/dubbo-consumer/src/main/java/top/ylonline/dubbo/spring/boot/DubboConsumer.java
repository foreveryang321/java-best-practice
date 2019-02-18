package top.ylonline.dubbo.spring.boot;

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
@DubboComponentScan(basePackages = {"top.ylonline.dubbo.spring.boot"})
@Slf4j
public class DubboConsumer {

    /**
     * Common
     */
    private static SpringApplicationBuilder configureSpringBuilder(SpringApplicationBuilder builder) {
        // builder.application().addListeners(new EnvironmentPreparedEventListener());
        builder.application().addPrimarySources(Collections.singletonList(DubboConsumer.class));
        return builder.sources(DubboConsumer.class);
    }

    public static void main(String[] args) {
        configureSpringBuilder(new SpringApplicationBuilder())
                .application()
                .run(args);
    }
}

