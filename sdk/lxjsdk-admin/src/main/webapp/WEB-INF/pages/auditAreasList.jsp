<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<script type="text/javascript" src="js/model/platformJoin/platformJoinArea.js"></script>
</head>
<body>
	<div style="padding: 3%">
	    <s:form id="platformJoinArea" action="auditAreasList.html" theme="simple">
	    <s:hidden id="platformJoinId" name="id" />
	    	<table id="listTab" class="tableStyle"  align="center">
			<tr align="center"><th colspan="6">${company }</th></tr>
			<tr style="text-align: center;">
				<th width="15%"><span>小区名称</span></th>
				<th width="50%"><span>小区地址</span></th>
				<th width="5%"><span>审核状态</span></th>
				<th width="15%"><span>审核时间</span></th>
				<th width="15%"><span>操作</span></th>
			</tr>
			<c:choose>
				<c:when test="${empty joinAreasList }">
					<tr><td colspan="6" align="center">没有可显示记录</td></tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${joinAreasList }" var="rst" varStatus="status" >
						<tr style="text-align: center;">
							<td width="15%">
								<span>
									${rst.areaName }							    
								</span>
							</td>
							<td width="50%">
								<span>
									${rst.areaAddress }							    
								</span>
							</td>
							<td width="5%">
									<c:choose>
						    			<c:when test="${rst.verifyStatus=='2'}">
						    					<span class="img_ok hand" title="审核通过" style="margin-left:15px;"></span>
						    			</c:when>
						    			<c:when test="${rst.verifyStatus=='1'}">
						    					<span class="img_delete hand" title="审核不通过" style="margin-left:15px;"></span>
						    			</c:when>
						    			<c:otherwise>
						    					<span class="img_warn hand" title="审核中" style="margin-left:15px;"></span>
						    			</c:otherwise>
						    		</c:choose>
							</td>
							<td width="15%">
								<span>
									<fmt:formatDate value="${rst.verifyTime}" pattern="yyyy-MM-dd HH:mm:ss" />						    
								</span>
							</td>
							<td width="15%">
								<span class="img_view hand" title="查看" onclick="viewPlatformArea('${rst.id}')"></span>
								<c:if test="${rst.verifyStatus=='0' || rst.verifyStatus ==null || rst.verifyStatus ==''}">
									<span class="img_guard hand" title="审核小区" style="margin-left:5px;"
											onclick="auditPlatformArea('${rst.id}')"></span>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</table>
	    </s:form>
		
	</div>
</body>
</html>