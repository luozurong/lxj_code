/*var ctmsHost = "https://tt.hori-gz.com:8443";
var token ="1517282348840d7cb809764d40ac8e44";
var time_stamp = new Date().getTime();
var id ="1513668008886777601ccdac4ec7950b";
var organizationSeq = "4400100001";
var subjectBackId = "";
var communityName = "束带结发";
var communityId = "030ff0da8ee811e7ab5a64006a1e8b8f";*/

var token = GetURLParameter("token");
var time_stamp = new Date().getTime();
var id = GetURLParameter("id");
var organizationSeq = GetURLParameter("organizationSeq");
var subjectBackId = GetURLParameter("subjectBackId");
var communityName = "";
var communityId = "";
var lxjversionsNames = sessionStorage.getItem("lxjversionsName");

/*var id ="151738290163142a96e3d2c541fb9aff";
var organizationSeq = "4400100001";
var token ="151781377480fe2366caa48f4b48be62";*/

var pageNum = 1;
var pageSize = 10;
var isVisitor = 0;
var up_type = "1";
var isPraise = 1;
var praiseFlag = true;
var invitationId = id;
var userAccount = "";
var tokenUser = "";

function refreshData() {

	setTimeout(function() {
		ajaxData();
		createTime2 = new Date().getTime();
		accessDetailRemainTimeState=false;
	}, 0);
	return 1;
} //当返回数据，要空的时候，就用这个，不然就在里面做个数据返回

var picList = new Array();
var imgClickStae = true;
var handler = function() {
	if(!imgClickStae) {
		return false;
	}
	imgClickStae = false;
	setTimeout("imgClickStae=true", 1500);
	var imgurl = this.src;
	var indexNUm = getindex(imgurl, picList);
	var jsonData = {
		selectedIndex: indexNUm,
		picList: picList
	}
	jsonData = JSON.stringify(jsonData);
	console.log(jsonData);
	nativeMethod("showPicPreview", jsonData);
};

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

function nativeMethodFunc() {
	var jsonData = {
		itemList: [{
			id: 1,
			title: "删除",
			icon: ""
		}]
	}
	jsonData = JSON.stringify(jsonData);
	nativeMethod("showMoreMenu", jsonData);
}
function complaintNativeMethod() {
	var jsonData = {
		itemList: [{
			id: 2,
			title: "举报",
			icon: ""
		}]
	}
	jsonData = JSON.stringify(jsonData);
	nativeMethod("showMoreMenu", jsonData);
}

function actionSheetDidSelected(id) {
	if(id == 1) {
		delTopicSubject(invitationId);
	}else if(id == 2){
		showActivity(ctmsHost + "/ctmsH5/invitation/compaintList.html?topicSubjectId="+invitationId, "举报");
		
	}
}
//请求数据
function ajaxData(needshowMoreMenu) {
	var params = {};
	params.header = {
		token: token,
		time_stamp: time_stamp
	};
	params.body = {
		id: id,
		organizationSeq: organizationSeq
		//pageNum : pageNum,
		//pageSize : pageSize
	}
	var paramData = JSON.stringify(params);
	var reqUrl = '/ctmsApi/topicSubject/getTopicSubjectDetail';
	$.ajax({
		type: 'GET',
		url: ctmsHost + reqUrl + '?str=' + paramData,
		async: false,
		headers: {
			'Content-Type': 'text/html'
		},
		dataType: 'jsonp',
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			console.log(data);
			if(data.result == 0) {
				isPraise = data.isPraise;
				isVisitor = data.isVisitor;
				$("#tokenUser").val(data.tokenUser);
				$("#userAccount").val(data.userAccount);
				$("#nick").text(data.nick);
				$("#topicContent").html(htmlEncodeJQ(data.content));
				$("#createTime").text(data.createTime);
				$("#communityName").val(data.communityName);
				$("#communityId").val(data.communityId);
				if(data.content.length == 0) {
					$(".topicContentbox").hide();
				}

				communityName = data.communityName;
				communityId = data.communityId;
				userAccount = data.userAccount;
				tokenUser = data.tokenUser;

				//$("#expurgate").text("删除");

				var num = parseInt(data.praiseNum);
				if(num < 1) {
					$(".thumbs").text("点赞");
					$(".img_thumbs").attr("src", "images/ic_like_nor@3x.png");
				} else {
					$(".thumbs").text(num);
					if(data.isPraise == 1) {
						$(".img_thumbs").attr("src", "images/ic_like_pre@3x.png");
					} else {
						$(".img_thumbs").attr("src", "images/ic_like_nor@3x.png");
					}
				}

				if(data.total == 0) {
					$(".estimate").text("评论");
				} else {
					$(".estimate").text(data.total);
				}

				//发帖人的头像
				var t_headPicture = '';
				if(data.sex == '2') {
					var t_headPicture = 'images/ic_woman@3x.png'; //女
				} else {
					var t_headPicture = 'images/ic_man@3x.png'; //男
				}

				if(data.headPicture != null && data.headPicture != '') {
					t_headPicture = data.headPicture;
				}
				$("#headPicture").attr("src", t_headPicture);

				//图片
				var _html = "";
				$(".content_img").html("");

				//for (var i = 0; i < data.topicPictureList.length; i++) {
				//	_html += '<div class="background-auto" style="background-image: url('+data.topicPictureList[i].picturePath+')"><img data-preview-src="" data-preview-group="1" src="' + data.topicPictureList[i].picturePath + '"></div>';
				//}

				var pictureList = data.topicPictureList;

				_html += '<div class="img-rule3">';
				for(var p = 0; p < pictureList.length; p++) {
					_html += ' <span style="background:url(' + pictureList[p].picturePath + ') no-repeat center;background-size:cover;" ><img data-preview-src="" data-preview-group="1" src="' + pictureList[p].picturePath + '" style="opacity:0;" data-preview-src="" data-preview-group="1"></span>';
				}
				_html += '</div>';

				/*if(pictureList.length==1){
				 _html += '<div class="container" style="background: url('+pictureList[0].picturePath+') no-repeat center;background-size: cover;">';
				 _html += '	  <img data-preview-src="" data-preview-group="1" src="'+pictureList[0].picturePath+'" style="opacity: 0;display: none" >';
				 _html += '</div>';
				 }else if(pictureList.length>1 && pictureList.length<9){
				 _html += '<div class="img-rule3">';
				 for(var p = 0; p < pictureList.length; p++){
				 _html += '<span style="background:url('+pictureList[p].picturePath+') no-repeat center;background-size:cover;" ><img data-preview-src="" data-preview-group="1" src="'+pictureList[p].picturePath+'" style="opacity:0;" data-preview-src="" data-preview-group="1"></span>';
				 }
				 _html += '</div>';
				 }else if(pictureList.length==9){
				 _html += '<div class="img-rule3">';
				 for(var p = 0; p < pictureList.length; p++){
				 _html += ' <span style="background:url('+pictureList[p].picturePath+') no-repeat center;background-size:cover;" ><img data-preview-src="" data-preview-group="1" src="'+pictureList[p].picturePath+'" style="opacity:0;" data-preview-src="" data-preview-group="1"></span>';
				 }
				 _html += '</div>';
				 }*/

				$(".content_img").append(_html);

				//修改长途的样式
				onePictureCss();

				//评论
				$("#totalSubjectBack").text(data.total);
				if(data.total > 0) {
					var backHtml = '';
					$(".special-comment-empty").hide();
					for(var j = 0; j < data.backList.length; j++) {
						var headPicture = ''; //默认头像
						if(data.backList[j].back_sex == '2') {
							var headPicture = 'images/ic_woman@3x.png'; //女
						} else {
							var headPicture = 'images/ic_man@3x.png'; //男
						}

						if(data.backList[j].back_headPicture != null && data.backList[j].back_headPicture != '') {
							headPicture = data.backList[j].back_headPicture;
						}

						//评论内容只显示500个字
						var content = data.backList[j].back_content;

						if(content.length > 500) {
							content = content.substring(0, 500);
						}

						//转义，防止js注入
						content = htmlEncodeJQ(content);

						backHtml += '<div class="subjectBack" id="' + data.backList[j].back_id + '">' +
							'<input type="hidden" value="' + data.backList[j].back_account + '"></input>' +
							'<img class="special-comment-pic" src=' + headPicture + '>' +
							'<div class="special-comment-content">' +
							'<div class="special-comment-name">' +
							'<span>' + data.backList[j].back_nick + '</span>';
						if(data.backList[j].back_type == 2) {
							backHtml += '<span class="reply">&nbsp;回复了&nbsp;<span/>';
							backHtml += '<span class="second">' + data.backList[j].restored_nick + '<span/>';
						}

						backHtml += '<span class="createTime">' + data.backList[j].back_createTime + '</span>' +
							'</div>' +
							'<div class="special-comment-word" onclick="delSubject(' + "'" + data.backList[j].back_id + "'" + ',' + "'" + data.backList[j].back_account + "'" + ',' + "'" + data.id + "'" + ')">' +
							content +
							'</div>' +
							'</div>' +
							'</div>';
					}

					$(".subjectBack").remove();
					$(".special-comment-count").after(backHtml);
					$(".special-load-more").hide();

					//评论头像点击事件
					$(".special-comment-pic").each(function() {
						$(this).click(function() {
							var userAccount = $(this).prev().val();
							//调用原生方法
							var params = {
								"userAccount": userAccount
							};
							var paramData = JSON.stringify(params);
							setTimeout(function() {
								accessDetailRemainTime((new Date().getTime()) - createTime2);
							}, 0);
							nativeMethod("viewUserInfo", paramData);
						});
					});

				} else {
					$(".special-comment-empty").show();
				}

				//禁言
				if(data.talkForbidStatus == 1) {
					//禁用评论输入框
					$("#back").attr("placeholder", "您已被禁言，不能发表评论");
					$("#back").attr("disabled", true);
				}
				/*if(emptyFunc() != null ){
				 //$(".comment-over").hide();
				 $(".comment-over").show();

				 }else{
				 $(".comment-over").hide();

				 //$(".comment-over").show();
				 }*/

				//弹出功能按键 
				if(data.userAccount == data.tokenUser && needshowMoreMenu) {
					nativeMethodFunc();
				}
				if(data.userAccount != data.tokenUser && needshowMoreMenu){
					complaintNativeMethod();
				}
				emptyFunc();
				if(isCondition(subjectBackId)) {
					if($(".content_img  img").length > 0) {
						var imgNum = $('.content_img  img').length;
						$('.content_img  img').load(function() {
							if(!--imgNum) {
								jumpFunc();
							}
						});
					} else {
						jumpFunc();
					}
				}

				setTimeout(function() {
					var previewImageS = document.getElementsByClassName("content_img")[0].getElementsByTagName("img");
					for(var i = 0; i < previewImageS.length; i++) {
						picList[i] = {
							url: previewImageS[i].src
						};
						//	previewImageS[i].removeEventListener("click", handler);
						previewImageS[i].addEventListener("click", handler)
					}
				}, 500);

				bigSpecialDetail(); //大数据获取帖子详情
			} else {
				lxjTip.alert(data.reason, {
					yes: function() {
						lxjTip.close(); //如果设定了yes回调，需进行手工关闭
						goBack();
					}
				});
			}
		}
	});
}

function emptyFunc() {
	/*var emptys = document.querySelector(".special-comment-empty").style.display="none";
	 return emptys;*/
	/*	if($(".special-comment-empty").hide()){
	 $(".comment-over").show();
	 }else{
	 $(".comment-over").hide();
	 }*/
	if($(".subjectBack").length == 0) {
		$(".special-comment-empty").show();
		$(".comment-over").hide();
	} else {
		$(".comment-over").show();
		$(".special-comment-empty").hide();
	}
}

//跳转是定位评论
function jumpFunc() {
	try {
		setTimeout(function() {
			if(isCondition(subjectBackId)) {
				$('html,body').animate({
					scrollTop: $("#" + subjectBackId).offset().top
				}, 300);
			}
		}, 500);
	} catch(e) {}
}

$(function() {
    setTitle("帖子详情");
	ajaxData(true);

	//发送输入框焦点判断
	/*$("#back").focus(function(){
	 //游客获取焦点，触发登录
	 if(isVisitor==1){
	 needLogin(token);
	 }
	 });
	 //针对搜狗输入法
	 $(".special-comment-fixed input").focus(function(){
	 setTimeout(function(){
	 document.body.scrollTop = document.body.scrollHeight;
	 },100);
	 });
	 //判断发送按键的颜色
	 $('.special-comment-fixed input').bind('input propertychange', function() {
	 if(!$(this).val().trim() == ""){
	 $(".special-comment-fixed span").addClass("special-comment-active");
	 }else{
	 $(".special-comment-fixed span").removeClass("special-comment-active");
	 }
	 });*/

	//帖子头像点击事件
	$("#headPicture").click(function() {
		var userAccount = $("#userAccount").val();
		//调用原生方法
		var params = {
			"userAccount": userAccount
		};
		var paramData = JSON.stringify(params);
		setTimeout(function() {
			accessDetailRemainTime((new Date().getTime()) - createTime2);
		}, 0);
		nativeMethod("viewUserInfo", paramData);
	});

})

//我是有底线的
function ccp() {
	//没有满屏时显示更多
	/*var headerHeight = $(".special-height").height()+53;
	 console.log(headerHeight);
	 if(windowHeight > headerHeight){
	 $(".special-load-more").text("我是有底线的");
	 $(".special-load-more").show();
	 }else{
	 fullFlag = false;
	 }*/
	var subjectLength = $(".subjectBack").length;
	if(subjectLength < 10) {
		$(".special-load-more").text("我是有底线的");
		$(".special-load-more").show();
	}
}
//返回顶部
var windowHeight = $(window).height();
$(window).scroll(function() {
	if($(window).scrollTop() >= 3 * windowHeight) {
		$(".top-fixed").slideDown(100);
	} else {
		$(".top-fixed").slideUp(100);
	}
});
$(".top-fixed").click(function() {
	$("html,body").animate({
		scrollTop: 0
	}, 500);
});
//发送
function send() {
	var jsonData = {
		eventId: "2.0-click17",
		eventName: "帖子评论发送按键点击数"
	};
	jsonData = JSON.stringify(jsonData);
	//调用APP接口，添加百度统计
	nativeMethod("baiduStatistics", jsonData);
	if($("#send").hasClass("special-comment-active")) {
		var content = $("#back").val();
		//处理特殊字符
		content = encodeURIComponent(content);
		var params = {};
		params.header = {
			token: token,
			time_stamp: time_stamp
		};
		params.body = {
			parentId: id,
			type: 4,
			content: content
		}

		var paramData = JSON.stringify(params);
		var reqUrl = '/ctmsApi/subjectBack/addSubjectBack';
		$.ajax({
			type: 'GET',
			url: ctmsHost + reqUrl + '?str=' + paramData,
			async: false,
			dataType: 'jsonp',
			jsonp: "jsoncallback",
			jsonpCallback: "success_jsonpCallback",
			success: function(data) {
				if(data.result == 0) {
					//清空输入框
					$("#back").val("");
					$(".special-comment-fixed span").removeClass("special-comment-active");
					//清空评论，加载最新评论
					$(".subjectBack").each(function() {
						$(this).remove();
					});
					$(".special-comment-empty").hide();
					pageNum = 1;
					ajaxData();
				} else {

					setTimeout(function() {
						lxjTip.msg(data.reason);
					}, 600)

				}
			}
		});

	}
}

//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}

/**
 * 点击头像跳转他人主页
 * @param userAccount
 */
function goUserHomePage(userAccount) {
	//调用原生方法
	var params = {
		"userAccount": userAccount
	};
	var paramData = JSON.stringify(params);
	setTimeout(function() {
		accessDetailRemainTime((new Date().getTime()) - createTime2);
	}, 0);
	nativeMethod("viewUserInfo", paramData);
}

//转义
function htmlEncodeJQ(str) {
	return $('<span/>').text(str).html();
}

//还原
function htmlDecodeJQ(str) {
	return $('<span/>').html(str).text();
}

/**
 * 删除评论
 * @return {[type id]}
 */
function delSubject(id, backAccount, parentId) {
	var tokenUser = $("#tokenUser").val();
	if(tokenUser == backAccount) {
		lxjTip.confirm('确定删除该评论?', {
			skin: 'demo4',
			btn: ['删除', '取消'],
			yes: function(index) {

				lxjTip.close(); //如果设定了yes回调，需进行手工关闭

				var params = {};
				params.header = {
					token: token,
					time_stamp: time_stamp
				};
				params.body = {
					id: id,
					type: 4,
					parentId: parentId
				}

				var paramData = JSON.stringify(params);
				var reqUrl = '/ctmsApi/subjectBack/deleteSubjectBack';

				$.ajax({
					type: 'GET',
					url: ctmsHost + reqUrl + '?str=' + paramData,
					async: false,
					dataType: 'jsonp',
					jsonp: "jsoncallback",
					jsonpCallback: "success_jsonpCallback",
					success: function(data) {
						if(data.result == 0) {
							$("#" + id).remove();

							var num = $(".estimate").text();

							if(num == "1") {
								$(".estimate").text("评论");
								$("#totalSubjectBack").text(0);
							} else {
								$(".estimate").text(parseInt(num) - 1);
								$("#totalSubjectBack").text(parseInt(num) - 1);
							}

							/*if($(".subjectBack").length==0){
							 $(".special-comment-empty").show();
							 $(".dropload-down").hide();
							 $(".comment-over").hide();
							 }else{
							 $(".comment-over").show();
							 $(".special-comment-empty").hide();
							 }*/
							emptyFunc();
						} else {
							lxjTip.alert(data.reason, {
								yes: function() {
									lxjTip.close(); //如果设定了yes回调，需进行手工关闭
								}
							});
						}
					}

				})
			}
		});
	}
}

/**
 * 删除帖子
 */
function delTopicSubject(id) {
	//删除帖子弹出框
	lxjTip.confirm('确定删除该帖子?', {
		skin: 'demo3',
		btn: ['删除', '取消'],
		yes: function(index) {
			lxjTip.close(); //如果设定了yes回调，需进行手工关闭

			var params = {};
			params.header = {
				token: token,
				time_stamp: time_stamp
			};
			params.body = {
				id: id,
			}

			var paramData = JSON.stringify(params);
			var reqUrl = '/ctmsApi/topicSubject/delTopicSubject';

			$.ajax({
				type: 'GET',
				url: ctmsHost + reqUrl + '?str=' + paramData,
				async: false,
				dataType: 'jsonp',
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback",
				success: function(data) {
					if(data.result == 0) {
						var jsonData = {
							type: 4,
							id: id,
						};
						var jsonData2 = JSON.stringify(jsonData);
						nativeMethod("syncDataState", jsonData2);
						goBack(); //关闭当前页面
					} else {
						lxjTip.alert(data.reason, {
							yes: function() {
								lxjTip.close(); //如果设定了yes回调，需进行手工关闭
							}
						});
					}
				}

			})
		}
	});
}

//一张长图的样式
function onePictureCss() {
	var loadImgLength = $(".container img").length
	for(var i = 0; i < loadImgLength; i++) {
		$(".container img").load(function() {
			$(this).show();
			var imgWidth = $(this).width();
			var imgHeight = $(this).height();
			console.log(imgWidth + " " + imgHeight);
			var compareFlag = imgWidth / imgHeight;
			var htmlSize = parseFloat($("html").css("font-size"));

			if(compareFlag >= 1) {
				var maxWidthValue1 = imgWidth > 2.64 * htmlSize ? 2.64 * htmlSize : imgWidth
				$(this).parent().css({
					"maxWidth": maxWidthValue1,
					"maxHeight": imgHeight
				});
				console.log(maxWidthValue1);
			} else if(compareFlag >= 0.565 && compareFlag < 1) {
				var maxWidthValue2 = imgWidth > 2 * htmlSize ? 2 * htmlSize : imgWidth;
				//console.log(maxWidthValue2);
				$(this).parent().css({
					"maxWidth": maxWidthValue2,
					"maxHeight": imgHeight
				});

			} else if(compareFlag < 0.565) {
				var maxWidthValue3 = imgWidth > 1.5 * htmlSize ? 1.5 * htmlSize : imgWidth
				$(this).parent().css({
					"maxWidth": maxWidthValue3,
					"maxHeight": 1.5 * 1.7 * htmlSize
				});
			}
		})
	}
}

//点赞,取消点赞
function praise() {

	if(isVisitor == 1) { //游客点赞跳转登录
		needLogin(token);
	} else {
		if(praiseFlag) {
			praiseFlag = false;

			if(isPraise == 1) { //已经点赞
				up_type = "2";
			} else {
				up_type = "1";
			}

			var params = {};
			params.header = {
				token: token,
				time_stamp: time_stamp
			};
			params.body = {
				channel_type: "2",
				operate_type: "2",
				up_type: up_type,
				id: id,
				name: "",
				organizationSeq: organizationSeq,
				createTime: new Date().format("yyyy-MM-dd hh:mm:ss"),
				communityId: $("#communityId").val(),
				communityName: $("#communityName").val()

			}

			var paramData = JSON.stringify(params);
			var reqUrl = '/ctmsApi/home/userThumbsUpCollectServlet';

			$.ajax({
				type: 'GET',
				url: ctmsHost + reqUrl + '?str=' + paramData,
				async: false,
				dataType: 'jsonp',
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback",
				success: function(data) {
					console.log(data);
					if(data.result == 0) {

						var num = $(".thumbs").text();

						if(up_type == 1) { //点赞
							isPraise = 1;
							if(num == "点赞") {
								$(".thumbs").text("1");
							} else {
								$(".thumbs").text(parseInt(num) + 1);
							}
							$(".img_thumbs").attr("src", "images/ic_like_pre@3x.png");
						} else { //取消点赞
							isPraise = 2;
							if(num == "1" || num == "0") {
								$(".thumbs").text("点赞");
							} else {
								$(".thumbs").text(parseInt(num) - 1);
							}
							$(".img_thumbs").attr("src", "images/ic_like_nor@3x.png");
						}
					} else {
						lxjTip.msg(data.reason)
					}
					up_type == 1 ? state = 1 : state = 0;
					var jsonData = {
						type: 3,
						id: id,
						state: state,
						info: data.upNum
					};
					console.log(jsonData);
					var jsonData2 = JSON.stringify(jsonData);
					nativeMethod("syncDataState", jsonData2);

					setTimeout(function() {
						praiseFlag = true;
					}, 1500)
				},
				error: function() {
					setTimeout(function() {
						praiseFlag = true;
					}, 1500)
				}
			})
		} else {
			lxjTip.msg("请勿频繁操作");
		}
	}

}

//跳转到评论页
function goCommentList() {
	var params = {};
	params.header = {
		token: token,
		time_stamp: time_stamp
	};
	params.body = {
		id: id,
		organizationSeq: organizationSeq
	}
	var paramData = JSON.stringify(params);
	var reqUrl = '/ctmsApi/topicSubject/getTopicSubjectDetail';
	$.ajax({
		type: 'GET',
		url: ctmsHost + reqUrl + '?str=' + paramData,
		async: false,
		headers: {
			'Content-Type': 'text/html'
		},
		dataType: 'jsonp',
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			console.log(data);
			if(data.result != 0) {
				lxjTip.msg(data.reason);
				return false;
			} else {
				if(isVisitor == 1) { //游客跳登录
					needLogin(token);
				} else {
					//window.location.href= ctmsHost + '/ctmsH5/special/commentList.html?type=4&id='+id+'&token='+token;
					var url = ctmsHost + '/ctmsH5/special/commentList.html?type=4&id=' + id + '&token=' + token;
					setTimeout(function() {
						accessDetailRemainTime((new Date().getTime()) - createTime2);
					}, 0);
					showActivity(url, "评论");
				}
			}
		}
	});

}

//date时间转换yyyy-mm-dd hh:MM:ss
Date.prototype.format = function(format) {
	var args = {
		"M+": this.getMonth() + 1,
		"d+": this.getDate(),
		"h+": this.getHours(),
		"m+": this.getMinutes(),
		"s+": this.getSeconds(),
		"q+": Math.floor((this.getMonth() + 3) / 3), //quarter
		"S": this.getMilliseconds()
	};
	if(/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for(var i in args) {
		var n = args[i];
		if(new RegExp("(" + i + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
	}
	return format;
};

function creatTimes() {
	var times = new Date();
	var YY = times.getFullYear();
	var MM = times.getMonth() + 1;
	if(MM < 10) MM = "0" + MM;
	var DD = times.getDate();
	if(DD < 10) DD = "0" + DD;

	var HH = times.getHours();
	if(HH < 10) HH = "0" + HH;
	var mm = times.getMinutes();
	if(mm < 10) mm = "0" + mm;
	var ss = times.getSeconds();
	if(ss < 10) ss = "0" + ss;
	times = YY + "-" + MM + "-" + DD + " " + HH + ":" + mm + ":" + ss;
	return times;
}

/**
 * 大数据获取帖子信息
 * @return {[type]} [description]
 */
var createTime2 = new Date().getTime();
var createTime = creatTimes();
var createTime1 = createTime;

function bigSpecialDetail() {
	if(lxjversionsNames < 4200){
		return false
	}
	isVisitor == 1 ? userAccount = token : userAccount = tokenUser;
	var param = {
		body: {
			list: [{
				type: "4",
				channelType: "",
				userAccount: userAccount,
				communityId: communityId,
				communityName: communityName,
				organizationSeq: organizationSeq,
				createTime: createTime,
				topicId: id,
				topicName: ""

			}]
		},
		header: {
			time_stamp: time_stamp,
			token: token
		}
	}

	param = JSON.stringify(param);
	$.ajax({
		type: 'GET',
		url: horiBigDataHost + "/horiBigData/bigSource/accessCommunityTopicDetailServlet?str=" + param,
		dataType: "jsonp",
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success: function(data) {
			if(data.result == 0) {

			}
		}
	});
}

/*大数据活动停留时间 */

function accessDetailRemainTime(remainTime) {
	if(lxjversionsNames < 4200){
		return false
	}
	if(accessDetailRemainTimeState){
    	return false;
    }
	accessDetailRemainTimeState=true;
	try {
		isVisitor == 1 ? userAccount = token : userAccount = userAccount;
		var params = {};
		var timestamp = new Date().getTime();
		params.header = {
			token: token,
			time_stamp: timestamp
		};
		params.body = {
			list: [{
				userAccount: userAccount,
				type: 4,
				operateType: 1,
				id: id,
				name: "",
				organizationSeq: organizationSeq,
				createTime: createTime1,
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
	} catch(e) {
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