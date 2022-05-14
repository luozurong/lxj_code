//	表格数据渲染
var data2 ;

var systemId="1";
//需要隐藏的按钮
var mapButton = {};
var userType;
var pageNoAll='1';
var pageSizeAll='10';
var contractType = [{"id":'',"text":"全部"},{"id":1,"text":"执行合同"},{"id":2,"text":"框架合同"}];
var contractStatus=[{"id":'',"text":"全部"},{"id":1,"text":"未提交"},{"id":2,"text":"待审批-业务管理"},
                    {"id":3,"text":"待审批-合同管理"},{"id":4,"text":"待审批-财务管理"},{"id":5,"text":"审批通过"},
                    {"id":6,"text":"业务-审批不通过"}, {"id":8,"text":"合同-审批不通过"}, {"id":9,"text":"财务-审批不通过"},{"id":7,"text":"已完成"}];
var roleType="1";
//临时附件列表
var atttempList=new Array();
$(function(){
    configMenuItem("合同管理","合同管理");
    initButton();
    //格式化时间
    $(".datebox-1").datebox({
        value : '',
        editable : false,
        onSelect : function(beginDate){
            $('.datebox-2').datebox('calendar').calendar({
                validator: function(date){
                    return beginDate<=date;//<=
                }
            });
        }
    });
    //格式化时间
    $(".datebox-2").datebox({
        value : '',
        editable : false,
        onSelect : function(endDate){
            $('.datebox-1').datebox('calendar').calendar({
                validator: function(date){
                    return endDate>=date;//<=
                }
            });
        }
    });
    $('#contractType').combobox({
	       limitToList:true,
	       value : '',
	       data:contractType,
	       valueField:'id',
	       textField:'text',
	       editable : false,
	       panelHeight:"auto"
	   });
    $('#contractStatus').combobox({
	       limitToList:true,
	       value : '',
	       data:contractStatus,
	       valueField:'id',
	       textField:'text',
	       editable : false,
	       panelHeight:"auto"
	   });
    var args=GetUrlParms();
    var contractCode='';
    if (args["contractCode"] != undefined){
    		contractCode=args["contractCode"];
    		findDepart('','',contractCode);
    	

    }else{
        findDepart('','');

    }
    $("#uploadFile").click(function(){
     	 upload();
     });
});
////获取URL所有参数
function GetUrlParms() {
    var args = new Object();
    var query = location.search.substring(1); //获取查询串   
    var pairs = query.split("&"); //在逗号处断开   
    for (var i = 0; i < pairs.length; i++) {
        var pos = pairs[i].indexOf('='); //查找name=value   
        if (pos == -1) continue; //如果没有找到就跳过   
        var argname = pairs[i].substring(0, pos); //提取name   
        var value = pairs[i].substring(pos + 1); //提取value   
        args[argname] = unescape(value); //存为属性   
    }
    return args;
}
function initButton(){
	//
	mapButton['contractSelect'] = 1;
	mapButton['contractNew'] = 1;
	mapButton['contractAppDetail'] = 1;
	mapButton['contractEdit'] = 1;
	mapButton['contractUpload'] = 1;
	mapButton['contractDelUpload'] = 1;
	mapButton['contractCommit'] = 1;
	mapButton['contractDel'] = 1;
	mapButton['contractCancel'] = 1;
	mapButton['contractApprove'] = 1;
	mapButton['contractApproveNo'] = 1;
	mapButton['contractEdit'] = 1;
	mapButton['contractFinish'] = 1;
	var menuUrl="/grms/contract/contract.html";
    $.ajax({  
        type : "POST",  //提交方式  
        url : "/ums/initButtonAction!initMenuButton.html",//路径  
        data:{"menuUrl":menuUrl},
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        success : function(result) {//返回数据根据结果进行相应的处理  
            if ( result.success) {
            	
            	//比较需要隐藏的按钮
            	var buttonObject= result.obj;
            	var admin=result.msg;
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
            	if(mapButton['contractSelect']==0){
            	  $("#contractSelect").hide();
              	}
              	if(mapButton['contractNew']==0){
              		$("#contractNew").hide();
              	}
             
            }else{
            	userType=0;
            }
            } else {  
            	
            }  
        }  
    }); 
  
}

//查询
function findDepart(pageSize,pageNo,contractCode){
	var createTimeBegin= $(".datebox-1").datebox('getValue');
	var createTimeEnd=$(".datebox-2").datebox('getValue');
	var contractOrAccount=$('#contractOrAccount').val();
	var contractType=$('#contractType').val();
	var contractStatus=$('#contractStatus').val();

	pageSize=pageSizeAll;
	pageNo=pageNoAll;
	var data=new Object();
	data.startTime=createTimeBegin;
	data.endTime=createTimeEnd;
	data.queryName=contractOrAccount;
	data.pageSize=pageSize;
	data.pageNumber=pageNo;
	data.contractType=contractType;
	data.contractStatus=contractStatus;
	if(contractCode!=''&&contractCode!=undefined&&contractCode!=null){
		data.contractCode=contractCode;
	}
    $.ajax({  
        type : "POST",  //提交方式  
        contentType : "application/json;charset=utf-8", 
        url : "/grms/contract/getContractListData",//路径  
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        data:  JSON.stringify(data),  
        success : function(result) {//返回数据根据结果进行相应的处理  
            if ( result.success) {  
            	var data2=result.obj.list;
            	var total=result.obj.total;
            	roleType=result.msg;
            	refresh(data2);
            	paginationpage(total,data2);
            } else {  
            	
            }  
        }  
    }); 
	
}


function refresh(data2){
//	表格数据渲染
    $('#dg').datagrid({
        border:true,
        scrollbarSize:0,
        nowrap:false,//允许换行
        selectOnCheck:false,
        data:data2,
/*        rownumbers:true,
*/        emptyMsg:'<span>无记录</span>',
        fitColumns:true,//宽度不自适应
        columns:[[
           {     field:'num',
                title:'序号',
                width:150,
                align:'left',
                formatter: function(value,row,index){
                	index=index+(pageNoAll-1)*pageSizeAll
                 	return 	index+1;
                }
            },
            {
                field:'id',
                title:'合同id',
                width:220,
                align:'left',
                hidden:'true'
            },
            {
                field:'customerResource',
                title:'客户资源系数',
                width:220,
                align:'left',
                hidden:'true'
            },
            {
                field:'projectName',
                title:'项目名称',
                width:150,
                align:'left',
                formatter:function(value,row,index){
                   return '<a href="/grms/project/viewProject.html?projectCode='+row.projectCode+' " >' + value + '</a>';

                }
            },
            {
                field:'contractCode',
                title:'合同ID',
                width:150,
                align:'left',
                hidden:'true'  
            },
            {
                field:'contractType',
                title:'合同类型',
                width:150,
                align:'left',
                formatter: function(value){
                	//合同类型（1：执行合同 2：框架合同）',
                	if(value==1){
                		return "执行合同 ";
                	}else if(value==2){
                		return "框架合同";
                	}
                
                }
               
            },
            {
                field:'contractName',
                title:'合同名称(点击查看详情)',
                width:280,
                align:'left',  
                formatter: function(value,row,index){
                		return '<a href="javascript:;"  onclick="contractWinDetail(\''+row.contractCode+'\',\''+row.contractType+'\',\''+row.contractName+'\',\''+row.fromContractName+'\',\''+row.fromContractCode+'\',\''+row.customerResource+'\')">' + value + '</a>';
                 }
            },
            {
                field:'fromContractCode',
                title:'是否关联框架合同',
                width:220,
                align:'left',
                formatter: function(value,row,index){
                	if(row.fromContractCode==''||row.fromContractCode==null){
                		return "否";
                	}else{
                		return "是";
                	}
                }
            },
            
            {
                field:'fromContractName',
                title:'关联框架合同名称',
                width:220,
                align:'left',
                hidden:'true'
            },
            
            {
                field:'attachmentDetailExtra',
                title:'合同附件（已完成后上传）',
                width:220,
                align:'left',
                hidden:'true'
            },
            {
                field:'attachments',
                title:'合同附件(点击可下载)',
                width:280,
                align:'center',
                formatter: function(value,row,index){
                	var allUrl='';
                	if(value!=null&&value!="null"){
                	var urlArray=value;
                	for(var i=0;i<urlArray.length;i++){
                		if(i==0){
                			allUrl = '<div href="javascript:;" style="color: #51d2b6" onclick="download2(\''+urlArray[i].fileUrl+'\',\''+urlArray[i].fileName+'\')">' + 
                			urlArray[i].fileName+ '</div>';
                		}else{
                			allUrl=allUrl+'<div href="javascript:;" style="color: #51d2b6" onclick="download2(\''+urlArray[i].fileUrl+'\',\''+urlArray[i].fileName+'\')">' + 
                			urlArray[i].fileName+ '</div>';
                		}
                	}
                	}
                 	return allUrl;	
                }
            },
            {
                field:'money',
                title:'合同金额',
                width:150,
                align:'left'
            },
            {
                field:'createrName',
                title:'业务员',
                width:150,
                align:'left'
            },
            {
                field:'createrTime',
                title:'创建时间',
                width:150,
                align:'left'
            },
            {
                field:'status',
                title:'合同状态',
                width:150,
                align:'left',
                formatter: function(value){
                	//'合同状态（1：未提交 2：待审批-业务管理 3:待审批-合同管理 4:待审批-财务管理 5:审批通过 6:审批不通过 7:已完成 
                	if(value==1){
                		return "未提交";
                	}else if(value==2){
                		return "待审批-业务管理";
                	}else if(value==3){
                		return "待审批-合同管理";
                	}else if(value==4){
                		return "待审批-财务管理";
                	}else if(value==5){
                		return "审批通过";
                	}else if(value==6){
                		return "业务-审批不通过";
                	}else if(value==7){
                		return "已完成";
                	}
                	else if(value==8){
                		return "合同-审批不通过";
                	}
                	else if(value==9){
                		return "财务-审批不通过";
                	}
                }
            },
            {
                field:'approve',
                title:'审批意见',
                width:150,
                align:'left',  
                formatter: function(value,row,index){
                	if(row.status>1){
                		return '<a href="javascript:;"  onclick="approveDetail(\''+row.contractCode+'\')">查看详情</a>';
                	}
                	
                }
            },
            {
                field:'handle',
                title:'操作',
                width:574,
                align:'left',
                formatter: function(value,row,index){
                	var buttonChoice='';
                	if(row.status==1){
                		var contractEdit = '<a href="javascript:;"  onclick="editContract(\''+row.id+'\')">编辑</a>';
                		var contractUpload = '<a href="javascript:;"  onclick="upLoadFilesWin(\''+row.id+'\',\''+row.status+'\')">上传附件</a>';
                		var contractDelUpload = '<a href="javascript:;"  onclick="upLoadFilesWinDel(\''+row.id+'\',\''+row.status+'\')">删除附件</a>';
                		var contractCommit = '<a href="javascript:;"  onclick="commitContract(\''+row.id+'\')">提交</a>';
                		var contractDel = '<a href="javascript:;"  onclick="checkDel(\''+row.id+'\')">删除合同</a>';
                		if(userType!=0){
                    		if(mapButton['contractEdit']==1){
                    			buttonChoice=contractEdit;
                    		}
                    		if(mapButton['contractUpload']==1){
                    			buttonChoice=buttonChoice+contractUpload;
                    		}
                    		if(mapButton['contractDelUpload']==1){
                    			buttonChoice=buttonChoice+contractDelUpload
                    		}
                    		if(mapButton['contractCommit']==1){
                    			buttonChoice=buttonChoice+contractCommit
                    		}
                    		if(mapButton['contractDel']==1){
                    			buttonChoice=buttonChoice+contractDel
                    		}
                    	}else{
                    		buttonChoice=contractEdit+contractUpload+contractDelUpload+contractCommit+contractDel;
                    	}
                		
                	}
                	if(row.status==2){
                		var contractCancel = '<a href="javascript:;"  onclick="revoke(\''+row.id+'\')">撤回</a>';
                		var contractApprove = '<a href="javascript:;"  onclick="approve(\''+row.id+'\')">审批通过</a>';
                		var contractApproveNo = '<a href="javascript:;"  onclick="approve2(\''+row.id+'\')">审批不通过</a>';
                		
                		if(userType!=0){
                    		if(mapButton['contractCancel']==1&&roleType=='0'){
                    			buttonChoice=contractCancel;
                    		}
                    		if(mapButton['contractApprove']==1&&roleType=='1'){
                    			buttonChoice=buttonChoice+contractApprove;
                    		}
                    		if(mapButton['contractApproveNo']==1&&roleType=='1'){
                    			buttonChoice=buttonChoice+contractApproveNo
                    		}
                    		
                    	}else{
                    		buttonChoice=contractCancel+contractApprove+contractApproveNo;
                    	}
                		
                	}
                 	if(row.status==3){
                		var contractCancel = '<a href="javascript:;"  onclick="revoke(\''+row.id+'\')">撤回</a>';
                		var contractApprove = '<a href="javascript:;"  onclick="approve(\''+row.id+'\')">审批通过</a>';
                		var contractApproveNo = '<a href="javascript:;"  onclick="approve2(\''+row.id+'\')">审批不通过</a>';
                		
                		if(userType!=0){
                    		if(mapButton['contractCancel']==1&&roleType=='1'){
                    			buttonChoice=contractCancel;
                    		}
                    		if(mapButton['contractApprove']==1&&roleType=='2'){
                    			buttonChoice=buttonChoice+contractApprove;
                    		}
                    		if(mapButton['contractApproveNo']==1&&roleType=='2'){
                    			buttonChoice=buttonChoice+contractApproveNo
                    		}
                    		
                    	}else{
                    		buttonChoice=contractCancel+contractApprove+contractApproveNo;
                    	}
                		
                	}
                	if(row.status==4){
                		var contractCancel = '<a href="javascript:;"  onclick="revoke(\''+row.id+'\')">撤回</a>';
                		var contractApprove = '<a href="javascript:;"  onclick="approve(\''+row.id+'\')">审批通过</a>';
                		var contractApproveNo = '<a href="javascript:;"  onclick="approve2(\''+row.id+'\')">审批不通过</a>';
                		
                		if(userType!=0){
                    		if(mapButton['contractCancel']==1&&roleType=='2'){
                    			buttonChoice=contractCancel;
                    		}
                    		if(mapButton['contractApprove']==1&&roleType=='3'){
                    			buttonChoice=buttonChoice+contractApprove;
                    		}
                    		if(mapButton['contractApproveNo']==1&&roleType=='3'){
                    			buttonChoice=buttonChoice+contractApproveNo
                    		}
                    		
                    	}else{
                    		buttonChoice=contractCancel+contractApprove+contractApproveNo;
                    	}
                		
                	}
                	if(row.status==5){
                		var contractCancel = '<a href="javascript:;"  onclick="revoke(\''+row.id+'\')">撤回</a>';
                		var contractUpload = '<a href="javascript:;"  onclick="upLoadFilesWin(\''+row.id+'\',\''+row.status+'\')">上传附件</a>';
                		var contractDelUpload = '<a href="javascript:;"  onclick="upLoadFilesWinDel(\''+row.id+'\',\''+row.status+'\')">删除附件</a>';
                		var contractEdit = '<a href="javascript:;"  onclick="editContract(\''+row.id+'\')">修改合同</a>';
                		var contractFinish = '<a href="javascript:;"  onclick="finishContract(\''+row.id+'\')">完成</a>';
                		if(userType!=0){
                    		if(mapButton['contractCancel']==1&&roleType=='3'){
                    			buttonChoice=contractCancel;
                    		}
                    		if(mapButton['contractUpload']==1&&roleType=='0'){
                    			buttonChoice=buttonChoice+contractUpload;
                    		}
                    		if(mapButton['contractDelUpload']==1&&roleType=='0'){
                    			buttonChoice=buttonChoice+contractDelUpload;
                    		}
                    		if(mapButton['contractEdit']==1&&roleType=='0'){
                    			buttonChoice=buttonChoice+contractEdit
                    		}
                    		if(mapButton['contractFinish']==1&&roleType=='0'){
                    			buttonChoice=buttonChoice+contractFinish
                    		}
                    	}else{
                    		buttonChoice=contractCancel+contractUpload+contractDelUpload+contractEdit+contractFinish;
                    	}
                	} 
                	if(row.status==6){
                		var contractEdit = '<a href="javascript:;"  onclick="editContract(\''+row.id+'\')">修改合同</a>';
                		var contractDel = '<a href="javascript:;"  onclick="checkDel(\''+row.id+'\')">删除合同</a>';
                		var contractCancel = '<a href="javascript:;"  onclick="revoke(\''+row.id+'\')">撤回</a>';

                		if(userType!=0){
                    		if(mapButton['contractEdit']==1&&roleType=='0'){
                    			buttonChoice=contractEdit;
                    		}
                    		if(mapButton['contractDel']==1&&roleType=='0'){
                    			buttonChoice=buttonChoice+contractDel;
                    		}  
                    		if(mapButton['contractCancel']==1&&roleType=='1'){
                    			buttonChoice=buttonChoice+contractCancel;
                    		} 
                    	}else{
                    		buttonChoice=contractEdit+contractDel;
                    	}
                	} 
                   	if(row.status==8){
                		var contractEdit = '<a href="javascript:;"  onclick="editContract(\''+row.id+'\')">修改合同</a>';
                		var contractDel = '<a href="javascript:;"  onclick="checkDel(\''+row.id+'\')">删除合同</a>';
                		var contractCancel = '<a href="javascript:;"  onclick="revoke(\''+row.id+'\')">撤回</a>';

                		if(userType!=0){
                    		if(mapButton['contractEdit']==1&&roleType=='0'){
                    			buttonChoice=contractEdit;
                    		}
                    		if(mapButton['contractDel']==1&&roleType=='0'){
                    			buttonChoice=buttonChoice+contractDel;
                    		}  
                    		if(mapButton['contractCancel']==1&&roleType=='2'){
                    			buttonChoice=buttonChoice+contractCancel;
                    		} 
                    	}else{
                    		buttonChoice=contractEdit+contractDel;
                    	}
                	} 
                   	if(row.status==9){
                		var contractEdit = '<a href="javascript:;"  onclick="editContract(\''+row.id+'\')">修改合同</a>';
                		var contractDel = '<a href="javascript:;"  onclick="checkDel(\''+row.id+'\')">删除合同</a>';
                		var contractCancel = '<a href="javascript:;"  onclick="revoke(\''+row.id+'\')">撤回</a>';

                		if(userType!=0){
                    		if(mapButton['contractEdit']==1&&roleType=='0'){
                    			buttonChoice=contractEdit;
                    		}
                    		if(mapButton['contractDel']==1&&roleType=='0'){
                    			buttonChoice=buttonChoice+contractDel;
                    		}  
                    		if(mapButton['contractCancel']==1&&roleType=='3'){
                    			buttonChoice=buttonChoice+contractCancel;
                    		} 
                    	}else{
                    		buttonChoice=contractEdit+contractDel;
                    	}
                	} 
                	if(row.status==7){
                		var contractUpload = '<a href="javascript:;"  onclick="upLoadFilesWin(\''+row.id+'\',\''+row.status+'\')">上传附件</a>';
                		var contractDelUpload = '<a href="javascript:;"  onclick="upLoadFilesWinDel(\''+row.id+'\',\''+row.status+'\')">删除附件</a>';
                		
                		if(userType!=0){
                    		if(mapButton['contractUpload']==1&&roleType=='0'){
                    			buttonChoice=contractUpload;
                    		}
                    		if(mapButton['contractDelUpload']==1&&roleType=='0'){
                    			buttonChoice=buttonChoice+contractDelUpload;
                    		}         		
                    	}else{
                    		buttonChoice=contractUpload+contractDelUpload;
                    	}
                	} 
                    return buttonChoice;
                }
            },

        ]],
        onLoadSuccess: function (data) {
            //去掉表格底部多出的19px
            $('#dg').datagrid('resize');
            var dataHeight =  $(".datagrid-view").height()-19;
            $(".datagrid-view").css("height",dataHeight );
        }
    });
// 

}
function paginationpage(total,data2){
    //分页
    $('#pp').pagination({
        total:total,
        layout:['list','first','prev','links','next','last','manual'],
        emptyMsg: '<span>无记录</span>',
        showRefresh:true,
        displayMsg:' ',
        pageList:[10,20,30],
        onSelectPage:function (pageNo, pageSize) {
        	pageNoAll=pageNo;
        	pageSizeAll=pageSize;
            findDepart('','','') ;   
           
        }
    });
    $(".pagination-page-list").parent().append("条");
    $(".pagination-page-list").parent().prepend("共计"+total+"条,每页显示： ");
}
/*点击创建*/
function approve(id){
	wrapMaskShow();//父级遮罩显示
	 $('body').css('overflow','hidden');//禁止滚动
	 
	 $('#roleWin').dialog({
	        width:580,
	        height:305,
	        modal:true,
	        collapsible:false,
	        minimizable:false,
	        maximizable:false,
	        closable:true,
	        draggable:false,
	        resizable:false,
	        inline:false,
	        title:"审批通过",
	        ok:"取消",
	        cancel:"确定",
	        buttons:[{
	            text:'取消',
	            handler:function(){
	                wrapMaskHide();	                
	                $('#roleWin').dialog('close');
	            }
	        },{
	            text:'确认',
	            handler:function(){
	                wrapMaskHide();
	                var desc=$('#approveY').val();
	                if(desc==''||desc==null){
	             	   warnMask('请填写审批意见');  
                       return;
	                }
	            	approveContract(id,desc,'y');
	            	$('#roleWin').dialog('close');
	            }
	        }],
	        onClose : function(){
	            wrapMaskHide();//父级遮罩隐藏
	            $('body').css('overflow','auto');//恢复滚动
	        }
	    });
	    winHiSelfAdaptation($('#roleWin_detail'));//弹窗定位
	

}

/*点击创建*/
function approve2(id){
	wrapMaskShow();//父级遮罩显示
	 $('body').css('overflow','hidden');//禁止滚动
	 
	 $('#roleWin2').dialog({
	        width:580,
	        height:305,
	        modal:true,
	        collapsible:false,
	        minimizable:false,
	        maximizable:false,
	        closable:true,
	        draggable:false,
	        resizable:false,
	        inline:false,
	        title:"审批不通过",
	        ok:"取消",
	        cancel:"确定",
	        buttons:[{
	            text:'取消',
	            handler:function(){
	                wrapMaskHide();	                
	                $('#roleWin2').dialog('close');
	            }
	        },{
	            text:'确认',
	            handler:function(){
	                wrapMaskHide();
	                var desc=$('#approveN').val();
	                if(desc==''||desc==null){
		             	   warnMask('请填写审批意见');  
	                       return;
		                }
	            	approveContract(id,desc,'n');
	            	$('#roleWin2').dialog('close');
	            }
	        }],
	        onClose : function(){
	            wrapMaskHide();//父级遮罩隐藏
	            $('body').css('overflow','auto');//恢复滚动
	        }
	    });
	    winHiSelfAdaptation($('#roleWin_detail'));//弹窗定位
	
	

}
/*审批已经*/
function approveDetail(contracCode){
	  $("#approveDetail").empty();
	 var type=initApproveDetail(contracCode);

	 wrapMaskShow();
	 $('#roleWin4').dialog({
	        width:580,
	        height:550,
	        modal:true,
	        collapsible:false,
	        minimizable:false,
	        maximizable:false,
	        closable:true,
	        draggable:false,
	        resizable:false,
	        inline:false,
	        title:"审批意见详情",
	        ok:"确定",
	        //cancel:"取消",
	        buttons:[{
	            text:'确定',
	            handler:function(){
	                wrapMaskHide();
	                //dataList(name);
	                $('#roleWin4').dialog('close');
	            }
	        
	        }],
	        onClose : function(){
	            wrapMaskHide();//父级遮罩隐藏
	            $('body').css('overflow','auto');//恢复滚动
	        }
	    });
	 winHiSelfAdaptation($('#roleWin4'));//弹窗定位

}

function download2(url,fileName) {
		//下载附件
	var urlall="/grms/attachment/downloadUrl?url="+url+"&fileName="+fileName;
	window.open(urlall,'_blank');

	}  

function initApproveDetail(contractCode){
    $.ajax({  
        type : "GET",  //提交方式  
        url : "/grms/contract/approveContractLog",//路径  
        data: {"contractCode":contractCode} ,  
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        success : function(result) {//返回数据根据结果进行相应的处理  
            if (result.success) {  
            	var apporveDetail=result.obj;
            	var temp="";
            	if(apporveDetail.length==0){
            	    
            		temp='<div>没有审批记录</div>';
            	}
            	for(var i=0;i<apporveDetail.length;i++){
            		var end;
            		if(apporveDetail[i].approveStatus==2){
            			end="业务-审批通过"
            		}else if(apporveDetail[i].approveStatus==3){
            			end="合同-审批通过"
            		}else if(apporveDetail[i].approveStatus==4){
            			end="财务-审批通过"
            		}else if(apporveDetail[i].approveStatus==6){
            			end="业务-审批不通过"
            		}else if(apporveDetail[i].approveStatus==8){
            			end="合同-审批不通过"
            		}else if(apporveDetail[i].approveStatus==9){
            			end="财务-审批不通过"
            		}
            		temp=temp+'<div>审批角色：'+ apporveDetail[i].approveRoleName +'</div><div> 审批人员：'+ apporveDetail[i].approveName +'</div>'
            		+'<div>审批操作：'+ end +'</div><div> 审批时间：'+ apporveDetail[i].approveTime +'</div>'
            		+ '<div>审批意见：'+ apporveDetail[i].approveDesc +'</div></br>';
            		if(i<apporveDetail.length-1){
            			temp=temp+"";
            		}
            	}
            	$("#approveDetail").append(temp); 
           } else {
        	   warnMask('加载详情错误');  
        	   return 1;
            }  
        }  
    }); 
	
}
function approveContract(id,desc,status){
    $.ajax({  
        type : "POST",  //提交方式  
        url : "/grms/contract/approveContract",//路径  
        data: {"id":id,"desc":desc,"status":status} ,  
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        success : function(result) {//返回数据根据结果进行相应的处理  
            if (result.success) {  
            	if(status=='y'){
                    $('#roleWin').window('close');
                    $('#approveY').val('');
            	}else{
                    $('#roleWin2').window('close');
                    $('#approveN').val('');
            	}
         	    infoMask('审批成功');  
         	    findDepart('','');
           } else {
        	   warnMask('审批错误');  
        	   return 1;
            }  
        }  
    }); 
	
}

function newContract(){
    $.ajax({
        type : "GET",  //提交方式
        url : "/grms/contract/clearSession",//路径
        async: false,
        dataType : "json",//数据，这里使用的是Json格式进行传输
        success : function(result) {//返回数据根据结果进行相应的处理
            if ( result.success) {
            } else {

            }
        }
    });
    window.location.href="/grms/contract/contractEdit.html?status=new";

}

function editContract(id){
    window.location.href="/grms/contract/contractEdit.html?status=edit&id="+id;

}

function checkDel(id){
    createConfirm("消息提醒","确定删除该合同？",true,false,"删除","取消",okCallbakFunciton,"",id,"");
    function okCallbakFunciton(){
        $.ajax({
            type : "GET",  //提交方式
            url : "/grms/contract/delContract",//路径
            async: false,
            data:  {"id":id},
            dataType : "json",//数据，这里使用的是Json格式进行传输
            success : function(result) {//返回数据根据结果进行相应的处理
                if ( result.success) {
                        infoMask("删除成功");
                        findDepart('','');
                } else {

                }
            }
        });

    }	
}

function revoke(id){
	var status="c";
	var desc="";
    createConfirm("消息提醒","确定撤回操作？",true,false,"确定","取消",okCallbakFunciton,"",id,"");
    function okCallbakFunciton(){
        $.ajax({
            type : "POST",  //提交方式
            url : "/grms/contract/approveContract",//路径
            async: false,
            data:  {"id":id,"desc":desc,"status":status} ,
            dataType : "json",//数据，这里使用的是Json格式进行传输
            success : function(result) {//返回数据根据结果进行相应的处理
                if ( result.success) {
                        infoMask("撤回成功");
                        findDepart('','');
                } else {
                    infoMask("撤回错误");

                }
            }
        });

    }	
}
function commitContract(id){
    createConfirm("消息提醒","确定提交审核？",true,false,"提交","取消",okCallbakFunciton,"",id,"");
    function okCallbakFunciton(){
        $.ajax({
            type : "GET",  //提交方式
            url : "/grms/contract/commitContract",//路径
            async: false,
            data:  {"id":id},
            dataType : "json",//数据，这里使用的是Json格式进行传输
            success : function(result) {//返回数据根据结果进行相应的处理
                if ( result.success) {
                        infoMask("提交成功");
                        findDepart('','');
                } else {
                    infoMask("提交错误");

                }
            }
        });

    }	
}
function finishContract(id){
    createConfirm("消息提醒","确定该合同已完成？",true,false,"确定","取消",okCallbakFunciton,"",id,"");
    function okCallbakFunciton(){
        $.ajax({
            type : "GET",  //提交方式
            url : "/grms/contract/commitContract",//路径
            async: false,
            data:  {"id":id},
            dataType : "json",//数据，这里使用的是Json格式进行传输
            success : function(result) {//返回数据根据结果进行相应的处理
                if ( result.success) {
                        infoMask("提交成功");
                        findDepart('','');
                } else {
                	if(result.msg!=''){
                        infoMask("项目已经终止，合同不能修改已完成状态！");
                	}else{
                        infoMask("提交错误");

                	}

                }
            }
        });

    }	
}
/*合同详情*/
function contractWinDetail(id,contractType,contractName,fromContractName,code,customerResource){
    $("#contractDetail").empty();
    contractDetail(id,contractType,contractName,fromContractName,code,customerResource)

	 wrapMaskShow();
	if(contractType=='2'){
		$('#roleWin6').dialog({
	        width:580,
	        height:300,
	        modal:true,
	        collapsible:false,
	        minimizable:false,
	        maximizable:false,
	        closable:true,
	        draggable:false,
	        resizable:false,
	        inline:false,
	        title:"框架类合同详情",
	        ok:"确定",
	        //cancel:"取消",
	        buttons:[{
	            text:'确定',
	            handler:function(){
	                wrapMaskHide();
	                //dataList(name);
	                $('#roleWin6').dialog('close');
	            }
	        
	        }],
	        onClose : function(){
	            wrapMaskHide();//父级遮罩隐藏
	            $('body').css('overflow','auto');//恢复滚动
	        }
	    });		
	}else{
			 $('#roleWin6').dialog({
			        width:580,
			        height:300,
			        modal:true,
			        collapsible:false,
			        minimizable:false,
			        maximizable:false,
			        closable:true,
			        draggable:false,
			        resizable:false,
			        inline:false,
			        title:"执行类合同详情",
			        ok:"确定",
			        //cancel:"取消",
			        buttons:[{
			            text:'确定',
			            handler:function(){
			                wrapMaskHide();
			                //dataList(name);
			                $('#roleWin6').dialog('close');
			            }
			        
			        }],
			        onClose : function(){
			            wrapMaskHide();//父级遮罩隐藏
			            $('body').css('overflow','auto');//恢复滚动
			        }
			    });			
	}			

}
function contractDetail(id,contractType,contractName,fromContractName,code,customerResource){
	var temp="";
	var fromContractMoney;
	if(customerResource==''||customerResource=="null"){
		customerResource="无";
	}
	if(fromContractName==''||fromContractName=="null"){
		fromContractName="无";
		fromContractMoney="无";
	}
	if(contractType=="1"){
		if(fromContractName!="无"){
		   $.ajax({
               type : "GET",  //提交方式
               url : "/grms/contract/getContractByCode",//路径
               async: false,
               data:  {"code":code},
               dataType : "json",//数据，这里使用的是Json格式进行传输
               success : function(result) {//返回数据根据结果进行相应的处理
                   if ( result.success) {
                		temp=temp+'<div>执行合同名称：'+contractName +'</div>'
                		+'<div>关联框架合同名称：'+ fromContractName +'</div>'
                		+ '<div>关联框架合同金额：'+ result.obj.money+'</div>'
                		+ '<div>客户资源系数：'+ customerResource+'</div>';
                		$("#contractDetail").append(temp); 

                   } else {

                   }
               }
           });
		}else{
			temp=temp+'<div>执行合同名称：'+contractName +'</div>'
    		+'<div>关联框架合同名称：'+ fromContractName +'</div>'
    		+ '<div>关联框架合同金额：'+ fromContractMoney+'</div>'
    		+ '<div>客户资源系数：'+ customerResource+'</div>';
    		$("#contractDetail").append(temp); 
		}

	}else{
		//查找出框架合同的其他执行合同
		 $.ajax({
	            type : "GET",  //提交方式
	            url : "/grms/contract/getContractOther",//路径
	            async: false,
	            data:  {"id":id},
	            dataType : "json",//数据，这里使用的是Json格式进行传输
	            success : function(result) {//返回数据根据结果进行相应的处理
	                if ( result.success) {
	                	var contract=result.obj;
	            		temp=temp+'<div>框架合同名称：'+contractName +'</div>'
	            			+ '<div>客户资源系数：'+ customerResource+'</div>';
	            		
	            		var table='<table  border="1">'
					      +'<tr> <td>关联执行合同名称    </td><td>关联执行合同金额</td></tr>';
	                	for(var i=0;i<contract.length;i++){
	                		table=table+'<tr> <td>'+contract[i].contractName +'</td><td>  '+contract[i].money +'</td></tr>'
	                	}
	                     table=table+'</table>';
	            		$("#contractDetail").append(temp+table); 
	                } else {
	                    infoMask("查看错误");

	                }
	            }
	        });
	}
}

//上传附件

/**
 * 页面单按钮上传
 */
function upload(){
	var formData = new FormData($("#upfile")[0]);
	if($("#upload").val()==''){
		infoMask('上传文件不能为空');
		return;
	}
	loadingShow("文件上传中，请稍后...");
	//formData.set('file', document.getElementById("upload").files[0]);
    var url = "/grms/fileUpload/upload";
	$.ajax({
        url: url,
        type: 'POST',
        cache: false,
        data:formData,
        processData: false,
        contentType: false,
        dataType: "json"
    }).done(function(data) {
        if(data.result=='true'){//文件上传成功
        	//文件url
        	var pathTemp=data.path;
        	var atttemp=new Object();
        	var fileName=$("#upload").val().split("\\");
        	
        	atttemp.fileName=fileName[fileName.length-1];
        	atttemp.fileUrl=pathTemp;
        	atttempList.push(atttemp);
        	
     		$("#filesUploadUrl").append('<div >"'+atttemp.fileName+'" </div>');
    		infoMask('文件上传成功');
    		$("#upload").val('');
    		loadingShow(false);

        }else{
    		infoMask('文件上传失败');
    		loadingShow(false);

        }
    });
}

/*上传文件弹窗*/
function upLoadFilesWin(id,status){
	$('#urlUploadFile').val('');
	atttempList=new Array();
    $("#filesUploadUrl").empty();
	 $.ajax({
         type : "GET",  //提交方式
         url : "/grms/contract/getAttachment",//路径
         async: false,
         data:  {"id":id,"status":"a"},
         dataType : "json",//数据，这里使用的是Json格式进行传输
         success : function(result) {//返回数据根据结果进行相应的处理
             if ( result.success) {
            	var urlArray=result.obj
            	for(var i=0;i<urlArray.length;i++){
    	     		$("#filesUploadUrl").append('<div >"'+urlArray[i].fileName+'" </div>');
    			}
    		} else {
                 infoMask("加载附件失败");

             }
         }
     });
	wrapMaskShow();//父级遮罩显示
	 $('body').css('overflow','hidden');//禁止滚动
	 
	 $('#roleWin8').dialog({
	        width:580,
	        height:300,
	        modal:true,
	        collapsible:false,
	        minimizable:false,
	        maximizable:false,
	        closable:true,
	        draggable:false,
	        resizable:false,
	        inline:false,
	        title:"上传附件",
	        ok:"取消",
	        cancel:"确定",
	        buttons:[{
	            text:'取消',
	            handler:function(){
	                wrapMaskHide();	                
	                $('#roleWin8').dialog('close');
	            }
	        },{
	            text:'确认',
	            handler:function(){
	                wrapMaskHide();
	                if(atttempList.length==0){
	                    infoMask("请上传附件");
                        return;
	                }
	                updateUrl(id,atttempList,status);
	            }
	        }],
	        onClose : function(){
	            wrapMaskHide();//父级遮罩隐藏
	            $('body').css('overflow','auto');//恢复滚动
	        }
	    });
	    winHiSelfAdaptation($('#roleWin_detail'));//弹窗定位

}  

/*删除文件弹窗*/
function upLoadFilesWinDel(id,status){
	$("#filesDelUrl").empty();
	var temp;
	if(status==7||status==5){
		temp='c';
	}else{
		temp='a';
	}
	 $.ajax({
         type : "GET",  //提交方式
         url : "/grms/contract/getAttachment",//路径
         async: false,
         data:  {"id":id,"status":temp},
         dataType : "json",//数据，这里使用的是Json格式进行传输
         success : function(result) {//返回数据根据结果进行相应的处理
             if ( result.success) {
            	var urlArray=result.obj
            	for(var i=0;i<urlArray.length;i++){
   				 $("#filesDelUrl").append('<div > <input type="checkbox" id="checkUrl"   value="'+urlArray[i].id+'" > "<span>'+urlArray[i].fileName+'</span>" </div>');
    			}
            	if(urlArray.length==0){
                	infoMask("没有要删除的文件");
                	return;
        		}
            	if(status!=7&&status!=5){
            		if(urlArray.length==1){
                    	infoMask("只有一个附件不能删除");
                    	return;
            		}
            	}
            	
            wrapMaskShow();//父级遮罩显示
           	 $('body').css('overflow','hidden');//禁止滚动
           	 
           	 $('#roleWin10').dialog({
           	        width:580,
           	        height:250,
           	        modal:true,
           	        collapsible:false,
           	        minimizable:false,
           	        maximizable:false,
           	        closable:true,
           	        draggable:false,
           	        resizable:false,
           	        inline:false,
           	        title:"删除附件",
           	        ok:"取消",
           	        cancel:"确定",
           	        buttons:[{
           	            text:'取消',
           	            handler:function(){
           	                wrapMaskHide();	                
           	                $('#roleWin10').dialog('close');
           	            }
           	        },{
           	            text:'确认',
           	            handler:function(){
           	                wrapMaskHide();
           	                var id=new Array();
           	            	 $.each($('input:checkbox:checked'),function(){
           	            			 id.push($(this).val());
           	            	
           	                 });
           	            	if(id.length==0){
          	            		 infoMask("请选择要删除的附件");
          	            		 return;
          	            	 	}
           	            	if(status!=7&&status!=5){

           	            		if(id.length==urlArray.length){
           	            		 infoMask("不能删除全部附件");
           	            		 return;
           	            	 	}
           	            	}
           	            	 delUrl(id,status);
           	            	$('#roleWin10').dialog('close');
           	            }
           	        }],
           	        onClose : function(){
           	            wrapMaskHide();//父级遮罩隐藏
           	            $('body').css('overflow','auto');//恢复滚动
           	        }
           	    });
           	    winHiSelfAdaptation($('#roleWin_detail'));//弹窗定位
    		} else {
                 infoMask("加载附件失败");

             }
         }
     });
	

} 
//更新附件url
function updateUrl(id,atttempList,status){
	//查找出框架合同的其他执行合同
	 $.ajax({
           type : "POST",  //提交方式
           url : "/grms/contract/updateContractUrl",//路径
           async: false,
           data:  {"id":id,"atttempList":JSON.stringify(atttempList),"status":status},
           dataType : "json",//数据，这里使用的是Json格式进行传输
           success : function(result) {//返回数据根据结果进行相应的处理
               if ( result.success) {
                   infoMask("更新附件成功");
                   findDepart('','');
	            	$('#roleWin8').dialog('close');
               } else {
                   infoMask("更新附件失败");

               }
           }
       });
}
//更新附件url
function delUrl(id){
	//查找出框架合同的其他执行合同
	 $.ajax({
           type : "POST",  //提交方式
           url : "/grms/contract/delContractUrl",//路径
           async: false,
           data:  {"id":id},
           traditional: true,
           dataType : "json",//数据，这里使用的是Json格式进行传输
           success : function(result) {//返回数据根据结果进行相应的处理
               if ( result.success) {
                   infoMask("更新附件成功");
                   findDepart('','');
               } else {
                   infoMask("更新附件失败");

               }
           }
       });
}