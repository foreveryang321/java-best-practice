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
- [nacos-spring-boot-example](nacos-spring-boot-example/README.md) Nacos + spring-boot 简单入门
- [nacos-spring-cloud-example](nacos-spring-cloud-example/README.md) Nacos + spring-cloud 简单入门
- [nacos-spring-cloud-gateway-example](nacos-spring-cloud-gateway-example/README.md) gateway 实现动态路由规则：Nacos 配置中心
- [sentinel-nacos-example](sentinel-nacos-example/README.md) Sentinel 配置动态规则数据源：Nacos 配置中心（zk、apollo同理）
- [sentinel-spring-cloud-example](sentinel-spring-cloud-example/README.md) sentinel 简单入门
- [spring-cloud-gateway-example](spring-cloud-gateway-example/README.md) gateway 简单入门
- [spring-webflux-example](spring-webflux-example/README.md) webflux 简单入门，提供堵塞、响应式对比