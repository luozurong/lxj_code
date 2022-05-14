var vue = new Vue({
    el:"#sdkApply",
    data:{
        company: "",
        appName: "",
        appPackage: "",
        picked:"2",
        userAccount: "",
        companyFlag: false,
        appNameFlag: false,
        terraceCompany:"",
        appPackageFlag: false,
        companyFlagWord: "请填入公司名称",
        appNameFlagWord: "请填入应用名称",
        appPackageFlagWord: "请填入Bundle Id",
        appPakegeName:"Bundle Id：",
        placeHolder:"请填入Bundle Id",
        submitFlag:[false,false,false],
        host: host,
        locationHost:locationHost,
        /*host: "http://192.168.51.24:8090",
        locationHost:"http://192.168.51.33:8090",*/
        token:"",
        time_stamp: new Date().getTime()
    },
    methods:{
        sdkSubmit:function(){
            this.sdkSubmitFunc();
        },
        sdkSubmitMouse:function(){
            this.sdkSubmitFunc();
        },
        sdkSubmitFunc:function(){
            //this.companyFunc();
            this.appNameFunc();
            this.appPackageFunc();
            if(this.submitFlag[0] == true && this.submitFlag[1] == true){
                this.sdkAjax();
            }
        },
        sdkAjax:function(){      
            var params={
                body: {
                    company: this.terraceCompany,
                    appName: this.appName,
                    appPackage:this.appPackage,
                    userAccount: this.userAccount,
                    appType:this.picked
                },
                header: {
                    token: this.token,
                    time_stamp: this.time_stamp
                }       
            }
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/addApplication?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            }).then(function(res){  
                if(res.body.result == 0){
                    window.location.href=this.locationHost+"/lxjsdkH5/joint/apply.html?str="+this.token+"&userAccount="+this.userAccount;
                }
            },function(res){  
                console.log(res);  
            });
        },
         goUser:function(){
             window.location.href = (this.locationHost+"/lxjsdkH5/user/my.html?str="+this.token+"&userAccount="+(this.userAccount));
        },
        GetURLParameter: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r != null) return decodeURI(r[2]);
            return null;
        },
        /*companyFunc:function(){
            if(this.isCondition(this.company)){
                this.companyFlag = true;
                this.submitFlag[0] = false;
               
            }else{
                this.companyFlag = false;
                this.submitFlag[0] = true;
            }
        },*/
        appNameFunc:function(){
            if(this.isCondition(this.appName)){
                this.appNameFlag = true;
                this.submitFlag[0] = false;
               
            }else{
                this.appNameFlag = false;
                this.submitFlag[0] = true;
            }
        },
        appPackageFunc:function(){
            if(this.isCondition(this.appPackage)){
                this.appPackageFlag = true;
                this.submitFlag[1] = false;
               
            }else{
                this.appPackageFlag = false;
                this.submitFlag[1] = true;
            }
        },
        isCondition:function(param) {
            if(param == null || param == "" || param == undefined || param == "null" || new RegExp("^[ ]+$").test(param)) {
                return true;
            }
            return false;
        },

    },
    mounted:function(){
        this.token = this.GetURLParameter("str");
        this.userAccount = this.GetURLParameter("userAccount");
        this.terraceCompany = sessionStorage.getItem("company");
        console.log(this.token);
        if(this.isCondition(this.token)){
            // window.location.href=this.locationHost+"/lxjsdkH5/index/login.html";
        }

        document.onkeydown=function(e){
            if(window.event.keyCode==13){
                vue.sdkSubmitMouse();
            }
        }
    },
    watch:{
        picked:function(newVal,oldVal){
            console.log(newVal);
            if(newVal == 2){
                this.appPakegeName = "Bundle Id："
                this.placeHolder = "请填入Bundle Id";
                this.appPackageFlagWord = this.placeHolder;
            }else{
                this.appPakegeName = "App包名：";
                this.placeHolder = "请填入APP包名";
                this.appPackageFlagWord = this.placeHolder;
            }
        }
    }
});