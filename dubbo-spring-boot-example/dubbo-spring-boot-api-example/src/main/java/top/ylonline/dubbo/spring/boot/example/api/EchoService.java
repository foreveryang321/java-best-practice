package top.ylonline.dubbo.spring.boot.example.api;

/**
 * @author YL
 */
public interface EchoService {
    /**
     * echo
     *
     * @param message msg
     *
     * @return 信息
     */
    String echo(String message);
}
