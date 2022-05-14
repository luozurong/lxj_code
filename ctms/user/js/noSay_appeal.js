//var token = '201804231059163101403';
var token = GetURLParameter("token");

$(function(){
	setTitle('禁言申诉');
//  禁言状态	
	isTalkForbid();
//	申诉记录 
//	getData();
//	canSubmit()

//	输入字数统计
	$('.reason_textarea').bind('input',function(){
		var txt = $(this).val().replace(/(\r\n)|(\n)/g,'00').length;//换行占两个位
		if(txt == 101){
			txt = 100;
		}
		$('.len').text(txt);
	});

})

//判断禁言状态
function isTalkForbid(){
	var time_stamp = new Date().getTime().toString();
	var params = {};
	params.header = {
		token : token,
		time_stamp : time_stamp
	};
	params.body = {
		
	};
	var paramData = JSON.stringify(params);
	var reqUrl = '/uums/servlet/searchUserInfoServlet';
	
	console.log(uumsHost + reqUrl+ '?str=' + paramData);
	
	$.ajax({
        type: 'GET',
        url : uumsHost + reqUrl+ '?str=' + paramData ,
        dataType: 'jsonp',
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
        success: function(data){
        	console.log(data)
        	if(data.result == 0){
        		var talkForbidStatus = data.obj.talkForbidStatus;
        		if(talkForbidStatus == 0){//未被禁言 不用申诉
        			waiting('您无需申诉，快去畅所欲言吧！');
        		}
    			getData(talkForbidStatus);
        		
        	}
        },
        error:function(xhr,type){
        }
	});
}

//申诉记录
function getData(talkForbidStatus){
	var time_stamp = new Date().getTime().toString();
	var params = {};
	params.header = {
		token : token,
		time_stamp : time_stamp
	};
	params.body = {
		
	};
	var paramData = JSON.stringify(params);
	var reqUrl = '/ctmsApi/appealReview/getAppealReviewList';
	
	console.log(ctmsHost + reqUrl+ '?str=' + paramData)
	$.ajax({
        type: 'GET',
        url : ctmsHost + reqUrl+ '?str=' + paramData ,
        dataType: 'jsonp',
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
        success: function(data){
        	console.log(data);
        	if(data.result == 0){//正确返回
        		var listLen = data.list.length;
        		
        		//不管状态 先拿记录
        		if(listLen > 0){
        			$('.record_list').show();
	            	$('html').css('background-color','#F4F5F6');
	            	$('.no_record').hide();
        			$('.record_list').html('');
	                for(var i=0; i<listLen; i++){
						var list = data.list[i];
	//					模板推进数据
						var recordTemplate = _.template($('#recordTemplate').html());
						$('.record_list').append(recordTemplate(list))
	                }
        		}else{// 如果没有数据
	            	$('.record_list').hide();
	            	$('.no_record').show();
	            	$('html').css('background-color','#FFFFFF');
        		}
        		
//      		判断禁言状态
        		if(talkForbidStatus != 0){//被禁言 需要申诉
        			if(listLen == 0){//没有审核中
		        		canSubmit();//可以提交
					}else if(listLen > 0){
			        	if(data.list[0].state == 0){//审核中
			        		waiting('您的申诉正在审核中，请耐心等待！');//不可提交 
			        	}else{//否则
			        		canSubmit();//可以提交
			        	}
		            }
        		}
        		
        	}else{
        		console.log(data.reason);//错误信息
        	}
            
        },
        complete :function(xhr,type){
        },
        error: function(xhr, type){
        }
    });
}

//审核中
function waiting(str){
	$('.content_reason').hide();
	$('.waiting').show();
	$('.waiting').text(str);
}
//可以提交
function canSubmit(){
	$('.waiting').hide();
	$('.content_reason').show();
//	提交
	
	$('.submit').click(function(){
		if($('.submit').hasClass('yes')){//禁止多次提交
			$(this).removeClass('yes').addClass('no');
			var text = $('.reason_textarea').val();
			var reason = text.replace(/(^\s+)|(\s+$)/g, "");//去空格
			if(reason ==''){
				$('.submit').removeClass('no').addClass('yes');
				lxjTip.msg('申诉理由不能为空！',{time:1500});
			}else{
				var time_stamp = new Date().getTime().toString();
				var params = {};
				params.header = {
					token : token,
					time_stamp : time_stamp
				};
				params.body = {
					reasonDetail : encodeURIComponent(reason)
				};
				
				var paramData = JSON.stringify(params);
				var reqUrl = '/ctmsApi/appealReview/addAppealReviewfo';
				console.log(ctmsHost + reqUrl +'?str='+ paramData)
	
				$.ajax({
					type: 'post',
					url: ctmsHost + reqUrl +'?str='+ paramData,
	//				async: false,
					dataType: 'jsonp',
					jsonp: "jsoncallback",
					jsonpCallback: "success_jsonpCallback",
					beforeSend: function(){
	//					console.log('提交前操作...')
					},
					success: function(data){
						$('.reason_textarea').val('');//清空输入框
						console.log(data);
						if(data.result == 0){
	//						console.log('提交成功!');
							lxjTip.alert('提交成功!', {
								yes: function() {
									lxjTip.close(); //如果设定了yes回调，需进行手工关闭
									getData();
								}
							});
						}else{
	//						console.log(data.reason);//错误内容
							lxjTip.alert(data.reason, {
								yes: function() {
									lxjTip.close(); //如果设定了yes回调，需进行手工关闭
	//								getData();
									isTalkForbid();
								}
							})
						}
						$('.submit').removeClass('no').addClass('yes');
					},
					error: function(XHR, TS){
					},
					complete: function (XHR, TS){
	//					console.log('结束操作...')
						$('.submit').removeClass('no').addClass('yes');
					}
				})
			}
			
		}
			
	});
	
	
}
