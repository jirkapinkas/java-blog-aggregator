<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<c:if test="${success eq true}">
	<div class="alert alert-success">Saved!</div>
</c:if>

<security:authorize access="${isAdmin}">
	<img src="<spring:url value="/spring/icon/${blog.id}" />" style="float:left;padding-right:10px" />
	
	<form method="post" enctype="multipart/form-data" action="users/upload-icon/${blog.id}.html">
		<span class="btn btn-default btn-file">
			Select icon (50 x 50 px) <input type="file" name="icon" />
		</span>
		<input type="submit" value="Upload" class="btn btn-primary" />
	</form>
	
	<div style="clear:both"></div>
	
	<br /><br />
</security:authorize>

<form:form commandName="blog" cssClass="form-horizontal blogForm">

		<div class="form-group">
			<label for="name" class="col-sm-2 control-label">Name:</label>
			<div class="col-sm-10">
				<form:input path="name" cssClass="form-control" />
				<form:errors path="name" />
			</div>
		</div>
		<div class="form-group">
			<label for="shortName" class="col-sm-2 control-label">Short&nbsp;name:</label>
			<div class="col-sm-10">
				<form:input path="shortName" cssClass="form-control" />
				<form:errors path="shortName" />
			</div>
		</div>
		<div class="form-group">
			<label for="url" class="col-sm-2 control-label">RSS&nbsp;/&nbsp;ATOM&nbsp;URL:</label>
			<div class="col-sm-10">
				<form:input path="url" cssClass="form-control" />
				<form:errors path="url" />
			</div>
		</div>
		<div class="form-group">
			<label for="homepageUrl" class="col-sm-2 control-label">Homepage&nbsp;URL:</label>
			<div class="col-sm-10">
				<form:input path="homepageUrl" cssClass="form-control" />
				<form:errors path="homepageUrl" />
			</div>
		</div>
		<div class="form-group">
			<label for="aggregator" class="col-sm-2 control-label">Blog aggregator:</label>
			<div class="col-sm-10">
				<form:checkbox path="aggregator" />
			</div>
		</div>

        <input type="submit" class="btn btn-primary" value="Save" />

</form:form>

<br /><br />
