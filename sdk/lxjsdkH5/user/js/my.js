var vue = new Vue({
	el:"#user",
	data:{
		oldPassword:"",
		newPassword:"",
		newPasswordAgain:"",
		oldPasswordFlag:false,
		newPasswordFlag:false,
		newPasswordAgainFlag:false,
		userTipFlag:false,
		userTipFlagWord:"",
		userSubmitFlag:[false,false,false],
		host: host,
        locationHost:locationHost,
        /*host: "http://192.168.51.24:8090",
        locationHost:"http://192.168.51.33:8090",*/
		token:"",
		time_stamp: new Date().getTime(),

	},
	methods:{
		userSubmit:function(){
			this.oldPasswordFunc();
			this.newPasswordFunc();
			this.newPasswordAgainFunc();
			if(this.userSubmitFlag[0] == true && this.userSubmitFlag[1] == true && this.userSubmitFlag[2] == true){
				console.log("修改成功");
				this.userSubmitAjax();
			}
		},
		userSubmitAjax:function(){
			 var params = {
                body:{
                    oldPassword:this.oldPassword,
                    newPassword:this.newPassword
                },
                header:{
                    token:this.token,
                    time_stamp:this.time_stamp
                }
            };
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/updatePassword?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
            }).then(function(res){  
               console.log(res);
               if(parseInt(res.body.result) == 0){
               	    this.userTipFlag = true;
               	    this.userTipFlagWord = "修改成功";
                    window.location.href= this.locationHost+"/lxjsdkH5/index/login.html";
               }else{
               		this.userTipFlag = true;
               		this.userTipFlagWord = res.body.reason;
               }
            },function(res){  
                 
            });
		},
		oldPasswordFunc: function(){
			if(this.isCondition(this.oldPassword) || /^[A-Za-z0-9]{6,20}$/.test(this.oldPassword) == false){
                this.oldPasswordFlag = true;
                this.userSubmitFlag[0] = false;
            }else{
                this.oldPasswordFlag = false;
                this.userSubmitFlag[0] = true;
            }
		},
		newPasswordFunc:function(){
			if(this.isCondition(this.newPassword) || /^[A-Za-z0-9]{6,20}$/.test(this.newPassword) == false){
                this.newPasswordFlag = true;
                this.userSubmitFlag[1] = false;
            }else{
                this.newPasswordFlag = false;
                this.userSubmitFlag[1] = true;
            }
		},
		newPasswordAgainFunc: function(){
			if(this.isCondition(this.newPasswordAgain) || /^[A-Za-z0-9]{6,20}$/.test(this.newPasswordAgain) == false || this.newPassword != this.newPasswordAgain){
                this.newPasswordAgainFlag = true;
                this.userSubmitFlag[2] = false;
            }else{
                this.newPasswordAgainFlag = false;
                this.userSubmitFlag[2] = true;
            }
		},
		isCondition:function(param) {
            if(param == null || param == "" || param == undefined || param == "null" || new RegExp("^[ ]+$").test(param) ) {
                return true;
            }
            return false;
        },
        GetURLParameter: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r != null) return unescape(r[2]);
            return null;
        }
	},
	mounted:function(){
		this.token = this.GetURLParameter("str");
	}
});