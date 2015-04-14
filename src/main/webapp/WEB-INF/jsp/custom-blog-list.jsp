<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<c:forEach items="${blogs.content}" var="blog">
	<c:if test="${isAdmin}">
		<a href="custom-blog/edit/${blog.shortName}.html" class="btn btn-small btn-primary">edit</a>
	</c:if>
	<strong>
		<fmt:formatDate value="${blog.publishedDate}" pattern="dd.MM.yyyy HH:mm:ss" />:
		<a href="custom-blog/${blog.shortName}.html">${blog.title}</a>
	</strong>
	<br />
	${blog.shortDescription}
	
	<br /><br />
	
</c:forEach>

<c:if test="${currPage < (blogs.totalPages - 1)}">
	<a href="?page=${currPage + 1}">Older items</a>
</c:if>
