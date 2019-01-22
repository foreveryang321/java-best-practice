# dubbo example

## dubbo-2.6.5 提示 AnnotationInjectedBeanPostProcessor 异常
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

- dubbo-spring-boot-provider-example 多协议配置（测试通过）
- dubbo-spring-boot-consumer-example 由于 dubbo-2.6.5 及以下版本的 Reference 还不支持 protocol 属性，注解暂时无法指定协议，但是可以使用xml配置指定协议