package top.ylonline.sb.redis.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.util.CollectionUtils;
import top.ylonline.common.cache.interceptor.TCacheErrorHandler;
import top.ylonline.common.cache.interceptor.TCacheResolver;
import top.ylonline.common.cache.interceptor.TKeyGenerator;
import top.ylonline.sb.redis.v2.cache.TRedisCacheManager;

import java.time.Duration;
import java.util.List;

/**
 * 初始化 redis 相关
 *
 * @author YL
 * @since spring-data-redis 2.0.0.RELEASE
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass(value = {RedisConnectionFactory.class, RedisOperations.class})
@ConditionalOnBean(RedisConnectionFactory.class)
@EnableConfigurationProperties({RedisProperties.class, CacheProperties.class})
@EnableCaching(proxyTargetClass = true)
@Slf4j
@RequiredArgsConstructor
public class TRedisAutoConfiguration extends CachingConfigurerSupport {
    private final CacheProperties cacheProperties;
    private final RedisConnectionFactory redisConnectionFactory;
    private final ResourceLoader resourceLoader;

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

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new TCacheResolver(cacheManager());
    }

    @Bean
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
        RedisCacheConfiguration defaultCacheConfig = determineConfiguration(resourceLoader.getClassLoader());
        // 初始化一个 nonLocking RedisCacheWriter
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        boolean enableStatistics = false;
        boolean allowInFlightCacheCreation = true;
        List<String> cacheNames = cacheProperties.getCacheNames();
        String[] initialCacheNames = new String[]{};
        if (!CollectionUtils.isEmpty(cacheNames)) {
            initialCacheNames = cacheNames.toArray(new String[0]);
        }
        TRedisCacheManager cm = new TRedisCacheManager(cacheWriter, defaultCacheConfig, allowInFlightCacheCreation, initialCacheNames);
        cm.setTransactionAware(enableStatistics);
        return cm;
    }

    /**
     * 以下代码参考 {@link org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration}
     */
    private org.springframework.data.redis.cache.RedisCacheConfiguration determineConfiguration(
            ClassLoader classLoader) {
        CacheProperties.Redis redisProperties = this.cacheProperties.getRedis();
        org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration
                .defaultCacheConfig();
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
        Duration timeToLive = redisProperties.getTimeToLive();
        if (timeToLive == null) {
            // 因为未配置 spring.cache.redis.time-to-live，将其设置为 1 小时。
            timeToLive = Duration.ofHours(1);
            log.warn("Because spring.cache.redis.time-to-live is not configured, set it to 1 hour.");
        }
        config = config.entryTtl(timeToLive);
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
