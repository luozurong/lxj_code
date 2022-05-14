/**
 * 平台接入js
 */
//查看
function view(id){
	var diag = new top.Dialog();
	diag.Title = "查看";
	diag.URL = "/lxjsdk/viewPlatform.html?id="+id;
	diag.Height = 300;
	diag.Width = 500;
	diag.ShowCancelButton = false;
    diag.OKEvent = function () {
    	diag.close();
	}
    diag.show();
	showProgressBar();
}
//审核
function audit(id){
	 var diag = new top.Dialog();
		diag.Title = "平台对接账号审核";
		diag.URL = "/lxjsdk/auditPlatFormPage.html?id="+id;
		diag.Height = 150;
		diag.Width = 300;
		diag.OkButtonText="审核通过";
		diag.ShowCancelButton = false;
	    diag.OKEvent = function () {
	    	top.Dialog.confirm("确定公司信息无误审核通过吗？",function(){
	    		jQuery.post("auditPlatForm.html",{"id":id,"verifyStatus":"2"},function(result){
					if(1==result){
						top.Dialog.alert("审核通过设置成功!");
						diag.close();
						$("#platForm").submit();
					}
					else {
						top.Dialog.alert("审核通过设置失败!");
						diag.close();
						$("#platForm").submit();
					}
				},"html");
	    		});		
		};
	    diag.show();
	    diag.addButton("next"," 审核不通过",function(){
	    	top.Dialog.confirm("确定公司信息错误审核不通过吗？",function(){

		    	jQuery.post("auditPlatForm.html",{"id":id,"verifyStatus":"1"},function(result){
					if(1==result){
						top.Dialog.alert("审核不通过设置成功!");
						diag.close();
						$("#platForm").submit();
					}
					else {
						top.Dialog.alert("审核不通过设置失败!");
						diag.close();
						$("#platForm").submit();
					}
				},"html");
				});	
		});
		showProgressBar();
}

function auditArea(id){
	var diag = new top.Dialog();
	diag.Title = "审核接入小区";
	diag.URL = "/lxjsdk/auditAreasList.html?id="+id;
	diag.Height = 300;
	diag.Width = 800;
	diag.ShowCancelButton = false;
    diag.OKEvent = function () {
    	diag.close();
	}
    diag.show();
	showProgressBar();
}

