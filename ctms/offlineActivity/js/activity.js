initializeSession();
//设置时间戳
var time_stamp = getTimeStamp();
var organizationSeq = GetURLParameter("organizationSeq");
var communityId = GetURLParameter("communityId");
var queryType = GetURLParameter("queryType");
var token = GetURLParameter("token");
var lxjversonNum=sessionStorage.getItem("lxjversionsName");
var channelType="";

/*var token = "1517384094275458dd42c8a04d8288fa";
var organizationSeq = '4400100001';
queryType = 1;*/
//communityId = "150604355355a2012344448241088223";
//var ctmsHost = "http://118.190.8.133:8090";
//var jsonData = {
//	id: "",
//	subjectType: "1"
//};

$(function() {
	getDate(10,1);
})
var page = 1;
var pageFalse = true;
$(window).scroll(function() {
	if(($(window).height() + $(window).scrollTop()) >= $("body").height()) {
		if(pageFalse) {
			page++;
			pageFalse = false;
			console.log(page)
			$(".special-load-more").show();
			setTimeout("getDate(10,page)", 1000);
		}
	}
});

function getDate(pageSize, pageN,refreshStatus) {
	//跳转到个人中心的我的活动列表页面
	if(queryType == 3) {
		setTitle("我的活动");
		channelType=3;
		var params = {};
		params.header = {
			token: token,
			time_stamp: time_stamp
		};
		params.body = {
			pageSize: pageSize,
			pageNum: pageN ? pageN : 1
		};
		var param = JSON.stringify(params);
		$.ajax({
			type: "post",
			async: false,
			url: ctmsHost + "/ctmsApi/activity/getMyActivity?str=" + param,
			dataType: "jsonp",
			jsonp: "jsoncallback",
			jsonpCallback: "success_jsonpCallback",
			success: function(data) {
				console.log(JSON.stringify(data))
				if (refreshStatus) {
				 $('#getActivity,#getActivityStatus3').empty();

				}
				if(page == 1 && data.activityList.length == 0) {
					$(".null").show();
				} else if(data.activityList.length > 0) {
					$(".null").hide();
					var getActivityTemplate = _.template($('#getActivityTemplate').html());
					$('#getActivity').append(getActivityTemplate({
						"data": data.activityList
					}));
					//已结束数据的推进
					var getActivityStatus3 = _.template($('#getActivityStatus3Template').html());
					$('#getActivityStatus3').append(getActivityStatus3({
						"data": data.activityList
					}));
					$(".special-load-more").hide();
					echo.init();
					pageFalse = true;
				} else {
					pageFalse = false;
					$(".special-load-more").text("没有更多数据了");
					$(".special-load-more").css("color","#ccc");
					setTimeout(function() {
						//$(".special-load-more").hide();
					}, 1000);
				}
			}
		});
		//	跳转到首页圈子里的更多活动
	} else if(queryType == 2) {
		setTitle("全部活动");
		var isLink="0";
		if (lxjversonNum>=4190) {
			isLink="1";
		}
		channelType=2;
		var params = {};
		params.header = {
			token: token,
			time_stamp: time_stamp
		};
		params.body = {
			organizationSeq: organizationSeq,
			communityId: communityId,
			pageSize: pageSize,
			pageNum: pageN ? pageN : 1,
			queryType: "2",
			isLink:isLink
		};

		var param = JSON.stringify(params);
		$.ajax({
			type: "post",
			async: false,
			url: ctmsHost + "/ctmsApi/activity/getActivity?str=" + param,
			dataType: "jsonp",
			jsonp: "jsoncallback",
			jsonpCallback: "success_jsonpCallback",
			success: function(data) {
				if (refreshStatus) {
				 $('#getActivity,#getActivityStatus3').empty();

				}
				console.log((data));				
				if(page == 1 && data.activityList.length == 0) {
					$(".null").show();
				} else if(data.activityList.length > 0) {
					$(".null").hide();
					var getActivityTemplate = _.template($('#getActivityTemplate').html());
					$('#getActivity').append(getActivityTemplate({
						"data": data.activityList
					}));
					//已结束数据的推进
					var getActivityStatus3 = _.template($('#getActivityStatus3Template').html());
					$('#getActivityStatus3').append(getActivityStatus3({
						"data": data.activityList
					}));
					$(".special-load-more").hide();
					echo.init();
					pageFalse = true;
				} else {
					pageFalse = false;
					$(".special-load-more").text("没有更多数据了");
						$(".special-load-more").css("color","#ccc");
					setTimeout(function() {
						//$(".special-load-more").hide();
					}, 1000);
				}
			}
		});
		//跳转到首页活动里的更多活动
	} else {
		setTitle("全部活动");
		var isLink="0";
		channelType=2;
		if (lxjversonNum>=4100) {
			isLink="1";
		}
		var params = {};
		params.header = {
			token: token,
			time_stamp: time_stamp
		};
		params.body = {
			organizationSeq: organizationSeq,
			pageSize: pageSize,
			pageNum: pageN ? pageN : 1,
			queryType: "3",
			isLink:isLink
		};

		var param = JSON.stringify(params);
		$.ajax({
			type: "post",
			async: false,
			url: ctmsHost + "/ctmsApi/activity/getActivity?str=" + param,
			dataType: "jsonp",
			jsonp: "jsoncallback",
			jsonpCallback: "success_jsonpCallback",
			success: function(data) {
				if (refreshStatus) {
				 $('#getActivity,#getActivityStatus3').empty();

				}
				
				if(page == 1 && data.activityList.length == 0) {
					
					$(".nullTips").text("暂无活动信息")
					$(".null").show();
				} else if(data.activityList.length > 0) {
					$(".null").hide();
					var getActivityTemplate = _.template($('#getActivityTemplate').html());
					$('#getActivity').append(getActivityTemplate({
						"data": data.activityList
					}));
					//已结束数据的推进
					var getActivityStatus3 = _.template($('#getActivityStatus3Template').html());
					$('#getActivityStatus3').append(getActivityStatus3({
						"data": data.activityList
					}));
					$(".special-load-more").hide();
					echo.init();
					pageFalse = true;
				} else {
					pageFalse = false;
					$(".special-load-more").text("没有更多数据了");
						$(".special-load-more").css("color","#ccc");
					setTimeout(function() {
						//$(".special-load-more").hide();
					}, 1000);
				}
			}
		});
	}

}

function statuss(ele) {
	if(queryType == 1 || queryType == 2) {
		var jsonData = {
			eventId: "2.0-click18",
			eventName: "活动点击数"
		};
		jsonData = JSON.stringify(jsonData);
		//调用APP接口，添加百度统计
		nativeMethod("baiduStatistics", jsonData);
	} else {
		var jsonData = {
			eventId: "2.0-click51",
			eventName: "活动点击数"
		};
		jsonData = JSON.stringify(jsonData);
		//调用APP接口，添加百度统计
		nativeMethod("baiduStatistics", jsonData);
	}
	var li = $(ele);
	voteStatus = li.attr("data-voteStatus");
	voteCategory = li.attr("data-voteCategory");
	status = li.attr("data-status");
	category = li.attr("data-category");
	activityId = li.attr("data-activityId");
	//报名参团活动
	if(category == 1) {
		showActivitySpecial(ctmsHost + "/ctmsH5/offlineActivity/offered.htm?activityId=" + activityId+"&channelType="+channelType, "活动详情", 3, null);
	} else if(category == 3) {
		showActivitySpecial(ctmsHost + "/ctmsH5/offlineActivity/offlineList.htm?activityId=" + activityId+"&channelType="+channelType, "活动详情", 3, null);
	} else if(category == 2){
				console.log(ctmsHost + "/ctmsH5/offlineActivity/voteActivity.htm?activityId=" + activityId+"&channelType="+channelType)

		showActivitySpecial(ctmsHost + "/ctmsH5/offlineActivity/voteActivity.htm?activityId=" + activityId+"&channelType="+channelType, "活动详情", 3, null);
	} else if(category == 4){
		//链接活动进行中
		if(status==2){
			if (li.attr("data-urlType")=="1") {
				//内链活动跳转
				showActivity(li.attr("data-activityUrl"),"");
			} else{
				//外链活动跳转
				showActivitySpecial(li.attr("data-activityUrl"),'',6, null);			
			}
		}else{
		//链接活动已结束或者未开始
			showActivity(ctmsHost + "/ctmsH5/offlineActivity/linkAcitivity.htm?activityId=" + activityId+"&channelType="+channelType, "活动详情", 3, null);
		}
	}
}

function refreshData() {
	setTimeout(function() {
		var pageSize = Math.ceil(($(".prebig").length) / 10) * 10;
		//page = page - 1;
		getDate(pageSize, 1,true);

	}, 0);
	return 1;
}