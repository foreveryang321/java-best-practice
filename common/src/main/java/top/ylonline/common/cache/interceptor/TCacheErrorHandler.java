package top.ylonline.common.cache.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * 自定义缓存异常处理程序
 * <pre>
 *     解决的问题：
 *     Redis 容易出现缓存问题（超时、Redis 宕机等），当使用 spring cache 的注释 Cacheable、Cacheput 等处理缓存问题时，
 *     我们无法使用 try catch 处理出现的异常，所以最后导致结果是整个服务报错无法正常工作。
 *     通过自定义 {@link TCacheErrorHandler} 来处理异常可以解决这个问题。
 * </pre>
 * <p>
 * 缓存仅仅是为了业务更快地查询而存在的，如果因为缓存操作失败导致正常的业务流程失败，有点得不偿失了。
 * 因此需要开发者自定义 CacheErrorHandler 处理缓存读写的异常。
 * </p>
 *
 * <p>
 * 如果缓存写发生了异常，就可能导致数据库的数据和缓存的数据不一致的问题。
 * 为了解决该问题，需要继续扩展 CacheErrorHandler 的 handleCachePutError 和 handleCacheEvictError 方法。
 * 思路就是将缓存写操作失败的 key 保存下来，通过重试任务删除这些 key 对应的缓存解决数据库数据与缓存数据不一致的问题。
 * </p>
 *
 * @author YL
 */
@Slf4j
public class TCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.error("key: {}, msg: {}", key, exception.getMessage(), exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.error("key: {}, value: {}, msg: {}", key, value, exception.getMessage(), exception);
        // 写入失败时，尝试删除缓存，如果 evict 也出现异常，则可能会直接抛给应用，这里需要 try catch 处理一下
        try {
            cache.evict(key);
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.error("key: {}, msg: {}", key, exception.getMessage(), exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        throw exception;
    }
}
