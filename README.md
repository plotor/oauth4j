## OAuth 2.0 授权协议实现

本项目为 [RFC6749](https://tools.ietf.org/html/rfc6749) 协议的标准实现，主要用于对该协议的学习和理解，也可以在此基础上结合自己的业务场景做相应的改造，用于生产环境授权。

项目采用 java 语言开发，基于 SSM 框架，开发和运行环境如下：

> - 数据库: MySQL 5.7
> - jdk: 1.8
> - maven：3.2.5

本项目主要实现了协议 [RFC6749](https://tools.ietf.org/html/rfc6749) 定义的 __授权码授权模式__ 和 __隐式授权模式__，并参考协议文档 “[HTTP Authentication: MAC Authentication](https://tools.ietf.org/html/draft-hammer-oauth-v2-mac-token-02)” 实现了 MAC 类型的令牌。

__相关博文：__

- [OAuth 2.0 开放授权那些事儿](http://www.zhenchao.org/2018/03/04/protocol/oauth-v2-protocol/)

---

### 一. 接口说明

项目相关表结构定义以及测试数据均放在 `sqls` 目录下。

__注意：__ 实际应用中请强制开启 HTTPS !

#### 1.1 授权码模式

##### 1. 请求获取授权码

路径： `/oauth/code`

方法： GET

参数：

参数名称 | 必须 | 类型 | 备注
--- | --- | --- | ---
client_id | 是 | long | 申请应用时分配的应用 ID
redirect_uri | 是 | string | 回调地址
response_type | 是 | string | 授权类型， 这里 `response_type=code`
scope | 否 | string | 开放数据接口权限 ID，可以传递多个，用空格分隔，如果不设置则适用该 APP 允许的所有权限代替。
state | 否 | string | 随机字符串，授权请求成功后原样返回，该参数用于防止 CSRF 攻击
skip_confirm | 否 | boolean | 已登录用户会看到切换帐号的页面, 如果应用不需要切换帐号, 可以加上参数 `skip_confirm=true`
force_login | 否 | boolean | 是否强制要登录，默认为 false

请求示例：

```http
http://localhost:8080/oauth/code?response_type=code&client_id=2882303761517520186
&redirect_uri=http://www.zhenchao.com&scope=1%204&state=emhlbmNoYW8gcGFzc3BvcnQgb2F1dGg=
```

响应：

- 成功

如果授权成功，授权服务器会重定向请求到 redirect_uri 所指定的地址，并带上相关参数：

```http
HTTP/1.1 302 Found
Location: http://www.zhenchao.com/?code=E670AC74F54CACC6222ADFFBEE51CADB&state=emhlbmNoYW8gcGFzc3BvcnQgb2F1dGg%3D
```

返回值说明：

参数名称 | 必须 | 类型 | 备注
--- | --- | --- | ---
code | 是 | string | 用来换取 access_token 的授权码，有效期为 10 分钟且只能使用一次
state | 否 | string | 如果请求时传递该参数，会原封不动回传

- 失败

如果授权失败，授权服务器会重定向请求到 redirect_uri 所指定的地址，并带上相关参数：

```http
HTTP/1.1 302 Found
Location: http://www.zhenchao.com/?error=ERROR&error_description=ERROR_DESCRIPTION&state=emhlbmNoYW8gcGFzc3BvcnQgb2F1dGg%3D
```

返回值说明：

参数名称 | 必须 | 类型 | 备注
--- | --- | --- | ---
error | 是 | int | 错误码]
error_description | 是 | string | 错误描述信息
state | 否 | string | 如果请求时传递该参数，会原封不动回传

##### 2. 请求下发访问令牌

路径： `/oauth/token`

方法： GET

参数：

参数名称 | 必须 | 类型 | 备注
--- | --- | --- | ---
client_id | 是 | long | 申请应用时分配的应用 ID
redirect_uri | 是 | string | 回调地址
grant_type | 是 | string | 授权码模式下 `grant_type=authorization_code`
client_secret | 是 | string | 申请应用时分配的 secret，用于保证请求的真实性
code | 是 | string | 接口 1 中下发的授权码
token_type | 否 | string | 令牌类型，可以是 bearer 或 mac，默认为 mac
issue_refresh_token | 否 | boolean | 是否下发刷新令牌，默认为 true

请求示例：

```http
http://localhost:8080/oauth/token?grant_type=authorization_code&client_id=2882303761517520186
&redirect_uri=http://www.zhenchao.com&code={your code here}
```

响应：

- 成功

如果请求成功，授权服务器会返回 JSON 格式的字符串：

参数名称 | 必须 | 类型 | 备注
--- | --- | --- | ---
access_token | 是 | string | 访问令牌
expires_in | 是 | long | 访问过期时间（单位：秒），默认生命周期为 2 小时
refresh_token | 是 | string | 刷新令牌，默认生命周期为 3 个月
scope | 是 | string | 访问令牌实际权限范围
mac_key | 是 | string | MAC类型访问令牌对应的加密密钥
mac_algorithm | 是 | string | MAC类型访问令牌对应的加密算法，目前只支持 HMacSha1
open_id | 是 | string | 用户在 APP 内的唯一标识 （暂未实现）

返回值示例：

```json
{
 "access_token": "access token value",
 "expires_in": 1497628646,
 "refresh_token": "refresh token value",
 "scope": "scope value",
 "token_type ": "mac",
 "mac_key ": "mac key value",
 "mac_algorithm": "hmac-sha-1",
 "openId":"32位字符串"
}
```

- 失败

如果请求失败，授权服务器会返回 JSON 格式的字符串：

参数名称 | 必须 | 类型 | 备注
--- | --- | --- | ---
error | 是 | int | 错误码
error_description | 是 | string | 错误描述信息

返回值示例：

```json
{
 "error": "error_code",
 "error_description": "错误描述"
}
```

#### 1.2 隐式授权模式

##### 1. 请求下发访问令牌

路径： `/oauth/implicit/token`

方法： GET

参数：

参数名称 | 必须 | 类型 | 备注
--- | --- | --- | ---
client_id | 是 | long | 申请应用时分配的应用 ID
redirect_uri | 是 | string | 回调地址
response_type | 是 | string | 授权类型, 这里 `response_type=token`
scope | 否 | string | 开放数据接口权限 ID，可以传递多个，用空格分隔，如果不设置则适用该 APP 允许的所有权限代替
state | 否 | string | 随机字符串，授权请求成功后原样返回，该参数用于防止 CSRF 攻击，强烈建议传递该参数
token_type | 否 | string | 令牌类型，可以是 bearer 或 mac，默认为 mac
skip_confirm | 否 | boolean | 已登录用户会看到切换帐号的页面, 如果应用不需要切换帐号, 可以加上参数 `skip_confirm=true`
force_login | 否 | boolean | 是否强制要登录，默认为 false

请求示例：

```http
http://localhost:8080/oauth/implicit/token?response_type=token&client_id=2882303761517520186
&redirect_uri=http://www.zhenchao.com&scope=1%204&state=emhlbmNoYW8gcGFzc3BvcnQgb2F1dGg=
```

响应：

- 成功

如果授权成功，授权服务器会重定向请求到 redirect_uri 所指定的地址，并在 url 的 fragment 位置带上相关参数：

```http
HTTP/1.1 302 Found
Location: http://www.zhenchao.com#access_token=AAAAHFM5T1ZCZG5hL0RBeTlRekJCSjlWYUduUHpaMD0xLjAuMAAtAAAAAFlEADEAAAFa4iRncwAAAAAAAYagKAAAAAAGJToAAAAHMSAyIDQgNQAAAANtYWM=
&token_type=mac&mac_key=fRgEdJsq6rR8TuH84mXkTZzAv0q6KyvQz7BVkkHYyln5FOVccPp4Cz4VuDcz9cfr&mac_algorithm=hmac-sha-1&expires_in=1497628721&scope=1+2+4+5
```

返回参数说明:

参数名称 | 必须 | 类型 | 备注
--- | --- | --- | ---
access_token | 是 | string | 访问令牌
expires_in | 是 | string | 访问令牌过期时间（单位：秒），默认生命周期为 2 小时
scope | 是 | string | 访问令牌实际权限范围
mac_key | 是 | string | MAC类型访问令牌对应的加密密钥
mac_algorithm | 是 | string | MAC类型访问令牌对应的加密算法，目前只支持HMacSha1
state | 否 | string | 如果请求时传递该参数，会原封不动回传

注意：隐式授权模式不下发 refresh\_token

- 失败

如果授权失败，授权服务器会重定向请求到 redirect_uri 所指定的地址，并在 url 的 fragment 位置带上相关参数：

```http
HTTP/1.1 302 Found
Location: http://www.zhenchao.com?error=ERROR&error_description=ERROR_DESCRIPTION&state=STATE
```

返回值说明：

参数名称 | 必须 | 类型 | 备注
--- | --- | --- | ---
error | 是 | int | 错误码
error_description | 是 | string | 错误描述信息
state | 否 | string | 如果请求时传递该参数，会原封不动回传

### TODO

- 刷新令牌下发逻辑
- 访问令牌刷新接口
- 令牌解析与验证 API

### 参考文献

1. [RFC5849 - The OAuth 1.0 Protocol](https://tools.ietf.org/html/rfc5849)
2. [RFC6749 - The OAuth 2.0 Authorization Framework](https://tools.ietf.org/html/rfc6749)
3. [RFC6750 - The OAuth 2.0 Authorization Framework: Bearer Token Usage](https://tools.ietf.org/html/rfc6750)
4. [HTTP Authentication: MAC Authentication (draft-hammer-oauth-v2-mac-token-02)](https://tools.ietf.org/html/draft-hammer-oauth-v2-mac-token-02)

如有任何疑问，请发邮件到 [zhenchao.wang@hotmail.com](mailto://zhenchao.wang@hotmail.com) 咨询。