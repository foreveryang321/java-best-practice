package top.ylonline.common.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义缓存过期时间配置注解，过期时间单位：s（秒）
 *
 * @author YL
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Expired {
    /**
     * expire time, default 60s.
     */
    long value() default 60;

    /**
     * Spring Expression Language (SpEL) expression for computing the expire time dynamically.
     *
     * <p>
     * 与 {@link #value()} 属性互斥. 使用该属性配置的过期时间优先级比 {@link #value()} 属性高。由于该实现是重新构建一个cacheName，这样会导致配合CachePut
     * 、CacheEvict等使用时会有问题。
     * </p>
     */
    String spEl() default "";
}
