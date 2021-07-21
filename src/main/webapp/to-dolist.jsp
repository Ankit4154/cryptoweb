<%@ page import="java.util.*" language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>To Do List</title>
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
	
	<hr>
	<p>
		Your favorite language has been set to : 
		<%
			//Default language
			String favoriteLang = "English";
		
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
				for(Cookie cookie : cookies) {
					if(cookie.getName().equals("app.favoriteLang")){
						//out.println(cookie.getValue());
						favoriteLang = cookie.getValue();
						break;
					}
				}
			}
		%>
		<h4><%= favoriteLang %></h4>
	</p>
	<hr>
	<a href="cookies-personalize.jsp"> CookiesForm </a>
	<br><br>
	<a href="to-dolist.jsp"> Homepage </a>
	
</body>
</html>