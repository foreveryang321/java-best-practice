package top.ylonline.common.cache.util;

/**
 * 缓存工具
 *
 * @author YL
 */
public final class CacheUtils {
    private static final String SYMBOL = "#";

    /**
     * 构建一个新的 cacheName，以实现动态过期时间配置
     *
     * @param cacheName 原始 cacheName
     * @param ttl       过期时间
     */
    public static String buildCacheNameForTtl(String cacheName, long ttl) {
        return cacheName + SYMBOL + ttl;
    }

    public static String[] splitCacheNameForTtl(String cacheNameForTtl) {
        return cacheNameForTtl.split(SYMBOL, -1);
    }
}
