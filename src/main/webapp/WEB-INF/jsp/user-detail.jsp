<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<h1><c:out value="${user.name}" /></h1>

<br /><br />

<script type="text/javascript">
$(document).ready(function() {
	$('.nav-tabs a:first').tab('show'); // Select first tab
	$(".triggerRemove").click(function(e) {
		var origin = $(this);
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
					$.post("../blog/remove/" + blogId + ".html", function (data) {
						location.reload(true); // reload page
					});
                	dialog.close();
                }
            }]
	    });
	});
});
</script>

<!-- Nav tabs -->
<ul class="nav nav-tabs">
	<c:forEach items="${user.blogs}" var="blog">
	  <li><a href="#blog_${blog.id}" data-toggle="tab"><c:out value="${blog.name}" /></a></li>
	</c:forEach>
</ul>

<!-- Tab panes -->
<div class="tab-content">
<c:forEach items="${user.blogs}" var="blog">
  <div class="tab-pane" id="blog_${blog.id}">
	<h1><c:out value="${blog.name}" /></h1>
	<p>
	
		<a href="<spring:url value="/blog-form.html?blogId=${blog.id}" />" class="btn btn-primary">edit blog</a>

		<button class="btn btn-danger triggerRemove" id="${blog.id}">
			 remove blog
		</button>
		<a href="<c:out value="${blog.url}" />" target="_blank">
			<c:out value="${blog.url}" />
		</a>
		<div style="clear:both"></div>
		<img src="<spring:url value="/spring/icon/${blog.id}" />" style="float:left;padding-right:10px" />
		
		<form method="post" enctype="multipart/form-data" action="${user.id}.html">
			<span class="btn btn-default btn-file">
				Select icon (50 x 50 px) <input type="file" name="icon" />
			</span>
			<input type="hidden" name="blogId" value="${blog.id}" /> 
			<input type="submit" value="Upload" class="btn btn-primary" />
		</form>
		
	</p>

	<p>Note: Only posts from last two months and max. 10 posts will be displayed.</p>

	<table class="table table-bordered table-hover table-striped">
		<thead>
			<tr>
				<th>Item</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${blog.items}" var="item">
				<tr>
					<td>
						<strong>
							<a href="<c:out value="${item.link}" />" target="_blank">
								<c:out value="${item.title}" />
							</a>
						</strong>
						<br />
						${item.description}
						<br />
						${item.publishedDate}
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
  </div>
</c:forEach>
</div>
