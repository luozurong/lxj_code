var onscrollStatus = false;
var pageSize = 20;
var pageNum = 1;

var clientType = GetURLParameter("clientType");
var token = GetURLParameter("token");

//var token = "1523930410392722320170a44ba99194";

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
		presentTotal: 20,
		SpecialColumnList: [],
		showState:false,
		goSpecialindexState:false,
		apiurl: ctmsHost + "/ctmsApi/specialColumn/getMySpecialColumnList", //获取关注数据
	},
	filters: {
		//状态过滤
	},
	mounted: function() {
		this.$nextTick(function() {
			//console.log(99999999);
			this.getDatejson();
			setTitle("我的关注");
		});
	},
	methods: {
		getDatejson: function() {
			var timestamp = new Date().getTime();
			var params = {};
			params.header = {
				token: token,
				time_stamp: timestamp
			};
			params.body = {
				userAccount: ''
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
				this.showState=true;
				if(response.body.result == 0) {
					vue.SpecialColumnList = response.body.list;
					//this.questionList=response.body.list;
					/*if(refreshState) {
						this.scoreActionList = response.body.list;
					} else {
						this.scoreActionList = this.scoreActionList.concat(response.body.data);
						this.totalScore = response.body.totalScore;
						this.pageCount = response.body.pageCount;
						this.page = response.body.page;
					}
					this.presentTotal = Math.ceil(this.scoreActionList.length / 20) * 20;*/
				} else {
					lxjTip.msg("亲！请稍后再来！" + " " + response.body.reason, {
						time: 100000
					})
				}

			}, function(response2) {
				// 响应错误回调
				onscrollStatus = true;
				this.showState=true;
				console.log(JSON.stringify(response2))
				//JSON.stringify(response.body)
				/*lxjTip.msg("亲！请稍后再来！" + " " + JSON.stringify(response2), {
					time: 10000
				})*/
				//	console.log(JSON.stringify(response))
			});
		},
		goSpecialindex: function(item) {
			if (vue.goSpecialindexState) {
				return false;
			}
			vue.goSpecialindexState=true;
			setTimeout(function(){
				vue.goSpecialindexState=false;
			},1000)
			var jsonData = {
				id: item.id, //作者id
			};
			var jsonData2 = JSON.stringify(jsonData);
			nativeMethod("goAuthorPage", jsonData2);
			console.log(5555)
		},

	}
});
vue.$watch('page', function(newValue, oldValue) {
	if(vue.pageCount == vue.page) {
		vue.bottomTextState = true;
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
		vue.goSpecialindexState=false;
		vue.getDatejson();
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
		/*	if(vue.scoreActionList.length >= vue.presentTotal) {
				pageNum = Math.ceil(vue.presentTotal / 20) + 1;
			} else {
				pageNum = Math.ceil(vue.presentTotal / 20);
			}
			if(pageNum > 1 && pageNum > Math.ceil(vue.presentTotal / 20)&&pageNum<=vue.pageCount) {
				//vue.getDatejson(20, pageNum);
			} else {
				console.log("没有更多信息")
			}*/
	}
};