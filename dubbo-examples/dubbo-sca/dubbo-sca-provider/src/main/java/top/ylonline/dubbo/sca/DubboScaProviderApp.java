package top.ylonline.dubbo.sca;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author YL
 */
@SpringBootApplication
@DubboComponentScan(basePackages = "top.ylonline.dubbo.sca")
@EnableDiscoveryClient
public class DubboScaProviderApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboScaProviderApp.class)
                .properties("spring.profiles.active=zk")
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
