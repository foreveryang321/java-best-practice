package top.ylonline.springboot.event.listener;// package top.ylonline.springboot.event.listener;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.context.ApplicationListener;
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.stereotype.Component;
// import top.ylonline.springboot.event.event.UserEvent;
//
// import java.util.concurrent.TimeUnit;
//
// /**
//  * @author YL
//  */
// @Component
// @Slf4j
// public class UserEventListener implements ApplicationListener<UserEvent> {
//
//     @Async("eventThreadPoolTaskExecutor")
//     @Override
//     public void onApplicationEvent(UserEvent event) {
//         log.info("开始：监听到 userEvent ---> user event: {}", event);
//         try {
//             TimeUnit.SECONDS.sleep(5);
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
//         log.info("结束：监听到 userEvent ---> user event: {}", event);
//     }
// }
