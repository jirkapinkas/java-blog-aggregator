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

<table class="table table-bordered table-hover table-striped display" id="dataTable">
	<thead>
		<tr>
			<th>blog</th>
			<th style="width:50px">popularity</th>
			<th style="width:100px">category</th>
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
					<img class="lazy" data-src="<spring:url value='/spring/icon/${blog.id}' />" style="float:left;padding-right:5px" />
					
					<a href="${blog.homepageUrl}" class="fa fa-home fa-lg" style="float:left;padding-top:0px;color:grey">
					</a>
					<a href="${blog.url}" class="fa fa-rss fa-lg"  style="float:left;padding-top:0px;padding-left:5px;color:grey">
					</a>
					<br />

					<a href="<spring:url value="/blog/${blog.shortName}.html" />">
						<strong>
							<c:out value="${blog.publicName}" />
						</strong>
					</a>
				</td>
				<td>
					<c:choose>
						<c:when test="${blog.popularity >= 30}">
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
						</c:when>
						<c:when test="${blog.popularity >= 20}">
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
						</c:when>
						<c:when test="${blog.popularity >= 15}">
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
						</c:when>
						<c:when test="${blog.popularity >= 10}">
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
						</c:when>
						<c:otherwise>
							<i class="fa fa-star" style="color:yellow;-webkit-text-stroke-width: 1px;-webkit-text-stroke-color: orange;"></i>
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<span class="label label-default">${blog.category.name}</span>
				</td>
				<security:authorize access="${isAdmin}">
					<td>
						<a href="<spring:url value='/users/${blog.user.id}.html' />">
							${blog.user.name}
						</a>
					</td>
					<td>
						<a href="<spring:url value='/blog-form.html?blogId=${blog.id}' />" class="btn btn-primary btn-xs">
							edit
						</a>
						<button class="btn btn-danger btn-xs triggerRemove" id="${blog.id}" onclick="removeBlog(this)">
							 remove
						</button>
						
						category:
						<select class="categorySelect" id="${blog.id}" onchange="categorySelectChange(this)">
							<c:if test="${blog.category.id != 0}">
								<option value="" selected="selected" disabled="disabled"></option>
							</c:if>
							<c:forEach items="${categories}" var="category">
								<option ${category.id eq blog.category.id ? 'selected = "selected"' : ''} value="${category.id}">
									${category.name}
								</option>
							</c:forEach>
						</select>
					</td>
				</security:authorize>
			</tr>
		</c:forEach>
	</tbody>
</table>

<script type="text/javascript">
$(document).ready(function() {
    $("img.lazy").unveil(unveilTreshold);
    $('#dataTable').DataTable( {
		"fnDrawCallback": function( oSettings ) {
			$("img.lazy").unveil(unveilTreshold);
		}
	});
} );
</script>

<security:authorize access="${isAdmin}">
<script type="text/javascript">
function categorySelectChange(element) {
	var blogId = $(element).attr("id");
	var categoryId = $(element).val();
	$.post("admin-categories/set/" + blogId + "/cat/" + categoryId + ".json", function(data) { });
}
function removeBlog(e) {
	var origin = $(e);
    BootstrapDialog.show({
        title: 'Really delete?',
        message: 'Really delete?',
        buttons: [{
            label: 'Cancel',
            action: function(dialog) {
            	dialog.close();
            }
        }, {
            label: 'Delete',
            cssClass: 'btn-primary',
            action: function(dialog) {
            	var blogId = origin.attr("id");
			$.post("blog/remove/" + blogId + ".html", function (data) {
				location.reload(true); // reload page
			});
            	dialog.close();
            }
        }]
    });
}
</script>
</security:authorize>