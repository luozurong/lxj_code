function refreshData() {
    if(vue.isIos){
        callNativeMethod('addActivityIndicator');
    }
    setTimeout(function() {
         vue.collectAjax(vue.collectNum);
    }, 0);
    return 1;
}

Vue.use(VueLazyload, {  
    preLoad: 1.3,  
    error: '../special/images/498@3x.png',  
    loading: '../special/images/498@3x.png',  
    attempt: 1  
});

var vue = new Vue({
	el:"#collect",
	data:{
		collectItem:[],
		collectFlag: true,
		emptyFlag:false,
		collectNum: 0,
		upImgUrlNor:"../special/images/ic_like_nor_xxhdpi.png",
		upImgUrlPre:"../special/images/ic_like_pre_xxhdpi.png",
        upTimeStart: new Date().getTime(),
        collectTimeStart: new Date().getTime(),
        upOnce:true,
        collectOnce: true,
        lazyImg: '../special/images/498@3x.png',
        isIos: "",
        setTime: 0,
		time_stamp:new Date().getTime(),
        ctmsHost:ctmsHost,
        organizationSeq:this.GetURLParameter("organizationSeq"),
        token:this.GetURLParameter("token")
        /*organizationSeq:"4400100001",
        token:"15253314550264e697f06a764f3a8944",*/
	},
	filters:{
		time:function(value){
			value = value.substring(5,16);
			return value;
		}
	},
	methods:{
		infinite:function(done) {
	        if (!this.collectFlag) {
	            setTimeout(function(){
	            	done(true);
	            }, 10)
	            return;
	        }

	      	setTimeout(function(){
	      		vue.collectNum++;
                var params = {
                    body:{
                        pageNum: vue.collectNum,
                        pageSize: 10,

                    },
                    header:{
                        token:vue.token,
                        time_stamp: vue.time_stamp
                    }
                }
                var paramsStr = encodeURI(JSON.stringify(params));
                var httpURL = vue.ctmsHost+"/ctmsApi/specialSubject/getColSpecialSubjectList?str="+paramsStr;
                vue.$http.jsonp(httpURL,{
                    emulateJSON: true,
                    method: "get",
                    dataType: "jsonp",
                    jsonp: "jsoncallback",
                    contentType:'application/x-www-form-urlencoded; charset=UTF-8',
                }).then(function(res){ 
                    for(var i = 0;i<res.body.list.length;i++){
                        vue.collectItem.push(res.body.list[i]);
                    }
                    res.body.list.length < 10 ? vue.collectFlag = false : vue.collectFlag = true;
                    if(vue.collectItem.length == 0){   
                        setTimeout(function(){
                            vue.emptyFlag = true;
                        },10);
                        document.querySelector(".loading-layer").style.display = "none";
                    }else{
                        document.querySelector(".loading-layer").style.display = "block";
                    }
                    setTimeout(function(){
                        done();
                    },0);
                    vue.setTime = 500;
                    
                },function(res){});
    	          	
	      	},vue.setTime); 
	    },
		collectAjax:function(collectNum){
			var params = {
                body:{
                	pageNum: 1,
					pageSize: 10*collectNum,

                },
                header:{
                    token:this.token,
                    time_stamp: this.time_stamp
                }
            }
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.ctmsHost+"/ctmsApi/specialSubject/getColSpecialSubjectList?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            }).then(function(res){ 
                vue.collectItem = [];
            	for(var i = 0;i<res.body.list.length;i++){
            		this.collectItem.push(res.body.list[i]);
            	}
            	res.body.list.length == 0 ? this.collectFlag = false : this.collectFlag = true;
            	/*if(this.collectItem.length == 0){	
            		setTimeout(function(){
            			vue.emptyFlag = true;
            		},2000);
            		document.querySelector(".loading-layer").style.display = "none";
            	}else{
            		document.querySelector(".loading-layer").style.display = "block";
            	}
   				console.log(res);*/
                if(this.isIos){
                    callNativeMethod('removeActivityIndicator');
                }
            
            },function(res){});
		},
		collectClick:function(id,title,createTime,index){
            /*var collectTimeEnd = new Date().getTime();
            var collectTimes = collectTimeEnd - this.collectTimeStart;

            if(collectTimes <= 1500){
                if(!this.collectOnce){
                    lxjTip.msg("请勿频繁操作");
                }
                this.collectTimeStart = collectTimeEnd;
            }else{
                if(!this.collectOnce){
                    this.collectTimeStart = collectTimeEnd;
                    this.collectClikAjax(id,title,createTime,index);
                }
            }
            if(this.collectOnce){
                this.collectOnce = false;
                this.collectTimeStart = collectTimeEnd;
                this.collectClikAjax(id,title,createTime,index);
            }*/
            this.collectClikAjax(id,title,createTime,index);
			
		},
		collectClikAjax:function(id,title,createTime,index){  
            var createTime = this.creatTimes();
			var params = {
                body:{
                	channel_type: "1",
					operate_type: "1",
			        up_type: "2",
			        id: id,
					name: title,
					organizationSeq: this.organizationSeq,
					createTime: createTime,
                },
                header:{
                    token:this.token,
                    time_stamp: this.time_stamp
                }
            }
            console.log(params);
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.ctmsHost+"/ctmsApi/home/userThumbsUpCollectServlet?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            }).then(function(res){ 
            	if(res.body.result == 0){
            		Vue.delete(this.collectItem,index);
                    if(this.collectItem.length == 0){   
                        setTimeout(function(){
                            vue.emptyFlag = true;
                        },10);
                        document.querySelector(".loading-layer").style.display = "none";
                    }else{
                        document.querySelector(".loading-layer").style.display = "block";
                    }
                    //传数据给原生
                    var jsonData = {
                        type: 1,
                        id:id,
                        state:0
                    };
                    var jsonData2 = JSON.stringify(jsonData);
                    nativeMethod("syncDataState",jsonData2);
            	}else{
                    lxjTip.msg(res.body.reason);
                }
   				console.log(res);

                
            },function(res){});
		},
		upClickNor:function(id,title,createTime,upType,index,$event){
			
            var upTimeEnd = new Date().getTime();
            var upTimes = upTimeEnd - this.upTimeStart;

            if(upTimes <= 1500){
                if(!this.upOnce){
                    lxjTip.msg("请勿频繁操作");
                }
                this.upTimeStart = upTimeEnd;
            }else{
                if(!this.upOnce){
                    this.upTimeStart = upTimeEnd;
                   this.upAjax(id,title,createTime,1,index,$event);
                }
            }
            if(this.upOnce){
                this.upOnce = false;
                this.upTimeStart = upTimeEnd;
                this.upAjax(id,title,createTime,1,index,$event);
            }
			
		},
		upClickPre:function(id,title,createTime,upType,index,$event){			
            var upTimeEnd = new Date().getTime();
            var upTimes = upTimeEnd - this.upTimeStart;

            if(upTimes <= 1500){
                if(!this.upOnce){
                    lxjTip.msg("请勿频繁操作");
                }
                this.upTimeStart = upTimeEnd;
            }else{
                if(!this.upOnce){
                    this.upTimeStart = upTimeEnd;
                    this.upAjax(id,title,createTime,2,index,$event);
                }
            }
            if(this.upOnce){
                this.upOnce = false;
                this.upTimeStart = upTimeEnd;
                this.upAjax(id,title,createTime,2,index,$event);
            }
			
		},
		upAjax:function(id,title,createTime,upType,index,$event){	
            var createTime = this.creatTimes();		
			var params = {
                body:{
                	channel_type: "1",
					operate_type: "2",
			        up_type: "'"+upType+"'",
			        id: id,
					name: title,
					organizationSeq: this.organizationSeq,
					createTime: createTime,
                },
                header:{
                    token:this.token,
                    time_stamp: this.time_stamp
                }
            }
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.ctmsHost+"/ctmsApi/home/userThumbsUpCollectServlet?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            }).then(function(res){ 
   				console.log(res);
                if(res.body.result == 0){
                    if(res.body.isUp == 1){
                        $event.target.src = this.upImgUrlPre;
                        this.collectItem[index].sum = res.body.upNum;
                        this.collectItem[index].upType = 1;
                    }else{
                        $event.target.src = this.upImgUrlNor;
                        this.collectItem[index].sum = res.body.upNum;
                        this.collectItem[index].upType = 0;
                    }
                    var jsonData = {
                        type: 2,
                        id:id,
                        state:upType,
                        info:res.body.upNum
                    };
                    var jsonData2 = JSON.stringify(jsonData);
                    nativeMethod("syncDataState",jsonData2);  
                }else{
                    lxjTip.msg(res.body.reason);
                }
   				 			            
            },function(res){});
		},
        specialDetailJump:function(id){
            var url = this.ctmsHost + '/ctmsH5/special/specialDetail.html?id='+id+'&token='+this.token+'&organizationSeq='+this.organizationSeq+'&channelType=7&otherChannelId=2';
           
            if (sessionStorage.getItem("lxjversionsName")>=5000) {
				showActivity(url, "");
			} else{
				 showActivitySpecial(url, "专题详情", 3, null); 
			}
        },
        commentJump:function(id){
            var url = ctmsHost + '/ctmsH5/special/commentList.html?id='+id+'&type=2&token='+this.token;
            showActivity(url, "评论");
        },
        shares:function(id,title,subTitle,indexPic){
            if(title!=null && title!=''){
                var jsonData = {
                        title: title,
                        subTitle:subTitle,
                        indexPic:indexPic,
                        fromType: 2,
                        url: ctmsHost + "/ctmsH5/special/specialShare.html?id="+id
                    };
                    var jsonData2 = JSON.stringify(jsonData);
                    nativeMethod("share",jsonData2);
            }
        },
        goIndex:function(){
            popToHomePage();
        },
		GetURLParameter: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r != null) return decodeURI(r[2]);
            return null;
        },
        creatTimes: function(){
            var times = new Date();
            var YY = times.getFullYear();
            var MM = times.getMonth()+1;
                if(MM < 10) MM = "0"+MM;
            var DD = times.getDate();
                if(DD < 10) DD = "0"+DD;

            var HH = times.getHours();
                if(HH < 10) HH = "0"+HH;
            var mm = times.getMinutes();
                if(mm < 10) mm = "0"+mm;
            var ss = times.getSeconds();
                if(ss < 10) ss = "0"+ss;
            times =  YY+"-"+MM+"-"+DD+" "+HH+":"+mm+":"+ss;
            return times;
        }
	},
	mounted:function(){
        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //g
        var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
        if (isAndroid) {
           //这个是安卓操作系统
           this.isIos = false;
        }
        if (isIOS) {
    　　　　//这个是ios操作系统
            this.isIos = true;
        }
	}
})
