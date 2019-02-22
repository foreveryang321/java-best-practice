# gateway-simple

- 初始化配置
```yaml
spring:
  application:
    name: spring-cloud-gateway-example
  cloud:
    gateway:
      routes:
        - id: demo
          uri: https://www.baidu.com
          predicates:
            - Path=/s
```
启动后，浏览器访问 [http://localhost:8080/s](http://localhost:8080/s)，请求会转发到 [https://www.baidu.com/s](https://www.baidu.com/s)，说明路由成功了

- 开启 gateway 端点
> 如果不配置，默认只开启 `health`、`info` 端点，配置成 `*` 表示开启所有端点
```yaml
management:
  endpoint:
    gateway:
      enabled: true
  endpoints:
    web:
      exposure:
        include: '*'
```

- 动态调整路由规则
    - 刷新路由规则
    ```sh
    curl -XPOST http://localhost:8080/actuator/gateway/refresh
    ```
    
    - 新增路由
    ```sh
    curl -H 'Content-Type: application/json; charset=UTF-8' -XPOST http://localhost:8080/actuator/gateway/routes/taobao -d '{
            "id": "taobao",
            "predicates": [
            {
                "name": "Path",
                "args":
                {
                    "_genkey_0": "/search"
                }
            }],
            "filters": [],
            "uri": "https://s.taobao.com/",
            "order": 0
        }'
    ```
    > 要调用一下`刷新路由规则`接口才会生效
    
    > 注意：args 的 `_genkey_0` key 命名，filters 也是一样的命名方式，后缀数字按照参数配置顺序递增
    
    - 删除路由
    ```sh
    curl -XDELETE http://localhost:8080/actuator/gateway/routes/taobao
    ```
    > 要调用一下`刷新路由规则`接口才会生效
    
    