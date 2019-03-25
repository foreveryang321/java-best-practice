package top.ylonline.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ylonline.springboot.event.event.UserEvent;
import top.ylonline.springboot.event.model.User;

import javax.annotation.Resource;

/**
 * Hello world!
 *
 * @author YL
 */
@SpringBootApplication
@EnableAsync
@RestController
@Slf4j
public class EventApp {
    /**
     * Common
     */
    private static SpringApplicationBuilder configureSpringBuilder(SpringApplicationBuilder builder) {
        // builder.listeners(new UserEventListener());
        return builder.sources(EventApp.class);
    }

    /**
     * for JAR deploy
     */
    public static void main(String[] args) {
        configureSpringBuilder(new SpringApplicationBuilder())
                .run(args);
    }

    @Resource
    private ApplicationEventPublisher publisher;

    @GetMapping("/publishObject")
    public String publishObject() {
        publisher.publishEvent(User.builder().id(123L).name("foreveryang123").age(11).build());
        log.info("发布 object 对象成功");
        return "publishObject 发布成功";
    }

    @GetMapping("/publishEvent")
    public String publishEvent() {
        publisher.publishEvent(new UserEvent(this, User.builder().id(456L).name("foreveryang456").age(22).build()));
        log.info("发布 event 成功");
        return "publishEvent 发布成功";
    }
}
