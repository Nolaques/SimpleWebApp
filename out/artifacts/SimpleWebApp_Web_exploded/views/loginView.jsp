<%--
  Created by IntelliJ IDEA.
  User: belyak
  Date: 27.03.2018
  Time: 18:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>

<jsp:include page="_header.jsp"></jsp:include>
<jsp:include page="_menu.jsp"></jsp:include>

    <h3>Login Page</h3>
    <p style="color: red;">${errorString}</p>

<form method="post" action="${pageContext.request.contextPath}/login">
    <table border="0">
    <tr>
        <td>User Name</td>
        <td><input type="text" name="userName" value="${user.userName}"></td>
    </tr>
        <tr>
            <td>Password</td>
            <td><input type="text" name="password" value="${user.password}"></td>
        </tr>
        <tr>
            <td>Remember me</td>
            <td><input type="checkbox" name="rememberMe" value="Y"/> </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Submit"/>
                <a href="${pageContext.request.contextPath}/">Cancel</a>
            </td>
        </tr>
    </table>
</form>

<p style="color:blue;">User Name: tom, password: tom001 or jerry/jerry001</p>

<jsp:include page="_footer.jsp"></jsp:include>

</body>
</html>
