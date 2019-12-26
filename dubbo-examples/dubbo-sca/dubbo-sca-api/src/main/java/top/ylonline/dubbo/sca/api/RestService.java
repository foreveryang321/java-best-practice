package top.ylonline.dubbo.sca.api;

/**
 * @author YL
 */
public interface RestService {
    /**
     * 通过 id 获取用户名
     *
     * @param id 用户id
     *
     * @return 用户名
     */
    String getName(long id);
}
