package top.ylonline.sb.redis.v1.cache;

import top.ylonline.sb.redis.v1.core.EnableTRedisConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义缓存过去注解。
 * <p>
 * 如果要使用 &#64;{@link CacheExpire}注解，需要启用 &#64;{@link EnableTRedisConfiguration}
 *
 * @author YL
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheExpire {
    /**
     * expire time, default 60s.
     */
    long value() default 60;
}
