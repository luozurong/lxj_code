
//客户管理，添加客户
var cityAreaInitValue=[
                           {
                               "id":0,
                               "code":"",
                               "name":"请选择",
                               "parentId":"",
                               "selected":true
                           }
 ];
$(function(){
	var salesmanName = $("#salesmanName").val();
	$("#ownerName").html(salesmanName);
	$('#city,#country,#town').combobox({
        limitToList:true,
        data:cityAreaInitValue,
        valueField:'code',
        textField:'name',
        editable:false,
        panelHeight:"auto"
    });
	proCityDistrict();
})
	
//公司栏市区焦点触发
function similarName(){
	var name = $("#companyName").val();
	$.ajax({
		 type : "post",
		 url : "/grms/customerManagement/getSimilarNameList.html",
		 dataType : "json",
		 data : {"condition":name},
		 success : function(result) {
			 if (result.succ) {
				 dataList = result.data;
				 initDg(1,10);
				 pagenationPage();
				 
				 if (dataList.length > 0) {
					 $('body').css('overflow','hidden');//禁止滚动
						//弹窗
						dialogue(name);
				}
			 }else{
				 
			 }
		 }
	 });
}

function dialogue(name){
	
	 wrapMaskShow();
	 $('#companyInfo').dialog({
	        width:500,
	       // height:300,
	        modal:true,
	        collapsible:false,
	        minimizable:false,
	        maximizable:false,
	        closable:true,
	        draggable:false,
	        resizable:false,
	        inline:false,
	        title:"消息提醒",
	        ok:"了解",
	        //cancel:"取消",
	        buttons:[{
	            text:'了解',
	            handler:function(){
	                wrapMaskHide();
	                //dataList(name);
	                $('#companyInfo').dialog('close');
	            }
	        
	        }],
	        onClose : function(){
	            wrapMaskHide();//父级遮罩隐藏
	            $('body').css('overflow','auto');//恢复滚动
	        }
	    });
}

//save
function confirmSave(){
	var companyType = $("#companyType").val();
	var num=0;
    $("input[type$='text']").each(function(n){
         if($(this).val()==""){
        	 $(this).next(".redText").show()
              num++;
         }else{
        	 $(this).next(".redText").hide()
         }
    });
    if(num>0){
    	infoMask("还有必填项未填!")
    	return;
    }else if(num == 0 && companyType == ""){
    	infoMask("请选择公司类型!")
    	return;
    }else{
    	var id = $("#detailId").val();
    	var name = $("#companyName").val();
    	//地址
    	var province = $("#province").combobox('getValue'); 
    	var provinceName = $("#province").combobox('getText'); 
    	var city = $("#city").combobox('getValue'); 
    	var cityName = $("#city").combobox('getText');
    	var area = $("#country").combobox('getValue'); 
    	var areaName = $("#country").combobox('getText');
    	if (provinceName == "请选择" || cityName == "请选择" || areaName=="请选择") {
    		infoMask("请选择完整的省、市、区地址信息!")
    		return;
		}
    	var address = $("#address").val();
    	companyType = $("#companyType").val();
    	var industry = $("#industry").val();
    	var department = $("#department").val();
    	var dutyName = $("#dutyName").val();
    	var dutyPhone = $("#dutyPhone").val();
    	if (dutyPhone != "") {
    		var a = isPoneAvailable(dutyPhone);
		}else{
			var a = true;
		}
    	var contactor = $("#contactor").val();
    	var contactorPhone = $("#contactorPhone").val();
    	var b = isPoneAvailable(contactorPhone);
    	var remark = $("#remark").val();
    	var ownerName = $("#salesmanName").val();
    	
    	if (a && b) {
    		$.ajax({
    			type : "post",
    			url : "/grms/customerManagement/save.html",
    			dataType : "json",
    			data : {"id":id,"name":name,"industry":industry,"customerTypeId":companyType,
    				"department":department,"dutyName":dutyName,"dutyPhone":dutyPhone,
    				"contactor":contactor,"contactorPhone":contactorPhone,"remark":remark,
    				"province":province,"provinceName":provinceName,"city":city,"cityName":cityName,
    				"address":address,"ownerName":ownerName,"area":area,"areaName":areaName},
    				success : function(result) {
    					if (result.succ) {
    						infoMask("添加成功!")
							setTimeout(function () {
								window.location.href = "/grms/customerManagement/list.html";
							}, 1200);
    					}else{
    						infoMask("添加失败:  服务器响应异常")
    					}
    				}
    		});
		}else{
			infoMask("请正确填写手机号码格式!")
			return;
		}
    }
}


function isPoneAvailable(str) {
    var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
    if (!myreg.test(str)) {
        return false;
    }else{
    	return true;
    } 
}



function cancel(){
	window.location.href = "/grms/customerManagement/list.html";
}

function proCityDistrict(){
	//省份的数据初始化,4级联动
    $('#province').combobox({
        url:'/ums/communityAction!initProvince.html',
        method:'post',
        value : '请选择省/市',
        valueField:'code',
        textField:'name',
        editable:false,
        onSelect:function(newValue){
        	provinceCode = newValue.code;
        	$("#provinceCode").val(provinceCode);
        	$("#cityCode").val("");
        	$("#countryCode").val("");
            $('#city').combobox({  
                url:'/ums/communityAction!getChildSelectData.html?code='+provinceCode,
                method:'post',
                value : '请选择城市',
                valueField:'code',
                textField:'name',
                onSelect:function(newValue){
                	cityCode = newValue.code;
                	$("#cityCode").val(cityCode);
                	$("#countryCode").val("");
                	$('#country').combobox({
            	        url:'/ums/communityAction!getChildSelectData.html?code='+cityCode,
            	        method:'post',
            	        value : '请选择区/县',
            	        valueField:'code',
            	        textField:'name',
            	        onSelect:function(newValue){
            	        	countryCode = newValue.code;
            	        	$("#countryCode").val(countryCode);
            	        	$('#town').combobox({
                    	        url:'/ums/communityAction!getChildSelectData.html?code='+countryCode,
                    	        method:'post',
                    	        value : '请选择',
                    	        valueField:'code',
                    	        textField:'name',
                    	        onSelect:function(newValue){
                    	        	townCode = newValue.code;
                    	        	$("#townCode").val(townCode);
									var self = $(this);
									comboboxOnSelect(self,newValue);
                    	        }
                    	    });
							var self = $(this);
							comboboxOnSelect(self,newValue);
            	        }
            	    });
					var self = $(this);
					comboboxOnSelect(self,newValue);
                }
            });
			var self = $(this);
			comboboxOnSelect(self,newValue);
        }
    });
	
}

function initDg(pageNo,pageSize){
	var xL=(pageNo-1)*pageSize;
	var yL=pageNo*pageSize;
	var data1=dataList.slice(xL,yL);
	//	表格数据渲染
	$('#companyList').datagrid({
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
			$('#companyList').datagrid('resize');	
	    },
	    onLoadSuccess:function(data){
	    	setTimeout(function(){
	    		$('#companyList').datagrid('resize');	
				var dataHeight =  $(".datagrid-view").height()-19;
		        $(".datagrid-view").css("height",dataHeight );
	    	},0)
			
	        
	    },
	    columns:[[
	        {
	            field:'name',
	            title:'公司名称',
	            width:200,
	            align:'center',
	        },
	        {
	            field:'salesman',
	            title:'业务员',
	            width:200,
	            align:'center',
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
	    pageList:[5,10,15],
	    //pageSize:10,
	    onSelectPage:function (pageNo, pageSize) {
	    	
	    	initDg(pageNo,pageSize);
	      
	    }
	});
	$(".pagination-page-list").parent().append("条");
    $(".pagination-page-list").parent().prepend("共计"+dataList.length+"条,每页显示： ");
}