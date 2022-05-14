var onscrollStatus = false;
var pageSize = 20;
var pageNum = 1;
var clientType = GetURLParameter("clientType");
var token = GetURLParameter("token");

/*var token = "1516257308374a6dd835b0d14d5e856d";*/


var timestamp = new Date().getTime();

var backHomePage = GetURLParameter("backHomePage"); //回退到生活首页
if(backHomePage == "1") {
	backToHomePage();
}

var vue = new Vue({
	el: '#app',
	data: {
		message: '',
		jsondate: {},
		questionList: [],
		presentTotal: 20,
		apiurl: ctmsHost + "/ctmsApi/qaCategory/getQaCategorys", //获取问题分类列表
	},
	filters: {
		//状态过滤
	},
	mounted: function() {
		this.$nextTick(function() {
			//console.log(99999999);
			setTitle("帮助与反馈");
			this.getDatejson(pageSize, 1);
		});

	},
	methods: {
		getDatejson: function(pagesize, pageNum, refreshState,flag) {		
			var timestamp = new Date().getTime();
			var params = {};
			params.header = {
				token: token,
				time_stamp: timestamp
			};
			params.body = {
				pageNum:pageNum,
				pageSize:pagesize
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
						this.questionList = response.body.list;
					} else {
						this.questionList = this.questionList.concat(response.body.list);
					}					
					this.presentTotal = Math.ceil(this.questionList.length / 20) * 20;				
				}else{
					lxjTip.msg("亲！请稍后再来！"+" "+response.body.reason,{time:100000})
				}

			}, function(response2) {
				// 响应错误回调
				onscrollStatus = true;
				console.log(JSON.stringify(response2.body))
				lxjTip.msg("亲！请稍后再来！"+" "+response2,{time:10000})
				//	console.log(JSON.stringify(response))
			});
		},
		//进入问题列表页面
		goQusetionList: function(qaCategoryId,qaCategoryTitle) {
			showActivity(ctmsHost + "/ctmsH5/user/question_list.htm?qaCategoryId="+qaCategoryId+"&qaCategoryTitle="+qaCategoryTitle, qaCategoryTitle?qaCategoryTitle:"");
		},
		goSaveFeedback: function() {
			showActivity(ctmsHost + "/ctmsH5/user/suggestion_feedback.htm","意见反馈");
		}
	}
});

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
		if(vue.questionList.length>=vue.presentTotal){
			pageNum=Math.ceil(vue.presentTotal / 20)+1;
		}else{
			pageNum=Math.ceil(vue.presentTotal / 20);
		}
		if (pageNum>1&&pageNum>Math.ceil(vue.presentTotal / 20)) {
			vue.getDatejson(20, pageNum);
		}else{
			console.log("没有更多信息")
		}
	}
};