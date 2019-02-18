package top.ylonline.dubbo27x.service;

import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import top.ylonline.dubbo27x.api.DubboService;

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
