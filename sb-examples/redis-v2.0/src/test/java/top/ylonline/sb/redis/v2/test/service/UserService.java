package top.ylonline.sb.redis.v2.test.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ylonline.common.cache.annotation.Expired;
import top.ylonline.sb.redis.v2.test.domain.User;

/**
 * @author YL
 */
@Service
public class UserService {
    /**
     * 使用默认
     */
    @Cacheable(
            value = "redis.v2.none",
            unless = "#result == null")
    public User none(Long id, String firstName, String lastName) {
        return new User(id, firstName, lastName);
    }

    /**
     * 指定具体过期时间
     */
    @Cacheable(
            value = "redis.v2.test",
            unless = "#result == null")
    @Expired(300)
    public User getUser(Long id, String firstName, String lastName) {
        return new User(id, firstName, lastName);
    }

    /**
     * SpEL 动态配置过期时间
     */
    @Cacheable(
            value = "redis.v2.test.ttl",
            unless = "#result == null")
    @Expired(el = "#ttl")
    public User getUser(Long id, String firstName, String lastName, long ttl) {
        return new User(id, firstName, lastName);
    }

}
