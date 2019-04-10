# dubbo-v2.7.x
> 环境：  
> dubbo 2.7.1  
> duboo-spring-boot-starter 2.7.1



## 启动 provider

访问以下地址来判断`provider`端使用多协议是否正常

[http://localhost:9090/single/echo?message=rest-protocol](http://localhost:9090/single/echo?message=rest-protocol)

[http://localhost:9090//multiple/echo?message=dubbo-or-rest-protocol](http://localhost:9090/multiple/echo?message=dubbo-or-rest-protocol)



<!-- more -->



## 启动 consumer

分别访问以下地址来判断`consumer`端使用多协议是否正常
- [http://localhost:8080/demo/dubbo?message=dubbo-protocol](http://localhost:8080/demo/dubbo?message=dubbo-protocol)
- [http://localhost:8080/demo/rest?message=rest-protocol](http://localhost:8080/demo/rest?message=rest-protocol)
- [http://localhost:8080/demo/multiple?message=dubbo-or-rest-protocol](http://localhost:8080/demo/multiple?message=dubbo-or-rest-protocol)



## 异常问题

### java.lang.NoClassDefFoundError: org/jboss/resteasy/client/jaxrs/engines/ApacheHttpClient4Engine

> 需要添加以下依赖包，该依赖 provider 端无需依赖，consumer 端需要依赖

```xml
<dependency>
    <groupId>org.jboss.resteasy</groupId>
    <artifactId>resteasy-client</artifactId>
    <version>${resteasy-client.version}</version>
</dependency>
```



### java.lang.RuntimeException: You must use at least one, but no more than one http method annotation on: top.ylonline
.dubbo.spring.boot.example.api.EchoService.echo..

> 出现这个异常，说明要在使用的接口标注：@Path、@POST、@Get等javax.ws.rs.*这个路径下相关注解，不应该在实现类标注



```java
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
```

### Duplicate application configs
当使用`org.apache.dubbo:dubbo-spring-boot-starter` 2.7.0，同时使用xml作为`dubbo`的配置时，会出现以下异常：
```text
Caused by: java.lang.IllegalStateException: Duplicate application configs: <dubbo:application name="dubbo-consumer-2.7.x" valid="true" id="dubbo-consumer-2.7.x" prefix="dubbo.application" /> and <dubbo:application valid="false" prefix="dubbo.application" />
```