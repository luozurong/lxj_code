var vue = new Vue({
    el: "#register",
    data: {
        userAccount:"",
		mobile:"",
		password:"",
		passwordAgain:"",
		userAccountFlag: false,
		userAccountFlagWord: "请输入用户名(2-12位数字、字母)",
		registedFlag: false,
		registedFlagWord: "账户已存在",
		mobileFlag:false,
		mobileFlagWord:"请输入正确的手机号码",
		passwordFlag: false,
		passwordFlagWord: "请输入正确密码(字母或数字6-20位)",
		passwordAginFlag: false,
		passwordAginFlagWord: "两次输入密码不一致",
		submitArray:[false,false,false,false],
		registedFlag: false,
		time_stamp: new Date().getTime(),
		token: "",
		host: host,
        locationHost:locationHost,
		/*host: "http://192.168.51.24:8090",
        locationHost:"http://192.168.51.33:8090",*/
		successFlag: false,
		successFlagWord: "注册成功"
    },
    methods:{
    	registerSubmit:function(){
    		this.registerSubmitFunc();
    	},
    	registerSubmitMouse:function(){
    		this.registerSubmitFunc();
    	},
    	registerSubmitFunc:function(){ 
    		//window.event.cancelBubble=true;
			this.userAccountFunc();
			this.mobileAccountFunc();
			this.passwordAccountFunc();
			this.passwordAgainAccountFunc();
			if(this.submitArray[0] == true && this.submitArray[1] == true && this.submitArray[2] == true && this.submitArray[3] == true){
				console.log("登录成功");
				this.validateUser();
				this.registerAjax();
			} 
    	},
    	registerAjax:function(){ //注册
			var params = {
				body:{
					userAccount:this.userAccount,
					password:this.password,
					mobile:this.mobile
				},
				header:{
					token: this.token,
					time_stamp: this.time_stamp
				}
			}
		    var paramsStr = encodeURI(JSON.stringify(params));
			var httpURL = this.host+"/lxjsdkApi/addUser?str="+paramsStr;
			this.$http.jsonp(httpURL,{
			    emulateJSON: true,
				method: "post",
				dataType: "jsonp",
				jsonp: "jsoncallback",
		       
			}).then(function(res){  
            	if(res.body.result == 0){
            		this.successFlag = true;
            		this.successFlagWord = "注册成功";
        			setTimeout(function(){
        				vue.successFlag = false;
        				window.location.href = vue.locationHost+"/lxjsdkH5/index/login.html";
        			},3000);
            	}else{
            		this.successFlag = true;
            		this.successFlagWord = "注册失败";
            		setTimeout(function(){
        				vue.successFlag = false;
        			},3000);
            	}
            });  
    	},
    	validateUser:function(){ //验证用户是否注册
    		var params = {
				body:{
					userAccount:this.userAccount
				},
				header:{
					token: this.token,
					time_stamp: this.time_stamp
				}
			}
		    var paramsStr = encodeURI(JSON.stringify(params));
			var httpURL = this.host+"/lxjsdkApi/validateUser?str="+paramsStr;
			this.$http.jsonp(httpURL,{
			    emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
			}).then(function(res){  
            	this.registedFlag = res.body.registed;
            },function(res){  
                console.log(res);  
            });  
    	},
    	userAccountFunc:function(){
    		if(this.isCondition(this.userAccount) || /^[A-Za-z0-9]{2,12}$/.test(this.userAccount) == false){
				this.userAccountFlag = true;
				this.submitArray[0] = false;
			}else{
				this.userAccountFlag = false;
				this.submitArray[0] = true;
			}
    	},
    	mobileAccountFunc:function(){
    		if(this.isCondition(this.mobile) || /^[0-9]{11,11}$/.test(this.mobile) == false){
				this.mobileFlag = true;
				this.submitArray[1] = false;
			}else{
				this.mobileFlag = false;
				this.submitArray[1] = true;
			}
    	},
    	passwordAccountFunc:function(){
    		if(this.isCondition(this.password) || /^[A-Za-z0-9]{6,20}$/.test(this.password) == false){
				this.passwordFlag = true;
				this.submitArray[2] = false;
			}else{
				this.passwordFlag = false;
				this.submitArray[2] = true;
			}
    	},
    	passwordAgainAccountFunc:function(){
			if(this.password != this.passwordAgain || this.passwordAgain == ""){
				this.passwordAginFlag = true;
				this.submitArray[3] = false;
				this.passwordAginFlagWord = "两次输入密码不一致";
			}else{
				this.passwordAginFlag = false;
				this.submitArray[3] = true;
			}
    	},
    	userAccountBlur:function(){
    		this.userAccountFunc();
    	},
    	mobileAccountBlur:function(){
			this.mobileAccountFunc();
    	},
    	passwordAccountBlur:function(){
    		this.passwordAccountFunc();
    	},
    	passwordAgainAccountBlur:function(){
    		this.passwordAgainAccountFunc();
    	},
    	isCondition:function(param) {
			if(param == null || param == "" || param == undefined) {
				return true;
			}
			return false;
		}
    },
    mounted:function(){
    	document.onkeydown=function(e){
            if(window.event.keyCode==13){
                vue.registerSubmitMouse();
            }
        }
    }
});