<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
</head>
<body>
	<div style="padding: 5%">
		<table class="tableStyle">
			<tr>
				<td width="35%">申请公司或企业</td>
				<td>${joinArea.company}</td>
			</tr>
			
			<tr>
				<td>小区名称</td>
				<td>${joinArea.areaName}</td>
			</tr>
			
			<tr>
				<td>小区地址</td>
				<td>${joinArea.areaAddress}</td>
			</tr>
			
			<tr>
						<td>审核状态
						</td>
						<td>
							<c:choose>
				    			<c:when test="${joinArea.verifyStatus=='2'}">
				    					<span style="color: green;">审核通过</span>
				    			</c:when>
				    			<c:when test="${joinArea.verifyStatus=='1'}">
				    					<span style="color: red;">审核不通过</span>
				    			</c:when>
				    			<c:otherwise>
				    					<span style="color: red;">审核中</span>
				    			</c:otherwise>
				    		</c:choose>
						</td>
					</tr>
					
			<tr>
				<td>审核人</td>
				<td>${joinArea.verifyManager}</td>
			</tr>
			<tr>
				<td>审核账号</td>
				<td>${joinArea.verifyAccount}</td>

			</tr>
			
			<tr>
				<td>接入账号</td>
				<td>${joinArea.joinAccount}</td>
			</tr>
		</table>
	</div>
	
</body>
</html>