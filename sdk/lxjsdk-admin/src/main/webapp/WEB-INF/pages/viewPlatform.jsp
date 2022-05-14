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
						<td width="10%">申请公司或企业名称
						</td>
						<td>${formJoin.company}
						</td>
					</tr>
					<tr>
						<td width="10%">接入帐号
						</td>
						<td>${formJoin.joinAccount }</td>
					</tr>
					<tr>
						<td width="10%">接入密码
						</td>
						<td>${formJoin.joinPassword }
						</td>
					</tr>
					<tr>
						<td width="10%">审核状态
						</td>
						<td>
							<c:choose>
				    			<c:when test="${formJoin.verifyStatus=='2'}">
				    					<span style="color: green;">审核通过</span>
				    			</c:when>
				    			<c:when test="${formJoin.verifyStatus=='1'}">
				    					<span style="color: red;">审核不通过</span>
				    			</c:when>
				    			<c:otherwise>
				    					<span style="color: red;">审核中</span>
				    			</c:otherwise>
				    		</c:choose>
						</td>
					</tr>
				
					<tr>
						<td width="10%">审核人
						</td>
						<td>${formJoin.verifyManager }</td>
					</tr>
					
					<tr>
						<td width="10%">审核账号
						</td>
						<td>${formJoin.verifyAccount }</td>
					</tr>
					
					<tr>
						<td width="10%">审核时间
						</td>
						<td><fmt:formatDate value="${formJoin.verifyTime}" pattern="yyyy-MM-dd HH:mm:ss" />
						</td>
					</tr>
					
					<tr>
						<td width="10%">应用创建账号
						</td>
						<td>${formJoin.createAccount }</td>
					</tr>
					
					<tr>
						<td width="10%">应用创建时间
						</td>
						<td><fmt:formatDate value="${formJoin.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
						</td>
					</tr>
				</table>
	</div>
</body>
</html>