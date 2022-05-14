
var dataList;
//需要隐藏的按钮
var mapButton = {};
var userType;


$(function(){
	configMenuItem("客户管理","客户列表");
    //search();
    //初始化页面按钮
    initButton();
});

/*******页面按钮*******/
function initButton(){
	//
	mapButton['customerAdd'] = 1;
	mapButton['customerSelect'] = 1;
	mapButton['deleteBatch'] = 1;
	mapButton['export'] = 1;
	mapButton['view'] = 1;
	mapButton['customerEdit'] = 1;
	mapButton['delete'] = 1;
	mapButton['customerTransfer'] = 1;
	var menuUrl="/grms/customerManagement/list.html";
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
            	//if(mapButton['create']==0){
            	 // $("#create").hide();
              	//}
              	//if(mapButton['select']==0){
              	//	$("#select").hide();
              	//}
              	if(mapButton['deleteBatch']==0){
            	  	$("#deleteBatch").hide();
              	}
            }else{
            	userType=0;
            }
              //refreshall();
              search();
            } else {  
            	
            }  
        }  
    }); 
    
}
function initDg(pageNo,pageSize){
	var xL=(pageNo-1)*pageSize;
	var yL=pageNo*pageSize;
	var data1=dataList.slice(xL,yL);
	//	表格数据渲染
	$('#dg').datagrid({
	    border:true,
	    scrollbarSize:0,
	    nowrap:false,//允许换行
	    emptyMsg: '<span>无记录</span>',
	    data:data1,
	    fitColumns:true,//宽度不自适应
	    checkOnSelect:false,//点击该复选框的时候才会选中或取消
	    emptyMsg:'<span>无记录</span>',
	    singleSelect:false,
	    selectOnCkeck:true,
	    ckeckOnSelect:true,
	    onBeforeLoad:function(param){
			$('#dg').datagrid('resize');	
	    },
	    onLoadSuccess:function(data){
	    	setTimeout(function(){
	    		$('#dg').datagrid('resize');	
				var dataHeight =  $(".datagrid-view").height()-19;
		        $(".datagrid-view").css("height",dataHeight );
	    	},0)	
	    },
	    columns:[[
			{
			    field:'ck',
			    title:'',
			    checkbox:true,
			    width:50,
			    align:'center'
			},
	        {
	            field:'createTime',
	            title:'添加时间',
	            width:200,
	            align:'center',
            	formatter : function(value){
                    var date = new Date(value);
                    var y = date.getFullYear();
                    var m = date.getMonth() + 1;
                    var d = date.getDate();
                    var h = date.getHours(); // hour
                    var M = date.getMinutes(); // minute
                    var s = date.getSeconds(); // second
                    return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (h < 10 ? '0' + h : h) + ':' + (M < 10 ? '0' + M : M) + ':' + (s < 10 ? '0' + s : s);
                }
	        },
	        {
	            field:'industry',
	            title:'主要经营',
	            width:200,
	            align:'center',
	        },
	        {
	            field:'customerType',
	            title:'公司类型',
	            width:200,
	            align:'center',
	        },
	        {
	        	field:'name',
	        	title:'公司名称',
	        	width:200,
	        	align:'center',
	        },
	        {
	        	field:'address',
	        	title:'公司地址',
	        	width:200,
	        	align:'center',
	        },
	        {
	        	field:'department',
	        	title:'部门',
	        	width:200,
	        	align:'center',
	        },
	        {
	        	field:'contactor',
	        	title:'主要联系人名称',
	        	width:200,
	        	align:'center',
	        },
	        {
	        	field:'contactorPhone',
	        	title:'主要联系人电话',
	        	width:200,
	        	align:'center',
	        },
	        {
	        	field : 'handle',
				title : '操作',
				width : 400,
				align : 'left',
				formatter : function(value, row, index) {
					var buttonChoice='';
					var detail = '<a href="javascript:;" data-id="1" onclick="detailCust(\'' + row.custId + '\')">详情</a>';
					var edit = '<a href="javascript:;" data-id="2" onclick="editCust(\'' + row.custId + '\')">修改</a>';
					var del = '<a href="javascript:;" data-id="3" onclick="dealDeleteOne(\'' + row.custId + '\')">删除</a>';
					var transfer = '<a href="javascript:;" data-id="4" onclick="transfer(\'' + row.custId + '\')">转移</a>';
	            	
					if(userType!=0){
						if (mapButton['view']==1) {
							buttonChoice = detail;
						}
						if (mapButton['customerEdit']==1) {
							buttonChoice = buttonChoice + edit;
						}
                		if(mapButton['delete']==1){
                			buttonChoice = buttonChoice + del;
                		}
                		if(mapButton['customerTransfer']==1){
                			buttonChoice = buttonChoice + transfer;
                		}
                	}else{
                		buttonChoice=detail + edit + del + transfer;
                	}
                    return buttonChoice;
					//return detail + edit + del + transfer;
	            }
	        }
	    ]]
	});
}

function pagenationPage(){
	//分页
	$('#pp').pagination({
	    total:dataList.length,
	    layout:['list','first','prev','links','next','last','manual'],
	    emptyMsg: '<span>无记录</span>',
	    showRefresh:true,
	    displayMsg:' ',
	    pageList:[10,20,30],
	    //pageSize:10,
	    onSelectPage:function (pageNo, pageSize) {
	    	
	    	initDg(pageNo,pageSize);
	      
	    }
	});
	$(".pagination-page-list").parent().append("条");
    $(".pagination-page-list").parent().prepend("共计"+dataList.length+"条,每页显示： ");
}
function search(){
	var condition = $("#conditionId").val();
 	$.ajax({
 		type : "post",
 		url : "/grms/customerManagement/getAllList.html",
 		dataType : "json",
 		data : {"condition":condition},
	 	success : function(result) {
	 		if (result.succ) {
				dataList = result.data;
				initDg(1,10);
				pagenationPage();
			}else{
				infoMask("列表加载失败:  服务器响应异常")
			}
	 	}
 	});
}

//delete by one
function dealDeleteOne(id){
	$.ajax({
 		type : "post",
 		url : "/grms/customerManagement/transferAndDelete.html",
 		dataType : "json",
 		data : {"id":id},
	 	success : function(result) {
	 		if (result.succ) {
	 			var a = result.flag;
	 			if (a) {
	 				doDelete(id);
				}else{
					infoMask("该客户存在正在执行流程中的项目，不可删除!")
				}
			}else{
				infoMask("获取详情信息失败:  服务器响应异常")
			}
	 	}
 	});
}
function doDelete(id){
	setTimeout(function(){
		$(".messager-button .l-btn").after(" ");
	},0)
	 createConfirm("确认消息","您确认想要删除该选项吗？",true,false,"确定","取消",okCallbakFunciton2,"","","");
	 function okCallbakFunciton2(){
		 $.ajax({
		    	   type : "post",
		    	   url : "/grms/customerManagement/delete.html",
		    	   dataType : "json",
		    	   data : {"ids":id},
		    	   success : function(result) {
		   	 		if (result.succ) {
		   	 			search();
		   			}else{
		   				infoMask("删除失败:  服务器响应异常")
		   			}
		   	 	}
	     });
	  }
}


//delete by batch
function delBatch(){
	var selectRows = $("#dg").datagrid("getSelections");
	if (selectRows.length < 1) {
		setTimeout(function(){
			$(".messager-button .l-btn").after('<span style="height:66px;display:inline-block;vertical-align:middle;"></span>');
		},0)
		
        infoMask("操作失败，无勾选项!")
        return;
    }
	
	 var strIds = "";
     //拼接字符串，这里也可以使用数组，作用一样
     for (var i = 0; i < selectRows.length; i++) {
         strIds += selectRows[i].custId + ",";
     }
     //截尾处理
     strIds = strIds.substr(0, strIds.length - 1);
	
	$.ajax({
 		type : "post",
 		url : "/grms/customerManagement/transferAndDelete.html",
 		dataType : "json",
 		data : {"id":strIds},
	 	success : function(result) {
	 		if (result.succ) {
	 			var a = result.flag;
	 			if (a) {
	 				doDeleteBatch(strIds);
				}else{
					infoMask("部分客户存在正在执行流程中的项目，不可批量删除!")
				}
			}else{
				infoMask("获取详情信息失败:  服务器响应异常")
			}
	 	}
 	});
}
function doDeleteBatch(strIds){
	setTimeout(function(){
		$(".messager-button .l-btn").after(" ");
	},0)
	 createConfirm("确认消息","确认删除所选项吗？",true,false,"确定","取消",okCallbakFunciton,"","","");
	 function okCallbakFunciton(){
		/* var strIds = "";
         //拼接字符串，这里也可以使用数组，作用一样
         for (var i = 0; i < selectRows.length; i++) {
             strIds += selectRows[i].custId + ",";
         }
         //截尾处理
         strIds = strIds.substr(0, strIds.length - 1);*/
         $.ajax({
		    	   type : "post",
		    	   url : "/grms/customerManagement/delete.html",
		    	   dataType : "json",
		    	   data : {"ids":strIds},
		    	   success : function(result) {
		   	 		if (result.succ) {
		   	 			search();
		   			}else{
		   				infoMask("删除失败:  服务器响应异常")
		   			}
		   	 	}
	       });
	  }
}



//save
function saveCustomer(){
	window.location.href = "/grms/customerManagement/toSaveCust.html";
}


//edit or update
function editCust(id){
	window.location.href = "/grms/customerManagement/toEditCust.html?id="+id;
}


//detail
function detailCust(id){
	var detail = "flag";
	window.location.href = "/grms/customerManagement/toCustDetail.html?id="+id+"&flag="+detail;
}

//transfer
function transfer(id){
	window.location.href = "/grms/customerManagement/toCustDetail.html?id="+id;
}

function exportToExcel(){
	 createConfirm("消息确认","导出客户信息需要一定的时间，您确认导出吗？",true,false,"确定","取消",okCallbakFunciton3,"","","");
	 function okCallbakFunciton3(){
		 var condition = $("#conditionId").val();
		 window.location.href= "/grms/customerManagement/exportExcel.html?condition="+condition;
		 search();
	  }
	
	
	/*wrapMaskShow();
	$('#exportComfirm').dialog({
	    title: "消息确认",
	    width: 380,
	    height: 290,
	    draggable:false,
	    closed: false,
	    modal: true,
	    onClose:function(){
	    	wrapMaskHide();
	    },
	    buttons:[{
	        text:'取消',
	        handler:function(){
	            $('#exportComfirm').dialog('close');
	            wrapMaskHide();
	        }
	    },{
	        text:'确认',
	        handler:function(){
			 var condition = $("#conditionId").val();
			 window.location.href= "/grms/customerManagement/exportExcel.html?condition="+condition;
			 $('#exportComfirm').dialog('close');
			 wrapMaskHide();
	       }
	    }]

	});*/
	//search();
}





