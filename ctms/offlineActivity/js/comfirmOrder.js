var onscrollStatus = false;
var clientType = GetURLParameter("clientType");
var areaCode = GetURLParameter("organizationSeq");
var householdSerial = GetURLParameter("householdSerial");
var activityId = GetURLParameter("activityId");
sessionStorage.setItem("host", host);
var token = GetURLParameter("token");
var lxjversionsNameC = GetURLParameter("lxjversionsName");
var receiverId = '';
//mmsHost="http://118.190.8.133:8090";

//var token = "150528852934af44ae94b162407787f6";
//areaCode = "4400100001";
//var activityId = '1505282301349ca2bf504cb74d3a8e80';

var vue = new Vue({
	el: '#app',
	data: {
		addressData: {},
		commodityData: {
			result: "0",
			reason: "",
			activityPrice: "0",
			activityImg: "images/c_s_error.png",
			activityName: "",
			leftCount: 20
		},
		count: 1,
		textContent: '',
		receiverId: receiverId,
		activityId: activityId,
		apiurl: mmsHost + "/mms/servlet/getUserReceiverInfo", //获取地址信息
		apiurl2: ctmsHost + "/ctmsApi/activity/confirmActivityInfo", //获取参团信息
		apiurl3: mmsHost + "/mms/servlet/saveSignUpActivityOrder", //提交参团报名信息
	},
	filters: {
		//状态过滤
	},
	mounted: function() {
		this.$nextTick(function() {
			//console.log(99999999);
			setLxjStorage("addressPageGo","2");
			this.getaddressDate(1);
			var text = document.getElementById("details_address");
			autoTextarea(text);

		});

	},
	methods: {
		getaddressDate: function(type, id) {
			var params = {};
			var timestamp = new Date().getTime();

			params.body = {
				type: type,
				id: id
			};
			params.header = {
				token: token,
				time_stamp: timestamp
			};
			var paramData = JSON.stringify(params);
			this.$http.jsonp(this.apiurl + "?str=" + encodeURI(paramData), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(response) {
				// 响应成功回调
				console.log(JSON.stringify(response.body))
				//this.message = JSON.stringify(response.body);
				if(response.body.result == 0) {
					if(type == 1 && response.body.full == 1 && response.body.receiverFlag == 1) {
						vue.receiverId = response.body.defaultReceiver.id;
						localStorage.setItem("receiverInfoId", vue.receiverId);
						this.addressData = response.body;
					} else if(type == 3) {
						vue.receiverId = response.body.receiver.id;
						localStorage.setItem("receiverInfoId", vue.receiverId);
						this.addressData = response.body;
					}
					vue.getDatejson2();
				}

			}, function(response2) {
				// 响应错误回调
				console.log(JSON.stringify(response2.body))
				//	console.log(JSON.stringify(response))
			});
		},
		getDatejson2: function() {
			var params2 = {};
			var timestamp = new Date().getTime();
			params2.header = {
				token: token,
				time_stamp: timestamp
			};
			params2.body = {
				activityId: activityId,
			};
			var paramData2 = JSON.stringify(params2);
			this.$http.jsonp(this.apiurl2 + "?str=" + encodeURI(paramData2), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(response) {
				// 响应成功回调	
				console.log(JSON.stringify(response.body))
				//this.message = JSON.stringify(response.body);
				if(response.body.result == 0) {
					this.commodityData = response.body;

				}

			}, function(response) {
				// 响应错误回调
				console.log("失败")
			});
		},
		goApply: function() {
			var jsonData = {
				eventId: "2.0-click23",
				eventName: "提交订单按键点击数"
			};
			jsonData = JSON.stringify(jsonData);
			//调用APP接口，添加百度统计
			nativeMethod("baiduStatistics", jsonData);
			var params3 = {};
			var timestamp = new Date().getTime();
			if(!vue.receiverId) {
				lxjTip.msg("请选择地址", {
					time: 2500
				});
				return false;
			}
			params3.header = {
				token: token,
				time_stamp: timestamp
			};
			params3.body = {
				organizationSeq: areaCode,
				receiverId: vue.receiverId,
				leaveMessage: vue.textContent,
				count: vue.count,
				activityId: vue.activityId,
				householdSerial: householdSerial,
			};
			var paramData3 = JSON.stringify(params3);
			this.$http.jsonp(this.apiurl3 + "?str=" + encodeURI(paramData3), {
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(response) {
				// 响应成功回调	
				console.log(JSON.stringify(response.body))
				//this.message = JSON.stringify(response.body);
				if(response.body.result == 0) {
					if(response.body.limitFlag == 1) {
						clearHistory();
						//					sessionStorage.setItem("successPage", "successfulappointment_supermarket.htm");
						//					sessionStorage.setItem("cancleOrderType", "1");
						//					sessionStorage.setItem("backStatus", "1"); //到支付界面点返回 回退到菜篮子
						var orderNo = response.body.totalOrderNo;
						var commitPrice = parseFloat(vue.commodityData.activityPrice * vue.count).toFixed(2);
						var consumePrice = 6;
						//sessionStorage.setItem("token", token);
						if(clientType == 'android' && sessionStorage.getItem('lxjversionsName') >= 5000) {
							setTimeout(function() {
								goBack();
							}, 0)
							showActivity(mmsHost + "/mms/html5/common/pay/finalstatement.htm" + "?orderNo=" + orderNo + "&cancleOrderType=1&backStatus=1&consumePrice=" + consumePrice + "&commitPrice=" + commitPrice + "&successPage=successfulappointment_supermarket.htm", '提交订单')
						} else {
							location.href = mmsHost + "/mms/html5/common/pay/finalstatement.htm" + "?orderNo=" + orderNo + "&cancleOrderType=1&backStatus=1&consumePrice=" + consumePrice + "&commitPrice=" + commitPrice + "&clientType=" + clientType + "&successPage=successfulappointment_supermarket.htm" + "&token=" + token + "&lxjversionsName=" + lxjversionsNameC;
						}
					} else {
						lxjTip.msg("订单状态异常！ ", {
							time: 3000
						});
					}
				} else {
					lxjTip.msg("订单状态异常！", {
						time: 3000
					});
				}

			}, function(response) {
				// 响应错误回调
				console.log("失败")
			});
		},
		//增加数量操作
		addData: function(count, leftCount) {
			if(count >= leftCount) {
				//				lxjTip.msg('亲，老板没那么多存货呢!', {
				//					time: 1500,
				//					skin: 'demo1'
				//				});
				return false;
			}
			if(count >= 100) {
				//				lxjTip.msg('亲，商品购买数量已达上限了!', {
				//					time: 1500,
				//					skin: 'demo1'
				//				});
				return false;
			}
			//newItem.shoppingCartCount = Number(item.shoppingCartCount) + 1;
			vue.count = Number(count) + 1;

		},
		//增加数量操作
		reduceData: function(count) {
			if(count == 1) {
				return false;
			}
			//newItem.shoppingCartCount = Number(item.shoppingCartCount) - 1;
			vue.count = count - 1;
		},
		//去地址页面
		goAddressPage: function() {
			if(clientType == "ios" && isCondition(lxjversionsNameC) && lxjversionsNameC.replace(/[^0-9]/ig, "") >= 4008&& lxjversionsNameC.replace(/[^0-9]/ig, "") <= 5180) {
				window.location.href = ctmsHost + "/ctmsH5/common/receiverInfo/receiveraddress.htm?receiverInfoId=" + localStorage.getItem("receiverInfoId") + "&clientType=" + clientType + "&token=" + token + "&lxjversionsName=" + lxjversionsNameC;
			} else {
				showActivity(ctmsHost + "/ctmsH5/common/receiverInfo/receiveraddress.htm?receiverInfoId=" + localStorage.getItem("receiverInfoId"), "选择地址");
			}
		},
		//增加数量操作
		//		inputChange: function(e, item) {
		//			var count = $("#" + item.id).siblings('.product_arguments').find("input").val();
		//			if(count <= 0) {
		//				$("#" + item.id).siblings('.product_arguments').find("input").val(1);
		//			}
		//			if(count > 0 && count <= 100 && item.stockQuantity >= count) {
		//
		//			} else if(count > 100) {
		//				lxjTip.msg('亲，商品购买数量已达上限了!', {
		//					time: 1500,
		//					skin: 'demo1'
		//				});
		//				$("#" + item.id).siblings('.product_arguments').find("input").val(100);
		//				count = 100;
		//			}
		//			if(item.stockQuantity < count) {
		//				lxjTip.msg('亲，老板没那么多存货呢!', {
		//					time: 1500,
		//					skin: 'demo1'
		//				});
		//				$("#" + item.id).siblings('.product_arguments').find("input").val(item.stockQuantity);
		//				count = item.stockQuantity;
		//			}
		//			vue.addShoppingcart(item.id, count, e, item);
		//		},

		//进入确认报名页面

	}
});

function focusOndetails() {
	document.getElementById("details_address").focus();
}
//获取URL参数
function GetURLParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}

function isCondition(param) { // (!typeof(a) === undefined)
	if(param != null && param != "" && param != undefined) {
		return true;
	}
	return false;
}

function sendWebMessage(items) {//getLxjStorage方法调用完毕，ios调用sendWebMessage回调内容
	var itemsObject = JSON.parse(items);
	
	var storageKey = itemsObject.storageKey;
	var storageContent = itemsObject.storageContent;
	if (storageKey=="addressPageGo"&&storageContent=="1") {
		getLxjStorage("receiverInfoId");	
	}else if (storageKey=="addressPageGo"&&storageContent=="2") {
		
	}
	if (storageKey=="receiverInfoId") {		
		localStorage.setItem(storageKey, storageContent);		
		setLxjStorage("addressPageGo","2");
		refreshNext();		
	}
	return 1;
}
function refreshNext() {
	var id = localStorage.getItem("receiverInfoId");
	if(isCondition(id)) {
		vue.getaddressDate(3, id);
	} else {
		vue.getaddressDate(1);
	}
}

function refreshData() {
	setTimeout(function() {
		if(clientType == "android" || sessionStorage.getItem("lxjversionsName") < 5180) {
			refreshNext();
		} else {
			getLxjStorage("addressPageGo");
		}

	}, 0);
	return 1;
}

function setLxjStorage(keyN, contN) {
	localStorage.setItem(keyN, contN);
	try {
		if(clientType == "android") {

		} else { //ios
			if(sessionStorage.getItem("lxjversionsName") >= 5103) {
				callNativeMethod('setLxjStorage', {
					storageKey: keyN,
					storageContent: contN ? contN : ""
				}, 0)
			}
		}
	} catch(e) {}
}

function getLxjStorage(keyN) {
	var returnText = "";
	try {
		if(clientType == "android") {
		} else { //ios
			if(sessionStorage.getItem("lxjversionsName") >= 5103) {
				callNativeMethod('getLxjStorage', {
					storageKey: keyN
				}, 0)
			}
		}
	} catch(e) {
	}
	return returnText;
}