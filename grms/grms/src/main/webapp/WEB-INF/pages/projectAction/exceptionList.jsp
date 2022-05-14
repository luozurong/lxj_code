<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

	<head lang="en">
		<meta charset="UTF-8">
		<title>执行管理</title>
		<meta http-equiv="X-UA-Compatible" content="edge" />
		<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/default.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/icon.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>/projectAction/css/people-common.css" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>/projectAction/css/projectAction.css" />
		<%-- <link rel="stylesheet" type="text/css" href="<%=basePath%>/homepage/css/index.css"/> --%>
		<style type="text/css">
			.pagination-page-list{
				display:none;
			}
		</style>
	</head>

	<body>
		<input id="account" value="${sessionScope.userAccount }" type="hidden"/>
		<div class="wrap">
			<div class="topTitle clearfix">
				<div class="search fl" id="goBack" onclick="goBack()" style="margin-left:25px;">返回</div>
			</div>
			<input type="hidden" id="jumpStr" value="${param.jumpStr }"/>
			<div class="conditions-select clearfix" style="margin-top:10px;width:1654px;">
				<div class="account fl" style="margin-left:-20px;">清单ID：</div>
				<div class="account1 fl">
					<input id="projectActionCode" style="width:150px;"/>
				</div>
				<div class="account fl" style="margin-left:50px;">处理状态：</div>
				<div style="margin-top: 20px;" class="fl">
					<select class="easyui-combobox" value="" id="status" style="width:75px;" panelHeight="auto">
			        	<option value="">全部</option>
			        	<option value="0">待处理</option>
			        	<option value="1">处理中</option>
			        	<option value="2">已处理</option>
			        </select>
				</div>
				<div class="account fl">上报日期：</div>
		        <div class="time-star fl">
			        <input class="easyui-datebox" id="createTime" data-options="sharedCalendar:'#cc'" style="width:178px;"/>
		            <div id="cc" class="easyui-calendar"></div>
		        </div>
				<div class="search fl" onclick="selectAll()" style="margin-left:80px;margin-top:22px;">查询</div>
			</div>
			<div class="content-wrap" style="background:#fff;">
				<table id="dg"></table>
				<div id="pp" style="position: relative;"></div>
			</div>
		</div>
		<script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
		<script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
		<script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
		<script type="text/javascript" src="<%=basePath%>/projectAction/js/exception.js"></script>
		<script src="<%=basePath%>/homepage/js/index.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>