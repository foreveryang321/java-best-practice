package top.ylonline.dubbo.spring.boot;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Collections;

/**
 * Hello world!
 *
 * @author YL
 */
@SpringBootApplication
@DubboComponentScan(basePackages = {"top.ylonline.dubbo.spring.boot"})
@Slf4j
public class DubboSpringBootProviderExample {

    /**
     * Common
     */
    private static SpringApplicationBuilder configureSpringBuilder(SpringApplicationBuilder builder) {
        // builder.application().addListeners(new EnvironmentPreparedEventListener());
        builder.application().addPrimarySources(Collections.singletonList(DubboSpringBootProviderExample.class));
        return builder.sources(DubboSpringBootProviderExample.class);
    }

    public static void main(String[] args) {
        configureSpringBuilder(new SpringApplicationBuilder())
                .application()
                .run(args);
    }
}

