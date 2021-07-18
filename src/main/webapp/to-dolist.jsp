<%@ page import="java.util.*" language="java" contentType="text/html; charset=ISO-8859-1"
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
	
	<!-- Add new item to To-Do list -->
	<%
		// Get To-do item from session
		List<String> items = (List<String>) session.getAttribute("toDoList");
		
		// if no to do items exist, create a new one
		if(items == null){
			items = new ArrayList<String>();
			session.setAttribute("toDoList", items);
		}
		// see if there is form data to add
		String item = request.getParameter("item");
		if(item != null && !item.trim().equals("")){
			if(!items.contains(item)){
				items.add(item);
			}
		}
	%>
	<!-- Display all To-Do items from session -->
	<hr>
	<b>To List items :</b>
	<br>
	<ol>
		<%
			for(String x : items){
				out.println("<li>" + x + "</li>");
			}
		%>
	</ol>
</body>
</html>