<%@ page language="java" contentType="text/javascript; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

var unveilTreshold = 200;

function itemClick(e) {
	var itemId = $(e.target).attr("id");
	$.post(
			"<spring:url value='/inc-count.html' />", 
			{ itemId: itemId },
			function(data, status) {
			}
	);
}

/*
 * Called when user clicks on the like button.
 */
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

/*
 * Called when user clicks on the dislike button.
 */
function itemDislike(e) {
	e.preventDefault();
	var itemId = $(e.target).attr("id");
	if($.cookie("dislike_" + itemId) == "1") {
		undislike(itemId);
	} else if($.cookie("like_" + itemId) == "1") {
		unlike(itemId);
		dislike(itemId);
		hide(itemId);
	} else {
		dislike(itemId);
		hide(itemId);
	}
}

function showCurrentState(id) {
	if($.cookie("like_" + id) == "1") {
		$(".icon_like_" + id).removeClass("fa-thumbs-o-up").addClass("fa-thumbs-up");
	}
	if($.cookie("dislike_" + id) == "1") {
		$(".icon_dislike_" + id).removeClass("fa-thumbs-o-down").addClass("fa-thumbs-down");
		hide(id);
	}
}

function hide(id) {
	// TODO
	// $(".icon_dislike_" + id).closest(".item-row").html("<tr><td>show disliked item</td></tr>");
}

function unhide(id) {
}

var arrLikeProgress = [];

function like(itemId) {
	if($.inArray(itemId, arrLikeProgress) !== -1) {
		return;
	}
	arrLikeProgress.push(itemId);
	$.post(
			"<spring:url value='/social/like.html' />", 
			{ itemId: itemId },
			function(data, status) {
				$(".likeCount_" + itemId).text(data);
				$.cookie("like_" + itemId, "1", {expires: 30, path: '/'});
				$.removeCookie("dislike_" + itemId);
				$(".icon_like_" + itemId).removeClass("fa-thumbs-o-up").addClass("fa-thumbs-up");
				arrLikeProgress.splice($.inArray(itemId, arrLikeProgress), 1);
			}
	);
}

function unlike(itemId) {
	if($.inArray(itemId, arrLikeProgress) !== -1) {
		return;
	}
	arrLikeProgress.push(itemId);
	$.post(
			"<spring:url value='/social/unlike.html' />", 
			{ itemId: itemId },
			function(data, status) {
				$(".likeCount_" + itemId).text(data);
				$.removeCookie("like_" + itemId);
				$(".icon_like_" + itemId).removeClass("fa-thumbs-up").addClass("fa-thumbs-o-up");
				arrLikeProgress.splice($.inArray(itemId, arrLikeProgress), 1);
			}
	);
}

function dislike(itemId) {
	if($.inArray(itemId, arrLikeProgress) !== -1) {
		return;
	}
	arrLikeProgress.push(itemId);
	$.post(
			"<spring:url value='/social/dislike.html' />", 
			{ itemId: itemId },
			function(data, status) {
				$(".dislikeCount_" + itemId).text(data);
				$.cookie("dislike_" + itemId, "1", {expires: 30, path: '/'});
				$.removeCookie("like_" + itemId);
				$(".icon_dislike_" + itemId).removeClass("fa-thumbs-o-down").addClass("fa-thumbs-down");
				arrLikeProgress.splice($.inArray(itemId, arrLikeProgress), 1);
			}
	);
}

function undislike(itemId) {
	if($.inArray(itemId, arrLikeProgress) !== -1) {
		return;
	}
	arrLikeProgress.push(itemId);
	$.post(
			"<spring:url value='/social/undislike.html' />", 
			{ itemId: itemId },
			function(data, status) {
				$(".dislikeCount_" + itemId).text(data);
				$.removeCookie("dislike_" + itemId, "1");
				$(".icon_dislike_" + itemId).removeClass("fa-thumbs-down").addClass("fa-thumbs-o-down");
				arrLikeProgress.splice($.inArray(itemId, arrLikeProgress), 1);
			}
	);
}
