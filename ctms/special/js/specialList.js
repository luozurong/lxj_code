var communityId = "";
var organizationSeq = "";
var token = "150777078544a97df62fd16441029b86";
var time_stamp = new Date().getTime();
//var ctmsHost = "https://tt.hori-gz.com:8443";
var ctmsHost = "http://192.168.51.24:8090";


/*var communityId = GetURLParameter("communityId");
var organizationSeq = GetURLParameter("organizationSeq");
var token = GetURLParameter("token");
var time_stamp = new Date().getTime();*/


//url截取参数
function GetURLParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

$(function(){
	setTitle("专题列表");
});

/**
 * [page description] 上拉刷新
 */
var page = 1;
var pageFalse = true;

loaded(function(){ //数据初始化
    ajaxData(page);
},function(){  //上拉加载
	if(pageFalse){
		page++;
		pageFalse = false;
		setTimeout("ajaxData(page)",1000);
	} 
	
},function(){ //下拉刷新
   /* console.log("down");
    if(pageFalse){
		page++;
		pageFalse = false;
		setTimeout("ajaxData(page)",1000);
	} */
});

/**
 * [ajaxData description] 列表数据请求
 * @param  {[type]} page 页数
 */
function ajaxData(page,pageSize,state){
	//alert("communityId:"+communityId);
	//alert("organizationSeq:"+organizationSeq);
	
	if(communityId==null || communityId=="null"){
		communityId = "";
	}
	if(organizationSeq==null || organizationSeq=="null"){
		organizationSeq = "";
	} 
	var ajaxParams = {};    //请求参数
	ajaxParams.header = {
		token:token,
		time_stamp:time_stamp
	};	
	ajaxParams.body = {
			pageSize: pageSize?pageSize:5,
			pageNum: page,
			communityId: communityId,
			organizationSeq: organizationSeq
	};
	var paramData = JSON.stringify(ajaxParams);
	$.ajax({
		url: ctmsHost + "/ctmsApi/specialSubject/getSpecialSubjectList?str=" +paramData,
		//data: {},
		async:true,
		dataType: "jsonp",
        jsonp: "jsoncallback",
        jsonpCallback: "success_jsonpCallback",
		success: function(data){
			$("#pullUp").show();
			scrollAjax = true; //判断加载中显示时间(必填)
			console.log("专题列表:"+data);
			if(state){
				$(".special").empty();
			}
			//当专题列表不为空时
			if(data.list.length > 0){
				var _html = '';
				for (var i = 0; i < data.list.length; i++) {
					var id = data.list[i].id;
					var content = data.list[i].content;
					var title = data.list[i].title;
					var secondTitle = data.list[i].second_title;
					var plNum = data.list[i].pl_num;
					var picture1_path = data.list[i].picture1_path;
					var creatTime = data.list[i].create_time;
					_html += '	<div onclick="goSpecialDetail('+"'"+id+"'"+')" class="special-item">';
					_html += '		<img class="special-pic" src='+picture1_path+'>';
					_html += '		<div class="special-content">';
					_html += '			<div class="special-name">'+title+'</div>';
					_html += '			<div class="special-disc">'+secondTitle+'</div>';
					_html += '			<div class="special-comment" onclick="goSpecialDetailComment('+"'"+id+"'"+')">';
					_html += '				<span>'+creatTime+'</span>';
					_html += '				<div>';
					_html += '					<img src="images/btn_ic_message.png" alt="">';
					_html += '					<span>'+plNum+'</span>';
					_html += '				</div>';
					_html += '			</div>';
					_html += '		</div>';
					_html += '	</div>';
				}
				$(".special").append(_html);
				$(".special-load-more").hide();

				var imgLength = $(".special-item img").length;
				$(".special-item img").load(function(){
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
			//	setTimeout("myScroll.refresh()",100);
				pageFalse = true;
			}else if(page==1 && data.list.length == 0){
				$("#pullUp").hide();
				var _html = '';
				_html += '<div class="special">';
				_html += '	<div class="null">';
				_html += '		<div class="nullPic">';
				_html += '			<img src="images/specialSubjectNull.png"/>';
				_html += '	    </div>';
				_html += '		<div class="nullTips">';
				_html += '		  "暂无信息"';
				_html += '		</div>';
				_html += '	</div>';
				_html += '</div>';
				$(".special").append(_html);
				$(".special-load-more").hide();

				//setTimeout("myScroll.refresh()",100);
				pageFalse = true;
			}else{
				pageFalse = false;
				$(".pullUpLabelNoData").show();
				$("#pullUp").hide();
			}
			//setTimeout("myScroll.refresh()",500);
		}
	});		
}

/**
 * [goSpecialDetail] 直接进入专题详情
 * @param  {[type]} id [description]
 * @return {[type]}    [description]
 */
function goSpecialDetail(id){
	var focusFlag = 0;
	showActivitySpecial(ctmsHost + "/ctmsH5/special/specialDetail.html?id="+id+"&token="+token+"&focusFlag="+focusFlag, "专题详情", 3, null);
//	window.location.href = ctmsHost + "/ctmsH5/special/specialDetail.html?id="+id+"&token="+token+"&focusFlag="+focusFlag;
}

/**
 * [goSpecialDetailComment] 点击评论进入专题详情
 * @return {[type]} [description]
 */
function goSpecialDetailComment(id){
	var focusFlag = 1;
	showActivitySpecial(ctmsHost + "/ctmsH5/special/specialDetail.html?id="+id+"&token="+token+"&focusFlag="+focusFlag, "专题详情", 3, null);
	//window.location.href = ctmsHost + "/ctmsH5/special/specialDetail.html?id="+id+"&token="+token+"&focusFlag="+focusFlag;
	console.log("点击评论进入专题详情");
	window.event.cancelBubble=true; 
}

function refreshData() {
	$(".pullUpLabelNoData").hide();
	 pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多';
	setTimeout(function() {
		var pageSize = Math.ceil(($(".special-item").length) / 10) * 10;
		ajaxData(1,pageSize,true);
	}, 0);
	return 1;
}