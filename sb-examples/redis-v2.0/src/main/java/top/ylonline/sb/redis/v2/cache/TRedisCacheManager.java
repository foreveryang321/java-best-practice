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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
public class TRedisCacheManager extends RedisCacheManager {
    @Getter
    private final RedisCacheConfiguration defaultCacheConfig;

    public TRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig,
                              boolean allowInFlightCacheCreation, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfig, allowInFlightCacheCreation, initialCacheNames);
        this.defaultCacheConfig = defaultCacheConfig;
    }

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    @Override
    public Cache getCache(String name) {
        if (log.isDebugEnabled()) {
            log.debug("getCache ---> name: {}", name);
        }
        // Quick check for existing cache...
        Cache cache = this.cacheMap.get(name);
        if (cache != null) {
            return cache;
        }
        String[] array = CacheUtils.splitCacheNameForTtl(name);
        String cacheName = array[0];
        cache = super.getCache(cacheName);
        if (cache != null && array.length > 1) {
            long ttl = Long.parseLong(array[1]);
            if (log.isDebugEnabled()) {
                log.debug("getCache ---> cacheName: {}, ttl: {}", cacheName, ttl);
            }
            RedisCacheConfiguration cacheConfiguration = this.defaultCacheConfig
                    .entryTtl(Duration.ofSeconds(ttl));
            cache = super.createRedisCache(cacheName, cacheConfiguration);
            cacheMap.put(name, cache);
        }
        return cache;
    }
}
