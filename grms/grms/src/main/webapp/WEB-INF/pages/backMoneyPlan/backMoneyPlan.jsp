<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>收款计划</title>
    <meta http-equiv="X-UA-Compatible" content="edge" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/default.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/icon.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/backMoneyPlan/css/endWorkOrder.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/backMoneyPlan/plugin/timezhou/css/style.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/backMoneyPlan/css/backMoneyPlan.css" />
    <style type="text/css">
    	.datagrid-cell-c2-stoppageAppearance,.datagrid-cell-c2-handleWay,.datagrid-cell-c2-deviceID{
    		white-space: nowrap !important;
    		text-overflow: ellipsis;
    	}
    </style>
</head>
<body style='min-width:1300px'>
<div class="workorder-breadcrumb">
    <span class="workorder-breadcrumb-icon"></span>
    <span>收款管理</span>
    <span class='ic_right'></span>
    <span>收款计划</span>
</div>

<div class="workorder-search">
	<div class="workorder-input">
        <select id="selectCondition" class="easyui-combobox" name="dept">
            <option value="">请选择</option>
            <option value="0">收款计划ID</option>
            <option value="1">关联项目ID</option>
            <option value="2">关联合同ID</option>
            <option value="3">合同名称</option>
            <option value="4">业务员</option>
        </select>
        <input class="easyui-textbox" style="width: 280px;" id="condition">
        
        <span class="workorder-input-word">合同收款状态:</span>
        <select id="backMoneyStatus" class="easyui-combobox" name="dept">
            <option value="">请选择</option>
            <option value="0">待收款</option>
            <option value="1">待审核</option>
            <option value="2">登记中</option>
            <option value="3">已收款</option>
            <option value="4">已结案</option>
        </select>
        
        <span class="workorder-time">收款创建日期：</span>
        <input  id="startTime"  type= "text" class= "easyui-datebox">
        <span>&nbsp;&nbsp;--&nbsp;&nbsp;</span>
        <input  id="endTime"  type= "text" class= "easyui-datebox">
    </div>
</div>
<div class="workorder-btn">
    <button onclick="search()" id="search">查询</button>
    <button onclick="exportBackMoneyPlan()" id="export">导出</button>
    <button onclick="createNew()" id="create">新建收款计划</button>
    <button onclick="allBack()" id="allBack">全部到账</button>
</div>
<div class="content-wrap">
    <div id="dg"></div>
    <div id="pp" style="position: relative; width: 100%;"></div>
</div>
    
<!--合同列表-->
<div id="allotWorkOrder"  style="display: none">
    <div class="allotWorkOrder-input" style="padding: 20px 0;width: 760px;overflow: hidden;">
        <input type="text" id="allot_maintainerName" style="width:180px;height: 32px;margin-left:40px;border: 1px solid #ccc;">
        <button onclick="searchMaintainer()" style="float: right;border: 1px solid #51d2b6;color: #51d2b6;width: 120px;height: 32px;line-height: 32px;border-radius: 3px;">搜索</button>
    </div>
    <div class="allotWorkOrder-tabel" style="margin-left: 40px;">
        <div id="allotWorkOrderTable" style="width: 720px;"></div>
        <div id="pp-allotWorkOrderTable" style="margin-top: 20px;width: 720px;border: 1px solid #eee;padding:5px 0;"></div>
    </div>
</div>

<div id="allbackDiv"  style="display: none">
	<div><textarea style="margin: 20px; width:89%; height: 120px;border:1px solid #ccc; display:block;" maxlength="300" placeholder="限300字" id="allbackRemark"></textarea>  </div>
</div>

<!--导出（确认）-->
<div id="exportStatistics" style="display: none">
    <div>导出维修记录需要一定的时间，您确认导出吗？</div>
</div>
<!--导出（提示）-->
<div id="hintStatistics"  style="display: none">
    <div>请选择需导出的记录</div>
</div>
<script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
<script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
<script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
<script type="text/javascript"	src="<%=basePath%>/backMoneyPlan/js/backMoneyPlan.js"></script>
</body>
</html>