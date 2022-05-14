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
    setTitle("��Z??");
} else {
    orderStatus = -1;
}
var backHomePage = GetURLParameter("backHomePage"); //�^�h��ͬ�����
if(backHomePage == "1") {
    backToHomePage();
}
setRefreshOnResume(); //��s�ɭ�
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
        apiurl: host + "/mms/servlet/getCommodityOrderList", //?��??�H��
        apiurl2: host + "/mms/servlet/getOrderStatusByOrderNo", //����??�e??
        apiurl3: host + "/mms/servlet/operateOrders", //??�ާ@
        apiurl4: host + "/mms/servlet/getWelfareAfterConfirmReceived", //ɬ�f���e
        apiurl5: host + "/mms/servlet/getOrderInfoByOrderNo" //��I�e??
    },
    filters: {
        //????
        statusFlag: function(status, type) {
            var orderStatus;
            if(status == 0) { //�N�I��
                orderStatus = 'images/ic_obligation@3x.png';
            } else if(status == 1) { //�݉Y?

                if(type == 1) { //�ӫ~??
                    orderStatus = 'images/ic_waitsend@3x.png';
                } else if(type == 3) { //�A�U�W��??
                    orderStatus = 'images/ic_waitserve@3x.png';
                }
            } else if(status == 2) { //�ݦ�?
                if(type == 1) { //�ӫ~??
                    orderStatus = 'images/ic_waitget@3x.png';
                } else if(type == 3) { //�A�U�W��??
                    orderStatus = 'images/ic_hadserve@3x.png';
                }
            } else if(status == 3) { //�h�ڤ�
                orderStatus = 'images/ic_refund@3x.png';
            } else if(status == 4) { //��Z�]�z��
                orderStatus = 'images/ic_processing@3x.png';
            } else if(status == 5) { //������\
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
                // ��?���\�^?
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
                    lxjTip.msg("??�w�簣", {
                        time: 1000
                    });
                }

            }, function(response2) {
                // ��???�^?
                onscrollStatus = true;
                console.log(JSON.stringify(response2.body))
                //	console.log(JSON.stringify(response))
            });
        },
        //??�ާ@
        updateOrderFlag: function(flag, orderNo) { //flag:3����??�A2��?��???
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
                // ��?���\�^?
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
                // ��???�^?
                console.log("��?")
            });
        },
        //??��?��?�eɬ�f
        getWel: function(flag, orderNo) { //flag:2��?��???
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
                // ��?���\�^?
                this.getDatejson(this.presentTotal, 1, true);
                console.log(JSON.stringify(response.body))
            }, function(response) {
                // ��???�^?
                this.getDatejson(this.presentTotal, 1, true);
                console.log("��?")
            });
        },
        //??�����e��??
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
                    // ��?���\�^?
                    if(response.body.result == 0) {
                        console.log(JSON.stringify(response.body))
                        var status = response.body.status;
                        var type = response.body.type;
                        if(status == 4 || status == 2) {
                            if(type == 3) {
                                lxjTip.msg("��?�w�w�ƪA�U�A�������??");

                            } else if(type == 1) {
                                lxjTip.msg("��?�w�Y?�A�������??");
                            }
                            vue.getDatejson(vue.presentTotal, 1, true);
                        } else {
                            if(status == 1) { //�w��I??��������
                                lxjTip.confirm('??�����Z��??�b1-7�Ĥu�@��?�h?��z��I��?��', {
                                    skin: 'demo3',
                                    btn: ['�n��', '�A�Q�Q'],
                                    yes: function(index) {
                                        vue.updateOrderFlag(3, orderNo);
                                        lxjTip.close(); //�p�G�z�w�Fyes�^?�A��?���u??
                                    }
                                });


                            } else if(status == 0) {
                                //����I??��������
                                lxjTip.confirm('�z�̩w�n����???�H', {
                                    skin: 'demo3',
                                    yes: function(index) {
                                        vue.updateOrderFlag(3, orderNo);
                                        lxjTip.close(); //�p�G�z�w�Fyes�^?�A��?���u??
                                    }
                                });
                            }
                        }
                    }
                },
                function(response) {
                    // ��???�^?
                    console.log("��?")
                });
        },
        //������?�P?
        getButtom: function(status, expressage, comment) {
            if(status == 3 && expressage == 0) {
                return false;
            } else if(status == 4 && expressage == 0 && comment == 1) {
                return false;
            } else {
                return true;
            }
        },
        //�簣??�ާ@
        deleOrder: function(orderNo) {
            lxjTip.confirm('�z�̩w�n�簣????�H', {
                skin: 'demo3',
                yes: function(index) {
                    vue.updateOrderFlag(6, orderNo);
                    lxjTip.close(); //�p�G�z�w�Fyes�^?�A��?���u??
                }
            });

        },
        //��?��?�ާ@
        receivingProduct: function(orderNo) {
            lxjTip.confirm('�z�̩w�w�r����?�F?�H', {
                skin: 'demo3',
                yes: function(index) {
                    vue.updateOrderFlag(2, orderNo);
                    lxjTip.close(); //�p�G�z�w�Fyes�^?�A��?���u??
                }
            });
        },
        //?�J???���ﭱ
        getOrderDetail: function(orderNo) {
            showActivity(host + "/mms/html5/personal/" + decodeURI("order_details.htm?orderNo=" + orderNo), "???��");
        },
        //?�J��I�ﭱ
        goPay: function(orderNo, type) {
            //			if(type != 3) {
            //				showActivity(host + "/mms/html5/personal/" + decodeURI("pay.htm?orderNo=" + orderNo), "�I��");
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
                // ��?���\�^?
                console.log(JSON.stringify(response.body));
                var odata = response.body;
                if(odata.result == 0) {
                    if(odata.commodityStatus != 0) { //?���U�[���ӫ~�b??��
                        if(odata.payTimeStatus != 0) { //???�V�W?
                            showActivity(host + "/mms/html5/personal/" + decodeURI("pay.htm?orderNo=" + orderNo), "�I��");
                        } else {
                            lxjTip.alert('��?�A�U?�V�w?,??�Y???�I', {
                                skin: 'demo2',
                                yes: function(index) {
                                    vue.updateOrderFlag(3, orderNo);
                                    lxjTip.close(); //�p�G�z�w�Fyes�^?�A��?���u??
                                }
                            });
                        }
                    } else {
                        lxjTip.alert('??�H���w���A??�Y???,?���s??�I', {
                            skin: 'demo2',
                            yes: function(index) {
                                vue.updateOrderFlag(3, orderNo);
                                lxjTip.close(); //�p�G�z�w�Fyes�^?�A��?���u??
                            }
                        });
                    }
                }
            }, function(response) {
                // ��???�^?
                console.log("��?")
            });
            //}
        },
        //?�J�d�ݪ��y�ﭱ
        checkLogistics: function(orderNo) {
            showActivity(host + "/mms/html5/personal/" + decodeURI("checkLogistics.htm?orderNo=" + orderNo), "�d�ݪ��y");
        },
        //?�J��?��Z�ﭱ
        goService: function(orderNo) {
            console.log(host + "/mms/html5/personal/" + decodeURI("service.htm?orderNo=" + orderNo))
            showActivity(host + "/mms/html5/personal/" + decodeURI("service.htm?orderNo=" + orderNo), "��?��Z");
        },
        //?�J?ɲ�ﭱ
        goEvaluate: function(orderNo, length, commodityId) {
            if(length == 1) {
                showActivity(host + "/mms/html5/personal/" + decodeURI("writeproductEvaluate.htm?orderNo=" + orderNo + "&commodityId=" + commodityId), "�ӫ~?ɲ");
            } else {
                showActivity(host + "/mms/html5/personal/" + decodeURI("productEvaluate.htm?orderNo=" + orderNo), "�ӫ~?ɲ");
            }
        },
    }
});

//?��URL��?
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
//��V��bY?�W����V�Z��
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

//��?���f����

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

//??���y�f������

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