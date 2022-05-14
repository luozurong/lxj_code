var vue = new Vue({
	el:".phone",
	data: {
		clickFlag: 2,
		resultList: [],
		rootFlag: true,
		token: GetURLParameter("token"),
		ctmsHost: ctmsHost,
		organizationSeq: GetURLParameter("organizationSeq"),
		lxjversionsName: sessionStorage.getItem("lxjversionsName") ? sessionStorage.getItem("lxjversionsName") : 0,
		/*organizationSeq: "4400100001",
		ctmsHost: 'http://tt.hori-gz.com:8090',
		token: '1540522551782874d8e230f7403eb021'*/
	},
	methods:{
		clickNav: function(index){
			this.clickFlag = index;
			var params = {
				body:{
					organizationSeq: this.organizationSeq,
					phoneType: index.toString(),
				},
				header:{
					token: this.token,
					time_stamp: new Date().getTime()
				}
			};
			var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = this.ctmsHost + "/ctmsApi/convenientPhoneManagement/getPhone?str="+paramsStr;
            this.$http.jsonp(httpURL,{
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                contentType:'application/x-www-form-urlencoded; charset=UTF-8',
            }).then(function(res){
            	console.log(res);
            	this.resultList = res.body.resultList;
            	if(index == 2 && res.body.result == '037'){
            		this.rootFlag = false;
            	}
            },function(res){
            	console.log(res);
            });
		},
		callTel: function(i,phoneNo){
			/*5.2版本前没有该方法*/
			if(vue.lxjversionsName<=5108){
				return false;
			}
			var jsonData = {
				phoneType: i.toString(),
				phoneNum: phoneNo,
			};
			var jsonData2 = JSON.stringify(jsonData);
			nativeMethod("phoneLog",jsonData2);
		}
	},
	mounted: function(){
		this.clickNav(this.clickFlag);
	}
})
function GetURLParameter(name){
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}