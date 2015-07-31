<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>OpenID Consumer Redirection</title>
</head>
<body onload="document.forms['openid-consumer-redirection'].submit();">
<form name="openid-consumer-redirection" action="${destinationUrl}" method="post">
<c:forEach var="parameter" items="${parameterMap}">
<input type="hidden" name="${parameter.key}" value="${parameter.value}"/>
</c:forEach>
<input type="hidden" name="client_id" value="${client_id}"/>
<input type="hidden" name="response_type" value="${response_type}"/>
<input type="hidden" name="state" value="${state}"/>
<input type="hidden" name="redirect_uri" value="${redirect_uri}"/>
<input type="hidden" name="feedback" value="${feedback}"/>
<button type="submit">Continue</button>
</form>
</body>
</html>