/*var time_stamp = new Date().getTime();
var id = "15133067450289aa2b83ca55475e969b";
var ctmsHost = "http://tt.hori-gz.com:8090";
var token = "1514941626992d3c8c2ba15b4d5fbc33";
var organizationSeq = "4400100001";*/

var time_stamp = new Date().getTime();
var id = GetURLParameter("id");
var token = GetURLParameter("token");
var organizationSeq = GetURLParameter("organizationSeq");
var otherChannelId = GetURLParameter("otherChannelId") == undefined ? "" : GetURLParameter("otherChannelId");

var focusFlag = true;
var userAccount = "";
var title = "";
var channelType = "";

/**
 * [ajaxData]分享数据请求
 * @param  
 */
var ajaxDataFlag = true;

var specialDetailParams = {};
specialDetailParams.body = {
	id: id
};
specialDetailParams.header = {
	token: token,
	time_stamp: time_stamp
}
console.log(specialDetailParams);
var param = JSON.stringify(specialDetailParams);
$.ajax({
	url: ctmsHost + "/ctmsApi/specialSubject/shareSpecialSubjectDetailContent?str=" + param,
	//data: {},
	dataType: "jsonp",
	jsonp: "jsoncallback",
	jsonpCallback: "success_jsonpCallback",
	success: function(datas) {
		if(datas.result == 0) {
			setTimeout(function() {
				$(".special-detail").show();
				userAccount = datas.userAccount;
				title = datas.title;
				//QQ分享自定义
				setShareInfo({
					title: title,
					summary: datas.secondTitle ? datas.secondTitle : "联享家专题",
					pic: datas.picture1Thumpath ? datas.picture1Thumpath : "",
					url: window.location.href
				});
				$(".special-detail-name").text(datas.title);
				if(datas.origin != '1') {
					var nameTitle = datas.authorName ? datas.authorName : "联享家"
					$(".special-detail-time span").eq(0).text(nameTitle);
				}

				$(".special-detail-time span").eq(1).text(datas.createTime);
				$(".special-pic>p").html(datas.content);
				if(datas.origin == '1') {
					//扎克文章
					var articleUrl = datas.articleUrl;
					$(".special-detail,.special-comment,.comment-over").hide();
					if((articleUrl.slice(0, 5)).indexOf('https') >= 0) {} else {
						articleUrl = articleUrl.replace('http', 'https');
					}
					$("#zkspecial_box iframe").attr('src', articleUrl);
					$("#zkspecial_box").show();
				} else {

				}

				bigSpecial();
			}, 300);
		} else {
			lxjTip.alert(datas.reason, {
				yes: function() {
					lxjTip.close(); //如果设定了yes回调，需进行手工关闭
				}
			});
		}

	}
});

//关闭下载栏
function closeFooter() {
	$(".special-download-fixed").css("display", "none");
}
$(function() {
	new Mlink({
		mlink: 'https://aejlur.mlinks.cc/A0mi?zhuangtiId=' + id + '&channelType=' + channelType,
		button: document.querySelector('a#goupdown')
	});
})

//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}

//返回顶部
$(function() {
	var windowHeight = $(window).height();
	$(window).scroll(function() {
		if($(window).scrollTop() >= 3 * windowHeight) {
			$(".top").slideDown(300);
		} else {
			$(".top").slideUp(300);
		}
	});

	$(".top").click(function() {
		console.log("top");
		$('body,html').animate({
			scrollTop: 0
		}, 300);
	});
})

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
var createTime2 = new Date().getTime();
var createTime1 = creatTimes();

function bigSpecial() {
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i) == "micromessenger") {
		channelType = "5"; //在微信中打开
	} else if(ua.match(/QQ/i) == "qq") {
		channelType = "6"; //在QQ空间打开
	} else {
		channelType = "7";
		otherChannelId == "" ? otherChannelId = 3 : otherChannelId = otherChannelId;
	}
	var createTime = creatTimes();
	var param = {
		header: {
			token: token,
			time_stamp: time_stamp
		},
		body: {
			list: [{
				userAccount: token,
				type: "3",
				channelType: channelType,
				id: id,
				name: title,
				organizationSeq: organizationSeq,
				createTime: createTime,
				communityId: "",
				communityName: "",
				appBannerType: "",
				otherChannelId: otherChannelId
			}]
		}
	}
	param = JSON.stringify(param);
	$.ajax({
		type: 'GET', // ctmsHost
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