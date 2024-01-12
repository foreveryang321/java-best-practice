package top.ylonline.sb.redis.v1.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;
import top.ylonline.common.cache.util.CacheUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * redis 缓存管理器
 * <pre>
 *     support spring-data-redis 1.8.10.RELEASE
 *     not support spring-data-redis 2.0.0.RELEASE +
 * </pre>
 * 解决的问题：
 * <pre>
 *      Redis 容易出现缓存问题（超时、Redis 宕机等），当使用 spring cache 的注释 Cacheable、Cacheput 等处理缓存问题时，
 *      我们无法使用 try catch 处理出现的异常，所以最后导致结果是整个服务报错无法正常工作。
 *      通过自定义 TRedisCacheManager 并继承 RedisCacheManager 来处理异常可以解决这个问题。
 * </pre>
 *
 * @author YL
 */
@Slf4j
public class TRedisCacheManager extends RedisCacheManager {

    public TRedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
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
            cache = this.createCache(cacheName, ttl);
            cacheMap.put(name, cache);
        }
        return cache;
    }

    protected RedisCache createCache(String cacheName, long expiration) {
        return new RedisCache(cacheName, this.isUsePrefix() ? this.getCachePrefix().prefix(cacheName) : null,
                this.getRedisOperations(), expiration, false);
    }

    @Override
    protected Cache getMissingCache(String name) {
        return this.createCache(name);
    }
}
