<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h3>${lmsg}</h3>
<h3>${eMsg}</h3>
<form action="login" method=GET>
<pre>
Email    :<input type="text" name="email"/><br>
Password :<input type="password" name="password"/><br>
<input type="submit" value="LOGIN"/>
</pre>
</form>
</body>
</html>