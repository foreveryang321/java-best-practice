package top.ylonline.sb.redis.v1.core;

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
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.ReflectionUtils;
import top.ylonline.sb.redis.v1.cache.CacheExpire;
import top.ylonline.sb.redis.v1.cache.CacheUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
class TRedisCacheManager extends RedisCacheManager implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;

    private Map<String, Long> expires = new LinkedHashMap<>();

    TRedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
        log.info("Initialize RedisCacheManager. redisOperations: {}", redisOperations);
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = super.getCache(name);
        return new TRedisCacheWrapper(cache);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            Class clazz = applicationContext.getType(beanName);
            doWith(clazz);
        }
        //设置有效期
        if (!expires.isEmpty()) {
            super.setExpires(expires);
        }
    }

    private void doWith(final Class clazz) {
        ReflectionUtils.doWithMethods(clazz, method -> {
            CacheExpire cacheExpire = AnnotationUtils.findAnnotation(method, CacheExpire.class);
            // cacheNames 配置优先级：Cacheable > Caching > CacheConfig
            Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
            Caching caching = AnnotationUtils.findAnnotation(method, Caching.class);
            CacheConfig cacheConfig = AnnotationUtils.findAnnotation(clazz, CacheConfig.class);

            List<String> cacheNames = CacheUtils.getCacheNames(cacheable, caching, cacheConfig);
            putExpires(cacheNames, cacheExpire);
        }, method -> AnnotationUtils.findAnnotation(method, CacheExpire.class) != null);
    }

    private void putExpires(List<String> cacheNames, CacheExpire cacheExpire) {
        for (String cacheName : cacheNames) {
            if (cacheName == null || "".equals(cacheName.trim())) {
                continue;
            }
            long expire = cacheExpire.value();
            if (log.isDebugEnabled()) {
                log.debug("cacheNames: {}, expire: {}s", cacheNames, expire);
            }
            if (expire >= 0) {
                expires.put(cacheName, expire);
            } else {
                log.warn("{} use default expiration.", cacheName);
            }
        }
    }
}
