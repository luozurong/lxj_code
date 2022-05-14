/**
 * 接入小区js
 */

function viewPlatformArea(id){
	var diag = new top.Dialog();
	diag.Title = "查看接入小区详情";
	diag.URL = "/lxjsdk/viewPlatformArea.html?id="+id;
	diag.Height = 280;
	diag.Width = 500;
	diag.ShowCancelButton = false;
    diag.OKEvent = function () {
    	diag.close();
	}
    diag.show();
	showProgressBar();
}

function auditPlatformArea(id){
	var diag = new top.Dialog();
	diag.Title = "审核接入小区";
	diag.URL = "/lxjsdk/auditPlatformArea.html?id="+id;
	diag.Height = 400;
	diag.Width = 500;
	diag.OkButtonText="审核通过";
	diag.ShowCancelButton = false;
    diag.OKEvent = function () {
    	var result = diag.innerFrame.contentWindow.sumitDataOk();
		if(result){
			top.Dialog.alert("审核通过设置成功!");
			diag.close();
			$("#platformJoinArea").submit();
		}
    	
	}
    diag.show();
    diag.addButton("next"," 审核不通过",function(){
    	top.Dialog.confirm("确定接入小区信息错误审核不通过吗？",function(){
    		var result = diag.innerFrame.contentWindow.sumitDataNotOk();
    		if(result){
    			top.Dialog.alert("审核不通过设置成功!");
    			diag.close();
    			$("#platformJoinArea").submit();
    		}
    	});
    });
	showProgressBar();
}