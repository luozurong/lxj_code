<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>其他设置</title>
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/default.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/icon.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/systemSettings/css/system_common.css" />
	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/systemSettings/css/terminalAlarmDetail.css"/>
	</head>
	<body style="min-width: 1130px;">
		<div class="workorder-breadcrumb">
		    <span class="workorder-breadcrumb-icon"></span>
		    <span>系统设置</span>
		    <span class='ic_right'></span>
		    <span>其他设置</span>
		</div>
		<div class="setBody">
			<table border="" cellspacing="" cellpadding="" class="editTable">
				<tr>
					<td>客户类型设置</td>
					<td>
						<div class="addbtn" onclick="add($(this),'customerType','','')" id="customerTypeBtn">+添加客户类型</div>
					</td>
				</tr>
				<tr> 
					<td>项目角色设置</td>
					<td><div class="addbtn" onclick="add($(this),'projectRole','','')" id="projectRoleBtn">+添加项目角色</div></td>
				</tr>
			</table>
			<div>
				<span class="save_btn" id="btn_voice" onclick="saveSet()">保存</span>
			</div>
		</div>
		
	<div id="delStatistics" style="display: none">
  		 <div>确定要删除吗？</div>
	</div>
	<!--自动弹窗-->
	<div class="autotips">
		<ins class="success_icon"></ins>
		<ins class="warning_icon"></ins>
		<span class="mes"></span>
	</div>
	
    <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
    <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
    <script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
    <script src="<%=basePath%>/systemSettings/js/otherSets.js" type="text/javascript" charset="utf-8"></script>
    <%-- <script type="text/javascript"	src="<%=basePath%>/homepage/js/index.js"></script> --%>
	</body>
</html>
