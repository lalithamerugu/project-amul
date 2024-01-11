<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h4>${msg}</h3>
<h4>${qMsg}</h3>

<h3>Add Today's Customers</h4>
<form action ="addCustomer" method=POST>
Customer Name                 :<input type="text" name="name"/><br>
Quantity Of Milk Served Today :<input type="text" name="milkQuantity"/><br>

<h4>Choose One Payment Option</h4>
<input type="radio" name="paymentMethod" value="onlinePayment" id="onlinePayment">
<label for="onlinePayment">Online Payment</label><br>

<input type="radio" name="paymentMethod" value="cashOnDelivery" id="cashOnDelivery">
<label for="cashOnDelivery">Pay With Cash</label><br>

<input type="submit" value="SAVE"/>
</form>
<a href="showCustomers">Click here to view today's customers</a>
</body>
</html>