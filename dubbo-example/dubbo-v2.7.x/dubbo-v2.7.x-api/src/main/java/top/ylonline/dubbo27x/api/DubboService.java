package top.ylonline.dubbo27x.api;

import javax.ws.rs.QueryParam;

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
    String echo(@QueryParam("message") String message);
}
