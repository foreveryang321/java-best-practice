package top.ylonline.spring.boot.example.redis.v1;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.ylonline.spring.boot.example.redis.v1.domain.User;
import top.ylonline.spring.boot.example.redis.v1.service.UserService;

import javax.annotation.Resource;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisV1AppTest {
    @Resource
    private UserService userService;

    @Test
    public void getUser() {
        User user = userService.getUser(1L, "F", "L");
        System.out.println(user.toString());
        Assert.assertEquals(1, user.getId().longValue());
    }
}
