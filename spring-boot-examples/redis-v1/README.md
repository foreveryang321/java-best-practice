# 自定义 CacheExpire 注解

自定义 CacheExpire 注解，实现 redis 缓存的过期时间配置

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

    @Cacheable(
            value = "redis.v1.test",
            unless = "#result == null")
    @CacheExpire(120)
    public User getUser(Long id, String firstName, String lastName) {
        return new User(id, firstName, lastName);
    }
}
```