var token = GetURLParameter("token");
var areaSerial=GetURLParameter("areaSerial");
var organizationSeq = GetURLParameter('organizationSeq');//机构编码

//tt本地调试
//var token ='201804161600089339007';
//var areaSerial = '00243';
//var ctmsHost = 'http://tt.hori-gz.com:8090'

var pageNum = 1;//页码
var pageSize = 10 ;//设置每页返回数据量
var myDataSize = 0;//实际每页返回数据量初始化 
if(token){
	var ts = token.substring(0,1);//token以  _ 开 头是游客
}

$(function(){
	setTitle('全部圈子');
	getData();

	mui.init({
		pullRefresh: {
			container: '#pullrefresh',
			down: {
				callback: pulldownRefresh,
				contentdown : "下拉刷新",
				contentover : "松开刷新数据",
				contentrefresh : "正在加载...",
//				auto:true
			},
			up: {
				callback : pullupRefresh,
				contentdown : "上拉加载更多",
				contentrefresh: '正在加载...',
				contentnomore : "我是有底线的"
			}
		}
		
	});
	
//	绑定点击
	mui("#pullrefresh").on('tap', '.follow_community', function (event) {
		this.click();
	});
	mui("#pullrefresh").on('tap', '.recommend_community', function (event) {
		this.click();
	});
	mui("#pullrefresh").on('tap', '.add_community', function (event) {
//		window.event.cancelBubble=true;//阻止冒泡
		event.stopPropagation()
		this.click();
	});
//	if (mui.os.plus) {
//		mui.plusReady(function() {
//			setTimeout(function() {
//				mui('#pullrefresh').pullRefresh().pullupLoading();
//			}, 1000);
//	
//		});
//	} else {
//		mui.ready(function() {
//			mui('#pullrefresh').pullRefresh().pullupLoading();
//		});
//	}
	
})

var haveFollow = false;//是否有关注数据
var haveRecommend = false;//是否有推荐数据
//获取关注圈子
function getMyCommunitys(){
	var time_stamp = new Date().getTime().toString();
	var params = {};
	params.header = {
		token : token,
		time_stamp : time_stamp
	};
	params.body = {
		userAccount:''
	}
	var paramData = JSON.stringify(params);
	var reqUrl = '/ctmsApi/community/getMyCommunitys';
	console.log(ctmsHost + reqUrl + '?str=' + paramData)
    $.ajax({
        type: 'GET',
        url: ctmsHost + reqUrl + '?str=' + paramData,
        dataType: 'jsonp',
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success : function(data){
			
			getAllCommonitys();//全部圈子
//			console.log(data)
			if(data.result == 0){
        		var listLen = data.list.length;
    			$('.followCommunity_list').html('');
        		if(listLen > 0){
        			
					haveFollow = true;//有数据
        			//先清空再加载数据
        			$('.null').hide();
        			
        			for(var i = 0; i<listLen; i++){
        				var list = data.list[i];
						var followTemplate = _.template($('#followTemplate').html());
						$('.followCommunity_list').append(followTemplate(list));
    				}
        			
        		}else{
        			haveFollow = false;//没数据
        		}
        	}else{
    			haveFollow = false;//没数据
//				$('.null').show();
//				$('#pullrefresh').hide();
//				$('.community_content').hide();
        		console.log(data.reason);//错误信息
        	}
		}
	})
}
//获取全部圈子 放在获取关注圈子里边
function getAllCommonitys(){
	$('.noData').hide();
	var time_stamp = new Date().getTime().toString();
	var params = {};
	params.header = {
		token : token,
		time_stamp : time_stamp
	};
	params.body = {
		areaSerial : areaSerial,
		pageNum : pageNum,
		pageSize : pageSize
	}
	var paramData = JSON.stringify(params);
	var reqUrl = '/ctmsApi/community/getAllCommonitys';
	console.log(ctmsHost + reqUrl + '?str=' + paramData)
    $.ajax({
        type: 'GET',
        url: ctmsHost + reqUrl + '?str=' + paramData,
//      url:'http://192.168.51.30:8090/ctmsApi/community/getAllCommonitys' + '?str=' + paramData,
        dataType: 'jsonp',
		jsonp: "jsoncallback",
		jsonpCallback: "success_jsonpCallback",
		success : function(data){
//			console.log(data)
			if(data.result == 0){
        		var listLen = data.list.length;
        		myDataSize = data.list.length;
        		
        		console.log('返回数据数量:'+myDataSize);
        		if(myDataSize<pageSize ){//返回数据量小于10 禁止下拉
        			mui('#pullrefresh').pullRefresh().disablePullupToRefresh();
        			$('.noData').show();
        			console.log('禁止上拉+页码pageNum:'+pageNum);
        		}else{//否则可下拉
        			console.log('可以上拉')
					mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed关键 不然快速下拉会不刷新
        			mui('#pullrefresh').pullRefresh().enablePullupToRefresh();
    				$('.noData').hide();
        		}
        		if(listLen > 0){
        			$('.null').hide();
        			
					$('#pullrefresh').show();
        			$('.community_content').show();
        			
        			console.log(pageNum)
        			
					if(pageNum == 1){//第一页
						haveRecommend = true;//有推荐数据
		    			
						$('.community_tips').hide();
						$('.greyBox').hide();
						$('.recommendCommunity_list').html('');
		    			if(haveFollow && haveRecommend){
		    				$('.greyBox').show();
							$('.community_tips').show();
		    			}
					}
        			
        			for(var i = 0; i<listLen; i++){
        				var list = data.list[i];
						var recommendTemplate = _.template($('#recommendTemplate').html());
						$('.recommendCommunity_list').append(recommendTemplate(list));
    				}

        		}else{//没数据
        			if(pageNum == 1){//第一页
						$('.community_tips').hide();
						$('.greyBox').hide();
						$('.recommendCommunity_list').html('');
						
	        			if(!haveFollow){
		      				$('.null').show();
		      				$('#pullrefresh').hide();
		      				$('.community_content').hide();
	        			}
        			}
        		}
        	}else{
  				$('.null').show();
  				$('#pullrefresh').hide();
  				$('.community_content').hide();
        		console.log(data.reason);//错误信息
        	}
		}
	})	
}

/**
 * 下拉刷新
 */
var p = true;
function pulldownRefresh() {
	if(p){
		b = false;
		p = false;
		console.log('下拉')
		setTimeout(function() {
			getData();
			mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed
			p = true;
			b = true;
		}, 1000);
	}
}
/**
   * 上拉加载
   */
var b = true; //mui上拉事件频率过快会偶发上拉多次，定义中间变量处理
function pullupRefresh() {
	if(b){
		b = false;
		setTimeout(function(){
			pageNum++;
			haveFollow = true;
			getAllCommonitys();//只加载全部圈子
			mui('#pullrefresh').pullRefresh().endPullupToRefresh(pageSize>myDataSize); //参数为true代表没有更多数据了。
		},500)
		setTimeout(function(){
			b = true;
		},1000);
	}else{//抛出----过快上拉多次
		mui('#pullrefresh').pullRefresh().endPullupToRefresh(pageSize>myDataSize); //参数为true代表没有更多数据了。
		$('.mui-pull-bottom-pocket').css('visibility','visible')
	}
}  

//数据初始化
function getData(){
	pageNum = 1;
//	$('.recommendCommunity_list').html('');
	getMyCommunitys();
}

//原生webview返回调用刷新
function refreshData() {
	setTimeout(function() {
		getData();
		mui('#pullrefresh').pullRefresh().scrollTo(0,0,100);//滚回顶部
	}, 0);
	return 1;
}

//	关注按钮点击
function follow(id){
	window.event.cancelBubble=true;//阻止冒泡
	
	
//	埋点统计
	var clickData = {
		eventId: "2.0-click10",
		eventName: "圈子关注点击数"
	};
	clickData = JSON.stringify(clickData);
	//调用APP接口，添加百度统计
	nativeMethod("baiduStatistics", clickData);
	
	if(ts != '_'){
		var time_stamp = new Date().getTime().toString();
	//	console.log(id);
		var params = {};
		params.header = {
			token : token,
			time_stamp : time_stamp
		};
		params.body = {
			status : "0",//关注
			communityIdList : [{
				communityId : id
			}],
			organizationSeq:organizationSeq
		};
		var paramData = JSON.stringify(params);
		var reqUrl = '/ctmsApi/community/updateAttCommunity';
		
		console.log(ctmsHost + reqUrl +'?str='+ paramData);
		
	    $.ajax({
	        type: 'POST',
	        url: ctmsHost + reqUrl +'?str='+ paramData,
	        dataType: 'jsonp',
			jsonp: "jsoncallback",
			jsonpCallback: "success_jsonpCallback",
	        success: function(data){
	        	
	        	console.log(data)
	        	
	        	if(data.result == 0){
	        		nativeMethod('refresh','');//关注成功时返回上个webview刷新
	//      		lxjTip.alert('关注成功!',{
	//      			yes: function() {
	//						lxjTip.close(); //如果设定了yes回调，需进行手工关闭
	//						getData();//重新请求刷新页面数据
	//					}
	//      		});
					lxjTip.msg('加入成功！',{time:1000});
					setTimeout(function(){
						getData();
						mui('#pullrefresh').pullRefresh().scrollTo(0,0,100);//滚回顶部
					},500);
	
	        	}else{
	        		lxjTip.alert(data.reason,{
	        			yes: function() {
							lxjTip.close(); //如果设定了yes回调，需进行手工关闭
						}
	        		});
	//      		console.log(data.reason);//错误信息
	        	}
	        }
	    });
	}else{
		needLogin(token);
	}

}

//跳转到圈子详情页
function goCommunityDetail(name,id,recommendFlag){
//	埋点统计
	var clickData = {
		eventId: "2.0-click9",
		eventName: "圈子点击数"
	};
	clickData = JSON.stringify(clickData);
	//调用APP接口，添加百度统计
	nativeMethod("baiduStatistics", clickData);
	
	var jsonData = '{"name":"'+name+'","id":"'+id+'","recommendFlag":"'+recommendFlag+'"}';
//	var str =  '{"id" :"111111111","recommendFlag":"0"}' ;
	nativeMethod('goCommunityDetail',jsonData);//传JsonData 原生跳转
}
