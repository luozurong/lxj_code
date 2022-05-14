var onscrollStatus = false;
var pageSize = 20;
var pageNum = 1;
var clientType = GetURLParameter("clientType");
var token = GetURLParameter("token");
var totalScore = GetURLParameter("totalScore") ? GetURLParameter("totalScore") : "--";

//var token = "151790212474a14245800f02441eacc3";

var timestamp = new Date().getTime();

var backHomePage = GetURLParameter("backHomePage"); //回退到生活首页
if(backHomePage == "1") {
	backToHomePage();
}

var vue = new Vue({
	el: '#app',
	data: {
		bottomTextState: false,
		jsondate: {},
		scoreActionList: [],
		pageCount: 1,
		page: 1,
		totalScore: "--",
		presentTotal: 20,
		apiurl: jfmsHost + "/jfmsApi/userIntegralRecord/list", //获取问题分类列表
	},
	filters: {
		//状态过滤
	},
	mounted: function() {
		this.$nextTick(function() {
			//console.log(99999999);
			this.getDatejson(pageSize, 1);
			setTitle("积分明细");
			setTimeout(function() {
				setWebviewRightbuttom();
			}, 300)
		});
	},
	methods: {
		getDatejson: function(pagesize, pageNum, refreshState, flag) {
			var timestamp = new Date().getTime();
			var params = {};
			params.header = {
				token: token,
				time_stamp: timestamp
			};
			params.body = {
				page: pageNum,
				pageSize: pagesize
			};
			var paramData = JSON.stringify(params);
			this.$http.jsonp(this.apiurl + "?str=" + encodeURI(paramData), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(response) {
				onscrollStatus = true;
				// 响应成功回调
				console.log(JSON.stringify(response.body))
				//this.message = JSON.stringify(response.body);
				this.jsondate = response.body;
				if(response.body.result == 0) {
					//this.questionList=response.body.list;
					if(refreshState) {
						this.scoreActionList = response.body.list;
					} else {
						this.scoreActionList = this.scoreActionList.concat(response.body.data);
						this.totalScore = response.body.totalScore;
						this.pageCount = response.body.pageCount;
						this.page = response.body.page;
					}
					this.presentTotal = Math.ceil(this.scoreActionList.length / 20) * 20;
				} else {
					lxjTip.msg("亲！请稍后再来！" + " " + response.body.reason, {
						time: 100000
					})
				}

			}, function(response2) {
				// 响应错误回调
				onscrollStatus = true;
				console.log(JSON.stringify(response2))
				//JSON.stringify(response.body)
				/*lxjTip.msg("亲！请稍后再来！" + " " + JSON.stringify(response2), {
					time: 10000
				})*/
				//	console.log(JSON.stringify(response))
			});
		},

	}
});
vue.$watch('page', function(newValue, oldValue) {
	if(vue.pageCount == vue.page) {
		vue.bottomTextState = true;
	}
});

function setWebviewRightbuttom() {
	var jsonData = {
		bottomText: "积分说明"
	}
	jsonData = JSON.stringify(jsonData);
	nativeMethod("showRightbuttom", jsonData);
}
var RightButtomClickState = false;

function actionRightButtomClick(bottomText) {
	if(bottomText == "积分说明" && !RightButtomClickState) {
		RightButtomClickState = true;
		setTimeout(function() {
			RightButtomClickState = false;
		}, 3000);
		var jsonData = {
			eventId: "2.0-click77",
			eventName: "【积分明细】积分说明点击次数"
		};
		jsonData = JSON.stringify(jsonData);
		//调用APP接口，添加百度统计
		nativeMethod("baiduStatistics", jsonData);
		showActivity(ctmsHost + "/ctmsH5/integral/integral_explain.htm", "积分说明");
	}
}
//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}

function refreshData() {
	setTimeout(function() {
		//vue.getDatejson(vue.presentTotal, 1, true);
	}, 0);
	return 1;
}
//滚动条在Y轴上的滚动距离
function getScrollTop() {　　
	var scrollTop = 0,
		bodyScrollTop = 0,
		documentScrollTop = 0;　　
	if(document.body) {　　　　
		bodyScrollTop = document.body.scrollTop;　　
	}　　
	if(document.documentElement) {　　　　
		documentScrollTop = document.documentElement.scrollTop;　　
	}　　
	scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;　　
	return scrollTop;
}

//文档的总高度

function getScrollHeight() {　　
	var scrollHeight = 0,
		bodyScrollHeight = 0,
		documentScrollHeight = 0;　　
	if(document.body) {　　　　
		bodyScrollHeight = document.body.scrollHeight;　　
	}　　
	if(document.documentElement) {　　　　
		documentScrollHeight = document.documentElement.scrollHeight;　　
	}　　
	scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight;　　
	return scrollHeight;
}

//浏览器视口的高度

function getWindowHeight() {　　
	var windowHeight = 0;　　
	if(document.compatMode == "CSS1Compat") {　　　　
		windowHeight = document.documentElement.clientHeight;　　
	} else {　　　　
		windowHeight = document.body.clientHeight;　　
	}　　
	return windowHeight;
}

window.onscroll = function() {　　
	//console.log(getScrollTop()+'   '+getWindowHeight()+'   '+getScrollHeight())
	if(getScrollHeight() - getScrollTop() - getWindowHeight() < 1000 && onscrollStatus) {　
		onscrollStatus = false;
		if(vue.scoreActionList.length >= vue.presentTotal) {
			pageNum = Math.ceil(vue.presentTotal / 20) + 1;
		} else {
			pageNum = Math.ceil(vue.presentTotal / 20);
		}
		if(pageNum > 1 && pageNum > Math.ceil(vue.presentTotal / 20)&&pageNum<=vue.pageCount) {
			vue.getDatejson(20, pageNum);
		} else {
			console.log("没有更多信息")
		}
	}
};