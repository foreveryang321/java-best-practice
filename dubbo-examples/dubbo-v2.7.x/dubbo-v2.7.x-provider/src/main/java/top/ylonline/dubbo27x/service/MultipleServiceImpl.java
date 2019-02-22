package top.ylonline.dubbo27x.service;

import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import top.ylonline.dubbo27x.api.MultipleService;

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
