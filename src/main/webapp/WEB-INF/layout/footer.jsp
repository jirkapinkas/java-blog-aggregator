<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

	<style type="text/css">
		/* adBlockMessage */
		.adBlockMessage {position: fixed; bottom: 0; left: 0; right: 0; background: #fffd59; color: #222; font-size: 16px; padding: 2em 1em; z-index: 2010; box-shadow: 0 -1px 29px rgba(9,0,0,.78); font-weight: bold; text-align: center; }
		.adBlockMessage a {color: #222; text-decoration: none; padding: 0.6em 1em; margin-left: 1em; border: 2px solid #dad91a; background-color: #fff; }
		.adBlockMessage a:hover {border-color: #222; }
	</style>

	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	<%@ taglib uri="http://granule.com/tags" prefix="granule" %>

	<script type="text/javascript" src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script>
	<script type="text/javascript" src="//cdn.datatables.net/1.10.5/js/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<granule:compress>
		<script type="text/javascript" src="<spring:url value='/resources/js/jquery.unveil.js' />"></script>
		<script type="text/javascript" src="<spring:url value='/resources/js/jquery.cookie.js' />"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/ads.js" />"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap-dialog.min.js" />"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/custom.js" />"></script>
	</granule:compress>
	<granule:compress>
		<link rel="stylesheet" href="<spring:url value='/resources/css/bootstrap-dialog.min.css' />" />
		<link rel="stylesheet" href="<spring:url value='/resources/css/custom.css' />" />
	</granule:compress>

	<div id="footer"></div>
	<script type="text/javascript">
		if( window.canRunAds === undefined ){
			// adblocker detected, show fallback
			document.write("<div class='adBlockMessage'><p>Please don't block advertisement. It's the only revenue we have and it keeps this website up and running (and free). Thank you.</p></div>");
			document.write("<style type='text/css'>#footer { margin-bottom: 80px !important}</style>");
		}
	</script>
	<noscript>
		<div class="adBlockMessage">
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