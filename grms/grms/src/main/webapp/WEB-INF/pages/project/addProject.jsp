<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html>
<html>

<head lang="en">
<meta charset="UTF-8">
<title>新建项目</title>
<meta http-equiv="X-UA-Compatible" content="edge" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/default.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/project/css/addProject.css" />
</head>
<body>
	<input type="text" id="menuId" value="${menuId}" disabled="disabled"
		hidden="true" />
	<input type="text" id="projectMeauList" value="${projectMeauList}" 	hidden="true" />
	<div class="wrap">
		<!-- <div id="" class="fl back">
			<a onclick="goback()">返回</a>

		</div>
		<div class="border fl"></div> -->
		<div class="topTitle clearfix">
		    <div class="go-back fl" style="margin-left:24px;margin-right:0" onclick="goback()">返回</div>		
			<div class="path1 fl">项目管理</div>
			<div class="path2 fl">新建项目</div>
		</div>
		<form action="" method="post">
		    <input type="hidden" id="projectId" name="projectId" value="${projectVo.id}" ></input> <!-- 项目id -->
		    <input type="hidden" id="projectCode" name="projectCode" value="${projectVo.projectCode}" ></input> <!-- 项目编号 -->
		
			<div class="personal">
				<div class="title1">新建项目信息</div>
				<input type="hidden" type="text" id="userDetailId" value=""
					maxlength="10" />

				<div>
					<div class="popTop">
						<label> <span class="xinhao">项目名称：</span>
						</label> <label> <input type="text"  id="name" value="${projectVo.name}"
							maxlength="50"  placeholder="限制输入50个字数" />
						</label>
					</div>

					<div class="customerBox">
						<label> <span class="xinhao">客户信息：</span>
						</label> <label> <input type="hidden" id="customerId" value="${projectVo.customerId }"  />
						   <input type="text"   style="width:250px;"	id="customerName" value="${projectVo.customerInfo }"  ${projectVo.customerInfo eq null or customerInfo eq ""?"hidden='true'" :"" }
							readonly="readonly" />
						</label> <label> <span class="buleButton" onclick="addCustomer()">选择客户</span>
						</label>
					</div>
                    <!-- 添加联系人 开始 ------------------------------------------------------------------------------------------->
					<div id="projectPeopleList">
                      
                      <c:choose>
                      <c:when test="${projectVo.projectPeoples== null || fn:length(projectVo.projectPeoples) == 0}">
                      <input type="text" id="projectPeoples" value="1" hidden="true">
                           <div class="projectPeople">
								<div class="projectPeople1">
								     <span class="buleButton fr" style="display: none;" onclick="removeProjectPeople(this)">删除</span>
								</div>
								<div class="projectPeople2">						   
									<div class="projectPeopleTBox">
										<label class="projectPeopleTl"> <span class="xinhao">项目角色：</span></label>
										<label class="projectPeopleTR"> <input id="projectRole1" class="project_role" name="projectRole" value='请选择' style ="padding :10px;width : 100%;height: 36px;border: 1px solid #ccc;" >  
										</label>
									</div>
									
									<div class="projectPeopleTBox">
										<label class="projectPeopleTl"> <span class="xinhao">联系人名称：</span></label>
										<label class="projectPeopleTR"> <input type="text" style="height: 34px;border:1px solid #ccc;border-radius:5px;" name="projectlinkName"  placeholder="请输入少于10位数字"
											maxlength="10" />
										</label>
									</div>
									
									<div class="projectPeopleTBox">
										<label class="projectPeopleTl"> <span class="xinhao">联系人电话：</span></label>
										<label class="projectPeopleTR"> <input name="projectlinkPhone" style="height: 34px;border:1px solid #ccc;border-radius:5px;" placeholder="请输入少于11位数字" type="text" id="name" 
											maxlength="11" />
										</label>
									</div>								
								</div>
							</div>
                      </c:when>
                      <c:otherwise>
                      <input type="text" id="projectPeoples" value="${fn:length(projectVo.projectPeoples)}" hidden="true">                      
                      <c:forEach var="projectPeople" items="${projectVo.projectPeoples}" varStatus="idx">
							<div class="projectPeople">
								<div class="projectPeople1">
								     <span class="buleButton fr"  onclick="removeProjectPeople(this)">删除</span>
								</div>
								<div class="projectPeople2">						   
									<div class="projectPeopleTBox">
										<label class="projectPeopleTl"> <span class="xinhao">项目角色：</span></label>
										<label class="projectPeopleTR"> <input class="project_role" id="projectRole${idx.index+1}"  name="projectRole" value='${projectPeople.projectRoleId eq null || projectPeople.projectRoleId==""?"请选择":projectPeople.projectRoleId}' style ="padding :10px;width : 100%;height: 32px;" >  
										</label>
									</div>
									
									<div class="projectPeopleTBox">
										<label class="projectPeopleTl"> <span class="xinhao">联系人名称：</span></label>
										<label class="projectPeopleTR"> <input  type="text" style="height: 34px;border:1px solid #ccc;border-radius:5px;" name="projectlinkName" value="${projectPeople.name }"  placeholder="请输入少于10位字数"
											maxlength="10" />
										</label>
									</div>
									
									<div class="projectPeopleTBox">
										<label class="projectPeopleTl"> <span class="xinhao">联系人电话：</span></label>
										<label class="projectPeopleTR"> <input name="projectlinkPhone" style="height: 34px;border:1px solid #ccc;border-radius:5px;" placeholder="请输入少于11位数字" type="text" id="name" value="${projectPeople.phone }"
											maxlength="11" />
										</label>
									</div>								
								</div>
							</div>
						</c:forEach>
						</c:otherwise>
						</c:choose>						
					</div>					
					<!-- 添加联系人 结束 ------------------------------------------------------------------------------------------->
					
					<div class="projectPeopleListAddBox">
					  <span class="buleButton fr" style="margin-left: 40px;cursor:pointer;" onclick="addProjectPeople()">新增一条</span>
					</div>	
					              
				</div>
				
				<div class="projectExplainTitle">
				   项目说明：
				</div>				 
				<div class="projectExplainBox">
				   <textarea id="description" rows="10" cols="70" maxlength="300"  placeholder="请输入少于300个字  ">${projectVo.description }</textarea>
				</div>	
			</div>

			<div class="personal">
				<div class="title1">新建资源清单</div>
				<div style="width:100%">
				  <div class="personalButton">  <span class="buleButton fl" onclick="addProjectProject()">新增</span>   <span onclick="downProductMeans()" class="buleButton fr">资源清单下载</span> </div>
				  <div class="personalListBox1">
				         <div class="personalListBox2" style="width:100%">
				         </div>
				  </div>

				  
				  <div class="projectFileBox">
				      <label> <span>选择附件：</span></label>
					 <label> 					 		
					       <input type="file" id="projectFile" name="projectFile" value="浏览">					       
					 </label>
					 <label>
					     <span class="buleButton" onclick="updateFile()">上传附件</span>
					 </label>
				  </div>
				  
				  <div class="projectFileListBox">				     
					<table id="projectFileList" border="1" style="margin-left: 10px;">
					    <tr>
					        <td>附件名称</td>
					        <td>操作</td>					        
					    </tr>
					    <c:forEach items="${attchmentsFrom }" var="attachment" varStatus="idx">
					        <tr id='${attachment.id }'>
					           <td>${attachment.fileName }</td>
					           <td>
                		        <a onclick='downloadAttchment("${attachment.id }","${attachment.fileUrl }")'>下载</a>
                		        <a onclick='deleteAttchment("${attachment.id}")'>删除</a>
                		       </td>
                		   </tr>
					    </c:forEach>
					    
					</table>
				  </div>
				  <div></div>
				</div>				
			</div>
			
			<div class="bottomButtonBox">
			    <span class="buleButton"  onclick="goback()">返回</span>
			    
			    <span class="buleButton" onclick="saveProject()">确认立项</span>
			</div>
		</form>
	</div>
	
	<!-- 确认立项弹窗 -->
	<div id="save_project" style="display: none">
		<div id="textHint"></div>
	</div>
		
	
	<!-- 添加客户的弹窗-->
	<div id="add_Customer" style="display: none">
		<div style="box-sizing: border-box;padding: 10px 10px 0;width:100%;">
			<div id="customerList" style="width:100%;"></div>
		</div>
		<div style="position: relative;">
			<div id="pp" style=""></div>
		</div>
	</div>
	
	<!--遮罩-->
	<div class="shadeBox">
		<div class="tipsInfo">
			<p>正在处理中......</p>
		</div>
	</div>

	<script type="text/javascript"
		src="<%=basePath%>/common/easyui/jquery.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/common/plugin/common.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/project/js/addProject.js"></script>
<script type="text/javascript">
function getResourceView(sss,$eleS){
	var list1 = [];
	var list2 = [];
	var list3 = [];
	var list4 = [];
	var dataList = [];
	var si = 0;
	for(var i = 0; i < sss.length; i++) {
		for(var j = 0; j < sss[i].projectMenus.length; j++) {
			if(sss[i].projectMenus.length > 1 && j == 0) {
				if(sss[i].businessName == "社区运营") {//社区运营和其他类型合并规则区分
					list1.push({
						index: si,
						rowspan: sss[i].projectMenus.length
					});
					list2.push({
						index: si,
						rowspan: sss[i].projectMenus.length
					});
					list3.push({
						index: si,
						rowspan: sss[i].projectMenus.length
					});
					list4.push({
						index: si,
						rowspan: sss[i].projectMenus.length
					});
				} else {
					list2.push({
						index: si,
						rowspan: sss[i].projectMenus.length
					});
					list4.push({
						index: si,
						rowspan: sss[i].projectMenus.length
					});
				}
			}
			si++;
			dataList.push({
				fieldName: sss[i].fieldName,
				businessName: sss[i].businessName,
				productType: sss[i].projectMenus[j].productType,
				productMenu: sss[i].projectMenus[j].productMenu,
				productSpec: sss[i].projectMenus[j].productSpec,
				areaNames: sss[i].projectMenus[j].areaNames,
				beginTime: sss[i].projectMenus[j].beginTime,
				endTime: sss[i].projectMenus[j].endTime,
				buyNum: sss[i].projectMenus[j].buyNum,
				id: sss[i].id,
				businessType:sss[i].businessType,
				listlenth:sss[i].projectMenus.length,
			})
		}
	}
	$eleS.datagrid({
		data: dataList,
		fitColumns: true,
		nowrap: false,
		scrollbarSize: 0,
		emptyMsg:'<span>无记录</span>',
		onLoadSuccess: function(data) {
			$(".datagrid-cell-check").addClass('dataCheck');
			$(".datagrid-header-check").addClass('dataCheck');
			for(var a = 0; a < list1.length; a++) {
				$eleS.datagrid('mergeCells', { //第一列合并
					index: list1[a].index,
					field: 'fieldName',
					rowspan: list1[a].rowspan
				});
			}
			for(var b = 0; b < list2.length; b++) {
				$eleS.datagrid('mergeCells', { //第一列合并
					index: list2[b].index,
					field: 'businessName',
					rowspan: list2[b].rowspan
				});
			}
			for(var c = 0; c < list3.length; c++) {
				$eleS.datagrid('mergeCells', { //第一列合并
					index: list3[c].index,
					field: 'areaNames',
					rowspan: list3[c].rowspan
				});
			}
			for(var d = 0; d < list4.length; d++) {
				$eleS.datagrid('mergeCells', { //第一列合并
					index: list4[d].index,
					field: 'id',
					rowspan: list4[d].rowspan
				});
			}
			
			$eleS.parent().find('td[field=areaName] .datagrid-cell').each(function() {
				$(this).attr('title', $(this).html());
			});
			setTimeout(function(){
				$eleS.datagrid('resize');
			},30)
		},
		columns: [
			[ //相应调整显示栏
				{
					field: 'fieldName',
					title: '场次名称',
					width: 100,
					align: "center",
					formatter: function(value,row,index){						
						if(value==''||value==null||value=="undefined"){
							return "/";
						}else{
							return value;
						}					
					}
				},
				{
					field: 'businessName',
					title: '业务',
					width: 110,
					align: "center"
				},
				{
					field: 'productType',
					title: '类型',
					width: 100,
					align: "center"
				},
				{
					field: 'productMenu',
					title: '产品清单',
					width: 80,
					align: "center"
				},
				{
					field: 'productSpec',
					title: '产品规格',
					width: 80,
					align: "center"
				},
				{
					field: 'buyNum',
					title: '购买数量',
					width: 80,
					align: "center"
				},
				{
					field: 'beginTime',
					title: '执行开始时间',
					width: 150,
					align: "center",
					formatter: function(value,row,index){						
						if(row.businessType=='1'){
							return value;
						}else{
							return value.substr(0,11);
						}						
					}
				},
				{
					field: 'endTime',
					title: '执行结束时间',
					width: 150,
					align: "center",
					formatter: function(value,row,index){
						if(row.businessType=='1'){
							return value;
						}else{
							return value.substr(0,11);
						}	
					}
				},
				{
					field: 'areaNames',
					title: '已选小区',
					width: 230,
					align: "center"
				},
				{
					field: 'id',
					title: '操作',
					width: 140,
					align: "center",
					formatter: function(value,row,index){
						var _htmlbt='<a href="javascript:;" data-id="1" onclick="updateProductMean(\''+value+'\',\''+row.businessType+'\')">编辑</a>'+'<a href="javascript:;" data-id="1" onclick="deleteProductMean(\''+value+'\',\''+row.businessType+'\','+index+',\''+row.listlenth+'\')">删除</a>';
						return _htmlbt;
					}
					
				}
			]
		]
	});
}

$(function(){	
	//var sss=$("#projectMeauList").val();
	console.log('${projectMeauList}');
	var sss=${projectMeauList == null ? "333" :projectMeauList};
	
	console.log(sss);
     //增加我们想要执行的代码
     if(sss==null|| sss===undefined){    
    	 
     }else{
    	 getResourceView(sss,$('.personalListBox2')); 
     }
});    
</script>
</body>

</html>