<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>OpenID Login</title>
<link href="/openid/css/bootstrap.min.css" rel="stylesheet">
<link href="/openid/css/style.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <div class="account-wall">
                <img src="http://upload.wikimedia.org/wikipedia/commons/c/c8/OpenID_logo.svg"
                    alt="">
                <form class="form-signin" action="/openid/consumer" method="post">
                <input type="text" class="form-control" style="margin-bottom: 11px;" name="openid_identifier" placeholder="Your OpenID..." required autofocus>
                <button class="btn btn-lg btn-primary btn-block" type="submit">
                    Login</button>
                <c:forEach var="parameter" items="${param}">
				<input type="hidden" name="${parameter.key}" value="${parameter.value}"/>
				</c:forEach>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>