setTitle("活动详情");
initializeSession();
//时间戳
var time_stamp = getTimeStamp();
var organizationSeq = GetURLParameter("organizationSeq");
var communityId = GetURLParameter("communityId");
var activityId = GetURLParameter("activityId");
var backHomePage = GetURLParameter("backHomePage"); //回退到生活首页

var channelType= GetURLParameter("channelType")?GetURLParameter("channelType"):"8"; //大数据统计页面进入来源
var communityId=GetURLParameter("communityId")?GetURLParameter("communityId"):"";
var communityName= GetURLParameter("communityName")?GetURLParameter("communityName"):""; 
var appBannerType=GetURLParameter("appBannerType")?GetURLParameter("appBannerType"):"";
var otherChannelId= GetURLParameter("otherChannelId")?GetURLParameter("otherChannelId"):""; 
if (activityId.indexOf('?')>0) {
	var idchannelType=activityId.split('?');	
	var idchannelType2=activityId.split('=');
	activityId=idchannelType[0];
	channelType=idchannelType2[1];
}
var createTime = getTime(); //页面访问时间
var createTime2 = new Date().getTime();
if(backHomePage == "1") {
	backToHomePage();
}
var userAccount='';
var isVisitor = false;
if(token && token.indexOf("_") == 0) {
	isVisitor = true;
	userAccount=token;
}
var title = '';
var subTitle = '';
var indexPic = '';
/*var token = "1517537689533f3831c945264c57a034";
var activityId = "15171187178832f5c3e741c748298501";
organizationSeq = "4400100001";*/
//var commodityId="";
var params = {};
params.header = {
	token: token,
	time_stamp: time_stamp
};
params.body = {
	activityId: activityId,
	organizationSeq: organizationSeq
};
var param = JSON.stringify(params);
$.ajax({
	type: "post",
	async: false,
	url: ctmsHost + "/ctmsApi/activity/getCommonActivity?str=" + param,
	dataType: "jsonp",
	jsonp: "jsoncallback",
	jsonpCallback: "success_jsonpCallback",
	success: function(data) {
		console.log(data);
		setTimeout(function() {
			addPageView();
		}, 200);
		if(data.result == 0) {
			title = data.title;
			subTitle = data.subTitle;
			indexPic = data.indexPic;
			if (!isVisitor) {
				userAccount=data.userAccount?data.userAccount:token;
			}
			setTimeout(function(){
				accessActivitySpecialDetail();
			},300)
			var getVoteActivity = _.template($('#getVoteActivityTemplate').html());
			$('#getVoteActivity').append(getVoteActivity(data));

			//点击标题内容图片预览大图
			$(".electDisc>p img").attr("data-preview-src", "");
			$(".electDisc>p img").attr("data-preview-group", "2");
			$(".electDisc>p a>img").removeAttr("data-preview-src");
			$(".electDisc>p a>img").removeAttr("data-preview-group");
			//			$(".electDisc>p a").attr("onclick","goNewwebview(this);return false;");

			//为你优选商品排序
			var commodityList = data.commodityList;
			if(commodityList.length == 0) {
				$(".detailBox").hide();
			} else {
				for(var j = 0; j < commodityList.length; j++) {
					var item = commodityList[j];
					var commodityPrice = item.commodityPrice;
					commodityPriceMax = (commodityPrice + "").split(".")[0];
					commodityPriceMin = (commodityPrice + "").split(".")[1];
					var commodityListBox = _.template($('#commodityListTemplate').html());
					$('#commodityList').append(commodityListBox(item));
				}
				$(".detailLeft").on("click", function() {
					var li = $(this);
					commodityId = li.attr("id");
					commodityType = li.attr("commodityType");
					sessionStorage.setItem("commodityId", commodityId);
					console.log(commodityType);
					setTimeout(function() {
						accessDetailRemainTime((new Date().getTime()) - createTime2);
					}, 0);
					//判断是服务到家商品还是商品超市商品
					if(commodityType == 1) {
						showActivity(mmsHost + "/mms/html5/supermarket/ProductDetail.htm?commodityId=" + commodityId, "商品详情");
					} else {
						showActivity(mmsHost + "/mms/html5/mall/productDetail.htm?commodityId=" + commodityId, "商品详情");
					}
					return false;
				});
			}

		} else {
			lxjTip.alert("活动信息异常");
		}
	}
});
var nativeMethodState = 1;

function getSharedata() {
	if(title.length > 0) {
		var jsonData = {
			title: title,
			subTitle: subTitle,
			indexPic: indexPic,
			url: ctmsHost + "/ctmsH5/offlineActivity/share.htm?activityId=" + activityId,
			ishoriBigData:1,
			id:activityId,
			type:2,
			fromType:1
		};
		var jsonData2 = JSON.stringify(jsonData);
		//nativeMethodState = 0;
		var aa = nativeMethod("share", jsonData2);
		//		lxjTip.msg("nativeMethod:"+aa,{time:300000});
		//		if (aa==1) {
		//			nativeMethodState=1;			
		//		}
	}
}
/*
var lxjversionsName = GetURLParameter("lxjversionsName");
function goNewwebview(ele) {
	var gourl = $(ele).attr("href");
	if(gourl.indexOf('hori') > 0) {
		showActivity(gourl, "");
	} else {
		if(isCondition(lxjversionsName) && Number(lxjversionsName.replace(/[^0-9]/ig, "")) >= 3011) {
			showActivitySpecial(gourl, '', 6, null);
		} else {
			window.open(gourl);
		}
	}
return false;
}*/
/*****大数据用户访问活动*****/
function accessActivitySpecialDetail() {
	if (sessionStorage.getItem("lxjversionsName")<4200) {
		return false;
	}
	try{
	var params = {};
	var timestamp = new Date().getTime();
	params.header = {
		token: token,
		time_stamp: timestamp
	};
	params.body = {
		list: [{
			userAccount: userAccount,
			type: 2,
			channelType: channelType,
			id: activityId,
			name: title,
			organizationSeq: organizationSeq,
			createTime: createTime,
			communityId: communityId,
			communityName: communityName,
			appBannerType: appBannerType,
			otherChannelId: otherChannelId
		}]
	};
	var param = JSON.stringify(params);
	$.ajax({
		type: "post",
		async: true,
		url: horiBigDataHost + "/horiBigData/bigSource/accessActivitySpecialDetailServlet?str=" + param,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function() {}
	});			
	}catch(e){
		//TODO handle the exception
	}
}
/*****大数据活动停留时间*****/
function accessDetailRemainTime(remainTime) {
	if(accessDetailRemainTimeState){
    	return false;
    }
	if (sessionStorage.getItem("lxjversionsName")<4200) {
		return false;
	}
	accessDetailRemainTimeState=true;
	try{
	var params = {};
	var timestamp = new Date().getTime();
	params.header = {
		token: token,
		time_stamp: timestamp
	};
	params.body = {
		list: [{
			userAccount: userAccount,
			type: 2,
			operateType: 1,
			id: activityId,
			name: title,
			organizationSeq: organizationSeq,
			createTime: createTime,
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
	}catch(e){
		//TODO handle the exception
	}
}
window.onbeforeunload = onbeforeunload_handler;
window.onunload = onunload_handler;
var accessDetailRemainTimeState=false;
document.addEventListener('webkitvisibilitychange', function() {
	if(document.webkitVisibilityState == 'hidden') {
		accessDetailRemainTime((new Date().getTime()) - createTime2);
	} else {}
})

function onbeforeunload_handler() {
	var warning = accessDetailRemainTime((new Date().getTime()) - createTime2);
	return warning;
}

function onunload_handler() {
	accessDetailRemainTime((new Date().getTime()) - createTime2);
}
//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return decodeURI(r[2]);
	return null;
}
function addPageView() {
	var params = {};
	params.header = {
		token: token,
		time_stamp: time_stamp
	};
	params.body = {
		activityId: activityId,
	};
	var param = JSON.stringify(params);
	$.ajax({
		type: "post",
		async: false,
		url: ctmsHost + "/ctmsApi/activity/addPageView?str=" + param,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {}
	});
}

function isCondition(param) {
	if(param != null && param != "" && param != undefined) {
		return true;
	}
	return false;
}

function refreshData() {
	setTimeout(function() {
		createTime = getTime(); //页面访问时间
		createTime2 = new Date().getTime();
		accessDetailRemainTimeState=false;
	}, 0);
	return 1;
}
var onscrollState = true;
window.onscroll = function() {
	if(onscrollState) {
		onscrollState = false;
		$(".reminder").show();
	}
};