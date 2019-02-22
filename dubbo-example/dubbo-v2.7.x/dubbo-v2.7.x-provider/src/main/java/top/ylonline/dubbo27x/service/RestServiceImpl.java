package top.ylonline.dubbo27x.service;

import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import top.ylonline.dubbo27x.api.RestService;

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
