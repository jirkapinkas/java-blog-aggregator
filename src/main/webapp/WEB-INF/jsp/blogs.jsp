<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp" %>

<h1 style="font-size:25px">All blogs:</h1>

<jsp:include page="../layout/adsense.jsp" />

<br />

<table class="table table-bordered table-hover table-striped">
	<security:authorize access="hasRole('ROLE_ADMIN')">
		<thead>
			<tr>
				<th>blog</th>
				<th>user</th>
				<th>edit</th>
				<th>last result</th>
			</tr>
		</thead>
	</security:authorize>
	<tbody>
		<c:forEach items="${blogs}" var="blog">
			<tr>
				<td>

					<img src="<spring:url value='/spring/icon/${blog.id}' />" alt="icon" style="float:left;padding-right:5px" />

					<table style="float:left">
						<tr>
							<td style="width:40px">
								<a href="${blog.homepageUrl}" target="_blank">
									<img src="<spring:url value='/resources/images/home.png' />" alt="home" style="width:15px" />
								</a>
								<a href="${blog.url}" target="_blank">
									<img src="<spring:url value='/resources/images/rss.png' />" alt="rss" style="width:15px"/>
								</a>
							</td>
							<td>
								<h1 style="font-size:25px">${title}</h1>
							</td>
						</tr>
					</table>

					<a href="<spring:url value="/blog/${blog.shortName}.html" />">
						<strong>
							<c:out value="${blog.name}" />
						</strong>
					</a>
				</td>
				<security:authorize access="hasRole('ROLE_ADMIN')">
					<td>
						<a href="<spring:url value='/users/${blog.user.id}.html' />">
							${blog.user.name}
						</a>
					</td>
					<td>
						<a href="<spring:url value='/blog-form.html?blogId=${blog.id}' />">
							edit
						</a>
					</td>
					<td>
						<c:choose>
							<c:when test="${blog.lastCheckStatus eq true}">
								ok
							</c:when>
							<c:otherwise>
								fail (${blog.lastCheckErrorCount} times), error: ${blog.lastCheckErrorText}
							</c:otherwise>
						</c:choose>
					</td>
				</security:authorize>
			</tr>
		</c:forEach>
	</tbody>
</table>
