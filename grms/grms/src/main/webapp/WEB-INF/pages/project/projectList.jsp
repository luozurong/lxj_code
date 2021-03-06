<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*" %>
<%@ page language="java" import="com.hori.vo.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html style="height: 100%;">
	<head>
	<meta charset="UTF-8">
	<title>项目列表</title>
	<meta http-equiv="X-UA-Compatible" content="edge" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/project/css/project.css" />
    <link rel="shortcut icon" href="<%=basePath%>/homepage/images/ywhlzt.ico" type="image/x-icon">


	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/homepage/css/index.css"/>
	</head>
	<body >
		<div class="wrap">
			<div class="topTitle clearfix">
				<div class="path1 fl">项目管理</div>
				<div class="path2 fl">项目列表</div>
			</div>
			<div class="conditions-select">
				<div class="projectStautsBox" >
					<div class="workorder-tabs">
						 <div class="workorder-tabs1" name="projectStauts" data-status="-2">
				            <span>全部</span>
				        </div>
				        <div class="workorder-tabs2" name="projectStauts" data-status="0">
				            <span>待审核</span>
				        </div>
				        <div  class="workorder-tabs3" name="projectStauts" data-status="1">
				            <span>审核通过</span>
				        </div>
				        <div  class="workorder-tabs4" name="projectStauts" data-status="2">
				            <span>审核不通过</span>
				        </div>
				        <c:if test="${roleType==0||userType==0}">
					        <div  class="workorder-tabs6" name="projectStauts" data-status="4">
					            <span>已撤回</span>
					        </div>
				        </c:if>
				        <div  class="workorder-tabs5" name="projectStauts" data-status="3">
				            <span>立项终止</span>
				        </div>
				        <input type="hidden" id="code">
				        <input type="hidden" id="pendingStatus" value="${pendingStatus }">
				        <input type="hidden" id="projectStauts" value="${querryBean.status}">
				        <input type="hidden" id="stauts" value="${projectStatus}">
				        <input type="hidden" id="projectCode" value="${projectCode}">
				        <input type="hidden" id="pageNumber" value="${querryBean.pageNumber}">
				        <input type="hidden" id="pageSize" value="${querryBean.pageSize}">
				        <input type="hidden" id="roleType" value="${roleType}">
				        <input type="hidden" id="userType" value="${userType}">
					</div>
				</div>
				<div class="accountBox">
				    <div class="accountBox2">
						<div class="account fl">
						  <span>项目名称:</span><input id="name"  value="${querryBean.name}"/>
						</div>
						<div class="account fl">
						  <span>项目编号:</span><input id="productCode" value="${querryBean.productCode}">
						</div>
						<div id="contractCodeDiv" class="account fl">
						  <span>合同编号:</span><input id="contractCode" value="${querryBean.contractCode}">
						</div>
						<div class="account fl">
							<span>客户名称:</span><input id="customerName"  value="${querryBean.customerName}"/>
						</div>
						<c:if test="${roleType!=0}">
							<div class="account fl">
							  <span>创建人:</span><input id="createrName" value="${querryBean.createrName}">
							</div>
						</c:if>
					</div>
				</div>
				<div class="searchBox" style="height: 50px;" >
					<c:if test="${roleType==0||userType==0}">
						<a id="createBtn" class="create" href="JavaScript:creatProject();">新建</a>
					</c:if>
					<a id="searchBtn" onClick="searchData()" class="search" href="#">查询</a>
				</div>
				
			</div>
			<div class="content-wrap">
				<div id="dg" style="width:100%"></div>
				<div id="pp" style="position: relative;margin-top:20px;border:1px solid #eee"></div>
			</div>
		</div>
		<div id="win" style="display:none">
			<div style="padding:5px;text-align:center;">
				<textarea id="remark" placeholder="请输入立项终止的意见" rows="" cols="" style="width:350px;height: 200px;"></textarea>
			</div>
			<!-- <div style="padding:5px;text-align:center;">
				<a onClick="cancelStopProject()" href="#" class="easyui-linkbutton stopProject">取消</a>
				<a onClick="ensureStopProject()" href="#" class="easyui-linkbutton stopProject">确定</a>
			</div> -->
		</div>
		   
        <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
        <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
        <script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
        <script type="text/javascript"	src="<%=basePath%>/project/js/project.js"></script>
        <script src="<%=basePath%>/homepage/js/index.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>
