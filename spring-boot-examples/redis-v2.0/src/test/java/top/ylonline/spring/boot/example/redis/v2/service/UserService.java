package top.ylonline.spring.boot.example.redis.v2.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ylonline.spring.boot.example.redis.v2.domain.User;
import top.ylonline.spring.boot.example.redis.v2.cache.CacheExpire;

/**
 * @author YL
 */
@Service
public class UserService {

    @Cacheable(
            value = "redis.v2.test",
            unless = "#result == null")
    @CacheExpire(120)
    public User getUser(Long id, String firstName, String lastName) {
        return new User(id, firstName, lastName);
    }
}
