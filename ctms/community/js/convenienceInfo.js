var topS = new Vue({
	el:"#top",
	data:{
		navList: null,
		info: [],
		emptyFlag: false, 
		onceFlag: true,
		ctmsHost: ctmsHost,
		token: GetURLParameter("token"),
		id:GetURLParameter("id"),
		classifyName:"",
		/*ctmsHost: 'http://tt.hori-gz.com:8090',
		token: "1540890548700066ab82c0874c769046",*/
		
	},
	methods:{
	    infoAjax: function(){ //获取导航栏列表
	    	var params = {
				body:{
					id: this.id?this.id:"1",//新增需求修改
				},
				header:{
					token: this.token,
					time_stamp: new Date().getTime()
				}
			};
			var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.ctmsHost+"/ctmsApi/classify/getClassifyList?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            }).then(function(res){

            	var infoObjTamp = [];
            	if(res.body.result == 0){
            		this.navList = res.body.list;
            		if(this.navList.length == 0){
            			this.emptyFlag = true;
            		}
            		for(var i = 0; i < this.navList.length; i++){
            			var infoObj = {list:[],pageNum: null,num: i,clickFlag: false}; 
            			infoObjTamp.push(infoObj);
            		}
            		this.info = infoObjTamp;            		       		
            		page.infoData();
            		page.infoDetailAjax(0);
            		page.contentClassifyAjax(0);
            		//新增开始
            		topS.classifyName=res.body.classifyName?res.body.classifyName:"便民资讯";    
            		setTitle(topS.classifyName);					
					setTimeout(function() {
						if(GetURLParameter("clientType") == 'android') {
							setTitle(topS.classifyName ? topS.classifyName : "便民资讯");
						} else {
							try {
								callNativeMethod('setNewtitle', {
									titleText: topS.classifyName ? topS.classifyName: "便民资讯"
								});
							} catch(e) {}
						}
					}, 100)
					//新增结束
            		
            	}
            },function(res){});
	    }
	},
	mounted:function(){
		this.infoAjax();
	}
});
var page = new Vue({
	el:"#page",
	data:{
		indexFlag: 0,
		info: [],
		swiperScrollFlag: true,
		onceFlag: true,
		runFlag: true,
		pageEmpty: false,
		activeIndex: 0,
		ctmsHost: topS.ctmsHost,
		token: topS.token,
		organizationSeq: GetURLParameter("organizationSeq"),
	},
	methods:{
		infoData: function(){
			var infoObjTamp = [];
    		for(var i = 0; i < topS.navList.length; i++){
    			var infoObj = {list:[],pageNum: 0,flag: true,word: false,emptyFlag: false}; 
    			infoObjTamp.push(infoObj);
    		}
    		this.info = infoObjTamp;
		},
	    infoDetailAjax: function(index){ //获取导航栏对应的信息列表
        	if(!this.info[index].flag){
				return false;
			}
			this.info[index].pageNum += 1;
			//新增开始
			var lastId="";
			var lastTime="";
			if (this.info[index].list.length>0) {				
				lastId=this.info[index].list[this.info[index].list.length-1].id;
				lastTime=this.info[index].list[this.info[index].list.length-1].createtime;
			}
		console.log(lastId)
			//新增结束
			
	    	var params = {
				body:{
					classifyId: topS.navList[index].id,
					organizationSeq: this.organizationSeq,					
					pageNum: this.info[index].pageNum,
					pageSize: 10,
					lastId:lastId,
					lastTime:lastTime
				},
				header:{
					token: this.token,
					time_stamp: new Date().getTime()
				}
			};			
			var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.ctmsHost+"/ctmsApi/classify/getConvenientInformation?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            }).then(function(res){ 
            	this.pageEmpty = true;
            	if(res.body.result == 0){
            		var listArray = res.body.list;
            		for(var i = 0;i < listArray.length;i++){
            			listArray[i].clickFlag = false;
            		}
            		this.info[index].list = this.info[index].list.concat(listArray);
            		this.info[index].pageNum = res.body.pageNum;
            		if(res.body.list.length < 10){
            			this.info[index].flag = false;
            			this.info[index].word = true;
            		}else{
            			this.info[index].word = false;
            		}
            		this.info[index].emptyFlag = true;
            	}

            	setTimeout(function(){
            		initSwipers(index);	
	          		scrollSwiper();
	          	},10);
            },function(res){});
	    },
	    goSpecialDetail: function(i,index,id,specialName){
	    	var showActivityName=specialName?specialName:"文章详情";
	    	this.info[i].list[index].clickFlag = true;
	    	showActivity(this.ctmsHost + "/ctmsH5/special/specialDetail.html?id="+id,showActivityName);
	    },
	    contentClassifyAjax: function(index){ //获取导航栏对应的信息列表
	    	var params = {
				body:{
					contentId: topS.navList[index].id,
					contentName: topS.navList[index].name,
					type: this.isIos(),
				},
				header:{
					token: this.token,
					time_stamp: new Date().getTime()
				}
			};
			var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.ctmsHost+"/ctmsApi/contentClassifyAnalysis/visitLog?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            }).then(function(res){ 
            	
            },function(res){});
	    },
	    isIos: function(){
	    	var u = navigator.userAgent;
			var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
			if(isiOS) return 1;
			else  return 0;
	    }
	},
	watch: {
		indexFlag: function(newVal,oldVal){
			if(this.info[newVal].list.length == 0){
				page.infoDetailAjax(newVal);
			}
			
			this.activeIndex = newVal;
			this.contentClassifyAjax(newVal);
			lineColor(newVal);
		}
	},
	mounted:function(){
		
	}
});

function refreshData() {
	setTimeout(function() {
		
	}, 0);
	return 1;
}
function GetURLParameter(name){
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}
var newspiper1;