<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<c:if test="${success eq true}">
	<div class="alert alert-success">Saved!</div>
</c:if>

<form:form commandName="configuration" cssClass="form-horizontal" autocomplete="off">

		<div class="form-group">
			<label for="brandName" class="col-sm-2 control-label">Brand name:</label>
			<div class="col-sm-10">
				<form:input path="brandName" cssClass="form-control" />
			</div>
		</div>

		<div class="form-group">
			<label for="title" class="col-sm-2 control-label">Title:</label>
			<div class="col-sm-10">
				<form:input path="title" cssClass="form-control" />
			</div>
		</div>

		<div class="form-group">
			<label for="title" class="col-sm-2 control-label">Homepage heading:</label>
			<div class="col-sm-10">
				<form:input path="homepageHeading" cssClass="form-control" />
			</div>
		</div>

		<div class="form-group">
			<label for="title" class="col-sm-2 control-label">Top page heading:</label>
			<div class="col-sm-10">
				<form:input path="topHeading" cssClass="form-control" />
			</div>
		</div>

		<div class="form-group">
			<label for="footer" class="col-sm-2 control-label">Footer:</label>
			<div class="col-sm-10">
				<form:textarea path="footer" rows="5" cssClass="form-control" />
			</div>
		</div>

		<div class="form-group">
			<label for="googleAdsense" class="col-sm-2 control-label">Google Adsense:</label>
			<div class="col-sm-10">
				<form:textarea path="googleAdsense" rows="5" cssClass="form-control" />
			</div>
		</div>

		<div class="form-group">
			<label for="googleAnalytics" class="col-sm-2 control-label">Google Analytics:</label>
			<div class="col-sm-10">
				<form:textarea path="googleAnalytics" rows="5" cssClass="form-control" />
			</div>
		</div>

        <input type="submit" class="btn btn-primary" value="Save" />

</form:form>

<br /><br />
