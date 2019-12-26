package top.ylonline.dubbo.sca.api;

/**
 * @author YL
 */
public interface DubboService {
    /**
     * echo
     *
     * @param message msg
     *
     * @return 信息
     */
    String echo(String message);
}
