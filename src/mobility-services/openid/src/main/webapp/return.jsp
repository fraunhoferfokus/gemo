<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="text/html; charset=UTF-8" import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>OpenId sample</title>
</head>
<body>
<div>
<div>Login Success!</div>
<div>
<fieldset>
<legend>Your OpenID</legend>
<input type="text" name="openid_identifier" value="${identifier}" style="width: 50em"/>
</fieldset>
</div>
<div id="sreg-result">
<fieldset>
<legend>Simple Registration</legend>
<table>
<tr>
<th>Fullname:</th>
<td>${fullname}</td>
</tr>
<tr>
<th>Date of birth:</th>
<td>${dob}</td>
</tr>	
</table>
</fieldset>
</div>
<div id="ax-result">
<fieldset>
<legend>Attribute Exchange</legend>
<table>
<c:forEach items="${attributes}" var="attribute">
<tr>
<th>${attribute.key}:</th>
<td>${attribute.value}</td>
</tr>
</c:forEach>
</table>
</fieldset>
</div>	
</div>
</body>
</html>