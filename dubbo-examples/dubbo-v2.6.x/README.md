# dubbo-v2.6.x
> 环境：  
> dubbo 2.6.5  
> duboo-spring-boot-starter 0.2.0  
> dubbo-registry-nacos 0.0.2


- provider 多协议配置
- consumer 由于 dubbo-2.6.5 及以下版本的 Reference 还不支持 protocol 属性（dubbo-2.7.0支持了），注解暂时无法指定协议，但是可以使用xml配置指定协议
- zk、nacos 注册中心


## 启动 provider

访问以下地址来判断`provider`端使用多协议是否正常

[http://localhost:9090/single/echo?message=rest-protocol](http://localhost:9090/single/echo?message=rest-protocol)

[http://localhost:9090/multiple/echo?message=dubbo-or-rest-protocol](http://localhost:9090/multiple/echo?message=dubbo-or-rest-protocol)


<!-- more -->



## 启动 consumer

分别访问以下地址来判断`consumer`端使用多协议是否正常
- [http://localhost:8080/demo/dubbo?message=dubbo-protocol](http://localhost:8080/demo/dubbo?message=dubbo-protocol)
- [http://localhost:8080/demo/rest?message=rest-protocol](http://localhost:8080/demo/rest?message=rest-protocol)
- [http://localhost:8080/demo/multiple?message=dubbo-or-rest-protocol](http://localhost:8080/demo/multiple?message=dubbo-or-rest-protocol)


## AnnotationInjectedBeanPostProcessor 异常
> dubbo-2.6.5 + dubbo-spring-boot-starter-0.2.0

[issue](https://github.com/apache/incubator-dubbo/issues/2869)
```text
Caused by: java.lang.ClassNotFoundException: com.alibaba.spring.beans.factory.annotation.AnnotationInjectedBeanPostProcessor
```
解决，添加以下依赖
```xml
<dependency>
    <groupId>com.alibaba.spring</groupId>
    <artifactId>spring-context-support</artifactId>
    <version>1.0.2</version>
</dependency>
```



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



## dubbo-spring-boot-starter 中 spring-boot-starter scope 传递问题

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>0.2.0</version>
</dependency>
```
由于`dubbo-spring-boot-starter`中依赖了`spring-boot-starter`的`scope`是`true`，会导致其继承`spring-boot-starter-test`的`scope
`，使其变成`test`
```xml
<!-- Spring Boot dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <scope>true</scope>
</dependency>
```



## spring 版本不一致问题

- 项目中 pom.xml
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.6.RELEASE</version>
    <relativePath/>
</parent>

<properties>
    <dubbo.version>2.6.5</dubbo.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo-dependencies-bom</artifactId>
            <version>${dubbo.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```



- dubbo-dependencies-bom pom.xml
```xml
<properties>
    <!-- Common libs -->
    <spring_version>4.3.16.RELEASE</spring_version>
</properties>

<dependencyManagement>
    <dependencies>
        <!-- Common libs -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-framework-bom</artifactId>
            <version>${spring_version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

由于`spring-boot-starter-parent`中的依赖的`spring`版本是`5.0.10
.RELEASE`，`dubbo-dependencies-bom`中依赖了`spring-framework-bom`，版本是`4.3.16
.RELEASE`，会导致`spring`的版本不一致问题



> 解决方法：将`dubbo-dependencies-bom`作为`parent`，`spring-boot-starter-parent`放到`dependencyManagement`中，但是如果这样的话，有可能引发其他依赖问题


## 从 zk 切换到 nacos 注册中心
- rest 协议会出问题

正常获取到的服务地址应该是：rest://192.168.56.1:9090/top.ylonline.dubbo.spring.boot.example.api.EchoService?anyhost=true...，但是改成 
Nacos 注册中心后，获取到的服务地址是：rest://192.168.56.1:9090?anyhost=true...，从而导致`RestProtocol`空指针异常
```text
java.lang.NullPointerException: null
	at com.alibaba.dubbo.rpc.protocol.rest.RestProtocol.getContextPath(RestProtocol.java:235)
	at com.alibaba.dubbo.rpc.protocol.rest.RestProtocol.doRefer(RestProtocol.java:191)
	...
```
> 据说，`dubbo-registry-nacos`下个版本修复这个问题，当前版本：0.0.2。本人提了一个 [PR](https://github.com/dubbo/dubbo-registry-nacos/pull/14) 修复了这个问题