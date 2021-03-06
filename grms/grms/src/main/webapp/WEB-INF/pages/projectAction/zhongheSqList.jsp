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
		<link rel="stylesheet" type="text/css" href="<%=basePath%>/homepage/css/index.css"/>
		<style type="text/css">
			.pagination-page-list{
				display:none;
			}
		</style>
	</head>

	<body>

        <input id="provinceCode"  type="hidden"/>
        <input id="cityCode"  type="hidden"/>
        <input id="countryCode"  type="hidden"/>
		<input type="hidden" id="jumpStr" value="${param.jumpStr }" />
		<div class="wrap">
			<div class="topTitle clearfix">
				<div class="path1 fl">执行管理</div>
				<div class="path2 fl">社区运营执行清单列表</div>
			</div>
			<div class="conditions-select clearfix"  style="width:1657px;">
				<div class="account fl" style="margin-left:-20px;">小区地址：</div>
				<div style="margin-top: 20px;" class="fl">
					<select class="easyui-combobox" value="" id="province" style="width:100px;"></select>
		            <select class="easyui-combobox" value="" id="city" style="width:100px;"></select>
		            <select class="easyui-combobox" value="" id="country" style="width:100px;"></select>
				</div>
				<div class="account fl">小区名称：</div>
				<div class="account1 fl">
					<input value="" id="areaName"/>
				</div>
				<div class="account fl">日期：</div>
		        <div class="time-star fl">
			        <input class="easyui-datebox" id="startTime" data-options="sharedCalendar:'#cc'" style="width:178px;"/>
		            <div id="cc" class="easyui-calendar"></div>
		        </div>
				<div class="fl status" style="margin-left:75px;margin-top: 27px;">执行状态：</div>
				<div class="account1 fl">
			        <select class="easyui-combobox" value="" id="actionStatus" style="width:70px;" panelHeight="auto">
			        	<option value="">全部</option>
			        	<option value="1">待确认</option>
			        	<option value="2">策划中</option>
			        	<option value="3">待执行</option>
			        	<option value="4">执行中</option>
			        	<option value="5">已完成</option>
			        </select>
			     </div>
				<div class="search fl" id="selectAll" style="margin-left:-20px;margin-top:22px;" onclick="selectAll()">搜索</div>
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
		<script type="text/javascript" src="<%=basePath%>/projectAction/js/zhongheSqList.js"></script>
		<script src="<%=basePath%>/homepage/js/index.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>