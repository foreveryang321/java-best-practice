# 前后端分离数据加密传输

## 原理

> 后端传输到前端

后端使用 RSA 私钥加密数据；前端使用 RSA 公钥解密数据。

> 前端传输到后端

前端使用 AES 加密数据，并使用 RSA 公钥加密 AES 的密钥；

后端使用 RSA 私钥解密前端传过来的 AES 密钥密文，再使用 RSA 解密后的 AES 密钥解密数据。

## AES加解密

[http://localhost:8087/demo/aes](http://localhost:8087/demo/aes)，打开浏览器控制天查看日志输出


## RSA 加解密 
[http://localhost:8087/demo/aes](http://localhost:8087/demo/aes)，打开浏览器控制天查看日志输出