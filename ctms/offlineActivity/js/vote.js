    setTitle("票选详情");
    setRefreshOnResume(); //刷新页面数据
    clearHistory(); // 清除历史记录
    initializeSession();
    switchFullScreen(false); //关闭全屏显示
    //时间戳
    var time_stamp = getTimeStamp();
    var organizationSeq = sessionStorage.getItem("organizationSeq");
    var communityId = GetURLParameter("communityId");
    var activityId = GetURLParameter("activityId");
    //var organizationSeq = '4400100143';
    //var communityId = "3242310";
    //var token="201709040939283728509";
    //var activityId = "15032813504044a8ebaf1b5148b08722";
    //var ctmsHost = "http://192.168.51.26:8090";
    select();
    function select(){
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
            url: ctmsHost + "/ctmsApi/activity/getVotingActivity?str=" + param,
            dataType: "jsonp",
            jsonp: "jsoncallback",
            jsonpCallback: "success_jsonpCallback",
            success: function(data) {
                console.log(data);
                var isMultiSelectNum=data.isMultiSelectNum;
                //活动标题数据插入
                $('#activityId').empty();
                var activityId = _.template($('#activityIdTemplate').html());
                $('#activityId').append(activityId(data));

                //点击标题内容图片预览大图
                $(".content>p>img").attr("data-preview-src", "");
                $(".content>p>img").attr("data-preview-group", "2");

                //判断是单选还是多选
                $('#isMultiSelect').empty();
                var isMultiSelect = _.template($('#isMultiSelectTemplate').html());
                $('#isMultiSelect').append(isMultiSelect(data));
                //投票里数据的插入
                var activityOptions = data.activityOptions;
                $("#activityOptions").empty();
                for(var i = 0; i<activityOptions.length; i++){
                    var int=i+1;
                    console.log(isMultiSelectNum);
                    if(data.isMultiSelect==0){
                        $(".pollnum span").html("(单选)");
                        var oHtml = "<div class='voteSelect'>"+"<div class='selectA'>"+" <label>"+"<div class='select_list'>"+"&nbsp;"+"</div>"+
                            "<input name='Fruit' class='select' type='radio' value='"+activityOptions[i].optionsId+"' />"+"<div class='voteImg'>"+"<img src='"+ activityOptions[i].picSource +"' width='100%' style='vertical-align: middle;'data-preview-src='' data-preview-group='1'/>"+"</div>"+"<div class='numberBox'>"+"<div class='number'>"+"<span class='margin-right:0.02rem;'>"+int+"."+"</span>"+ activityOptions[i].title +"</div>"+"<div class='pollsBox'>"+"<span class='polls'>"+activityOptions[i].pollNums+"</span>"+"票"+"</div>"+"</div>"+"</label>"+"</div>"+"</div>";
                        $("#activityOptions").append(oHtml)
                    }else{

                        $(".pollnum span").html("(最多选择"+isMultiSelectNum+"票)");
                        var oHtmls = "<div class='voteSelect'>"+"<div class='selectA'>"+" <label>"+"<div class='select_list'>"+"&nbsp;"+"</div>"+
                            "<input name='Fruit' class='select' type='checkbox' value='"+activityOptions[i].optionsId+"' />"+"<div class='voteImg'>"+"<img src='"+ activityOptions[i].picSource +"' width='100%' style='vertical-align: middle;'data-preview-src='' data-preview-group='1'/>"+"</div>"+"<div class='numberBox'>"+"<div class='number'>"+"<span class='margin-right:0.02rem;'>"+int+"."+"</span>"+ activityOptions[i].title +"</div>"+"<div class='pollsBox'>"+"<span class='polls'>"+activityOptions[i].pollNums+"</span>"+"票"+"</div>"+"</div>"+"</label>"+"</div>"+"</div>";
                        $("#activityOptions").append(oHtmls);

                    }
                }

                //点击票选图片预览大图
                $(".voteImg img").attr("data-preview-src", "");
                $(".voteImg img").attr("data-preview-group", "1");
                //点击选票的样式
                $(".select").click(function(){
                    $(".select_list").removeClass("select_list1");
                    var checked = $("input[name='Fruit']:checked");
                    checked.siblings('.select_list').addClass("select_list1");

                    //最多只能投票多少的时候 提示框显示
                    if(isMultiSelectNum!=null){
                        if(checked.length>isMultiSelectNum){
                            lxjTip.msg("最多可投"+isMultiSelectNum+"票");
                            $(this).attr('checked',false);
                            $(this).prev('.select_list').removeClass("select_list1");
                        }
                    }

                });
                $(".voteBtn").click(function(){
                    if($('.voteBtn').hasClass('yes')){
                        var checked = $("input[name='Fruit']:checked");
                        if(checked.length==0){
                            lxjTip.msg('请先选择选项')
                        }else{
                            var optionIds='';
                            for(var k = 0;k<checked.length;k++){
                                optionIds += checked.eq(k).val()+',';
                            }
                            optionIds = optionIds.substring(0,optionIds.length-1);
                            submit(optionIds);
                        }
                    }

                });

                //为你优选商品排序
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
                        if(commodityType==1){
                            showActivity(host+"/mms/html5/supermarket/ProductDetail.htm?commodityId="+commodityId,"商品详情");
                        }else{
                            showActivity(host+"/mms/html5/mall/productDetail.htm?commodityId="+commodityId,"商品详情");
                        }
                        return false;
                    });
                }
                //判断是否已经投票
                if (data.isVoted == 1) {
                    $('.voteBtn').removeClass('yes');
                    $(".voteBtn div").html("您已投票");
                    $(".voteBtn div").css("background", "#ccc");
                } else {
                    $('.voteBtn').addClass('yes');
                    $(".voteBtn div").html("投票");
                    $(".voteBtn div").css("background", "#fc9153");
                }
            }
        });
    }


    function submit(optionIds){
        var params = {};
        params.header = {
            token: token,
            time_stamp: time_stamp
        };
        params.body = {
            activityId:activityId,
            optionIds:optionIds
        };
        var param = JSON.stringify(params);
        $.ajax({
            type: "post",
            async: false,
            url: ctmsHost + "/ctmsApi/activity/submitPollActivity?str=" + param,
            dataType: "jsonp",
            jsonp: "jsoncallback",
            jsonpCallback: "success_jsonpCallback",
            success: function(data) {
                if(data.code==0){
                    $('.voteBtn').removeClass('yes');
                    lxjTip.msg('投票成功！');
                    select();
                    $(".voteBtn div").html("您已投票");
                    $(".voteBtn div").css("background", "#ccc");
                }
            }
        });

    }
    var onscrollState=true;
    window.onscroll = function() {
        if (onscrollState) {
            onscrollState=false;
            $(".reminder").show();
        }
    };