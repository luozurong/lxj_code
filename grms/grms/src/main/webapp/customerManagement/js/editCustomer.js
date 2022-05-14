
//编辑客户
$(function(){
	transferInfo();
	proCityDistrict();
});

function proCityDistrict(){
	var provinceCode = $("#provinceCode").val();
	var cityCode = $("#cityCode").val();
	var districtCode = $("#districtCode").val();
	
	//省份的数据初始化,4级联动
    var url = '/ums/communityAction!initProvince.html';
    $.ajaxSettings.async = false;
    $.post(url, function(result) {
        $("#province").combobox({
            data : result,
            valueField:'code',
            textField:'name',
            cache:false,
            editable:false, //只读
            onLoadSuccess: function () { //加载完成后,设置选中的项
                $(this).combobox("setValue",provinceCode);
            },
            onSelect:function(record){
                var provinceId = record.code;
                $("#provinceCode").val(provinceId);
                $("#cityCode").val("");
                $("#districtCode").val("");
                var url = '/ums/communityAction!getChildSelectData.html?code='+provinceId;
                $.post(url, function(result1) { //二级联动
                    $("#city").combobox({
                        data : result1,
                        valueField:'code',
                        textField:'name',
                        cache:false,
                        editable:false,
                        onLoadSuccess: function (data) { //加载完成后,设置选中的项
                            $(this).combobox("select",cityCode);
                        },
                        onSelect:function(record){
                            var cityId = record.code;
                            $("#cityCode").val(cityId);
                            $("#districtCode").val("");
                            var url = '/ums/communityAction!getChildSelectData.html?code='+cityId;
                            $.post(url,function(result2) { //三级联动
                                $("#area").combobox({
                                    data : result2,
                                    valueField:'code',
                                    textField:'name',
                                    cache:false,
                                    editable:false,
                                    onLoadSuccess: function () { //加载完成后,设置选中的项
                                    	 $(this).combobox("select",districtCode);
                                    },
                                    onSelect:function(record){
                                    	var districtCode = record.code;
                                        $("#districtCode").val(districtCode);
                                    }
                                });
                            },'json');
                        }
                    });
                },'json');
            }
        });
    },'json');
    $.ajaxSettings.async = true;
}
function cancelEdit(){
	window.location.href = "/grms/customerManagement/list.html";
}


function addCust(){
	var num=0;
	$("input[type$='text']").each(function(n){
        if($(this).val()==""){
       	 $(this).next(".redText").show()
             num++;
        }else{
       	 $(this).next(".redText").hide()
        }
    });
	if (num>0) {
		infoMask("还有必填项未填!")
    	return;
	}else{
		var id = $("#detailId").val();
		var name = $("#companyName").val();
		
    	var province = $("#province").combobox('getValue'); 
    	var provinceName = $("#province").combobox('getText'); 
    	var city = $("#city").combobox('getValue'); 
    	var cityName = $("#city").combobox('getText');
    	var area = $("#area").combobox('getValue'); 
    	var areaName = $("#area").combobox('getText');
    	var address = $("#address").val();
    	//var companyType = document.getElementById("companyType").value;
    	var companyType = $("#companyType").val();
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
		
		if (a && b) {
			$.ajax({
				type : "post",
				url : "/grms/customerManagement/save.html",
				dataType : "json",
				data : {"id":id,"name":name,"industry":industry,"customerTypeId":companyType,
					"department":department,"dutyName":dutyName,"dutyPhone":dutyPhone,
					"contactor":contactor,"contactorPhone":contactorPhone,"remark":remark,
					"province":province,"provinceName":provinceName,"city":city,"cityName":cityName,
					"address":address,"area":area,"areaName":areaName},
					success : function(result) {
						if (result.succ) {
							
							infoMask("修改成功!")
							setTimeout(function () {
								window.location.href = "/grms/customerManagement/list.html";
							}, 1200);
						}else{
							infoMask("修改失败:  服务器响应异常")
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
