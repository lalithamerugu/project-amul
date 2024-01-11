<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.demo.model.CustomerDetails" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h3>${qMsg}</h3>
<h3>Add Today's Customer<h3>
<form action="/addTodaysCustomer" method="POST">
<pre>
Name   : <select name="name">
			<option value="">-SELECT-</option><br>
			<%
				List<CustomerDetails> customers= (List<CustomerDetails>)request.getAttribute("customerslist");
				for(CustomerDetails c:customers){
			%>
				<option value="<%= c.getName() %>"><%= c.getName() %></option>
			 <%
           		 }
       		 %>	
		</select>
		
Milk Quantity : <input type="text" name="milkQuantity"/><br>
<input type="submit" value="Submit">
</pre>
</form>
</body>
</html>