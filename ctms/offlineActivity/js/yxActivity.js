var vue = new Vue({
	el: "#marketing",
	data: {
		showType: 1,
		banner: '',
		commodityList: [],
		collectFlag: true,
		noDataText4:'抱歉，没有更多了',
		setTime: 0,
		pageNum: 0,
		ctmsHost: mmsHost,
		titleName: '',		
		token: this.GetURLParameter("token"),
		areaCode: this.GetURLParameter("areaCode"),
		marketId: this.GetURLParameter("marketId"),
		organizationSeq: this.GetURLParameter("organizationSeq"),
		/*token: "15254043262505f71a6d2fa74919b79a",
		ctmsHost: 'https://tt.hori-gz.com:8443',
		marketId: '1522131205691fec9eb654ea4c3b88b5',
		organizationSeq: '4400100001'*/
	},
	methods: {
		infinite: function(done) {
			//this.noData="66666666"
			if(!this.collectFlag) {
				setTimeout(function() {
					done(true);
				}, 10)
				return;
			}
			setTimeout(function() {
				vue.marketingAjax(function() {
					done();
				})
			}, vue.setTime)
		},
		marketingAjax: function(doneFunc) {
			this.pageNum++;
			var params = {
				body: {
					pageNum: this.pageNum,
					pageSize: 10,
					organizationSeq: this.organizationSeq,
					marketId: this.marketId
				},
				header: {
					token: vue.token,
					time_stamp: new Date().getTime()
				}
			}
			var paramsStr = encodeURI(JSON.stringify(params));
			var httpURL = this.ctmsHost + "/mms/servlet/getRecommendRegionCommodity?str=" + paramsStr;
			this.$http.jsonp(httpURL, {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			}).then(function(res) {
				/*var u = navigator.userAgent; 
				var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); 
				if (isiOS) {
				    contact_setTitle(res.data.name);
				}else{
					setTitle(res.data.name);
				}*/

				if(vue.titleName == '') {
					vue.titleName = res.data.name;
					setTitle(res.data.name);					
					setTimeout(function() {
						if(GetURLParameter("clientType") == 'android') {
							setTitle(res.data.name ? res.data.name : "营销活动");
						} else {
							try {
								callNativeMethod('setNewtitle', {
									titleText: res.data.name ? res.data.name: "营销活动"
								});
							} catch(e) {}
						}
					}, 100)					
				}

				this.showType = res.data.showType;
				this.banner = res.data.banner;
				for(var i = 0; i < res.data.commodityList.length; i++) {
					this.commodityList.push(res.data.commodityList[i]);
				}
				if(res.data.commodityList.length < 10) {
					this.collectFlag = false
				}

				doneFunc();
				this.setTime = 500;
			}, function(res) {});
		},
		goDetail: function(id) {
			var url = this.ctmsHost + "/mms/html5/supermarket/ProductDetail.htm?commodityId=" + id + "&token=" + this.token + "&areaCode=" + this.areaCode;
			showActivity(url, "商品详情");
		},
		GetURLParameter: function(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if(r != null) return unescape(r[2]);
			return null;
		},
	},
	filters: {
		pricePre: function(value) {
			var val = parseInt(value);
			return val;
		},
		priceNext: function(value) {
			var val = '.' + String(parseFloat(value).toFixed(2)).split('.')[1];
			return val;
		}
	}
});

function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}