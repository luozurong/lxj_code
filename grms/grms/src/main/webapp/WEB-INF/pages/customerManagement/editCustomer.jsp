<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<title>修改客户信息</title>
	<meta http-equiv="X-UA-Compatible" content="edge" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"> 
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
	<link rel="stylesheet" type="text/css" href="<%=basePath%>/customerManagement/css/addCustomerMessager.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>/customerManagement/css/custome_common.css"/>
	<style type="text/css">
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
		<input type=hidden id='detailId'  value="${id}" />
		
		<input id="provinceCode"  type="hidden" value="${requestScope.custMap.provinceId}"/>
        <input id="cityCode"  type="hidden" value="${requestScope.custMap.cityId }"/>
        <input id="districtCode"  type="hidden" value="${requestScope.custMap.districtId }"/>
        <div class="wrap">
		    <div class="topTitle clearfix">
		        <div class="go-back fl" style="margin-left:24px;margin-right:0" onclick="cancelEdit()">返回</div>
				<div class="path1 fl">客户管理</div>
				<!-- <div class="path2 fl">客户管理</div> -->
				<div class="path2 fl">修改客户信息</div>
			</div>
		</div>
		<div class="content-wrap">
			<div class="addCustomerMessager">
				<div class="addCustomer-item">
					<span class="addCustomer-item-name">业务员：</span>
					<span id = "salesman">${requestScope.custMap.salesman}</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>公司名称：</span>
					<input class="easyui-validatebox" maxlength="20" type="text" data-options="required:true" id="companyName" value="${requestScope.custMap.name }">
					<span class="redText">不可为空</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>公司地址：</span>
				        <select class="easyui-combobox" value="" id="province" style="width:100px;height:34px;"></select>
				        <select class="easyui-combobox" value="" id="city" style="width:100px;height:34px;"></select>
			            <select class="easyui-combobox" value="" id="area" style="width:100px;height:34px;"></select>
				        
			            <input type="text" style="width: 300px;" id="address" maxlength="30" class="easyui-validatebox" data-options="required:true" value="${requestScope.custMap.address }" placeholder="详细地址">
						<span class="redText">不可为空</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>公司类型：</span>
					<!-- <input class="easyui-validatebox" data-options="required:true" type="text" value="${requestScope.custMap.customerType }" id="companyType">
					<span class="redText">不可为空</span> -->
					<select id="companyType" name="companyType" required="required" style="width:110px;height:34px;">
						<option value="${requestScope.custMap.customerTypeId }">${requestScope.custMap.customerType }</option>
						<c:forEach items="${type }" var="t" varStatus="tp">
							<option value="${t.id }">${t.name}</option>
						</c:forEach>
					</select>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>主要经营：</span>
					<input class="easyui-validatebox" data-options="required:true" type="text" value="${requestScope.custMap.industry }" id="industry" maxlength="20">
					<span class="redText">不可为空</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>所属部门：</span>
					<input class="easyui-validatebox" data-options="required:true" type="text" value="${requestScope.custMap.department }" id="department" maxlength="20">
					<span class="redText">不可为空</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name">负责人名称：</span>
					<input class="addCustomer-item-input" value="${requestScope.custMap.dutyName }" id="dutyName" maxlength="20">
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name">负责人电话：</span>
					<input class="addCustomer-item-input" value="${requestScope.custMap.dutyPhone }" id="dutyPhone" maxlength="20">
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>主要联系人名称：</span>
					<input class="easyui-validatebox" data-options="required:true" type="text" value="${requestScope.custMap.contactor }" id="contactor" maxlength="20">
					<span class="redText">不可为空</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name"><span class="cl1-1">*</span>主要联系人电话：</span>
					<input  class="easyui-validatebox" data-options="required:true" type="text" value="${requestScope.custMap.contactorPhone }" id="contactorPhone" maxlength="20">
					<span class="redText">不可为空</span>
				</div>
				<div class="addCustomer-item">
					<span class="addCustomer-item-name">备注说明：</span>
					<textarea id="remark" rows="5" cols="40" maxlength="200" style="border: 1px solid #ccc;padding:5px;">${requestScope.custMap.remark }</textarea>
				</div>
				
				
				<div class="addCustomer-item">
						<span class="addCustomer-item-name">客户信息移交记录：</span>
					    <div id="transferInfoBox"  style="width:400px;margin-top: 11px;display: inline-block;vertical-align: top;">
						    <div id="transferInfo"  style="width:400px;">					    
						    </div>
					    </div>
					</div>
				
				<div class="bottomButtonBox">
					  <!--   <span class="buleButton"  onclick="addCust()">修改</span>				    
					    <span class="buleButton" onclick="cancelEdit()">取消</span> -->
					    <button class="buleButton"  onclick="addCust()" style="margin-right:40px">修改</button>				    
					    <button class="buleButton" onclick="cancelEdit()">取消</button>
				</div>
				
				<!-- <div>
					<span class="searchBtn" onclick="addCust()">修改</span>
					<span class="searchBtn" onclick="cancelEdit()">取消</span>
				</div> -->
			</div>
		</div>
        <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
        <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
        <script type="text/javascript"  src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
        <script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
        <script src="<%=basePath%>/customerManagement/js/editCustomer.js" type="text/javascript" charset="utf-8"></script>
        <script src="<%=basePath%>/customerManagement/js/transferInfo.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>
