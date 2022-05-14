var vue = new Vue({
    el: "#login",
    data: {
        userAccount:"",
		password:"",
		userFlag: false,
		userFlagWord: "请输入正确用户名(2-12位数字、字母)",
		pswFlag: false,
		pswFlagWord: "请输入正确密码(6-20位数字、字母)",
		noHasFlag:false,
		noHasFlagWord:"",
		loginSubmitFlag: [false,false],
		host: host,
        locationHost:locationHost,
        /*host: "http://192.168.51.24:8090",
        locationHost:"http://192.168.51.33:8090",*/
		time_stamp: new Date().getTime(),
		token: "",
    },
    methods:{
    	loginSubmit:function(){
    		this.loginSubmitFunc();
    	},
    	loginSubmitMouse:function(){
    		this.loginSubmitFunc();
    	},
    	loginSubmitFunc:function(){
			this.userAccountFun();
    		this.pswFlagFunc(); 		
    		if(this.loginSubmitFlag[0] == true && this.loginSubmitFlag[1] == true){
    			this.loginAjax();
    		}
    	},
    	loginAjax:function(){
    		var params = {
				body:{
					userAccount:this.userAccount,
					password:this.password
				},
				header:{
					token: this.token,
					time_stamp: this.time_stamp
				}
			}
		    var paramsStr = encodeURI(JSON.stringify(params));
			var httpURL = this.host+"/lxjsdkApi/login?str="+paramsStr;
			this.$http.jsonp(httpURL,{
			    emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
		       
			}).then(function(res){  
            	if(res.body.result != 0){
            		this.noHasFlag = true;
            		this.noHasFlagWord = res.body.reason;
            	}else{
            		this.token = res.body.token;
            		//window.location.href= "index.html?token="+this.token+"&userAccount="+this.userAccount;
            		window.location.href= "index.html";
            		sessionStorage.setItem("token", this.token);
            		sessionStorage.setItem("userStorage", this.userAccount);
            		sessionStorage.setItem("passwordStorage", this.password);
            		sessionStorage.setItem("userAccount",res.body.userAccount);
            		sessionStorage.setItem("token",res.body.token);
            	}
            	console.log(res);
            },function(res){  
                console.log(res);  
            });
    	},
    	userAccountFun:function(){
    		if(this.isCondition(this.userAccount) || /^[A-Za-z0-9]{2,12}$/.test(this.userAccount) == false){
				this.userFlag = true;
				this.loginSubmitFlag[0] = false;
				this.noHasFlag = false;
			}else{
				this.userFlag = false;
				this.loginSubmitFlag[0] = true;
				this.noHasFlag = false;
			}
    	},
    	pswFlagFunc:function(){
    		if(this.isCondition(this.password) || /^[A-Za-z0-9]{6,20}$/.test(this.password) == false){
				this.pswFlag = true;
				this.loginSubmitFlag[1] = false;
				this.noHasFlag = false;
			}else{
				this.pswFlag = false;
				this.loginSubmitFlag[1] = true;
				this.noHasFlag = false;
			}
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
                vue.loginSubmitMouse();
            }
        }
    }
});
