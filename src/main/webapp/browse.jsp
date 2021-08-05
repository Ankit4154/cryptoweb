<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="Locale"
	value="${not empty param.Locale ? param.Locale : pageContext.request.locale}"
	scope="session" />
<fmt:setLocale value="${Locale}" />
<fmt:setBundle basename="com.crypto.i18n.labels" />

<!DOCTYPE html>
<html>
<head>
<title>Books!</title>
</head>
<body style="font-family: Arial; font-size: 20px;">
	<div
		style="height: 65px; align: center; background: #2075E5; font-family: Arial; color: white;">
		<br> <b> <a href=""
			style="font-family: garamond; font-size: 34px; margin: 0 0 0 10px; color: white; text-decoration: none;">Books<i>
					Save it!</i></a></b>
	</div>
	<div
		style="height: 25px; background: #2075E5; font-family: Arial; color: white;">
		<b> <a href="<%=request.getContextPath()%>/bookmark?Locale=de_DE"
			style="font-size: 16px; color: white; margin-left: 10px; text-decoration: none;">German
				(DE)</a> <a href="<%=request.getContextPath()%>/bookmark?Locale=es_ES"
			style="font-size: 16px; color: white; margin-left: 10px; text-decoration: none;">Spanish
				(ES)</a> <a href="<%=request.getContextPath()%>/bookmark?Locale=hi_IN"
			style="font-size: 16px; color: white; margin-left: 10px; text-decoration: none;">Hindi
				(IN)</a> <a href="<%=request.getContextPath()%>/bookmark"
			style="font-size: 16px; color: white; margin-left: 10px; text-decoration: none;">English
				(US)</a> <a href="<%=request.getContextPath()%>/bookmark/markedbooks"
			style="font-size: 16px; color: white; margin-left: 50%; text-decoration: none;">Saved
				Books</a> <a href="<%=request.getContextPath()%>/auth/logout"
			style="font-size: 16px; color: white; margin-left: 10px; text-decoration: none;">Logout</a>

		</b>
	</div>
	<br>
	<br>

	<table>
		<c:forEach var="book" items="${books}">
			<tr>
				<td><img src="${book.imageUrl}" width="175" height="200">
				</td>

				<td style="color: gray;"><fmt:message key="label.by" />: <span
					style="color: #B13100;">${book.authors[0]}</span> <br> <br>
					<fmt:message key="label.rating" />: <span style="color: #B13100;">${book.amazonRating}</span>
					<br> <br> <fmt:message key="label.publicationYear" />: <span
					style="color: #B13100;">${book.publicationYear}</span> <br> <br>
					<a
					href="<%=request.getContextPath()%>/bookmark/save?bid=${book.id}"
					style="font-size: 18px; color: #0058A6; font-weight: bold; text-decoration: none">Save</a>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</c:forEach>
	</table>
	<hr>
	<p style="font-size: 15px">
		Selected locale : ${Locale}<br> Page request language :
		<%=request.getLocale()%></p>
</body>
</html>