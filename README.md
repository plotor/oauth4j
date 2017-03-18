### OAuth2.0协议实现

---

本项目为[RFC6749](https://tools.ietf.org/html/rfc6749)协议的标准实现，主要用于对该协议的学习和理解，也可以在本项目基础上结合自己的业务场景做相应的改造，用于生产环境授权。

项目采用java语言开发，采用 __Spring MVC__, __Spring__, 以及 __MyBatis__ 框架，开发和运行环境如下：

> - 数据库: MySQL 5.7
> - jdk版本: 1.8

本项目主要实现了[RFC6749](https://tools.ietf.org/html/rfc6749)定义的授权码授权模式和隐式授权模式，并参考文档“[HTTP Authentication: MAC Authentication](https://tools.ietf.org/html/draft-hammer-oauth-v2-mac-token-02)”实现了MAC类型的令牌。由于token的验证，以及OpenID的生成已经超出了OAuth2.0协议的范畴，本项目未作相应实现，token属于对称加密字符串，所以内部元素可以自行定义和验证，你可以采用AOP或注解的方式来实现token验证的逻辑，而OpenID只需要保证在（user_id, client_id）维度唯一即可。

项目相关博文：

1. [OAuth2.0协议原理与实现：协议原理](http://www.zhenchao.org/2017/03/04/oauth-v2-principle/)
2. [OAuth2.0协议原理与实现：TOKEN生成算法](http://www.zhenchao.org/2017/03/11/oauth-v2-token/)
3. OAuth2.0协议原理与实现：协议实现

---

### 运行与测试

项目相关表结构定义以及测试数据均放在`sqls`目录下。

__注意：__ 实际应用中请强制开启HTTPS!

#### 授权码模式

- 请求获取授权码

```http
http://localhost:8080/oauth/authorize/code?response_type=code&client_id=2882303761517520186&redirect_uri=http://www.zhenchao.com&scope=1%204&state=emhlbmNoYW8gcGFzc3BvcnQgb2F1dGg=
```

响应示例：

```http
HTTP/1.1 302 Found
Location: http://www.zhenchao.com/?code=E670AC74F54CACC6222ADFFBEE51CADB&state=emhlbmNoYW8gcGFzc3BvcnQgb2F1dGg%3D
```

- 请求下发访问令牌

```http
http://localhost:8080/oauth/authorize/token?grant_type=authorization_code&client_id=2882303761517520186&redirect_uri=http://www.zhenchao.com&code={your code here}
```

响应示例：

```json
{
    "access_token": "AAAAHGU4Y0Mrd1I2T3VhOHlRak9walY2T3hHSjM2VT0xLjAuMAAtAAAAAFlD/+YAAAFa4iNFYQAAAAAAAYagKAAAAAAGJToAAAAHMSAyIDQgNQAAAANtYWM=", 
    "expires_in": 1497628646, 
    "refresh_token": "refresh token", 
    "mac_key": "LJ2H7DxyKBGsJlGBryRsVzNKCzpF57owjeOf7CxMJUFegWO7YlLi24M0sDRfvooq", 
    "mac_algorithm": "hmac-sha-1"
}
```

#### 隐式授权模式

- 请求下发访问令牌

```http
http://localhost:8080/oauth/implicit/token?response_type=token&client_id=2882303761517520186&redirect_uri=http://www.zhenchao.com&scope=1%204&state=emhlbmNoYW8gcGFzc3BvcnQgb2F1dGg=
```

响应示例：

```http
HTTP/1.1 302 Found
Location: http://www.zhenchao.com#access_token=AAAAHFM5T1ZCZG5hL0RBeTlRekJCSjlWYUduUHpaMD0xLjAuMAAtAAAAAFlEADEAAAFa4iRncwAAAAAAAYagKAAAAAAGJToAAAAHMSAyIDQgNQAAAANtYWM=&token_type=mac&mac_key=fRgEdJsq6rR8TuH84mXkTZzAv0q6KyvQz7BVkkHYyln5FOVccPp4Cz4VuDcz9cfr&mac_algorithm=hmac-sha-1&expires_in=1497628721&scope=1+2+4+5
```

### 参考文献

1. [RFC5849 - The OAuth 1.0 Protocol](https://tools.ietf.org/html/rfc5849)
2. [RFC6749 - The OAuth 2.0 Authorization Framework](https://tools.ietf.org/html/rfc6749)
3. [RFC6750 - The OAuth 2.0 Authorization Framework: Bearer Token Usage](https://tools.ietf.org/html/rfc6750)
4. [ HTTP Authentication: MAC Authentication (draft-hammer-oauth-v2-mac-token-02)](https://tools.ietf.org/html/draft-hammer-oauth-v2-mac-token-02)

如有任何疑问，请发邮件到[zhenchao.wang@hotmail.com](mailto://zhenchao.wang@hotmail.com)咨询。