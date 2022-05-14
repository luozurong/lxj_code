/*var ctmsHost = 'http://192.168.51.24:8090';
var token = "150820276435e0dd540d16254aac926b";*/
var token =GetURLParameter("token");
var time_stamp = new Date().getTime();
var type = '1';
var pageNum = 1;
var pageSize = 5;
var totalPage = 1;
var pageFlag = true;
var topicFlag = true;
var firstPage = true;
var dropload = null;
var topicSubjectId = '';
var dom = '';
var sendFlag = true;
var otherFlag = false;
var dropload;
$(function(){ 
	//tabs
	$(".topic-header>div").click(function(){
        $(this).addClass("topic-header-active").siblings().removeClass("topic-header-active");
        $(".lists").html("");
        type = $(this).index()+1+'';
        pageNum = 1;
        totalPage = 1;
        ajaxData();
    })
	 dropload= $('.header').dropload({
	     domUp : {
            domClass   : 'dropload-up',
            // 下拉过程显示内容
            domRefresh : '<div class="dropload-refresh">↓下拉刷新</div>',
            // 下拉到一定程度显示提示内容
            domUpdate  : '<div class="dropload-update">↑释放更新</div>',
            // 释放后显示内容
            domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
	      },
        domDown : {
            domClass   : 'dropload-down',
            // 滑动到底部显示内容
            domRefresh : '<div class="dropload-refresh"></div>',
            // 内容加载过程中显示内容
            domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
            // 没有更多内容-显示提示
            domNoData  : '<div class="dropload-noData">我是有底线的</div>'
        },
	     loadUpFn:function(me){//刷新
	    	$(".lists").html("");
	    	 pageNum = 1;
	    	 var params = {};
	    		params.header = {
	    			token : token,
	    			time_stamp : time_stamp
	    		};
	    		params.body = {
	    			type : type,
	    			pageNum : pageNum,
	    			pageSize : pageSize
	    		}
	    		var paramData = JSON.stringify(params);
	    		var reqUrl = '/ctmsApi/topicSubject/getParticipateInPostList';
	    	    $.ajax({
	    	        type: 'GET',
	    	        url: ctmsHost + reqUrl + '?str=' + paramData,
	    	        //async:false,
	    	        dataType: 'jsonp',
	    			jsonp: "jsoncallback",
	    			jsonpCallback: "success_jsonpCallback",
	    	        success: function(data){
	    	        	if(data.result == 0){
	    	        		totalPage = data.totalPage;
	    	        		$("#all").text(data.all);
	    	        		$("#publish").text(data.publish);
	    	        		$("#comment").text(data.comment);
	    	        		
	    	        		//迭代数据,获取html
    	            		var _html =getHtml(data);
    	            		if(_html==''){
    	            			_html = dataNull();
    	            		}
    	        			
    	        			$(".lists").html(_html);
    	        			
    	        			//一张图片的时候调用onePictureCss方法修改样式
    	        			onePictureCss();
    	        			
    	        			//没有评论会有个div的颜色不一样，直接将颜色改成白色就看不出
	    	        		$(".topic-comment").each(function(){
	    	        			if($(this).children().children().length==0){
	    	        				//本来颜色#f3f4f5
	    	        				$(this).css("background","#ffffff");
	    	        			}
	    	        		});
    	        			
    	        			// 每次数据加载完，必须重置
	    	        		me.resetload();	
	    	   	    	 	if(data.totalPage <= pageNum){
	    	   	    	 		me.lock('down');
	    	   	    	 		//me.noData();
	    	   	    	 	}else{
	    	   	    	 		me.unlock();
	    	   	    	 		me.noData(false)
	    	   	    	 	}
	    	        	}else{
	    	        		lxjTip.alert(data.reason,{
	    	        			yes: function() {
	    							lxjTip.close(); //如果设定了yes回调，需进行手工关闭
	    						}
	    	        		});
	    	        		// 每次数据加载完，必须重置
	    	        		me.resetload();
	    	        	}
	    	        }
	    	    });
	     },
	     loadDownFn : function(me){//加载数据
	    	
	    	if(!firstPage){
	    		 pageNum++;
	    	 }else{
	    		 firstPage = false;
	    	 }
	    	 var params = {};
	    		params.header = {
	    			token : token,
	    			time_stamp : time_stamp
	    		};
	    		params.body = {
	    			type : type,
	    			pageNum : pageNum,
	    			pageSize : pageSize
	    		}
	    		var paramData = JSON.stringify(params);
	    		var reqUrl = '/ctmsApi/topicSubject/getParticipateInPostList';
	    	    $.ajax({
	    	        type: 'GET',
	    	        url: ctmsHost + reqUrl + '?str=' + paramData,
	    	        async:false,
	    	        dataType: 'jsonp',
	    			jsonp: "jsoncallback",
	    			jsonpCallback: "success_jsonpCallback",
	    	        success: function(data){
	    	        	if(data.result == 0){
	    	        		totalPage = data.totalPage;
	    	        		$("#tokenUser").val(data.tokenUser);
	    	        		$("#all").text(data.all);
	    	        		$("#publish").text(data.publish);
	    	        		$("#comment").text(data.comment);
	    	        		
	    	        		var _html ='';
	    	        		if(data.list.length>0){
	    	        			//迭代数据,获取html
	    	            		_html =getHtml(data);
		    	        		$(".lists").append(_html);
		    	        		
		    	        		//一张图片的时候调用onePictureCss方法修改样式
	    	        			onePictureCss();
		    	        		
		    	        		//没有评论会有个div的颜色不一样，直接将颜色改成白色就看不出
		    	        		$(".topic-comment").each(function(){
		    	        			if($(this).children().children().length==0){
		    	        				//本来颜色#f3f4f5
		    	        				$(this).css("background","#ffffff");
		    	        			}
		    	        		});
		    	        		
		    	        		// 每次数据插入，必须重置
		                        me.resetload();
	    	        		}else{
	    	        			
	    	        			if($(".lists").children().length==0){
	    	        				_html = dataNull();
	    	        				$(".lists").html(_html);
	    	        			}
	    	        			// 锁定
	                            //me.lock();
	                            me.noData();
		                        cssFuc();
	                            // 每次数据插入，必须重置
		                        me.resetload();
	    	        		}
	    	        		firstPage = false;
	    	        	}else{
	    	        		lxjTip.alert(data.reason,{
	    	        			yes: function() {
	    							lxjTip.close(); //如果设定了yes回调，需进行手工关闭
	    						}
	    	        		});
	    	        	}
	    	        }
	    	    });   	
	     }
	 });
   
 })
 
 
 //请求数据
function ajaxData(){
	$(".dropload-down").children().remove();
	$(".dropload-down").append('<div class="dropload-load"><span class="loading"></span>加载中...</div>');
	var params = {};
	params.header = {
		token : token,
		time_stamp : time_stamp
	};
	params.body = {
		type : type,
		pageNum : pageNum,
		pageSize : pageSize
	}
	var paramData = JSON.stringify(params);
	var reqUrl = '/ctmsApi/topicSubject/getParticipateInPostList';
    $.ajax({
        type: 'GET',
        url: ctmsHost + reqUrl + '?str=' + paramData,
        //async:false,
        dataType: 'jsonp',
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
        success: function(data){
        	console.log(data);
        	if(data.result == 0){
        		$(".lists").html("");
        		totalPage = data.totalPage;
        		$("#all").text(data.all);
        		$("#publish").text(data.publish);
        		$("#comment").text(data.comment);
        		//迭代数据,获取html
        		var _html =getHtml(data);
        		if(_html==''){
        			_html = dataNull();
        		}
        		
        		$(".lists").append(_html);
        		
        		//一张图片的时候调用onePictureCss方法修改样式
    			onePictureCss();
        		
        		//没有评论会有个div的颜色不一样，直接将颜色改成白色就看不出
        		$(".topic-comment").each(function(){
        			if($(this).children().children().length==0){
        				//本来颜色#f3f4f5
        				$(this).css("background","#ffffff");
        			}
        		});
        		
        		if(totalPage <= pageNum){
                	dropload.lock('down');
                	cssFuc();
                	//dropload.noData();
                	// 重置
                    dropload.resetload();
                    //$(".dropload-noData").html("");
           	 	}else{
           	 		dropload.unlock();
           	 		dropload.noData(false)
           	 		// 重置
                    dropload.resetload();
           	 	}
                
        	}else{
        		lxjTip.alert(data.reason,{
        			yes: function() {
						lxjTip.close(); //如果设定了yes回调，需进行手工关闭
					}
        		});
        	}
        }
    });
}

function cssFuc(){
	if($(".lists").children().eq(0).hasClass("null")){
    	$(".dropload-down").hide();
    	$(".topic-content").css("minHeight","6.74rem");
    }else{
    	$(".dropload-down").show();
    	$(".topic-content").css("minHeight","");
    }
}
/**
 * 解析json,拼接html
 * @param data
 */
function getHtml(data){
	var _html = '';
	for (var i = 0; i < data.list.length; i++) {
			
			var headPicture = 'images/ic_default_avatar.png';
			if(data.list[i].type==1){//帖子
				_html += '<div class="topic-content-item" id='+"'"+data.list[i].topicSubjectId+"'"+'>'
								+'<div class="topic-item">';
				//头像
				if(data.list[i].topicHeadPicture!=null&&data.list[i].topicHeadPicture!=''){
					headPicture = data.list[i].topicHeadPicture;
				}
				_html +='<img src="'+headPicture+'"  onclick="goUserHomePage('+"'"+data.list[i].topicAccount+"'"+')">'
						+'<div>'
							+'<div>'
							+'	<span>'+data.list[i].topicNick+'</span>'
							+'  <span class="special-time">'+data.list[i].topicCreateTime+'</span>'
							+'</div>'
							+'<div>'
							+'	<span>来自·'+data.list[i].topicCommunityName+'</span>';
				if(data.list[i].topicDel=='0'){
					 _html +='  <span class="special-canncel" onclick="delTopicSubject('+"'"+data.list[i].topicSubjectId +"'"+')">删除<span>';
				}
				_html +=	 '</div>'
						+'</div>'
						
					+'</div>'
					+'<div class="topic-main">'
					+'<div class="topic-main-word" onclick="goDetial('+"'"+data.list[i].topicSubjectId+"'"+',1)"><pre>'+htmlEncodeJQ(data.list[i].topicContent)+'</pre></div>'
					+'<div class="topic-main-pic">';
				//图片
				var pictureList = data.list[i].topicPictureList;
				/*for(var j = 0; j < pictureList.length; j++) {
					if (pictureList.length == 1) {

						_html += '<div class="background-autoa" onclick="goDetial(' + "'" + data.list[i].topicSubjectId + "'" + ',1)" style="background-image: url(' + pictureList[j].picturePath + ')"></div>';
					}else{
						_html += '<div class="background-autob" onclick="goDetial(' + "'" + data.list[i].topicSubjectId + "'" + ',1)" style="background-image: url(' + pictureList[j].picturePath + ')"></div>';
					}
				}*/
				
				if(pictureList.length==1){
					_html += '<div class="container" style="background: url('+pictureList[0].picturePath+') no-repeat center;background-size: cover;">';
					_html += '	  <img src="'+pictureList[0].picturePath+'" style="opacity: 0;display: none">';
					_html += '</div>';
				}else if(pictureList.length>1 && pictureList.length<4){
					_html += '<div class="img-rule2">';
					for(var i = 0; i < pictureList.length; i++){
			            _html += " <span style='background:url("+pictureList[i].picturePath+") no-repeat center;background-size:cover;'></span>";
			        }
					_html += '</div>';
				}else if(pictureList.length==4){
					_html += '<div class="img-rule3">';
					for(var i = 0; i < pictureList.length; i++){
			            _html += " <span style='background:url("+pictureList[i].picturePath+") no-repeat center;background-size:cover;'></span>";
			        }
					_html += '</div>';
				}
				
				$(".topic-pic-two").append(_html);

				_html +='</div>'
					+'</div>'
					+'<div class="topic-comment">'
						+'<div class="topic-comment-item">';
				
				//评论
				var subjectBack = data.list[i].topicSubjectBack;
				for(var k = 0; k < subjectBack.length; k++){
					_html +='<div class="topic-comment-wrap" onclick="delSubject('+"'"+subjectBack[k].backId+"'"+','+"'"+subjectBack[k].backAccount+"'"+')" id="'+subjectBack[k].backId+'">'
						 	+ '<span>'+subjectBack[k].backNick+'</span>'
						 	+ '<span>'+htmlEncodeJQ(subjectBack[k].content)+'</span>'
						   +'</div>';
				}
				
				_html +='</div>';
					    
				if(data.list[i].topicPlNum > 2){
					_html +='<div class="topic-comment-more" onclick="goDetial('+"'"+data.list[i].topicSubjectId+"'"+',1)">'
				   				+'<span>查看更多的评论</span>'
				   				+'<span>'+data.list[i].topicPlNum+'</span>'
				   			+'</div>'
				}
				   		
				_html +='</div>'
			   		+'<div class="topic-input">'
			   			+'<img src="images/ic_see@3x.png">'
			   			+'<span>'+data.list[i].topicReadNum+'</span>'
			   			+'<input type="text" readOnly data-id="'+data.list[i].topicSubjectId+'" onclick="myComment(this)" value="" placeholder="我要评论">'
			   		+'</div>'
		   		+'</div>'
			}else if(data.list[i].type==2){//专题
				_html +='<div class="special-item" onclick="goDetial('+"'"+data.list[i].specialSubjectId+"'"+',2)">'
					  		+'<img class="special-pic" src="'+data.list[i].specialPicturePath+'">'
					  		+'<div class="special-content">'
					  			+'<div class="special-name">'+data.list[i].specialTitle+'</div>'
					  			+'<div class="special-disc">'+data.list[i].specialSecondTitle+'</div>'
					  			+'<div class="special-comment">'
					  				+'<span>'+data.list[i].specialCreateTime+'</span>'
					  				+'<div>'
					  					+'<img src="images/btn_ic_message.png" alt="">'
					  					+'<span>'+data.list[i].specialPlNum+'</span>'
				  					+'</div>'
			  					+'</div>'
		  					+'</div>'
		  				+'</div>';
			}	
	}
	
	return _html;
}

/**
 * 空数据显示的html
 */
function dataNull(){
	var _html = '<div class="null">'
				+'<div class="nullPic">'
				+	'<img src="images/topicSubject_null.png"/>'
				+'</div>'
				+'<div class="nullTips">'
				+	'暂无信息'
				+'</div>'
			  +'</div>';
	return _html;
}

//跳转到详情
function goDetial(id,type){
	$(".special-fixed-world").blur();
	$(".special-input-fixed").css("display","none");
	setTimeout(function(){
		if(type==1){
			var jsonData = {
				eventId: "2.0-click50",
				eventName: "帖子点击次数"
			};
			jsonData = JSON.stringify(jsonData);
			//调用APP接口，添加百度统计
			nativeMethod("baiduStatistics", jsonData);
			//setTitle("帖子详情");
			//window.location.href= ctmsHost + '/ctmsH5/invitation/invitationDetail.html?id='+id+'&token='+token;
			var url = ctmsHost + '/ctmsH5/invitation/invitationDetail.html?id='+id+'&token='+token;
			showActivity(url, "帖子详情");
		}else if(type==2){
			//setTitle("专题详情");
			//window.location.href= ctmsHost + '/ctmsH5/special/specialDetail.html?id='+id+'&token='+token;
			var url = ctmsHost + '/ctmsH5/special/specialDetail.html?id='+id+'&token='+token;
			showActivity(url, "专题详情");
		}
	},500);
	
}

//点击头像跳转到他人主页
function goUserHomePage(userAccount){
	//调用原生方法
	var params = {"userAccount":userAccount};
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

//评论
function myComment(o){
	topicSubjectId = $(o).attr("data-id");
	dom = o;
	$(o).blur();
	$(".special-input-fixed").css("display","block");
	$(".special-fixed-world").focus();


	var topicContentHeight = $(".lists").height();
	var windowHeight = $(window).height();
	//lxjTip.msg(topicContentHeight+" "+windowHeight);
	if(topicContentHeight < windowHeight){
		$(".lists").scrollTop( windowHeight-topicContentHeight );
	}
	setTimeout(function(){ 
            document.body.scrollTop = document.body.scrollHeight; 
    },2); 
}

//发送消息
function sendComment(){
	sendFlag = true;
	console.log("点击评论");
	var jsonData = {
		eventId: "2.0-click49",
		eventName: "发布评论功能使用次数"
	};
	jsonData = JSON.stringify(jsonData);
	//调用APP接口，添加百度统计
	nativeMethod("baiduStatistics", jsonData);
	var content = $(".special-fixed-world").text();
	//处理特殊字符
	content = encodeURIComponent(content);
	var params = {};
	params.header = {
		token : token,
		time_stamp : time_stamp
	};
	params.body = {
		parentId : topicSubjectId,
		type : 4,
		content : content
	}
	
	var paramData = JSON.stringify(params);
	var reqUrl = '/ctmsApi/subjectBack/addSubjectBack';
    $.ajax({
        type: 'GET',
        url: ctmsHost + reqUrl + '?str=' + paramData,
        async:false,
        dataType: 'jsonp',
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
        success: function(data){
        	if(data.result==0){
        		$(".special-fixed-world").text("");
        		$(".special-input-fixed").hide();
        		
        		//刷新数据
        		refreshData();
        		
        		/*//更新到帖子评论
        		//获取当前帖子评论div
        		var back = $(dom).parent().prev().children().get(0);
        		$(back).css("background","#f3f4f5");
        		$(back).prepend('<div class="topic-comment-wrap" onclick="delSubject('+"'"+data.id+"'"+')" id="'+data.id+'"><span>'+data.userName+'</span><span>'+htmlEncodeJQ(data.content)+'</span></div>');
        		//更新评论数
        		if($(dom).parent().prev().children().length == 2 && $(back).children().length > 2){//已经有“点击查看更多”元素
        			var backNumDom = $(dom).parent().prev().children().get(1);
        			var numSpan = $(backNumDom).children().get(1);
        			$(numSpan).text(parseInt($(numSpan).text()) + 1);
        			//删除第三条评论
        			$(back).children().last().remove();
        		}else{
        			if($(back).children().length > 2){
        				var more ='<div class="topic-comment-more" onclick="goDetial('+"'"+topicSubjectId+"'"+',1)">'
					   				+'<span>查看更多的评论</span>'
					   				+'<span>3</span>'
		   					     +'</div>'
        				$(dom).parent().prev().append(more);
        				//删除第三条评论
        				$(back).children().last().remove();
        			}
        		}*/
        	}else{
        		$(".special-fixed-world").text("");
        		$(".special-input-fixed").hide();
        		lxjTip.alert(data.reason,{
        			yes: function() {
						lxjTip.close(); //如果设定了yes回调，需进行手工关闭
					}
        		});
        	}
        }
    });
	
}

//隐藏发送
$(function(){
	var $windowHeight = $(window).height();
	$(window).resize(function() {
		if($windowHeight <= $(window).height()) {
			//$(".topic-content").append("2154");
			$('.special-input-fixed').hide();
			//$(".").blur();
		}
	});	
	$('.special-fixed-world').blur(function(){
		setTimeout(function(){
			if(!sendFlag || !otherFlag){
				console.log("其他操作");
				$('.special-input-fixed').hide();
			}
		},300);
	});

	$('.special-fixed-world').focus(function(){
		$('.special-input-fixed').show();
	});	

	//软键盘弹出后，点击其他地方，软键盘隐藏
	$(".header").on("touchstart",function(){
		otherFlag = true;
		console.log("tochstart");
		if($('.special-input-fixed').show()){
			$('.special-input-fixed').hide();
			$(".special-fixed-world").blur();
		}
		 window.event.cancelBubble=true; 
	});
})

//转义
function htmlEncodeJQ ( str ) {
	return $('<span/>').text( str ).html();
}

//还原
function htmlDecodeJQ ( str ) {
	return $('<span/>').html( str ).text();
}

//刷新
function refreshData() {
	setTimeout(function() {
		pageSize = Math.ceil(($(".special-item").length + $(".topic-content-item").length) / 10) * 10;
		pageNum = 1;
		ajaxData();
	}, 0);
	return 1;
}

/**
 * 删除帖子
 * @return {[type id]} 
 */
function delTopicSubject(id){
	lxjTip.confirm('确定删除该帖子?', {
		skin: 'demo3',
		btn:['删除', '取消'],
		yes: function(index) {
			lxjTip.close(); //如果设定了yes回调，需进行手工关闭
			
			var params = {};
			params.header = {
					token : token,
					time_stamp : time_stamp
			};
			params.body = {
				id : id,
			}
				
			var paramData = JSON.stringify(params);
			var reqUrl = '/ctmsApi/topicSubject/delTopicSubject';
			
			$.ajax({
		        type: 'GET',
		        url: ctmsHost + reqUrl + '?str=' + paramData,
		        async:false,
		        dataType: 'jsonp',
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback",
		        success: function(data){
		        	if(data.result==0){
		        		$("#"+id).remove();
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

/**
 * 删除评论
 * @return {[type id]} 
 */
function delSubject(id,backAccount){
	var tokenUser = $("#tokenUser").val();
	
	if(tokenUser == backAccount){
		lxjTip.confirm('确定删除该评论?', {
			skin: 'demo3',
			btn:['删除', '取消'],
			yes: function(index) {
				
				lxjTip.close(); //如果设定了yes回调，需进行手工关闭
				
				var params = {};
				params.header = {
						token : token,
						time_stamp : time_stamp
				};
				params.body = {
					id : id,
					type : 4
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
			        	if(data.result==0){
			        		//$("#"+id).remove();
			        		refreshData();
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
}

setInterval(function(){
 	document.body.scrollTop = document.body.scrollHeight; 
},100);


//一张图片的时候调用这个
function onePictureCss(){
	var loadImgLength = $(".container img").length
	for(var i = 0; i < loadImgLength; i++){
		$(".container img").load(function(){
		$(this).show();
        var imgWidth = $(this).width();
        var imgHeight = $(this).height();
        console.log(imgWidth+" "+imgHeight);
        var compareFlag = imgWidth/imgHeight;
        var htmlSize = parseFloat($("html").css("font-size"));
       

        if(compareFlag >= 1){
            var maxWidthValue1 = imgWidth > 2.64*htmlSize ? 2.64*htmlSize :imgWidth
            $(this).parent().css({"maxWidth":maxWidthValue1,"maxHeight":imgHeight});
            console.log(maxWidthValue1);
        }else if(compareFlag >= 0.565 && compareFlag < 1){
            var maxWidthValue2 = imgWidth > 2*htmlSize ? 2*htmlSize :imgWidth;
            //console.log(maxWidthValue2);
            $(this).parent().css({"maxWidth":maxWidthValue2,"maxHeight":imgHeight});
             
        }else if(compareFlag < 0.565){
            var maxWidthValue3 = imgWidth > 1.5*htmlSize ? 1.5*htmlSize :imgWidth
            $(this).parent().css({"maxWidth":maxWidthValue3,"maxHeight":1.5*1.7*htmlSize});
        }
	})
	}
	
}