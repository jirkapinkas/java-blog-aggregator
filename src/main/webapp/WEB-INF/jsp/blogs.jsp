<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp" %>

<c:if test="${isAdmin}">
	<h3>Errors:</h3>
	<ul>
		<c:forEach items="${blogs}" var="blog">
			<c:if test="${not blog.lastCheckStatus}">
				<li>
					fail ${blog.name} (${blog.lastCheckErrorCount} times), error: ${blog.lastCheckErrorText}
				</li>
			</c:if>
		</c:forEach>
	</ul>
</c:if>

<h1 style="font-size:25px">All blogs:</h1>

<jsp:include page="../layout/adsense.jsp" />

<br />

<script type="text/javascript">
$(document).ready(function() {
    $("img.lazy").unveil(200);
    $('#dataTable').DataTable( {
		"fnDrawCallback": function( oSettings ) {
			$("img.lazy").unveil(200);
		}
	});
} );
</script>

<table class="table table-bordered table-hover table-striped display" id="dataTable">
	<thead>
		<tr>
			<th>blog</th>
			<security:authorize access="${isAdmin}">
				<th>user</th>
				<th>edit</th>
			</security:authorize>
			</tr>
		</thead>
	<tbody>
		<c:forEach items="${blogs}" var="blog">
			<tr>
				<td>
					<img class="lazy" data-src="<spring:url value='/spring/icon/${blog.id}' />" alt="icon" style="float:left;padding-right:5px" />
					
					<a href="${blog.homepageUrl}" target="_blank" class="fa fa-home fa-lg" style="float:left;padding-top:0px;color:grey">
					</a>
					<a href="${blog.url}" target="_blank" class="fa fa-rss fa-lg"  style="float:left;padding-top:0px;padding-left:5px;color:grey">
					</a>
					<h1 style="font-size:25px;">${title}</h1>

					<a href="<spring:url value="/blog/${blog.shortName}.html" />">
						<strong>
							<c:out value="${blog.name}" />
						</strong>
					</a>
				</td>
				<security:authorize access="${isAdmin}">
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
				</security:authorize>
			</tr>
		</c:forEach>
	</tbody>
</table>
