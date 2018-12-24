# nacos-spring-cloud-example
> 目前 Nacos 已经更新到 0.7.0 版本

> 官网对 bootstrap 和 application 两种配置文件的区别：Spring Cloud 构建于 Spring Boot 之上，在 Spring Boot 中有两种上下文，一种是 bootstrap, 另外一种是 application, bootstrap 是应用程序的父上下文，也就是说 bootstrap 加载优先于 application。bootstrap 主要用于从额外的资源来加载配置信息，还可以在本地外部配置文件中解密属性。这两个上下文共用一个环境，它是任何 Spring 应用程序的外部属性的来源。bootstrap 里面的属性会优先加载，它们默认也不能被本地相同配置覆盖

- bootstrap.properties
```properties
spring.application.name=nacos-spring-cloud-example
spring.cloud.nacos.config.server-addr=192.168.56.101:8848
```
> 配置`spring.application.name=nacos-spring-cloud-example`会自动拉取 Nacos 配置中心 data_id：nacos-spring-cloud-example.properties 的配置

- 在 [Nacos](https://nacos.io/zh-cn/) 控制台 public 命名空间下新建 `nacos-spring-cloud-example.properties` 配置

```properties
example.id=123
example.name=nacos-spring-cloud-example.properties
```

- DemoController.java
```java
@RestController
@RefreshScope
@Slf4j
public class DemoController {
    @NacosValue(value = "${example.id}", autoRefreshed = true)
    private int id;
    @Value("${example.name}")
    private String name;

    @GetMapping("/demo")
    public String demo() {
        return String.format("id: %s, name: %s", id, name);
    }
}
```
> 这里要添加`@RefreshScope`注解，否则不能动态更新 id、name 的值。这里的`@NacosValue`不起作用，获取到 id 值为0

- 请求 [http://localhost:8080/nacos/demo](http://localhost:8080/nacos/demo)
```bash
$ curl http://localhost:8080/nacos/demo
id: 0, name: nacos-spring-cloud-example.properties
```

- 修改配置，验证是否可以实时更新
  - 修改 `example.id`、`example.name` 的值
  ```properties
  example.id=123456
  example.name=nacos-spring-cloud-example.properties.新
  ```
  - 请求 [http://localhost:8080/nacos/demo](http://localhost:8080/nacos/demo)
  ```bash
  $ curl http://localhost:8080/nacos/demo
  id: 0, name: nacos-spring-cloud-example.properties.新
  ```
