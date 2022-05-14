/*var onscrollStatus = false;
var pageSize = 20;
var pageNum = 1;*/
var clientType = GetURLParameter("clientType");
var token = GetURLParameter("token");
var qaQuestionId = GetURLParameter("qaQuestionId");
var qaQuestionindex=GetURLParameter("qaQuestionindex")?GetURLParameter("qaQuestionindex"):1;
/*var token = "1515553693710278d7991f734a208b1f";
 */

var timestamp = new Date().getTime();

var backHomePage = GetURLParameter("backHomePage"); //回退到生活首页
if(backHomePage == "1") {
	backToHomePage();
}

var vue = new Vue({
	el: '#app',
	data: {
		qaQuestionindex: qaQuestionindex,
		qaQuestion: {},
		apiurl: ctmsHost + "/ctmsApi/qaQuestion/getQaQuestionDetail", //获取问题信息
	},
	filters: {
		//状态过滤
	},
	mounted: function() {
		this.$nextTick(function() {
			//console.log(99999999);
			setTitle("帮助与反馈");
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
				qaQuestionId: qaQuestionId
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
				if(response.body.result == 0) {
					this.qaQuestion = response.body.qaQuestion;	
				}

			}, function(response2) {
				// 响应错误回调
				onscrollStatus = true;
				console.log(JSON.stringify(response2.body))
				//	console.log(JSON.stringify(response))
			});
		},

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
/*function getScrollTop() {　　
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
		if(vue.orderList.length>=vue.presentTotal){
			pageNum=Math.ceil(vue.presentTotal / 20)+1;
		}else{
			pageNum=Math.ceil(vue.presentTotal / 20);
		}
		if (pageNum>1&&pageNum>Math.ceil(vue.presentTotal / 20)) {
		//	vue.getDatejson(10, pageNum);
		}
	}
};*/