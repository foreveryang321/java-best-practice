# 自定义 Expired 注解

- 自定义 Expired 注解，实现 redis 缓存的过期时间配置
- 支持 Spring Expression Language (SpEL) ，实现动态配置过期时间。注意：使用 SpEL 配置过期时间时，配合`@Cacheput`、`CacheEvict`等使用时会有问题

## 环境
- spring-boot 1.x
- jedis
- redis server


## 使用方式

```java
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    @Expired(spEl = "#ttl")
    public User getUser(Long id, String firstName, String lastName, long ttl) {
        return new User(id, firstName, lastName);
    }
}
```