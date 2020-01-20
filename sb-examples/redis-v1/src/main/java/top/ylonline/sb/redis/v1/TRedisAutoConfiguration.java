package top.ylonline.sb.redis.v1;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
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
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import top.ylonline.common.cache.interceptor.TCacheErrorHandler;
import top.ylonline.common.cache.interceptor.TCacheResolver;
import top.ylonline.common.cache.interceptor.TKeyGenerator;
import top.ylonline.sb.redis.v1.cache.TRedisCacheManager;

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
@EnableConfigurationProperties({RedisProperties.class, CacheProperties.class})
@EnableCaching(proxyTargetClass = true)
@Slf4j
public class TRedisAutoConfiguration extends CachingConfigurerSupport {
    private final RedisConnectionFactory redisConnectionFactory;

    TRedisAutoConfiguration(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    private static final StringRedisSerializer SERIALIZER = new StringRedisSerializer();
    /**
     * fastjson serializer
     * <pre>
     *     使用 FastJsonRedisSerializer 会报错：java.lang.ClassCastException
     *     FastJsonRedisSerializer<Object> fastSerializer = new FastJsonRedisSerializer<>(Object.class);
     * </pre>
     */
    private static final GenericFastJsonRedisSerializer FAST_SERIALIZER = new GenericFastJsonRedisSerializer();

    /**
     * 配置 RedisTemplate，设置序列化器
     * <pre>
     *     在类里面配置 RestTemplate，需要配置 key 和 value 的序列化类。
     *     key 序列化使用 StringRedisSerializer, 不配置的话，key 会出现乱码。
     * </pre>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // set key serializer
        // 设置key序列化类，否则key前面会多了一些乱码
        template.setKeySerializer(SERIALIZER);
        template.setHashKeySerializer(SERIALIZER);

        // fastjson serializer
        template.setValueSerializer(FAST_SERIALIZER);
        template.setHashValueSerializer(FAST_SERIALIZER);
        // 如果 KeySerializer 或者 ValueSerializer 没有配置，则对应的 KeySerializer、ValueSerializer 才使用这个 Serializer
        template.setDefaultSerializer(FAST_SERIALIZER);

        JedisConnectionFactory factory = (JedisConnectionFactory) redisConnectionFactory;
        if (log.isInfoEnabled()) {
            log.info("spring.redis.database: {}", factory.getDatabase());
            log.info("spring.redis.host: {}", factory.getHostName());
            log.info("spring.redis.port: {}", factory.getPort());
            log.info("spring.redis.timeout: {}", factory.getTimeout());
            log.info("spring.redis.password: {}", factory.getPassword());
            JedisPoolConfig pool = factory.getPoolConfig();
            log.info("spring.redis.use-pool: {}", factory.getUsePool());
            log.info("spring.redis.min-idle: {}", pool.getMinIdle());
            log.info("spring.redis.max-idle: {}", pool.getMaxIdle());
            log.info("spring.redis.max-active: {}", pool.getMaxTotal());
            log.info("spring.redis.max-wait: {}", pool.getMaxWaitMillis());
            log.info("spring.redis.test-on-borrow: {}", pool.getTestOnBorrow());
            log.info("spring.redis.test-on-create: {}", pool.getTestOnCreate());
            log.info("spring.redis.test-while-idle: {}", pool.getTestWhileIdle());
            log.info("spring.redis.time-between-eviction-runs-millis: {}", pool
                    .getTimeBetweenEvictionRunsMillis());
        }

        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }

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
        TRedisCacheManager manager = new TRedisCacheManager(redisTemplate());
        // manager.setLoadRemoteCachesOnStartup(true);
        // 如果没有配置过期时间，则使用默认的过期时间：30 min
        manager.setDefaultExpiration(30 * 60L);
        // 注 默认会添加 key 前缀以防止两个单独的缓存使用相同的 key，否则 Redis 将存在重复的 key，有可能返回不可用的值。
        // 如果创建自己的 RedisCacheManager，强烈建议你保留该配置处于启用状态。
        manager.setUsePrefix(true);
        return manager;
    }
}
