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
${list}
${deleteMsg}
<h1>Customers List</h1>
    <table border=5>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Address</th>
                <th>Mobile Number</th>
                <th>Email</th>
                <th>SELECT</th>
                <th>SELECT</th>
            </tr>
        </thead>
        <tbody>
            <%
            List<CustomerDetails> laps = (List<CustomerDetails>) request.getAttribute("mylist");
            for (CustomerDetails ll : laps) {
            %>
            <tr>
                <td><%= ll.getCustomId() %></td>
                <td><%= ll.getName() %></td>
                <td><%= ll.getAddress() %></td>
                <td><%= ll.getMobileNumber() %></td>
                <td><%= ll.getEmail() %></td>
                <td><a href="dodelete?id=<%=ll.getId() %>">DELETE</a></td>
                <td><a href="edit?id=<%=ll.getId() %>">EDIT</a></td>
            </tr>
            <%
            }
            %>
        </tbody>
    </table>


</body>
</html>