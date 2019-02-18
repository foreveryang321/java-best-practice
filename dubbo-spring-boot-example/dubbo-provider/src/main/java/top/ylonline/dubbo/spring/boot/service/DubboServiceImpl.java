package top.ylonline.dubbo.spring.boot.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import top.ylonline.dubbo.spring.boot.api.DubboService;

import javax.ws.rs.QueryParam;

/**
 * @author YL
 */
@Service(protocol = {"dubbo"})
public class DubboServiceImpl implements DubboService {

    @Override
    public String echo(@QueryParam("message") String message) {
        return "{\"message\": \"" + message + "\", \"protocol\": \"dubbo\", \"url\": \""
                + RpcContext.getContext().getUrl() + "\"}";
    }
}
