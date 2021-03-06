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
    <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
	<script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
	<script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
	<script type="text/javascript"	src="<%=basePath%>/js/zhongheDepart/projectActionDetail.js"></script>
	<script src="<%=basePath%>/homepage/js/index.js" type="text/javascript" charset="utf-8"></script>
    <link rel="shortcut icon" href="<%=basePath%>/homepage/images/ywhlzt.ico" type="image/x-icon">
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/homepage/css/index.css"/>
	    <style type="text/css">
	    	.wrap{
	    		margin: 20px;
	    	}
	    	.body-box{
	    		margin-left: 50px;
	    	}
	    	.file-ipt{
			 	display:inline-block;
			 	width: 60px;
			 	position: absolute;
			 	top:0;
			 	left: 0;
			 	z-index: 1;
			 	opacity: 0;
			 }
	    	.table-wrap{
	    		width: 100%;
	    		overflow: hidden;
	    	}
	    	.table-wrap>div{
	    		width: 30%;
	    		float: left;
	    		line-height: 50px;
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
	    	.buleButton{
				background-color:#51d2b6;
				color:#fff;
				line-height: 40px;
				border-radius:3px;
				text-align: center;
				padding:0 20px;
				display: inline-block;
			}
			#areas{
	    	  font-size:14px;
	    	  line-height:28px;
	    	  color:#666;
	    	  padding:10px;
	    	  padding-top:0;
	    	}
	    	#areas h2{
	    	  font-size:15px;
	    	  line-height:35px;
	    	  font-weight: initial;
	    	  color:#222;
	    	}
	    	#projectProductList .datagrid-row-checked{
	    	  background-color:#fff!important;
	    	}
	    	#projectProductList .datagrid-header td,#projectProductList .datagrid-body td,#projectProductList .datagrid-footer td {
			        border-width: 0 1px 1px 0;
			}
			#projectProductList .datagrid-btable tbody tr:nth-child(even) {
			    background: #fff;
			}
	    </style>	 
	</head>
	<body >
		<div class="topTitle clearfix">
					<div class="go-back fl" style="margin-left:24px;margin-right:0" onclick="window.history.go(-1)">返回</div>		
					<div class="path1 fl">项目管理</div>
					<div class="path2 fl">查看清单详情</div>
			</div>
			<input id="actionCode" type="hidden" value="${actionCode}"/>
			<input id="roleType" type="hidden" value="${roleType}"/>
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
			
			<div class="blackbackground">
				<div style="background-color:rgba(102, 102, 102, 1);width:100%;height:44px;float: left;margin:20px 0px 20px 0px" >
					<div style="color: #FFFFFF;border-style:solid; border-width:10px; border-color:rgba(102, 102, 102, 1)" >
						<span >查看资源清单</span>
					</div>
				</div>
			</div>
			<div style="background-color: rgba(22, 155, 213, 1);height: 40px;float: right;border-radius:5px;margin: 20px" >
				<div style="visibility: visible;margin:10px;color: #FFFFFF" onclick="downloadData()">
					<a href="javascript:;"   style="color: #FFFFFF">资源清单下载</a> 
				</div>
			</div>
			
			
			<div id="projectProductList"  style='width:97%;height:300px'>
				<table id="SQ_datagrid">
					<thead>
						<tr>
							<th data-options="field:'businessType',width:100,align:'center',formatter:oganiOper">执行部门</th>
							<th data-options="field:'fieldName',width:100,align:'center'">场次名称</th>
							<th data-options="field:'productType',width:100,align:'center'">类型</th>
							<th data-options="field:'productMenu',width:200,align:'center'">产品清单</th>
							<th data-options="field:'productSpec',width:100,align:'center'">产品规格</th>
							<th data-options="field:'buyNum',width:100,align:'center'">购买数量</th>
							<th data-options="field:'beginTime',width:100,align:'center'">执行开始日期</th>
							<th data-options="field:'endTime',width:100,align:'center'">执行结束日期</th>
							<th data-options="field:'areaName',width:150,align:'center'">已选小区</th>
						</tr>
					</thead>
				</table>
				
			</div>
			
			<div id="win" style="display:none">
				<div id="areas">
				</div>
			</div>
			
			<div class="blackbackground">
				<div style="background-color:rgba(102, 102, 102, 1);width:100%;height:44px;float: left;margin:20px 0px 20px 0px" >
					<div style="color: #FFFFFF;border-style:solid; border-width:10px; border-color:rgba(102, 102, 102, 1)" >
						<span >项目附件</span>
					</div>
				</div>
			</div>
			
				<div id="projectActionList"  style='width:97%;height:300px'>
					<table id="fj_datagrid"><!-- 附件表格 -->
						<thead>
							<tr>
								<th data-options="field:'fileName',width:200,align:'center'">附件名称</th>
								<th data-options="field:'handle',width:100,align:'center',formatter:formatHandle">操作</th>
							</tr>
						</thead>
					</table>
				</div>
			<c:if test="${roleType == '8'}">
				<div id="zhCommunity_datagrid_tb">
					 	<label> 					 		
						       <a class="buleButton" href="javascript:;"><input type="file" class="file-ipt" id="projectFile" name="projectFile"  onchange="uploadFile()">上传附件</a>					       
						 </label>
						<!--  <label>
						     <span class="buleButton" onclick="uploadFile()">上传附件</span>
						 </label> -->
				</div>
			</c:if>
			
			
			
			<div  style="text-align: center;">
				<div style="background-color: rgba(22, 155, 213, 1);display:inline-block;height: 40px;border-radius:5px;margin: 20px 0;" >
					<div style="visibility: visible;margin:10px 50px 10px 50px;color: #FFFFFF" onclick="window.history.go(-1)">
					<a href="javascript:;"  style="color: #FFFFFF">返回</a>
					</div>
				</div>
				
			</div>
			
			
		</div>
	</body>
	
</html>