<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<h3>
	<c:if test="${isAdmin}">
		<a href="../admin-news/edit/${news.shortName}.html" class="btn btn-small btn-primary">edit</a>
	</c:if>
	${news.title}
</h3>

<div style="padding-bottom:10px;color:grey"><fmt:formatDate value="${news.publishedDate}" pattern="dd.MM.yyyy HH:mm:ss" /></div>


<jsp:include page="../layout/adsense.jsp" />

<br />

${news.description}

<br /><br />

${configuration.disqusCode}

${configuration.newsSocialButtons}