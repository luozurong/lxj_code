/*var id = "151747376476a7cc64cb4c8d4f458ada";
var ctmsHost = ctmsHost;
var token = "1517532880692b2a53252c924e4681a8"; 
var time_stamp = new Date().getTime();
var type = 2;
*/
var id = GetURLParameter("id");
var token = GetURLParameter("token");
var time_stamp = new Date().getTime();
var type = GetURLParameter("type");

var dataValueLength = "";
var restoredFlag = false;
var restoredAccount = "";
var restoredId = "";
var backWord = ""; //回复文字;
var talkForbidStatus = 0;
var u = navigator.userAgent;
var isIOS = u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端

//评论列表
function commentList(syncState) {
	setTitle("评论");
	var paramsClick = {
		header: {
			time_stamp: time_stamp,
			token: token
		},
		body: {
			id: id,
			type: parseInt(type)
		}
	}
	var param = JSON.stringify(paramsClick);
	$.ajax({
		type: 'GET',
		url: ctmsHost + "/ctmsApi/subjectBack/subjectBackList?str=" + param,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			console.log(data);
			talkForbidStatus = data.talkForbidStatus;
			if(talkForbidStatus == 1) {
				$(".commentInput").attr("placeholder", "您已经被禁言...");
				$(".commentInputDiv").text("您已经被禁言...");
				$(".commentInput").attr("readOnly", "readOnly");
			}
			$(".comment-wrap-all").html("");
			$(".comment-num p span").text(data.list.length);
			if(syncState && sessionStorage.getItem("lxjversionsName") >= 5100) {
				var jsonData = {
					type: type == 2 ? 7 : 8,
					id: id,
					info: data.list.length //评论数
				};
				var jsonData2 = JSON.stringify(jsonData);
				nativeMethod("syncDataState", jsonData2);
			}
			if(data.list.length != 0) {
				for(var i = 0; i < data.list.length; i++) {
					var content = data.list[i].content;
					if(content.length > 500) {
						data.list[i].content = content.substring(0, 500) + "...";
					} else if(content.length == 500) {
						data.list[i].content = data.list[i].content;
					}
					data.list[i].content = htmlEncodeJQ(data.list[i].content);
				}
				var tEmpty = _.template($("#commentItem").html());
				$('.comment-wrap-all').append(tEmpty({
					"data": data.list
				}));

				if($(".comment-item").length > 0) {
					$(".comment-over").show();
				}
			} else {
				var tEmpty = _.template($("#special_comment_empty").html());
				$('.comment-wrap-all').append(tEmpty("暂时还没有评论，快点来抢沙发！"));
				$(".comment-over").hide();
			}
		}
	});
}
commentList();

$('.comment-fixed-left div').bind('keydown', function(e) {
	var key = e.which;
	if(key == 13 && $('.comment-fixed-left div').text("") != "") {
		e.preventDefault();
		commentSubmit();
		$('.comment-fixed-left div').text("");
	}

});

function commentClik(e) {
	if(talkForbidStatus == 1) {
		lxjTip.msg("您已经被禁言");
		return false;
	}
	restoredFlag = true;
	restoredId = $(e).attr("data-val");
	restoredNick = $(e).attr("data-ids");
	restoredAccount = $(e).attr("data-value");
	dataValueLength = restoredNick.length + 3;
	backWord = "回复:" + restoredNick + ":";
	$(".comment-fixed-left input").val("");
	$(".comment-fixed-left input").val(backWord);
	$(".comment-fixed-left input").focus();

	//回复别人评论时传值给原生输入框
	if(isIOS) {
		callNativeMethod("showNativeTextField", {
			"userName": backWord
		});
	}
}

$(".comment-fixed-left input").bind('input propertychange', function() {
	if(restoredFlag) {
		if($(".comment-fixed-left input").val().trim() == "" || $(".comment-fixed-left input").val().trim().length == dataValueLength + 1) {
			$(".comment-fixed-right").removeClass("comment-fixed-active");
		} else {
			$(".comment-fixed-right").addClass("comment-fixed-active");
		}
	} else {
		if($(".comment-fixed-left input").val().trim() == "") {
			$(".comment-fixed-right").removeClass("comment-fixed-active");
		} else {
			$(".comment-fixed-right").addClass("comment-fixed-active");
		}
	}

	if(restoredFlag && $(".comment-fixed-left input").val().trim().length == dataValueLength) {
		$(".comment-fixed-left input").val("");
		restoredFlag = false;
		restoredAccount = "";
		$(".comment-fixed-right").removeClass("comment-fixed-active");
	}
});

//调ios调出输入框
if(isIOS && isCondition(interlxjversionsName) && interlxjversionsName >= 4008) {
	$("#comment_fixed_ios").show();
} else {
	$("#comment_fixed_andrio").show();
}

//直接评论调出软键盘
function keyPop(pop) {
	var popText = $(pop).text();
	if(talkForbidStatus == 1) {
		return false;
	}
	if($(pop).text() == "说点什么吧...") {
		callNativeMethod("showNativeTextField", {
			"userName": ""
		});

	} else {
		callNativeMethod("showNativeTextField", {
			"userName": popText
		});
	}

}

function commentSendIOS(json) {
	commentButtonSendAjax(json);
}

function commentButtonSend() {
	var commentInputWord = $(".commentInput").text();
	if(commentInputWord == "说点什么吧..." && commentInputWord == "") {
		lxjTip("评论不能为空");
	} else {
		commentButtonSendAjax(commentInputWord);
	}

}

function commentButtonSendAjax(json) {
	if(json.length == 0 || json == "说点什么吧...") {
		lxjTip.msg("评论不能为空");
		return false;
	}
	var content = "";
	var backType = "";
	if(json.substring(0, dataValueLength + 1) == backWord) {
		content = json.substring(dataValueLength + 1, json.length);
		backType = 2;
	} else {
		content = json;
		restoredId = "";
		restoredAccount: "";
		backType = 1;
	}

	var param = {
		body: {
			parentId: id,
			type: parseInt(type),
			content: content,
			restoredId: restoredId,
			restoredAccount: restoredAccount,
			backType: backType
		},
		header: {
			token: token,
			time_stamp: time_stamp
		}
	}
	var paramString = JSON.stringify(param);
	commentSubmitAjax(paramString);
}

function IOSCommentText(text) {
	if(text.length == 0 || text == "说点什么吧...") {
		$(".commentInputDiv").removeClass("comment-color-active");
		$(".commentInputDiv").text("说点什么吧...");
		$(".comment-fixed-right").removeClass("comment-fixed-active");

	} else {
		$(".commentInputDiv").addClass("comment-color-active");
		$(".commentInputDiv").text(text);
		$(".comment-fixed-right").addClass("comment-fixed-active");

	}
}

//提交评论
function commentSubmit() {
	if($(".comment-fixed-right").hasClass("comment-fixed-active")) {
		var content = "";
		var backType = "";
		if(restoredFlag) {
			content = $(".comment-fixed-left input").val().substr(dataValueLength + 1, $(".comment-fixed-left input").val().length);
			backType = 2;
		} else {
			content = $(".comment-fixed-left input").val();
			restoredId = "";
			restoredAccount: "";
			backType = 1;
		}
		var param = {
			body: {
				parentId: id,
				type: parseInt(type),
				content: content,
				restoredId: restoredId,
				restoredAccount: restoredAccount,
				backType: backType
			},
			header: {
				token: token,
				time_stamp: time_stamp
			}
		}
		var paramString = JSON.stringify(param);
		commentSubmitAjax(paramString);

	}
}

function commentSubmitAjax(paramString) {
	$.ajax({
		type: 'GET',
		url: ctmsHost + "/ctmsApi/subjectBack/addSubjectBack?str=" + paramString,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			if(data.result == 0) {
				$(".comment-fixed-left input").val("");
				$(".commentInputDiv").text("说点什么吧...");
				$(".comment-fixed-right").removeClass("comment-fixed-active");
				$(".commentInputDiv").removeClass("comment-color-active");
				if(type == 2) {
					commentList(true);
				} else {
					commentList(true);
				}

			} else {
				lxjTip.msg(data.reason);
			}

		}
	});
}
//删除评论 
function deleteComment(backId, parentId) {
	lxjTip.confirm('确定删除该评论?', {
		skin: 'demo4',
		btn: ['删除', '取消'],
		yes: function(index) {

			lxjTip.close(); //如果设定了yes回调，需进行手工关闭

			var params = {};
			params.header = {
				token: token,
				time_stamp: time_stamp
			};
			params.body = {
				id: backId,
				type: parseInt(type),
				parentId: id
			}

			var paramData = JSON.stringify(params);
			var reqUrl = '/ctmsApi/subjectBack/deleteSubjectBack';
			$.ajax({
				type: 'GET',
				url: ctmsHost + reqUrl + '?str=' + paramData,
				async: false,
				dataType: 'jsonp',
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback",
				success: function(data) {
					if(data.result == 0) {

						$("#" + backId).parent().remove();
						var num = $(".comment-num p span").text();
						$(".comment-num p span").text(parseInt(num) - 1);
						if($(".comment-wrap-all>div").length == 0) {
							$(".comment-wrap-all").html("");
							var tEmpty = _.template($("#special_comment_empty").html());
							$(".comment-wrap-all").append(tEmpty("暂时还没有评论，快点来抢沙发！"));
							$(".comment-over").hide();
						}
						if(sessionStorage.getItem("lxjversionsName") >= 5100) {
							var numsD=parseInt(num) - 1;
							var jsonData = {
								type: type == 2 ? 7 : 8,
								id: id,
								info:numsD //评论数
							};
							var jsonData2 = JSON.stringify(jsonData);
							nativeMethod("syncDataState", jsonData2);
						}

					} else {
						lxjTip.alert(data.reason, {
							yes: function() {
								lxjTip.close(); //如果设定了yes回调，需进行手工关闭
							}
						});
					}
				}

			})
		}
	});
}

//光标移到最后
function setFocus(el) {
	el = el[0]; // jquery 对象转dom对象  
	el.focus();
	var range = document.createRange();
	range.selectNodeContents(el);
	range.collapse(false);
	var sel = window.getSelection();
	//判断光标位置，如不需要可删除
	if(sel.anchorOffset != 0) {
		return;
	};
	sel.removeAllRanges();
	sel.addRange(range);
};

//通过点击头像跳转
function goToUser(ele) {
	window.event.cancelBubble = true;
	var userAccount = String($(ele).attr("useraccount"));
	//调用原生方法
	var params = {
		"userAccount": userAccount.toString()
	};
	var paramData = JSON.stringify(params);
	nativeMethod("viewUserInfo", paramData);
}

//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}

function isCondition(param) {
	if(param != null || param != "" || param != undefined || param != "null") {
		return true;
	}
	return false;
}

//转义
function htmlEncodeJQ(str) {
	return $('<span/>').text(str).html();
}

/*var u = navigator.userAgent;
var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
if(isCondition(interlxjversionsName) && interlxjversionsName>= 4008 && isiOS) {
	callNativeMethod('disableNativeScorll');
}*/