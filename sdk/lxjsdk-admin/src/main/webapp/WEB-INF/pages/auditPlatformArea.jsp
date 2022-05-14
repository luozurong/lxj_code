<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<script type="text/javascript">
    var hasCheck = 0;
	function sumitDataOk(){
		var organizationSeq = $(":radio:checked").val();
		var checkFlag = $("#checkFlag").val();
		if(organizationSeq =='' || organizationSeq ==undefined){
			top.Dialog.alert("没有选择小区!");
			return false;
		}else{
			var joinAreaId = $("#joinAreaId").val();
				$.ajax({
					type: "POST",
					url: "auditPass.html",
					data: "id="+joinAreaId+"&organizationSeq="+organizationSeq+"&result=2",
					async: false,
					success: function(msg){
						if (msg == "0"){//该住房业主信息尚未完善
							hasCheck = "-1";
						}
						else if(msg =="1"){
							hasCheck = "1";
						}
					},
				    error:function (msg){
						hasCheck = "-2";//网络异常
				    }				
			});
			
		}
		if(hasCheck =="0"){
		}else if(hasCheck =="-1"){
			top.Dialog.alert("审核失败！");
			return false;
		}else if(hasCheck =="-2"){
			top.Dialog.alert("网络异常！");
			return false;
		}else if(hasCheck =="1"){
			return true;
		}
	}
	
	function sumitDataNotOk(){
		var joinAreaId = $("#joinAreaId").val();
		var hasCheck = "1";
		$.ajax({
			   type: "POST",
			   url: "auditPass.html",
			   data: "id="+joinAreaId+"&result=1",
			   async: false,
			   success: function(msg){
				   if (msg == "0"){//该住房业主信息尚未完善
						hasCheck = "-1";
					}
			   },
			   error:function (msg){
				   hasCheck = "-2";//网络异常
			   }
		
			});
		
		if(hasCheck =="1"){
			return true;
		}else if(hasCheck =="-1"){
			top.Dialog.alert("审核失败！");
			return false;
		}else if(hasCheck =="-2"){
			top.Dialog.alert("网络异常！");
			return false;
		}
	}
</script>
</head>
<body>
	<div style="padding: 5%">
		<s:hidden id="joinAreaId" name="id" />
		<s:hidden id="checkFlag" value ="0" />
		<table id="listTab" class="tableStyle"  style="text-align: center;">
			<tr>
				<th width="5%"><span>选择</span></th>
				<th width="30%"><span>小区名称</span></th>
				<th width="50%"><span>小区地址</span></th>
			</tr>
			<c:choose>
				<c:when test="${empty areas }">
					<tr><td colspan="3" align="center">没有可显示记录</td></tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${areas }" var="rst" varStatus="status" >
						<tr style="text-align: center;">
							<td width="5%"><span><input type="radio" name="organizationSeq"  value="${rst.organizationSeq}"/></span></td>
							<td width="30%">
								<span>
									${rst.areaName }							    
								</span>
							</td>
							<td width="50%">
								<span>
									${rst.address }							    
								</span>
							</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</table>
	</div>
</body>
</html>