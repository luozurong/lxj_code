 
var vue = new Vue({
    el: "#content",
    data: {
        mobile:"",
        code: "",
        userAccountFlag:false,
        codeFlag:false,
        sendFlag:false,
        userAccountFlagWord:"请输入正确手机号",
        codeFlagWord:"验证码不正确",
        time: "",
        sendAginFlag: false,                     //重发计时隐藏
        sendCodeFlag:true,                       //获取验证码显示
        sendClass: false,
        sureSubmit: [false,false],
        reasonFlag: false,                       //验证码消息隐藏
        reasonFlagWorld:"",
        successFlag: false,                      //修改密码成功提示
        successFlagWorld: "",
        codeAjaxFlag: false,
        codeAjaxFlagWorld: "",                   //验证码发送状态
        token:"",
        time_stamp:new Date().getTime(),
        host: host,
        locationHost:locationHost,
        /*host: "http://192.168.51.24:8090",
        locationHost:"http://192.168.51.33:8090",*/
    },
    methods:{
    	sendCode:function(){
            this.userAccountFunc();
            if(!this.userAccountFlag){
                if(this.sendFlag){
                     this.sendAjax();
                }
            }  
    	},
        retrieveSubmit:function(){
            this.retrieveSubmitFunc();
        },
        retrieveSubmitMouse:function(){
            this.retrieveSubmitFunc();
        },
        retrieveSubmitFunc:function(){
            this.userAccountFunc();
            this.codeFunc();
            if(this.sureSubmit[0] == true && this.sureSubmit[1] == true){
                this.retrieveAjax();
                console.log(this.sureSubmit)
            }else{
                console.log(this.sureSubmit)
            }
        },
        retrieveAjax:function(){
            var params = {
                body:{
                    code:this.code,
                    mobile:this.mobile
                },
                header:{
                    token:this.token,
                    time_stamp: this.time_stamp
                }
            }
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/getNewPassword?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            }).then(function(res){  
                if(res.body.result != 0){
                    this.successFlag = true;
                    this.successFlagWorld = res.body.reason;
                }else{
                    this.successFlag = true;
                    this.successFlagWorld = "密码修改成功";
                    window.location.href = this.locationHost+"/lxjsdkH5/index/login.html";
                   
                }
                console.log(res);
            },function(res){  
                console.log(res);  
            });
        },
        sendAjax:function(){
            var params = {
                body:{
                    mobile:this.mobile
                },
                header:{
                    token:this.token,
                    time_stamp: this.time_stamp
                }
            }
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/getMessageCode?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            }).then(function(res){  
                console.log(res);
                if(res.body.result != 0){
                    this.reasonFlag = true;
                    this.reasonFlagWorld = res.body.reason;
                }else{
                    sessionStorage.setItem("mobile",this.mobile);
                    this.time = 120;
                    this.reasonFlag = false;
                   
                    this.codeAjaxFlag = true;
                    this.codeAjaxFlagWorld = res.body.code;
                    if(res.body.code == "发送成功"){
                        this.sendAginFlag = true;
                        this.sendCodeFlag = false;
                    }else{
                        this.sendAginFlag = false;
                        this.sendCodeFlag = true;
                    }
                    setTimeout(function(){
                        vue.codeAjaxFlag = false;
                    },5000);
                }
            },function(res){  
                console.log(res);  
            });
        },
        userAccountFunc: function(){
            if(this.isCondition(this.mobile) || /^[0-9]{11,11}$/.test(this.mobile) == false){
                this.userAccountFlag = true;  
                this.sendFlag = false; 
                this.sureSubmit[0] = false;    
            }else{
                this.userAccountFlag = false;
                this.sendFlag = true;
                this.sureSubmit[0] = true;   
            }
        },
        codeFunc: function(){
            if(this.isCondition(this.code)){
                this.codeFlag = true;  
                this.sureSubmit[1] = false;   
            }else{
                this.codeFlag = false;
                this.sureSubmit[1] = true; 
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
                vue.retrieveSubmitMouse();
            }
        }
        if(!this.isCondition(sessionStorage.getItem("mobile"))){
            this.mobile = sessionStorage.getItem("mobile");
        }
    },
    watch:{
        time:function(newVal,oldVal){
            setTimeout(function(){
                if(vue.time >0){
                   vue.time -= 1; 
                   //sessionStorage.setItem("vueTime",vue.time); 
                }else{
                   vue.sendAginFlag = false; 
                   vue.sendCodeFlag = true;
                }
            },1000);
        },
        mobile:function(newVal,oldVal){
            if(!this.isCondition(newVal)){
                this.sendClass = true;
            }else{
                this.sendClass = false;
            }
        },
        code:function(newVal,oldVal){
            
        }
    }   
});
