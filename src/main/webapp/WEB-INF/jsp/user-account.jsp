<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<!-- Button trigger modal -->
<button class="btn btn-primary btn-lg" data-toggle="modal"
	data-target="#myModal">New blog</button>

<br /><br />

<div class="alert alert-info">Add only a blog, which isn't already
	tracked. Your submission will be reviewed by an administrator and if
	accepted, news from the blog will be displayed on front page. If not
	accepted, the blog will be deleted.</div>

<form:form commandName="blog" cssClass="form-horizontal blogForm">
	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
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
					<div class="form-group">
						<label for="aggregator" class="col-sm-3 control-label">Aggregator:</label>
						<div class="col-sm-9">
							<form:checkbox path="aggregator" />
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

<br />
<br />

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(".triggerRemove")
								.click(
										function(e) {
											var origin = $(this);
											BootstrapDialog
													.show({
														title : 'Really delete?',
														message : 'Really delete?',
														buttons : [
																{
																	label : 'Cancel',
																	action : function(
																			dialog) {
																		dialog
																				.close();
																	}
																},
																{
																	label : 'Delete',
																	cssClass : 'btn-primary',
																	action : function(
																			dialog) {
																		var blogId = origin
																				.attr("id");
																		$
																				.post(
																						"../blog/remove/"
																								+ blogId
																								+ ".html",
																						function(
																								data) {
																							location
																									.reload(true); // reload page
																						});
																		dialog
																				.close();
																	}
																} ]
													});
										});
						$(".blogForm")
								.validate(
										{
											rules : {
												name : {
													required : true,
													minlength : 1
												},
												url : {
													required : true,
													url : true,
													remote : {
														url : "<spring:url value='/blog/available.html' />",
														type : "get",
														data : {
															url : function() {
																return $("#url")
																		.val();
															}
														}
													}
												},
												homepageUrl : {
													required : true,
													minlength : 1,
													url : true
												},
												shortName : {
													required : true,
													minlength : 1
												}
											},
											highlight : function(element) {
												$(element).closest(
														'.form-group')
														.removeClass(
																'has-success')
														.addClass('has-error');
											},
											unhighlight : function(element) {
												$(element)
														.closest('.form-group')
														.removeClass(
																'has-error')
														.addClass('has-success');
											},
											messages : {
												url : {
													remote : "Such blog already exists!"
												}
											}
										});
					});
</script>


<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>name</th>
			<th>operations</th>
			<th>accepted?</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${user.blogs}" var="blog">
			<tr>
				<td>
					<a href="blog/${blog.shortName}.html">
						<c:out value="${blog.name}" />
					</a>
				</td>
				<td>
					${blog.category eq null ? "not yet reviewed" : "accepted"}
				</td>
				<td>
					<a href="<spring:url value="/blog-form.html?blogId=${blog.id}" />" class="btn btn-primary">edit</a>
					<button class="btn btn-danger triggerRemove" id="${blog.id}">remove</button>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
