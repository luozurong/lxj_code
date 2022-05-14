/**
 * 应用接入js
 */

//审核
function audit(id){
	 var diag = new top.Dialog();
		diag.Title = "平台对接账号审核";
		diag.URL = "/lxjsdk/auditApplicationPage.html?id="+id;
		diag.Height = 250;
		diag.Width = 350;
		diag.ShowCancelButton = false;
		diag.OkButtonText="审核通过";
	    diag.OKEvent = function () {
	    	jQuery.post("auditApplication.html",{"id":id,"verifyStatus":"2"},function(result){
				if(1==result){
					top.Dialog.alert("审核通过设置成功!");
					diag.close();
					$("#application").submit();
				}
				else {
					top.Dialog.alert("审核通过设置失败!");
					diag.close();
					$("#application").submit();
				}
			},"html");
	    	
		};
	    
	    diag.show();
	    diag.addButton("next"," 审核不通过",function(){
	    	jQuery.post("auditApplication.html",{"id":id,"verifyStatus":"1"},function(result){
				if(1==result){
					top.Dialog.alert("审核不通过设置成功!");
					diag.close();
					$("#application").submit();
				}
				else {
					top.Dialog.alert("审核不通过设置失败!");
					diag.close();
					$("#application").submit();
				}
			},"html");
		});
		showProgressBar();
}


function view(id){
	var diag = new top.Dialog();
	diag.Title = "查看";
	diag.URL = "/lxjsdk/viewApplication.html?id="+id;
	diag.Height = 300;
	diag.Width = 500;
	diag.ShowCancelButton = false;
    diag.OKEvent = function () {
    	diag.close();
	}
    diag.show();
	showProgressBar();
}