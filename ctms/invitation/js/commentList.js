/*var id = "1504663824403e2be5da02ca405eae1b";
var ctmsHost = "http://192.168.51.24:8090";
var token = "15123495949322952e4f7ec5479a9fa3"; 
var time_stamp = new Date().getTime();*/


var id = GetURLParameter("id");
var token = GetURLParameter("token");
var time_stamp = new Date().getTime();


var dataValueLength = "";
var restoredFlag = false;
var restoredAccount = "";
var restoredId = "";
var visitor = 0;
//评论列表
function commentList(){
	var paramsClick = {
	    header: {
	        time_stamp: time_stamp,
	        token: token
	    },	    
		body:{
			id:id,
			type:2
		}
	}
	var param = JSON.stringify(paramsClick);
	$.ajax({
		type: 'GET',
		url: ctmsHost + "/ctmsApi/subjectBack/subjectBackList?str="+param, 
		dataType: "jsonp",
        jsonp: "jsoncallback",
        jsonpCallback: "success_jsonpCallback",
		success: function(data){
			console.log(data);
			/*if(data.visitor == 1){
				visitor = 1;
			}*/
			$(".comment-wrap-all").html("");
			$(".comment-num p span").text(data.list.length);
			if(data.list.length != 0){
				for(var i = 0;i<data.list.length; i++){
					data.list[i].content = htmlEncodeJQ(data.list[i].content);
				}
				var tEmpty=_.template($("#commentItem").html());
				$('.comment-wrap-all').append(tEmpty({"data":data.list}));
			}else{
				var tEmpty=_.template($("#special_comment_empty").html());
				$('.comment-wrap-all').append(tEmpty("暂时还没有评论，快点来抢沙发！"));
		        		
			}
		}
	});	
}
commentList();

$('.comment-fixed-left div').bind('keydown', function (e) {
    var key = e.which;
    if (key == 13 && $('.comment-fixed-left div').text("") != "") {
        e.preventDefault();
        commentSubmit();
        $('.comment-fixed-left div').text("");
    }

});
function commentClik(e){
	restoredFlag = true;
	restoredId = $(e).attr("data-val");
	restoredAccount = $(e).attr("data-value");
	dataValueLength = restoredAccount.length+3;
	$(".comment-fixed-left div").text("");
	$(".comment-fixed-left div").append("<span style='color:#666'>回复:" + restoredAccount+ ":</span>");
	$(".comment-fixed-left div").focus();
	setFocus($(".comment-fixed-left div"));
}
$(".comment-fixed-left div").css("width",$(".comment-fixed-left div").width());


function myEditChange(){
	if(restoredFlag){
		if($(".comment-fixed-left div").text().trim() == "" || $(".comment-fixed-left div").text().trim().length == dataValueLength+1){
    		$(".comment-fixed-right").removeClass("comment-fixed-active");
	    }else{
	    	$(".comment-fixed-right").addClass("comment-fixed-active");
	    }
	}else{
		if($(".comment-fixed-left div").text().trim() == ""){
    		$(".comment-fixed-right").removeClass("comment-fixed-active");
	    }else{
	    	$(".comment-fixed-right").addClass("comment-fixed-active");
	    }
	}
    
    if(restoredFlag && $(".comment-fixed-left div").text().trim().length == dataValueLength){
    	$(".comment-fixed-left div").text("");
    	restoredFlag = false;
    	restoredAccount = "";
    	$(".comment-fixed-right").removeClass("comment-fixed-active");
    }
}
$(".comment-fixed-left div").focus(function(){
	if(visitor == 1){
		needLogin(token);
		return false;
	}
});
//提交评论
function commentSubmit(){
	if(visitor == 1){
		needLogin(token);
		return false;
	}
	if($(".comment-fixed-right").hasClass("comment-fixed-active")){
		var content = "";
		var backType = "";
		if(restoredFlag){
			content = $(".comment-fixed-left div").text().substr(dataValueLength+1,$(".comment-fixed-left div").text().length);
			backType = 2;
		}else{
			content = $(".comment-fixed-left div").text();
			restoredId = "";
			restoredAccount:"";
			backType = 1;
		}
		var param = {
			body: {
				parentId:id,
				type:2,
				content:content,
				restoredId:restoredId,
				restoredAccount:restoredAccount,
				backType:backType
			},
			header: {
				token: token,
				time_stamp: time_stamp
			}
		}
		var paramString = JSON.stringify(param);
		$.ajax({
			type: 'GET',
			url: ctmsHost + "/ctmsApi/subjectBack/addSubjectBack?str="+paramString, 
			dataType: "jsonp",
	        jsonp: "jsoncallback",
	        jsonpCallback: "success_jsonpCallback",
			success: function(data){
				console.log(data);
				if(data.result == 0){
					$(".comment-fixed-left div").text("");
					$(".comment-fixed-right").removeClass("comment-fixed-active");
					commentList();
				}else{
					lxjTip.msg(data.reason);
				}
				
			}
		});	

	}	
}

//删除评论 
function deleteComment(backId,parentId){
	lxjTip.confirm('确定删除该评论?', {
		skin: 'demo4',
		btn:['删除', '取消'],
		yes: function(index) {
			
			lxjTip.close(); //如果设定了yes回调，需进行手工关闭
			
			var params = {};
			params.header = {
					token: token,
					time_stamp: time_stamp
			};
			params.body = {
				id: backId,
				type: 2,
				parentId:parentId
			}
				
			var paramData = JSON.stringify(params);
			var reqUrl = '/ctmsApi/subjectBack/deleteSubjectBack';
			$.ajax({
		        type: 'GET',
		        url: ctmsHost + reqUrl + '?str=' + paramData,
		        async:false,
		        dataType: 'jsonp',
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback",
		        success: function(data){
		        	if(data.result == 0){
		        		$("#"+backId).remove();
		        		var num = $(".comment-num p span").text();
		        		$(".comment-num p span").text(parseInt(num)-1);

		        		if($(".comment-wrap-all>div").length-1 == 0){
		        			$(".comment-wrap-all").html("");
		        			var tEmpty=_.template($("#special_comment_empty").html());
							$(".comment-wrap-all").append(tEmpty("暂时还没有评论，快点来抢沙发！"));
		        		}
		        	}else{
		        		lxjTip.alert(data.reason,{
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

//评论固定栏
setInterval(function(){
 	document.body.scrollTop = document.body.scrollHeight; 
},1000);
//软键盘弹出后，点击其他地方，软键盘隐藏
$(".comment").on("touchstart",function(){
	$(".comment-fixed-left input").blur();
});

//光标移到最后
function setFocus(el) {
    el = el[0]; // jquery 对象转dom对象  
    el.focus();
    var range = document.createRange();
    range.selectNodeContents(el);
    range.collapse(false);
    var sel = window.getSelection();
    //判断光标位置，如不需要可删除
    if(sel.anchorOffset!=0){
        return;
    };
    sel.removeAllRanges();
    sel.addRange(range);
};


//通过点击头像跳转
function goToUser(ele){
	var userAccount=String($(ele).attr("useraccount"));
	//调用原生方法
	var params = {"userAccount":userAccount.toString()};
	var paramData = JSON.stringify(params);
	nativeMethod("viewUserInfo", paramData);
}

//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}


//转义
function htmlEncodeJQ ( str ) {
	return $('<span/>').text( str ).html();
}