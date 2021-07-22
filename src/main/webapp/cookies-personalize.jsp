<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Personalize Site</title>
</head>
<!-- Cookie logic, reading for data and setting cookie -->
<%
// read form data
//String favLang = (String)session.getAttribute("favoriteLang");
String favLang = request.getParameter("favoriteLang");

// create cookie
Cookie cookie = new Cookie("app.favoriteLang", favLang);

// set the life span of cookie, total number of seconds
cookie.setMaxAge(60 * 60 * 24 * 365);

// send cookie to browser
response.addCookie(cookie);
%>
<body>
	<form action="cookies-personalize.jsp">
		Select favorite language : <select name="favoriteLang">
			<option>Hindi</option>
			<option>English</option>
			<option>Telugu</option>
		</select> <input type="submit" value="submit" />
	</form>
	<hr>
	<br>
	<br> Your favorite language has been set to :
	${param.favoriteLang}
	<br>
	<br> From existing cookie :
	<%
	String cookieName = request.getHeader("Cookie");
	int langInd = cookieName.indexOf("Lang=") + 5;
	int semiInd = cookieName.indexOf(";", langInd);
	if (semiInd != -1) {
		if (cookieName.substring(langInd, semiInd).isEmpty() || cookieName.substring(langInd, semiInd).isBlank()) {
			out.println(" Empty ");
		} else {
			out.println(cookieName.substring(langInd, semiInd));
		}
	} else {
		out.println(cookieName.substring(langInd, cookieName.length()));
	}
	%>
	<br>
	<br>
	<a href="to-dolist.jsp"> Return to Homepage</a>
</body>
</html>