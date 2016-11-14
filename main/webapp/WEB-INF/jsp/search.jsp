<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>


<form action="search.html" method="get">
<p><input id="search" name="term" type="text" class="forminput" size="40"
  value="${param.term}" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty jobs}">
<table class="viewtable autowidth">
<tr><th>Title</th></tr>
<c:forEach items="${jobs}" var="jobs">
<tr>
  <td>${jobs.title}</td>
  
</tr>
</c:forEach>
</table>
</c:if>


</body>
</html>