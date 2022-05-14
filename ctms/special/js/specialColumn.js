var vue = new Vue({
	el:".column",
	data:{
		columnItem: [],
		columnFlag:true,
		pageNum:0,
		setTime: 0,
		token: GetURLParameter("token"),
		ctmsHost: ctmsHost,
		organizationSeq: GetURLParameter("organizationSeq")
		/*ctmsHost: ctmsHost, 
		organizationSeq: "4400100183",
		token: "151807725216454511376379403ea414",*/
	},
	methods:{
	    loadMore:function(done) {
        	if (!this.columnFlag) {      
	            done(true);
	            return;
	        }
	        setTimeout(function() {
	            vue.pageNum++;
				var params = {
					body:{
						pageSize:10,
						pageNum: vue.pageNum,
						organizationSeq:vue.organizationSeq
					},
					header:{
						token: vue.token,
						time_stamp: new Date().getTime()
					}
				}
				console.log(params);
				var paramsStr = encodeURI(JSON.stringify(params));
	            var httpURL = vue.ctmsHost+"/ctmsApi/specialColumn/getSpecialColumnList?str="+paramsStr;
	            vue.$http.jsonp(httpURL,{
	                emulateJSON: true,
	                method: "get",
	                dataType: "jsonp",
	                jsonp: "jsoncallback",
	                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
	            }).then(function(res){ 
	            	document.querySelector(".loading-layer").style.display = "block";
	            	console.log(res);
	            	for(var i = 0;i<res.body.specialColumnList.length;i++){
	            		vue.columnItem.push(res.body.specialColumnList[i]);
	            	}
	   				if(res.body.specialColumnList.length == 0 || res.body.specialColumnList.length < 7){
	   					vue.columnFlag = false
	   				}else{
	   					vue.columnFlag = true;
	   				}
	   				vue.setTime = 500;
	   				setTimeout(function(){
		            	done();
		            },0)
	            },function(res){});
	            
	        }, vue.setTime)
      	},
		specialMain: function(specialColumnId){
			var url = ctmsHost + '/ctmsH5/special/specialMain.html?specialColumnId='+specialColumnId+'&token='+this.token+'&organizationSeq='+this.organizationSeq;
			showActivitySpecial(url, "专栏主页", 3, null);
		},
		GetURLParameter: function(name){
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if(r != null) return unescape(r[2]);
			return null;
		}
	},
	mounted:function(){
	
	}
});