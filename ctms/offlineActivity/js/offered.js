var onscrollState = true;

var clientType = GetURLParameter("clientType");
var activityId = GetURLParameter("activityId");
var organizationSeq = GetURLParameter("organizationSeq");
var token = GetURLParameter("token");
var isShare = GetURLParameter("isShare");
var userAccount = '';

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
if(isShare == 1) {
	isShare = false;
} else {
	isShare = true;
}
var title = '';
var subTitle = '';
var indexPic = '';
setTitle("活动详情");
//var horiBigDataHost= 'https://118.190.123.78:8443';
//var token = "151781589279d586de7de74c4dd99ded";
//activityId = "151677525099bbaeea9efb4749159453";
//activityId = "1504227810124aad16bf0cf141b89f0b";//qian
//150415895756bf053a9268c041029ac7
//organizationSeq = "4400100001";
//ctmsHost="http://118.190.8.133:8090";
var timestamp = new Date().getTime();
var isVisitor = false;
if(token && token.indexOf("_") == 0) {
	isVisitor = true;
	userAccount = token;
}
var backHomePage = GetURLParameter("backHomePage"); //回退到生活首页
if(backHomePage == "1") {
	backToHomePage();
}
/*var jsondate={};
jsondate={
	userAccount:"userAccount",
	organizationSeq:"organizationSeq",
	token:"token"
}*/

var vue = new Vue({
	el: '#app',
	data: {
		jsondate: {},
		orderList: [],
		onbottomState: false,
		isSharecomeIn: isShare,
		apiurl: ctmsHost + "/ctmsApi/activity/getApplyActivity", //报名活动详情接口
		apiurl2: ctmsHost + "/mms/servlet/getOrderStatusByOrderNo", //取消订单前验证
		apiurl3: ctmsHost + "/ctmsApi/activity/addPageView", //活动浏览量统计
		apiurl4: horiBigDataHost + "/horiBigData/bigSource/accessActivityPartakeServlet", //大数据统计参与接口
		apiurl5: horiBigDataHost + "/horiBigData/bigSource/accessActivitySpecialDetailServlet", //大数据活动访问
		apiurl6: horiBigDataHost + "/horiBigData/bigSource/accessDetailRemainTimeOrLoadServlet", //大数据活动停留时间
	},
	filters: {
		//状态过滤

	},
	mounted: function() {
		this.$nextTick(function() {
			//console.log(99999999);
			setTimeout(function() {
				vue.addPageView();
			}, 500);

			this.getDatejson();
		});

	},
	methods: {
		getDatejson: function() {
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

			var paramData = JSON.stringify(params);
			this.$http.jsonp(this.apiurl + "?str=" + encodeURI(paramData), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback"
			}).then(function(response) {
				// 响应成功回调
				console.log(JSON.stringify(response.body))
				//this.message = JSON.stringify(response.bodycommodityLIst);				
				if(response.body.result == 0) {
					title = response.body.title;
					subTitle = response.body.subTitle;
					indexPic = response.body.indexPic;
					if(!isVisitor) {
						userAccount = response.body.userAccount ? response.body.userAccount : token;
					}
					setTimeout(function() {
						vue.accessActivitySpecialDetail();
					}, 600);
					this.jsondate = response.body;
					setTimeout(function() {
						mui("#activitylist_main img").each(function(i, e) {
							e.setAttribute("data-preview-src", '');
							e.setAttribute("data-preview-group", '2');
						});
					}, 500);
				} else {
					lxjTip.alert("活动信息异常");
				}

			}, function(response2) {
				// 响应错误回调
				lxjTip.alert("活动信息异常");
				console.log(JSON.stringify(response2.body))
				//	console.log(JSON.stringify(response))
			});
		},
		addPageView: function() {
			var params = {};
			var timestamp = new Date().getTime();
			params.header = {
				token: token,
				time_stamp: timestamp
			};
			params.body = {
				activityId: activityId,
			};

			var paramData = JSON.stringify(params);
			this.$http.jsonp(this.apiurl3 + "?str=" + encodeURI(paramData), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(response) {
				// 响应成功回调
				console.log(JSON.stringify(response.body))

			}, function(response2) {
				console.log(JSON.stringify(response2.body))
				//	console.log(JSON.stringify(response))
			});
		},
		//大数据活动参与
		accessActivityPartake: function(appShareType) {
			if(sessionStorage.getItem("lxjversionsName") < 4200) {
				return false;
			}
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
			var paramData = JSON.stringify(params);
			this.$http.jsonp(this.apiurl4 + "?str=" + encodeURI(paramData), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(response) {
				// 响应成功回调
				console.log(JSON.stringify(response.body))

			}, function(response2) {
				console.log(JSON.stringify(response2.body))
				//	console.log(JSON.stringify(response))
			});
		},
		//大数据活动详情
		accessActivitySpecialDetail: function() {
			if(sessionStorage.getItem("lxjversionsName") < 4200) {
				return false;
			}
			try {
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
				var paramData = JSON.stringify(params);
				this.$http.jsonp(this.apiurl5 + "?str=" + encodeURI(paramData), {
					emulateJSON: true,
					method: "get",
					dataType: "jsonp",
					jsonp: "jsoncallback",
					jsonpCallback: "success_jsonpCallback"
				}).then(function(response) {}, function(response2) {});
			} catch(e) {}
		},
		//大数据活动停留
		accessDetailRemainTime: function(remainTime) {
			if(sessionStorage.getItem("lxjversionsName") < 4200) {
				return false;
			}
			if(accessDetailRemainTimeState) {
				return false;
			}
			accessDetailRemainTimeState = true;
			try {
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
				var paramData = JSON.stringify(params);
				this.$http.jsonp(this.apiurl6 + "?str=" + encodeURI(paramData), {
					emulateJSON: true,
					method: "get",
					dataType: "jsonp",
					jsonp: "jsoncallback",
					jsonpCallback: "success_jsonpCallback"
				}).then(function(response) {}, function(response2) {});
			} catch(e) {
				//TODO handle the exception
			}
		},
		//进入确认报名页面
		goApply: function(price, isExistCommunity, code) {
			if(isVisitor) {
				needLogin(token);
				return false;
			}
			setTimeout(function() {
				vue.accessActivityPartake(1);
			}, 0);
			var jsonData = {
				eventId: "2.0-click21",
				eventName: "参与活动按键点击数"
			};
			jsonData = JSON.stringify(jsonData);
			//调用APP接口，添加百度统计
			nativeMethod("baiduStatistics", jsonData);
			setTimeout(function() {
				vue.accessDetailRemainTime((new Date().getTime()) - createTime2);
			}, 0);
			if(price > 0) {
				showActivity(ctmsHost + "/ctmsH5/offlineActivity/confirmOrder.htm?activityId=" + activityId, "确认订单");
			} else {
				showActivity(ctmsHost + "/ctmsH5/offlineActivity/submitApply.htm?activityId=" + activityId, "活动报名信息");
			}

		},
		inviteFriend: function(id, code) {
			/*if(isVisitor) {
				needLogin(token);
				return false;
			}*/
			setTimeout(function() {
				vue.accessActivityPartake(2);
			}, 0);
			var jsonData = {
				eventId: "2.0-click20",
				eventName: "邀请好友按键点击数"
			};
			jsonData = JSON.stringify(jsonData);
			//调用APP接口，添加百度统计
			nativeMethod("baiduStatistics", jsonData);
			if(title.length > 0) {
				var jsonData = {
					title: title,
					subTitle: subTitle,
					indexPic: indexPic,
					url: ctmsHost + "/ctmsH5/offlineActivity/share.htm?activityId=" + activityId
				};
				var jsonData2 = JSON.stringify(jsonData);
				var aa = nativeMethod("share", jsonData2);
				//		if (aa==1) {
				//			lxjTip.msg("成功返回1")
				//		}
			}

		},
		goShoping: function(commodityType, commodityId) {
			setTimeout(function() {
				vue.accessDetailRemainTime((new Date().getTime()) - createTime2);
			}, 0);
			if(commodityType == 1) {
				showActivity(mmsHost + "/mms/html5/supermarket/ProductDetail.htm?commodityId=" + commodityId, "商品详情");
			} else {
				showActivity(mmsHost + "/mms/html5/mall/productDetail.htm?commodityId=" + commodityId, "商品详情");
			}
		},
	}
});

window.onscroll = function() {　　
	if(onscrollState) {
		onscrollState = false;
		vue.onbottomState = true;
		//Vue.set(vue.onbottomState,0, true);
	}
};
//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return decodeURI(r[2]);
	return null;
}

function getSharedata() {
	var jsonData = {
		eventId: "2.0-click19",
		eventName: "分享按键点击数"
	};
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
			fromType: 1
		};
		/*ishoriBigData:1,//不是必填,值为1时,该分享功能需合立大数据统计
			id:activityId,//不是必填  活动ID|专题ID
			type:2  //不是必填 2:活动详情页、3:专题详情页*/
		var jsonData2 = JSON.stringify(jsonData);
		var aa = nativeMethod("share", jsonData2);
	}
}

function refreshData() {
	setTimeout(function() {
		createTime = getTime();
		createTime2 = new Date().getTime();
		accessDetailRemainTimeState = false;
		vue.getDatejson();
	}, 0);
	return 1;
}
window.onbeforeunload = onbeforeunload_handler;
window.onunload = onunload_handler;
var accessDetailRemainTimeState = false;
document.addEventListener('webkitvisibilitychange', function() {
	if(document.webkitVisibilityState == 'hidden') {
		vue.accessDetailRemainTime((new Date().getTime()) - createTime2);
	} else {}
})

function onbeforeunload_handler() {
	var warning = vue.accessDetailRemainTime((new Date().getTime()) - createTime2);
	return warning;
}

function onunload_handler() {
	vue.accessDetailRemainTime((new Date().getTime()) - createTime2);
}