package top.ylonline.sb.redis.v2.cache;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import top.ylonline.common.cache.util.CacheUtils;

import java.time.Duration;
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

    /**
     * 该实现主要解决了 {@link top.ylonline.common.cache.interceptor.TCacheResolver} 中构建了新 cacheName，导致 CachePut、CacheEvict
     * 等使用失效问题。
     *
     * @param name {@link top.ylonline.common.cache.interceptor.TCacheResolver} 中生成的新的 cacheName
     * @return cache 实例
     */
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
