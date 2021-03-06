<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*" %>
<%@ page language="java" import="com.hori.vo.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html style="height: 100%;">
	<head>
	<meta charset="UTF-8">
	<title>执行管理</title>
	<meta http-equiv="X-UA-Compatible" content="edge" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/default.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/projectAction/css/projectActionExceptionList.css" />
    <link rel="shortcut icon" href="<%=basePath%>/homepage/images/ywhlzt.ico" type="image/x-icon">
	</head>
	<body >
		<div class="workorder-breadcrumb">
	    	<span class="go-back"  onClick="goback()">返回</span>
	 	    <span class="workorder-breadcrumb-icon"></span>
		    <span>执行清单列表</span>
		    <span class="ic_right"></span>
		    <span>异常明细</span>
		</div>
		<div class="wrap">
			
			<div class="conditions-select">
				<div style="height: 60px; padding: 10px 0;">
					<div class="account fl" style="margin-left: 0px;">上报部门：</div>
					<div class="account1 fl">
						<select id="businessType" style="height: 34px;" class="easyui-combobox" name="businessType"  data-options="editable:false,panelHeight:120,width:120">   
						    <option value="-1" <c:if test="${-1 eq querryBean.businessType}">selected</c:if> >全部</option>
						    <option value="1" <c:if test="${1 eq querryBean.businessType}">selected</c:if> >社区运营</option>   
						    <option value="2" <c:if test="${2 eq querryBean.businessType}">selected</c:if> >电商运营</option>   
						    <option value="3" <c:if test="${3 eq querryBean.businessType}">selected</c:if> >用户运营</option>   
						    <option value="4" <c:if test="${4 eq querryBean.businessType}">selected</c:if> >媒管</option>   
						    <option value="0" <c:if test="${4 eq querryBean.businessType}">selected</c:if> >财务</option>   
						</select>
					</div>
					<div class="account fl">处理状态：</div>
					<div class="account1 fl">
						<select id="status" style="height: 34px;" class="easyui-combobox" name="status"  data-options="editable:false,panelHeight:90,width:120">   
						    <option value="-1" <c:if test="${-1 eq querryBean.status}">selected</c:if> >全部</option>
						    <option value="0" <c:if test="${0 eq querryBean.status}">selected</c:if> >待处理</option>
						    <option value="1" <c:if test="${1 eq querryBean.status}">selected</c:if> >处理中</option>
						    <option value="2" <c:if test="${2 eq querryBean.status}">selected</c:if> >已处理</option>   
						</select>
					</div>
					<div class="account fl">上报时间：</div>
					<div class="account1 fl">
						<input style="height: 34px;width:130px;" id="createTime" value="<fmt:formatDate value='${querryBean.createTime}' type='date' pattern='yyyy-MM-dd'/>" type="text"  ></input>
					</div> 
					<div class="account1 fl" >
						<a id="searchBtn" style="height: 34px;width: 120px;" onClick="findMenu()" class="search" href="#">查询</a>
					</div>
				</div>
				<input id="productCode" type="hidden" value="${productCode}">
				<input type="hidden" id="pageNumber" value="${querryBean.pageNumber}">
				<input type="hidden" id="pageSize" value="${querryBean.pageSize}">
			</div>
			<div class="content-wrap">
				<table id="dg"></table>
				<div id="pp" style="position: relative;"></div>
			</div>
		</div>
		
		<div id="win" style="display:none;">
			<input id="exceptionId" type="hidden"/>
			<input id="handleType" type="hidden"/>
			<div style="padding:10px;text-align:center;">
				<div id="finance">
					<span class="word">应扣金额(元)：</span>
					<input id="money" style="width:120px"/>
				</div>
				<div>
					<input style="width: 90px;" id="resultRemark" />
				</div>
			</div>
		</div>
		<div id="areaInfo" style="display:none;">
			<div class='areaInfoBox'>			    
				<div style="padding: 13px 0;">
					<div class='areaInfo'>
						<span>小区编码：</span><span id="areaCode"></span>
					</div>
					<div class='areaInfo'>
						<span>小区地址：</span><span id="areaAddress" ></span>
					</div >
					<div class='areaInfo'>
						<span>小区名称：</span><span id="areaName" ></span>
					</div >
					<div class='areaInfo'>
						<span>使用开始时间：</span><span id="beginTime" ></span>
					</div>
					<div class='areaInfo'>
						<span>使用结束时间：</span><span id="endTime" ></span>
					</div>
				</div>
			</div>
		</div>
		
        <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
        <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
        <script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
        <script type="text/javascript"	src="<%=basePath%>/projectAction/js/projectActionExceptionList.js"></script>
        <script src="<%=basePath%>/homepage/js/index.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>
