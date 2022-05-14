switchFullScreen(false); //关闭全屏显示
setTitle("活动详情");
//时间戳
var activityId = GetURLParameter("activityId");
var organizationSeq = GetURLParameter("organizationSeq");
var token = GetURLParameter("token");
var isShare = GetURLParameter("isShare");

var channelType = GetURLParameter("channelType") ? GetURLParameter("channelType") : "8"; //大数据统计页面进入来源
var communityId = GetURLParameter("communityId") ? GetURLParameter("communityId") : "";
var communityName = GetURLParameter("communityName") ? GetURLParameter("communityName") : "";
var appBannerType = GetURLParameter("appBannerType") ? GetURLParameter("appBannerType") : "";
var otherChannelId = GetURLParameter("otherChannelId") ? GetURLParameter("otherChannelId") : "";
if (activityId.indexOf('?')>0) {
	var idchannelType=activityId.split('?');	
	var idchannelType2=activityId.split('=');
	activityId=idchannelType[0];
	channelType=idchannelType2[1];
}

var createTime = getTime(); //页面访问时间
var createTime2 = new Date().getTime();
var clickevent = 1;
if(isShare == 1) {
	isShare = false;
} else {
	isShare = true;
}

//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return decodeURI(r[2]);
	return null;
}
/*var organizationSeq = '4400100001';
var token = "151313556494f8ec7ca39ebe49919c03";
var activityId = "151313358212fa7bbf10341f4a73aaf1";*/
var userAccount = '';
var isVisitor = false;
if(token && token.indexOf("_") == 0) {
	isVisitor = true;
	userAccount = token;
}
var backHomePage = GetURLParameter("backHomePage"); //回退到生活首页
if(backHomePage == "1") {
	backToHomePage();
}
var title = '';
var subTitle = '';
var indexPic = '';
var code = '';

//var ctmsHost = "http://192.168.51.26:8090";
select();
setTimeout(function() {
	addPageView();
}, 500);

function addPageView() {
	var timestamp = new Date().getTime();
	var params = {};
	params.header = {
		token: token,
		time_stamp: timestamp
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

function refreshData() {
	setTimeout(function() {
		createTime = getTime(); //页面访问时间
		createTime2 = new Date().getTime();
		accessDetailRemainTimeState=false;
		select();
	}, 0);
	return 1;
}

function getSharedata() {
	var jsonData = {};
	if(clickevent == 1) {
		jsonData = {
			eventId: "2.0-click25",
			eventName: "参选分享按键点击数"
		};
	} else {
		jsonData = {
			eventId: "2.0-click29",
			eventName: "投票分享按键点击数"
		};
	}
	jsonData = JSON.stringify(jsonData);
	//调用APP接口，添加百度统计
	nativeMethod("baiduStatistics", jsonData);
	if(title.length > 0) {
		var jsonData = {
			title: title,
			subTitle: subTitle,
			indexPic: indexPic,
			url: ctmsHost + "/ctmsH5/offlineActivity/share.htm?activityId=" + activityId,
			ishoriBigData: 1,
			id: activityId,
			type: 2,
			fromType:1
		};
		var jsonData2 = JSON.stringify(jsonData);
		var aa = nativeMethod("share", jsonData2);
		//		if (aa==1) {
		//			lxjTip.msg("成功返回1")
		//		}
	}
}

function select() {
	var params = {};
	var timestamp = new Date().getTime();
	params.header = {
		token: token,
		time_stamp: timestamp
	};
	params.body = {
		activityId: activityId,
		organizationSeq: organizationSeq
	};
	var param = JSON.stringify(params);

	$.ajax({
		type: "post",
		async: false,
		url: ctmsHost + "/ctmsApi/activity/getElectionVoteActivity?str=" + param,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			console.log(JSON.stringify(data));
			if(data.result == 0) {
				title = data.title;
				subTitle = data.subTitle;
				indexPic = data.indexPic;
				if(!isVisitor) {
					userAccount = data.userAccount ? data.userAccount : token;
				}
				setTimeout(function(){
					accessActivitySpecialDetail();
				},600)
				code = data.code;
				//活动标题数据插入
				$('#activityId').empty();
				var activityIdData = _.template($('#activityIdTemplate').html());
				$('#activityId').append(activityIdData(data));

				//点击标题内容图片预览大图
				$(".content>p>img").attr("data-preview-src", "");
				$(".content>p>img").attr("data-preview-group", "2");

				//为你优选商品排序
				var commodityList = data.commodityList;
				if(commodityList.length == 0) {
					$(".detailBox").hide();
				} else {
					$('#commodityList').empty();
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
						setTimeout(function() {
							accessDetailRemainTime((new Date().getTime()) - createTime2);
						}, 0);
						if(commodityType == 1) {
							showActivity(mmsHost + "/mms/html5/supermarket/ProductDetail.htm?commodityId=" + commodityId, "商品详情");
						} else {
							showActivity(mmsHost + "/mms/html5/mall/productDetail.htm?commodityId=" + commodityId, "商品详情");
						}
						return false;
					});
				}

				if(data.electionVoteFlag == 2) {
					clickevent = 2;
					var isMultiSelectNum = data.isMultiSelectNum;

					//判断是单选还是多选
					$('#isMultiSelect').empty();
					if(data.activityOptions.length > 0) {
						var isMultiSelect = _.template($('#isMultiSelectTemplate').html());
						$('#isMultiSelect').append(isMultiSelect(data));
						//投票里数据的插入
						var activityOptions = data.activityOptions;
						$("#activityOptions").empty();
						for(var i = 0; i < activityOptions.length; i++) {
							var int = i + 1;
							if(data.isMultiSelect == 0) {
								$(".pollnum span").html("(单选)");
								var oHtml = "<div class='voteSelect'>" + "<div class='selectA'>" + " <label>" + "<div class='select_list'>" + "&nbsp;" + "</div>" +
									"<input name='Fruit' class='select' type='radio' value='" + activityOptions[i].optionsId + "' />" + "<div class='voteImg'>" + "<img src='" + activityOptions[i].picSource + "' width='100%' style='vertical-align: middle;'data-preview-src='' data-preview-group='1'/>" + "</div>" + "<div class='numberBox'>" + "<div class='number'>" + "<span class='margin-right:0.02rem;'>" + int + "." + "</span>" + activityOptions[i].title + "</div>" + "<div class='pollsBox'>" + "<span class='polls'>" + activityOptions[i].pollNums + "</span>" + "票" + "</div>" + "</div>" + "</label>" + "</div>" + "</div>";
								$("#activityOptions").append(oHtml)
							} else {

								$(".pollnum span").html("(最多选择" + isMultiSelectNum + "票)");
								var oHtmls = "<div class='voteSelect'>" + "<div class='selectA'>" + " <label>" + "<div class='select_list'>" + "&nbsp;" + "</div>" +
									"<input name='Fruit' class='select' type='checkbox' value='" + activityOptions[i].optionsId + "' />" + "<div class='voteImg'>" + "<img src='" + activityOptions[i].picSource + "' width='100%' style='vertical-align: middle;'data-preview-src='' data-preview-group='1'/>" + "</div>" + "<div class='numberBox'>" + "<div class='number'>" + "<span class='margin-right:0.02rem;'>" + int + "." + "</span>" + activityOptions[i].title + "</div>" + "<div class='pollsBox'>" + "<span class='polls'>" + activityOptions[i].pollNums + "</span>" + "票" + "</div>" + "</div>" + "</label>" + "</div>" + "</div>";
								$("#activityOptions").append(oHtmls);

							}
						}
					}

					//点击票选图片预览大图
					$(".voteImg img").attr("data-preview-src", "");
					$(".voteImg img").attr("data-preview-group", "1");
					//点击选票的样式
					$(".select").click(function() {
						var jsonData = {
							eventId: "2.0-click28",
							eventName: "投票按键点击数"
						};
						jsonData = JSON.stringify(jsonData);
						//调用APP接口，添加百度统计
						nativeMethod("baiduStatistics", jsonData);
						$(".select_list").removeClass("select_list1");
						var checked = $("input[name='Fruit']:checked");
						checked.siblings('.select_list').addClass("select_list1");

						//最多只能投票多少的时候 提示框显示
						if(isMultiSelectNum != null) {
							if(checked.length > isMultiSelectNum) {
								lxjTip.msg("最多可投" + isMultiSelectNum + "票");
								$(this).attr('checked', false);
								$(this).prev('.select_list').removeClass("select_list1");
							}
						}
					});
					$(".voteBtn").click(function() {
						if(isVisitor) {
							needLogin(token);
							return false;
						}
						var jsonData = {
							eventId: "2.0-click27",
							eventName: "投票按键点击数"
						};
						jsonData = JSON.stringify(jsonData);
						//调用APP接口，添加百度统计
						nativeMethod("baiduStatistics", jsonData);
						if($('.voteBtn').hasClass('yes')) {
							var checked = $("input[name='Fruit']:checked");
							if(checked.length == 0) {
								lxjTip.msg('请先选择选项');
							} else {
								var optionIds = '';
								for(var k = 0; k < checked.length; k++) {
									optionIds += checked.eq(k).val() + ',';
								}
								optionIds = optionIds.substring(0, optionIds.length - 1);
								submit(optionIds);
							}
						}

					});

					//判断是否已经投票
					if(data.isVoted == 1) {
						$('.voteBtn').removeClass('yes');
						$(".voteBtn div").html("您已投票");
						$(".voteBtn div").css("background", "#ccc");
					} else {
						$('.voteBtn').addClass('yes');
						$(".voteBtn div").html("投票");
						$(".voteBtn div").css("background", "#fc9153");
					}

					if(data.activityOptions.length == 0) {
						$('.voteBtn').removeClass('yes');
						$(".voteBtn div").html("暂无投票选项");
						$(".voteBtn div").css("background", "#ccc");
					}
					if(data.voteStatus === 0) {
						$('.voteBtn').removeClass('yes');
						$(".voteBtn div").html("活动已结束");
						$(".voteBtn div").css("background", "#ccc");
					}
				} else {
					clickevent = 1;
					//点击参选按钮页面跳转

					if(data.isElection == 0 && data.electionStatus !== 0) {
						$('.voteBtn').addClass('yes');
						$(".voteBtn div").html("我要参选");
						$(".voteBtn div").css("background", "#fc9153");
						$(".voteBtn").click(function() {
							if(isVisitor) {
								needLogin(token);
								return false;
							}
							setTimeout(function() {
								accessActivityPartake(4);
							}, 0);
							var jsonData = {
								eventId: "2.0-click24",
								eventName: "参选按键点击数"
							};
							jsonData = JSON.stringify(jsonData);
							//调用APP接口，添加百度统计
							nativeMethod("baiduStatistics", jsonData);
							if($('.voteBtn').hasClass('yes')) {
								setTimeout(function() {
									accessDetailRemainTime((new Date().getTime()) - createTime2);
								}, 0);
								showActivity(ctmsHost + "/ctmsH5/offlineActivity/submit.htm?activityId=" + activityId, "上传资料");
							}
							// isElection是否已经参选，0.没有，1.已参选
						});
					} else if(data.isElection == 1) {
						$(".voteBtn div").html("您已参选");
						$('.voteBtn').removeClass('yes');
						$(".voteBtn div").css("background", "#ccc");
					}
					// 1-只有参选  2-有参选跟投票  3只有投票
					if(data.electionVoteType == 2 && data.electionStatus === 0) {
						$(".voteBtn div").html("已截止参选");
						$('.voteBtn').removeClass('yes');
						$(".voteBtn div").css("background", "#ccc");
					}
					if(data.electionVoteType == 1 && data.electionStatus === 0) {
						$(".voteBtn div").html("活动已结束");
						$('.voteBtn').removeClass('yes');
						$(".voteBtn div").css("background", "#ccc");
					}

				}
				// 游客登录
				if(data.isExistCommunity != 1) {
					$(".voteBtn").hide();
					$("#nothingbottom").hide();
					$("#disisExistCommunity").css("display", "-webkit-flex");
				}

			} else {
				lxjTip.alert("活动信息异常");
			}

		}
	});
}
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
/*****大数据活动参与次数*****/
function accessActivityPartake(appShareType) {
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
			appShareType: appShareType,
			activityId: activityId,
			activityName: title,
			organizationSeq: organizationSeq,
			createTime: createTime,
		}]
	};
	var param = JSON.stringify(params);
	$.ajax({
		type: "post",
		async: true,
		url: horiBigDataHost + "/horiBigData/bigSource/accessActivityPartakeServlet?str=" + param,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {}
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

function submit(optionIds) {
	var params = {};
	var timestamp = new Date().getTime();
	params.header = {
		token: token,
		time_stamp: timestamp
	};
	params.body = {
		activityId: activityId,
		optionIds: optionIds,
		organizationSeq: organizationSeq
	};
	var param = JSON.stringify(params);
	$.ajax({
		type: "post",
		async: false,
		url: ctmsHost + "/ctmsApi/activity/submitPollActivity?str=" + param,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			if(data.result == 0) {
				$('.voteBtn').removeClass('yes');
				setTimeout(function() {
					accessActivityPartake(3);
				}, 0);
				lxjTip.msg('投票成功！');
				select();
				$(".voteBtn div").html("您已投票");
				$(".voteBtn div").css("background", "#ccc");
			}
		}
	});

}
var onscrollState = true;
window.onscroll = function() {
	if(onscrollState) {
		onscrollState = false;
		$(".reminder").show();
	}
};