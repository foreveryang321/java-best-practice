# dubbo-v2.7.x
> 环境：  
> dubbo 2.7.0  
> duboo-spring-boot-starter 2.7.0



## 启动 provider

访问以下地址来判断`provider`端使用多协议是否正常

[http://localhost:9090/top.ylonline.dubbo27x.api.RestService/single/echo?message=rest-protocol](http://localhost:9090/top.ylonline.dubbo27x.api.RestService/single/echo?message=rest-protocol)

[http://localhost:9090/top.ylonline.dubbo27x.api.MultipleService/multiple/echo?message=dubbo-or-rest-protocol](http://localhost:9090/top.ylonline.dubbo27x.api.MultipleService/multiple/echo?message=dubbo-or-rest-protocol)

> 使用接口的全限类名作为`contextpath`是dubbo-2.7.0的bug，请看[issue](https://github.com/apache/incubator-dubbo/issues/3445)，此[PR](https://github.com/apache/incubator-dubbo/pull/3479)修复了这个问题


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

> main 类
```java
@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                WebSocketServletAutoConfiguration.class,
                JmxAutoConfiguration.class
        }
)
@ImportResource(locations = {"classpath:dubbo.xml"})
@Slf4j
public class DubboConsumer270 {

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumer270.class, args);
    }
}
```

> dubbo.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
       http://dubbo.apache.org/schema/dubbo
       http://dubbo.apache.org/schema/dubbo/dubbo.xsd">

    <dubbo:application name="dubbo-consumer-2.7.x">
        <dubbo:parameter key="qos.enable" value="false"/>
    </dubbo:application>

    <dubbo:registry address="zookeeper://192.168.56.101:2181" client="curator"/>

    <dubbo:consumer check="false" client="netty"/>

    <dubbo:reference id="dubboService"
                     interface="top.ylonline.dubbo27x.api.DubboService"/>
</beans>
```

> pom.xml
```xml
<properties>
    <dubbo.version>2.7.0</dubbo.version>
    <spring-boot.version>2.0.6.RELEASE</spring-boot.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>${spring-boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
            <version>${dubbo.version}</version>
            <exclusions>
                <exclusion>
                    <groupId>com.google.code.gson</groupId>
                    <artifactId>gson</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>io.netty</groupId>
                    <artifactId>netty-all</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>${dubbo.version}</version>
        </dependency>
        <!-- Dubbo zookeeper registry dependency -->
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.9</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-api</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>log4j</groupId>
                    <artifactId>log4j</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>io.netty</groupId>
                    <artifactId>netty</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>2.12.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>2.12.0</version>
        </dependency>
        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-all</artifactId>
            <version>4.1.29.Final</version>
        </dependency>
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>21.0</version>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.5</version>
        </dependency>
    </dependencies>
</dependencyManagement>


<!-- dubbo -->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
</dependency>

<!-- zk registry dependency -->
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
</dependency>
```

解决方法：
- 去掉`dubbo-spring-boot-starter`依赖，使用`xml`方式配置`dubbo`
- 不使用`xml`方式配置`dubbo`，单单使用`dubbo-spring-boot-starter`方式

### No such extension org.apache.dubbo.metadata.store.MetadataReportFactory by name redis

问题描述请看：[issue](https://github.com/apache/incubator-dubbo/issues/3514)

本人提交了一个[PR](https://github.com/apache/incubator-dubbo/pull/3515)修复了此问题

同时，也可以添加以下依赖来解决这个问题
```xml
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-metadata-report-redis</artifactId>
    <version>2.7.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-metadata-report-api</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```