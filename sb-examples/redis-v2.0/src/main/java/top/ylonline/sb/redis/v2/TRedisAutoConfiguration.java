package top.ylonline.sb.redis.v2;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import top.ylonline.common.cache.interceptor.TCacheErrorHandler;
import top.ylonline.common.cache.interceptor.TCacheResolver;
import top.ylonline.common.cache.interceptor.TKeyGenerator;
import top.ylonline.common.util.StrUtils;
import top.ylonline.sb.redis.v2.anno.EnableTRedisConfiguration;
import top.ylonline.sb.redis.v2.cache.TRedisCacheManager;

import java.time.Duration;

/**
 * 初始化 redis 相关
 *
 * @author YL
 * @since spring-data-redis 2.0.0.RELEASE
 */
@Configuration
@ConditionalOnBean(
        annotation = {
                EnableTRedisConfiguration.class
        }
)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties({RedisProperties.class, CacheProperties.class})
@EnableCaching(proxyTargetClass = true)
@Slf4j
public class TRedisAutoConfiguration extends CachingConfigurerSupport {
    private final RedisConnectionFactory redisConnectionFactory;
    private final CacheProperties cacheProperties;

    TRedisAutoConfiguration(RedisConnectionFactory redisConnectionFactory, CacheProperties cacheProperties) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.cacheProperties = cacheProperties;
    }

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
        StringRedisSerializer serializer = TRedisCacheManager.STRING_SERIALIZER;
        // 设置key序列化类，否则key前面会多了一些乱码
        template.setKeySerializer(serializer);
        template.setHashKeySerializer(serializer);

        // fastjson serializer
        GenericFastJsonRedisSerializer fastSerializer = TRedisCacheManager.FASTJSON_SERIALIZER;
        template.setValueSerializer(fastSerializer);
        template.setHashValueSerializer(fastSerializer);
        // 如果 KeySerializer 或者 ValueSerializer 没有配置，则对应的 KeySerializer、ValueSerializer 才使用这个 Serializer
        template.setDefaultSerializer(fastSerializer);

        if (log.isInfoEnabled()) {
            log.info("redis: {}", redisConnectionFactory);
            if (redisConnectionFactory instanceof LettuceConnectionFactory) {
                LettuceConnectionFactory factory = (LettuceConnectionFactory) redisConnectionFactory;
                log.info("spring.redis.database: {}", factory.getDatabase());
                log.info("spring.redis.host: {}", factory.getHostName());
                log.info("spring.redis.port: {}", factory.getPort());
                log.info("spring.redis.timeout: {}", factory.getTimeout());
                log.info("spring.redis.password: {}", factory.getPassword());
            } else if (redisConnectionFactory instanceof JedisConnectionFactory) {
                JedisConnectionFactory factory = (JedisConnectionFactory) redisConnectionFactory;
                log.info("spring.redis.database: {}", factory.getDatabase());
                log.info("spring.redis.host: {}", factory.getHostName());
                log.info("spring.redis.port: {}", factory.getPort());
                log.info("spring.redis.timeout: {}", factory.getTimeout());
                log.info("spring.redis.password: {}", factory.getPassword());
            }
        }
        // factory
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
        // 初始化一个 nonLocking RedisCacheWriter
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

        CacheProperties.Redis redis = cacheProperties.getRedis();
        // 获取默认过期时间
        Duration ttl = redis.getTimeToLive();
        if (ttl == null) {
            // 如果没有配置，则默认过期时间为 30 min
            ttl = Duration.ofMinutes(30);
        }
        String keyPrefix = redis.getKeyPrefix();
        boolean cacheNullValues = redis.isCacheNullValues();
        boolean useKeyPrefix = redis.isUseKeyPrefix();
        // 设置默认过期时间：30 分钟
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                // 使用注解时 key、value 的序列化方式
                .serializeKeysWith(TRedisCacheManager.STRING_PAIR)
                .serializeValuesWith(TRedisCacheManager.FASTJSON_PAIR);
        if (useKeyPrefix && StrUtils.isNotBlank(keyPrefix)) {
            defaultCacheConfig = defaultCacheConfig.prefixKeysWith(keyPrefix);
        }
        if (!cacheNullValues) {
            defaultCacheConfig = defaultCacheConfig.disableCachingNullValues();
        }
        return new TRedisCacheManager(cacheWriter, defaultCacheConfig);
    }
}
