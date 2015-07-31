<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="assets/ico/favicon.png">

    <!-- Bootstrap core CSS -->
    <link href="/openid/css/bootstrap.min.css" rel="stylesheet">
    <script type="text/javascript" src="/openid/js/jquery-2.0.3.min.js"></script>
    <script type="text/javascript">
	$(document).ready(function() {

		$('#form')[0].reset();
		//result texts
		var checking_html = 'Checking...';
		var too_short_html = 'Username too short.';
		var length_ok_html = 'Length ok.';
	
		$('#username_input').keyup(function(){
			if($('#username_input').val().length < 3){
// 				$('#feedback').removeClass("alert-success").addClass("alert-danger");
				$('#feedback').html(too_short_html).show();
			} else {
// 				$('#feedback').removeClass("alert-danger").addClass("alert-info");
				$('#feedback').html(length_ok_html).show();
			
			}  		
		});
		
		$('#username_input').blur(function(){
			if($('#username_input').val().length >= 3){
				//else show the checking_text and run the function to check
				$('#feedback').html(checking_html).show();
				check_availability();
				}
		});
	});

	//function to check username availability
	function check_availability(){
	
			//get the username
			var username = $('#username_input').val();
	
			//use ajax to run the check
			$.post("/openid/reg/check_user", { username: username },
				function(result){
					//if the result is 0
					if(result == 0){
						//show that the username is available
// 						$('#feedback').removeClass("alert-info").addClass("alert-success");
						$('#feedback').hide();
					}else{
						//show that the username is NOT available
// 						$('#feedback').removeClass("alert-info").addClass("alert-danger");
						$('#feedback').html(username + ' is not Available').show();
					}
			});
	
	}
    </script>
	<title>OpenID-Provider Login</title>
</head>
<body>
<form class="form-horizontal" id="form" role="form" action="/openid/reg/register" method="post">
  <div class="panel-group" id="accordion">
  	<div class="panel panel-default">
	    <div class="panel-heading">
	      <h4 class="panel-title">
	        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
	          Required Information
	        </a>
	      </h4>
	    </div>
	    <div id="collapseOne" class="panel-collapse collapse in">
	      <div class="panel-body">
			  <div class="form-group">
			    <label for="firstname" class="col-lg-2 control-label">Firstname</label>
			    <div class="col-lg-3">
			      <input type="text" class="form-control" id="firstname" name="firstname" placeholder="Firstname" autofocus>
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="lastname" class="col-lg-2 control-label">Lastname</label>
			    <div class="col-lg-3">
			      <input type="text" class="form-control" id="lastname" name="lastname" placeholder="Lastname">
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="username" class="col-lg-2 control-label">Username</label>
			    <div class="col-lg-3">
			      <input type="text" class="form-control" id="username_input" name="username" placeholder="Username">
					<label class="error" id="feedback" style="display:none">
					</label>
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="password" class="col-lg-2 control-label">Password</label>
			    <div class="col-lg-3">
			      <input type="password" class="form-control" id="password" name="password" placeholder="Password">
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="email" class="col-lg-2 control-label">Email</label>
			    <div class="col-lg-3">
			      <input type="email" class="form-control" id="email" name="email" placeholder="Email">
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="birthdate" class="col-lg-2 control-label">Birthdate</label>
			    <div class="col-lg-3">
			      <input type="text" class="form-control" id="birthdate" name="birthdate" placeholder="Date of birth" >
			    </div>
			  </div>
		  	</div>
  		</div>
  	</div>
   <div class="panel panel-default">
    <div class="panel-heading">
      <h4 class="panel-title">
        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
          Additional Information
        </a>
      </h4>
    </div>
    <div id="collapseTwo" class="panel-collapse collapse">
      <div class="panel-body">
      <!-- insert optional -->
		  <div class="form-group">
		    <label for="street" class="col-lg-2 control-label">Street</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="street" name="street" placeholder="Street" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="housenr" class="col-lg-2 control-label">Nr</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="housenr" name="housenr" placeholder="House Number" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="location" class="col-lg-2 control-label">Location</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="location" name="location" placeholder="Location" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="postalcode" class="col-lg-2 control-label">Postal code</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="postalcode" name="postalcode" placeholder="ZIP Code" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="phonenumber" class="col-lg-2 control-label">Phone Number</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="phonenumber" name="phonenumber" placeholder="Phone number" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="bankaccountowner" class="col-lg-2 control-label">Bank Account Owner</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="bankaccountowner" name="bankaccountowner" placeholder="Bank account Owner" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="bankaccountnumber" class="col-lg-2 control-label">Bank Account Number</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="bankaccountnumber" name="bankaccountnumber" placeholder="bankaccountnumber" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="bankcode" class="col-lg-2 control-label">Bankcode</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="bankcode" name="bankcode" placeholder="Bankcode" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="preferences" class="col-lg-2 control-label">Preferences</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="preferences" name="preferences" placeholder="Preferences" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="vehicletype" class="col-lg-2 control-label">Vehicle Type</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="vehicletype" name="vehicletype" placeholder="Vehicle Type" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="publictransportaffinity" class="col-lg-2 control-label">Public Transport Affinity</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="publictransportaffinity" name="publictransportaffinity" placeholder="publictransportaffinity" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="drivinglicenseclass" class="col-lg-2 control-label">Driving License Class</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="drivinglicenseclass" name="drivinglicenseclass" placeholder="Driving License Class" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="drivinglicensedate" class="col-lg-2 control-label">Driving License Date</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="drivinglicensedate" name="drivinglicensedate" placeholder="Driving License Date" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="drivinglicenselocation" class="col-lg-2 control-label">Driving License Location</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="drivinglicenselocation" name="drivinglicenselocation" placeholder="Driving License Location" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="drivinglicenseid" class="col-lg-2 control-label">Driving License ID</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="drivinglicenseid" name="drivinglicenseid" placeholder="Driving License ID" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="lang" class="col-lg-2 control-label">Language</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="lang" name="lang" placeholder="Language" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="creditcardnumber" class="col-lg-2 control-label">Creditcard Number</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="creditcardnumber" name="creditcardnumber" placeholder="Creditcard Number" >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="termsandconditionsdate" class="col-lg-2 control-label">TAC Date</label>
		    <div class="col-lg-3">
		      <input type="text" class="form-control" id="termsandconditionsdate" name="termsandconditionsdate" placeholder="TAC Date" >
		    </div>
		  </div>
  </div>
  </div>
  </div>
		  <div class="form-group">
		    <div class="col-lg-offset-2 col-lg-10">
		      <button type="submit" class="btn btn-default">Register</button>
		    </div>
		  </div>
  </div>
  
</form>
</body>
<script type="text/javascript" src="/openid/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/openid/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="/openid/js/main.js"></script>
</html>