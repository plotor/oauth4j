### OAuth2.0 Implementation for Passport (beta version)

---

This project based on __SpringMVC__, __Spring__, and __MyBatis__

database: MySQL 5.7

jdk version: 1.8

In the future, we will take [Play!](https://www.playframework.com/) as the web framework!

---

### demo

#### Authorization Code Pattern

- request authorization code

```
http://localhost:8080/oauth/authorize/code?response_type=code&client_id=2882303761517520186&redirect_uri=http://www.zhenchao.com&scope=1%204&state=emhlbmNoYW8gcGFzc3BvcnQgb2F1dGg=
```

- request access token by code

```
http://localhost:8080/oauth/authorize/token?grant_type=authorization_code&client_id=2882303761517520186&redirect_uri=http://www.zhenchao.com&code={your code here}
```

#### Implicit Pattern

- request implicit access token

```
http://localhost:8080/oauth/implicit/token?response_type=token&client_id=2882303761517520186&redirect_uri=http://www.zhenchao.com&scope=1%204&state=emhlbmNoYW8gcGFzc3BvcnQgb2F1dGg=
```