<%@ page language="java" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>登录</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link href="css/zhenchao.css" rel="stylesheet">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script type="application/javascript" src="js/jquery-3.1.0.min.js"></script>
    <script type="application/javascript" src="js/bootstrap.min.js"></script>
    <script type="application/javascript" src="js/fb.js"></script>
</head>
<body>
<div class="panel panel-default" style="width: 50%; margin: auto; margin-top: 100px;">
    <div class="panel-heading">
        <h3 class="panel-title">
            用户登录
        </h3>
    </div>
    <div class="panel-body">
        <form role="form" action="/login" method="post">
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
