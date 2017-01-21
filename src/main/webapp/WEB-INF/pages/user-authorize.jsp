<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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
        <div><img src="${user.avatar}"/><span>${user.username}</span></div>
        <ul class="list-group">
            <c:forEach items="${scopes}" var="scope">
                <li class="list-group-item"><c:out value="${scope.name}"/></li>
            </c:forEach>
        </ul>
        <form role="form" action="/oauth/user/authorize" method="post">
            <input type="hidden" name="callback" value="${callback}"/>
            <input type="hidden" name="user_id" value="${user.id}"/>
            <input type="hidden" name="client_id" value="${app.clientId}"/>
            <button type="submit" class="btn btn-default">确认授权</button>
        </form>
    </div>
</div>
</body>
</html>
