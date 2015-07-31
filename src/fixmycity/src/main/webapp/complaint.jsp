<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="de.fhg.fokus.cm.ejb.*"%>
<%@ page language="java"
	import="de.fhg.fokus.cm.service.ComplaintService"%>
<%@ page language="java"
	import="de.fhg.fokus.cm.service.impl.ComplaintServiceImpl"%>
<%@ page language="java"
	import="de.fhg.fokus.cm.service.CategoryService"%>
<%@ page language="java"
	import="de.fhg.fokus.cm.service.impl.CategoryServiceImpl"%>
<%@ page language="java" import="de.fhg.fokus.cm.service.StatusService"%>
<%@ page language="java"
	import="de.fhg.fokus.cm.service.impl.StatusServiceImpl"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Complaint</title>
</head>
<body>

	<h1>Form to create a new complaint</h1>
	<form action="cm/complaints" method="post">
		title <input name="title" value="ComplaintTitle" /> <br />
		description
		<textarea name="description" cols=40 rows=6></textarea>
		<br /> <label>Tags</label><input name="tags" value="tag1,tag2,tag3" />
				<label>Status</label><br />
		<%
			StatusService stat = new StatusServiceImpl();
			List<Status> sl = stat.getStatuses().getStatuses();
			for (Iterator<Status> iter = sl.iterator(); iter.hasNext();) {
				Status element = iter.next();
				out.println("<input type='radio' name='statusId' value='"
						+ element.getId() + "'>" + element.getTitle() + "<br/>");
			}
		%>
		<br /> <label>Kategorie</label><br />
		<%
			ComplaintService cms = new ComplaintServiceImpl();
			CategoryService cat = new CategoryServiceImpl();
			List<Category> cl = cat.getCategories().getCategories();
			for (Iterator<Category> iter = cl.iterator(); iter.hasNext();) {
				Category element = iter.next();
				out.println("<input type='radio' name='categoryId' value='"
						+ element.getId() + "'>" + element.getTitle() + "<br/>");
			}
		%>

		<label>latitude</label><input name="latitude" value="52" /> <br /> <label>longitude</label><input
			name="longitude" value="13" /> <br /> <label>countryCode</label><input
			name="countryCode" value="DE" /> <br /> <label>city</label><input
			name="city" value="Berlin" /> <br /> <label>street</label><input
			name="street" value="Strasse" /> <br /> <label>houseNo</label><input
			name="houseNo" value="0815" /> <br /> <label>postalCode</label><input
			name="postalCode" value="10000" /> <input type="submit"
			value="Submit" />
	</form>
</body>
</html>

