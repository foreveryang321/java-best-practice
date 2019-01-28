package top.ylonline.dubbo.spring.boot.service;

import com.alibaba.dubbo.config.annotation.Service;
import top.ylonline.dubbo.spring.boot.api.UserService;

/**
 * @author YL
 */
@Service(protocol = {"dubbo"})
public class UserServiceImpl implements UserService {

    @Override
    public String getNameById(long id) {
        return "{\"id\": " + id + ", \"name\": your id" + id + "}";
    }
}
