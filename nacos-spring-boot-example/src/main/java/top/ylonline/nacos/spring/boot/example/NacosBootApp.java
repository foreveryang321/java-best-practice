package top.ylonline.nacos.spring.boot.example;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableNacosConfig
@NacosPropertySources({
        @NacosPropertySource(dataId = NacosBootApp.DATAID)
})
@RestController
@Slf4j
public class NacosBootApp {
    static final String DATAID = "nacos-spring-boot-example.properties";

    public static void main(String[] args) {
        SpringApplication.run(NacosBootApp.class, args);
    }

    @NacosConfigListener(
            dataId = NacosBootApp.DATAID,
            timeout = 500
    )
    public void onChangeListener(String newContent) throws Exception {
        log.info("------------------------------------------------------------------");
        log.info("{}", newContent);
        log.info("------------------------------------------------------------------");
    }

    @NacosValue(value = "${example.id}", autoRefreshed = true)
    private int id;
    @Value("${example.name}")
    private String name;

    @GetMapping("/demo")
    public String demo() {
        return String.format("id: %s, name: %s", id, name);
    }
}
