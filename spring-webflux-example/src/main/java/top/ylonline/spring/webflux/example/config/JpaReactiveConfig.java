package top.ylonline.spring.webflux.example.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

/**
 * @author YL
 */
@Configuration
public class JpaReactiveConfig {
    @Value("${spring.datasource.maximum-pool-size:99}")
    private int connectionPoolSize;

    @Bean
    @Qualifier("jdbcScheduler")
    public Scheduler jdbcScheduler() {
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(connectionPoolSize));
    }

    // @Bean
    // public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
    //     return new TransactionTemplate(transactionManager);
    // }
}
