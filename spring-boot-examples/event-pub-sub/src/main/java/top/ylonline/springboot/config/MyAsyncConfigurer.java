package top.ylonline.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步执行线程池配置
 *
 * @author YL
 */
@Configuration
public class MyAsyncConfigurer implements AsyncConfigurer {

    @Bean("eventThreadPoolTaskExecutor")
    public Executor eventThreadPoolTaskExecutor() {
        return executor(5, 10, 20, 30);
    }

    @Override
    public Executor getAsyncExecutor() {
        return executor(10, 128, 256, 60);
    }

    private Executor executor(int coreSize, int maxSize, int queueSize, int keepAlive) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(queueSize);
        executor.setKeepAliveSeconds(keepAlive);
        executor.setThreadNamePrefix("async-executor-");
        executor.initialize();
        return executor;
    }
}
