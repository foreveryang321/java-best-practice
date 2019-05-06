# spring 事件发布和监听
> 使用事件发布和监听，最大的作用应该是为了***业务解耦***。类似的有：消息队列的发布订阅模式

[spring-boot 官方文档](https://docs.spring.io/spring-boot/docs/2.0.8.RELEASE/reference/htmlsingle/#boot-features-application-events-and-listeners)

比如：用户注册成功后，发送注册成功邮件、短信通知用户。

## 同步方式
如果使用同步的方式发送注册成功事件，会导致线程堵塞，增加用户注册的等待时间。

## 异步方式
使用异步的方式发送邮件、短信通知，可以减少用户等待注册成功的时间。

- @EnableAsync
- 连接池配置（实现AsyncConfigurer，或者自定义Bean）
- @Async、@EventListener
