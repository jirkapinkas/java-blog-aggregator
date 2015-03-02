<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp" %>

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
                	var userId = origin.attr("id");
					$.post("users/remove/" + userId + ".html", function (data) {
						location.reload(true); // reload page
					});
                	dialog.close();
                }
            }]
	    });
	});
});
</script>

<table class="table table-bordered table-hover table-striped">
	<thead>
		<tr>
			<th>user name</th>
			<th>operations</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${users}" var="user">
			<tr>
				<td>
					<a href="<spring:url value="/users/${user.id}.html" />">
						<c:out value="${user.name}" />
					</a>
				</td>
				<td>
					<button id="${user.id}" class="btn btn-danger triggerRemove">
						remove
					</button>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
