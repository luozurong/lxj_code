//var ctmsHost = 'http://192.168.51.20:8090';
//var token = "151243668969fa876a104d704934b8c1";
//var ctmsHost = 'http://118.190.8.133:8090';
var token =GetURLParameter("token");
var organizationSeq = GetURLParameter("organizationSeq");
var time_stamp = new Date().getTime();
var type = '1';
var pageNum = 1;
var pageSize = 10;
var totalPage = 1;
var pageFlag = true;
var topicFlag = true;
var topicSubjectId = '';
var dom = '';
var sendFlag = true;
var onceFlag = true;

$(function(){ 
	//tabs
	$(".topic-header>div").click(function(){
        $(this).addClass("topic-header-active").siblings().removeClass("topic-header-active");
        /*$(".lists").html("");*/
        onceFlag = true;
        type = $(this).index()+1+'';
        pageNum = 1;
        totalPage = 1;
        ajaxData();
        $(".pullUpLabelNoData").hide();
		$("#pullUp").show();
        myScroll.scrollTo(0,0,1);
    })
   
 })

//数据初始化，上拉下拉

	loaded(function(){
		ajaxData();
		$(".pullUpLabelNoData").hide();
		$("#pullUp").show();

	},function(){  //上拉加载
		if(pageFlag){
			if(pageNum < totalPage){
				pageNum++;
				pageFlag = false;
				setTimeout("ajaxData()",1000);
			}else{
				$(".pullUpLabelNoData").show();
				scrollAjax = true;
				$("#pullUp").hide();
			}
		} 
		
	},function(){ //下拉刷新
		$(".pullUpLabelNoData").hide();
		$("#pullUp").show();
		onceFlag = true;
		//清空内容
		/*$(".lists").html("");*/
		pageNum = 1;
		ajaxData();
	});
 
 //请求数据
function ajaxData(){
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
        	scrollAjax = true; //判断加载中显示时间(必填)

        	if(data.result == 0){

        		totalPage = data.totalPage;
        		$("#all").text(data.all);
        		$("#publish").text(data.publish);
        		$("#comment").text(data.comment);
        		$("#praise").text(data.praise);
        		//迭代数据,获取html
        		//var _html =getHtml(data);
        		var _html =getHtmlList(data);
        		
        		if(pageNum == 1 && totalPage <= pageNum){
        			if(_html==''){
            			_html = dataNull();
            		}
        		}
        		
        		if(onceFlag){
        			$(".lists").html("");
        		}
        		
        		$(".lists").append(_html);
        		setTimeout(function(){
        			 myScroll.refresh();
        		},300);
        		//一张图片的时候调用onePictureCss方法修改样式
    			onePictureCss();
        		
        		//没有评论会有个div的颜色不一样，直接将颜色改成白色就看不出
        		/*$(".topic-comment").each(function(){
        			if($(this).children().children().length==0){
        				//本来颜色#f3f4f5
        				$(this).css("background","#ffffff");
        			}
        		});*/
        		
        		var imgLength = $(".special-item img").length;
				$("#special img").load(function(){
					if(!--imgLength){
						console.log(1);
						var theListHeight = $("#thelist").height();
						var winHeight = $(window).height();
						if(theListHeight < winHeight){
							$(".pullUpLabelNoData").show();
							$("#pullUp").hide();
						}
					}
					
				})

				if(pageNum==1 && data.list.length==0){
					$(".pullUpLabelNoData").hide();
					$("#pullUp").hide();
				}

				if(pageNum==1 && pageNum==totalPage){
					$(".pullUpLabelNoData").show();
					$("#pullUp").hide();
				}
				//$("#pullUp").show();
        		pageFlag = true;
        		onceFlag = false;
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

/**
 * 解析json,拼接html(旧)
 * @param data
 */
function getHtml(data){
	var _html = '';
	for (var i = 0; i < data.list.length; i++) {
			
			var headPicture = 'images/ic_default_avatar.png';
			if(data.list[i].type==1){//帖子
				_html += '<div class="topic-content-item" onclick="goDetial('+"'"+data.list[i].topicSubjectId+"'"+',1)" id='+"'"+data.list[i].topicSubjectId+"'"+'>'
								+'<div class="topic-item">';
				//头像
				if(data.list[i].topicHeadPicture!=null&&data.list[i].topicHeadPicture!=''){
					headPicture = data.list[i].topicHeadPicture;
				}
				_html +='<img style="border-radius: 100%;" src="'+headPicture+'">'
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
					+'<div class="topic-main-word"><pre>'+htmlEncodeJQ(data.list[i].topicContent)+'</pre></div>'
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
					_html += '	  <img src="'+pictureList[0].picturePath+'" style="opacity: 0;display: none" onclick="goDetial(' + "'" + data.list[i].topicSubjectId + "'" + ',1)">';
					_html += '</div>';
				}else if(pictureList.length>1 && pictureList.length<4){
					_html += '<div class="img-rule2">';
					for(var p = 0; p < pictureList.length; p++){
			            _html += ' <span style="background:url('+pictureList[p].picturePath+') no-repeat center;background-size:cover;" onclick="goDetial(' + "'" + data.list[i].topicSubjectId + "'" + ',1)"></span>';
			        }
					_html += '</div>';
				}else if(pictureList.length==4){
					_html += '<div class="img-rule3">';
					for(var p = 0; p < pictureList.length; p++){
			            _html += ' <span style="background:url('+pictureList[p].picturePath+') no-repeat center;background-size:cover;" onclick="goDetial(' + "'" + data.list[i].topicSubjectId + "'" + ',1)"></span>';
			        }
					_html += '</div>';
				}
				
				$(".topic-pic-two").append(_html);

				_html +='</div>'
					+'</div>';
				
				/*//评论
				_html +='<div class="topic-comment" style="display:none;">'
						+'<div class="topic-comment-item">';
				
				var subjectBack = data.list[i].topicSubjectBack;
				for(var k = 0; k < subjectBack.length; k++){
					_html +='<div class="topic-comment-wrap" onclick="delSubject('+"'"+subjectBack[k].backId+"'"+','+"'"+subjectBack[k].backAccount+"'"+')" id="'+subjectBack[k].backId+'">'
						 	+ '<span>'+subjectBack[k].backNick+'</span>'
						 	+ '<span>'+htmlEncodeJQ(subjectBack[k].content)+'</span>'
						   +'</div>';
				}
				
				_html +='</div>';
					    
				if(data.list[i].topicPlNum > 2){
					_html +='<div class="topic-comment-more" style="display:none;" onclick="goDetial('+"'"+data.list[i].topicSubjectId+"'"+',1)">'
				   				+'<span>查看更多的评论</span>'
				   				+'<span>'+data.list[i].topicPlNum+'</span>'
				   			+'</div>'
				}
				   		
				_html +='</div>';*/
				_html +='<div class="topic-input" >'
			   			//+'<img style="display:none;" src="images/ic_see@3x.png">'
			   			//+'<span style="display:none;">'+data.list[i].topicReadNum+'</span>'
			   			//+'<input style="display:none;" type="text" readOnly data-id="'+data.list[i].topicSubjectId+'" onclick="myComment(this)" value="" placeholder="我要评论">'
			   		+'</div>'
		   		+'</div>';
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
	var _html = '<div class="null" style="min-height: 6.73rem;">'
				+'<div class="nullPic">'
				+	'<img src="images/topicSubject_null.png"/>'
				+'</div>'
				+'<div class="nullTips">'
				+	'暂无信息'
				+'</div>'
				+'<div onclick="popToHomePage()" class="nullTips_radius">'
				+	'去看看热门话题'
				+'</div>'
			  +'</div>';
	return _html;
}

//跳转到详情
function goDetial(id,type,subjectBackId){
	console.log(type);
	$(".special-fixed-world").blur();
	$(".special-input-fixed").css("display","none");
	setTimeout(function(){
		if(type==4||type==5||type==7){
			var jsonData = {
				eventId: "2.0-click50",
				eventName: "帖子点击次数"
			};
			jsonData = JSON.stringify(jsonData);
			//调用APP接口，添加百度统计
			nativeMethod("baiduStatistics", jsonData);
			//window.location.href= ctmsHost + '/ctmsH5/invitation/invitationDetail.html?id='+id+'&token='+token+'&organizationSeq='+organizationSeq;
			var url = ctmsHost + '/ctmsH5/invitation/invitationDetail.html?id='+id+'&token='+token+'&organizationSeq='+organizationSeq+'&subjectBackId='+subjectBackId;
			showActivity(url, "帖子详情");
		}else if(type==2||type==6){
			//window.location.href= ctmsHost + '/ctmsH5/special/specialDetail.html?id='+id+'&token='+token+'&organizationSeq='+organizationSeq;
			var url = ctmsHost + '/ctmsH5/special/specialDetail.html?id='+id+'&token='+token+'&organizationSeq='+organizationSeq+'&channelType=2&subjectBackId='+subjectBackId;
			
			if (sessionStorage.getItem("lxjversionsName")>=5000) {
				showActivity(url, "");
			} else{
				showActivity(url, "专题详情");
			}
		    //showActivitySpecial(url, "专题详情", 3, null);
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
	console.log("评论");
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
	window.event.cancelBubble = true;
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
    window.event.cancelBubble = true;
}

//隐藏发送
/*$(function(){
	var $windowHeight = $(window).height();
	$(window).resize(function() {
		if($windowHeight <= $(window).height()) {
			//$(".topic-content").append("2154");
			$('.special-input-fixed').hide();
			//$(".").blur();
		}
	});	
	

	$('.special-fixed-world').focus(function(){
		$('.special-input-fixed').show();
	});	
	$('.special-fixed-world').blur(function(){
		setTimeout(function(){
			$('.special-input-fixed').hide();
		},10);
	});
	//软键盘弹出后，点击其他地方，软键盘隐藏
	$(".header").on("touchstart",function(){
		$('.special-input-fixed').hide();
		$(".special-fixed-world").blur();

	});
})*/

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
	$(".pullUpLabelNoData").hide();
	pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多';
	setTimeout(function() {
		pageSize = Math.ceil(($(".special-item").length + $(".topic-content-item").length) / 10) * 10;
		pageNum = 1;
		  onceFlag = true;
		 ajaxData();
	}, 0);
	return 1;
}

/**
 * 删除帖子
 * @return {[type id]} 
 */
function delTopicSubject(id){
	window.event.cancelBubble=true ;
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
		        		
		        		//tabs(全部，发布减一)
		        		var all = parseInt($("#all").text())-1;
		        		var publish = parseInt($("#publish").text())-1;
		        		$("#all").text(all);
		        		$("#publish").text(publish);
		        		
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

/**
 * 解析json,拼接html
 * @param data
 * @returns {String}
 */
function getHtmlList(data){
	var _html = '';
	//默认头像
	var headPicture = '';
	if(data.tokenSex=='2'){
		var headPicture = 'images/ic_woman@3x.png';
	}else{
		var headPicture = 'images/ic_man@3x.png';
	}
	
	//用户头像
	if(data.tokenHeadPicture!=null && data.tokenHeadPicture!=''){
		headPicture = data.tokenHeadPicture;
	}
	
	for (var i = 0; i < data.list.length; i++) {
		_html+='<div class="topic-content-item" onclick="goDetial('+"'"+data.list[i].id+"'"+",'"+data.list[i].type+"',"+"'"+data.list[i].messageId+"'"+')" id='+"'"+data.list[i].id+"'"+'>';
		_html+='	<div class="topic-item">';
		_html+='		<img  style="border-radius: 100%;" src="'+headPicture+'">';
		_html+='		<div>';
		_html+='			<div style="width: 2.1rem;">';
		_html+='				<span>'+data.tokenNick+'</span>';
		_html+='  				<span>';
		_html+='					<p class="special-radius">';
										if(data.list[i].backType==2){
											_html+='我回复&nbsp'+data.list[i].backNick+':';
										}
		_html+=							htmlEncodeJQ(data.list[i].messageContent);
		_html+='					</p>';
		_html+='				</span>';
		_html+=' 				<span class="special-time">'+data.list[i].messageTime+'</span>';
		_html+='			</div>';
		_html+='		</div>';
		_html+='	<div style="flex-grow: 0;width:0.6rem;height:0.6rem;" class="topicPicture">';
					if(data.list[i].picture != ""){
		_html+='		<img src="'+data.list[i].picture+'">';
					}else{
		_html+='		<div class="word-right">'+data.list[i].content+'</div>';
					}
		_html+='	</div>';
		_html+='</div>';
		_html+='</div>';
		_html +='<div class="topic-input" ></div>';
	}
	
	return _html;
}