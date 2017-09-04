<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<sitemesh:head/>
</head>
<body>  
 <%@ include file="../layouts/header.jsp"%>  
  <div id="content">
   <sitemesh:body/>  
  </div>  
 <%@ include file="../layouts/footer.jsp"%>  
</body> 
</html>