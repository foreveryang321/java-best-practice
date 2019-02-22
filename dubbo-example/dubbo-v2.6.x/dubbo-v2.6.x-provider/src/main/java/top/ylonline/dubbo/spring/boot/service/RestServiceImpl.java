package top.ylonline.dubbo.spring.boot.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import top.ylonline.dubbo.spring.boot.api.RestService;

import javax.ws.rs.QueryParam;

/**
 * @author YL
 */
@Service(protocol = {"rest"})
// @Path("/single")
public class RestServiceImpl implements RestService {

    // @GET
    // @Path("/echo")
    @Override
    public String echo(@QueryParam("message") String message) {
        return "{\"message\": \"" + message + "\", \"protocol\": \"rest\", \"url\": \""
                + RpcContext.getContext().getUrl() + "\"}";
    }
}
