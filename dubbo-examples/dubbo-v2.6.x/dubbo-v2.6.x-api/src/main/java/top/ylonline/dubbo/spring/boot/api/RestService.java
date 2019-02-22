package top.ylonline.dubbo.spring.boot.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * @author YL
 */
@Path("/single")
public interface RestService {
    /**
     * echo
     *
     * @param message msg
     *
     * @return 信息
     */
    @GET
    @Path("/echo")
    String echo(@QueryParam("message") String message);
}
