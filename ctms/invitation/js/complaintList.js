var topicSubjectId = GetURLParameter('topicSubjectId');//帖子id
var token = GetURLParameter("token");

//var ctmsHost = 'http://192.168.51.41:8090';
//var topicSubjectId ='1525333607572acc6e5467ed4f9babe1';
//var token = ''
var vue = new Vue({
	el:'#app',
	data:{
		lists:[//投诉列表
		],
		activeItem:{},
		isNull:true,
		pushing:false,//正在提交
		getReportCategoryApi:ctmsHost+'/ctmsApi/reportCategory/getReportCategory',
		addReportRecordApi:ctmsHost+'/ctmsApi/reportRecord/addReportRecord'
	},
	methods:{
		getData:function(){
			var _this = this;
			var time_stamp = new Date().getTime().toString();
			var params = {};
			params.header = {
				token : token,
				time_stamp : time_stamp
			};
			params.body = {
				pageNum:1,
				pageSize:99,
			};
			var paramData = JSON.stringify(params);
			console.log(params)
			this.$http.jsonp(this.getReportCategoryApi + "?str=" + encodeURI(paramData), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(response){
				var data = response.body;
				console.log(data)
				if(data.result === '0'){
					_this.lists = data.list
				}else{
					lxjTip.alert(data.reason)
				}
				
			},function(response2){
				lxjTip.alert(JSON.stringify(response2.body))
			})
		},
		selectItem:function(list){
			console.log(list)
			this.activeItem = list
			this.isNull = false
		},
		push:function(){
			var _this = this;
			if(!_this.pushing){//是否正在提交
				_this.pushing = true;
				var time_stamp = new Date().getTime().toString();
				var params = {};
				params.header = {
					token : token,
					time_stamp : time_stamp
				};
				params.body = {
					categoryId:_this.activeItem.id,
					categoryName:_this.activeItem.name,
					topicSubjectId:topicSubjectId
				};
				var paramData = JSON.stringify(params);
				console.log(params)
				
				this.$http.jsonp(this.addReportRecordApi + "?str=" + encodeURI(paramData), {
					emulateJSON: true,
					method: "post",
					dataType: "jsonp",
					jsonp: "jsoncallback",
					jsonpCallback: "success_jsonpCallback"
				}).then(function(response){
					var data = response.body;
					console.log(data)
					if(data.result === '0'){
						lxjTip.msg('提交成功', {
							time: 2000
						});
						setTimeout(function(){
							_this.pushing = false;
							goBack();
						},2000)
					}else{
						_this.pushing = false;
						lxjTip.alert('提交失败，请稍后再试!')
					}
					
				},function(response2){
					_this.pushing = false;
					lxjTip.alert('提交失败，请稍后再试!')
				})
			}
		}
	},
	mounted:function(){
		setTitle('举报');
		this.getData();
	},
	computed:{
	}
})
function refreshData() {
	setTimeout(function() {
//		vue.getData();
	}, 0);
	return 1;
}
