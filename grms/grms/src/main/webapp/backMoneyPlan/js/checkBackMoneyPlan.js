
//初始化按钮
$(function(){
	
});



//审核通过
function pass(){	
	createConfirm("确认消息","确定审核通过？",true,false,"确定","取消",okCallbakFunciton,"","","");
	 function okCallbakFunciton(){
		 var backMoneyPlanCode = $("#backMoneyPlanCode").val();
		    $.ajax({  
		        type : "POST",  //提交方式  
		        url : "/grms/backMoneyPlan/checkerBackMoneyPlan",//路径  
		        dataType : "json",//数据，这里使用的是Json格式进行传输  
		        data:  {"backMoneyPlanCode":backMoneyPlanCode,"type":"1"},  
		        success : function(result) {//返回数据根据结果进行相应的处理  
		            if ( result.success) {  
		            	infoMask('审核成功');
		            	window.location.href="/grms/backMoneyPlan/list.html";
		            } else {  
		            	infoMask('审核失败');
		            }  
		        }  
		    });
	  }	
	/*$.messager.confirm("确认消息", "确定审核通过？", function (isDelete) {
		wrapMaskHide();
		if (isDelete) {
			$.ajax({  
		        type : "POST",  //提交方式  
		        url : "/grms/backMoneyPlan/checkerBackMoneyPlan",//路径  
		        dataType : "json",//数据，这里使用的是Json格式进行传输  
		        data:  {"backMoneyPlanCode":backMoneyPlanCode,"type":"1"},  
		        success : function(result) {//返回数据根据结果进行相应的处理  
		            if ( result.success) {  
		            	infoMask('审核成功');
		            } else {  
		            	infoMask('审核失败');
		            }  
		        }  
		    }); 
    	  }
		});*/
	 
}

function notPass(){
	createConfirm("确认消息","确定审核不通过？",true,false,"确定","取消",okCallbakFunciton2,"","","");
	 function okCallbakFunciton2(){
		 var backMoneyPlanCode = $("#backMoneyPlanCode").val();
		 $.ajax({  
		        type : "POST",  //提交方式  
		        url : "/grms/backMoneyPlan/checkerBackMoneyPlan",//路径  
		        dataType : "json",//数据，这里使用的是Json格式进行传输  
		        data:  {"backMoneyPlanCode":backMoneyPlanCode,"type":"2"},  
		        success : function(result) {//返回数据根据结果进行相应的处理  
		            if ( result.success) {  
		            	infoMask('审核不通过');
		            	window.location.href="/grms/backMoneyPlan/list.html";
		            } else {  
		            	infoMask('审核失败');
		            }  
		        }  
		    });
	  }	
	/*var backMoneyPlanCode = $("#backMoneyPlanCode").val();
	$.messager.confirm("确认消息", "确定审核不通过？", function (isDelete) {
		wrapMaskHide();
		if (isDelete) {
			$.ajax({  
		        type : "POST",  //提交方式  
		        url : "/grms/backMoneyPlan/checkerBackMoneyPlan",//路径  
		        dataType : "json",//数据，这里使用的是Json格式进行传输  
		        data:  {"backMoneyPlanCode":backMoneyPlanCode,"type":"2"},  
		        success : function(result) {//返回数据根据结果进行相应的处理  
		            if ( result.success) {  
		            	infoMask('审核成功');
		            } else {  
		            	infoMask('审核失败');
		            }  
		        }  
		    }); 
    	  }
		});*/
}

