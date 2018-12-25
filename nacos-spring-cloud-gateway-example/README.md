# nacos-spring-cloud-gateway-example

实现通过[Nacos](https://nacos.io/zh-cn/)动态配置 gateway 的路由规则。

`spring-cloud-starter-alibaba-nacos-config` 的原理是将配置信息注入到`Spring`的`environment`中，并在配置更新时自动触发`context refresh`事件，从而将`environment`环境中的配置变更为最新配置

- 在 Nacos 控制台新建 gateway.yaml 配置

Data ID: gateway.yaml

Group: DEFAULT_GROUP
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: baidu
          uri: https://www.baidu.com
          predicates:
            - Path=/s
        - id: taobao
          uri: https://www.taobao.com/
          predicates:
            - Path=/markets/3c/tbdc
```
- 配置 bootstrap.properties
```properties
spring.cloud.nacos.config.ext-config[0].data-id=gateway.yaml
spring.cloud.nacos.config.ext-config[0].refresh=true
```
> refresh要配置true，否则不能动态更新

- 执行 NacosGatewayApp 启动网关项目

- 浏览器访问

分别访问[http://localhost:8080/s](http://localhost:8080/s)和[http://localhost:8080/markets/3c/tbdc](http://localhost:8080/markets/3c/tbdc)
，能正常转发到百度、淘宝网站

- 修改 gateway.yaml 配置
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: baidu
          uri: https://www.baidu.com
          predicates:
            - Path=/s
```
控制台打印一下日志，说明配置修改已经被监听到
```text
org.springframework.cloud.endpoint.event.RefreshEventListener.handle(37) | Refresh keys changed: [spring.cloud
.gateway.routes.xxx]
```


再次访问[http://localhost:8080/markets/3c/tbdc](http://localhost:8080/markets/3c/tbdc)，会返回404页面
> 说明修改的配置已经动态更新了

- 动态更新的原理

> `spring-cloud-gateway` 的 RouteRefreshListener 已经监听了 ApplicationEvent 事件，所以当 Nacos 中的路由规则变更后，会监听到`context refresh`事件，从而调用 reset 方法 publish 路由更新的事件

**org.springframework.cloud.gateway.route.RouteRefreshListener.java**
```java
@Override
public void onApplicationEvent(ApplicationEvent event) {
    if (event instanceof ContextRefreshedEvent
            || event instanceof RefreshScopeRefreshedEvent
            || event instanceof InstanceRegisteredEvent) {
        reset();
    }
    else if (event instanceof ParentHeartbeatEvent) {
        ParentHeartbeatEvent e = (ParentHeartbeatEvent) event;
        resetIfNeeded(e.getValue());
    }
    else if (event instanceof HeartbeatEvent) {
        HeartbeatEvent e = (HeartbeatEvent) event;
        resetIfNeeded(e.getValue());
    }
}

private void reset() {
    this.publisher.publishEvent(new RefreshRoutesEvent(this));
}
```
