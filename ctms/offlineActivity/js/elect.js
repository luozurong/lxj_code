setTitle("参选详情");
setRefreshOnResume(); //刷新页面
clearHistory(); // 清除历史缓存
initializeSession();
//时间戳
var time_stamp = getTimeStamp();
var organizationSeq = sessionStorage.getItem("organizationSeq");
var communityId = GetURLParameter("communityId");
var activityId = GetURLParameter("activityId");
//var organizationSeq = '4400100143';
//var communityId = "3242310";
//var token="201709040939283728509";
//var ctmsHost = "http://192.168.51.26:8090";
//var activityId = "150449069790b846c8d6edfd4df485d8";


var params = {};
params.header = {
    token: token,
    time_stamp: time_stamp
};
params.body = {
    activityId:activityId
};
var param = JSON.stringify(params);
$.ajax({
    type: "post",
    async: false,
    url: ctmsHost + "/ctmsApi/activity/getVoteActivity?str=" + param,
    dataType: "jsonp",
    jsonp: "jsoncallback",
    jsonpCallback: "success_jsonpCallback",
    success: function(data) {
        console.log(data);
        var activityId = data.activityId;
        var electionStatus = data.electionStatus;
        var getVoteActivity = _.template($('#getVoteActivityTemplate').html());
        $('#getVoteActivity').append(getVoteActivity(data));

        //点击标题内容图片预览大图
        $(".electDisc>p>img").attr("data-preview-src", "");
        $(".electDisc>p>img").attr("data-preview-group", "2");
        if(electionStatus==1){
            $('.voteBtn').removeClass('yes');
            $(".voteBtn div").html("我要参选");
            //点击参选按钮页面跳转
            $(".voteBtn").click(function(){
                console.log(1111);
                showActivity(ctmsHost + "/ctmsApi/ctmsH5/offlineActivity/submit.htm?activityId=" + activityId, "上传资料");
            });
        }else if(electionStatus==1){
            $(".voteBtn div").html("参选已结束");
            $(".voteBtn div").css("background", "#ccc");
        }


        //为你优选商品推进数据
        var commodityList = data.commodityList;
        if (commodityList.length == 0) {
            $(".detailBox").hide();
        } else {
            for (var j = 0; j < commodityList.length; j++) {
                var item = commodityList[j];
                var commodityPrice = item.commodityPrice;
                commodityPriceMax=(commodityPrice  + "").split(".")[0];
                commodityPriceMin=(commodityPrice  + "").split(".")[1];
                var commodityListBox = _.template($('#commodityListTemplate').html());
                $('#commodityList').append(commodityListBox(item));
            }

            $(".detailLeft").on("click",function(){
                var li = $(this);
                commodityId=li.attr("id");
                commodityType=li.attr("commodityType");
                sessionStorage.setItem("commodityId",commodityId);
                console.log(commodityType);
                //判断商品是属于服务到家还是商品超市 跳转到对应的商品详情页
                if(commodityType==1){
                    showActivity(mmsHost+"/mms/html5/supermarket/ProductDetail.htm?commodityId="+commodityId,"商品详情");
                }else{
                    showActivity(mmsHost+"/mms/html5/mall/productDetail.htm?commodityId="+commodityId,"商品详情");
                }
                return false;
            });
        }
    }
});

//function submit(){
//    var params = {};
//    params.header = {
//        token: token,
//        time_stamp: time_stamp
//    };
//    params.body = {
//        activityId:activityId,
//        optionsId:optionsId,
//        voteTitle:title,
//        votePicSource:votePicSource,
//        votePicThumb:votePicThumb,
//        source:"1"
//    };
//    var param = JSON.stringify(params);
//    $.ajax({
//        type: "post",
//        async: false,
//        url: ctmsHost + "/ctmsApi/activity/submitVoteActivity?str=" + param,
//        dataType: "jsonp",
//        jsonp: "jsoncallback",
//        jsonpCallback: "success_jsonpCallback",
//        success: function(data) {
//            if(data.code==0){
//                $('.voteBtn').removeClass('yes');
//                lxjTip.msg('参选成功');
//                $(".voteBtn div").html("您已参选");
//                $(".voteBtn div").css("background", "#ccc");
//            }else{
//                lxjTip.msg('参选失败');
//            }
//        }
//    });
//
//}
//避免提示框的 已经到底了 在为你优选商品少的情况下出现
var onscrollState=true;
window.onscroll = function() {
    if (onscrollState) {
        onscrollState=false;
        $(".reminder").show();
    }
};