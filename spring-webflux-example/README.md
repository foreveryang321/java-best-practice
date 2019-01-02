# spring-webflux-example

spring-webflux 响应式编程。本项目提供一个堵塞的例子（[http://localhost:8080/user/block](http://localhost:8080/user/block)
）、一个响应式的（[http://localhost:8080/user/rx](http://localhost:8080/user/rx)）例子

> 由于这里使用的是`spring-data-jpa`，而它目前是还不支持响应式api，所有需要做一下适配

## 堵塞
- 请求

使用浏览器或者postman请求：[http://localhost:8080/user/block](http://localhost:8080/user/block)

抓包可以看到以下相应头部（Response Headers）内容
```text
Content-Length: 253
Content-Type: application/json;charset=UTF-8;q=0.8
```

- 响应内容
```json
[
    {"id":1,"name":"test_1"},
    {"id":2,"name":"test_2"},
    {"id":3,"name":"test_3"},
    {"id":4,"name":"test_4"},
    {"id":5,"name":"test_5"},
    {"id":6,"name":"test_6"},
    {"id":7,"name":"test_7"},
    {"id":8,"name":"test_8"},
    {"id":9,"name":"test_9"},
    {"id":10,"name":"test_10"}
]
```

## 响应式
```java
@GetMapping(value = "/rx", produces = "application/stream+json")
@ResponseBody
public Flux<User> rx() {
    return this.userService.findAll();
}
```
> application/stream+json 可以让调用方识别出，这些数据是分批响应式传递，而不用等待传输完才开始处理


- 请求

使用浏览器或者postman请求：[http://localhost:8080/user/rx](http://localhost:8080/user/rx)

抓包可以看到以下相应头部（Response Headers）内容
```text
Content-Type: application/stream+json;q=0.8;charset=UTF-8
transfer-encoding: chunked
```

- 响应内容
```json
{"id":1,"name":"test_1"}
{"id":2,"name":"test_2"}
{"id":3,"name":"test_3"}
{"id":4,"name":"test_4"}
{"id":5,"name":"test_5"}
{"id":6,"name":"test_6"}
{"id":7,"name":"test_7"}
{"id":8,"name":"test_8"}
{"id":9,"name":"test_9"}
{"id":10,"name":"test_10"}
```
> 可以发现，响应式返回的不是标准的json格式数据，而是单行返回每个实体
