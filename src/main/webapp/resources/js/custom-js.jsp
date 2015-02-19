<%@ page language="java" contentType="text/javascript; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

function itemClick(e) {
	var itemId = $(e.target).attr("id");
	$.post(
			"<spring:url value='/inc-count.html' />", 
			{ itemId: itemId },
			function(data, status) {
			}
	);
}

function itemLike(e) {
	e.preventDefault();
	var itemId = $(e.target).attr("id");
	if($.cookie("like_" + itemId) == "1") {
		unlike(itemId);
	} else if($.cookie("dislike_" + itemId) == "1") {
		undislike(itemId);
		like(itemId);
	} else {
		like(itemId);
	}
}

function itemDislike(e) {
	e.preventDefault();
	var itemId = $(e.target).attr("id");
	if($.cookie("dislike_" + itemId) == "1") {
		undislike(itemId);
	} else if($.cookie("like_" + itemId) == "1") {
		unlike(itemId);
		dislike(itemId);
	} else {
		dislike(itemId);
	}
}

function like(itemId) {
	$.post(
			"<spring:url value='/social/like.html' />", 
			{ itemId: itemId },
			function(data, status) {
				$(".likeCount_" + itemId).text(data);
				$.cookie("like_" + itemId, "1", {expires: 30, path: '/'});
				$.removeCookie("dislike_" + itemId);
				$(".icon_like_" + itemId).removeClass("fa-thumbs-o-up").addClass("fa-thumbs-up");
			}
	);
}

function unlike(itemId) {
	$.post(
			"<spring:url value='/social/unlike.html' />", 
			{ itemId: itemId },
			function(data, status) {
				$(".likeCount_" + itemId).text(data);
				$.removeCookie("like_" + itemId);
				$(".icon_like_" + itemId).removeClass("fa-thumbs-up").addClass("fa-thumbs-o-up");
			}
	);
}

function dislike(itemId) {
	$.post(
			"<spring:url value='/social/dislike.html' />", 
			{ itemId: itemId },
			function(data, status) {
				$(".dislikeCount_" + itemId).text(data);
				$.cookie("dislike_" + itemId, "1", {expires: 30, path: '/'});
				$.removeCookie("like_" + itemId);
				$(".icon_dislike_" + itemId).removeClass("fa-thumbs-o-down").addClass("fa-thumbs-down");
			}
	);
}

function undislike(itemId) {
	$.post(
			"<spring:url value='/social/undislike.html' />", 
			{ itemId: itemId },
			function(data, status) {
				$(".dislikeCount_" + itemId).text(data);
				$.removeCookie("dislike_" + itemId, "1");
				$(".icon_dislike_" + itemId).removeClass("fa-thumbs-down").addClass("fa-thumbs-o-down");
			}
	);
}
