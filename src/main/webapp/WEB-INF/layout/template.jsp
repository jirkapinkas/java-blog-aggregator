<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" autoFlush="false" buffer="512kb"%>
<%-- Do not remove autoFlush="false" buffer="512kb" !!!!!!!! --%>
<%-- It's because of Ziplet - gzip compression --%>
<%-- JSP's out of the box perform auto-flush and it breaks Ziplet --%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>
<head>

<%@ include file="taglib.jsp" %>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" />
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css" />
<link href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" />
<link href="//cdn.datatables.net/1.10.5/css/jquery.dataTables.min.css" rel="stylesheet" />


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta name="viewport" content="width=device-width, initial-scale=1" />

<link rel="icon" href="<spring:url value="/spring/favicon" />" />
<link rel="apple-touch-icon" href="<spring:url value="/spring/appleTouchIcon" />" />

<title>${configuration.title}: <tiles:insertAttribute name="title" /></title>

<link rel="alternate" type="application/rss+xml" title="News feed" href="<spring:url value='/news/feed.xml' />" />

<!-- Begin Cookie Consent plugin by Silktide -->
<script type="text/javascript">
    window.cookieconsent_options = {"message":"This website uses cookies to ensure you get the best experience on our website","dismiss":"Got it!","learnMore":"More info","link":null,"theme":"dark-bottom"};
</script>
<script type="text/javascript" src="<spring:url value="/resources/js/cookieconsent.min.js" />"></script>
<!-- End Cookie Consent plugin -->

</head>
<body>

${configuration.googleAnalytics}

<%@ taglib uri="http://tiles.apache.org/tags-tiles-extras" prefix="tilesx" %>

<tilesx:useAttribute name="current"/>

  <!-- Static navbar -->
      <div class="navbar navbar-default navbar-static-top" role="navigation">
        <div class="container">
          <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" style="padding:10px" href="<spring:url value='/' />">
            	<img src="<spring:url value="/spring/icon" />" alt="logo" />
            	${configuration.brandName}
            </a>
          </div>
          <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
              <li class="${current == 'index' ? 'active' : ''}"><a href='<spring:url value="/" />'>Latest</a></li>

              <li class="dropdown ${current == 'top-views' ? 'active' : ''}">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Top <span class="caret"></span></a>
                <ul class="dropdown-menu" role="menu">
	              <li class="${current == 'top-views' && maxValue == 'week' ? 'active' : ''}"><a href='<spring:url value="/index.html?top-views&max=week" />'>Top this week</a></li>
	              <li class="${current == 'top-views' && maxValue == 'month' ? 'active' : ''}"><a href='<spring:url value="/index.html?top-views&max=month" />'>Top this month</a></li>
                </ul>
              </li>

              <security:authorize access="hasRole('ROLE_ADMIN')">
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Administer <span class="caret"></span></a>
                  <ul class="dropdown-menu" role="menu">
	              	<li class="${current == 'users' ? 'active' : ''}"><a href="<spring:url value="/users.html" />">Users</a></li>
	              	<li class="${current == 'configuration' ? 'active' : ''}"><a href="<spring:url value="/configuration.html" />">Configuration</a></li>
	              	<li class="${current == 'admin-detail' ? 'active' : ''}"><a href="<spring:url value="/admin-detail.html" />">Admin detail</a></li>
	              	<li class="${current == 'admin-categories' ? 'active' : ''}"><a href="<spring:url value="/admin-categories.html" />">Categories</a></li>
	                <li class="${current == 'news-form' ? 'active' : ''}"><a href="<spring:url value="/admin-news/add.html" />">Add news</a></li>
              	  </ul>
              	</li>
              </security:authorize>
              <li class="${current == 'blogs' ? 'active' : ''}"><a href="<spring:url value="/blogs.html" />">Blogs</a></li>
	          <li class="${current == 'news' ? 'active' : ''}"><a href="<spring:url value="/news.html" />">News</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
              <security:authorize access="! isAuthenticated()">
	              <li class="${current == 'login' ? 'active' : ''}"><a href="<spring:url value="/login.html" />">Login</a></li>
	              <li class="${current == 'register' ? 'active' : ''}"><a href="<spring:url value="/register.html" />">Register</a></li>
              </security:authorize>
              <security:authorize access="isAuthenticated()">
              	<li class="${current == 'account' ? 'active' : ''}"><a href="<spring:url value="/account.html" />">My account</a></li>
              	<li><a href="<spring:url value="/logout" />">Logout ${pageContext.request.remoteUser}</a></li>
              </security:authorize>
            </ul>
          </div><!--/.nav-collapse -->
        </div><!--/.container-fluid -->
      </div>

	<div class="container">

	<tiles:insertAttribute name="body" />

	<br>
	<br>
	<div style="text-align: center">
		<tiles:insertAttribute name="footer" />
	</div>

</div>

</body>
</html>