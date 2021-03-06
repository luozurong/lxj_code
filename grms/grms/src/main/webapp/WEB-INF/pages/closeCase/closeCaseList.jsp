<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
	String userTypeForClose = null;	
	if(session != null && session.getAttribute("userTypeForClose") != null){
		userTypeForClose = session.getAttribute("userTypeForClose").toString();
	}	

%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

	<head lang="en">
		<meta charset="UTF-8">
		<title>结案列表</title>
		<meta http-equiv="X-UA-Compatible" content="edge" />
		<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/default.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/icon.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>/closeCase/css/people-common.css" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>/closeCase/css/closeCase.css" />
	</head>

	<body>
		<div class="wrap">
			<div class="topTitle clearfix">
				<div class="path1 fl">结案管理</div>
				<div class="path2 fl">结案列表</div>
			</div>
			<div class="conditions-select clearfix">
	            <div class="clearfix clearfixed">
					<div class="account fl">关键字:</div>
					<div class="account1 fl" style="margin: 20px 10px 0 5px; ">
						<input value="" id="keyword" placeholder="请输入结案编号/项目名称/合同名称/收款计划编号" style="width: 360px;padding-left:5px"/>
					</div>
	            </div>
				<div class="clearfix clearfixed">
					<div class="account fl">创建日期:</div>
		            <div class="fl" style="margin: 20px 10px 0 5px; ">
				        <input class="easyui-datebox" id="createTimeStart" data-options="sharedCalendar:'#cc'" />
				        <div id="cc" class="easyui-calendar"></div>
		            </div>
		            <div class=" fl" style="margin: 20px 60px 0 5px; ">
				        <input class="easyui-datebox" id="createTimeEnd" data-options="sharedCalendar:'#cc'" />
				        <div id="cc" class="easyui-calendar"></div>
		            </div>
					<div class="time fl" >执行日期:</div>
		            <div class="fl" style="margin: 20px 10px 0 5px; ">
				        <input class="easyui-datebox" id="actionTimeStart" data-options="sharedCalendar:'#cc'" />
				        <div id="cc" class="easyui-calendar"></div>
		            </div>
		            <div class="fl" style="margin: 20px 10px 0 5px; ">
				        <input class="easyui-datebox" id="actionTimeEnd" data-options="sharedCalendar:'#cc'" />
				        <div id="cc" class="easyui-calendar"></div>
		            </div>
				</div>
				<div class="clearfix clearfixed">
		            <div class="account fl">结案类型:</div>	
					<div class="account1 fl" style="margin: 20px 10px 0 5px; ">
						<select class="easyui-combobox" id="type" panelHeight="auto">
				        	<option value="">全部</option>
				        	<option value="1">正常结案</option>
				        	<option value="0">异常结案</option>
				        </select>
					</div>
					<div class="account fl">结案状态:</div>	
					<div class="account1 fl" style="margin: 20px 10px 0 5px; ">
						<select class="easyui-combobox" id="status" panelHeight="auto">
				        	<option value="">全部</option>
				        	<option value="1">待提交-社区运营</option>
				        	<option value="2">待提交-用户运营</option>
				        	<option value="3">待提交-电商运营</option>
				        	<option value="4">待提交-媒管</option>
				        	<option value="5">已提交</option>
				        	<option value="6">待处理</option>
				        	<option value="7">待结案</option>
				        	<option value="8">已结案</option>
				        </select>
					</div>
					<div>
						<input type="hidden" id="userType" value="<%=userTypeForClose%>"/>
					</div>
					<div style="margin-left: 60px;" class="search fl" onclick="selectAll()" id="search">搜索</div>
				</div>
				<div class="clearfix clearfixed" id="batch">
	                <div class="upload fl upload-all" id="batchUpload">
	      				<span> 批量上传</span> 
	      				<input type="file" class="inputfixed" id="file" onchange="uploadAttach()"/>       
	                </div>
					<div class="create fl" onclick="batchUpdate(1)" id="batchCommit">批量提交</div>
					<div class="delete fl" onclick="batchUpdate(0)" id="batchCancel">批量撤回</div>
				</div>
			</div>
			<div class="content-wrap">
				<table id="dg"></table>
				<div id="pp" style="position: relative;"></div>
			</div>
			<div id="roleWin_change"  style="display: none;width: 400px;">
				<div id="roleWin-wrap" class="roleWin-wrap">
					<div id="content-box" class="content-box" id="content_box_1">
						<div>删除结案附件</div>
						<div id="attach"></div>	
					</div>
				</div>
			</div>
			
		</div>
		<script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
		<script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
		<script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
		<script type="text/javascript" src="<%=basePath%>/closeCase/js/closeCase.js"></script>
	</body>
</html>