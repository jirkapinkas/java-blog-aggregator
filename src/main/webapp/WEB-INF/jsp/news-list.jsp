<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/taglib.jsp"%>

<h1 style="font-size: 25px">
	<a href="news/feed.xml" class="fa fa-rss fa-lg"  style="float:left;padding-right:5px;padding-top:8px;color:#333333;font-size:20px"></a>
	Latest news from this website:
</h1>

<jsp:include page="../layout/adsense.jsp" />

<c:forEach items="${newsPage.content}" var="news">
	<h3>
		<c:if test="${isAdmin}">
			<a href="admin-news/edit/${news.shortName}.html" class="btn btn-small btn-primary">edit</a>
			<button onclick="removeNews(this)" id="${news.id}" class="btn btn-danger btn-small">delete</button>
		</c:if>
		<a href="news/${news.shortName}.html">${news.title}</a>
	</h3>
	<div style="padding-bottom:10px;color:grey"><fmt:formatDate value="${news.publishedDate}" pattern="dd.MM.yyyy HH:mm:ss" /></div>
	${news.shortDescription}
	
	<br /><hr />
	
</c:forEach>

<c:if test="${currPage < (newsPage.totalPages - 1)}">
	<a href="?page=${currPage + 1}">Older items</a>
</c:if>

<security:authorize access="${isAdmin}">
<script type="text/javascript">
function removeNews(e) {
	var origin = $(e);
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
            	var newsId = origin.attr("id");
			$.post("admin-news/delete/" + newsId + ".html", function (data) {
				location.reload(true); // reload page
			});
            	dialog.close();
            }
        }]
    });
}
</script>
</security:authorize>