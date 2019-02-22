package top.ylonline.dubbo.spring.boot.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import top.ylonline.dubbo.spring.boot.api.MultipleService;

import javax.ws.rs.QueryParam;

/**
 * @author YL
 */
@Service(protocol = {"dubbo", "rest"})
// @Path("/multiple")
public class MultipleServiceImpl implements MultipleService {

    // @GET
    // @Path("/echo")
    @Override
    public String echo(@QueryParam("message") String message) {
        return "{\"message\": \""
                + message + "\", \"protocol\": \"dubbo + rest\", \"url\": \""
                + RpcContext.getContext().getUrl() + "\"}";
    }
}
