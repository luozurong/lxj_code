var token = GetURLParameter("token");
//var token = '201805311000443431079';

var vue = new Vue({
	el:'#app',
	data:{
		lists:[
//		{id:'1',picturePath:'images/btn_ic_collect_pre_xxhdpi.png',name:'但是当时抠脚大叔到可视erect坐着名字',content:'我去玩群无'},
//		{id:'1',picturePath:'images/btn_ic_collect_pre_xxhdpi.png',name:'但是当时抠脚大叔到可视erect坐着名字',content:'我去玩群无'},
//		{id:'1',picturePath:'images/btn_ic_collect_pre_xxhdpi.png',name:'但是当时抠脚大叔到可视erect坐着名字',content:'我去玩群无'},
		],
		allAuthorApi:ctmsHost+'/ctmsApi/specialColumn/getAllRecommendSpecialColumnList',//api地址
	},
	mounted:function(){
		setTitle('全部');
		this.getData();
		
	},
	methods:{
		getData:function(){
			var time_stamp = new Date().getTime().toString();
			var params = {};
			params.header = {
				token : token,
				time_stamp : time_stamp
			};
			params.body = {
			};
			var paramData = JSON.stringify(params);
			
			this.$http.jsonp(this.allAuthorApi + "?str=" + encodeURI(paramData), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(response){
				var data = response.body;
				if(data.result === '0'){
//					console.log(data.list)
					this.lists = data.list;
				}else{
					lxjTip.alert(data.reason)
				}
				
			},function(response2){
				console.log(JSON.stringify(response2.body))
			})
		},
		authorDetail:function(id){
			var jsonData = {
				id: id, //作者id
			};
			var jsonData2 = JSON.stringify(jsonData);
			nativeMethod("goAuthorPage", jsonData2);
		}
	}
})
function refreshData() {
	setTimeout(function() {
		vue.getData();
	}, 0);
	return 1;
}