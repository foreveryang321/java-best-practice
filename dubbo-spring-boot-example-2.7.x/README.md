# dubbo example

- provider 多协议配置
- consumer 由于 dubbo-2.6.5 及以下版本的 Reference 还不支持 protocol 属性（dubbo-2.7.0支持了），注解暂时无法指定协议，但是可以使用xml配置指定协议
- zk、nacos 注册中心


## 启动 provider

访问以下地址来判断`provider`端使用多协议是否正常

[http://localhost:9090/top.ylonline.dubbo27x.api.RestService/single/echo?message=rest-protocol]
(http://localhost:9090/top.ylonline.dubbo27x.api.RestService/single/echo?message=rest-protocol)

[http://localhost:9090/top.ylonline.dubbo27x.api.MultipleService/multiple/echo?message=dubbo-or-rest-protocol]
(http://localhost:9090/top.ylonline.dubbo27x.api.MultipleService/multiple/echo?message=dubbo-or-rest-protocol)

> 使用接口的全限类名作为contextpath是dubbo2.7.0的bug，请看[issue](https://github.com/apache/incubator-dubbo/issues/3445)


<!-- more -->



## 启动 consumer

分别访问以下地址来判断`consumer`端使用多协议是否正常
- [http://localhost:8080/demo/dubbo?message=dubbo-protocol](http://localhost:8080/demo/dubbo?message=dubbo-protocol)
- [http://localhost:8080/demo/rest?message=rest-protocol](http://localhost:8080/demo/rest?message=rest-protocol)
- [http://localhost:8080/demo/multiple?message=dubbo-or-rest-protocol](http://localhost:8080/demo/multiple?message=dubbo-or-rest-protocol)



## consumer 端

- java.lang.NoClassDefFoundError: org/jboss/resteasy/client/jaxrs/engines/ApacheHttpClient4Engine

> 需要添加以下依赖包，该依赖 provider 端无需依赖，consumer 端需要依赖

```xml
<dependency>
    <groupId>org.jboss.resteasy</groupId>
    <artifactId>resteasy-client</artifactId>
    <version>${resteasy-client.version}</version>
</dependency>
```



- java.lang.RuntimeException: You must use at least one, but no more than one http method annotation on: top.ylonline.dubbo.spring.boot.example.api.EchoService.echo..

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