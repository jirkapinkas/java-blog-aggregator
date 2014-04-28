<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<h1>Latest news from the Java world:</h1>

<jsp:include page="../layout/adsense.jsp" />

<table class="table table-bordered table-hover table-striped">
	<tbody>
		<tr>
			<td>
				<strong>count:</strong>
				<span class="badge">blogs: ${blogCount}</span>
				<span class="badge">last update: ${lastIndexDate} minutes ago</span>
				<security:authorize access="hasRole('ROLE_ADMIN')">
					<span class="badge">items: ${itemCount}</span>
					<span class="badge">users: ${userCount}</span>
				</security:authorize>
			</td> 
		</tr>
		<c:forEach items="${items}" var="item">
			<tr>
				<c:choose>
					<c:when test="${item.enabled}">
						<c:set var="customCss" value="" />
					</c:when>
					<c:otherwise>
						<c:set var="customCss" value="text-decoration: line-through;color:grey" />
					</c:otherwise>
				</c:choose>
				<td>
					<strong>
						<a href="<c:out value="${item.link}" />" target="_blank" style="${customCss}" class="itemLink">
							${item.title} <span class="glyphicon glyphicon-share-alt"></span></a>
					</strong>
					<br />
					<span style="${customCss}" class="itemDesc">${item.description}</span>
					<br /><br />
					<fmt:formatDate value="${item.publishedDate}" pattern="dd-MM-yyyy hh:mm:ss" />:
					<strong>
						<c:out value="${item.blog.name}" />
					</strong>
					<security:authorize access="hasRole('ROLE_ADMIN')">
						<a href="<spring:url value="/items/toggle-enabled/${item.id}.html" />" class="btn btn-primary btn-xs btnToggleEnabled">
							<c:choose>
								<c:when test="${item.enabled}">
									disable
								</c:when>
								<c:otherwise>
									enable
								</c:otherwise>
							</c:choose>
						</a>
					</security:authorize>
				</td>
			</tr>
		</c:forEach>
		<tr class="loadNextRow">
			<td class="loadNextColumn">
				<div style="text-align: center">
					<strong><a href="/?page=${nextPage}" class="loadButton" rel="next">load next 10 items</a></strong>
				</div>
			</td>
		</tr>
	</tbody>
</table>

<script>

	$(document).ready(function() {

		var currentPage = 0;
		$(".loadButton").click(function(e) {
			e.preventDefault();
			var nextPage = currentPage + 1;
			$.getJSON( "<spring:url value='/page/' />" + nextPage + ".json", function( data ) {
				var html = "";
				$.each(data, function(key, value) {
					html += "<tr><td>";
					html += "<strong>";
					var css = "";
					if(value.enabled == false) {
						css = "text-decoration: line-through;color:grey";
					}
					html += "<a href='" + value.link + "' target='_blank' class='itemLink' style='" + css + "'>";
					html += value.title;
					html += "</a>";
					html += "</strong>";
					html += "<br />";
					html += "<span class='itemDesc' style='" + css + "'>";
					html += value.description;
					html += "</span>";
					html += "<br />";
					html += "<br />";
					var date = new Date(value.publishedDate);
					html += ("0" + date.getDate()).slice(-2) + "-" + ("0" + (date.getMonth() + 1)).slice(-2) + "-" + date.getFullYear();
					html += " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2) + ":" + ("0" + date.getSeconds()).slice(-2);
					html += ": ";
					html += "<strong>";
					html += value.blog.name;
					html += "</strong>";
 					html += adminMenu(value);
					html += "</td></tr>";
				});
				var newCode = $(".table tr:last").prev().after(html);
				adminHandler(newCode);
			});
			currentPage++;
		});
		
	});

</script>

<security:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin" />

<c:choose>
	<c:when test="${isAdmin eq true}">
		<script type="text/javascript">
		
			$(document).ready(function() {
				$(".btnToggleEnabled").click(toggleEnabledItem);
			});
		
			// generate menu for administrator
			function adminMenu(item) {
				var html = " ";
				html += '<a href="<spring:url value="/" />items/toggle-enabled/' + item.id + '.html" class="btn btn-primary btn-xs btnToggleEnabled">';
				if(item.enabled) {
					html += 'disable';
				} else {
					html += 'enable';
				}
				html += '</a>';
				return html;
			}

			var toggleEnabledItem = function (e) {
				e.preventDefault();
				var href = $(this).attr("href");
				var curr = $(this);
				$.getJSON( href, function(data) {
					var css1 = "";
					var css2 = "";
					if(data == true) {
						css1 = "";
						css2 = "";
						$(curr).text("disable");
					} else {
						css1 = "line-through";
						css2 = "grey";
						$(curr).text("enable");
					}
					var itemLink = $(curr).closest("tr").find(".itemLink");
					itemLink.css("text-decoration", css1);
					itemLink.css("color", css2);
					var itemDesc = $(curr).closest("tr").find(".itemDesc");
					itemDesc.css("text-decoration", css1);
					itemDesc.css("color", css2);
				});
			};

			function adminHandler(newCode) {
				$(".btnToggleEnabled").on("click", toggleEnabledItem);
			}

		</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			function adminMenu(item) {
				return "";
			}
			function adminHandler(newCode) {
			}
		</script>
	</c:otherwise>
</c:choose>

<jsp:include page="../layout/adsense.jsp" />
