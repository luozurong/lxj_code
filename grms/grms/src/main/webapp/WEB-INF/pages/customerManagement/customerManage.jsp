<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>客户管理</title>
    	<meta http-equiv="X-UA-Compatible" content="edge" />
    	<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css"/>
    	<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/default.css"/>
    	<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css"/>
    	<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css"/>
    	<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/icon.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/customerManagement/css/customerManage.css"/>
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/customerManagement/css/custome_common.css"/>	   
	    <style type="text/css">
		 .messager-info{
		   margin-top:9px;
		 }
		</style>
	</head>
	<body style="min-width:1100px ;">
		
		<div class="wrap">
		    <div class="topTitle clearfix">
				<div class="path1 fl">客户管理</div>
				<div class="path2 fl">客户列表</div>
			</div>
			<div class="conditions-select">			
				<div class="accountBox">
				    <div class="accountBox2">
						<div class="account fl">
						  <input value="" style="padding-left: 10px;width:150px" id="conditionId" placeholder="名称/电话"/>
						</div>
						<div class="account fl" style="margin-left: 30px;">
						  <button onclick="search()" class="whileButton" id="search">搜索</button>
						</div>
						<div class="account fl">
						  <button onclick="saveCustomer()" class="whileButton" id="add">添加</button>
						</div>
						<div class="account fl">
						  <button onclick="delBatch()" class="redButton" id="deleteBatch">批量删除</button>
						</div>
						<div class="account fl">
						  <button onclick="exportToExcel()" class="blueButton" id="export">导出Excel</button>
						</div>												
					</div>
				</div>							
			</div>
			
			<!-- <div class="conditions-select clearfix">

				<div class="searchFooter">
					<span class="searchBtn" onclick="search()">搜索</span>
					<span class="searchBtn" onclick="addCust()">添加</span>
					<span class="searchBtn" onclick="delCust()">批量删除</span>
					<span class="exportBtn" onclick="exportToExcel()">导出Excel</span>
				</div> 
			</div>-->
			<!-- <div class="content-wrap" style="margin-top: 20px;">
			    <div id="dg1"></div>
			    
			    <table id="list_data"></table>
			    <div id="pp" style="position: relative; width: 100%;"></div>
			</div>
			<div id="exportComfirm" style="display: none">
	   			 <div>导出维修记录需要一定的时间，您确认导出吗？</div>
			</div> -->
			
			<div class="content-wrap">
				<div id="dg" style="width:100%"></div>
				<div id="pp" style="position: relative;margin-top:20px;border:1px solid #eee"></div>
			
			</div>
			
		</div>
		
		<div id="exportComfirm" style="display: none">
	   		<div>导出维修记录需要一定的时间，您确认导出吗？</div>
		</div>
		<script src="<%=basePath%>/common/easyui/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="<%=basePath%>/common/easyui/jquery.easyui.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript" charset="utf-8"></script>
		<script src="<%=basePath%>/common/plugin/common.js" type="text/javascript" charset="utf-8"></script>
		<script src="<%=basePath%>/customerManagement/js/customerManagement.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>
