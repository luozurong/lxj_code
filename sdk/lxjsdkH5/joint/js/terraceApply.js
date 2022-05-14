 var vue = new Vue({
    el: "#terraceApply",
    data: {
        company: "",
        areaAddress: "",
        areaName: "",
        userAccount: "",
        companyFlag: false,
        addrFlag:false,
        areaCodeFlag: false,
        companyFlagWord: "请填入公司名称",
        addrFlagWord:"请选择地址",
        areaCodeFlagWord: "请填入小区",
    	getEstateFlag: true,
        inputEstateFlag: false,
        companySpan: "合力正通测试小区",
        province:"",
        city:"",
        area:"",
        street:"",
        provinceList:"",
        cityList:"",
        areaList:"",
        streetList:"",
        addrAllFlag:[false,false,false,false],
        submitFlag:[false,false,false],
        host: host,
        locationHost:locationHost,
        /*host: "http://192.168.51.24:8090",
        locationHost:"http://192.168.51.33:8090",*/
        time_stamp:new Date().getTime(),
        token:""
    },
    methods:{
    	getEstate:function(){
            this.companySpan = sessionStorage.getItem("company");
            var estateFlag = parseInt(sessionStorage.getItem("estateFlag"));
            if(estateFlag == 1){
                this.getEstateFlag = true;
                this.inputEstateFlag = false;

            }else{
                this.getEstateFlag = false;
                this.inputEstateFlag = true;
            }
        },
        addTerrace:function(){
            this.addTerraceFunc();
        },
        addTerraceMouse:function(){
            this.addTerraceFunc();
        },
        addTerraceFunc:function(){
            this.companyFunc();
            this.areaCodeFunc();
            if(!this.flagFunc(this.addrAllFlag)){
                this.addrFlag = true;
            }else{
                this.addrFlag = false;
            }

            if(this.submitFlag[0] == true && this.submitFlag[1] == true && this.submitFlag[2] == true){
                this.terraceAjax();
            }
        },
        terraceAjax:function(){ //获取平台账号密码
            var params = {
                body:{
                    company: this.company,
                    areaAddress: this.areaAddress,
                    areaName: this.areaName,
                    userAccount: this.userAccount
                },
                header:{
                    token:this.token,
                    time_stamp:this.time_stamp
                }
            }
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/addPlatFormJoin?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
            }).then(function(res){  
                if(res.body.result == 0){
                    window.location.href=this.locationHost+"/lxjsdkH5/joint/apply.html?str="+this.token+"&userAccount="+this.userAccount;
                }
                
            },function(res){  
                 
            });
        },
        initProvince:function(){
            var params = {
                body:{},
                header:{
                    token:this.token,
                    time_stamp:this.time_stamp
                }
            };
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/initProvince?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
            }).then(function(res){  
                if(res.body.result == 0){
                   this.provinceList= res.body.provinces; 
                }
            
            },function(res){  
                 
            });
        },
        initCity:function(val){
            var params = {
                body:{
                    code:val
                },
                header:{
                    token:this.token,
                    time_stamp:this.time_stamp
                }
            };
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/getCitySelectData?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
            }).then(function(res){  
                if(res.body.result == 0){
                   this.cityList= res.body.citys; 
                }
            },function(res){  
                 
            });
        },
        initArea:function(val){
            var params = {
                body:{
                    code:val
                },
                header:{
                    token:this.token,
                    time_stamp:this.time_stamp
                }
            };
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/getAreaSelectData?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
            }).then(function(res){  
                if(res.body.result == 0){
                   this.areaList= res.body.areas; 
                }
            },function(res){  
                 
            });
        },
        initStreet:function(val){
            var params = {
                body:{
                    code:val
                },
                header:{
                    token:this.token,
                    time_stamp:this.time_stamp
                }
            };
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/getStreetSelectData?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
            }).then(function(res){  
                if(res.body.result == 0){
                   this.streetList= res.body.streets; 
                }
            },function(res){  
                 
            });
        },
        goUser:function(){
             window.location.href = (this.locationHost+"/lxjsdkH5/user/my.html?str="+this.token+"&userAccount="+(this.userAccount));
        },
        companyFunc:function(){
            if(this.isCondition(this.company)){
                this.companyFlag = true;
                this.submitFlag[0] = false;
            }else{
                this.companyFlag = false;
                this.submitFlag[0] = true;
            }
        },
        areaCodeFunc:function(){
            if(this.isCondition(this.areaName)){
                this.areaCodeFlag = true;
                this.submitFlag[2] = false;
            }else{
                this.areaCodeFlag = false;
                this.submitFlag[2] = true;
            }
        },
        flagFunc:function(arr){
            if(this.arrayFunc(arr) == undefined){
                return true;
            }else{
                return false; 
            }
        },
        arrayFunc:function(arr){
            for(var i = 0;i< arr.length;i++){
                if( this.addrAllFlag[i] == false){
                    return false;
                }
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
            if(r != null) return decodeURI(r[2]);
            return null;
        },
    },
    mounted:function(){
        this.token = this.GetURLParameter("str");
        this.userAccount = this.GetURLParameter("userAccount");
        if(this.isCondition(this.token)){
             window.location.href=this.locationHost+"/lxjsdkH5/index/login.html";
        }
        this.getEstate();
        if(this.getEstateFlag){
            this.company = this.companySpan;
        }
        this.initProvince();
        if(this.isCondition(this.token)){
             window.location.href=this.locationHost+"/lxjsdkH5/index/login.html";
        }

        document.onkeydown=function(e){
            if(window.event.keyCode==13){
                vue.addTerraceMouse();
            }
        }
    },
    watch:{
        province:function(newVal,oldVal){
            if(this.isCondition(this.province)){
                this.addrAllFlag[0] = false;
            }else{
                 this.addrAllFlag[0] = true;
            }
            this.initCity(newVal.code);
            this.city = "";
            this.area = "";
            this.street = "";
        },
        city:function(newVal,oldVal){
            if(this.isCondition(this.city)){
                this.addrAllFlag[1] = false;
            }else{
                 this.addrAllFlag[1] = true;
            }
            this.initArea(newVal.code);
            this.area = "";
            this.street = "";
        },
        area:function(newVal,oldVal){
            if(this.isCondition(this.area)){
                this.addrAllFlag[2] = false;

            }else{
                this.addrAllFlag[2] = true;
                
            }
            this.initStreet(newVal.code);
            this.street = "";
        },
        street:function(newVal,oldVal){
            if(this.isCondition(this.street)){
                this.addrAllFlag[3] = false;
                this.submitFlag[1] = false;

            }else{
                this.addrAllFlag[3] = true;
                this.areaAddress = this.province.name+this.city.name+this.area.name+this.street.name;
                console.log(this.areaAddress);
                this.submitFlag[1] = true;
            }
        }
    }
});
