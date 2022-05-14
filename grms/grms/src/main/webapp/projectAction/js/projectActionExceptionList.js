var pageNoAll=1;
var pageSizeAll=10;
$(function(){
	
	$('#createTime').datebox({
		editable:false
	});
	
	$('#searchBtn').linkbutton({  
		
	});
	$('#gobackBtn').linkbutton({  
		
	});
	
	var pageSize = $("#pageSize").val();
	var pageNumber = $("#pageNumber").val();
	
	if(pageSize!=null&&pageSize!=""){
		pageSizeAll = pageSize
	}
	if(pageNumber!=null&&pageNumber!=""){
		pageNoAll = pageNumber
	}
	
	findMenu();
	
	$('#resultRemark').textbox({
		width:350,
		height:200,
		multiline:true,
		deltaX:10,
		prompt:"请输入异常处理的备注"
	})
	
	$('#ensureExceptionHandle').linkbutton({    
	}); 
	$('#cancelExceptionHandle').linkbutton({  
		
	});
	$('#money').numberbox({    
	    min:0,    
	    precision:2,   
	}); 
	
	/*$('#win').window('close');*/
	
	
})

//查询数据
function findMenu(){
	var businessType=$('#businessType').val();
	var status=$('#status').val();
	var createTime=$('#createTime').val();
	var productCode = $('#productCode').val();
	
	pageNo=pageNoAll;
	pageSize=pageSizeAll;
	var queryBean='{"businessType":"'+businessType+'","createTime":"'+createTime
	+'","status":"'+status+'","productCode":"'+productCode
	+'","pageNumber":"'+pageNo+'","pageSize":"'+pageSize+'"}';
	
    $.ajax({  
        type : "POST",  //提交方式  
        url : "/grms/projectAction/getPAExceptionlistData.html",//路径  
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        data:  {"queryBean":queryBean},  
        success : function(result) {//返回数据根据结果进行相应的处理  
            if ( result.success) {  
            	var data2=result.rows;
            	var total=result.total;
            	refresh(data2);
            	paginationpage(total,data2);
            	
            } else {  
            	
            }  
        }  
    }); 
}

//刷新表格数据
function refresh(data){
//	表格数据渲染
  $('#dg').datagrid({
      border:true,
      scrollbarSize:0,
      nowrap:false,//允许换行
      data:data,
      singleSelect:true,
      emptyMsg:'<span>无记录</span>',
      onLoadSuccess:function(){ //dom操作
          $('#dg').datagrid('resize');
          initButton();
      },
      columns:[[
          {
              field:'departmentName',
              title:'上报部门',
              width:200,
              align:'left'
          },
          {
              field:'projectActionCode',
              title:'清单编号',
              width:200,
              align:'left'
              
          },
          {
              field:'createTime',
              title:'上报时间',
              width:300,
              align:'left'
          },
          {
              field:'type',
              title:'异常类型',
              width:200,
              align:'left',formatter:function(value){
            	  if(value==1){
            		  return "执行异常 "; 
            	  }else if(value==2){
            		  return "财务异常";
            	  }
              }
          },
          {
              field:'exceptionRemark',
              title:'上报原因',
              width:400,
              align:'left'
          },
          {
              field:'status',
              title:'处理状态',
              width:200,
              align:'left',
              formatter: function(value){
            	  if(value==0){
            		  return "待处理"
            	  }else if(value==1){
            		  return "处理中"
            	  }else if(value==2){
            		  return "已处理"
            	  }
                
              }
          },
          {
              field:'result',
              title:'处理结果',
              width:200,
              align:'left',
              formatter: function(value,row,index){
            	  
            	  if(value!=4&&row.status==0){
            		  return "/"
            	  }
            	  //1:置换场地 2:扣款 3:继续 4:暂停 5:恢复 6终止
            	  if(value==1){
            		  return "置换场地"
            	  }else if(value==2){
            		  return "扣款"
            	  }else if(value==3){
            		  return "继续"
            	  }else if(value==4){
            		  return "暂停"
            	  }else if(value==5){
            		  return "恢复"
            	  }else if(value==6){
            		  return "终止"
            	  }
                
              }
          },{
              field:'confirmStatus',
              title:'执行组织确认',
              width:250,
              align:'left',
              formatter: function(value,row,index){
            	  
            	  if(row.result!=4&&row.status==0){
            		  return "/"
            	  }
            	  
            	  if(value==0){
            		  return "否"
            	  }else if(value==1){
            		  return "是"
            	  }
                
              }
          },{
              field:'resultRemark',
              title:'处理备注',
              width:400,
              align:'left'
          },
          
          {
              field:'handle',
              title:'操作',
              width:600,
              align:'left',
              formatter: function(value,row,index){
            	  
            	  var businessType = row.businessType;
            	  var status = row.status;
            	  var type = row.type;
            	  var workOrder = "";
            	  //异常处理状态为未处理才有操作
            	  if(status==0){
            		  
            		  if(type==1){
            			  
            			  var deductionWorkOrder = '<a href="javascript:;" data-id="1" name="deduction" onclick="exceptionOperation(1,\''+index+'\')">扣款</a>  ';
            			  
            			  workOrder = deductionWorkOrder;
            			  
            		  }else{
            			  
            			  var pauseWorkOrder = '<a href="javascript:;" data-id="5" name="pause" onclick="exceptionOperation(5,\''+index+'\')">暂停</a>  ';
            		  
            			  workOrder = pauseWorkOrder;
            		  }
            		  
            		  
            		  var terminationWorkOrder = ' <a href="javascript:;" data-id="2" name="termination" onclick="exceptionOperation(2,\''+index+'\')">终止</a>  ';
            		  var goOnWorkOrder = ' <a href="javascript:;" data-id="3" name="goOn" onclick="exceptionOperation(3,\''+index+'\')">继续</a>  ';
            		  
            		  workOrder = workOrder+terminationWorkOrder+goOnWorkOrder;
            		  
            		  if(businessType==1){
            			  
            			  var replacementWorkOrder = ' <a href="javascript:;" data-id="4" name="replace" onclick="exceptionOperation(4,\''+index+'\')">场地置换</a>  ';
            			  
            			  workOrder = replacementWorkOrder+workOrder;
            			  
                	  }
            		  
            	  }
            	  
	              return workOrder;
              }
          }
      ]]
  });
}

var expType = null;

//异常处理操作
function exceptionOperation(type,index){
	//获取要操作得一行数据
	var checkedItem=$('#dg').datagrid('getData').rows[index];
	var id = checkedItem.id
	var actionCode = checkedItem.projectActionCode;
	expType = checkedItem.type;
	//将要终止项目的id存在隐藏域中
	$("#exceptionId").val(id);
	
	$("#finance").css("display", "none");
	
	$("#resultRemark").textbox('clear');
	
	$("#money").numberbox("clear");
	if(type!=4){
		
		//$('#win').window('open');
		handleAlert();

	}
	
	//操作类型：1、扣款2、终止3、继续4、场地置换5、暂停
	if(type==1){
		
		
		$("#finance").css("display", "block");
		$("#handleType").val(1);
	
	}else if(type==2){
		
		if(expType==1){
			$("#finance").css("display", "block");
			
		}
		
		$("#handleType").val(2);
	}else if(type==3){
		
		$("#handleType").val(3);
	}else if(type==4){
		
		$.ajax({
	        type : "GET",  //提交方式  
	        url : "/grms/projectAction/getAreaInfo.html?actionCode=" + actionCode,//路径  
	        dataType : "json",//数据，这里使用的是Json格式进行传输  
	        success : function(result) {//返回数据根据结果进行相应的处理  
	            if ( result.success) {
	            	var obeject=result.obeject;
	            	
	            	$("#areaCode").html(obeject.organizationSeq)
	            	$("#areaAddress").html(obeject.areaAddress)
	            	$("#areaName").html(obeject.areaName)
	            	$("#beginTime").html(obeject.beginTime)
	            	$("#endTime").html(obeject.endTime)
	            	
	            	wrapMaskShow();//父级遮罩显示
	        		$('body').css('overflow','hidden');//禁止滚动
	        		$('#areaInfo').dialog({
	        		        width:500,
	        		        modal:true,
	        		        collapsible:false,
	        		        minimizable:false,
	        		        maximizable:false,
	        		        closable:true,
	        		        draggable:false,
	        		        resizable:false,
	        		        inline:false,
	        		        title:"当前场地信息",
	        		        ok:"点击选择要置换的小区",
	        		        buttons:[{
	        		            text:'点击选择要置换的小区',
	        		            width:220,
	        		            handler:function(){
	        		                wrapMaskHide();
	        		                $('#areaInfo').dialog('close');
	        		                $("#areaInfo").hide();
	        		                window.location.href="/grms/projectAction/substitutionArea.html?actionCode=" + actionCode+"&exceptionId="+id;
	        		            }
	        		        }],
	        		        onClose : function(){
	        		            wrapMaskHide();//父级遮罩隐藏
	        		            $('body').css('overflow','auto');//恢复滚动
	        		        }
	        		    });
	        		    winHiSelfAdaptation($('#roleWin_detail'));//弹窗定位    
	            	
	            } else {  
	            	
	            }  
	        }  
	    }); 
		
		
	}else if(type==5){
		
		$("#handleType").val(5);
	}
}

//确定执行异常操作按钮
function ensureExceptionHandle(){
	
	//获取要终止项目异常的id(隐藏域中)
	var exceptionId=$("#exceptionId").val();
	
	var handleType=$("#handleType").val();
	
	var resultRemark=$("#resultRemark").textbox('getValue');
	
	//handleType 操作类型：1、扣款2、终止3、继续4、场地置换5、暂停
	if(handleType!=1){
		
		if(handleType==2){
			 var money=$("#money").numberbox("getValue");
			 if(expType!=2&&(!money||money==null||money=='')){
				 parent.$.messager.alert('提示','请输入金额！！');
				 return
			 }
			 
				 createConfirm("异常处理操作提示","确定终止执行清单？",true,false,"确定","取消",okCallbakFunciton2,"","","");
				 function okCallbakFunciton2(){
					 $.ajax({  
					        type : "GET",  //提交方式  
					        url : "/grms/projectAction/stopProjectAction.html?exceptionId=" + exceptionId+"&resultRemark="+resultRemark+"&money="+money,//路径  
					        dataType : "json",//数据，这里使用的是Json格式进行传输  
					        success : function(result) {//返回数据根据结果进行相应的处理  
					            if (result.success) {  
					            	
					            	parent.$.messager.alert('提示','终止执行清单操作处理成功！！');
					            	findMenu()
					            	
					            } else {  
					            	parent.$.messager.alert('提示','终止执行清单操作处理失败！！失败原因：'+result.msg);
					            }  
					        }  
					    });
				  }	
				 
		}else if(handleType==3){
			 createConfirm("异常处理操作提示","确定继续执行清单？",true,false,"确定","取消",okCallbakFunciton3,"","","");
			 function okCallbakFunciton3(){
				 $.ajax({  
				        type : "GET",  //提交方式  
				        url : "/grms/projectAction/goOnProjectAction.html?exceptionId=" + exceptionId+"&resultRemark="+resultRemark,//路径  
				        dataType : "json",//数据，这里使用的是Json格式进行传输  
				        success : function(result) {//返回数据根据结果进行相应的处理  
				            if ( result.success) {  
				            	
				            	parent.$.messager.alert('提示','继续执行清单操作处理成功！！');
				            	findMenu()
				            	
				            } else {  
				            	parent.$.messager.alert('提示','继续执行清单操作处理失败！！失败原因：'+result.msg);
				            }  
				        }  
				    });
			  }	
			
			
		}else if(handleType==5){
			createConfirm("异常处理操作提示","确定暂停执行清单？",true,false,"确定","取消",okCallbakFunciton4,"","","");
			 function okCallbakFunciton4(){
				 $.ajax({  
				        type : "GET",  //提交方式  
				        url : "/grms/projectAction/pauseProjectAction.html?exceptionId=" + exceptionId+"&resultRemark="+resultRemark,//路径  
				        dataType : "json",//数据，这里使用的是Json格式进行传输  
				        success : function(result) {//返回数据根据结果进行相应的处理  
				            if ( result.success) {  
				            	
				            	parent.$.messager.alert('提示','暂停执行清单操作处理成功！！');
				            	findMenu()
				            	
				            } else {  
				            	parent.$.messager.alert('提示','暂停执行清单操作处理失败！！失败原因：'+result.msg);
				            }  
				        }  
				    });
		  }							
		}
		
	}else{
		
		var money=$("#money").numberbox("getValue");
		if(!money||money==null||money==''){
			 parent.$.messager.alert('提示','请输入金额！！');
			 return
		 }
		createConfirm("异常处理操作提示","确定对执行清单进行扣款操作？",true,false,"确定","取消",okCallbakFunciton5,"","","");
		 function okCallbakFunciton5(){
			 $.ajax({  
			        type : "GET",  //提交方式  
			        url : "/grms/projectAction/deduction.html?exceptionId=" + exceptionId+"&resultRemark="+resultRemark+"&money="+money,//路径  
			        dataType : "json",//数据，这里使用的是Json格式进行传输  
			        success : function(result) {//返回数据根据结果进行相应的处理  
			            if ( result.success) {  
			            	
			            	parent.$.messager.alert('提示','对执行清单进行扣款操作处理成功！！');
			            	findMenu()
			            	
			            } else {  
			            	parent.$.messager.alert('提示','对执行清单进行扣款操作处理失败！！');
			            }  
			        }  
			    });
	    }					
	}
	
	$('#win').dialog('close');
    $("#win").hide();
	
}

//取消执行异常操作按钮
function cancelExceptionHandle(){
	$("#detail").textbox('clear');
}



//返回
function goback(){
	window.location.href="/grms/projectAction/gobackProject.html"
}

function handleAlert(){	
	 wrapMaskShow();//父级遮罩显示
	 $('body').css('overflow','hidden');//禁止滚动
	 $('#win').dialog({
	        width:400,
	        modal:true,
	        collapsible:false,
	        minimizable:false,
	        maximizable:false,
	        closable:true,
	        draggable:false,
	        resizable:false,
	        inline:false,
	        title:"异常处理消息提醒",
	        ok:"确定",
	        cancel:"取消",
	        buttons:[{
	            text:'取消',
	            handler:function(){
	                wrapMaskHide();
	                
	                cancelExceptionHandle()
	                $('#win').dialog('close');
	                $("#win").hide();
	            }
	        },
	                 {
	            text:'确认',
	            handler:function(){
	                wrapMaskHide();
	                ensureExceptionHandle()
	            }
	        }],

	        onClose : function(){
	            wrapMaskHide();//父级遮罩隐藏
	            $('body').css('overflow','auto');//恢复滚动
	        }
	    });
	    winHiSelfAdaptation($('#roleWin_detail'));//弹窗定位    
}


//需要隐藏的按钮
var mapButton = {};
var userType;
function initButton(){
	//
	mapButton['goOn'] = 1;
	mapButton['replace'] = 1;
	mapButton['pause'] = 1;
	mapButton['deduction'] = 1;
	mapButton['termination'] = 1;
	var menuUrl="/grms/projectAction/projectActionList.html";
    $.ajax({  
        type : "POST",  //提交方式  
        url : "/grms/initButtonController/getButttonList.html",//路径  
        data:{"menuUrl":menuUrl},
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        success : function(result) {//返回数据根据结果进行相应的处理  
            if ( result.success) {
            	//比较需要隐藏的按钮
            	var buttonObject= result.button;
            	var admin=result.admin;
            	if(admin!="admin"){
            	var temp=1;
            	for(var prop in mapButton){
            		temp=1;
            		for(var j=0;j<buttonObject.length;j++){
            			if(prop==buttonObject[j].resourceCode){
            				temp=0;
            				break;
            				}
            			}
            			if(temp==1){
            				mapButton[prop]=0;
            			}
            		}    
            	if(mapButton['goOn']==0){
            	  $("[name='goOn']").remove();
              	}
              	if(mapButton['replace']==0){
              		$("[name='replace']").remove();
              	}
              	if(mapButton['pause']==0){
              		$("[name='pause']").remove();
              	}
              	if(mapButton['deduction']==0){
              		$("[name='deduction']").remove();
              	}
              	if(mapButton['termination']==0){
              		$("[name='termination']").remove();
              	}
            }else{
            	userType=0;
            }
            } else {  
            	
            }  
        }  
    }); 
    
}


function paginationpage(total,data2){
//  分页
    $('#pp').pagination({
        total:total,
        layout:['list','first','prev','links','next','last','manual'],
        emptyMsg: '<span>无记录</span>',
        showRefresh:true,
        pageSize:pageSizeAll,
        pageNumber:pageNoAll,
        displayMsg:' ',
        pageList:[10,20,30],
        onSelectPage:function (pageNo, pageSize) {
        	pageNoAll=pageNo;
        	pageSizeAll=pageSize;
        	findMenu()
        }
    });
    $(".pagination-page-list").parent().append("条");
    $(".pagination-page-list").parent().prepend("共计"+total+"条,每页显示： ");

}