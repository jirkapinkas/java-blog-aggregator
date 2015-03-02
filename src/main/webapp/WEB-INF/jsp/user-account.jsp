<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<!-- Button trigger modal -->
<button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
  New blog
</button>


<form:form commandName="blog" cssClass="form-horizontal blogForm">
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">New blog</h4>
      </div>
      <div class="modal-body">

		<div class="form-group">
			<label for="name" class="col-sm-3 control-label">Name:</label>
			<div class="col-sm-9">
				<form:input path="name" cssClass="form-control" />
				<form:errors path="name" />
			</div>
		</div>
		<div class="form-group">
			<label for="shortName" class="col-sm-3 control-label">Short&nbsp;name:</label>
			<div class="col-sm-9">
				<form:input path="shortName" cssClass="form-control" />
				<form:errors path="shortName" />
			</div>
		</div>
		<div class="form-group">
			<label for="url" class="col-sm-3 control-label">RSS&nbsp;/&nbsp;ATOM&nbsp;URL:</label>
			<div class="col-sm-9">
				<form:input path="url" cssClass="form-control" />
				<form:errors path="url" />
			</div>
		</div>
		<div class="form-group">
			<label for="homepageUrl" class="col-sm-3 control-label">Homepage&nbsp;URL:</label>
			<div class="col-sm-9">
				<form:input path="homepageUrl" cssClass="form-control" />
				<form:errors path="homepageUrl" />
			</div>
		</div>

      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <input type="submit" class="btn btn-primary" value="Save" />
      </div>
    </div>
  </div>
</div>
</form:form>

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
	$(".blogForm").validate(
			{
				rules: {
					name: {
						required : true,
						minlength : 1
					},
					url: {
						required : true,
						url: true,
						remote : {
							url: "<spring:url value='/blog/available.html' />",
							type: "get",
							data: {
								url: function() {
									return $("#url").val();
								}
							}
						}
					},
					homepageUrl: {
						required : true,
						minlength : 1,
						url: true
					},
					shortName: {
						required : true,
						minlength : 1
					}
				},
				highlight: function(element) {
					$(element).closest('.form-group').removeClass('has-success').addClass('has-error');
				},
				unhighlight: function(element) {
					$(element).closest('.form-group').removeClass('has-error').addClass('has-success');
				},
				messages: {
					url: {
						remote: "Such blog already exists!"
					}
				}
			}
		);
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
	
	<a href="<c:out value="${blog.url}" />" target="_blank"><c:out value="${blog.url}" /></a></p>

	<p>Note: Only posts from last two months and max. 10 posts will be displayed.</p>

	<table class="table table-bordered table-hover table-striped">
		<thead>
			<tr>
				<th>item</th>
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
						<c:out value="${item.publishedDate}" />
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
  </div>
</c:forEach>
</div>
