 
var vue = new Vue({
    el: "#indexWrap",
    data: {
    	loginBeforeFlag: true,
        loginAfterFlag: false,
        userAccount:"",
        sdkManageFlag:false,
        token:"",
        time_stamp:new Date().getTime(),
        host: host,
        locationHost:locationHost,
        /*host: "http://192.168.51.24:8090",
        locationHost:"http://192.168.51.33:8090",*/
    },
    methods:{
    	noLogin:function(){
            var params = {
                body:{},
                header:{
                    token:this.token,
                    time_stamp: this.time_stamp
                }
            }
            var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.host+"/lxjsdkApi/logout?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            }).then(function(res){ 
                this.loginBeforeFlag = true;
                this.loginAfterFlag = false;
                //window.location.href= "http://192.168.51.33:8090/lxjsdkH5/index/index.html";
                sessionStorage.setItem("token","");
                sessionStorage.setItem("userAccount","");
                this.token = "";
                this.userAccount = "";
            },function(res){  
                console.log(res);  
            });
    	},
        goApply:function(){
            window.location.href = (this.locationHost+"/lxjsdkH5/joint/apply.html?str="+this.token+"&userAccount="+(this.userAccount));
            console.log(encodeURI(this.locationHost+"/lxjsdkH5/joint/apply.html?str="+this.token+"&userAccount="+(this.userAccount)));
        },
        goUser:function(){
             window.location.href = (this.locationHost+"/lxjsdkH5/user/my.html?str="+this.token+"&userAccount="+(this.userAccount));
        },
        indexIphone:function(){
           // window.location.href = this.locationHost+"/lxjsdkH5/docCenter/docCenter.html?iframeId=docCenter8.html";
            console.log(".indexIphone");
            window.location.href="../docCenter/docCenter.html?iframeId=docCenter8.html"

        },
    	isCondition:function(param) {
			if(param == null || param == "" || param == undefined) {
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
    created:function(){
        //this.token = this.GetURLParameter("token");
        //this.userAccount = this.GetURLParameter("userAccount");
        this.token = sessionStorage.getItem("token");
        this.userAccount = sessionStorage.getItem("userAccount");
        console.log(this.token);
        if(this.isCondition(this.token)){
             this.loginBeforeFlag = true;
             this.loginAfterFlag = false;
             this.sdkManageFlag = false;
       
        }else{
             this.loginBeforeFlag = false;
             this.loginAfterFlag = true;
             console.log(1);
             this.sdkManageFlag = true;
        }
    },
    mounted:function(){
    	var mySwiper = new Swiper ('.swiper-container', {
		    loop: true,  
		    autoplay: 3000,
		    pagination: '.swiper-pagination'
		}); 
		
    }
});
