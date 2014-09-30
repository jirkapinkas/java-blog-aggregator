<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>
<head>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" />

<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css" />

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<script type="text/javascript" 
		src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script>

<script
	src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta name="viewport" content="width=device-width, initial-scale=1" />

<meta name="google-site-verification" content="_BaqlJSZEUBFZuhDCT7wR01BdgLQzmafK_2hf1Q4e7M" />

<link rel="icon" href="<spring:url value="/favicon.ico" />" />

<title><tiles:getAsString name="title" /></title>

</head>
<body>

<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-49851353-1', 'topjavablogs.com');
  ga('send', 'pageview');

</script>

<%@ taglib uri="http://tiles.apache.org/tags-tiles-extras" prefix="tilesx" %>

<tilesx:useAttribute name="current"/>

<div class="container">

  <!-- Static navbar -->
      <div class="navbar navbar-default" role="navigation">
        <div class="container-fluid">
          <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" style="padding:10px" href="<spring:url value="/" />">
            	<img src="<spring:url value="/resources/images/logo.png" />" alt="logo" />
            </a>
          </div>
          <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
              <li class="${current == 'index' ? 'active' : ''}"><a href='<spring:url value="/" />'>Latest</a></li>
              <li class="${current == 'top-views' && maxValue == 'week' ? 'active' : ''}"><a href='<spring:url value="/index.html?top-views&max=week" />'>Top this week</a></li>
              <li class="${current == 'top-views' && maxValue == 'month' ? 'active' : ''}"><a href='<spring:url value="/index.html?top-views&max=month" />'>Top this month</a></li>
              <li class="${current == 'top-views' && maxValue == null ? 'active' : ''}"><a href='<spring:url value="/index.html?top-views" />'>Top all time</a></li>
              <security:authorize access="hasRole('ROLE_ADMIN')">
              	<li class="${current == 'users' ? 'active' : ''}"><a href="<spring:url value="/users.html" />">Users</a></li>
              </security:authorize>
              <li class="${current == 'roadmap' ? 'active' : ''}"><a href="<spring:url value="/roadmap.html" />">Roadmap</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
              <security:authorize access="! isAuthenticated()">
	              <li class="${current == 'register' ? 'active' : ''}"><a href="<spring:url value="/register.html" />">Register</a></li>
	              <li class="${current == 'login' ? 'active' : ''}"><a href="<spring:url value="/login.html" />">Login</a></li>
              </security:authorize>
              <security:authorize access="isAuthenticated()">
              	<li class="${current == 'account' ? 'active' : ''}"><a href="<spring:url value="/account.html" />">My account</a></li>
              	<li><a href="<spring:url value="/logout" />">Logout ${pageContext.request.remoteUser}</a></li>
              </security:authorize>
            </ul>
          </div><!--/.nav-collapse -->
        </div><!--/.container-fluid -->
      </div>

	<tiles:insertAttribute name="body" />

	<br>
	<br>
	<div style="text-align: center">
		<tiles:insertAttribute name="footer" />
	</div>

</div>

</body>
</html>