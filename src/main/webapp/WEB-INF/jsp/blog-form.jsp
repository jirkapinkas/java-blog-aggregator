<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>


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

        <input type="submit" class="btn btn-primary" value="Save" />

</form:form>

<br /><br />
