var token = GetURLParameter("token");
var time_stamp = new Date().getTime();
var id = GetURLParameter("id") ? GetURLParameter("id") : 'kongid';

var organizationSeq = GetURLParameter("organizationSeq");
var subjectBackId = GetURLParameter("subjectBackId");
var channelType = GetURLParameter("channelType");
var appBannerType = GetURLParameter("appBannerType") == undefined ? "" : GetURLParameter("appBannerType");
var otherChannelId = GetURLParameter("otherChannelId") == undefined ? "" : GetURLParameter("otherChannelId");
var communityId = GetURLParameter("communityId") == undefined ? "" : GetURLParameter("communityId");
var communityName = GetURLParameter("communityName") == undefined ? "" : GetURLParameter("communityName");
var createTime2 = new Date().getTime();
var createTime1 = creatTimes();
var lxjversionsNames = sessionStorage.getItem("lxjversionsName");
if(id.indexOf('?') > 0) {
	var idchannelType = id.split('?');
	var idchannelType2 = id.split('=');
	id = idchannelType[0];
	channelType = idchannelType2[1];
}

/*var ctmsHost = ctmsHost;
var token = "152704418690691ed18808b544588d6c";
var id = "15270441272542c2064653be415fbd97";
var organizationSeq = "4400100001";*/
/*var subjectBackId = "";
var time_stamp = new Date().getTime();
var channelType = "3";
var appBannerType = "";
var communityId = "454454";
var communityName = "水电费三分";
var otherChannelId = "";*/

var backAccount = "";
var backName = "";
var title = "";
var subTitle = "";
var indexPic = "";
var fullFlag = true;
var focusFlag = GetURLParameter("focusFlag");
var visitor = 0;
var userAccount;
var page = 1;
var pageFalse = true;
var collectClick = 0;
var collectClickFlag = true;
var upClick = 0;
var upClickFlag = true;
var collectOnce = true;
var upOnce = true;
var authorId = ''; //作者id
var followType = ''; //用户关注作者状态
var collectIconNor = "images/btn_ic_collect_nor_xxhdpi.png";
var collectIconPre = "images/btn_ic_collect_pre_xxhdpi.png";
var upTypePre = "images/ic_like_pre_xxhdpi.png";
var upTypeNor = "images/ic_like_nor_xxhdpi.png";

ajaxData();

//刷新
function refreshData() {
	setTimeout(function() {
		ajaxData();
		createTime1 = getTime(); //页面访问时间
		createTime2 = new Date().getTime();
		accessDetailRemainTimeState = false;
	}, 0);
	return 1;
}

//返回顶部
var windowHeight = $(window).height();
$(window).scroll(function() {
	if($(window).scrollTop() >= 3 * windowHeight) {
		$(".top-fixed").slideDown(100);
	} else {
		$(".top-fixed").slideUp(100);
	}
});

$(".top-fixed").click(function() {
	$("html,body").animate({
		scrollTop: 0
	}, 500);
});

function ccp() {
	var subjectLength = $(".subjectBack").length;
	if(subjectLength < 10) {
		$(".special-load-more").text("我是有底线的");
		$(".special-load-more").show();
	}
}

$(".top").click(function() {
	$(".special-header").animate({
		scrollTop: 0
	}, 300);
});

/**
 * [ajaxData] 专题详情、评论数据请求
 * @param  {[page]} page 评论页数
 */
var ajaxDataFlag = true;

function ajaxData() {
	var specialDetailParams = {};
	specialDetailParams.body = {
		id: id,
		/*pageSize:10,
		pageNum:page*/
	};
	specialDetailParams.header = {
		token: token,
		time_stamp: time_stamp
	}

	var param = JSON.stringify(specialDetailParams);
	$.ajax({
		url: ctmsHost + "/ctmsApi/specialSubject/specialSubjectDetail?str=" + param,
		//data: {},
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			console.log(data);
			$('.special-comment-item').html("");
			$(".special-fixed").show();

			//评论数
			var commentNum = data.list.length;
			$("#specialBackNum").text(commentNum);
			commentNum == 0 ? $(".commemt-num span").text("评论") : $(".commemt-num span").text(commentNum);

			//点赞数
			data.upSum == 0 ? $(".plNum span").text("点赞") : $(".plNum span").text(data.upSum);
			data.upType == 1 ? $(".plNum img").attr("src", upTypePre) : $(".plNum img").attr("src", upTypeNor);

			//是否收藏
			data.ctType == 1 ? $(".special-collect img").attr("src", collectIconPre) : $(".special-collect img").attr("src", collectIconNor)

			if(data.visitor == 1) {
				visitor = 1;
			}
			userAccount = data.userAccount;
			//禁言
			if(data.talkForbidStatus == 1) {
				//禁用评论输入框
				$("#llls").attr("placeholder", "您已被禁言，不能发表评论");
				$("#llls").attr("disabled", true);
			}

			//只加载一次详情信息
			if(ajaxDataFlag) {

				ajaxDataFlag = false;
				$(".special-detail-name").text(data.title);
				if (data.origin != '1') {
					var nameTitle=data.authorName ? data.authorName : "联享家"
					$(".special-detail-time span").eq(0).text(nameTitle);
				}	
				$(".special-detail-time span").eq(1).text(data.createTime);
				$(".special-pic>p").html(data.content);

				title = data.title;
				subTitle = data.secondTitle;
				indexPic = data.picture1Path;

				$(".special-header>div:first-child").show();
			}
			/*console.log(66);
			setTimeout(function(){
				var video, output;
				var scale = 0.8;
				//var initialize = function() {
					video = document.getElementsByTagName("video")[0];				
					//video.addEventListener('loadeddata', captureImage);
				//};

				//var captureImage = function() {
					var canvas = document.createElement("canvas");
					canvas.width = video.videoWidth * scale;
					canvas.height = video.videoHeight * scale;
					canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);

					var img = document.createElement("img");
					img.src = canvas.toDataURL("image/png");
					console.log(canvas.toDataURL("image/png"))
					$("video").attr("poster",canvas.toDataURL("image/png"))
			},3000)
				var video, output;
				var scale = 0.8;
				//var initialize = function() {
					video = document.getElementsByTagName("video")[0];				
					//video.addEventListener('loadeddata', captureImage);
				//};

				//var captureImage = function() {
					var canvas = document.createElement("canvas");
					canvas.width = video.videoWidth * scale;
					canvas.height = video.videoHeight * scale;
					canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);

					var img = document.createElement("img");
					img.src = canvas.toDataURL("image/png");
					console.log(canvas.toDataURL("image/png"))
					$("video").attr("poster",canvas.toDataURL("image/png"))
				//};

				//initialize();*/
			

			$(".special-comment-count span span").text(data.total >= 100 ? "99+" : data.total);
			//评论信息
			if(data.list.length > 0) {
				//评论内容只显示250个字
				for(var i = 0; i < data.list.length; i++) {
					var content = data.list[i].content;
					if(content.length > 500) {
						data.list[i].content = content.substring(0, 500) + "...";
					} else if(content.length == 500) {
						data.list[i].content = data.list[i].content;
					}
					data.list[i].content = htmlEncodeJQ(data.list[i].content);
				}
				var t = _.template($("#special_comment").html());
				$('.special-comment-item').append(t({
					"data": data.list
				}));

				$(".comment-over").show();
				$(".special-comment-item>div").css({
					"borderBottom": "1px solid #eee"
				});
				$(".special-comment-item>div:last-child").css({
					"borderBottom": "none"
				});
				ccp(); //判断时候够十条评论数据
			} else {
				var tEmpty = _.template($("#special_comment_empty").html());
				$('.special-comment-item').append(tEmpty({
					"data": data.list
				}));
				$(".comment-over").hide();
			}

			//图片加载完后定位
			if($(".special-pic p section p img").length > 0) {
				var imgNum = $('.special-pic p section p img').length;
				$('.special-pic img').load(function() {
					if(!--imgNum) {
						jumpFunc();
					}
				});
			} else {
				jumpFunc();
			}
			if(data.origin == '1') {
				setTitle("文章详情");
				//扎克文章
				var articleUrl = data.articleUrl;
				$(".special-detail,.special-comment,.comment-over").hide();
				if((articleUrl.slice(0, 5)).indexOf('https') >= 0) {} else {
					articleUrl = articleUrl.replace('http', 'https');
				}
				$("#zkspecial_box iframe").attr('src', articleUrl);
				$("#zkspecial_box").show();
			} else {
				authorId = data.authorId;
				followType = data.followType; // 用户对文章作者关注状态（0未关注,1关注）
				//console.log()
				if(isCondition(authorId)&&isCondition(data.authorName)) {
					//&&sessionStorage.getItem("lxjversionsName")>=4300
					getfollowTypeButtom(followType);
				}
				if(sessionStorage.getItem("lxjversionsName") >= 5000) {
					setTimeout(function() {
						if(GetURLParameter("clientType") != 'ios') {
							setTitle(data.authorName ? data.authorName : "文章详情");
						} else {
							try {
								callNativeMethod('setNewtitle', {
									titleText: data.authorName ? data.authorName : "文章详情"
								});
							} catch(e) {}
						}
					}, 100)
				} else {
					setTitle("专题详情");
				}

			}
			bigSpecial(); // 大数据获取专题详情信息	
		}
	});
}

/**
 * [inputFocus description] 自动获取键盘输入状态
 * @return {[type]} [description]
 */
$(function() {
	//setTitle("文章详情");
	if(focusFlag == "1") {
		rrhjsahdkj();
	}
});

//获取右上角关注图标
function getfollowTypeButtom(state) {
	// 用户对文章作者关注状态（0未关注,1关注）
	if(state == "1") {
		console.log("已关注状态");
		var jsonData = {
			buttonText: "已关注", //按钮名字
			buttonId: "1", //按钮id
			borderNone: "0", //1按钮没有边框，0按钮有边框
		};
		var jsonData2 = JSON.stringify(jsonData);
		nativeMethod("showAttentionBtn", jsonData2);
	} else {
		console.log("未关注状态");
		var jsonData = {
			buttonText: "+ 关注", //按钮名字
			buttonId: "2", //按钮id
			borderNone: "0", //1按钮没有边框，0按钮有边框
		};
		var jsonData2 = JSON.stringify(jsonData);
		nativeMethod("showAttentionBtn", jsonData2);
	}
}
//右上角事件响应
var attentionBtnClickState = false;

function attentionBtnClick(buttonText, buttonId) {
	if(attentionBtnClickState) {
		return false;
	}
	attentionBtnClickState = true;
	if(buttonText == "已关注" && buttonId == "1") {
		updateAttSpecialColumn(0);
	} else if(buttonText == "+ 关注" && buttonId == "2") {
		updateAttSpecialColumn(1);
	}
}

function syncDataStateSpecial(state) {
	var jsonData = {
		type: "6", //作者关注状态刷新
		id: authorId, //作者id
		followOrNot: state, //0未关注，1已关注
	};
	var jsonData2 = JSON.stringify(jsonData);
	nativeMethod("syncDataState", jsonData2);
}

function updateAttSpecialColumn(status1) {
	var time_stamp = new Date().getTime();
	var paramsup = {};
	paramsup.body = {
		status: status1,
		organizationSeq: organizationSeq,
		specialColumnIdList: [
			authorId
		],
	};
	paramsup.header = {
		time_stamp: time_stamp,
		token: token
	}
	var param = encodeURI(JSON.stringify(paramsup));

	$.ajax({
		type: 'GET',
		url: ctmsHost + "/ctmsApi/specialColumn/updateAttSpecialColumn?str=" + param,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			if(data.result == 0) {
				getfollowTypeButtom(status1);
				syncDataStateSpecial(status1 == '1' ? 1 : 0);
			} else {
				lxjTip.msg(data.reason);
			}
			attentionBtnClickState = false;
		},
		error: function(XmlHttpRequest, textStatus, errorThrown) {
			attentionBtnClickState = false;
		}
	});
}

function rrhjsahdkj() {
	setTimeout(function() {
		$(".special-comment-fixed div input").trigger("click").focus();
	}, 3000);
}
/**
 * 通过点击头像跳转
 */
function goToUser(ele) {
	var userAccount = String($(ele).attr("useraccount"));
	//userAccount.length == 7 ? "0" + userAccount: userAccount;
	//调用原生方法
	var params = {
		"userAccount": userAccount.toString()
	};
	var paramData = JSON.stringify(params);
	setTimeout(function() {
		accessDetailRemainTime((new Date().getTime()) - createTime2);
	}, 0);
	nativeMethod("viewUserInfo", paramData);
}

/**
 * [description] 判断是发送按键的颜色
 * 
 */
$(function() {
	$('.special-comment-fixed input').bind('input propertychange', function() {
		if(!$(this).val().trim() == "") {
			$(".special-comment-fixed span").addClass("special-comment-active");
		} else {
			$(".special-comment-fixed span").removeClass("special-comment-active");
		}
	});
})

/**
 * [description] 提交评论信息
 */
$(function() {
	$("#llls").focus(function() {
		if(visitor == 1) {
			needLogin(token);
			return false;
		}
	});

	$(".special-comment-fixed span").click(function() {
		if(visitor == 1) {
			needLogin(token);
			return false
		}
		if($(this).hasClass("special-comment-active")) {
			var content = $('.special-comment-fixed input').val()
			var sendCommentValue = encodeURIComponent(content);
			var specialCommentParams = {};
			specialCommentParams.header = {
				token: token,
				time_stamp: time_stamp
			};
			specialCommentParams.body = {
				parentId: id,
				type: 2,
				content: sendCommentValue
			}

			var param = JSON.stringify(specialCommentParams);
			console.log(specialCommentParams);
			$.ajax({
				type: 'GET',
				url: ctmsHost + "/ctmsApi/subjectBack/addSubjectBack?str=" + param,
				//data: {},
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback",
				success: function(data) {
					if(data.result == 0) {
						//清空输入框
						$("#llls").val("");
						$(".special-comment-fixed span").removeClass("special-comment-active");
						//清空评论，加载最新评论
						$(".subjectBack").each(function() {
							$(this).remove();
						});
						$(".special-comment-item").html("");
						$(".special-load-more").text("加载更多中...");
						ajaxData();
						page = 1;
						specialCommentFirst();
					} else {
						setTimeout(function() {

							lxjTip.msg(data.reason);
						}, 300);
					}
				}
			});
		}

	});

	$(".special-comment-fixed input").focus(function() {
		setTimeout(function() {
			document.body.scrollTop = document.body.scrollHeight;
		}, 100);
	});
})

/**
 * [share description] 点击分享
 */

$(".special-share").click(function() {
	getSharedata();
});

function getSharedata() {
	if(visitor == 1) {
		needLogin(token);
		return false;
	}

	var jsonData = {
		title: title,
		subTitle: subTitle,
		indexPic: indexPic,
		ishoriBigData: 1,
		id: id,
		fromType: 2,
		type: 3,
		url: ctmsHost + "/ctmsH5/special/specialShare.html?id=" + id
	};
	var jsonData2 = JSON.stringify(jsonData);
	nativeMethod("share", jsonData2);
}

/**
 * 点击收藏
 */
var collectTimeStart = new Date().getTime();
$(".special-collect").click(function() {
	var collectTimeEnd = new Date().getTime();
	var collectTimes = collectTimeEnd - collectTimeStart;

	if(collectTimes <= 1500) {
		if(!collectOnce) {
			lxjTip.msg("请勿频繁操作");
		}
		collectTimeStart = collectTimeEnd;
	} else {
		if(!collectOnce) {
			collectTimeStart = collectTimeEnd;
			collectAjax();
		}
	}
	if(collectOnce) {
		collectOnce = false;
		collectTimeStart = collectTimeEnd;
		collectAjax();
	}
});

function collectAjax() {
	if(visitor == 1) {
		needLogin(token);
		return false;
	}
	var collectSrc = $(".special-collect img").attr("src");
	collectSrc == collectIconPre ? upType = "2" : upType = "1";
	var paramsClick = {
		header: {
			time_stamp: time_stamp,
			token: token
		},
		body: {
			channel_type: "1",
			operate_type: "1",
			up_type: upType,
			id: id,
			name: title,
			organizationSeq: organizationSeq,
			createTime: creatTimes(),
		}
	}
	console.log(paramsClick);
	var param = encodeURI(JSON.stringify(paramsClick));

	$.ajax({
		type: 'GET',
		url: ctmsHost + "/ctmsApi/home/userThumbsUpCollectServlet?str=" + param,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			if(data.result == 0) {
				data.isCollect == "1" ? $(".special-collect img").attr("src", collectIconPre) : $(".special-collect img").attr("src", collectIconNor);
				//传数据给原生
				upType == 1 ? state = 1 : state = 0;
				var jsonData = {
					type: 1,
					id: id,
					state: state
				};
				var jsonData2 = JSON.stringify(jsonData);
				nativeMethod("syncDataState", jsonData2);
			} else {
				lxjTip.msg(data.reason);
			}
		}
	});
}

//点赞
var upTimeStart = new Date().getTime();
$(".plNum").click(function() {
	var upTimeEnd = new Date().getTime();
	var upTimes = upTimeEnd - upTimeStart;

	if(upTimes <= 1500) {
		if(!upOnce) {
			lxjTip.msg("请勿频繁操作");
		}
		upTimeStart = upTimeEnd;
	} else {
		if(!upOnce) {
			upTimeStart = upTimeEnd;
			upAjax();
		}
	}
	if(upOnce) {
		upOnce = false;
		upTimeStart = upTimeEnd;
		upAjax();
	}

});

function upAjax() {
	if(visitor == 1) {
		needLogin(token);
		return false;
	}
	var upSrc = $(".plNum img").attr("src");
	upSrc == upTypePre ? upType = "2" : upType = "1";
	var paramsClick = {
		header: {
			time_stamp: time_stamp,
			token: token
		},
		body: {
			channel_type: "1",
			operate_type: "2",
			up_type: upType,
			id: id,
			name: title,
			organizationSeq: organizationSeq,
			createTime: creatTimes(),
		}
	}
	console.log(paramsClick);
	var param = encodeURI(JSON.stringify(paramsClick));
	$.ajax({
		type: 'GET',
		url: ctmsHost + "/ctmsApi/home/userThumbsUpCollectServlet?str=" + param,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			console.log(data);
			if(data.result == 0) {
				data.isUp == "1" ? $(".plNum img").attr("src", upTypePre) : $(".plNum img").attr("src", upTypeNor);
				$(".plNum span").text(data.upNum);
				//传数据给原生
				upType == 1 ? state = 1 : state = 0;
				var jsonData = {
					type: 2,
					id: id,
					state: state,
					info: data.upNum
				};
				var jsonData2 = JSON.stringify(jsonData);
				nativeMethod("syncDataState", jsonData2);
			}
		}
	});
}

/**
 * 删除评论
 */
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
				type: 2,
				parentId: parentId
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
						$("#" + backId).remove();
						var num = $("#specialBackNum").text();
						$("#specialBackNum").text(parseInt(num) - 1);
						if($(".subjectBack").length == 0) {
							var tEmpty = _.template($("#special_comment_empty").html());
							$('.special-comment-item').append(tEmpty("暂时还没有评论，快点来抢沙发！"));
							$(".comment-over").hide();
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

//跳转是定位评论
function jumpFunc() {
	setTimeout(function() {
		if(isCondition(subjectBackId)) {
			$('html,body').animate({
				scrollTop: $("#" + subjectBackId).offset().top
			}, 300);
		}
	}, 500);
}

//调整到评论页
function commentJump() {
	if(visitor == 1) {
		needLogin(token);
		return false;
	}
	setTimeout(function() {
		accessDetailRemainTime((new Date().getTime()) - createTime2);
	}, 0);
	var url = ctmsHost + '/ctmsH5/special/commentList.html?id=' + id + '&type=2&token=' + token + '&parentId';
	showActivity(url, "评论");
}

function isCondition(param) {
	if(param != null && param != "" && param != undefined) {
		return true;
	}
	return false;
}

/**
 * [description] 回到第一个评论
 * @param  {[type]} )
 */
function specialCommentFirst() {
	var specialDetailHeight = $(".special-detail").height() - 300;
	console.log(1);
	$('.special-header').animate({
		scrollTop: specialDetailHeight
	}, 300);
}

//转义
function htmlEncodeJQ(str) {
	return $('<span/>').text(str).html();
}

//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return decodeURI(r[2]);
	return null;
}

$(".special-header").on("touchstart", function() {
	$("#llls").blur();
});

function creatTimes() {
	var times = new Date();
	var YY = times.getFullYear();
	var MM = times.getMonth() + 1;
	if(MM < 10) MM = "0" + MM;
	var DD = times.getDate();
	if(DD < 10) DD = "0" + DD;
	var HH = times.getHours();
	if(HH < 10) HH = "0" + HH;
	var mm = times.getMinutes();
	if(mm < 10) mm = "0" + mm;
	var ss = times.getSeconds();
	if(ss < 10) ss = "0" + ss;
	times = YY + "-" + MM + "-" + DD + " " + HH + ":" + mm + ":" + ss;
	return times;
}

function bigSpecial() {
	if(lxjversionsNames < 4200) {
		return false;
	}
	var createTime = creatTimes();
	visitor == 1 ? userAccount = token : userAccount = userAccount;
	var param = {
		header: {
			token: token,
			time_stamp: time_stamp
		},
		body: {
			list: [{
				userAccount: userAccount,
				type: "3",
				channelType: channelType,
				id: id,
				name: title,
				organizationSeq: organizationSeq,
				createTime: createTime,
				communityId: communityId,
				communityName: decodeURI(communityName),
				appBannerType: appBannerType,
				otherChannelId: otherChannelId
			}]
		}
	}
	console.log(param);
	param = JSON.stringify(param);
	$.ajax({
		type: 'GET',
		url: horiBigDataHost + "/horiBigData/bigSource/accessActivitySpecialDetailServlet?str=" + param,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			console.log(data);
		}
	});
}

/*大数据活动停留时间*/
function accessDetailRemainTime(remainTime) {
	if(lxjversionsNames < 4200) {
		return false;
	}
	if(accessDetailRemainTimeState) {
		return false;
	}
	accessDetailRemainTimeState = true;
	try {
		visitor == 1 ? userAccount = token : userAccount = userAccount;
		var params = {};
		var timestamp = new Date().getTime();
		params.header = {
			token: token,
			time_stamp: timestamp
		};
		params.body = {
			list: [{
				userAccount: userAccount,
				type: 3,
				operateType: 1,
				id: id,
				name: title,
				organizationSeq: organizationSeq,
				createTime: createTime1,
				remainTime: remainTime,
			}]
		};
		var param = JSON.stringify(params);
		$.ajax({
			type: "post",
			async: true,
			url: horiBigDataHost + "/horiBigData/bigSource/accessDetailRemainTimeOrLoadServlet?str=" + param,
			dataType: "jsonp",
			jsonp: "jsoncallback",
			jsonpCallback: "success_jsonpCallback",
			success: function() {}
		});
	} catch(e) {
		//TODO handle the exception
	}
}
window.onbeforeunload = onbeforeunload_handler;
window.onunload = onunload_handler;
document.addEventListener('webkitvisibilitychange', function() {
	if(document.webkitVisibilityState == 'hidden') {
		accessDetailRemainTime((new Date().getTime()) - createTime2);
	} else {}
})
var accessDetailRemainTimeState = false;

function onbeforeunload_handler() {
	var warning = accessDetailRemainTime((new Date().getTime()) - createTime2);
	return warning;
}

function onunload_handler() {
	accessDetailRemainTime((new Date().getTime()) - createTime2);
}