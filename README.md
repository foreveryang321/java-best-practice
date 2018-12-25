# java-learning-examples

本例子基于 spring-boot 2.0.6.RELEASE、spring-cloud Finchley.SR2

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.6.RELEASE</version>
    <relativePath/>
</parent>

<properties>
    <spring-cloud.version>Finchley.SR2</spring-cloud.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

# Examples
- [nacos-spring-boot-example](nacos-spring-boot-example/README.md) Nacos + spring-boot example
- [nacos-spring-cloud-example](nacos-spring-cloud-example/README.md) Nacos + spring-cloud example
- [nacos-spring-cloud-gateway-example](nacos-spring-cloud-gateway-example/README.md) Nacos + spring-cloud-gateway 实现动态路由规则配置
- [sentinel-spring-cloud-example](sentinel-spring-cloud-example/README.md) sentinel example
- [spring-cloud-gateway-example](spring-cloud-gateway-example/README.md) spring-cloud-gateway example