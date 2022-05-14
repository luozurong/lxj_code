var valueArr = '';
$(function(){
	
	$(".department-choose span").click(function(){
		if($(this).hasClass("department-choose-active")){
			$(this).removeClass("department-choose-active");
		}else{
			$(this).addClass("department-choose-active");
		}
		
		valueArr = '';
		$(".department-choose .department-choose-active").each(function(){
			valueArr += $(this).attr("data-value")+',';
		})
	})
	
	roleWin();
});

//确定
function saveData(){
	var departmentName = $("#departmentName").text();
	var departmentId = $("#departmentId").val();
	
	var businessType = valueArr.substring(0, valueArr.length - 1);;
	var province = '';
	var provinceName = '';
	var city = '';
	var cityName = '';
	var areaFlag = '';
	var roots=$('#box2').tree('getChecked');    
    for (var i = 0; i < roots.length; i++) {
    	a= roots[i].id;
        b= roots[i].text;
        if(a=='0086'){//判断是否全国
        	areaFlag = "0";
        	break; 
        }else{
        	if(roots[i].parentId==''||roots[i].parentId==null){//没有父节点则为省市
        		province += a+",";
        		provinceName += b+",";
        	}else{
        		city += a+",";
        		cityName += b+",";
        	}
        	areaFlag = "1";
        }
    }
    province = province.substring(0, province.length - 1);
    provinceName = provinceName.substring(0, provinceName.length - 1);
    city = city.substring(0, city.length - 1);
    cityName = cityName.substring(0, cityName.length - 1);
	$.ajax({
		type:"POST",
		url:"/grms/departmentBusiness/save",
		data:{"departmentId":departmentId,"departmentName":departmentName,"businessType":businessType,"province":province,"provinceName":provinceName,
				"city":city,"cityName":cityName,"areaFlag": areaFlag},
		dataType:"json",
		success:function(result){
			if(result.success){
				infoMask("创建成功");
				window.location.href="/grms/departmentBusiness/list.html";
			}else{
				infoMask("创建失败");
			}
		}
	})
}

//取消
function cancel(){
	window.history.go(-1);
}
/*function cancel(){
	var str = '1,2,3,4,5,';
	str.substring(0, str.length - 1);
	alert(str)
}*/


/*点击创建，详情，编辑  弹窗*/
function roleWin(){
	 initTreeArea('','','','','');
}

//分配区域
function initTreeArea(parentId,target,layer,userId,status){
	 $.ajax({  
         type : "POST",  //提交方式  
         url : "/ums/areaManagementAction!areaTree.html",//路径  
 		 data:{"id":parentId,"layer":layer,"userId":userId,"status":status},
 		 beforeSend: function () {
 			 
        },
         dataType : "json",//数据，这里使用的是Json格式进行传输  
         success : function(result) {//返回数据根据结果进行相应的处理  
        	   if ( result.success) {  
	               	var treejson;
	               	treejson=result.obj.menuPro;
	               	if(parentId==''){
	                 	append2(treejson);
	
	            	 }else{
	            		 $('#box2').tree('append', {
	            				parent: target,
	            				data:treejson
	            		 })
	            	 }
               }    
         }  
     }); 
	
}
function append2(treejson){
	  //树数据渲染
  $('#box2').tree({
  	data : treejson,
      animate : true,
      checkbox : true,
      cascadeCheck : true,
      onlyLeafCheck : false,
      lines : false,
      dnd : false,
      onCheck : function (node,checked) {
    	  
      },  
      onBeforeExpand:function(node,param){                         

      } 
  });
}
