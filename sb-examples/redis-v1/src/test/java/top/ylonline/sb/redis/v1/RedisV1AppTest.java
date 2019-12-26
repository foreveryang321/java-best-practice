package top.ylonline.sb.redis.v1;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.ylonline.sb.redis.v1.domain.User;
import top.ylonline.sb.redis.v1.service.UserService;

import javax.annotation.Resource;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisV1App.class)
public class RedisV1AppTest {
    @Resource
    private UserService userService;

    @Test
    public void normal() {
        User user = userService.getUser(1L, "cache", "normal");
        System.out.println(user.toString());
        Assert.assertEquals(1, user.getId().longValue());
    }

    /**
     * 动态配置过期时间
     */
    @Test
    public void dynamicTTL() {
        User user = userService.getUser(2L, "cache", "dynamicTTL", 900L);
        System.out.println(user.toString());
        Assert.assertEquals(2, user.getId().longValue());
    }
}
