# nacos-spring-boot-example
> 目前 Nacos 已经更新到 0.7.0 版本

> 项目使用了 spring profile 多环境配置（dev、local、pro），所以需要在`nacos`控制台配置新建3个命名空间（假设新建的3个命名空间也分别是dev、local、pro）分别对应 spring profile 多环境

- 配置 spring profile
```yaml
spring:
  profiles:
    active: local
```

- 在 [Nacos](https://nacos.io/zh-cn/) 控制台 local 命名空间下新建 `nacos-spring-boot-example.properties` 配置
```properties
example.id=123
example.name=nacos-spring-boot-example.properties
```

- 请求 [http://localhost:8080/nacos/demo](http://localhost:8080/nacos/demo)
```bash
$ curl http://localhost:8080/nacos/demo
id: 123, name: nacos-spring-boot-example.properties
```

- 修改配置，验证是否可以实时更新
  - 修改 `example.id`、`example.name` 的值
  ```properties
  example.id=123456
  example.name=nacos-spring-boot-example.properties.new
  ```
  - 请求 [http://localhost:8080/nacos/demo](http://localhost:8080/nacos/demo)
  ```bash
  $ curl http://localhost:8080/nacos/demo
  id: 123456, name: nacos-spring-boot-example.properties
  ```

  `@NacosValue`配置 autoRefreshed = true 是可以实时更新配置的，而`Spring`的`@Value`不能实时更新