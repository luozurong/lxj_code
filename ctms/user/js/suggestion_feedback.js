var clientType = GetURLParameter("clientType");
var token = GetURLParameter("token");
var timestamp = new Date().getTime();

//orderNo = "201703171130269949664";
//var token = "151693970420e255b3a762774dac87e6";
//var host = "http://118.190.8.133:8090";

var backHomePage = GetURLParameter("backHomePage"); //回退到生活首页
if(backHomePage == "1") {
	backToHomePage();
}

function getindex(item, items) {
	var index = 0;
	for(var i = 0; i < items.length; i++) {
		if(items[i].url == item) {
			index = i;
			break;
		}
	}
	return index;
}

var vue = new Vue({
	el: '#app',
	data: {
		message: '',
		jsonget: {},
		onfocusState: true,
		applyState: false,
		imgClickStae: true,
		applyText: '提交',
		feedBackImages: [],
		apiurl: ctmsHost + "/ctmsApi/feedback/saveFeedback", //
	},
	filters: {
		//状态过滤

	},
	mounted: function() {
		this.$nextTick(function() {
			//console.log(99999999);
			setTitle("意见反馈");
			if(isCondition(localStorage.getItem("suggestion_feedbackMessage"))) {
				vue.message = localStorage.getItem("suggestion_feedbackMessage");
			}
			if(isCondition(localStorage.getItem("suggestion_feedbackUploadimgs"))) {
				vue.feedBackImages = JSON.parse(localStorage.getItem("suggestion_feedbackUploadimgs"));
			}
			setTimeout(function() {
				var text = document.getElementById("feedBackContent");
				autoTextarea(text);
			}, 300);
		});

	},
	methods: {
		//订单操作
		updateOrderFlag: function() { //
			if(vue.applyState) {
				return;
			}
			vue.applyState = true;
			var params2 = {};
			var feedBackImages = vue.feedBackImages;
			var feedBackContent = vue.message.replace(/</g, "&lt;").replace(/>/g, "&gt;");
			var feedBackContent2 = trim(feedBackContent);
			if(feedBackContent2.length <= 0) {
				lxjTip.msg("内容不能为空");
				vue.applyState = false;
				setTimeout(function() {
					//document.getElementById("feedBackContent").focus();
				}, 1000);
				return false;
			}
			console.log(5)
			var timestamp = new Date().getTime();
			params2.header = {
				token: token,
				time_stamp: timestamp
			};
			//JSON.parse(JSON.stringify(feedBackImages).replace(/picturePath/g, "url"))
			params2.body = {
				topicPictureList: JSON.stringify(feedBackImages).replace(/o_path/g, "picturePath").replace(/\"/g, "'"),
				content: feedBackContent
			};
			var paramData2 = JSON.stringify(params2);
			this.$http.jsonp(this.apiurl + "?str=" + encodeURI(paramData2), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(response) {
				if(response.body.result == 0) {
					lxjTip.msg("感谢您的反馈，我们会尽快处理");
					setTimeout(function() {
						goBack();
					}, 2000);
				} else {
					lxjTip.msg("提交失败" + " 原因：" + response.body.reason);
					vue.applyState = false;
				}
				localStorage.removeItem("suggestion_feedbackMessage");
				localStorage.removeItem("suggestion_feedbackUploadimgs");
			}, function(response) {
				// 响应错误回调
				lxjTip.msg("提交失败");
				vue.applyState = false;
				console.log("失败")
			});
		},
		imgClick: function(url) {
			window.event.cancelBubble = true;
			var picList = JSON.parse(JSON.stringify(vue.feedBackImages).replace(/picturePath/g, "url"))
			if(!vue.imgClickStae) {
				return false;
			}
			vue.imgClickStae = false;
			setTimeout("vue.imgClickStae=true", 1500);
			var imgurl = url;
			var indexNUm = getindex(imgurl, picList);
			var jsonData = {
				selectedIndex: indexNUm,
				picList: picList
			}
			jsonData = JSON.stringify(jsonData);
			console.log(jsonData);
			nativeMethod("showPicPreview", jsonData);
		},
		//点击上传图片按钮
		imageUpload: function() {
			var picturenum = vue.feedBackImages.length;
			var maxPicture = 9 - parseInt(picturenum);
			if(maxPicture > 0) {
				selectPicture(maxPicture);
			}
		},
		//点击删除图片按钮
		delePicture: function(url) {
			window.event.cancelBubble = true;
			var upLoadimglenght = vue.feedBackImages.length;
			for(var i = 0; i < upLoadimglenght; i++) {
				if(vue.feedBackImages[i].picturePath == url) {
					vue.feedBackImages.splice(i, 1);
					break;
				}
			}
		},
		//点击书写内容时
		onfocusopen: function(url) {
			vue.onfocusState = false;
		},
		onbluropen: function(url) {
			setTimeout('vue.onfocusState = true',300);
		},
		newviewopen: function() {
			window.event.cancelBubble = true;
			var eleId = 'feedBackContent';
			setTimeout(function() {
				var viewBottom = window.innerHeight;
				var weizhi;
				//输出viewBottom
				var element = document.getElementById(eleId);
				var getElementPosition = function(elem) {
					var defaultRect = {
						top: 0,
						left: 0
					};
					weizhi = elem.getBoundingClientRect();
				}
				getElementPosition(element);
				var elementBottom = weizhi.bottom;
				if(viewBottom >= elementBottom) {} else {
					var vuleheight = elementBottom - viewBottom + 50;
					window.scrollTo(0, vuleheight);
				}
			}, 300);
		},

	}
});
vue.$watch('message', function(newValue, oldValue) {
	localStorage.setItem("suggestion_feedbackMessage", vue.message);
});
vue.$watch('feedBackImages', function(newValue, oldValue) {
	localStorage.setItem("suggestion_feedbackUploadimgs", JSON.stringify(vue.feedBackImages));
});

function onPictureSelected(json) {
	var josnObj = JSON.parse(json);
	if(josnObj.result == '0') {
		var imageArray = josnObj.list;
		if(imageArray.length > 0) {
			// 获取到上传图片的路径
			for(var i = 0; i < imageArray.length; i++) {
				var itemimg = {
					picturePath: imageArray[i].o_path
				}
				vue.feedBackImages.push(itemimg);
			}
		}
	}
}
//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}

function lTrim(str) {
	if(str.charAt(0) == " ") {
		str = str.slice(1);
		str = lTrim(str);
	}
	return str;
}

function rTrim(str) {
	var iLength;
	iLength = str.length;
	if(str.charAt(iLength - 1) == " ") {
		str = str.slice(0, iLength - 1);
		str = rTrim(str);
	}
	return str;
}

function trim(str) {
	return lTrim(rTrim(str));
}

function refreshData() {
	setTimeout(function() {

	}, 0);
	return 1;
}

var wHeight = document.documentElement.clientHeight;
window.onresize = function() {
	var newHeight = document.documentElement.clientHeight;
	if(clientType == "ios") {
		return false;
	}
	if(newHeight >= wHeight) {
		document.getElementById("feedBackContent").blur();
		vue.onfocusState = true;
	} else {
		vue.onfocusState = false;
	}
}