var vue = new Vue({
	el: ".collect",
	data: {
		columnItem: [],
		content: '',
		name: '',
		totalCount: '',
		picturePath: '',
		activeFlag: true,
		pullImg: "images/btn_ic_pull-down.png",
		columnFlag: true,
		pageNum: 0,
		fixedFlag: true,
		lengthFlag: false,
		token: GetURLParameter("token"),
		specialColumnId: GetURLParameter("specialColumnId"),
		ctmsHost: ctmsHost,
		organizationSeq: GetURLParameter("organizationSeq")
		//token:"1517377468946a8b8b48431341d4a7e9",
		/*specialColumnId:"234",
		ctmsHost:'http://192.168.51.69:8090',
		organizationSeq:"14"*/
	},
	filters: {
		times: function(value) {
			var value = value.substring(5, 16);
			return value;
		}
	},
	methods: {
		infinite: function(done) {
			if(!this.columnFlag) {
				done(true)
				return;
			}
			setTimeout(function() {
				vue.pageNum++;
				var params = {
					body: {
						pageSize: 10,
						pageNum: vue.pageNum,
						specialColumnId: vue.specialColumnId,
						organizationSeq:""
					},
					header: {
						token: "",
						time_stamp: new Date().getTime()
					}
				}
				var paramsStr = encodeURI(JSON.stringify(params));
				var httpURL = vue.ctmsHost + "/ctmsApi/specialColumn/shareSpecialColumnByDetails?str=" + paramsStr;
				vue.$http.jsonp(httpURL, {
					emulateJSON: true,
					method: "get",
					dataType: "jsonp",
					jsonp: "jsoncallback",
					contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
				}).then(function(res) {

					vue.GetLength(res.body.content) > 50 ? vue.lengthFlag = true : vue.lengthFlag = false;

					console.log(res);
					document.querySelector(".loading-layer").style.display = "block";
					document.querySelector(".special-main-header").style.opacity = 1;
					document.querySelector(".special-download-fixed").style.display = "block";
					vue.content = res.body.content;
					vue.name = res.body.name;
					vue.totalCount = res.body.totalCount;
					vue.picturePath = res.body.picturePath;
					for(var i = 0; i < res.body.specialList.length; i++) {
						vue.columnItem.push(res.body.specialList[i]);
					}
					if(res.body.specialList.length == 0 || res.body.specialList.length < 10) {
						vue.columnFlag = false;
					} else {
						vue.columnFlag = true;
					}
					
					setTimeout(function() {
						done()
					}, 0)

					//二次分享
					setShareInfo({
						title: res.body.name,
						summary: res.body.content,
						pic: res.body.picturePath,
						url: window.location.href
					});
				}, function(res) {});
				
			}, 500)
		},
		columnAjax: function() {
			
		},
		GetLength: function(str){  
		    return str.replace(/[^\x00-\xff]/g,"aa").length;  
		},
		closeFooter: function() {
			this.fixedFlag = false;
		},
		specialDetail: function(specialColumnId) {
			console.log(specialColumnId);
			var url = ctmsHost + '/ctmsH5/special/specialShare.html?id=' + specialColumnId + '&token=' + this.token + '&organizationSeq=' + this.organizationSeq + '&channelType=7';
			window.location.href = url;
		},
		GetURLParameter: function(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if(r != null) return unescape(r[2]);
			return null;
		},
		contentChange: function() {
			if(this.activeFlag == false) {
				this.activeFlag = true;
				this.pullImg = "images/btn_ic_pull-down.png"
			} else {
				this.activeFlag = false;
				this.pullImg = "images/btn_ic_pack-up.png"
			}
		}
	},
	mounted: function() {
		this.columnAjax();
	}
});

function getSharedata() {
	var jsonData = {
		title: vue.title,
		subTitle: vue.content,
		indexPic: vue.picturePath,
		url: ctmsHost + "/ctmsH5/special/specialMainShare.html?id=" + vue.specialColumnId
	};
	var jsonData2 = JSON.stringify(jsonData);
	nativeMethod("share", jsonData2);
}

new Mlink({
	mlink: 'https://aejlur.mlinks.cc/AbEk?specialColumnId=' + vue.specialColumnId,
	button: document.querySelector('a#goupdown')
});