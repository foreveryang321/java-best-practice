package top.ylonline.common.cache.annotation;

import java.lang.annotation.*;

/**
 * 自定义缓存过期时间配置注解，过期时间单位：s（秒）
 * <pre>
 *     1、{@link CacheExpired#el()}优先级比{@link CacheExpired#value()}高，优先使用{@link CacheExpired#el()}的配置
 * </pre>
 *
 * @author YL
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheExpired {
    /**
     * 过期时间，单位：秒，默认：-1（使用全局默认过期时间）
     * <pre>
     *     与 {@link #el()} 属性互斥，优先使用 {@link #el()} 配置。
     * </pre>
     */
    long value() default -1;

    /**
     * 过期时间，单位：秒
     * Spring Expression Language (SpEL) expression for computing the expiration time dynamically.
     * <pre>
     *     与 {@link #value()} 属性互斥，优先使用当前配置。
     * </pre>
     */
    String el() default "";
}
