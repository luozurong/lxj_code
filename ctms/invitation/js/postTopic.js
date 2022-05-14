var clientType = GetURLParameter("clientType");
var token = GetURLParameter("token");
var communityId = GetURLParameter("communityId");
var organizationSeq = GetURLParameter("organizationSeq");

var communityName = GetURLParameter("communityName") ? decodeURI(GetURLParameter("communityName")) : "未知圈子";
var timestamp = new Date().getTime();

var isShowbuttom = false;
if(sessionStorage.getItem("lxjversionsName") >= 4200) {
	setWebviewRightbuttom();
} else {
	isShowbuttom = true;
}

/*var token = "152402059921b1e14a1c78334a248103";
var communityId = "152395450434f71c1f5256204178914c";
var communityName = "全局圈子";
var organizationSeq = "4400100001"*/

var isVisitor = false;
if(token && token.indexOf("_") == 0) {
	isVisitor = true;
}
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
		communityName: communityName,
		isShowbuttom: isShowbuttom,
		isVisitor: isVisitor,
		jsonget: {},
		onfocusState: true,
		applyState: false,
		imgClickStae: true,
		applyText: '发布',
		feedBackImages: [],
		apiurl: ctmsHost + "/ctmsApi/topicSubject/addTopicSubject",
		apiurl2: ctmsHost + "/ctmsApi/community/updateAttCommunity",
	},
	filters: {
		//状态过滤

	},
	mounted: function() {
		this.$nextTick(function() {
			this.updateAttCommunity();
			//console.log(99999999);
			setTitle("发布");
			/*if(isCondition(localStorage.getItem("suggestion_feedbackMessage"))) {
				vue.message = localStorage.getItem("suggestion_feedbackMessage");
			}
			if(isCondition(localStorage.getItem("suggestion_feedbackUploadimgs"))) {
				vue.feedBackImages = JSON.parse(localStorage.getItem("suggestion_feedbackUploadimgs"));
			}*/
			setTimeout(function() {
				var text = document.getElementById("feedBackContent");
				autoTextarea(text);
			}, 300);
		});

	},
	methods: {
		updateAttCommunity: function() {
			var params2 = {};
			var timestamp = new Date().getTime();
			params2.header = {
				token: token,
				time_stamp: timestamp
			};
			params2.body = {
				status: "0", //关注
				organizationSeq:organizationSeq,
				communityIdList: [{
					communityId: communityId
				}]
			};
			var paramData3 = JSON.stringify(params2);
			console.log(paramData3)
			this.$http.jsonp(this.apiurl2 + "?str=" + encodeURI(paramData3), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(response) {
			}, function(response) {
				// 响应错误回调
			});
		},
		//发布帖子
		addTopicSubject: function() {
			if(vue.applyState) {
				return;
			}
			if(isVisitor) {
				needLogin(token);
				return false;
			}
			vue.applyState = true;
			var params2 = {};
			
			var feedBackImages = vue.feedBackImages;
			var content = vue.message.replace(/</g, "&lt;").replace(/>/g, "&gt;");
			var feedBackContent2 = trim(content);
			if(feedBackContent2.length <= 0 && feedBackImages.length <= 0) {
				lxjTip.msg("请说点什么吧");
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
			var feedBackImages2 = feedBackImages;
			if(feedBackImages2.length > 0) {
				for(var i = 0; i < feedBackImages2.length; i++) {
					feedBackImages2[i].sortNum = (Number(i) + 1);
				}
			}
			params2.body = {
				topicPictureList: JSON.stringify(feedBackImages2),
				content: content,
				communityId: communityId,
			};
			var paramData2 = JSON.stringify(params2);
			this.$http.jsonp(this.apiurl + "?str=" + encodeURIComponent(paramData2), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(response) {
				if(response.body.result == 0) {
					lxjTip.msg("发布成功");
					
					setTimeout(function() {
						vue.message="";
						vue.feedBackImages=[];
						vue.applyState = false;
						goCommunitypage();
					}, 500);				
					
				} else {
					lxjTip.msg("发布失败" + " 原因：" + response.body.reason);
					vue.applyState = false;
				}
				/*	localStorage.removeItem("suggestion_feedbackMessage");
					localStorage.removeItem("suggestion_feedbackUploadimgs");*/
			}, function(response) {
				// 响应错误回调
				lxjTip.msg("发布失败");
				vue.applyState = false;
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
			vue.onfocusState = true;
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
/*vue.$watch('message', function(newValue, oldValue) {
	localStorage.setItem("suggestion_feedbackMessage", vue.message);
});
vue.$watch('feedBackImages', function(newValue, oldValue) {
	localStorage.setItem("suggestion_feedbackUploadimgs", JSON.stringify(vue.feedBackImages));
});*/

function onPictureSelected(json) {
	var josnObj = JSON.parse(json);
	if(josnObj.result == '0') {
		var imageArray = josnObj.list;
		if(imageArray.length > 0) {
			// 获取到上传图片的路径
			for(var i = 0; i < imageArray.length; i++) {
				var itemimg = {
					picturePath: imageArray[i].o_path,
					pictureThumpath: imageArray[i].t_path,
					sortNum: 1
				}
				vue.feedBackImages.push(itemimg);
			}
		}
	}
}
function goCommunitypage(){	
	var jsonData = {
		name: communityName,
		id: communityId,
		recommendFlag:'0'
	};
	jsonData = JSON.stringify(jsonData);
	//调用APP接口，添加百度统计
	nativeMethod("goCommunityDetail", jsonData);
	//var jsonData = '{"name":"'+name+'","id":"'+id+'","recommendFlag":"'+recommendFlag+'"}';
	//nativeMethod('goCommunityDetail',jsonData);//传JsonData 原生跳转
}
//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return decodeURI(r[2]);
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

function setWebviewRightbuttom() {
	var jsonData = {
		bottomText: "发送"
	}
	jsonData = JSON.stringify(jsonData);
	nativeMethod("showRightbuttom", jsonData);
}
var RightButtomClickState = false;

function actionRightButtomClick(bottomText) {
	if(bottomText == "发送" && !RightButtomClickState) {
		RightButtomClickState = true;
		setTimeout(function() {
			RightButtomClickState = false;
		}, 3000);
		vue.addTopicSubject()
	}
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