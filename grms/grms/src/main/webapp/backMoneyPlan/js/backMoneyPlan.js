var pageNoAll=1;
var pageSizeAll=10;
var mapButton = {};
var userType;
//需要隐藏的按钮
var dataList;
//初始化按钮
$(function(){
	configMenuItem("收款管理","收款计划");
	initButton();
	dateBoxInit();
	timeselect();
	search();
});

function initButton(){
	mapButton['search'] = 1;
	mapButton['export'] = 1;
	mapButton['create'] = 1;
	mapButton['allBack'] = 1;
	
	mapButton['editPlan'] = 1;
	mapButton['delPlan'] = 1;
	mapButton['checkPlan'] = 1;
	mapButton['planDetail'] = 1;
	mapButton['withdraw'] = 1;
	var menuUrl="/grms/backMoneyPlan/list.html";
    $.ajax({  
        type : "POST",  //提交方式  
        url : "/grms/initButtonController/getButttonList",//路径  
        data : {"menuUrl":menuUrl},
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        async : false,
        success : function(result) {//返回数据根据结果进行相应的处理  
            if ( result.success) {
            	//比较需要隐藏的按钮
            	var buttonObject = result.button;
            	var admin = result.admin;
            	//如果是管理员
            	if(admin){
            		userType=0;
            		return;
            	}
            	var temp=1;
            	for(var prop in mapButton){
            		temp=1;
            		for(var j=0;j<buttonObject.length;j++){
            			if(prop==buttonObject[j].resourceCode){
            				temp=0;
            				break;
        				}
        			}
        			if(temp==1){
        				mapButton[prop]=0;
        			}
        		}
            	if(mapButton['search']==0){
            	  $("#search").hide();
              	}
            	if(mapButton['export']==0){
              	  $("#export").hide();
                }
            	if(mapButton['create']==0){
              	  $("#create").hide();
                }
            	if(mapButton['allBack']==0){
              	  $("#allBack").hide();
                }
            }
        }  
    }); 
}

//时间控件初始化
function dateBoxInit(){
    //格式化时间
    $("#startTime").datebox({
        editable:false,
        
    });
    //格式化时间
    $("#endTime").datebox({
        editable:false,

    });

}

//适用日期选择，格式化时间
function timeselect(){
	//var date=new Date();
    $("#startTime").datebox({
        onSelect : function(beginDate){
        	$('#endTime').datebox('calendar').calendar({
        		validator: function(date){
        			return beginDate<=date;//<=
        		}
        	});
        }
    });

    $("#endTime").datebox({
        onSelect : function(endDate){
        	$('#startTime').datebox('calendar').calendar({
        		validator: function(date){
        			return endDate>=date;//<=
        		}
        	});
        }
    });
}
//查询数据
function search(){
	var selectCondition =$('#selectCondition').val();
	var condition =$('#condition').val();
	var backMoneyStatus =$('#backMoneyStatus').val();
	var startTime = $('#startTime').datebox('getValue');
	var endTime = $('#endTime').datebox('getValue');
	pageNo=pageNoAll;
	pageSize=pageSizeAll;
    $.ajax({  
        type : "GET",  //提交方式  
        url : "/grms/backMoneyPlan/getBackMoneyPlanList",//路径  
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        data:  {"selectCondition":selectCondition,"condition":condition,"backMoneyStatus":backMoneyStatus,"startTime":startTime,"endTime":endTime,"pageNo":pageNo,"pageSize":pageSize},  
        success : function(result) {//返回数据根据结果进行相应的处理  
            if ( result.success) {  
            	dataList = result.data;
            	var total=result.total;
				initDg();
				paginationpage(total);
            } else {  
            	infoMask('加载列表失败');
            }  
        }  
    }); 
}

//刷新表格数据
function initDg(){
//	表格数据渲染
    $('#dg').datagrid({
        border:true,
        scrollbarSize:0,
        nowrap:false,//允许换行
        data:dataList,
        emptyMsg:'<span>无记录</span>',
        onLoadSuccess:function(){ //dom操作
            $('#dg').datagrid('resize');
        },
        columns:[[
      	        {
      	            field:'ck',
      	            title:'',
      	            checkbox:true,
      	            width:50,
      	            align:'center'
      	        },
      	        {
      	            field:'backMoneyPlanCode',
      	            title:'收款计划ID',
      	            width:50,
      	            align:'center',
      	        },
      	        {
      	            field:'projectCode',
      	            title:'项目ID',
      	            width:50,
      	            align:'center'
      	        },
      	        {
      	            field:'contractCode',
      	            title:'合同ID',
      	            width:50,
      	            align:'center',
      	        },
      	        {
      	            field:'contractName',
      	            title:'合同名称',
      	            width:50,
      	            align:'center',
      	        },
      	        {
      	            field:'name',
      	            title:'公司名称',
      	            width:50,
      	            align:'center'
      	        },
      	        {
      	            field:'createrName',
      	            title:'业务员',
      	            width:50,
      	            align:'center'
      	        },
      	        {
      	            field:'approveTime',
      	            title:'合同审批时间',
      	            width:50,
      	            align:'center'
      	        },
      	        {
      	            field:'money',
      	            title:'合同总金额',
      	            width:50,
      	            align:'center'
      	        },
      	        {
      	            field:'collectedMoney',
      	            title:'合同实收金额',
      	            width:50,
      	            align:'center'
      	        },
      	        {
      	            field:'unCollectedMoney',
      	            title:'合同未收金额',
      	            width:50,
      	            align:'center'
      	        },
      	        {
      	            field:'reduceMoney',
      	            title:'扣款金额',
      	            width:50,
      	            align:'center'
      	        }, 
      	        {
      	            field:'planStatus',
      	            title:'合同收款状态',
      	            width:50,
      	            align:'center'
      	        },
      	        {
      	            field:'handle',
      	            title: '操作',
      	            width: 200,
      	            align: 'center',
      	            formatter: function (value, row, index) {
      	            	
      	            	var lookStatistics = '';
      	            	var recordStatistics = '';
      	            	var remarkStatistics = '';
      	            	var checkStatistics = '';
      	            	var withdrawStatistics = '';
      	            	
      	        		/*lookStatistics = '<a href="javascript:;" onclick="editPlan(\''+row.backMoneyPlanCode+'\',\''+row.contractCode+'\')">编辑收款计划</a>';
      	        		recordStatistics = '<a href="javascript:;" onclick="delPlan(\''+row.backMoneyPlanCode+'\')">删除收款计划</a>';
      	        		remarkStatistics = '<a href="javascript:;" onclick="planDetail(\''+row.backMoneyPlanCode+'\',\''+row.contractCode+'\')">计划详情</a>';
      	        		checkStatistics = '<a href="javascript:;" onclick="checkPlan(\''+row.backMoneyPlanCode+'\',\''+row.contractCode+'\')">审核</a>';
      	        		withdrawStatistics = '<a href="javascript:;" onclick="withdraw(\''+row.backMoneyPlanCode+'\')">撤回</a>';*/
      	            	
      	        		if(row.checkStatus==0){//待审核、审核不通过（编辑、删除）
      	        			if(userType!=0){
      	        				if(mapButton['editPlan']==1){
      	        					lookStatistics = '<a href="javascript:;" onclick="editPlan(\''+row.backMoneyPlanCode+'\',\''+row.contractCode+'\')">编辑收款计划</a>';
      	        				}
      	        				if(mapButton['delPlan']==1){
    	          	        		recordStatistics = '<a href="javascript:;" onclick="delPlan(\''+row.backMoneyPlanCode+'\',\''+row.projectCode+'\')">删除收款计划</a>';
      	        				}
      	        				if(mapButton['checkPlan']==1){
    	          	        		checkStatistics = '<a href="javascript:;" onclick="checkPlan(\''+row.backMoneyPlanCode+'\',\''+row.contractCode+'\')">审核</a>';
      	        				}
      	        			}else{
  	        					lookStatistics = '<a href="javascript:;" onclick="editPlan(\''+row.backMoneyPlanCode+'\',\''+row.contractCode+'\')">编辑收款计划</a>';
	          	        		recordStatistics = '<a href="javascript:;" onclick="delPlan(\''+row.backMoneyPlanCode+'\',\''+row.projectCode+'\')">删除收款计划</a>';
	          	        		checkStatistics = '<a href="javascript:;" onclick="checkPlan(\''+row.backMoneyPlanCode+'\',\''+row.contractCode+'\')">审核</a>';
      	        			}
      	        		}else if(row.checkStatus==1){//已审核（）
      	        			if(userType!=0){
      	        				if(mapButton['planDetail']==1){
          	        				remarkStatistics = '<a href="javascript:;" onclick="planDetail(\''+row.backMoneyPlanCode+'\',\''+row.contractCode+'\')">计划详情</a>';
      	        				}
      	        				if(mapButton['withdraw']==1){
          	        				withdrawStatistics = '<a href="javascript:;" onclick="withdraw(\''+row.backMoneyPlanCode+'\')">撤回</a>';
      	        				}
      	        			}else{
      	        				remarkStatistics = '<a href="javascript:;" onclick="planDetail(\''+row.backMoneyPlanCode+'\',\''+row.contractCode+'\')">计划详情</a>';
      	        				withdrawStatistics = '<a href="javascript:;" onclick="withdraw(\''+row.backMoneyPlanCode+'\')">撤回</a>';
      	        			}
      	        		}else if(row.checkStatus==2){//已结案
      	        			if(userType!=0){
      	        				if(mapButton['planDetail']==1){
          	        				remarkStatistics = '<a href="javascript:;" onclick="planDetail(\''+row.backMoneyPlanCode+'\',\''+row.contractCode+'\')">计划详情</a>';
      	        				}
      	        			}else{
      	        				remarkStatistics = '<a href="javascript:;" onclick="planDetail(\''+row.backMoneyPlanCode+'\',\''+row.contractCode+'\')">计划详情</a>';
      	        			}
      	        			
      	        		}
      	        		
      	        		return lookStatistics + recordStatistics + remarkStatistics + checkStatistics + withdrawStatistics;
      	            }
      	        }
      	    ]]
    });
}

//跳转审核页
function checkPlan(backMoneyPlanCode,contractCode){
	window.location.href = "/grms/backMoneyPlan/jumpToCheckBackMoneyPlan?backMoneyPlanCode="+backMoneyPlanCode+"&contractCode="+contractCode;
}


//跳转编辑收款计划页
function editPlan(backMoneyPlanCode,contractCode){
	window.location.href = "/grms/backMoneyPlan/jumpToEditBackMoneyPlan?backMoneyPlanCode="+backMoneyPlanCode+"&contractCode="+contractCode;
}

//撤回
function withdraw(backMoneyPlanCode){
	$.ajax({  
        type : "POST",  //提交方式  
        url : "/grms/backMoneyPlan/withdrawBackMoneyPlan?backMoneyPlanCode="+backMoneyPlanCode,//路径  
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        success : function(result) {//返回数据根据结果进行相应的处理  
            if ( result.success) {  
            	infoMask(result.title);
            	location.reload();
            } else {  
            	infoMask('撤回失败');
            }  
        }  
    }); 
}

//删除收款计划
function delPlan(backMoneyPlanCode,projectCode){
	createConfirm("确认消息","确认删除该合同的所有收款计划？？",true,false,"确定","取消",okCallbakFunciton,"","","");
	 function okCallbakFunciton(){
		    $.ajax({  
		        type : "POST",  //提交方式  
		        url : "/grms/backMoneyPlan/delBackMoneyPlan?backMoneyPlanCode="+backMoneyPlanCode+"&projectCode="+projectCode,//路径  
		        dataType : "json",//数据，这里使用的是Json格式进行传输  
		        success : function(result) {//返回数据根据结果进行相应的处理  
		            if ( result.success) {  
		            	infoMask('删除成功');
		            	location.reload();
		            } else {  
		            	infoMask('删除失败');
		            }  
		        }  
		    }); 
	  }	
}

//跳转计划详情页
function planDetail(backMoneyPlanCode,contractCode){
	window.location.href = "/grms/backMoneyPlan/jumpToPlanDetail?backMoneyPlanCode="+backMoneyPlanCode+"&contractCode="+contractCode;
}


function paginationpage(total){
//  分页
    $('#pp').pagination({
    	total:total,
        layout:['list','first','prev','links','next','last','manual'],
        emptyMsg: '<span>无记录</span>',
        showRefresh:true,
        displayMsg:' ',
        pageSize:pageSizeAll,
        pageNumber:pageNoAll,
        pageList:[10,20,30],
        onSelectPage:function (pageNo, pageSize) {
        	pageNoAll=pageNo;
        	pageSizeAll=pageSize;
        	search();
        }
    });
    $(".pagination-page-list").parent().append("条");
    $(".pagination-page-list").parent().prepend("共计"+total+"条,每页显示： ");

}



//时间轴
$(document).ready(function(e) {
    var h = $(".about4_main ul li:first-child").height()/2;//<!--第一个li高度的一半-->
    var h1 = $(".about4_main ul li:last-child").height()/2;//<!--最后一个li高度的一半-->
    $(".line").css("top",h/2);
    $(".line").height($(".about4_main").height()-h1-h);
});



/****************搜索***********************/
function searchRecord(){
	
	$("#dg").datagrid('load', {
		selectCondition :$('#selectCondition').val(),
		condition :$('#condition').val(),
		backMoneyStatus :$('#backMoneyStatus').val(),
		startTime : $('#startTime').datebox('getValue'),
		endTime : $('#endTime').datebox('getValue'),
	});
}


/*$(".pagination-page-list").parent().append("条");
$(".pagination-page-list").parent().prepend("共计"+data2.length+"条,每页显示： ");
*/



/***************导出*****************/
function exportStatistics(){
	wrapMaskShow();
	$('#exportStatistics').dialog({
	    title: "消息确认",
	    width: 380,
	    height: 290,
	    draggable:false,
	    closed: false,
	    modal: true,
	    onClose:function(){
	    	wrapMaskHide();
	    },
	    buttons:[{
	        text:'取消',
	        handler:function(){
	            $('#exportStatistics').dialog('close');
	            wrapMaskHide();
	        }
	    },{
	        text:'确认',
	        handler:function(){
	        	var hasDataValue = $("#dg").datagrid("getChecked");
	        	var orderNumArr = new Array();
	        	for(var i=0;i<hasDataValue.length;i++){
	        		var orderNum = hasDataValue[i].orderNum;
	        		orderNumArr[i]=orderNum;
	        	}
	        	if(hasDataValue.length<=0){
	            	infoMask('请选择需要导出的记录！');
	            	
	            }else{
	            	window.location.href="/uoms/maintenanceRecordAction!exportToExcel.html?orderNumArr="+orderNumArr;
	            }
	            $('#exportStatistics').dialog('close');
	            wrapMaskHide();
	        }
	    }]
	});
}

/***************导出全部（确认）**********************/
function exportAll(){
	wrapMaskShow();
	$('#exportStatistics').dialog({
	    title: "消息确认",
	    width: 380,
	    height: 290,
	    draggable:false,
	    closed: false,
	    modal: true,
	    onClose:function(){
	    	wrapMaskHide();
	    },
	    buttons:[{
	        text:'取消',
	        handler:function(){
	            $('#exportStatistics').dialog('close');
	            wrapMaskHide();
	        }
	    },{
	        text:'确认',
	        handler:function(){
	        	var areas = $("#areas").val();
	            var operationPer = $("#operationPer").val();
	            var terminalName = $("#terminalName").val();
	            var deviceID = $("#deviceID").val();
	            /*var provinceCode = $("#province").combobox('getValue'); 
	        	var cityCode = $("#city").combobox('getValue'); 
	        	var countryCode = $("#country").combobox('getValue'); 
	        	var townCode = $("#town").combobox('getValue');*/
	            var provinceVal = $("#provinceCode").val(); 
	        	var cityVal = $("#cityCode").val(); 
	        	var countryVal = $("#countryCode").val(); 
	        	var townVal = $("#townCode").val();
	        	var startTime = $('#startTime').datebox('getValue');
	        	var endTime = $('#endTime').datebox('getValue');
	        	var stoppageCategory = $("#stoppageCategory input[type='radio']:checked").val();
	        	var stoppageSublevelCategory = $("#stoppageSubCategory input[type='radio']:checked").val();
	    		var deviceType = $("#deviceType input[type='radio']:checked").val();
	    		var sublevelType = $("#sublevelType input[type='radio']:checked").val();
	    		
	    		if(typeof(sublevelType)=="undefined"){ 
	    		    sublevelType = "";
	    		}else{
	    			sublevelType = $("#sublevelType input[type='radio']:checked").val();
	    		}
	        	window.location.href="/uoms/maintenanceRecordAction!exportToExcelAll.html?areas="
	        							+areas+"&operationPer="+operationPer+"&terminalName="+terminalName+"&deviceID="+deviceID+"&startTime="+startTime+"&endTime="
	        							+endTime+"&stoppageCategory="+stoppageCategory+"&stoppageSublevelCategory="+stoppageSublevelCategory+"&deviceType="+deviceType+"&sublevelType="
	        							+sublevelType+"&queryBean.province="+provinceVal+"&queryBean.city="+cityVal+"&queryBean.country="+countryVal+"&queryBean.town="+townVal;
	            $('#exportStatistics').dialog('close');
	            wrapMaskHide();
	        }
	    }]

	});
	searchRecord();
}



/***************导出（确认）***************/
$('#hintStatistics').dialog({
    title: "消息确认",
    draggable:false,
    width: 520,
    height: 360,
    closed: true,
    modal: true
});


function exportBackMoneyPlan(){
	var selectCondition =$('#selectCondition').val();
	var condition =$('#condition').val();
	var backMoneyStatus =$('#backMoneyStatus').val();
	var startTime = $('#startTime').datebox('getValue');
	var endTime = $('#endTime').datebox('getValue');
	window.location.href="/grms/backMoneyPlan/exportBackMoneyPlan?selectCondition="+selectCondition+"&condition="+condition
		+"&backMoneyStatus="+backMoneyStatus+"&startTime="+startTime+"&endTime="+endTime;
}



/****************合同列表***********************/
function createNew(){

	$('#allotWorkOrderTable').datagrid('options').url='/grms/backMoneyPlan/getContractList';
	$('#allotWorkOrderTable').datagrid('load',{pageNo:1});
	//显示遮罩
	wrapMaskShow();
	$('#allotWorkOrder').dialog('open');
}

winHiSelfAdaptation($('#allotWorkOrder'));
$('#allotWorkOrder').dialog({
  title: "合同管理列表",
  width: 800,
  height: 500,
  draggable:false,
  closed: true,
  modal: true,
  buttons:[{
      text:'取消',
      handler:function(){
          $('#allotWorkOrder').dialog('close');
        //隐藏遮罩
      	wrapMaskHide();
      }
  },{
      text:'确定',
      handler:function(){
          var row = $('#allotWorkOrderTable').datagrid('getChecked');
  			if(row !== null&&row.length!=0){
    			//隐藏遮罩
    			wrapMaskHide();
				var contractCode=row[0].contractCode;
				//var data =  {"projectCode":row.projectCode,"contractCode":row.contractCode,"contractName":row.contractName,"customerName":row.customerName,"createrName":row.createrName,"money":row.money,"approveTime":row.approveTime}; 
				window.location.href = "/grms/backMoneyPlan/jumpToCreateBackMoneyPlan?contractCode="+contractCode;
  			}else{
  				infoMask('请选择');
      		}
  		}
  }],
  onClose:function(){
  	wrapMaskHide();
  }
});
$(window).resize(function(){
	$('#dg').datagrid('resize');	
	setTimeout(function(){
		$("#allotWorkOrderTable .datagrid-view").css("height",$("#allotWorkOrderTable .datagrid-btable").height());
	},1000);
})


$('#allotWorkOrderTable').datagrid({
  nowrap:false,//允许换行
  autoSizeColumn: false,
 
  fitColumns: false,
  emptyMsg:'<span>无记录</span>',
  checkOnSelect:false,//点击该复选框的时候才会选中或取消
  singleSelect:true,
  onLoadSuccess: function (data) {//Fires when data is loaded successfully.
	   $('#allotWorkOrderTable').datagrid('resize');
		$('#pp-allotWorkOrderTable').pagination('refresh', {
			total: data.total,
			pageNumber: data.page
		});
		console.log(data);
	},
  columns:[[
      {
          field:'ck',
          checkbox:true,
          width:60,
          align:'left'
      },
      {
          field:'projectCode',
          title:'项目ID',
          width:100,
          align:'left'
      },
      {
          field:'contractCode',
          title:'合同ID',
          width:100,
          align:'left'
      },
      {
          field:'contractName',
          title:'合同名称',
          width:110,
          align:'left'
      },
      {
          field:'name',
          title:'公司名称',
          width:75,
          align:'left'
      },
      {
          field:'createrName',
          title:'业务员',
          width:72,
          align:'left'
      },
      {
          field:'money',
          title:'项目金额',
          width:70,
          align:'left'
      },
      {
          field:'approveTime',
          title:'审核时间',
          width:80,
          align:'left'
      }
  ]]
});
$('#pp-allotWorkOrderTable').pagination({
  //total:data2.length,
  layout:['prev','links','next','manual'],
  showRefresh:true,
  pageSize:10,
  links:3,
  onSelectPage:function (pageNo, pageSize) {
      var start = (pageNo - 1) * pageSize;
      var end = start + pageSize;
      var condition = $("#allot_maintainerName").val();
      $("#allotWorkOrderTable").datagrid("reload", {pageNo:pageNo,condition:condition});
  }
});

//搜索
function searchMaintainer(){
	$('#allotWorkOrderTable').datagrid('load', {
		condition : $.trim($("#allot_maintainerName").val()),
	});
}

//全部到账
function allBack(){
	var hasDataValue = $("#dg").datagrid("getChecked");
	var orderNumArr = new Array();
	var projectCodeArr = new Array();
	if(hasDataValue.length<=0){
		infoMask('请选择要到账的收款计划');
	}else{
		wrapMaskShow();
		$('#allbackDiv').dialog({
		    title: "请填写全部到账说明",
		    width: 380,
		    height: 290,
		    draggable:false,
		    closed: false,
		    modal: true,
		    onClose:function(){
		    	wrapMaskHide();
		    },
		    buttons:[{
		        text:'取消',
		        handler:function(){
		            $('#allbackDiv').dialog('close');
		            wrapMaskHide();
		        }
		    },{
		        text:'确认',
		        handler:function(){
		        	var allbackRemark = $("#allbackRemark").val();
		    		for(var i=0;i<hasDataValue.length;i++){
		    			var orderNum = hasDataValue[i].backMoneyPlanCode;
		    			orderNumArr[i]=orderNum;
		    			
		    			var projectCode = hasDataValue[i].projectCode;
		    			projectCodeArr[i]=projectCode;
		    		}
		    		$.ajax({  
		    			type : "POST",  //提交方式  
		    			url : "/grms/backMoneyPlan/allToAccount?backMoneyPlanCodes="+orderNumArr+"&allbackRemark="+allbackRemark+"&projectCodeArr="+projectCodeArr,//路径  
		    			dataType : "json",//数据，这里使用的是Json格式进行传输  
		    			success : function(result) {//返回数据根据结果进行相应的处理  
		    				if ( result.success) { 
		    					infoMask('全部到账');
		    					window.location.reload();
		    				} else {  
		    					if(result.title!=null){
		    						infoMask(result.title);
		    					}else{
		    						infoMask('到账异常');
		    					}
		    					
		    				}  
		    			}  
		    		});
		        	
		            $('#allbackDiv').dialog('close');
		            wrapMaskHide();
		        }
		    }]
		});
	}
}
