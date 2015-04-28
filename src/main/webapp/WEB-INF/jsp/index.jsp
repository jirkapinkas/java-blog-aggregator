<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<c:choose>
	<c:when test="${blogDetail eq true}">
		<table style="width:100%">
			<tr>
				<td style="width:42px">
					<a href="${blog.homepageUrl}" class="fa fa-home fa-lg" style="float:left;padding-top:14px;color:#333333">
					</a>
					<a href="${blog.url}" class="fa fa-rss fa-lg"  style="float:left;padding-top:14px;padding-left:5px;color:#333333">
					</a>
				</td>
				<td>
					<h1 style="font-size:25px">${title}</h1>
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<h1 style="font-size:25px">${title}</h1>
	</c:otherwise>
</c:choose>

<jsp:include page="../layout/adsense.jsp" />

<br />

<table class="table table-bordered table-hover table-striped tableItems">
	<tbody>
		<tr>
			<td>
				<span class="label label-default">${blogCount} blogs</span>
				<span class="label label-default">last update was ${lastIndexDate} minutes ago</span>
				<security:authorize access="${isAdmin}">
					<span class="label label-default">items: ${itemCount}</span>
					<span class="label label-default">users: ${userCount}</span>
				</security:authorize>
				<c:if test="${blogDetail eq null}">
					<c:forEach items="${categories}" var="category">
						<span class="label label-primary categoryLabel withTooltip" id="${category.id}" style="cursor: pointer;" data-toggle="tooltip" data-placement="top" title="toggle category visibility">${category.name} (${category.blogCount})</span>
					</c:forEach>
				</c:if>
				<script type="text/javascript">
					$(document).ready(function() {
						$('.withTooltip').tooltip();
						// TODO save settings to cookie
						// select all categories
						$.getJSON("<spring:url value='/all-categories.json' />", function(data) {
							selectedCategories = data;
						});
						$(".categoryLabel").click(function (e) {
							var categoryId = parseInt($(this).attr("id"));
							var arrIndex = $.inArray(categoryId, selectedCategories);
							if(arrIndex != -1) {
								selectedCategories.splice(arrIndex, 1);
								$(this).css("text-decoration", "line-through");
							} else {
								selectedCategories.push(categoryId);
								$(this).css("text-decoration", "none");
							}
							// reload first page
							$(".tableItems tbody .item-row").remove();
							currentPage = -1;
							loadNextPage();
						});
					});
				</script>
			</td> 
		</tr>
		<c:forEach items="${items}" var="item">
			<tr class="item-row">
				<c:choose>
					<c:when test="${item.enabled}">
						<c:set var="customCss" value="" />
					</c:when>
					<c:otherwise>
						<c:set var="customCss" value="text-decoration: line-through;color:grey" />
					</c:otherwise>
				</c:choose>
				<td>
				
					
					<table style="float:left;margin-right:5px">
						<tr>
							<td style="padding:2px">
								<i style="color:#6273a9;cursor:pointer;" 
								   class="fa fa-thumbs-o-up icon_like_${item.id}" 
								   id="${item.id}" 
								   onClick="itemLike(event)"
								   title="like"></i>
							</td>
							<td style="padding:2px">
								<span class="likeCount_${item.id}">${item.displayLikeCount}</span>
							</td>
						</tr>
						<tr>
							<td style="padding:2px">
								<i style="color:#6273a9;cursor:pointer;" 
								   class="fa fa-thumbs-o-down icon_dislike_${item.id}" 
								   id="${item.id}" 
								   onClick="itemDislike(event)"
								   title="dislike"></i>
							</td>
							<td style="padding:2px">
								<span class="dislikeCount_${item.id}">${item.dislikeCount}</span>
							</td>
						</tr>
					</table>
				
					<script type="text/javascript">
						$(document).ready(function() {
							showCurrentState("${item.id}");
							$("img.lazy").unveil(unveilTreshold);
						});
					</script>

						<a id="${item.id}" href="<c:out value="${item.link}" />" style="${customCss}" class="itemLink" onClick="itemClick(event)" target="_blank">
							<img class="lazy" id="${item.id}" data-src="<spring:url value='/spring/icon/${item.blog.id}' />" style="float:left;padding-right:5px" />
							<strong id="${item.id}">${item.title} <span class="glyphicon glyphicon-share-alt"></span></strong></a>
					<br />
					<span style="${customCss}" class="itemDesc">${item.description}</span>
					<br /><br />
					<c:if test="${item.publishedDate > yesterdayDate}">
						<i class="fa fa-plus" title="today"></i>
					</c:if>
					<span class="label" style="color: grey;"><fmt:formatDate value="${item.publishedDate}" pattern="dd-MM-yyyy hh:mm:ss" /></span>
					<span class="label label-info"><a href="<spring:url value='/blog/${item.blog.shortName}.html' />" style="color: white"><c:out value="${item.blog.name}" /></a></span>
					<c:if test="${item.blog.category.shortName != null}">
						<span class="label label-default"><c:out value="${item.blog.category.name}" /></span>
					</c:if>
					<security:authorize access="${isAdmin}">
						<span class="label label-default"><i class='fa fa-eye'></i> ${item.clickCount}</span>
						<span class="label label-default"><i class='fa fa-thumbs-up'></i> ${item.likeCount}</span>
						<span class="label label-default"><i class='fa fa-twitter'></i> ${item.twitterRetweetCount}</span>
						<span class="label label-default"><i class='fa fa-facebook'></i> ${item.facebookShareCount}</span>
						<span class="label label-default"><i class='fa fa-linkedin'></i> ${item.linkedinShareCount}</span>
						<a href="<spring:url value="/items/toggle-enabled/${item.id}.html" />" class="btn btn-primary btn-xs btnToggleEnabled" style="margin-left:5px" onclick="event.preventDefault();toggleEnabledItem(this);">
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
					<%-- Static URL for those, who doesn't have javascript turned on. --%>
					<c:choose>
						<c:when test="${blogDetail eq true}">
							<c:set var="noscriptNextPageUrl" value="?page=${nextPage}&shortName=${blogShortName}" />
						</c:when>
						<c:when test="${topViews eq true}">
							<c:choose>
								<c:when test="${max eq true}">
									<c:set var="noscriptNextPageUrl" value="?page=${nextPage}&top-views&max=${maxValue}" />
								</c:when>
								<c:otherwise>
									<c:set var="noscriptNextPageUrl" value="?page=${nextPage}&top-views" />
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:set var="noscriptNextPageUrl" value="?page=${nextPage}" />
						</c:otherwise>
					</c:choose>
					<strong><a href="<spring:url value='' />${noscriptNextPageUrl}" class="loadButton">load next 10 items</a></strong>
				</div>
			</td>
		</tr>
	</tbody>
</table>

<c:choose>
	<c:when test="${topViews eq true}">
		<script>
			var topViews = true;
		</script>
	</c:when>
	<c:otherwise>
		<script>
			var topViews = false;
		</script>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${max eq true}">
		<script>
			var max = true;
			var maxValue = "${maxValue}";
		</script>
	</c:when>
	<c:otherwise>
		<script>
			var max = false;
		</script>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${blogDetail eq true}">
		<script>
			var blogDetail = true;
			var blogShortName = "${blogShortName}";
		</script>
	</c:when>
	<c:otherwise>
		<script>
			var blogDetail = false;
		</script>
	</c:otherwise>
</c:choose>

<script>

	$(function() {
		$(".loadButton").click(loadNextPage);
	});

	function loadNextPage(e) {
		if(e != null) {
			e.preventDefault();
		}
		startRefresh();
		var nextPage = currentPage + 1;
		var url = "<spring:url value='/page/' />" + nextPage + ".json";
		var iconBaseUrl = "<spring:url value='/spring/icon/' />";
		var blogDetailBaseUrl = "<spring:url value='/blog/' />";
		if(topViews == true) {
			url = url + "?topviews=true";
			if(max == true) {
				url = url + "&max=" + maxValue;
			}
		} else if(blogDetail == true) {
			url = url + "?shortName=" + blogShortName;
		}
		if(url.indexOf("?") == -1) {
			url = url + "?selectedCategories=" + selectedCategories.join(',');
		} else {
			url = url + "&selectedCategories=" + selectedCategories.join(',');
		}


		$.getJSON( url, function( data ) {
			var html = "";
			$.each(data, function(key, value) {
				html += "<tr class='item-row'><td>";

				// show like / dislike buttons
				html += ' <table style="float:left;margin-right:5px">';
				html += ' <tr>';
				html += ' <td style="padding:2px">';
				html += ' <i style="color:#6273a9;cursor:pointer;" ';
				html += ' class="fa fa-thumbs-o-up icon_like_' + value.id + '" ';
				html += ' id="' + value.id + '" ';
				html += ' onClick="itemLike(event)" title="like"></i>';
				html += ' </td>';

				html += ' <td style="padding:2px">';
				html += ' <span class="likeCount_' + value.id + '">' + value.displayLikeCount + '</span>';
				html += ' </td>';

				html += ' </tr>';
				html += ' <tr>';
				html += ' <td style="padding:2px">';
				html += ' <i style="color:#6273a9;cursor:pointer;" ';
				html += ' class="fa fa-thumbs-o-down icon_dislike_' + value.id + '" ';
				html += ' id="' + value.id + '" ';
				html += ' onClick="itemDislike(event)" title="dislike"></i>';
				html += ' </td>';

				html += ' <td style="padding:2px">';
				html += ' <span class="dislikeCount_' + value.id + '">' + value.dislikeCount + '</span>';
				html += ' </td>';

				html += ' </tr>';
				html += ' </table>';

				var css = "";
				if(value.enabled == false) {
					css = "text-decoration: line-through;color:grey";
				}
				html += "<a href='" + value.link + "' class='itemLink' style='" + css + "' onClick='itemClick(event)' id='" + value.id + "' target='_blank'>";
				html += "<img class='lazy' data-src='" + iconBaseUrl + value.blog.id + "' alt='icon' style='float:left;padding-right:10px' id='" + value.id + "' />";
				html += "<strong id='" + value.id + "'>";
				html += value.title;
				html += " <span class='glyphicon glyphicon-share-alt'></span>";
				html += "</strong>";
				html += "</a>";
				html += "<br />";
				html += "<span class='itemDesc' style='" + css + "'>";
				html += value.description;
				html += "</span>";
				html += "<br />";
				html += "<br />";
				var date = new Date(value.publishedDate);
				if(date.getTime() > "${yesterdayDate.time}") {
					html += '<i class="fa fa-plus" title="today"></i> ';
				}

				html += "<span class='label' style='color: grey;'>";
				html += ("0" + date.getDate()).slice(-2) + "-" + ("0" + (date.getMonth() + 1)).slice(-2) + "-" + date.getFullYear();
				html += " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2) + ":" + ("0" + date.getSeconds()).slice(-2);
				html += "</span>";
				html += "<span class='label label-info' style='margin-left: 5px'><a href='" + blogDetailBaseUrl + value.blog.shortName + ".html' style='color: white;'>";
				html += value.blog.name;
				html += "</a></span>";
				if(value.blog.category != null) {
					html += ' <span class="label label-default" style="margin-left: 5px">' + value.blog.category.name + '</span>';
				}
				html += adminMenu(value);
				html += "</td></tr>";
			});
			var newCode = $(".table tr:last").prev().after(html);
			$("img.lazy").unveil(unveilTreshold);
			// set like / dislike buttons state
			$.each(data, function(key, value) {
				showCurrentState(value.id);
			});
			finishRefresh();
		});
		currentPage++;
	}
	
</script>

<c:choose>
	<c:when test="${isAdmin eq true}">
		<script type="text/javascript">
		
			// generate menu for administrator
			function adminMenu(item) {
				var html = "";
				html += ' <span class="label label-default" style="margin-left: 5px">views: ' + item.clickCount + '</span> ';
				html += '<a href="<spring:url value="/" />items/toggle-enabled/' + item.id + '.html" class="btn btn-primary btn-xs btnToggleEnabled" onclick="event.preventDefault();toggleEnabledItem(this);">';
				if(item.enabled) {
					html += 'disable';
				} else {
					html += 'enable';
				}
				html += '</a>';
				return html;
			}

			function toggleEnabledItem (e) {
				var href = $(e).attr("href");
				var curr = $(e);
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

		</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			function adminMenu(item) {
				return "";
			}
		</script>
	</c:otherwise>
</c:choose>

<jsp:include page="../layout/adsense.jsp" />
