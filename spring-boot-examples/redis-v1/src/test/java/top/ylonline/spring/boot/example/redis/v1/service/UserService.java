package top.ylonline.spring.boot.example.redis.v1.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ylonline.spring.boot.example.redis.v1.cache.CacheExpire;
import top.ylonline.spring.boot.example.redis.v1.domain.User;

/**
 * @author YL
 */
@Service
public class UserService {

    @Cacheable(
            value = "redis.v1.test",
            unless = "#result == null")
    @CacheExpire(120)
    public User getUser(Long id, String firstName, String lastName) {
        return new User(id, firstName, lastName);
    }
}
