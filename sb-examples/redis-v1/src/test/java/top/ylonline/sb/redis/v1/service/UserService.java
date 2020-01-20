package top.ylonline.sb.redis.v1.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ylonline.common.cache.annotation.Expired;
import top.ylonline.sb.redis.v1.domain.User;

/**
 * @author YL
 */
@Service
public class UserService {

    /**
     * 指定具体过期时间
     */
    @Cacheable(
            value = "redis.v1.test",
            unless = "#result == null")
    @Expired(300)
    public User getUser(Long id, String firstName, String lastName) {
        return new User(id, firstName, lastName);
    }

    /**
     * SpEL 动态配置过期时间
     */
    @Cacheable(
            value = "redis.v1.test.ttl",
            unless = "#result == null")
    @Expired(el = "#ttl")
    public User getUser(Long id, String firstName, String lastName, long ttl) {
        return new User(id, firstName, lastName);
    }
}
