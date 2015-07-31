<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>OpenID Login</title>
<link href="/openid/css/bootstrap.min.css" rel="stylesheet">
<link href="/openid/css/style.css" rel="stylesheet">
<script type="text/javascript" src="/openid/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="/openid/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $(".descr").tooltip({
        placement : 'right'
    });
});
</script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-6 col-md-4 col-md-offset-4">
				<div class="account-wall">
					<img
						src="http://upload.wikimedia.org/wikipedia/commons/c/c8/OpenID_logo.svg"
						alt="">
					<form class="form-signin" role="form" name="openid-login-form"
						action="${destinationUrl}" method="get">
						<p>The client with the ID ${client_id} wants to have the
							following permissions:</p>
						<c:forEach var="scopeDescriptions" items="${scopeDescriptions}">
							<p class="form-control-static descr" style="width: 50px"
								data-toggle="tooltip"
								data-original-title="${scopeDescriptions.value}">${scopeDescriptions.key} <span
									class="glyphicon glyphicon-info-sign"></span>
							</p>
						</c:forEach>
						<p class="form-control-static">Please sign in to complete the
							action.</p>
						<div class="radio">
							<label> <input type="radio" name="feedback" id="option1"
								value="accept" checked> Accept permissions.
							</label>
						</div>
						<div class="radio">
							<label> <input type="radio" name="feedback" id="option2"
								value="reject"> Reject permissions.
							</label>
						</div>
						<c:set var="userid" value="${identifier}" />
						<input type="text" class="form-control" name="openid_identifier"
							placeholder="${fn:substringAfter(userid, '/openid/user/')}"
							disabled> <input type="password" class="form-control"
							name="password" placeholder="Password" required autofocus>
						<button class="btn btn-lg btn-primary btn-block" type="submit">
							Sign in</button>
						<input type="hidden" name="identifier" value="${identifier}" /> <input
							type="hidden" name="_loginAction" value="true" />
						<c:if test="${not empty client_id}">
							<input type="hidden" name="client_id" value="${client_id}" />
						</c:if>
						<c:if test="${not empty response_type}">
							<input type="hidden" name="response_type"
								value="${response_type}" />
						</c:if>
						<c:if test="${not empty state}">
							<input type="hidden" name="state" value="${state}" />
						</c:if>
						<c:if test="${not empty scope}">
							<input type="hidden" name="scope" value="${scope}" />
						</c:if>
						<c:if test="${not empty redirect_uri}">
							<input type="hidden" name="redirect_uri" value="${redirect_uri}" />
						</c:if>
						<c:forEach var="parameter" items="${paramMap}">
							<input type="hidden" name="${parameter.key}"
								value="${parameter.value}" />
						</c:forEach>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>