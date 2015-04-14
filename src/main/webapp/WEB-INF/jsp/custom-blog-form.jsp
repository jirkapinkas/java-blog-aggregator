<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<h2>Custom blog item:</h2>

<c:if test="${success eq true}">
	<div class="alert alert-success">Saved!</div>
</c:if>

<form:form commandName="customBlog" cssClass="form-horizontal blogForm">
		<form:hidden path="id" />
		<div class="form-group">
			<label for="title" class="col-sm-2 control-label">Title:</label>
			<div class="col-sm-10">
				<form:input path="title" cssClass="form-control" />
				<form:errors path="title" />
			</div>
		</div>
		<c:if test="${customBlog.id != null}">
			<div class="form-group">
				<label for="shortName" class="col-sm-2 control-label">Short name:</label>
				<div class="col-sm-10">
					<form:input path="shortName" cssClass="form-control" />
					<form:errors path="shortName" />
				</div>
			</div>
		</c:if>
		<div class="form-group">
			<label for="shortDescription" class="col-sm-2 control-label">Short description:</label>
			<div class="col-sm-10">
				<form:textarea path="shortDescription" cssClass="form-control" />
				<form:errors path="shortDescription" />
			</div>
		</div>
		<div class="form-group">
			<label for="description" class="col-sm-2 control-label">Description:</label>
			<div class="col-sm-10">
				<form:textarea path="description" cssClass="form-control" />
				<form:errors path="description" />
			</div>
		</div>

        <input type="submit" class="btn btn-primary" value="Save" />

</form:form>

<br /><br />
