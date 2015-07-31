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
<title>Search Complaints</title>
</head>
<body>

	<h1>Search</h1>
	<form action="cm/complaints" method="get">
		<label>Breitengrad</label> <input name="latitude" value="52" /> <br />
		<label>Längengrad</label> <input name="longitude" value="13" /> <br />
		<label>Radius in km</label> <input name="radius" value="30" /> <br />
		<label>Stichwörter</label> <input name="tags"
			value="strasse,schnee,abfall" /> <br /> <label>Kategorie</label><br />
		<%
			ComplaintService cms = new ComplaintServiceImpl();
			CategoryService cat = new CategoryServiceImpl();
			StatusService stat = new StatusServiceImpl();
		%>
		<%
			List<Category> cl = cat.getCategories().getCategories();
			out.println("<input type=\"radio\" name=\"categoryId\" value=\"\" checked>alle Kategorien<br/>");
			for (Iterator<Category> iter = cl.iterator(); iter.hasNext();) {
				Category element = iter.next();
				out.println("<input type=\"radio\" name=\"categoryId\" value=\""
						+ element.getId()
						+ "\">"
						+ element.getTitle()
						+ "<br/>");
			}
		%>
		<label>Status</label><br />
		<%
			List<Status> sl = stat.getStatuses().getStatuses();
			out.println("<input type=\"radio\" name=\"statusId\" value=\"\" checked>alle Status<br/>");
			for (Iterator<Status> iter = sl.iterator(); iter.hasNext();) {
				Status element = iter.next();
				out.println("<input type=\"radio\" name=\"statusId\" value=\""
						+ element.getId() + "\">" + element.getTitle()
						+ "<br/>");
			}
		%>
		<input type="checkbox" name="hasPhoto" value="true"> nur mit
		Fotos<br> <label>min Bewertung</label> <input name="minRating"
			value="2" /> <br /> <label>max Bewertung</label> <input
			name="maxRating" value="4" /> <br /> <label>Start</label> <input
			name="offset" value="0" /> <br /> <label>Anzahl der
			Ergebnisse</label> <input name="limit" value="20" /><br /> <label>exakter
			Benutzername</label> <input name="userId" value="" /> <br /> <label>Benutzername</label>
		<input name="user" value="" /> <br /> <label>Sortierung nach</label>
		<input name="orderBy" value="title" /> <br /> <label>Absteigend
			sortieren</label> <input type="checkbox" name="orderDesc" value="true">
		ja<br> <br /> <input type="submit" value="Submit" />
	</form>


</body>
</html>

