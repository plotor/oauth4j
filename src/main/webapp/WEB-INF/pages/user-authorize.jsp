<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <base href="<%=basePath%>">
    <title>用户授权 - OAuth 2.0</title>
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
<div class="panel panel-default" style="width: 400px; margin: auto; margin-top: 100px;">
    <div class="panel-heading">
        <h3 class="panel-title">
            确认授权
        </h3>
    </div>
    <div class="panel-body">
        <div style="text-align: center;"><img src="${app.logo}"/></div>
        <hr/>
        <div><img style="width:50px; height:50px; border-radius:50px;" src="${user.avatar}"/>&emsp;
            <span>${user.username}</span>
            <button type="button" class="btn btn-default" style="float: right;">切换账号</button>
        </div>
        <br/>
        <div style="font-weight: bold; margin-bottom: 10px; padding-left: 12px;">登录后应用将获取您的以下信息：</div>
        <ul class="list-group">
            <c:forEach items="${scopes}" var="scope">
                <li class="list-group-item" style="list-style-type:square;"><c:out value="${scope.name}"/></li>
                <input type="hidden" name="scope-id" value="${scope.id}"/>
            </c:forEach>
        </ul>
        <form role="form" action="/oauth/user/authorize" method="post" style="text-align: center;">
            <input type="hidden" name="callback" value="${callback}"/>
            <input type="hidden" name="user_id" value="${user.id}"/>
            <input type="hidden" name="client_id" value="${app.appId}"/>
            <input type="hidden" id="scope" name="scope" value=""/>
            <input type="hidden" name="state" value="${state}"/>
            <button type="submit" class="btn btn-success" style="width: 50%;">确&emsp;认&emsp;授&emsp;权</button>
        </form>
    </div>
</div>
<script type="application/javascript">
    $(function () {
        var scopes = [];
        $(".list-group input").each(function (index, sid) {
            scopes[index] = sid.value;
        });
        $("#scope").val(scopes.join('-'));
    });
</script>
</body>
</html>
