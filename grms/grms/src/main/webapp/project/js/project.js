var dataStatus = null;
var pageNoAll=1;
var pageSizeAll=10;
var roleType = null;
var userType = null;
$(function(){		
	configMenuItem("项目管理","项目列表");
	//初始化项目状态选择值
	dataStatus = -2;
	//全部选中
	var statusVal = 1;
	var pendingStatus = $("#pendingStatus").val();
	if(pendingStatus=='1' || pendingStatus=='2' || pendingStatus=='3' 
		|| pendingStatus=='4' || pendingStatus=='5' || pendingStatus=='6'){
		statusVal = pendingStatus;
	}
	
	var pageSize = $("#pageSize").val();
	var pageNumber = $("#pageNumber").val();
	var projectStauts = $("#projectStauts").val();
	var stauts = $("#stauts").val();
	var projectCode = $("#projectCode").val();
	userType = $("#userType").val()
	
	if(projectCode!=null&&projectCode!=""){
		var productCodeEle = $("#productCode")
		if(!productCodeEle.val()){
			productCodeEle.val(projectCode)
		}
	}
	
	if(stauts&&stauts!=""){
		statusVal = stauts*1+2
		if(stauts==-2){
			
			statusVal = 1;
		
		}
		dataStatus = stauts
		
	}
	
	
	if(pageSize!=null&&pageSize!=""){
		pageSizeAll = pageSize
	}
	
	if(pageNumber!=null&&pageNumber!=""){
		pageNoAll = pageNumber
	}
	if(projectStauts&&projectStauts!=""){
		statusVal = projectStauts*1+2
		if(projectStauts==-2){
			
			statusVal = 1;
		
		}
		dataStatus = projectStauts
		
	}
	
	
	$(".workorder-tabs .workorder-tabs"+statusVal).addClass("workorder-select").siblings().removeClass("workorder-select");
	$(".addProject-pop>div").eq(0).addClass("addProject-pop-active").siblings().removeClass("addProject-pop-active");
	
	roleType = $("#roleType").val()
	
	/*$("[name='projectStauts']").on("click", function(){
		
		//选择项目状态值发生改变则重新查询
		if(dataStatus!=$(this).attr("data-status")){
			dataStatus = $(this).attr("data-status")
			console.log(dataStatus)			
		}	
	});*/
	$(".workorder-tabs>div").click(function(){
		if($(this).hasClass("workorder-tabs1")){
			dataStatus=-2;
		}
		if($(this).hasClass("workorder-tabs2")){
			dataStatus=0;
		}
		if($(this).hasClass("workorder-tabs3")){
			dataStatus=1;
		}
		if($(this).hasClass("workorder-tabs4")){
			dataStatus=2;
		}
		if($(this).hasClass("workorder-tabs5")){
			dataStatus=3;
		}
		if($(this).hasClass("workorder-tabs6")){
			dataStatus=4;
		}
		$(this).addClass("workorder-select").siblings().removeClass("workorder-select");
		$(".addProject-pop>div").eq($(this).index()).addClass("addProject-pop-active").siblings().removeClass("addProject-pop-active");
		setTimeout(function(){
			//dataFunc();
		},500);
		if(dataStatus==-2||dataStatus==1||dataStatus==3){
			$("#contractCodeDiv").show();
		}else{
			$("#contractCodeDiv").hide();
		}
		
		pageNoAll = 1;
		
		findMenu();
		
	});
	
	
	$('#createBtn').linkbutton({    
	}); 
	$('#searchBtn').linkbutton({  
		
	});
	
	/*$('#win').window({
		title:"消息提醒",
	    width:400,    
	    height:350,    
	    modal:true,
	    collapsible:false,
	    minimizable:false,
	    maximizable:false
	});*/
	if(dataStatus==-2||dataStatus==1||dataStatus==3){
		$("#contractCodeDiv").show();
	}else{
		$("#contractCodeDiv").hide();
	}
	
	
	
	//$('#win').window('close');
	
	findMenu();
	
	
})
//终止项目
function stopProject(){	
	$('#remark').textbox({
		width:350,
		height:200,
		multiline:true,
		deltaX:10,
		prompt:"请输入立项终止的意见"
	})
	
	$("#remark").textbox("clear");
	
	 wrapMaskShow();//父级遮罩显示
	 $('body').css('overflow','hidden');//禁止滚动	 
	 $('#win').dialog({
	        width:400,
	        height:350,
	        modal:true,
	        collapsible:false,
	        minimizable:false,
	        maximizable:false,
	        closable:true,
	        draggable:false,
	        resizable:false,
	        inline:false,
	        title:"消息提醒",
	        ok:"确定",
	        cancel:"取消",
	        buttons:[{
	            text:'取消',
	            handler:function(){
	            	cancelStopProject();
	            }
	        },{
	            text:'确认',
	            handler:function(){
	                
	                ensureStopProject();
	               
	            }
	        }],

	        onClose : function(){
	            wrapMaskHide();//父级遮罩隐藏
	            $('body').css('overflow','auto');//恢复滚动
	        }
	    });
	    winHiSelfAdaptation($('#roleWin_detail'));//弹窗定位    	    
}

//查找
function findMenu(){
	var productCode=$('#productCode').val();
	var name=$('#name').val();
	var contractCode=$('#contractCode').val();
	var customerName=$('#customerName').val();
	var createrName=$('#createrName').val()?$('#createrName').val():"";
	
	pageNo=pageNoAll;
	pageSize=pageSizeAll;
	var projectQueryBean='{"productCode":"'+productCode+'","name":"'+name+'","contractCode":"'+contractCode
	+'","customerName":"'+customerName+'","status":"'+dataStatus+'","createrName":"'+createrName
	+'","pageNumber":"'+pageNo+'","pageSize":"'+pageSize+'"}';
	
    $.ajax({  
        type : "POST",  //提交方式  
        url : "/grms/project/getlistData.html",//路径  
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        data:  {"projectQueryBean":projectQueryBean},  
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
			{     field:'num',
			    title:'序号',
			    width:80,
			    align:'left',
			    formatter: function(value,row,index){
			    	index=index+(pageNoAll-1)*pageSizeAll
			     	return 	index+1;
			    }
			},
            {
                field:'productCode',
                title:'项目编号',
                width:220,
                align:'center'
            },
            {
                field:'name',
                title:'项目名称',
                width:300,
                align:'center'
                
            },
            {
                field:'createTime',
                title:'创建时间',
                width:200,
                align:'center'
            },
            {
                field:'contractCode',
                title:'合同编号',
                width:200,
                align:'center'
            },
            {
                field:'customerName',
                title:'客户名称',
                width:150,
                align:'center'
            },
            {
                field:'createrName',
                title:'创建人',
                width:150,
                align:'center'
            },
            {
                field:'status',
                title:'状态',
                width:150,
                align:'center',
                formatter: function(value){
                	if(value==0){
                		return "待审核";
                	}else if(value==1){
                		return "审核通过";
                	}else if(value==2){
                		return "审核不通过";
                	}else if(value==3){
                		return "立项终止";
                	}else if(value==4){
                		return "撤回";
                	}
                }
            },
            {
                field:'handle',
                title:'操作',
                width:250,
                align:'center',
                formatter: function(value,row,index){                	
                	//处理操作得按钮（根据用户的类型）
                	var status = row.status
                	
                	var detailWorkOrder = '<a name="detail" href="javascript:;" data-id="1" onclick="projectOperation(1,\''+index+'\')">查看</a>  ';
                	var workOrder = detailWorkOrder;
                	
                	//状态：-1删除  0待审核 1审核通过 2审核不通过 3立项终止 4撤回
                	if(status==0){
                		
                		var revokeWorkOrder = '<a name="revoke" href="javascript:;" data-id="2" onclick="projectOperation(2,\''+index+'\')">撤回</a> ';
                		var examWorkOrder = '<a name="exam" href="javascript:;" data-id="5" onclick="projectOperation(5,\''+index+'\')">审批</a> ';
                		
                		if(roleType==1){
                			workOrder = "";
                		}
                		
                		workOrder = workOrder+revokeWorkOrder+examWorkOrder;
                		
                	}else if(status==1){
                		
                		var stopWorkOrder = '<a href="javascript:;" name="stop" data-id="6" onclick="projectOperation(6,\''+index+'\')">终止</a> ';
                		workOrder = workOrder+stopWorkOrder;
                		
                	}else if(status==2){
                		var editWorkOrder = '<a href="javascript:;" name="edit" data-id="3" onclick="projectOperation(3,\''+index+'\')">编辑</a> ';
                		var deleteWorkOrder = '<a href="javascript:;" name="delete" data-id="4" onclick="projectOperation(4,\''+index+'\')">删除</a> ';
                		
                		if(roleType==1){
                			deleteWorkOrder = "";
                		}
                		console.log(userType)
                		workOrder = workOrder+editWorkOrder+deleteWorkOrder;
                		
                	}else if(status==3){
                			
                		var deleteWorkOrder = '<a href="javascript:;" name="delete" data-id="4" onclick="projectOperation(4,\''+index+'\')">删除</a> ';
                		workOrder = workOrder+deleteWorkOrder;
                	
                	}else if(status==4){
                		var editWorkOrder = '<a href="javascript:;" name="edit" data-id="3" onclick="projectOperation(3,\''+index+'\')">编辑</a> ';
                		var deleteWorkOrder = '<a href="javascript:;" name="delete" data-id="4" onclick="projectOperation(4,\''+index+'\')">删除</a> ';
                		workOrder = workOrder+editWorkOrder+deleteWorkOrder;
                	}
                	
                    return workOrder;
                }
            }
        ]]
    });
}

//需要隐藏的按钮
var mapButton = {};
function initButton(){
	//
	mapButton['delet'] = 1;
	mapButton['edit'] = 1;
	mapButton['stop'] = 1;
	mapButton['exam'] = 1;
	mapButton['revoke'] = 1;
	mapButton['detail'] = 1;
	var menuUrl="/grms/project/list.html";
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
            	
            	
            	if(mapButton['delet']==0){
            	  $("[name='delete']").remove();
              	}
              	if(mapButton['edit']==0){
              		$("[name='edit']").remove();
              	}
              	if(mapButton['stop']==0){
              		$("[name='stop']").remove();
              	}
              	if(mapButton['exam']==0){
              		$("[name='exam']").remove();
              	}
              	if(mapButton['revoke']==0){
              		$("[name='revoke']").remove();
              	}
              	if(mapButton['detail']==0){
              		$("[name='detail']").remove();
              	}
            }else{
            	userType=0;
            }
            } else {  
            	
            }  
        }  
    }); 
    
}
//隐藏没有权限的按钮
function hideBtn(){
	
}

function searchData(){
	pageNoAll=1;
	findMenu();
}

//项目操作
function projectOperation(type,index){
	
	var checkedItem=$('#dg').datagrid('getData').rows[index];
	var productCode = checkedItem.productCode
	
	//操作类型：1、查看 2、撤回 3、编辑 4、删除 5、审核 6、终止
	if(type==1){
		
		window.location.href="/grms/project/viewProject.html?projectCode=" + productCode;
	}else if(type==2){
		//wrapMaskShow();
		
		if(userType!=0&&roleType!=0){
			$.messager.alert('提示','无操作权限！！！',"",function(){
	    		wrapMaskHide();	
	    	});
			return
		}
		
		 createConfirm("操作提示","确定撤回项目？",true,false,"确定","取消",okCallbakFunciton,"","","");
		 function okCallbakFunciton(){
		    	$.ajax({  
			        type : "POST",  //提交方式  
			        url : "/grms/project/revoke.html",//路径  
			        dataType : "json",//数据，这里使用的是Json格式进行传输  
			        data:  {"productCode":productCode},
			        success : function(result) {//返回数据根据结果进行相应的处理  
			            if ( result.success) {
			            	wrapMaskShow();	
			            	$.messager.alert('提示','项目撤回成功！！',"",function(){
			            		wrapMaskHide();	
			            	});
			            	findMenu()
			            }else{
			            	wrapMaskShow();	
			            	$.messager.alert('提示','项目撤回失败！！失败原因：'+result.msg,"",function(){
			            		wrapMaskHide();	
			            	});
			            }
			        }  
			    }); 
		  }			

		
	}else if(type==3){
		
		if(userType!=0&&roleType!=0){
			$.messager.alert('提示','无操作权限！！！',"",function(){
	    		wrapMaskHide();	
	    	});
			return
		}
		
	    window.location.href="../project/addProject.html?type="+1+"&productCode="+productCode;
		
	}else if(type==4){
		
		if(userType!=0&&roleType!=0){
			$.messager.alert('提示','无操作权限！！！',"",function(){
	    		wrapMaskHide();	
	    	});
			return
		}
		
		 createConfirm("操作提示","确定删除项目？",true,false,"确定","取消",okCallbakFunciton2,"","","");
		 function okCallbakFunciton2(){
			 $.ajax({  
			        type : "POST",  //提交方式  
			        url : "/grms/project/delet.html",//路径  
			        dataType : "json",//数据，这里使用的是Json格式进行传输  
			        data:  {"productCode":productCode},  
			        success : function(result) {//返回数据根据结果进行相应的处理  
			            if ( result.success) {
			            	wrapMaskShow();	
			            	$.messager.alert('提示','项目删除成功！！',"",function(){
			            		wrapMaskHide();	
			            	});
			            	findMenu();
			            }else{
			            	wrapMaskShow();	
			            	$.messager.alert('提示','项目删除失败！！失败原因：'+result.msg,"",function(){
			            		wrapMaskHide();	
			            	});
			            }
			        }  
			    }); 
		  }	

		
	}else if(type==5){
		
		if(userType!=0&&roleType!=1){
			$.messager.alert('提示','无操作权限！！！',"",function(){
	    		wrapMaskHide();	
	    	});
			return
		}
		
		window.location.href="/grms/project/viewProject.html?projectCode=" + productCode;
		
	}else if(type==6){
		
		
		if(userType!=0&&roleType!=1){
			$.messager.alert('提示','无操作权限！！！',"",function(){
	    		wrapMaskHide();	
	    	});
			return
		}
		
		//$('#win').window('open');
		stopProject();
		//将要终止项目的id存在隐藏域中
		$("#code").val(productCode);
	}
	
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

//确定终止项目按钮
function ensureStopProject(){
	
	//获取要终止项目的id(隐藏域中)
	var productCode=$("#code").val();
	
	var remark=$("#remark").textbox('getValue');
	
	wrapMaskHide();		
 	$('#win').dialog('close');
	
	//撤回项目异步发送请求
	$.ajax({  
        type : "POST",  //提交方式  
        url : "/grms/project/stopProject.html",//路径  
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        data:  {"productCode":productCode,"remark":remark},  
        success : function(result) {//返回数据根据结果进行相应的处理  
            if ( result.success) {
            	wrapMaskShow();	
            	$.messager.alert('提示','立项终止成功！！',"",function(){
            		wrapMaskHide();	
            	});
            	findMenu();
            }else{
            	wrapMaskShow();	
            	$.messager.alert('提示','立项终止失败！！失败原因：'+result.msg,"",function(){
            		wrapMaskHide();	
            	});
            }
        }  
    }); 
	
}

//取消终止项目按钮
function cancelStopProject(){
	$("#detail").textbox('clear');
	wrapMaskHide();		
 	$('#win').dialog('close');
	//$('#win').window('close');
}

function creatProject(){
    window.location.href="../project/addProject.html?type="+0;
}

