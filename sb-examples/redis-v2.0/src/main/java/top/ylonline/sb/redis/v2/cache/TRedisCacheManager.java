package top.ylonline.sb.redis.v2.cache;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ReflectionUtils;
import top.ylonline.common.cache.annotation.Expired;
import top.ylonline.common.cache.util.CacheUtils;
import top.ylonline.common.util.StrUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 自定义 RedisCacheManager 缓存管理器
 * <pre>
 *     support spring-data-redis 1.8.10.RELEASE
 *     not support spring-data-redis 2.0.0.RELEASE +
 * </pre>
 *
 * @author YL
 */
@Slf4j
public class TRedisCacheManager extends RedisCacheManager implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;

    private Map<String, RedisCacheConfiguration> initialCacheConfiguration = new LinkedHashMap<>();

    /**
     * key serializer
     */
    public static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();

    /**
     * value serializer
     * <pre>
     *     使用 FastJsonRedisSerializer 会报错：java.lang.ClassCastException
     *     FastJsonRedisSerializer<Object> fastSerializer = new FastJsonRedisSerializer<>(Object.class);
     * </pre>
     */
    public static final GenericFastJsonRedisSerializer FASTJSON_SERIALIZER = new GenericFastJsonRedisSerializer();

    /**
     * key serializer pair
     */
    public static final RedisSerializationContext.SerializationPair<String> STRING_PAIR = RedisSerializationContext
            .SerializationPair.fromSerializer(STRING_SERIALIZER);
    /**
     * value serializer pair
     */
    public static final RedisSerializationContext.SerializationPair<Object> FASTJSON_PAIR = RedisSerializationContext
            .SerializationPair.fromSerializer(FASTJSON_SERIALIZER);

    @Getter
    private RedisCacheConfiguration defaultCacheConfig;

    public TRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig) {
        super(cacheWriter, defaultCacheConfig);
        this.defaultCacheConfig = defaultCacheConfig;
    }

    @Override
    public Cache getCache(String name) {
        return super.getCache(name);
    }

    @Override
    protected RedisCache getMissingCache(String name) {
        long ttl = CacheUtils.computeTtl(name, defaultCacheConfig.getTtl().getSeconds());

        RedisCacheConfiguration config = getRedisCacheConfiguration(name, Duration.ofSeconds(ttl));
        return super.createRedisCache(name, config);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            final Class<?> clazz = applicationContext.getType(beanName);
            doWith(clazz);
        }
        super.afterPropertiesSet();
    }

    @Override
    protected Collection<RedisCache> loadCaches() {
        List<RedisCache> caches = new LinkedList<>();
        for (Map.Entry<String, RedisCacheConfiguration> entry : initialCacheConfiguration.entrySet()) {
            caches.add(super.createRedisCache(entry.getKey(), entry.getValue()));
        }
        return caches;
    }

    private void doWith(final Class<?> clazz) {
        ReflectionUtils.doWithMethods(clazz, method -> {
            ReflectionUtils.makeAccessible(method);
            Expired expired = AnnotationUtils.findAnnotation(method, Expired.class);
            if (expired == null) {
                return;
            }
            long expire = expired.value();
            if (expire >= 0) {
                Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
                Caching caching = AnnotationUtils.findAnnotation(method, Caching.class);
                CacheConfig cacheConfig = AnnotationUtils.findAnnotation(clazz, CacheConfig.class);
                List<String> cacheNames = CacheUtils.getCacheNames(cacheable, caching, cacheConfig);
                add(cacheNames, expire);
            }
        }, method -> null != AnnotationUtils.findAnnotation(method, Expired.class));
    }

    private void add(List<String> cacheNames, long expire) {
        for (String cacheName : cacheNames) {
            if (cacheName == null || "".equals(cacheName.trim())) {
                continue;
            }
            if (log.isInfoEnabled()) {
                log.info("cacheName: {}, expire#value: {}s", cacheName, expire);
            }
            RedisCacheConfiguration config = getRedisCacheConfiguration(cacheName, Duration.ofSeconds(expire));
            initialCacheConfiguration.put(cacheName, config);
        }
    }

    private RedisCacheConfiguration getRedisCacheConfiguration(String cacheName, Duration ttl) {
        boolean allowCacheNullValues = defaultCacheConfig.getAllowCacheNullValues();
        boolean useKeyPrefix = defaultCacheConfig.usePrefix();
        String keyPrefix = defaultCacheConfig.getKeyPrefixFor(cacheName);
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .serializeKeysWith(TRedisCacheManager.STRING_PAIR)
                .serializeValuesWith(TRedisCacheManager.FASTJSON_PAIR);
        if (useKeyPrefix && StrUtils.isNotBlank(keyPrefix)) {
            config = config.prefixKeysWith(keyPrefix);
        }
        if (!allowCacheNullValues) {
            config = config.disableCachingNullValues();
        }
        return config;
    }
}
