package top.ylonline.common.cache.util;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 缓存工具
 *
 * @author YL
 */
public abstract class CacheUtils {
    /**
     * 获取 cacheNames
     * <pre>
     *     处理优先级 cacheable = caching > cacheConfig
     * </pre>
     *
     * @param cacheable   缓存注解 Cacheable
     * @param caching     缓存注解 Caching
     * @param cacheConfig 缓存注解 CacheConfig
     */
    public static List<String> getCacheNames(Cacheable cacheable, Caching caching, CacheConfig cacheConfig) {
        List<String> list = new ArrayList<>();
        // Cacheable
        if (cacheable != null) {
            String[] cacheNames = cacheable.cacheNames();
            if (cacheNames.length > 0) {
                list.addAll(Arrays.asList(cacheNames));
            }
        }

        // Caching
        if (caching != null) {
            Cacheable[] cacheables = caching.cacheable();
            for (Cacheable cache : cacheables) {
                String[] cacheNames = cache.cacheNames();
                if (cacheNames.length > 0) {
                    list.addAll(Arrays.asList(cacheNames));
                }
            }
        }
        if (list.size() > 0) {
            return list;
        }

        // CacheConfig
        if (cacheConfig != null) {
            String[] cacheNames = cacheConfig.cacheNames();
            if (cacheNames.length > 0) {
                list.addAll(Arrays.asList(cacheNames));
            }
        }
        return list;
    }

    private static final String SYMBOL = "#";
    private static final Pattern PATTERN_TTL = Pattern.compile(SYMBOL + "(\\d+)");

    /**
     * 构建一个新的 cacheName，已实现动态过期时间配置
     *
     * @param cacheName 原始 cacheName
     * @param ttl       过期时间
     */
    public static String buildCacheNameForTtl(String cacheName, long ttl) {
        return cacheName + SYMBOL + ttl;
    }

    /**
     * 计算过期时间
     *
     * @param cacheName  缓存名称
     * @param defaultTtl 默认过期时间，单位：s（秒）
     */
    public static long computeTtl(String cacheName, long defaultTtl) {
        Matcher matcher = PATTERN_TTL.matcher(cacheName);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return defaultTtl;
    }
}
