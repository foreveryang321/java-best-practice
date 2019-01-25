package top.ylonline.dubbo.spring.boot.example.api;

/**
 * @author YL
 */
public interface UserService {
    /**
     * 获取用户名
     *
     * @param id 用户 ID
     *
     * @return 用户名
     */
    String getNameById(long id);
}
