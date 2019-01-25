package top.ylonline.dubbo.spring.boot.example.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import top.ylonline.dubbo.spring.boot.example.api.EchoService;

// import javax.ws.rs.GET;
// import javax.ws.rs.Path;
// import javax.ws.rs.QueryParam;

/**
 * @author YL
 */
@Service(protocol = {"dubbo", "rest"})
// @Path("/demo")
public class EchoServiceImpl implements EchoService {

    // @GET
    // @Path("/echo")
    @Override
    public String echo(/*@QueryParam("message") */String message) {
        return "{\"message\": \"" + message + "\", \"url\": \"" + RpcContext.getContext().getUrl() + "\"}";
    }
}
