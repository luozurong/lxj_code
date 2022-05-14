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
			<td>申请公司或企业</td>
			<td>${application.company}</td>
		</tr>
		<tr>
						<td>应用名称
						</td>
						<td>${application.appName }</td>
					</tr>
		
		<tr>
						<td>app包名
						</td>
						<td>${application.appPackage }
						</td>
					</tr>
		<tr>
						<td>app类型
						</td>
						<td>
						<c:choose>
			    			<c:when test="${application.appType=='1'}">
			    					<span>安卓</span>
			    			</c:when>
			    			
			    			<c:otherwise>
			    					<span>苹果</span>
			    			</c:otherwise>
			    		</c:choose>
						</td>
					</tr>
		<tr>			
			<td>应用创建帐号</td>
			<td>${application.createAccount }</td>
		</tr>
		
	</table>
	</div>
</body>
</html>