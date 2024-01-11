<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="/saveServed" method="GET">
<h3>${servedmsg}</h3>
<pre>
Enter Your Unique Code : <input type="text" name="uniqueCode"/><br>
<input type="submit" value="SUBMIT"/>
</pre>
</form>
</body>
</html>