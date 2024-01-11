<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
${customidMsg}
<form action="/saveCustomer" method=POST>
<pre>
Name          :<input type="text" name="name"/>
Address       :<textarea name="address"></textarea>
Mobile Number :<input type="text" name="mobileNumber"/>
Email         :<input type="text" name="email"/>
<input type="submit" value="Register"/>
</pre>
</form>
</body>
</html>