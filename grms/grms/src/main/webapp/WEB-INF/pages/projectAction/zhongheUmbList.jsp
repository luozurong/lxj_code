<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>执行清单列表</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
<script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
<script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
<script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
<script type="text/javascript"	src="<%=basePath%>/js/zhongheDepart/zhUmbCommunityList.js"></script>
<script src="<%=basePath%>/homepage/js/index.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>
	<!-- 综合-用户媒管电商清单数据表格 -->
	<table id="zhCommunity_datagrid">
		<thead>
			<tr>
				<th data-options="field:'beginTime',width:100,align:'center'">投放开始日期</th>
				<th data-options="field:'endTime',width:100,align:'center'">投单结束日期</th>
				<th data-options="field:'businessType',width:100,align:'center',formatter:oganiOper">执行部门</th>
				<th data-options="field:'projectName',width:100,align:'center'">项目名称</th>
				<th data-options="field:'actionCode',width:100,align:'center'">执行清单ID</th>
				<th data-options="field:'areaNum',width:200,align:'center'">小区数量</th>
				<th data-options="field:'actionStatus',width:100,align:'center',formatter:actionOper">执行状态</th>
				<th data-options="field:'projectName',width:100,align:'center'">是否异常</th>
				<th data-options="field:'handle',width:100,align:'center',formatter:formatOper">操作</th>
			</tr>
		</thead>
	</table>
	<!-- 数据表格CRUD按钮 -->
	<div id="zhCommunity_datagrid_tb">
		<div>
			<span>项目名称：</span>
            <input value="" id="projectName"/>
			<span>执行清单ID：</span>
            <input value="" id="projectActionCode" style="width:100px;" panelHeight="auto"/>
			<span>投放开始日期：</span>
            <input class="easyui-datebox" id="startTime"/>
			<span>&nbsp;—&nbsp;投放结束日期：</span>
 			<input class="easyui-datebox" id="endTime" style="width:178px;"/>
			<span>执行状态：</span>
 			<select class="easyui-combobox" value="" id="actionStatus" style="width:70px;" panelHeight="auto">
			        	<option value="">全部</option>
			        	<option value="1">待确认</option>
			        	<option value="2">策划中</option>
			        	<option value="3">待执行</option>
			        	<option value="4">执行中</option>
			        	<option value="5">已完成</option>
			  </select>
			 <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="keyWordSearch()" >搜索</a> 
	   </div>
	 
	</div>
	
	
</body>
</html>