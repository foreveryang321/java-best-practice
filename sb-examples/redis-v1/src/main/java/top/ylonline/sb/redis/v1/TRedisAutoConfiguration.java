package top.ylonline.sb.redis.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import top.ylonline.common.cache.interceptor.TCacheErrorHandler;
import top.ylonline.common.cache.interceptor.TCacheResolver;
import top.ylonline.common.cache.interceptor.TKeyGenerator;
import top.ylonline.sb.redis.v1.cache.TRedisCacheManager;

import java.util.List;

/**
 * 初始化 Redis 相关 Bean
 * <pre>
 *     support spring-data-redis 1.8.10.RELEASE
 *     not support spring-data-redis 2.0.0.RELEASE +
 * </pre>
 *
 * @author YL
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnBean(RedisTemplate.class)
@EnableConfigurationProperties({RedisProperties.class, CacheProperties.class})
@EnableCaching(proxyTargetClass = true)
@Slf4j
@RequiredArgsConstructor
public class TRedisAutoConfiguration extends CachingConfigurerSupport {
    private final CacheProperties cacheProperties;
    private final RedisTemplate<Object, Object> redisTemplate;

    /**
     * 如果 @Cacheable、@CachePut、@CacheEvict 等注解没有配置 key，则使用这个自定义 key 生成器
     * <pre>
     *     但自定义了缓存的 key 时，难以保证 key 的唯一性，此时最好指定方法名，比如：@Cacheable(value="", key="{#root.methodName, #id}")
     * </pre>
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new TKeyGenerator();
    }

    @Override
    public CacheResolver cacheResolver() {
        return new TCacheResolver(cacheManager());
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new TCacheErrorHandler();
    }

    /**
     * 配置 RedisCacheManager，使用 cache 注解管理 redis 缓存
     * <pre>
     *     这里一定要加上&#64;{@link Bean}注解
     * </pre>
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        return cacheManager(redisTemplate);
    }

    /**
     * 以下代码参考 {@link org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration}
     */
    public RedisCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
        TRedisCacheManager cacheManager = new TRedisCacheManager(redisTemplate);
        cacheManager.setUsePrefix(true);
        List<String> cacheNames = this.cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            cacheManager.setCacheNames(cacheNames);
        }
        // 配置使用默认的过期时间：1 小时
        cacheManager.setDefaultExpiration(60 * 60L);
        return cacheManager;
    }
}
