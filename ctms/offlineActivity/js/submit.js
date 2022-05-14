var votePicSource = '';
var votePicThumb = '';
var activityId = GetURLParameter('activityId'); //活动ID
var token = GetURLParameter('token');

function imageUpload() {
	$(".topic #evaluateText").blur();
	setTimeout(function() {
		selectPicture(1);
	}, 300)
}
//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}

function onPictureSelected(json) {
	var josnObj = JSON.parse(json);
	if(josnObj.result == '0') {
		var imageArray = josnObj.list;
		if(imageArray.length > 0) {
			for(var i = 0; i < imageArray.length; i++) {
				votePicSource = imageArray[i].o_path;
				votePicThumb = imageArray[i].t_path;
				$(".pic").attr("src", votePicSource);
				$('.picture').css('background-image', "url(" + votePicSource + ")");
				$(".upload_btn").hide();
				$(".picture").show();
			}
		}
	}
}

function delet() {
	//点击删除按钮删除上传的图片
	votePicSource = '';
	votePicThumb = '';
	$(".picture").hide();
	$(".upload_btn").show();
	$(".pic").attr("src", '');
	$('.picture').css('background-image', '');
}

//提交参选资料
function doSubmit() {
	var jsonData = {
		eventId: "2.0-click26",
		eventName: "确认提交按键点击数"
	};
	jsonData = JSON.stringify(jsonData);
	//调用APP接口，添加百度统计
	nativeMethod("baiduStatistics", jsonData);
	var time_stamp = Date.parse(new Date());
	var commentContent = $("#evaluateText").val(); //标题
	var rm1 = commentContent.replace(/\s+/g, "");
	var title = rm1.replace(/[\r\n\"]/g, "");

	//  if(!isCondition(rm2) && feedBackImages.length == 0) {
	//      layer.msg("参选失败", {
	//          time: 1500
	//      });
	//      return false;
	//  }
	//  if(!isCondition(orderNo) || !isCondition(commodityId)) {
	//      layer.msg("您已参选", {
	//          time: 1500
	//      });
	//      return false;
	//  }
	if(title == '') {
		lxjTip.msg('标题不能为空!', {
			time: 1500
		});
		return false;
	}
	if(votePicSource == '' || votePicThumb == '') {
		lxjTip.msg('请上传图片!', {
			time: 1500
		});
		return false;
	}
	$(".submit").removeAttr("onclick", "doSubmit()");
	var params = {};
	params.header = {
		token: token,
		time_stamp: time_stamp
	};
	params.body = {
		voteTitle: title,
		votePicSource: votePicSource,
		votePicThumb: votePicThumb,
		activityId: activityId,
		source: "1"
	};
	var paramData = JSON.stringify(params);
	var reqUrl = ctmsHost + "/ctmsApi/activity/submitVoteActivity?str=" + encodeURIComponent(paramData);
	$.ajax({
		type: "get",
		url: reqUrl,
		async: false,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		//timeout: 10000,
		jsonpCallback: "success_jsonpCallback",
		success: function(data2) {
			if(data2.code == 1) { //游客跳登录
				needLogin(token);
				return false;
			}
			if(data2.result == 0 && data2.code == 0) {
				lxjTip.msg("参选成功", {
					time: 1000
				});
				setTimeout(function() {
					goBack();
				}, 1500);
			} else {
				lxjTip.msg(JSON.stringify(data2.reason), {
					time: 10000
				});
				$(".submit").attr("onclick", "doSubmit()");
			}
		},
		complete: function(xhr, type) {
			$(".submit").attr("onclick", "doSubmit()");
		}
	});
}

function refreshData() {
	setTimeout(function() {

	}, 0);
	return 1;
}