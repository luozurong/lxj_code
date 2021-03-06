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
		  .conditions-select{
		     margin:0;
		     background-color: #f8f8f8;
		  }
		  .textbox-text{
		  }
		  .pagination-page-list{
				display:none;
			}
		</style>
	</head>

	<body>

		<input id="is_selectAll"  type="hidden" value="0"/><!-- 是否点击过搜索按钮，0否1是 -->
		<input id="account" value="${sessionScope.userAccount }" type="hidden"/>
		<input id="departId" value="${sessionScope.userDetailVo.departId }" type="hidden"/><!-- 当前用户的部门 -->
		<input type="hidden" id="jumpStr" value="${param.jumpStr }" />
		<input id="pageFlag" value="ymd" type="hidden"/><!-- 当前页面的标志，确保唯一性，用于与异常记录页面的项目跳转 -->
		<div class="wrap">
			<div class="topTitle clearfix">
				<div class="path1 fl">执行管理</div>
				<div class="path2 fl">执行清单列表</div><!-- 用户运营/媒管/电商共用页面 -->
			</div>
			
			<div class="conditions-select clearfix">
			          <div class="accountBox" style="width:1675px;">
					    <div class="accountBox2">
						        
						        <div class="account fl">
								  <span class="accounttext1">项目名称:</span><input id="projectName"  />
								</div>
								<div class="account fl">
								  <span class="accounttext1"  style="width:90px;">执行清单ID：</span><input id="projectActionCode" style="width:145px;" />
								</div>
								<div class="account fl">
								  <span class="accounttext1">投放日期：</span>
								  <input style="width:115px;height:34px;" id="startTime" data-options="sharedCalendar:'#cc'"  type= "text" class= "easyui-datebox"><div id="cc" class="easyui-calendar"></div> --至-- 
	        			          <input style="width:115px;height:34px;" id="endTime" data-options="sharedCalendar:'#cc'"  type= "text" class= "easyui-datebox">
								</div>
						       <div class="account fl status">
								  <span class="accounttext1">执行状态：</span>
									<select id="actionStatus" style="height:34px;width:70px;" class="easyui-combobox" name="actionStatus"  panelHeight="auto">   
									    <option value="">全部</option>
							        	<option value="1">待确认</option>
							        	<option value="2">策划中</option>
							        	<option value="3">待执行</option>
							        	<option value="4">执行中</option>
							        	<option value="5">已完成</option>   
									</select>
								</div>
								<div class="search fl" style="margin-left:20px;margin-top:22px;display:none;" id="selectAll" onclick="selectAll()">搜索</div>
								<c:if test="${sessionScope.roleType == 7 || sessionScope.roleType == -1 }">
									<button id="allBack" onclick="exportMGData()"style="margin-left:20px;margin-top:22px;display:none;" class="redButton" >导出媒管数据</button>
								</c:if>
				        </div>
			        </div>
					
				</div>
			</div>
			<div class="content-wrap" style="background:#fff;margin-left:21px;">
				<table id="dg"></table>
				<div id="pp" style="position: relative;"></div>
			</div>
			<div id="dlg_re" style="display: none;"><!-- 上报异常对话框 -->
				<textarea id="exRemark" maxlength="500" style="width: 498px; height: 250px;" placeholder="请输入执行异常原因描述"
					oninput="return LessThan(this);" onchange="return LessThan(this);"></textarea>
				<span id="txtNum" text-align="right">0/500</span>
			</div>
		</div>
		<script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
		<script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
		<script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
		<script type="text/javascript" src="<%=basePath%>/projectAction/js/ymdProjectAction.js"></script>
		<script src="<%=basePath%>/homepage/js/index.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>