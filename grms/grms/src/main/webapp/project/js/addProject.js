function goback(){
	window.location.href="../project/goback.html";
}

//添加客户
function addCustomer(){	
	initCustomerList();
	
	 wrapMaskShow();//父级遮罩显示
	 $('body').css('overflow','hidden');//禁止滚动
	 
	 $('#add_Customer').dialog({
	        width:740,
	        height:500,
	        modal:true,
	        collapsible:false,
	        minimizable:false,
	        maximizable:false,
	        closable:true,
	        draggable:false,
	        resizable:false,
	        inline:false,
	        title:"选择客户",
	        ok:"确定",
	        cancel:"取消",
	        buttons:[{
	            text:'取消',
	            handler:function(){
	                wrapMaskHide();
	                
	                $('#add_Customer').dialog('close');
	            }
	        },{
	            text:'确认',
	            handler:function(){
	                wrapMaskHide();	                	                
	                if($("input[name='customer']:checked").val()){	                	
	                	var customer_id=$("input[name='customer']:checked").val();
	                	$("#customerId").val(customer_id);
	                	var customerInfo=$("#customerList").datagrid("getSelected");	           	                
	                	$("#customerName").val(customerInfo.contactor+'-'+customerInfo.department+'-'+customerInfo.name);
	                	$("#customerName").attr("hidden",false);
	                }
	            	
	            	$('#add_Customer').dialog('close');
	            }
	        }],

	        onClose : function(){
	            wrapMaskHide();//父级遮罩隐藏
	            $('body').css('overflow','auto');//恢复滚动
	        }
	    });
	    winHiSelfAdaptation($('#roleWin_detail'));//弹窗定位
	    	    
}

function initCustomerList(){
	
//	表格数据渲染
    $('#customerList').datagrid({
    	//url:'customer.json',
    	url:'../customer/getlistData.html',
    	queryParams:{
    		pageSize:5,
        	pageNo:	1
    	},
        border:true,
        pageSize:5,
        scrollbarSize:0,
        nowrap:false,//允许换行
        fitColumns:true,//宽度自适应
        selectOnCheck:true,
        singleSelect:true,
        emptyMsg:'<span>无记录</span>',
        fitColumns:true,//宽度不自适应
        checkOnSelect:false,//点击该复选框的时候才会选中或取消
        onClickRow: function(rowIndex, rowData){
            //加载完毕后获取所有的checkbox遍历
            var radio = $("input[name='customer']")[rowIndex].disabled;
            //如果当前的单选框不可选，则不让其选中
            if (radio!= true) {
                //让点击的行单选按钮选中
                $("input[name='customer']")[rowIndex].checked = true;
            }
            else {
                $("input[name='customer']")[rowIndex].checked = false;
            }
        },
        columns:[[
          {
              field:'id_remark',
              title:'',          
              width:10,
              align:'center',
              formatter: function (value, row, index) {  
                  return "<input name='customer' type='radio' value='"+row.id+"'/> ";  
              }  
          },{
                field:'contactor',
                title:'主要联系人',                
                width:40,
                align:'center'
            },{
                field:'dutyName',
                title:'负责人名称',
                width:40,
                align:'center'
            }/*,{
                field:'id',
                title:'客户id',
                checkbox:true,
                width:50,
                align:'center'
            }*/,{
            	 field:'name',
                 title:'客户名称',                 
                 width:50,
                 align:'center'
            },{
           	 field:'department',
             title:'所属部门',
             width:50,
             align:'center'
            },{
              	 field:'id',
                 title:'客户ID',
                 width:120,
                 align:'center'
            }
        ]],
        onLoadSuccess: function (data) {   
        	paginationpage(data.total);
        	inputStyle();
			 
			 setTimeout(function(){
				 $(".datagrid-row,.datagrid-header-check").click(function(){
				    setTimeout(function(){
				        inputStyle();
				    },100)
				});
			 },10)
        }
    });
    
}

function paginationpage(total){
	//console.log(total+"测试")
    //分页
    $('#pp').pagination({
        total:total,
        layout:['first','prev','links','next','last','manual'],
        emptyMsg: '<span>无记录</span>',
        showRefresh:true,
        pageSize:5,
        displayMsg:' ',
        onSelectPage:function (pageNo, pageSize) {
        	pageNoAll=pageNo;
        	pageSizeAll=pageSize;
            $("#customerList").datagrid('load', {    
            	pageSize:5,
            	pageNo:	pageNo
             });
       }
      });
}

/**
 * 删除按钮是否隐藏
 */
function selectDeleteShow(){
	if($(".projectPeople").length>1){
		$(".projectPeople").each(function(){
			$(this).find(".buleButton").css('display','block');//显示
		});		
	}else{
		$(".projectPeople").each(function(){
			$(this).find(".buleButton").css('display','none');//显示
		});		
	}
}

function addProjectRoleId(projectPeople){
	$("#"+projectPeople).combobox({    
	    url:'../projectRole/getProjectRoleList.html',    
	    valueField:'id',    
	    textField:'name',
	    editable:false,
	    onSelect:function(){
	    	
	    }
	});  	
}

//添加联系人
function addProjectPeople(){
	var project_role_id=uuid();
	var addProjectPeople='<div   class="projectPeople" style="margin-top: 20px;">	<div class="projectPeople1"><span class="buleButton fr" onclick="removeProjectPeople(this)">删除</span></div>	<div class="projectPeople2"> <div class="projectPeopleTBox">	<label class="projectPeopleTl"> <span class="xinhao">项目角色：</span></label>	<label class="projectPeopleTR"> <input id="'+project_role_id+'" class="project_role"  style ="padding :10px;width : 100%;height: 36px;border: 1px solid #ccc;" name="projectRole" value="请选择" style ="padding :10px;width : 170px;height: 32px;" >  	</label>	</div>	<div class="projectPeopleTBox">	<label class="projectPeopleTl"> <span class="xinhao">联系人名称：</span></label>	<label class="projectPeopleTR"> <input type="text" style="height: 34px;border:1px solid #ccc;border-radius:5px;" name="projectlinkName" value=""  placeholder="请输入少于10位数字"	maxlength="10" />	</label></div>	<div class="projectPeopleTBox">	<label class="projectPeopleTl"> <span class="xinhao">联系人电话：</span></label>	<label class="projectPeopleTR"> <input name="projectlinkPhone" placeholder="请输入少于11位数字" type="text" id="name" style="height: 34px;border:1px solid #ccc;border-radius:5px;" value=""	maxlength="11" />	</label></div>	</div>';
	$("#projectPeopleList").append($(addProjectPeople));
	addProjectRoleId(project_role_id);
	
	selectDeleteShow();
}

//删除联系人
function removeProjectPeople(e){
	var nodes=$(".projectPeople"); // 全部子节点
	if(nodes.length>1){
         $(e).parent().parent().remove();
	}else{
		infoMask("至少需要保留一个联系人");
	}
	
	selectDeleteShow();
}

//异步上传附件
function updateFile(){
    var filePath=$("#projectFile").val();
    
	if($("#projectFile").val()!=null&&$("#projectFile").val()!=''){
		var houzhui=filePath.substring(filePath.lastIndexOf(".")+1,filePath.length);
		if(houzhui =='bat' || houzhui=='exe'){
		    infoMask("文件格式有误,上传失败,请重新选择");
		  //清除 查看附件按钮的内容
        	$("#projectFile").val("");
			return;
		}
		
	var formData = new FormData();
    formData.append("file", $("#projectFile")[0].files[0]); 
    //formData.append("uploadFileName",$("#projectFile").val());
    
    //文件大小判断
    //console.log("哈哈："+$("#projectFile")[0].files[0].size);
    var fileSize=$("#projectFile")[0].files[0].size;
    if(fileSize>(1024*1024*50)){    //暂定50M
    	infoMask("附件过大,请重新上传");
    	return;
    }
   
    if($("#projectFile").val())
    
    	showShade(true , "正在上传附件,请稍后......");
	    $.ajax({
	        url: "../attachment/uploadProjectFile.html",
	        type: "POST",
	        data: formData,
	        /**
	        *必须false才会自动加上正确的Content-Type
	        */
	        contentType: false,
	        /**
	        * 必须false才会避开jQuery对 formdata 的默认处理
	        * XMLHttpRequest会对 formdata 进行正确的处理
	        */
	        processData: false,
	        beforeSend:function(){
	        	
	        },
	        success: function (data) {
	        	var obj = JSON.parse(data);
                if(true == obj.success){                	
                    //将数据保存在表格中
                	console.log(obj.obj) 
                	var attchment=obj.obj;               	
                	var	tr="<tr id='"+attchment.id+"'><td>"+attchment.fileName+"</td><td>"+
                		 '<a onclick="downloadAttchment(\''+attchment.id+"','"+attchment.fileUrl+'\')">下载</a>'+
                		 '<a onclick="deleteAttchment(\''+attchment.id+'\')">删除</a>'+
                		 "</td></tr>";
                		
                	console.log(tr) 
                	$("#projectFileList").append(tr);
                	                              	
                	//清除 查看附件按钮的内容
                	$("#projectFile").val("");
                	                	
                    infoMask('上传成功');                 
                    $(document).click();
                }else{
                	infoMask('上传失败:' +obj.msg);
                }
	        },
	        error: function () {
	        	infoMask('上传失败:' +"服务器响应异常");
	        },
	        complete : function (XHR, TS){
                showShade(false);
            }
	    });
	}else{
		infoMask('请选择要上传的文件');  
	}
}



//下载附件
function downloadAttchment(attchmentId,fileUrl){
	window.location.href="../attachment/download.html?id="+attchmentId;
}

//删除附件,只做session删除, 立项时在对服务器文件进行删除
function deleteAttchment(attchmentId){
	console.log("删除:"+attchmentId);
	showShade(true , "正在删除,请稍后......");
	$.ajax({
        url: "../attachment/deleteAttchment.html?id="+attchmentId,
        type: "GET",               
        success: function (data) {
        	var obj = JSON.parse(data);
            if(true == obj.success){ 
            	infoMask('删除成功');
            	
            	$("#"+attchmentId).remove();
            	
            }else{
            	infoMask('删除失败');
            }    
        },
        error: function () {
        	infoMask('删除文件失败');
        },
        complete : function (XHR, TS){
            showShade(false);
        }
    });	   
}

/*
显示遮罩
*/
function showShade(show , text){
   if(true == show){
       if(text){
           $('.shadeBox .tipsInfo p').html(text);
       }     	
	    wrapMaskShow();//父级遮罩显示
	    $('body').css('overflow','hidden');//禁止滚动	    
        $('.shadeBox').show();
   }else{
       wrapMaskHide();//父级遮罩隐藏
       $('body').css('overflow','auto');//恢复滚动
       $('.shadeBox').hide();
   }

}


//立项标志
var  save_flag=true;  //默认立项通过
var  save_no=""; //不通过原因
//立项
function saveProject(){
	 //获取表单数据	 
	 var params=packageFrom();
	 
	// console.log(JSON.stringify(params));
	
	var obj=$('.personalListBox2').datagrid("getData").rows;	
	/*if(obj.length<=0){
		save_flag=false;  //默认立项通过
        save_no="未有清单,不可立项"; //不通过原因
	}*/
	
	//console.log(save_flag+":"+save_no);
	
	if(save_flag==false){
		save_flag=true;
		infoMask(save_no);
		return;
	}
	 
	// $("#textHint").text(textHint);
	 
	// wrapMaskShow();//父级遮罩显示
	// $('body').css('overflow','hidden');//禁止滚动
	 createConfirm("消息提醒","确认立项后将不可修改，要修改请在审批之前进行撤回后再修改。",true,false,"确定","取消",okCallbakFunciton,"","","");
	 function okCallbakFunciton(){
		 showShade(true , "立项提交中......");             
	 	 $.ajax({
	 	        url: "../project/createProject.html",
	 	        type:"POST",
	 	        data:JSON.stringify(params),
	 	        dataType:"json",
	 	        contentType:'application/json;charset=utf-8',
	 	        success: function (data) {	        	        	
	                 if(true == data.success){                		                        	
	                 	successMask('保存成功',"/grms/project/list.html");                 
	                     
	                 }else{
	                	//infoMask('保存失败:资源被占用');
	                 	infoMask(data.msg);
	                 }
	 	        },
	 	        error: function () {
	 	        	infoMask('保存失败:' +"服务器响应异常");
	 	        },
	 	        complete : function (XHR, TS){
	                 showShade(false);
	             }
	 	 }); 
	  }	

}

function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";
 
    var uuid = s.join("");
    return uuid;
}


$(function(){ 
	for(var i=1;i<=$("#projectPeoples").val();i++){
		addProjectRoleId("projectRole"+i);
	}
	
	
	initCustomerList();
	
	//联系人显示与隐藏
	selectDeleteShow();

});

//新增
function addProjectProject(){
	var params=packageFrom();
	console.log(params);
	
	project_vo_str=encodeURIComponent(JSON.stringify(params));
	//console.log(project_vo_str);
	window.location.href="../projectProduct/goProjectProduct.html?operate=0&projectVoStr="+encodeURIComponent(JSON.stringify(params));
}


//获取表单数据并封装
function packageFrom(){
	var params={};
	//项目id
	var id=$("#projectId").val();
	if(id&&id!='')
	params.id=id;
	
	//项目编号
	var projectCode=$("#projectCode").val();
	if(projectCode&&projectCode.trim()!=''){
		params.projectCode=projectCode;
	}
	
	
	//项目名称	
	var name=$("#name").val();
	if(name&&name.trim()!=''){
		params.name=name;
	}else{		
		save_flag=false;
		save_no="项目名称不能为空";
	}
	
	
	//客户id
	var customerId=$("#customerId").val();
	if(customerId&&customerId!=''){
		params.customerId=customerId;
	}else{		
		save_flag=false;
		save_no="请选择客户";
	}
	
	
	//项目客户信息
	var customerInfo=$("#customerName").val();
	if(customerInfo&&customerInfo!='')
	params.customerInfo=customerInfo;
	
	//联系人信息
	var listLink=[];
		
	$("div .projectPeople").each(function(index,element){
		var project_people={};			
		projectRoleName=$(element).find('.project_role').combobox("getText");
		//console.log("projectRoleName:"+projectRoleName);
		projectRoleId=$(element).find('.project_role').combobox("getValue");
		projectlinkName=$(element).find('input[name="projectlinkName"]').val();
		projectlinkPhone=$(element).find('input[name="projectlinkPhone"]').val();		
		project_people.projectRoleId=projectRoleId;
		project_people.projectRoleName=projectRoleName;
		project_people.name=projectlinkName;
		project_people.phone=projectlinkPhone;
		
		if(projectRoleId.trim()==''||projectRoleId=='请选择'){
			save_flag=false;
		     save_no="项目角色不能为空";
		}
		if(projectlinkName.trim()==''){
			save_flag=false;
		     save_no="联系人姓名不能为空";
		}
		if(projectlinkPhone.trim()==''){
			save_flag=false;
		    save_no="联系人电话不能为空";
		}/*else if(!(/^[1][3,4,5,7,8][0-9]{9}$/.test(projectlinkPhone))){
			save_flag=false;
		    save_no="请输入正确的联系人手机号";
		}*/else if(!(/^[0-9]{1,11}$/.test(projectlinkPhone))){
			save_flag=false;
		    save_no="请输入正确的联系人手机号";
		}
		
		listLink.push(project_people);
	});
	params.projectPeoples=listLink;
		
	//项目说明
	var description=$("#description").val();
	//console.log("description:"+description);
	if(description&&description!='')
	params.description=description;
	
	return params;
}


//清单修改,编辑
function updateProductMean(projectProductId,businessType){
	var params=packageFrom();
	window.location.href='../projectProduct/goProjectProduct.html?operate=1&businessType='+businessType+'&id='+projectProductId+'&projectVoStr='+encodeURIComponent(JSON.stringify(params));
}

//清单,删除

function deleteProductMean(projectProductId,businessType,iiii,len){
	createConfirm("消息提醒","确认是否要删除？",true,false,"确定","取消",function(){
		wrapMaskHide();		                
	    showShade(true , "数据删除中......");
	    
	    $.ajax({
	        url: "../projectProduct/deleteProductMean.html",
	        type:"POST",
	        data:{
	        	id:projectProductId,  //删除项目清单id
	        	type:businessType,  //删除项目清单类型
	        },
	        success: function (data) {	 
	        	var result=JSON.parse(data);
	            if(true == result.success){                		                        	
	            	var num;
	            	var obj=$('.personalListBox2').datagrid("getData").rows;	
	            	for(var i=0;i<obj.length;i++){
	            		if(obj[i].id==projectProductId){
	            	    	  num=i;
	            	    	  break;
	            	     }
	            	}
	            	for(var i=0;i<len;i++){	
	            		$('.personalListBox2').datagrid("deleteRow",num);			
	            	}
	            	infoMask('删除清单成功');
	            	
	            }else{
	            	infoMask('删除清单失败:' +result.msg);
	            }
	        },
	        error: function () {
	        	infoMask('删除清单失败:' +"服务器响应异常");
	        },
	        complete : function (XHR, TS){
	            showShade(false);
	        }
	    });	
	},"","","");	
}

/**
 * 下载资源清单
 */
function downProductMeans(){
	if($('.personalListBox2').datagrid("getData").rows.length<=0){
		infoMask("未选中清单,无法下载!!!");
	}else{
		window.location.href="../project/exportDetail?projectType=1";
	}
}
	


