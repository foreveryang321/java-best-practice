# sentinel-spring-cloud-example

- 正常返回

  - 请求 [http://localhost:8080/sentinel/demo](http://localhost:8080/sentinel/demo) 可以看到正常返回`Hello world!`。

  - 同时可以在[Sentinel](https://github.com/alibaba/Sentinel) Dashboard 看到 
`sentinel-spring-cloud-example` 项


- 添加流控规则

  - 在`簇点链路`菜单选择对 `/demo` 添加流控规则，阈值类型=QPS，单机阈值=0
  
  - 再次请求 [http://localhost:8080/sentinel/demo](http://localhost:8080/sentinel/demo) 可以看到返回`Blocked by Sentinel (flow 
limiting)`，表明配置的流控规则已经生效了。