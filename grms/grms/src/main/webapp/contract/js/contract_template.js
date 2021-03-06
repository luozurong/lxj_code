//	表格数据渲染
var data2 ;

var systemId="1";
//需要隐藏的按钮
var mapButton = {};
var userType;
var pageNoAll='1';
var pageSizeAll='10';

$(function(){
    configMenuItem("合同管理","合同模板管理");
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
    initButton();
    $("#uploadFile").click(function(){
   	 upload();
   });
});

function initButton(){
	//
	mapButton['contractTepSelect'] = 1;
	mapButton['contractTepUpload'] = 1;
	mapButton['contractTepDel'] = 1;
	var menuUrl="/grms/contract/contractPlate.html";
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
            	if(mapButton['contractTepSelect']==0){
            	  $("#contractTepSelect").hide();
              	}
              	if(mapButton['contractTepUpload']==0){
              		$("#contractTepUpload").hide();
              	}
             
            }else{
            	userType=0;
            }
                findDepart('','');
            } else {  
            	
            }  
        }  
    }); 
  
}
/**
 * 页面单按钮上传
 */
function upload(){
	var formData = new FormData($("#upfile")[0]);
	if($("#upload").val()==''){
		infoMask('上传文件不能为空');
		return;
	}
	//formData.set('file', document.getElementById("upload").files[0]);
    var url = "/grms/fileUpload/upload";
    loadingShow("文件上传中，请稍后...");
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
        	var path=data.path;
        	$('#url').val(path);//显示图片，demo
    		infoMask('文件上传成功');
    	    loadingShow(false);

        }else{
    		infoMask('文件上传失败');
    	    loadingShow(false);

        }
    });
}

//查询
function findDepart(pageSize,pageNo){
	var createTimeBegin= $(".datebox-1").datebox('getValue');
	var createTimeEnd=$(".datebox-2").datebox('getValue');
	var contractOrAccount=$('#contractOrAccount').val();
	pageSize=pageSizeAll;
	pageNo=pageNoAll;
	var data=new Object();
	data.startTime=createTimeBegin;
	data.endTime=createTimeEnd;
	data.contractOrAccount=contractOrAccount;
	data.pageSize=pageSize;
	data.pageNumber=pageNo;
	
    $.ajax({  
        type : "POST",  //提交方式  
        contentType : "application/json;charset=utf-8", 
        url : "/grms/contract/getContractTemplateListData",//路径  
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        data:  JSON.stringify(data),  
        success : function(result) {//返回数据根据结果进行相应的处理  
            if ( result.success) {  
            	var data2=result.obj.list;
            	var total=result.obj.total;
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
                title:'合同模板id',
                width:220,
                align:'left',
                hidden:'true'
            },
            {
                field:'name',
                title:'合同模板名称',
                width:150,
                align:'left'
            },
            {
                field:'url',
                title:'模板附件(可点击下载)',
                width:400,
                align:'left',
                formatter: function(value,row,index){
                	var short=row.url.split("/");
                	var name=short[short.length-1].replace(/\([\d)]*\)/g,"");
                	var allUrl = '<div href="javascript:;" style="color: #51d2b6" onclick="download2(\''+row.url+'\',\''+name+'\')">' +name+ '</div>';
                 	return allUrl;	
                }  
            },
            {
                field:'createrAccount',
                title:'创建人员',
                width:190,
                align:'left'
            },
            {
                field:'createTime',
                title:'创建时间',
                width:280,
                align:'left'
            },
            {
                field:'handle',
                title:'操作',
                width:574,
                align:'left',
                formatter: function(value,row,index){
                	var buttonChoice='';
                    var contractTepDel = '<a href="javascript:;" data-id="3" onclick="checkDel(\''+row.id+'\')">删除</a>';
                    if(userType!=0){
                		if(mapButton['contractTepDel']==1){
                			buttonChoice=contractTepDel;
                		}
                	}else{
                		buttonChoice=contractTepDel;
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
            findDepart('','') ;   
           
        }
    });
    $(".pagination-page-list").parent().append("条");
    $(".pagination-page-list").parent().prepend("共计"+total+"条,每页显示： ");
}
/*点击创建*/
function roleWin(obj,index){
	wrapMaskShow();//父级遮罩显示
	 $('body').css('overflow','hidden');//禁止滚动
	 
	 $('#roleWin').dialog({
	        width:580,
	        height:230,
	        modal:true,
	        collapsible:false,
	        minimizable:false,
	        maximizable:false,
	        closable:true,
	        draggable:false,
	        resizable:false,
	        inline:false,
	        title:"",
	        ok:"取消",
	        cancel:"确定",
	        buttons:[{
	            text:'取消',
	            handler:function(){
	                wrapMaskHide();	
	                $('#name').val('');
	            	$('#url').val('');
	            	$('#upload').val('');
	                $('#roleWin').dialog('close');
	            }
	        },{
	            text:'保存',
	            handler:function(){
	                wrapMaskHide();
	                var name = $('#name').val();
	            	var url=$('#url').val();
	            	if(name==""){
	            		infoMask('合同模板名称不能为空');
	            		return;
	            	}
	            	var url=$('#url').val();
	            	if(url==""){
	            		infoMask('请上传文件');
	            		return;
	            	}
	            	var status=saveMenu(name,url);
	                
	              
	            	$('#roleWin').dialog('close');
	            }
	        }],
	        onClose : function(){
	            wrapMaskHide();//父级遮罩隐藏
	            $('body').css('overflow','auto');//恢复滚动
	        }
	    });
	    winHiSelfAdaptation($('#roleWin_detail'));//弹窗定位			
 /*   var title = "";
    var rowId;
    addbutton();
    $("#button").css({"display":"block"});
    wrapMaskShow();//父级遮罩显示
    $('body').css('overflow','hidden');//禁止滚动
    $('#roleWin').window({
        width:580,
        height:230,
        modal:true,
        collapsible:false,
        minimizable:false,
        maximizable:false,
        cache : false ,
        closable:true,
        draggable:false,
        resizable:false,
        inline:false,
        title:title,
        onClose : function(){
            wrapMaskHide();//父级遮罩隐藏
            $('body').css('overflow','auto');//恢复滚动
        }
    });
    winHiSelfAdaptation($('#roleWin'));//弹窗定位
    $('.quxiao').click(function(){
        $('#roleWin').window('close');
    	$('#name').val('');
    	$('#url').val('');
    	$('#upload').val('');
    })

    $('.comfirm').click(function(){
    	var name = $('#name').val();
    	var url=$('#url').val();
    	if(name==""){
    		infoMask('合同模板名称不能为空');
    		return;
    	}
    	var url=$('#url').val();
    	if(url==""){
    		infoMask('请上传文件');
    		return;
    	}
    	var status=saveMenu(name,url);
    })*/

}

function addbutton(){
    $('#roleWin1').find("#button").remove();
    $('#roleWin1').append('<div id="button" class="the-bottom clearfix" ><span class="quxiao fl" >取消</span><span class="comfirm fl" >保存</span></div>');

}


function saveMenu(name,url){
	var object=new Object();
	object.name=name;
	object.url=url;	
    $.ajax({  
        type : "POST",  //提交方式  
        contentType : "application/json;charset=utf-8", 
        url : "/grms/contract/saveContractTemplate",//路径  
        data: JSON.stringify(object) ,  
        dataType : "json",//数据，这里使用的是Json格式进行传输  
        success : function(result) {//返回数据根据结果进行相应的处理  
            if (result.success) {  
            	$('#name').val('');
            	$('#url').val('');
            	$('#upload').val('');
                $('#roleWin').window('close');
         	    infoMask('保存成功');  
         	    findDepart('','');
           } else {
        	   warnMask('合同模板名称重复，请重新命名');  
        	   return 1;
            }  
        }  
    }); 
	
}
function download2(url,fileName) {
	//下载附件
	var urlall="/grms/attachment/downloadUrl?url="+url+"&fileName="+fileName;
	window.open(urlall,'_blank');

	}  
function checkDel(id){
    createConfirm("消息提醒","请确认是否删除",true,false,"删除","取消",okCallbakFunciton,"",id,"");
    function okCallbakFunciton(){
        $.ajax({
            type : "GET",  //提交方式
            url : "/grms/contract/delContractTemplate",//路径
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
