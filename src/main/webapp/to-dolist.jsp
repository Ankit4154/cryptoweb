<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<!-- Create HTML form -->
	<form action="to-dolist.jsp">
		Add new item : <input type="text" name="item"/>
		
		<input type="submit" value="submit" />	
	
	</form>
	<br>
	Item entered : <%= request.getParameter("item")%>
	
	<!-- Add new item to To-Do list -->
	
	<!-- Display all To-Do items from session -->
</body>
</html>