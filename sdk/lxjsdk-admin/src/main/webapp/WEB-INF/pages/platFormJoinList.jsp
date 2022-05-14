<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>审核管理</title>
<link href="css/easyui.css" rel="stylesheet" type="text/css"/>
<link href="styles/displaytag.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="js/stree.js"></script>
<script type="text/javascript" src="js/selectTree.js"></script>
<script type="text/javascript" src="js/tsmsFormUtil.js"></script>
<script type="text/javascript" src="js/model/platformJoin/platformJoin.js"></script>
</head>
<body>
<div class="position">
	<div class="center">
	<div class="left">
	<div class="right">
		<span>当前位置：审核管理>> 平台对接申请账号</span>
	</div>	
	</div>	 
	</div>
</div>	

<div id="scrollContent">
	<s:form id="platForm" action="platFormJoin.html" theme="simple">
		<div class="box2" roller="false">
				<table >
					<tr>
						<td >申请公司或企业名称：</td>
						<td ><input id="company" name="queryBean.company" value="${queryBean.company}" type="text" style="width: 120px;" /></td>
					
						<td >审核状态：</td>
						<td><s:select list="#{'':'全部','0':'审核中','1':'审核不通过','2':'审核通过'}" name="queryBean.verifyStatus" theme="simple"/></td>
						<hori:auth value="LXJSDK:12101:1210101:search">
						<td >
							<button type="submit" onclick=""><span class="icon_find">查询</span></button>
						</td></hori:auth>
					</tr>
					
				</table>
		</div>
		
		
		
		<div>
			<display:table name="plats" class="tableStyle" id="pla" requestURI="platFormJoin.html"
				 pagesize="50" cellpadding="0" cellspacing="0" style="text-align:center;" >	
				
				<display:column property="company" sortable="false" headerClass="sortable"
								title="申请公司或企业" 
								style="word-break:break-all;"/>
				
				<display:column property="joinAccount" sortable="false" headerClass="sortable"
								title="接入账号"
								style="word-break:break-all;"/>
				
				<display:column media="html" sortable="false" headerClass="sortable" title="审核状态" style="width:3%">
		    		<c:choose>
		    			<c:when test="${pla.verifyStatus=='2'}">
		    					<span class="img_ok hand" title="审核通过" style="margin-left:15px;"></span>
		    			</c:when>
		    			<c:when test="${pla.verifyStatus=='1'}">
		    					<span class="img_delete hand" title="审核不通过" style="margin-left:15px;"></span>
		    			</c:when>
		    			<c:otherwise>
		    					<span class="img_warn hand" title="审核中" style="margin-left:15px;"></span>
		    			</c:otherwise>
		    		</c:choose>
				</display:column>					
				<display:column property="verifyManager" sortable="false" headerClass="sortable"
								title="审核人"
								style="word-break:break-all;"/>

				<display:column property="verifyAccount" sortable="false" headerClass="sortable"
								title="审核账号"
								style="word-break:break-all;"/>
				
				<display:column property="verifyTime" sortable="false" headerClass="sortable"
								format="{0,date,yyyy-MM-dd HH:mm:ss}"
							    title="审核时间"
								style="word-break:break-all;" />		
								
				<display:column property="createAccount" sortable="false" headerClass="sortable"
								title="应用创建账号"
								style="word-break:break-all;"/>				
				<display:column media="html" sortable="false" headerClass="sortable" title="操作" style="width:10%">
					
					<hori:auth value="LXJSDK:12101:1210101:view">
					<span class="img_view hand" title="查看" style="margin-left:5px;"
								onclick="view('${pla.id}')"></span></hori:auth>
					<hori:auth value="LXJSDK:12101:1210101:audit">			
					<c:if test="${pla.verifyStatus=='0' || pla.verifyStatus ==null}">
						<span class="img_guard hand" title="审核公司" style="margin-left:5px;"
								onclick="audit('${pla.id}')"></span>
					</c:if>
					</hori:auth>
					<hori:auth value="LXJSDK:12101:1210101:auditArea">			
					<c:if test="${pla.verifyStatus=='2'}">
						<span class="img_guard hand" title="接入小区" style="margin-left:5px;"
								onclick="auditArea('${pla.id}')"></span>
					</c:if>
					</hori:auth>
				</display:column>				
			</display:table>
		</div>
		
	</s:form>
</div>
</body>
</html>