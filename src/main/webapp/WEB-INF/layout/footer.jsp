<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.lang.RandomStringUtils" %>

    <%
		String adBlockMessageClass = RandomStringUtils.randomAlphabetic(30);
    	pageContext.setAttribute("adBlockMessageClass", adBlockMessageClass);
    %>
    
	<style type="text/css">
		/* adBlockMessage */
		.${adBlockMessageClass} {position: fixed; bottom: 0; left: 0; right: 0; background: #fffd59; color: #222; font-size: 16px; padding: 2em 1em; z-index: 2010; box-shadow: 0 -1px 29px rgba(9,0,0,.78); font-weight: bold; text-align: center; }
		.${adBlockMessageClass} a {color: #222; text-decoration: none; padding: 0.6em 1em; margin-left: 1em; border: 2px solid #dad91a; background-color: #fff; }
		.${adBlockMessageClass} a:hover {border-color: #222; }
	</style>

	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

	<script type="text/javascript" src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script>
	<script type="text/javascript" src="//cdn.datatables.net/1.10.5/js/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

	<% if ("dev".equals(System.getProperty("spring.profiles.active"))) { %>
		<%-- javascript resources & custom css used in development --%>
		<script type="text/javascript" src="<spring:url value='/resources/js/jquery.unveil.js' />"></script>
		<script type="text/javascript" src="<spring:url value='/resources/js/jquery.cookie.js' />"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap-dialog.min.js" />"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/custom.js" />"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/ads.js" />"></script>
		<link rel="stylesheet" href="<spring:url value='/resources/css/bootstrap-dialog.min.css' />" />
		<link rel="stylesheet" href="<spring:url value='/resources/css/custom.css' />" />
	<% } else { %>
		<%-- javascript resources & custom css used in production
		     minified using minify-maven-plugin (configuration is in pom.xml) --%>
		<script type="text/javascript" src="<spring:url value='/resources/js/script.min.js' />"></script>
		<link rel="stylesheet" href="<spring:url value='/resources/css/style.min.css' />" />
		<script type="text/javascript" src="<spring:url value="/resources/js/ads.js" />"></script>
	<% } %>

	<!-- Begin Cookie Consent plugin by Silktide -->
	<script type="text/javascript">
	    window.cookieconsent_options = {"message":"This website uses cookies to ensure you get the best experience on our website","dismiss":"Got it!","learnMore":"More info","link":null,"theme":"dark-bottom"};
	</script>
	<script type="text/javascript" src="<spring:url value="/resources/js/cookieconsent.min.js" />"></script>
	<!-- End Cookie Consent plugin -->

	<div id="footer"></div>
	<script type="text/javascript">
		if( window.canRunAds === undefined ){
			// adblocker detected, show fallback
			document.write("<div class='${adBlockMessageClass}'><p>Please don't block advertisement. It's the only revenue we have and it keeps this website up and running (and free). Thank you.</p></div>");
			document.write("<style type='text/css'>#footer { margin-bottom: 80px !important}</style>");
		}
	</script>
	<noscript>
		<div class="${adBlockMessageClass}">
			<p>
				Please don't block advertisement. It's the only revenue we have and it keeps this website up and running (and free). Thank you.
			</p>
		</div>
		<style type="text/css">
			#footer {
				margin-bottom: 80px !important
			}
		</style>
	</noscript>


${configuration.footer}

<br />
<br />