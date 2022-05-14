Vue.use(VueLazyload, {  
    preLoad: 1.3,  
    error: 'images/498@3x.png',  
    loading: 'images/498@3x.png',  
    attempt: 1  
});
var vue = new Vue({
	el:".collect",
	data:{
		columnItem: [],
		content: '',
		name: '',
		totalCount: '',
		picturePath: '',
		specialColumnId: '',
		activeFlag: true,
		pullImg: "images/btn_ic_pull-down.png",
		columnFlag:true,
		pageNum:0,
		setTime: 0,
		upOnce:true,
        collectOnce: true,
		collectTimeStart: new Date().getTime(),
		collectImg: "images/btn_ic_collect_pre_xxhdpi.png",
		lengthFlag: false,
		lazyImg: 'images/498@3x.png',
		isIos: "",
		time_stamp:new Date().getTime(),
		token: GetURLParameter("token"),
		specialColumnId: GetURLParameter("specialColumnId"),
		ctmsHost:ctmsHost,
		organizationSeq:GetURLParameter("organizationSeq")
		/*token:"1517532880692b2a53252c924e4681a8",
		specialColumnId:"151745336339f14f51bf78774bfaa897",
		ctmsHost:ctmsHost,
		organizationSeq:"4400100183"*/
	},
	filters:{
		times:function(value){
			var value = value.substring(5,16);
			return value;
		}
	},
	methods:{
	    infinite: function(done) {
	        if (!this.columnFlag) {      
	            done(true)
	          	return;
	        }
	        setTimeout(function() {
	          	
	          	vue.pageNum++;
				var params = {
					body:{
						pageSize:10,
						pageNum: vue.pageNum,
						specialColumnId: vue.specialColumnId,
						organizationSeq: vue.organizationSeq
					},
					header:{
						token: vue.token,
						time_stamp: new Date().getTime()
					}
				}
				var paramsStr = encodeURI(JSON.stringify(params));
	            var httpURL = vue.ctmsHost+"/ctmsApi/specialColumn/specialColumnDetailsList?str="+paramsStr;
	            vue.$http.jsonp(httpURL,{
	                emulateJSON: true,
	                method: "get",
	                dataType: "jsonp",
	                jsonp: "jsoncallback",
	                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
	            }).then(function(res){ 
	            	vue.GetLength(res.body.content) > 100 ? vue.lengthFlag = true : vue.lengthFlag = false;

	            	document.querySelector(".loading-layer").style.display = "block";
	            	document.querySelector(".special-main-header").style.opacity = 1;
	        
	            	vue.content = res.body.content;
					vue.name = res.body.name;
					vue.totalCount = res.body.totalCount;
					vue.picturePath = res.body.picturePath;
					vue.specialColumnId = res.body.specialColumnId;
	            	for(var i = 0; i < res.body.specialList.length;i++){
	            		vue.columnItem.push(res.body.specialList[i]);
	            	}
	            	if(res.body.specialList.length == 0 || res.body.specialList.length < 10){
	            		vue.columnFlag = false;
	            	}else{
	            		vue.columnFlag = true;
	            	}
	            	vue.setTime = 500;
	            	setTimeout(function() {
		            	done()
		          	},0)
	            },function(res){})
	        }, vue.setTime);
	    },
	    specialMainAjax: function(pageNum){
	    	var params = {
					body:{
						pageSize:10*pageNum,
						pageNum: 1,
						specialColumnId: vue.specialColumnId,
						organizationSeq: vue.organizationSeq
					},
					header:{
						token: vue.token,
						time_stamp: new Date().getTime()
					}
				}
				var paramsStr = encodeURI(JSON.stringify(params));
	            var httpURL = vue.ctmsHost+"/ctmsApi/specialColumn/specialColumnDetailsList?str="+paramsStr;
	            vue.$http.jsonp(httpURL,{
	                emulateJSON: true,
	                method: "get",
	                dataType: "jsonp",
	                jsonp: "jsoncallback",
	                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
	            }).then(function(res){ 

	            	vue.GetLength(res.body.content) > 100 ? vue.lengthFlag = true : vue.lengthFlag = false;

	            	document.querySelector(".loading-layer").style.display = "block";
	            	document.querySelector(".special-main-header").style.opacity = 1;

	            	vue.content = res.body.content;
					vue.name = res.body.name;
					vue.totalCount = res.body.totalCount;
					vue.picturePath = res.body.picturePath;
					vue.specialColumnId = res.body.specialColumnId;

					vue.columnItem = [];
	            	for(var i = 0; i < res.body.specialList.length;i++){
	            		vue.columnItem.push(res.body.specialList[i]);
	            	}
	            	if(res.body.specialList.length == 0 || res.body.specialList.length < 10){
	            		vue.columnFlag = false;
	            	}else{
	            		vue.columnFlag = true;
	            	}
	            	vue.setTime = 500;
	            	
	            	
	            	if(this.isIos){
					    callNativeMethod('removeActivityIndicator');
					}

	            	setTimeout(function() {
		            	done()
		          	},0)
	            },function(res){})
	    },
	    share:function(id,title,subTitle,indexPic){
	    	if(title!=null && title!=''){
                var jsonData = {
                        title: title,
                        subTitle:subTitle,
                        indexPic:indexPic,
                        id:vue.specialColumnId,
                        fromType: 2,
                        url: ctmsHost + "/ctmsH5/special/specialShare.html?id="+id
                    };
                    var jsonData2 = JSON.stringify(jsonData);
                    nativeMethod("share",jsonData2);
            }
	    },
	    collectClick:function(id,title,createTime,isCollect,i,$event){
            var collectTimeEnd = new Date().getTime();
            var collectTimes = collectTimeEnd - this.collectTimeStart;

            if(collectTimes <= 1500){
                if(!this.collectOnce){
                    lxjTip.msg("请勿频繁操作");
                }
                this.collectTimeStart = collectTimeEnd;
            }else{
                if(!this.collectOnce){
                    this.collectTimeStart = collectTimeEnd;
                    this.collectClikAjax(id,title,createTime,isCollect,i,$event);
                }
            }
            if(this.collectOnce){
                this.collectOnce = false;
                this.collectTimeStart = collectTimeEnd;
                this.collectClikAjax(id,title,createTime,isCollect,i,$event);
            }
			
		},
	    collectClikAjax:function(id,title,createTime,isCollect,i,$event){  
            var createTime = this.creatTimes();
			var params = {
                body:{
                	channel_type: "1",
					operate_type: "1",
			        up_type: (parseInt(isCollect)+1).toString(),
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
            	if(res.body.result == 0){
            		if(vue.columnItem[i].isCollect == "0"){
            			$event.target.src = "images/btn_ic_collect_pre_xxhdpi.png";
            			vue.columnItem[i].isCollect = "1";
            			vue.columnItem = vue.columnItem;
            		}else{
            			$event.target.src = "images/btn_ic_collect_nor_xxhdpi.png";
            			vue.columnItem[i].isCollect = "0";
            			vue.columnItem = vue.columnItem;
            		} 
            	}else{
                    lxjTip.msg(res.body.reason);
                }
   				console.log(res);

                
            },function(res){});
		},
		upClick:function(id,title,createTime,isUp,i,$event){			
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
                    this.upAjax(id,title,createTime,isUp,i,$event);
                }
            }
            if(this.upOnce){
                this.upOnce = false;
                this.upTimeStart = upTimeEnd;
                this.upAjax(id,title,createTime,isUp,i,$event);
            }
			
		},
		upAjax:function(id,title,createTime,isUp,i,$event){	
            var createTime = this.creatTimes();		
			var params = {
                body:{
                	channel_type: "1",
					operate_type: "2",
			        up_type: (parseInt(isUp)+1).toString(),
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
                   if(vue.columnItem[i].isUp == "0"){
            			$event.target.src = "images/ic_like_pre_xxhdpi.png";
            			vue.columnItem[i].isUp = "1";
            			vue.columnItem[i].upNum = res.body.upNum;
            			vue.columnItem = vue.columnItem;
            		}else{
            			$event.target.src = "iimages/ic_like_nor_xxhdpi.png";
            			vue.columnItem[i].isUp = "0";
            			vue.columnItem = vue.columnItem;
            			vue.columnItem[i].upNum = res.body.upNum;
            		} 
                }else{
                    lxjTip.msg(res.body.reason);
                }
   				 			            
            },function(res){});
		},
		specialDetail: function(specialColumnId){
			console.log(specialColumnId);
			var url = ctmsHost + '/ctmsH5/special/specialDetail.html?id='+specialColumnId+'&token='+this.token+'&organizationSeq='+this.organizationSeq+'&channelType=7&otherChannelId=1';
			showActivitySpecial(url, "专题详情", 3, null);
		},
		commentList: function(specialColumnId){
			var url = ctmsHost + '/ctmsH5/special/commentList.html?id='+specialColumnId+'&token='+this.token+'&type=2';
			showActivity(url, "评论");
		},
		GetLength: function(str){
			if(str != null){
				 return str.replace(/[^\x00-\xff]/g,"aa").length;
			}else{
				return 0;
			}
		     
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
        },
		GetURLParameter: function(name){
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if(r != null) return unescape(r[2]);
			return null;
		},
		contentChange: function(){
			if(this.activeFlag == false){
				this.activeFlag = true;
				this.pullImg = "images/btn_ic_pull-down.png"
			}else{
				this.activeFlag = false;
				this.pullImg = "images/btn_ic_pack-up.png"
			}
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
});

function getSharedata(){	
	var jsonData = {
		eventId: "2.0-click81",
		eventName: "专栏主页分享按键点击数"
	};
	jsonData = JSON.stringify(jsonData);
	nativeMethod("baiduStatistics", jsonData);	//调用APP接口，添加百度统计

	var jsonData = {
		title: vue.name,
		subTitle:vue.content,
		indexPic:vue.picturePath,
		id:vue.specialColumnId,
		url: ctmsHost + "/ctmsH5/special/specialMainShare.html?token="+vue.token+"&specialColumnId="+vue.specialColumnId+"&organizationSeq="+vue.organizationSeq
	};
	var jsonData2 = JSON.stringify(jsonData);
	nativeMethod("share",jsonData2);
}

function refreshData() {
	if(vue.isIos){
	    callNativeMethod('addActivityIndicator');
	}
	
	setTimeout(function() {
		vue.specialMainAjax(vue.pageNum);
	}, 0);
	return 1;
}
