<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<c:if test="${success eq true}">
	<div class="alert alert-success">Saved!</div>
</c:if>

<form:form commandName="user" cssClass="form-horizontal" autocomplete="off">

<!-- fix for chrome, because autocomplete="off" isn't working -->
<!-- http://stackoverflow.com/questions/12374442/chrome-browser-ignoring-autocomplete-off -->
<input style="display:none"/>
<input type="password" style="display:none"/>

		<div class="form-group">
			<label for="password" class="col-sm-2 control-label">Username:</label>
			<div class="col-sm-10">
				<form:input path="name" cssClass="form-control" />
				<form:errors path="name" />
			</div>
		</div>

		<div class="form-group">
			<label for="password" class="col-sm-2 control-label">Password:</label>
			<div class="col-sm-10">
				<form:password path="password" cssClass="form-control" />
				<form:errors path="password" />
			</div>
		</div>

        <input type="submit" class="btn btn-primary" value="Save" />

</form:form>

<br /><br />
