# 前后端分离数据加密传输

## 原理

> 后端传输到前端

后端使用`AES`加密数据，并使用`RSA私钥`加密`AES密钥`；

前端使用`RSA公钥`解密`AES密钥`，再使用解密后的`AES密钥`解密数据。

> 前端传输到后端

前端使用`AES`加密数据，并使用`RSA公钥`加密`AES密钥`；

后端使用`RSA私钥`解密前端传过来的`AES密钥`，再使用`RSA`解密后的`AES 密钥`解密数据。

## AES加解密

[http://localhost:8087/demo/aes](http://localhost:8087/demo/aes)，打开浏览器控制台查看日志输出


## RSA 加解密 
[http://localhost:8087/demo/aes](http://localhost:8087/demo/aes)，打开浏览器控制台查看日志输出