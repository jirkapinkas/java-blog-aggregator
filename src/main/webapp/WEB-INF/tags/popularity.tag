<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ attribute name="popularity" description="blog's popularity" required="true" type="java.lang.Integer" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
	<c:when test="${popularity >= 30}">
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star" style="color:orange;"></i>
	</c:when>
	<c:when test="${popularity >= 20}">
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star-o" style="color:orange;"></i>
	</c:when>
	<c:when test="${popularity >= 15}">
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star-o" style="color:orange;"></i>
		<i class="fa fa-star-o" style="color:orange;"></i>
	</c:when>
	<c:when test="${popularity >= 10}">
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star-o" style="color:orange;"></i>
		<i class="fa fa-star-o" style="color:orange;"></i>
		<i class="fa fa-star-o" style="color:orange;"></i>
	</c:when>
	<c:otherwise>
		<i class="fa fa-star" style="color:orange;"></i>
		<i class="fa fa-star-o" style="color:orange;"></i>
		<i class="fa fa-star-o" style="color:orange;"></i>
		<i class="fa fa-star-o" style="color:orange;"></i>
		<i class="fa fa-star-o" style="color:orange;"></i>
	</c:otherwise>
</c:choose>
