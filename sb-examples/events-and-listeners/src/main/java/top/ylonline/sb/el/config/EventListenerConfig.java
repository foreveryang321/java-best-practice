package top.ylonline.sb.el.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import top.ylonline.sb.el.domain.User;
import top.ylonline.sb.el.event.UserEvent;

import java.util.concurrent.TimeUnit;

/**
 * @author YL
 */
@Configuration
@Slf4j
public class EventListenerConfig {

    // @Async("eventThreadPoolTaskExecutor")
    // @EventListener
    // public void handleEvent(Object event) {
    //     log.info("监听所有事件 ---> event: {}", event);
    //     try {
    //         TimeUnit.SECONDS.sleep(5);
    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     }
    //     log.info("监听所有事件 ---> event: {}", event);
    // }

    @Async("eventThreadPoolTaskExecutor")
    @EventListener
    public void handleUserEvent(UserEvent event) {
        log.info("开始：监听到 userEvent 事件 ---> event: {}, user event: {}, time: {}", event, event.getUser(),
                event.getTimestamp());
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("结束：监听到 userEvent 事件 ---> event: {}, user event: {}, time: {}", event, event.getUser(),
                event.getTimestamp());
    }

    @Async("eventThreadPoolTaskExecutor")
    @EventListener
    public void handleUser(User user) {
        log.info("开始：监听到 user 对象 ---> user: {}", user);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("结束：监听到 user 对象 ---> user: {}", user);
    }
}
