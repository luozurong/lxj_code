var vue = new Vue({
    el: '#apply',
    data: {
        userAccount: "",
        addApplyNo: 1,
        picked: "-1",
        sdkItem: "",
        sdkItemNoData: false,
        company:"",
        joinAccount:"",
        joinPassword:"",
        createTime:"",
        verifyStatus: "",
        terraceItem: "",
        terraceAreaItem: "",
        terraceItemFlag:false,
        oneStatus: true,
        twoStatus: false,
        terraceFlag: false,
        addAreaFlag: false,
        isTrue:"",
        host: host,
        locationHost:locationHost,
        /*host: "http://192.168.51.24:8090",
        locationHost:"http://192.168.51.33:8090",*/
        time_stamp: new Date().getTime(),
        token: "",

    },
    methods:{
    	addApply:function(num){ //tab切换
    		this.addApplyNo = num;
    		if(num == 1){
    			this.oneStatus = true;
    			this.twoStatus = false;
    		}else{
    			this.oneStatus = false;
    			this.twoStatus = true;
    		}
    	},
    	sdkAjax:function(pickedNum){ //我的应用列表
    		var params = {
    			body:{
					verifyStatus: this.picked,
                    userAccount:this.userAccount
    			},
    			header:{
    				token:this.token,
    				time_stamp:this.time_stamp
    			}
    		}
    		var paramsStr = encodeURI(JSON.stringify(params));
    		var httpURL = this.host+"/lxjsdkApi/getApplicationList?str="+paramsStr;
    		this.$http.jsonp(httpURL,{
			    emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
			}).then(function(res){  
                console.log(res);
            	var data = res.body;
            	this.sdkItem = data.list;
            	if(data.list.length == 0){
            		this.sdkItemNoData = true;
            	}else{
            		this.sdkItemNoData = false;
            	}  
            },function(res){  
                console.log(res);  
            });  
    	},
        delAjax:function(index){ //删除我的应用  
            layer.confirm('是否删除该项数据？', {
              btn: ['是','否'] //
            }, function(i){
              vue.delAjaxFunc(index);
              layer.close(i);
            }, function(){});
        },
        delAjaxFunc:function(index){
            var id = this.sdkItem[index].id;
            var params = {
                body:{
                    id: id
                },
                header:{
                    token:this.token,
                    time_stamp:this.time_stamp
                }
            }
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/delApplication?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
            }).then(function(res){  
                if(res.body.result == 0){
                    Vue.delete(this.sdkItem,index);
                }
            },function(res){  
                 
            }); 
        },
        terraceAjax:function(){ //获取平台账号密码
            var params = {
                body:{
                    userAccount: this.userAccount
                },
                header:{
                    token:this.token,
                    time_stamp:this.time_stamp
                }
            }
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/getPlatFormList?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
            }).then(function(res){  
                console.log(res);
                this.terraceItem = res.body.platFormJoinList;
                if(res.body.isRegisted == 1){
                    this.terraceFlag = false;
                    this.addAreaFlag = true;
                    this.isTrue = false;
                  
                }else{
                    this.terraceFlag = true;
                    this.addAreaFlag = false;
                    this.isTrue = true;
                } 
                if(res.body.platFormJoinList.length != 0){
                    this.terraceItemFlag = false;
                    var terraceItemIndex = 0;

                    for(var i = 0;i < this.terraceItem.length; i++){
                        if(parseInt(this.terraceItem[i].verifyStatus) == 2){
                            this.company = this.terraceItem[i].company
                            if(res.body.isRegisted == 1){
                                this.terraceFlag = false;
                                this.addAreaFlag = true;
                               
                                if(res.body.platFormJoinList[i].verifyStatus == 2){
                                    this.terraceAreaItem = this.terraceItem[i].platFormJoinAreaList;
                                    this.company = this.terraceItem[i].company;
                                    sessionStorage.setItem("company",this.company);
                                }
                            }else{
                                this.terraceFlag = true;
                                this.addAreaFlag = false;
                                 
                            } 
                        }else{
                            console.log(1);
                        }
                    }
                     
                }else{
                     this.terraceItemFlag = true;
                }             
            },function(res){  
                 
            });
        },
        addApplyUrl:function(){
            if(!this.isTrue){
                window.location.href=this.locationHost+"/lxjsdkH5/joint/sdkApply.html?str="+this.token+"&userAccount="+this.userAccount
            }
        },
        addEstate:function(){ //添加小区
            sessionStorage.setItem("estateFlag",1);
            sessionStorage.setItem("inputEstateFlag",0);
            sessionStorage.setItem("company",this.company);
            window.location.href=this.locationHost+"/lxjsdkH5/joint/terraceApply.html?str="+this.token+"&userAccount="+this.userAccount
        },
        inputEstate:function(){
            sessionStorage.setItem("inputEstateFlag",1);
            sessionStorage.setItem("estateFlag",0);
            window.location.href=this.locationHost+"/lxjsdkH5/joint/terraceApply.html?str="+this.token+"&userAccount="+this.userAccount
        },
        goIndex:function(){
            window.location.href=this.locationHost+"/lxjsdkH5/index/index.html?str="+this.token+"&userAccount="+this.userAccount
        },
        goUser:function(){
             window.location.href = (this.locationHost+"/lxjsdkH5/user/my.html?str="+this.token+"&userAccount="+(this.userAccount));
        },
        isCondition:function(param) {
            if(param == null || param == "" || param == undefined || param == "null") {
                return true;
            }
            return false;
        },
        GetURLParameter: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r != null) return decodeURI(r[2]);
            return null;
        }
       
    },
    created:function(){
        this.token = this.GetURLParameter("str");
        this.userAccount = this.GetURLParameter("userAccount");
        console.log(this.userAccount); 
        if(this.isCondition(this.token)){
             window.location.href=this.locationHost+"/lxjsdkH5/index/login.html";
        }
    },
    mounted:function(){	
        this.terraceAjax(); 
            
    },
    watch:{
    	picked:function(newValue,oldValue){
    		this.sdkAjax(newValue);
    	},
        userAccount:function(newValue,oldValue){
            this.sdkAjax(newValue);
            this.terraceAjax(newValue);
        }
    },
    filters: {
        status: function (value) {
            if (!value) return '';
            if(value == 0){
                value = "审核中";
            }else if(value == 1){
                value = "未通过";
            }else if(value == 2){
                value = "通过";
            }
            return value;
        },
        time: function(value){
            if (!value) return '';
            var da = value;
            da = new Date(da);
            var year = da.getFullYear();
            var month = da.getMonth()+1;
            var date = da.getDate();
            var value = [year,month,date].join('-');
            return value;
        },
        appType:function(value){
            if (!value) return '';
            if(value == 1) return "安卓";
            if(value == 2) return "ios";
        }
    }
    
})
