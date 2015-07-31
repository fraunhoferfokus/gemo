<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>OpenID Provider Redirection</title>
</head>
<body onload="document.forms['openid-provider-redirection'].submit();">
<form name="openid-provider-redirection" action="${message.OPEndpoint}" method="get">
<c:forEach var="parameter" items="${paramMap}">
<input type="hidden" name="${parameter.key}" value="${parameter.value}"/>
</c:forEach>
<c:forEach var="parameter" items="${message.parameterMap}">
<input type="hidden" name="${parameter.key}" value="${parameter.value}"/>
</c:forEach>
<button type="submit">Continue</button>
</form>
</body>
</html>