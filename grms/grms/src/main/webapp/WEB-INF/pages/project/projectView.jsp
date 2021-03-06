<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*" %>
<%@ page language="java" import="com.hori.vo.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html style="height: 100%;">
	<head>
	<meta charset="UTF-8">
	<title>查看项目</title>
	<meta http-equiv="X-UA-Compatible" content="edge" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/project/css/project.css" />
    <script src="<%=basePath%>/common/easyui/jquery.min.js"></script>
    <script src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/common/plugin/common.js"></script>
    <link rel="shortcut icon" href="<%=basePath%>/homepage/images/ywhlzt.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/project/css/addProject.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/homepage/css/index.css"/>
	    <style type="text/css">
	    	.wrap{
	    		margin: 0 20px 20px;
	    		font-size: 14px;
	    	}
	    	.body-box{
	    		padding-left: 50px;
	    		background: #fff;
	    	}
	    	.table-wrap{
	    		width: 100%;
	    		overflow: hidden;
	    	}
	    	.table-wrap>div{
	    		width: 30%;
	    		float: left;
	    		line-height: 50px;
	    		font-size: 14px;
	    	}
	    	.table-wrap>div.table-width{
	    		width: 50%;
	    	}
	    	.table-wrap-three{
	    		overflow:hidden;
	    	}
	    	.table-wrap-three span{
	    		float:left;
	    		line-height: 50px;
	    	}
	    	.table-wrap-three>div{
	    		float: left;
	    		line-height:50px;
	    		width:70%;
	    	}
	    	.line-borders:last-child{
	    		border-bottom:none;
	    	}
	    	
	    	.projectProductList_Box .datagrid-row-checked{
	    	  background-color:#fff!important;
	    	}
	    	.projectProductList_Box .datagrid-header td,.projectProductList_Box .datagrid-body td,.projectProductList_Box .datagrid-footer td {
			        border-width: 0 1px 1px 0;
			}
			.projectProductList_Box .datagrid-btable tbody tr:nth-child(even) {
			    background: #fff;
			}
			#projectProductListDiv{
				background: #fff;
			}
			.line-borders span{
				font-size: 14px;
			}
			.line-borders:first-child{
				border-top: none !important;
			}
			.panel-tool a{
				display:none;
			}
	    </style>	
	    
	<script type="text/javascript">
		//填充资源清单表格
		function getResourceView(sss,$eleS){
			console.log(sss)
			var list1 = [];
			var list2 = [];
			var list3 = [];
			var dataList = [];
			var si = 0;
			for(var i = 0; i < sss.length; i++) {
				for(var j = 0; j < sss[i].projectMenus.length; j++) {
					if(sss[i].projectMenus.length > 1 && j == 0) {
						if(sss[i].businessName == "社区运营") {//社区运营和其他类型合并规则区分
							list1.push({
								index: si,
								rowspan: sss[i].projectMenus.length
							});
							list2.push({
								index: si,
								rowspan: sss[i].projectMenus.length
							});
							list3.push({
								index: si,
								rowspan: sss[i].projectMenus.length
							});
						} else {
							list2.push({
								index: si,
								rowspan: sss[i].projectMenus.length
							});
						}
					}
					si++;
					dataList.push({
						fieldName: sss[i].fieldName,
						businessName: sss[i].businessName,
						businessType: sss[i].businessType,
						productType: sss[i].projectMenus[j].productType,
						productMenu: sss[i].projectMenus[j].productMenu,
						productSpec: sss[i].projectMenus[j].productSpec,
						areaNames: sss[i].projectMenus[j].areaNames,
						beginTime: sss[i].projectMenus[j].beginTime,
						endTime: sss[i].projectMenus[j].endTime,
						buyNum: sss[i].projectMenus[j].buyNum
					})
				}
			}
			$eleS.datagrid({
				data: dataList,
				fitColumns: true,
				nowrap: false,
				scrollbarSize: 0,
				onLoadSuccess: function(data) {
					$(".datagrid-cell-check").addClass('dataCheck');
					$(".datagrid-header-check").addClass('dataCheck');
					for(var a = 0; a < list1.length; a++) {
						$eleS.datagrid('mergeCells', { //第一列合并
							index: list1[a].index,
							field: 'fieldName',
							rowspan: list1[a].rowspan
						});
					}
					for(var b = 0; b < list2.length; b++) {
						$eleS.datagrid('mergeCells', { //第一列合并
							index: list2[b].index,
							field: 'businessName',
							rowspan: list2[b].rowspan
						});
					}
					for(var c = 0; c < list3.length; c++) {
						$eleS.datagrid('mergeCells', { //第一列合并
							index: list3[c].index,
							field: 'areaNames',
							rowspan: list3[c].rowspan
						});
					}
					$eleS.parent().find('td[field=areaNames] .datagrid-cell').each(function() {
						$(this).attr('title', $(this).html());
					});
					setTimeout(function(){
						$eleS.datagrid('resize');
					},30)
				},
				columns: [
					[ //相应调整显示栏
						{
							field: 'fieldName',
							title: '场次名称',
							width: 100,
							align: "center",
							formatter: function(value,row,index){
								if(value==''||value==null||value=="undefined"){
									return "/";
								}else{
									return value;
								}	
							}
						},
						{
							field: 'businessName',
							title: '业务',
							width: 110,
							align: "center"
						},
						{
							field: 'productType',
							title: '类型',
							width: 100,
							align: "center"
						},
						{
							field: 'productMenu',
							title: '产品清单',
							width: 80,
							align: "center"
						},
						{
							field: 'productSpec',
							title: '产品规格',
							width: 80,
							align: "center"
						},
						{
							field: 'buyNum',
							title: '购买数量',
							width: 80,
							align: "center"
						},
						{
							field: 'beginTime',
							title: '执行开始时间',
							width: 120,
							align: "center",
							formatter: function(value,row,index){
								if(row.businessType=='1'){
									return value;
								}else{
									return value.substr(0,11);
								}	
							}
						},
						{
							field: 'endTime',
							title: '执行结束时间',
							width: 120,
							align: "center",
							formatter: function(value,row,index){
								if(row.businessType=='1'){
									return value;
								}else{
									return value.substr(0,11);
								}	
							}
						},
						{
							field: 'areaNames',
							title: '已选小区',
							width: 230,
							align: "center"
						}
					]
				]
			});
		}
		
		//下载资源清单列表
		function download(){
			//wrapMaskShow();
			 createConfirm("下载","是否下载资源清单？",true,false,"确定","取消",okCallbakFunciton3,"","","");
			 function okCallbakFunciton3(){
		         location.href="<%=basePath%>/project/exportDetail?projectCode=${projectVo.projectCode}"
 
			  }

		}
		
		//跳转到收款列表
		function planback(id,code){
			location.href="<%=basePath%>/backMoneyPlan/jumpToPlanDetail?backMoneyPlanCode="+code+"&contractCode="+id;
		}
		
		function actionback(id){
			location.href="<%=basePath%>/zhongheDepart/actionDetail.do?actionCode="+id;
		}

		//下载附件
		function attachmentDownload(id){
			//wrapMaskShow();
			 createConfirm("下载","是否下载附件？",true,false,"确定","取消",okCallbakFunciton4,"","","");
			 function okCallbakFunciton4(){
		         location.href="<%=basePath%>/project/download?id="+id;    //http://127.0.0.1:8090/grms/project/download?id=15483365915155484   其中id为附件id
 
			  }

		}
		
		
		//填充执行清单表格
		function getProjectAction(sss,$eleS){
			var dataList = [];
			for(var i = 0; i < sss.length; i++) {
				dataList.push({
					beginTime: sss[i].beginTime,
					areaName: sss[i].areaName,
					provinceName: sss[i].provinceName,
					cityName: sss[i].cityName,
					countryName: sss[i].countryName,
					areaAddress: sss[i].areaAddress,
					businessType: sss[i].businessType,
					projectName: sss[i].projectName,
					fieldName: sss[i].fieldName,
					actionStatus: sss[i].actionStatus,
					exceptionStatus: sss[i].exceptionStatus,
					actionCode: sss[i].actionCode
				})
			}
			$eleS.datagrid({
				data: dataList,
				fitColumns: true,
				nowrap: false,
				scrollbarSize: 0,
				onLoadSuccess: function(data) {
					$(".datagrid-cell-check").addClass('dataCheck');
					$(".datagrid-header-check").addClass('dataCheck');
					$eleS.parent().find('td[field=projectProductId] .datagrid-cell').each(function() {
						$(this).attr('title', $(this).html());
					});
					setTimeout(function(){
						$eleS.datagrid('resize');
					},30)
				},
				columns: [
					[ //相应调整显示栏
						{
							field: 'beginTime',
							title: '执行日期',
							width: 100,
							align: "center"
						},
						{
							field: 'areaName',
							title: '小区名称',
							width: 80,
							align: "center",
							formatter: function(value,row,index){
								if(value==''||value==null||value=="undefined"){
									return "/";
								}else{
									return value;
								}	
							}
						},
						{
							field: 'provinceName',
							title: '省份',
							width: 80,
							align: "center",
							formatter: function(value,row,index){
								if(value==''||value==null||value=="undefined"){
									return "/";
								}else{
									return value;
								}	
							}
						},
						{
							field: 'cityName',
							title: '城市',
							width: 80,
							align: "center",
							formatter: function(value,row,index){
								if(value==''||value==null||value=="undefined"){
									return "/";
								}else{
									return value;
								}	
							}
						},
						{
							field: 'countryName',
							title: '区（县）',
							width: 80,
							align: "center",
							formatter: function(value,row,index){
								if(value==''||value==null||value=="undefined"){
									return "/";
								}else{
									return value;
								}	
							}
						},
						{
							field: 'areaAddress',
							title: '详细地址',
							width: 100,
							align: "center",
							formatter: function(value,row,index){
								if(value==''||value==null||value=="undefined"){
									return "/";
								}else{
									return value;
								}	
							}
						},
						{
							field: 'businessType',
							title: '执行组织',
							width: 80,
							align: "center",
							formatter: function(value,row,index){
								if(row.businessType=='1'){
									return "社区运营";
								}else if(row.businessType=='2'){
									return "媒管";
								}else if(row.businessType=='3'){
									return "用户运营";
								}else if(row.businessType=='4'){
									return "电商运营";
								};		
							}
						},
						{
							field: 'projectName',
							title: '项目名称',
							width: 80,
							align: "center"
						},
						{
							field: 'fieldName',
							title: '场次名称',
							width: 80,
							align: "center",
							formatter: function(value,row,index){
								if(value==''||value==null||value=="undefined"){
									return "/";
								}else{
									return value;
								}	
							}
						},
						{
							field: 'actionStatus',
							title: '执行状态',
							width: 80,
							align: "center",
							formatter: function(value,row,index){
								if(row.actionStatus=='1'){
									return "待确认";
								}else if(row.actionStatus=='2'){
									return "策划中";
								}else if(row.actionStatus=='3'){
									return "待执行";
								}else if(row.actionStatus=='4'){
									return "执行中";
								}else if(row.actionStatus=='5'){
									return "已完成";
								}
							}
						},
						{
							field: 'exceptionStatus',
							title: '是否异常',
							width: 80,
							align: "center",
							formatter: function(value,row,index){
								if(row.exceptionStatus=='1'){
									return "正常";
								}else {
									return "异常";
								}	
							}
						},
						{
							field: 'actionCode',
							title: '操作',
							width: 80,
							align: "center",
							formatter: function(value,row,index){
								var _htmlbt='<a href="javascript:;" data-id="1" style="color: #51d2b6"  onclick="actionback(\''+row.actionCode+'\')" >清单详情</a>';
								return _htmlbt;
							}
						}
					]
				]
			});
		}
		
		//填充收款表格
		function getbackMoneyPlan(sss,$eleS){
			var dataList = [];
			for(var i = 0; i < sss.length; i++) {
				dataList.push({
					backMoneyPlanCode: sss[i].backMoneyPlanCode,
					projectCode: sss[i].projectCode,
					contractCode: sss[i].contractCode,
					contractName: sss[i].contractName,
					countryName: sss[i].countryName,
					money: sss[i].money,
					customerName: sss[i].name,
					createrName: sss[i].createrName,
					approveTime: sss[i].approveTime,
					collectedMoney: sss[i].collectedMoney,
					unCollectedMoney: sss[i].unCollectedMoney,
					reduceMoney: sss[i].reduceMoney,
					planStatus: sss[i].planStatus
				})
			}
			console.log(dataList)
			$eleS.datagrid({
				data: dataList,
				fitColumns: false,
				nowrap: false,
				scrollbarSize: 0,
				onLoadSuccess: function(data) {
					$(".datagrid-cell-check").addClass('dataCheck');
					$(".datagrid-header-check").addClass('dataCheck');
					setTimeout(function(){
						$eleS.datagrid('resize');
					},0)
				},
				columns: [
					[ //相应调整显示栏
						{
							field: 'backMoneyPlanCode',
							title: '收款计划ID',
							width: 170,
							align: "center"
						},
						{
							field: 'projectCode',
							title: '项目ID',
							width: 170,
							align: "center"
						},
						{
							field: 'contractCode',
							title: '合同ID',
							width: 170,
							align: "center"
						},
						{
							field: 'contractName',
							title: '合同名称',
							width: 170,
							align: "center"
						},
						{
							field: 'customerName',
							title: '公司名称',
							width: 200,
							align: "center"
						},
						{
							field: 'createrName',
							title: '业务员',
							width: 170,
							align: "center"
						},
						{
							field: 'approveTime',
							title: '合同审批时间',
							width: 200,
							align: "center"
						},
						{
							field: 'money',
							title: '合同总金额',
							width: 170,
							align: "center"
						},
						{
							field: 'collectedMoney',
							title: '合同实收金额',
							width: 170,
							align: "center"
						},
						{
							field: 'unCollectedMoney',
							title: '合同未收金额',
							width: 170,
							align: "center"
						},
						{
							field: 'reduceMoney',
							title: '扣款金额',
							width: 170,
							align: "center"
						},
						{
							field: 'planStatus',
							title: '合同收款状态',
							width: 170,
							align: "center"
						},
						{
							field: 'handler',
							title: '操作',
							width: 200,
							align: "center",
							formatter: function(value,row,index){
								var _htmlbt='<a href="javascript:;" data-id="1" style="color: #0066FF" onclick="planback(\''+row.contractCode+'\',\''+row.backMoneyPlanCode+'\')" >计划详情</a>';
								return _htmlbt;
							}
						}
					]
				]
			});
		}
		
		/* 业务主管提交审批提交 */
		function submit(){
			var productCode = "${projectVo.projectCode}" ;
			var remark = $("#remark").val();
			var examResult = $('input[name="examResult"]:checked').val();
			if (remark == null || remark == "undefined" || remark == '') {
				parent.$.messager.alert("消息提示","请填写审批意见！")
				return false;
			} ;
			if (examResult == null || examResult == "undefined" || examResult == '') {
				parent.$.messager.alert("消息提示","请选择审批是否通过！")
				return false;
			} ;
			 createConfirm("操作提示","确定是否审批项目？",true,false,"确定","取消",okCallbakFunciton,"","","");
			 function okCallbakFunciton(){
				//审批项目异步发送请求
					$.ajax({  
				        type : "POST",  //提交方式  
				        url : "/grms/project/exam.html",//路径  
				        dataType : "json",//数据，这里使用的是Json格式进行传输  
				        data:  {"productCode":"${projectVo.projectCode}","remark":remark,"examResult":examResult},  
				        success : function(result) {//返回数据根据结果进行相应的处理  
				            if ( result.success) {
				            	parent.$.messager.alert('消息提示','项目审批成功！！');
				            	location.reload();
				            }else{
				            	parent.$.messager.alert('消息提示',result.msg);
				            }
				        } 
				    }); 
			  }
		}
		
		/* 检验项目资源是否占用 */
		function testSubmit(){
			var examResult = $('input[name="examResult"]:checked').val();
			$.ajax({  
		        type : "POST",  //提交方式  
		        url : "/grms/project/exam.html",//路径  
		        dataType : "json",//数据，这里使用的是Json格式进行传输  
		        data:  {"productCode":"${projectVo.projectCode}","flag":1,"examResult":examResult},  
		        success : function(result) {//返回数据根据结果进行相应的处理  
		            if ( result.success) {
		            	
		            }else{
		            	parent.$.messager.alert('消息提醒', result.msg);
		            }
		        } 
		    }); 
		}
		
		/* 立项终止 */
		function projectStop(){
			 $.ajax({  
		        type : "POST",  //提交方式  
		        url : "/grms/project/stopProject.html",//路径  
		        async : false, //同步请求
		        dataType : "json",//数据，这里使用的是Json格式进行传输  
		        data:  {"productCode":"${projectVo.projectCode}","flag":1},  
		        success : function(result) {//返回数据根据结果进行相应的处理  
		            if ( result.success) {
		            	$("#exportStatisticsBtn").show();
		    			$('#exportStatisticsBtn').dialog({    
		    			    title: '消息提醒',    
		    			    width: 400,    
		    			    height: 350,    
		    			    closed: false,       
		    			    modal: true,
		    			    buttons:[{
		    		            text:'确认',
		    		            handler:function(){
		    		            	$.ajax({  
		    		    		        type : "POST",  //提交方式  
		    		    		        url : "/grms/project/stopProject.html",//路径  
		    		    		        dataType : "json",//数据，这里使用的是Json格式进行传输  
		    		    		        data:  {"productCode":"${projectVo.projectCode}","remark":$("#textareaValue").val()},  
		    		    		        success : function(result) {//返回数据根据结果进行相应的处理  
		    		    		            if ( result.success) {
		    		    		            	parent.$.messager.alert('消息提醒','立项终止成功。');
		    		    		            	location.reload();
		    		    		            }else{
		    		    		            	parent.$.messager.alert('消息提醒','合同已完成，不可进行终止操作。');
		    		    		            	return;
		    		    		            }
		    		    		        } 
		    		    		    }); 
		    		            }
		    		        },{
		    		            text:'取消',
		    		            handler:function(){
		    		            	$('#exportStatisticsBtn').dialog("close");
		    		            	return;
		    		            }
		    		        }]
		    			}); 
		            }else{
		            	parent.$.messager.alert('消息提醒','合同已完成，不可进行终止操作。');
		            	return;
		            }
		        } 
		    });  
		}
		
		
		/* 撤回项目 */
		function revoke(){
			 createConfirm("操作提示","确定是否撤回项目？",true,false,"确定","取消",okCallbakFunciton2,"","","");
			 function okCallbakFunciton2(){
				//审批项目异步发送请求
					$.ajax({  
				        type : "POST",  //提交方式  
				        url : "/grms/project/revoke.html",//路径  
				        dataType : "json",//数据，这里使用的是Json格式进行传输  
				        data:  {"productCode":"${projectVo.projectCode}"},  
				        success : function(result) {//返回数据根据结果进行相应的处理  
				            if ( result.success) {
				            	parent.$.messager.alert('消息提示','项目撤回成功！！');
				            	location.reload();
				            }else{
				            	parent.$.messager.alert('消息提示','项目撤回失败！！');
				            }
				        } 
				    }); 
			  }

		}
		
		
		//跳转合同页面
		function gocontract(){
			var url = "<%=basePath%>/contract/contractEdit.html?projectName="+"${projectVo.name}"+"&projectCode="+"${projectVo.projectCode}";
			url=encodeURI(url);
			location.href=url;
		}
		
		
		/* 页面启动时加载 */
		$(function(){
			var pps = ${projectProductListStr} ;
			if(pps!=null&&pps!=""){
		        getResourceView(pps,$('#projectProductList'));
			}else{
				$("#projectProductListDiv").hide();
			};
			var pas = ${projectActionListStr} ;
			if(pas!=null&&pas!=""){
				getProjectAction(pas,$('#projectActionList'));
			}else{
				$("#projectActionListDiv").hide();
			};
			var bps = ${backMoneyPlanlistStr};
			if(bps!=null&&bps!=''){
				getbackMoneyPlan(bps,$('#backMoneyPlanlist'));
			}else{
		        $("#backMoneyPlanlistDiv").hide();
			} 
	     });       
	</script>    
	</head>
	<body >
		<div class="topTitle clearfix">
					<div class="path1 fl">项目管理</div>
					<div class="path2 fl">查看项目</div>
			</div>
		<div class="wrap">
			<!-- 隐藏div 方便弹窗 -->
			<div id="exportStatisticsBtn" style="display: none">
			    <textarea id="textareaValue" style="height:150px;display:block;width: 340px;margin: 32px auto;box-sizing:border-box;padding: 5px;">请输入终止立项意见</textarea>
			</div>
			
			
			
			<div class="blackbackground">
				<div style="background-color:#eee;width:100%;height:44px;float: left;margin:0px 0px 20px 0px" >
					<div style="color: #333;border-style:solid; border-width:10px; border-color:#eee" >
						<span >查看项目信息</span>
					</div>
				</div>
			</div>
			<div class="body-box">
				<div class="table-wrap">
					<div>
						<span>项目名称：</span>
						<span>${ projectVo.name}</span>	
					</div>	
					<div>
						<span>项目ID：</span>
						<span>${ projectVo.projectCode}</span>	
					</div>
					<div>
						<span>项目进度：</span>
						<span>
						<c:if test="${projectVo.status == 0}">待审核</c:if>
						<c:if test="${projectVo.status == 1}">审核通过</c:if>
						<c:if test="${projectVo.status == 2}">审核不通过</c:if>
						<c:if test="${projectVo.status == 3}">立项终止</c:if>
						<c:if test="${projectVo.status == 4}">撤回</c:if>
						</span>	
					</div>
					<div>
						<span>客户信息：</span>
						<span>${projectVo.customerName}</span>	
					</div>
					<div class="table-width">
						<span>客户ID：</span>
						<span>${projectVo.customerId}</span>	
					</div>
					<c:if test="${projectVo.contractStatus == 7}">
						<div>
							<span>合同名称：</span>
							<span>${projectVo.contractName}</span>	
						</div>
						<div class="table-width">
							<span>合同ID：</span>
							<span>${projectVo.contractCode}</span>	
						</div >
					</c:if>
					<c:forEach items="${projectVo.roles}" var="r"> 
						<div>
							<span>项目角色：</span>
							<span>${r.projectRoleName}</span>	
						</div>	
						<div>
							<span>联系人名称：</span>
							<span>${r.name}</span>	
						</div>
						<div>
							<span>联系电话：</span>
							<span>${r.phone}</span>	
						</div>
					</c:forEach>
				</div>	
				<div class="table-wrap-three">
					<span>项目说明：</span>
					<div  style="margin: 2px"><pre style="line-height: 17px;white-space:normal; font-family:'Microsoft yahei'">${projectVo.description}</pre></div>	
				</div>
			</div>
			
			
			<!-- 资料清单 -->
			<div id="projectProductListDiv">
				<div class="blackbackground">
					<div style="background-color:#eee;width:100%;height:44px;float: left;margin:0px 0px 20px 0px;" >
						<div style="color: #333;border-style:solid; border-width:10px; border-color:#eee" >
							<span >查看资料清单</span>
						</div>
					</div>
				</div>
			
				<div style="background-color: #51d2b6;height: 40px;float: right;border-radius:5px;margin: 20px" >
					<div style="visibility: visible;margin:10px;color: #FFFFFF" onclick="download()">
						<a href="javascript:;"   style="color: #FFFFFF">资源清单下载</a> 
					</div>
				</div>
				
				<div class="projectProductList_Box" style="margin: 20px 20px 0 20px;width:100%;">
					<div id="projectProductList"  style='width:97%'></div>
				</div>
				
				<div class="projectFileListBox">	
					<c:if test="${fn:length(projectAttachmentList) > 0}">     
					<table id="projectFileList" border="1">
					    <tr>
					        <td>附件名称</td>
					        <td>操作</td>					        
					    </tr>
					    <c:forEach items="${projectAttachmentList}" var="r" varStatus="idx">
					        <tr >
					           <td>${r.fileName }</td>
					           <td>
                		        <a onclick="attachmentDownload('${r.id}')">下载</a>
                		       </td>
                		   </tr>
					    </c:forEach>
					</table>
				    </c:if>	
				  </div>
			</div>
			
			
			<!-- 执行清单 -->
			<div id="projectActionListDiv" style="background: #fff;">
				<div class="blackbackground">
					<div style="background-color:#eee;width:100%;height:44px;float: left;margin:0px 0px 20px 0px" >
						<div style="color: #333;border-style:solid; border-width:10px; border-color:#eee" >
							<span >查看执行清单</span>
						</div>
					</div>
				</div>
				
				<div class="projectActionList_Box" style="margin: 0 20px 20px;padding-bottom: 20px;width:97%;">
					<div id="projectActionList"  style='width:100%'></div>
				</div>
			</div>
			
			<div id="backMoneyPlanlistDiv">
				<div class="blackbackground">
					<div style="background-color:#eee;height:44px;width: 100%;" >
						<div style="color: #333;border-style:solid;border-width:10px; border-color:#eee" >
							<span >查看收款计划</span>
						</div>
					</div>
				</div>
				<div class="backMoneyPlanlist_Box" style="padding: 20px 20px;background: #fff;">
					<div id="backMoneyPlanlist"  style='width:100%;'></div>
				</div>
				<div id="attachmentList_Box" style="margin:20px 0;">
					<c:if test="${fn:length(closeAttachmentList) > 0}">
						<span  style='padding: 5px 0;'>可选进行结案资料下载：</span>
						<c:forEach items="${closeAttachmentList}" var="r"> 
								<span style='padding: 5px 40px;display:inline-block;'>
									<a href="javascript:;" style="color: #51d2b6;" onclick="attachmentDownload('${r.id}')">${r.fileName} </a>
								</span>
						</c:forEach>
					</c:if>
				</div>
			</div>
			
			<!-- 审核记录 -->
			<c:if test="${!empty projectApproveLogList}">
				<div class="blackbackground" style="overflow:hidden;clear: both;background: #fff;">
					<div style="background-color:#eee;width:100%;height:44px;float: left;margin:0 0px 20px 0px " >
						<div style="color: #333;border-style:solid; border-width:10px; border-color:#eee" >
							<span >查看审批记录</span>
						</div>
					</div>
				</div>
				
				<div class="wangbacheng" style="background:#fff;padding: 0 20px;">
					<c:forEach items="${projectApproveLogList}" var="r"> 
						<div class="line-borders" style="line-height: 50px; overflow:hidden;border-top: 1px solid #eee;">
							<span  style=''>状态 ：</span>
							<span  style=''>
								<c:if test="${r.status == -1}">删除</c:if>
								<c:if test="${r.status == 0}">待审核 </c:if>
								<c:if test="${r.status == 1}">审核通过 </c:if>
								<c:if test="${r.status == 2}">审核不通过</c:if>
								<c:if test="${r.status == 3}">立项终止</c:if>
								<c:if test="${r.status == 4}">撤回</c:if>
							</span>
							<div  style=''>
								<span>操作人 ：</span>
								<span>${r.createrName}</span>
								<span style="margin-left:50px;">操作人ID：</span>
								<span>${r.createrAccount}</span>
							</div>
							<div  style=''>
								<span>时间：</span>
								<span ><fmt:formatDate value='${r.approveTime}' pattern='yyyy年MM月dd日   HH:mm:ss' /></span>
							 </div>
							<c:if test="${!empty r.remark}">
								<div  style=''>
									<span>意见 ：</span>
									<span>${r.remark}</span>
								</div>
							</c:if>
						</div>	
					</c:forEach>
				</div>
			</c:if>
			
			<!-- 业务主管审批操作 -->
			<c:if test="${(roleType == 1 || roleType == -1)&& projectVo.status == 0}">
				<div class="blackbackground">
					<div style="background-color:#4d525a;width:100%;height:44px;float: left;margin:20px 0px 0px 0px " >
						<div style="color: #FFFFFF;border-style:solid; border-width:10px; border-color:#4d525a" >
							<span >审批操作</span>
						</div>
					</div>
				</div>
				<div style="background: #fff;padding: 0 20px;">
					<label class="radio-icon" style="padding: 15px 0;display:inline-block;">
						<input type="radio" name='examResult'  value="1" onclick="testSubmit()">
						<span style="line-height: 20px;display:inline-block;">审批通过</span></input>
					</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<label class="radio-icon" style="padding: 15px 0;display:inline-block;">
						<input type="radio" name='examResult'  value="2"/>
						<span style="line-height: 20px;display:inline-block;">审批不通过</span>
					</label>
				</div>
				<div  style="background: #fff;padding: 0 20px;">
					<span style="line-height: 20px;vertical-align: top;">审批意见：</span>
					<textarea style="padding: 10px; width: 300px;height: 150px;border: 1px solid #ccc;margin-bottom: 20px;" rows="" cols="" placeholder="必填，300字以内" maxlength="300" id="remark" style="width:400px;height:200px;"></textarea>
				</div>
			</c:if>
		
			<!-- 下方按钮 -->
			<div  style="text-align: center;">
				<div style="background-color: #51d2b6;display:inline-block;height: 40px;border-radius:5px;margin: 20px 0;" >
					<div style="visibility: visible;margin:10px 50px 10px 50px;color: #FFFFFF" >
					<a href="<%=basePath%>/project/goback.html"  style="color: #FFFFFF">返回</a>
					</div>
				</div>
				
				<!-- 业务主管立项终止 -->
				<c:if test="${(roleType == 1 || roleType == -1) && projectVo.status == 1 }">
					<div style="background-color: #51d2b6;display:inline-block;height: 40px;border-radius:5px;margin:  20px 100px" onclick="projectStop()">
						<div style="visibility: visible;margin:10px 50px 10px 50px">
							<a href="javascript:;"  style="color: #FFFFFF">立项终止</a>
						</div>
					</div>
				</c:if>
				
				<!-- 业务主管审批 -->
				<c:if test="${(roleType == 1 || roleType == -1) && projectVo.status == 0 }">
					<div style="background-color: #51d2b6;display:inline-block;height: 40px;border-radius:5px;margin:  20px 100px" onclick="submit()">
						<div style="visibility: visible;margin:10px 50px 10px 50px">
							<a href="javascript:;"  style="color: #FFFFFF">确认提交</a>
						</div>
					</div>
				</c:if>
				
				<!-- 业务员撤回 -->
				<c:if test="${(roleType == 0  || roleType == -1)&& projectVo.status == 0 }">
					<div style="background-color: #51d2b6;display:inline-block;height: 40px;border-radius:5px;margin:  20px 100px" onclick="revoke()">
						<div style="visibility: visible;margin:10px 50px 10px 50px">
							<a href="javascript:;"  style="color: #FFFFFF">撤回</a>
						</div>
					</div>
				</c:if>
				
				<!-- 业务员编辑 -->
				<c:if test="${(roleType == 0  || roleType == -1)&& (projectVo.status == 4 || projectVo.status == 2)}">
					<div style="background-color: #51d2b6;display:inline-block;height: 40px;border-radius:5px;margin:  20px 100px" >
						<div style="visibility: visible;margin:10px 50px 10px 50px">
							<a href="<%=basePath%>/project/addProject.html?type=1&productCode=${projectVo.projectCode}"  style="color: #FFFFFF">编辑</a>
						</div>
					</div>
				</c:if>
				<!-- 业务员生成合同-->
				<c:if test="${(roleType == 0 || roleType == -1) && projectVo.status == 1 && empty projectVo.contractCode}">
					<div style="background-color: #51d2b6;display:inline-block;height: 40px;border-radius:5px;margin:  20px 100px" onclick="gocontract()">
						<div style="visibility: visible;margin:10px 50px 10px 50px">
							<a href='javascript:;' style="color: #FFFFFF">生成合同</a>
						</div>
					</div>
				</c:if>
			</div>		
		</div>
	</body>
</html>