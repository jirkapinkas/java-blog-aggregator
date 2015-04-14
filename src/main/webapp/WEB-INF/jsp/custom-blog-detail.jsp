<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<strong>
	<fmt:formatDate value="${blog.publishedDate}" pattern="dd.MM.yyyy HH:mm:ss" />:
	${blog.title}
</strong>
<br />
${blog.description}

<br /><br />
	
