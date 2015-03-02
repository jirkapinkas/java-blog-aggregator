<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<c:if test="${success eq true}">
	<div class="alert alert-success">Saved!</div>
</c:if>

<!-- Button trigger modal -->
<button class="btn btn-primary btn-lg triggerAdd">
  New category
</button>

<form:form commandName="category" cssClass="form-horizontal categoryForm">
	<form:hidden path="id" />
	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title" id="myModalLabel">Category form</h4>
	      </div>
	      <div class="modal-body">
	
			<div class="form-group">
				<label for="name" class="col-sm-3 control-label">Name:</label>
				<div class="col-sm-9">
					<form:input path="name" cssClass="form-control" />
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-3 control-label">Short name:</label>
				<div class="col-sm-9">
					<form:input path="shortName" cssClass="form-control" />
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

<table class="table table-bordered table-hover table-striped">
	<thead>
		<tr>
			<th>name</th>
			<th>operations</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${categories}" var="category">
			<tr>
				<td>
					${category.name}
				</td>
				<td>
					<button class="btn btn-danger triggerRemove" id="${category.id}">
						remove
					</button>
					<button class="btn btn-primary triggerEdit" id="${category.id}">
						edit
					</button>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<script type="text/javascript">
$(document).ready(function() {
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
                	var categoryId = origin.attr("id");
					$.post("admin-categories/delete/" + categoryId + ".html", function (data) {
						location.reload(true); // reload page
					});
                	dialog.close();
                }
            }]
	    });
	});
	$(".triggerEdit").click(function(e) {
		$.get("admin-categories/" + $(this).attr("id") + ".json", function (data) {
			$("#name").val(data.name);
			$("#shortName").val(data.shortName);
			$("#id").val(data.id);
		});
		$('#myModal').modal();
	});
	$(".triggerAdd").click(function(e) {
		$("#name").val("");
		$("#shortName").val("");
		$("#id").val("0");
		$('#myModal').modal();
	});
});
</script>