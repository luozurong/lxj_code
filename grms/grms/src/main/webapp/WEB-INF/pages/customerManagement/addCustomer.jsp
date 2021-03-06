<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>

<head lang="en">
<meta charset="UTF-8">
<title>添加客户</title>
<meta http-equiv="X-UA-Compatible" content="edge" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
     <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/default.css" />   
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/icon.css" />   
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
	<link rel="stylesheet" type="text/css" href="<%=basePath%>/customerManagement/css/addCustomerMessager.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/customerManagement/css/custome_common.css"/>
	
<!--<link rel="stylesheet" type="text/css" href="<%=basePath%>/project/css/people-common.css" />-->

<style type="text/css">
  .window{
     top:20% !important;
      position: fixed;
      margin-left:0;
  }
  .addCustomer-item input{
	     border:1px solid #ccc;
	     height:32px;
	     line-height: 32px;
	     padding-left:5px;
	     border-radius: 3px;
	  }
</style>


</head>
	<body>
		<input type=hidden id="salesmanName"  value="${salesman}"/>
		 <div class="wrap">
		    <div class="topTitle clearfix">
		       <div class="go-back fl" style="margin-left:24px;margin-right:0" onclick="cancel()">返回</div>
				<div class="path1 fl">客户管理</div>
				<!-- <div class="path2 fl">客户管理</div> -->
				<div class="path2 fl">添加客户</div>
			</div>
		</div>
		<div class="content-wrap">
		 <div class="addCustomerMessager">
				<div class="addCustomer-item">
					<span class="addCustomer-item-name">业务员：</span>
					<span id ="ownerName" name="ownerName"></span>
				</div>
				
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>公司名称：</span>
					<input  type="text" maxlength="20" class="easyui-validatebox" data-options="required:true" id="companyName" onBlur="similarName()">
					<span class="redText">不可为空</span>
				</div>
				
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>公司地址：</span>
				        <select id="province" class="easyui-combobox" name="dept" style="width:100px;height:34px;">
				            <option value="">请选择省/市</option>
				        </select>
				        <select id="city" class="easyui-combobox" name="dept" style="width:100px;height:34px;">
				            <option value="">请选择城市</option>
				        </select>
				        <select id="country" class="easyui-combobox" name="dept" style="width:100px;height:34px;">
				            <option value="">请选择区/县</option>
			            </select>
			            <input type="text" class="easyui-validatebox" data-options="required:true" id="address" placeholder="详细地址" maxlength="30">
			            <span class="redText">不可为空</span>
				</div>
				
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>公司类型：</span>
					<select id="companyType" name="companyType" required="required" style="width:110px;height:34px;">
						<option value="">==请选择==</option>
						<c:forEach items="${type }" var="t" varStatus="tp">
							<option value="${t.id }">${t.name}</option>
						</c:forEach>
					</select>
					<span class="redText">不可为空</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>主要经营：</span>
					<input type="text" class="easyui-validatebox" maxlength="20" data-options="required:true" id="industry">
					<span class="redText">不可为空</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>所属部门：</span>
					<input type="text" class="easyui-validatebox" maxlength="20" data-options="required:true" id="department">
					<span class="redText">不可为空</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name">负责人名称：</span>
					<input class="addCustomer-item-input" id="dutyName" maxlength="20">
					
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name">负责人电话：</span>
					<input class="addCustomer-item-input" maxlength="20" id="dutyPhone">
					
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>主要联系人名称：</span>
					<input class="easyui-validatebox" maxlength="20" data-options="required:true" type="text" msg="主要联系人名称" id="contactor">
					<span class="redText">不可为空</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>主要联系人电话：</span>
					<input  class="easyui-validatebox" maxlength="20" data-options="required:true" type="text" msg="主要联系人电话" id="contactorPhone">
					<span class="redText">不可为空</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name">备注说明：</span>
					<textarea id = "remark" rows="5" cols="40" maxlength="200" style="border: 1px solid #ccc;padding:5px;"></textarea>
				</div>
				<div class="bottomButtonBox">
					    <button class="buleButton" style="margin-right:40px"  onclick="confirmSave()">添加</button>				    
					    <button class="buleButton" onclick="cancel()">取消</button>
				</div>
				<!-- <span onclick="confirmSave()">添加</span>
				<span onclick="cancel()">取消</span> -->
				
				<!-- 添加弹窗：公司栏失去焦点触发-->
				<div id="companyInfo" style="display: none">
					<div style="width: 100%;box-sizing: border-box;padding: 10px 10px 0;min-height: 170px;max-height: 350px;">					
						<div style="width: 100%;">
							<div id="companyList" style="width: 100%;"></div>
						</div>
						<div style="position: relative;">
							<div id="pp" style=""></div>
						</div>
					</div>
				</div>
				<!--遮罩-->
			<!-- 	<div class="shadeBox">
					<div class="tipsInfo">
						<p>正在处理中......</p>
					</div>
				</div> -->
				
		<!-- </div> -->
		  </div>
		</div>
        <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
        <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
        <script type="text/javascript"  src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
        <script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
        <script src="<%=basePath%>/customerManagement/js/addCustomer.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>
