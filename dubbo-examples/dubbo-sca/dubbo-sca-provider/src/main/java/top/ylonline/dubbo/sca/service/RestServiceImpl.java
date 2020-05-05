// package top.ylonline.dubbo.sca.service;
//
// import org.apache.dubbo.config.annotation.Service;
// import top.ylonline.dubbo.sca.api.RestService;
//
// import javax.ws.rs.GET;
// import javax.ws.rs.Path;
// import javax.ws.rs.QueryParam;
//
// /**
//  * @author YL
//  */
// @Service(protocol = "rest")
// @Path("/rest")
// public class RestServiceImpl implements RestService {
//
//     @GET
//     @Path("/get")
//     @Override
//     public String getName(@QueryParam("id") long id) {
//         return "user id: " + id;
//     }
// }
