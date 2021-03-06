var onscrollStatus = false;
var pageSize = 10;
var pageNum = 1;
var clientType = GetURLParameter("clientType");
var orderStatus = GetURLParameter("orderStatus");
sessionStorage.setItem("host", host);
var token = GetURLParameter("token");

var token = "1521440720450af9b437f0f64879a6a3";
var host = "https://tt.hori-gz.com:8443";
//orderStatus="9";
var timestamp = new Date().getTime();
 if(orderStatus == 4) {
    setTitle("售后??");
} else {
    orderStatus = -1;
}
var backHomePage = GetURLParameter("backHomePage"); //回退到生活首??
if(backHomePage == "1") {
    backToHomePage();
}
setRefreshOnResume(); //刷新界面
var params = {};
params.header = {
    token: token,
    time_stamp: timestamp
};
params.body = {
    orderStatus: orderStatus,
    orderDetailNo: "",
    pageSize: pageSize,
    pageNum: pageNum
};
var vue = new Vue({
    el: '#app',
    data: {
        message: '999999',
        jsondate: {},
        orderList: [],
        presentTotal: 10,
        apiurl: host + "/mms/servlet/getCommodityOrderList", //?取??信息
        apiurl2: host + "/mms/servlet/getOrderStatusByOrderNo", //取消??前??
        apiurl3: host + "/mms/servlet/operateOrders", //??操作
        apiurl4: host + "/mms/servlet/getWelfareAfterConfirmReceived", //优惠推送
        apiurl5: host + "/mms/servlet/getOrderInfoByOrderNo" //支付前??
    },
    filters: {
        //????
        statusFlag: function(status, type) {
            var orderStatus;
            if(status == 0) { //代付款
                orderStatus = 'images/ic_obligation@3x.png';
            } else if(status == 1) { //待?Y?

                if(type == 1) { //商品??
                    orderStatus = 'images/ic_waitsend@3x.png';
                } else if(type == 3) { //服?U?W型??
                    orderStatus = 'images/ic_waitserve@3x.png';
                }
            } else if(status == 2) { //待收?
                if(type == 1) { //商品??
                    orderStatus = 'images/ic_waitget@3x.png';
                } else if(type == 3) { //服?U?W型??
                    orderStatus = 'images/ic_hadserve@3x.png';
                }
            } else if(status == 3) { //退款中
                orderStatus = 'images/ic_refund@3x.png';
            } else if(status == 4) { //售后?]理中
                orderStatus = 'images/ic_processing@3x.png';
            } else if(status == 5) { //交易成功
                orderStatus = 'images/ic_hadfinish@3x.png';
            } else if(status == 6) { //????
                orderStatus = 'images/ic_hadclose@3x.png';
            }
            return orderStatus;
        },

    },
    mounted: function() {
        this.$nextTick(function() {
            //console.log(99999999);
            this.getDatejson(pageSize, 1);
        });

    },
    methods: {
        getDatejson: function(pagesize, pageNum, refreshState,flag) {
            params.body.pageSize = pagesize;
            params.body.pageNum = pageNum;
            params.header.time_stamp=new Date().getTime();
            var paramData = JSON.stringify(params);
            this.$http.jsonp(this.apiurl + "?str=" + encodeURI(paramData), {
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                jsonpCallback: "success_jsonpCallback"
            }).then(function(response) {
                onscrollStatus = true;
                // ???成功回?
                console.log(JSON.stringify(response.body))
                //this.message = JSON.stringify(response.body);
                this.jsondate = response.body;
                if(response.body.result == 0) {

                    if(refreshState) {
                        this.orderList = response.body.orderList;
                    } else {
                        this.orderList = this.orderList.concat(response.body.orderList);
                    }
                    this.presentTotal = Math.ceil(this.orderList.length / 10) * 10;

                }
                if (flag==6&&response.body.result == 0) {
                    lxjTip.msg("??已?蝪?", {
                        time: 1000
                    });
                }

            }, function(response2) {
                // ?????回?
                onscrollStatus = true;
                console.log(JSON.stringify(response2.body))
                //	console.log(JSON.stringify(response))
            });
        },
        //??操作
        updateOrderFlag: function(flag, orderNo) { //flag:3取消??，2确?收???
            var params2 = {};
            params2.header = {
                token: token,
                time_stamp: timestamp
            };
            params2.body = {
                flag: flag,
                orderNo: orderNo
            };
            params2.header.time_stamp=new Date().getTime();
            var paramData2 = JSON.stringify(params2);
            this.$http.jsonp(this.apiurl3 + "?str=" + encodeURI(paramData2), {
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                jsonpCallback: "success_jsonpCallback"
            }).then(function(response) {
                // ???成功回?
                if(flag == 6&&response.body.result==0){
                    setTimeout(function(){
                        vue.getDatejson(vue.presentTotal, 1, true,6);
                    },100)
                }
                this.getDatejson(this.presentTotal, 1, true);
                if(flag == 2) {
                    this.getWel(2, orderNo);
                }
            }, function(response) {
                // ?????回?
                console.log("失?")
            });
        },
        //??确?收?送优惠
        getWel: function(flag, orderNo) { //flag:2确?收???
            var params2 = {};
            params2.header = {
                token: token,
                time_stamp: timestamp
            };
            params2.body = {
                flag: 2,
                orderNo: orderNo
            };
            params2.header.time_stamp=new Date().getTime();
            var paramData2 = JSON.stringify(params2);
            this.$http.jsonp(this.apiurl4 + "?str=" + encodeURI(paramData2), {
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                jsonpCallback: "success_jsonpCallback"
            }).then(function(response) {
                // ???成功回?
                this.getDatejson(this.presentTotal, 1, true);
                console.log(JSON.stringify(response.body))
            }, function(response) {
                // ?????回?
                this.getDatejson(this.presentTotal, 1, true);
                console.log("失?")
            });
        },
        //??取消前的??
        beforeCancel: function(orderNo) {
            var params2 = {};
            params2.header = {
                token: token,
                time_stamp: timestamp
            };
            params2.body = {
                orderStatus: "-1",
                orderNo: orderNo
            };
            params2.header.time_stamp=new Date().getTime();
            var paramData2 = JSON.stringify(params2);
            this.$http.jsonp(this.apiurl2 + "?str=" + encodeURI(paramData2), {
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                jsonpCallback: "success_jsonpCallback"
            }).then(function(response) {
                    // ???成功回?
                    if(response.body.result == 0) {
                        console.log(JSON.stringify(response.body))
                        var status = response.body.status;
                        var type = response.body.type;
                        if(status == 4 || status == 2) {
                            if(type == 3) {
                                lxjTip.msg("店?已安排服?U，不能取消??");

                            } else if(type == 1) {
                                lxjTip.msg("店?已?Y?，不能取消??");
                            }
                            vue.getDatejson(vue.presentTotal, 1, true);
                        } else {
                            if(status == 1) { //已支付??取消提示
                                lxjTip.confirm('??取消后款??在1-7?臚u作日?退?到您支付的???', {
                                    skin: 'demo3',
                                    btn: ['好的', '再想想'],
                                    yes: function(index) {
                                        vue.updateOrderFlag(3, orderNo);
                                        lxjTip.close(); //如果?z定了yes回?，需?行手工??
                                    }
                                });


                            } else if(status == 0) {
                                //未支付??取消提示
                                lxjTip.confirm('您确定要取消???？', {
                                    skin: 'demo3',
                                    yes: function(index) {
                                        vue.updateOrderFlag(3, orderNo);
                                        lxjTip.close(); //如果?z定了yes回?，需?行手工??
                                    }
                                });
                            }
                        }
                    }
                },
                function(response) {
                    // ?????回?
                    console.log("失?")
                });
        },
        //底部按?判?
        getButtom: function(status, expressage, comment) {
            if(status == 3 && expressage == 0) {
                return false;
            } else if(status == 4 && expressage == 0 && comment == 1) {
                return false;
            } else {
                return true;
            }
        },
        //?蝪???操作
        deleOrder: function(orderNo) {
            lxjTip.confirm('您确定要?蝪?????？', {
                skin: 'demo3',
                yes: function(index) {
                    vue.updateOrderFlag(6, orderNo);
                    lxjTip.close(); //如果?z定了yes回?，需?行手工??
                }
            });

        },
        //确?收?操作
        receivingProduct: function(orderNo) {
            lxjTip.confirm('您确定已?r收到?了?？', {
                skin: 'demo3',
                yes: function(index) {
                    vue.updateOrderFlag(2, orderNo);
                    lxjTip.close(); //如果?z定了yes回?，需?行手工??
                }
            });
        },
        //?入???情?麰?
        getOrderDetail: function(orderNo) {
            showActivity(host + "/mms/html5/personal/" + decodeURI("order_details.htm?orderNo=" + orderNo), "???情");
        },
        //?入支付?麰?
        goPay: function(orderNo, type) {
            //			if(type != 3) {
            //				showActivity(host + "/mms/html5/personal/" + decodeURI("pay.htm?orderNo=" + orderNo), "付款");
            //			} else {
            var params2 = {};
            params2.header = {
                token: token,
                time_stamp: timestamp
            };
            params2.body = {
                orderStatus: -1,
                orderNo: orderNo
            };
            params2.header.time_stamp=new Date().getTime();
            var paramData2 = JSON.stringify(params2);
            this.$http.jsonp(this.apiurl5 + "?str=" + encodeURI(paramData2), {
                emulateJSON: true,
                method: "get",
                dataType: "jsonp",
                jsonp: "jsoncallback",
                jsonpCallback: "success_jsonpCallback"
            }).then(function(response) {
                // ???成功回?
                console.log(JSON.stringify(response.body));
                var odata = response.body;
                if(odata.result == 0) {
                    if(odata.commodityStatus != 0) { //?有下架的商品在??中
                        if(odata.payTimeStatus != 0) { //????V超?
                            showActivity(host + "/mms/html5/personal/" + decodeURI("pay.htm?orderNo=" + orderNo), "付款");
                        } else {
                            lxjTip.alert('所?服?U??V已?,??即???！', {
                                skin: 'demo2',
                                yes: function(index) {
                                    vue.updateOrderFlag(3, orderNo);
                                    lxjTip.close(); //如果?z定了yes回?，需?行手工??
                                }
                            });
                        }
                    } else {
                        lxjTip.alert('??信息已更改，??即???,?重新??！', {
                            skin: 'demo2',
                            yes: function(index) {
                                vue.updateOrderFlag(3, orderNo);
                                lxjTip.close(); //如果?z定了yes回?，需?行手工??
                            }
                        });
                    }
                }
            }, function(response) {
                // ?????回?
                console.log("失?")
            });
            //}
        },
        //?入查看物流?麰?
        checkLogistics: function(orderNo) {
            showActivity(host + "/mms/html5/personal/" + decodeURI("checkLogistics.htm?orderNo=" + orderNo), "查看物流");
        },
        //?入申?售后?麰?
        goService: function(orderNo) {
            console.log(host + "/mms/html5/personal/" + decodeURI("service.htm?orderNo=" + orderNo))
            showActivity(host + "/mms/html5/personal/" + decodeURI("service.htm?orderNo=" + orderNo), "申?售后");
        },
        //?入?价?麰?
        goEvaluate: function(orderNo, length, commodityId) {
            if(length == 1) {
                showActivity(host + "/mms/html5/personal/" + decodeURI("writeproductEvaluate.htm?orderNo=" + orderNo + "&commodityId=" + commodityId), "商品?价");
            } else {
                showActivity(host + "/mms/html5/personal/" + decodeURI("productEvaluate.htm?orderNo=" + orderNo), "商品?价");
            }
        },
    }
});

//?取URL???
function GetURLParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r != null) return unescape(r[2]);
    return null;
}

function refreshData() {
    setTimeout(function() {
        vue.getDatejson(vue.presentTotal, 1, true);
        //		var listnumber = $(".orderInformation li").length;
        //		if(listnumber<10){
        //			listnumber=10;
        //			currentPageNum=1;
        //		}else{
        //			currentPageNum=Math.ceil(listnumber/10);
        //			listnumber=Math.ceil(listnumber/10)*10;
        //		}
        //		getOrderList(listnumber, true);
    }, 0);
    return 1;
}
//???V?蝳bY?上的???V距离
function getScrollTop() {
    var scrollTop = 0,
        bodyScrollTop = 0,
        documentScrollTop = 0;
    if(document.body) {
        bodyScrollTop = document.body.scrollTop;
    }
    if(document.documentElement) {
        documentScrollTop = document.documentElement.scrollTop;
    }
    scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
    return scrollTop;
}

//文?的?f高度

function getScrollHeight() {
    var scrollHeight = 0,
        bodyScrollHeight = 0,
        documentScrollHeight = 0;
    if(document.body) {
        bodyScrollHeight = document.body.scrollHeight;
    }
    if(document.documentElement) {
        documentScrollHeight = document.documentElement.scrollHeight;
    }
    scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight;
    return scrollHeight;
}

//??器?y口的高度

function getWindowHeight() {
    var windowHeight = 0;
    if(document.compatMode == "CSS1Compat") {
        windowHeight = document.documentElement.clientHeight;
    } else {
        windowHeight = document.body.clientHeight;
    }
    return windowHeight;
}

window.onscroll = function() {
    //console.log(getScrollTop()+'   '+getWindowHeight()+'   '+getScrollHeight())
    if(getScrollHeight() - getScrollTop() - getWindowHeight() < 1000 && onscrollStatus) {
        onscrollStatus = false;
        if(vue.orderList.length>=vue.presentTotal){
            pageNum=Math.ceil(vue.presentTotal / 10)+1;
        }else{
            pageNum=Math.ceil(vue.presentTotal / 10);
        }
        if (pageNum>1&&pageNum>Math.ceil(vue.presentTotal / 10)) {
            vue.getDatejson(10, pageNum);
        }
    }
};