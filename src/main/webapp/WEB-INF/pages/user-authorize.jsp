<%@ page language="java" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>用户授权 - OAuth 2.0</title>
</head>
<body>
<div class="panel panel-default" style="width: 50%; margin: auto; margin-top: 100px;">
    <div class="panel-heading">
        <h3 class="panel-title">
            确认授权
        </h3>
    </div>
    <div class="panel-body">
        <div><a href="${app.logo}"/></div>
        <form role="form" action="#" method="post">
            <input type="hidden" name="callback" value="${callback}">
            <div class="form-group">
                <label for="username">用户名</label>
                <input type="text" class="form-control" name="username" placeholder="请输入用户名">
            </div>
            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" class="form-control" name="password" placeholder="请输入密码">
            </div>
            <button type="submit" class="btn btn-default">普通登录</button>
        </form>
    </div>
</div>
</body>
</html>
