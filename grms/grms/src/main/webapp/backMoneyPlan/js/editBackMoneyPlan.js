var i = $(".newTemplate").length+1;
var addSure = true;
//初始化按钮
$(function(){
	$(".newTemplate").each(function(index,element){
		type=$(element).find("label input:checked").val();
		if(type==2){//有结算款
			addSure = false;
		}
	});
	
	$("input[type='radio']").each(function(){
	    if($(this).attr("checked") == "checked"){ 
	    	console.log(1);
	    	$(this).parent().addClass("radio-bg");
	    }
	});
	
	$(".newTemplate .planTimes").datebox({
		onSelect : function(beginDate){
        	var nextTime = $(this).parent().parent().parent().parent().next().find(".planTimes");
        	//$(this).parent().parent().parent().parent().nextAll().find('.planTimes').eq(0).datebox('');
        	var nextAll = $(this).parent().parent().parent().parent().nextAll();
        //	console.log(nextTime);
        	//console.log(nextTime);
        	nextAll.each(function(index,ele){
        		var num = index
        		$(ele).find(".planTimes").eq(0).datebox('setValue', '');       		       		      		
        	})
        	if(nextTime.length > 0){
        		nextTime.datebox('calendar').calendar({ 
                    validator: function(date){
                        return beginDate<=date;
                    }
                });  
        	}       	  	
        }
	}); 
	$(".newTemplate .planTimes").each(function(index,ele){
		$(".newTemplate .table-wrap").find(".textbox-text,a").click(function(e){
			var thisEleID=$(this);
			if($(this)[0].tagName=="INPUT"){
				$(".newTemplate .table-wrap .datebox .textbox-text").each(function(index,ele){
	        		if($(ele).attr("id")==thisEleID.attr("id")){
	        			dataBoxClickIndex=index;
	        			return false;
	        		}     		       		      		
	        	})
			}else{
				$(".newTemplate .table-wrap .datebox .textbox-text").each(function(index,ele){
	        		if($(ele).attr("id")==thisEleID.parent().next().attr("id")){
	        			dataBoxClickIndex=index;
	        			return false;
	        		}  		       		      		
	        	})
			}
		});
		dataBoxClickIndex=-1;
		if($('.newTemplate .planTimes').eq(-2).length > 0&&index>=1){
			if($('.newTemplate .planTimes').eq(-2).datebox('getValue')!=''&&$('.newTemplate .planTimes').eq(-2).datebox('getValue')!=null&&$('.newTemplate .planTimes').eq(-2).datebox('getValue')!='underfide'){
				$('.newTemplate .planTimes').eq(index).datebox('calendar').calendar({
					validator: function(date){
						var now = new Date();						
						var indexT=dataBoxClickIndex=='-1'?(index-1):(dataBoxClickIndex-1);
						var timeStaI=$('.newTemplate .planTimes').eq(indexT).datebox('getValue');
						var timeStaI2=timeStaI.split(' ')[0].split('-')
			        	var d1 =new Date(timeStaI2[0],timeStaI2[1]-1,timeStaI2[2])
						return d1<=date;
					}
				});
			}
		}	
		
	})
/*	if($('.newTemplate .planTimes').eq(-2).length > 0){
		if($('.newTemplate .planTimes').eq(-2).datebox('getValue')!=''&&$('.newTemplate .planTimes').eq(-2).datebox('getValue')!=null&&$('.newTemplate .planTimes').eq(-2).datebox('getValue')!='underfide'){
			$('.newTemplate .planTimes').last().datebox('calendar').calendar({
				validator: function(date){
					var now = new Date();
					var timeStaI=$('.newTemplate .planTimes').eq(-2).datebox('getValue');
					var timeStaI2=timeStaI.split(' ')[0].split('-')
		        	var d1 =new Date(timeStaI2[0],timeStaI2[1]-1,timeStaI2[2])
					return d1<=date;
				}
			});
		}
	}*/	
	
	
});


//提交
function submit(){
	setTimeout(function(){
		submitAjax();
	},500);
}

var saveFlag = true;
function submitAjax(){
	if(!saveFlag){
		return;
	}
	saveFlag=false;
	var param = packageFrom();
	var flag = false;
	var timeFlag = true;
	var allMoney=parseFloat(0);
	$(".newTemplate").each(function(index,element){
		//结算款
		type=$(element).find("label input:checked").val();
		if(type==2){
			flag = true;
		}
		//收款时间
		planBackTime =$(element).find('.planTimes').eq(0).datebox('getValue');
		if(planBackTime==""){//时间为空不能提交
			timeFlag = false;
		}
		//收款金额
		planBackMoney=parseFloat($(element).find('.planMoneys').val());
		allMoney = allMoney+planBackMoney;
	});
	
	   var money = parseFloat($("#money").val());
	   if(!timeFlag){
		   infoMask('时间为空，不能提交');
		   return;
	   }
	   if(!flag){
		   infoMask('无结算款，无法提交');
		   return;
	   }
	   if(allMoney.toFixed(2)!=money.toFixed(2)){
		   infoMask('应收款总和与合同总金额不一致');
		   return;
	   }
	
			var contractCode = $("#contractCode").val();
			$.ajax({  
		        type : "POST",  //提交方式  
		        url : "/grms/backMoneyPlan/editBackMoneyPlan",//路径  
		        dataType : "json",//数据，这里使用的是Json格式进行传输  
		        data: JSON.stringify(param),  
		        contentType:'application/json;charset=utf-8',
		        success : function(result) {//返回数据根据结果进行相应的处理  
		            if ( result.success) {  
		            	infoMask('修改成功');
		            	saveFlag = true;
		            	window.location.href="/grms/backMoneyPlan/list.html";
		            } else {  
		            	saveFlag = true;
		            	infoMask('修改失败');
		            }  
		        }  
		    }); 
}

//删除模板
function removeTemplate(str,e){
	$("#newTemplate"+str).remove();
	
	$(".newTemplate").each(function(index,element){
		var indexo = index+1;
		$(element).find("span[name='wrap-span']").html("第"+indexo+"期：");	
	});
	
	i--;
	infoMask('模板删除成功');
	if($(e).parents(".newTemplate").find(".checkedlabel input").is(":checked")){
		addSure = true;
	}
}


function moneyBlur(e){
	var money = parseFloat($("#money").val());
	if(!addSure){
		var planMoneysLength = $(".planMoneys").length;
		$(".planMoneys").each(function(index,ele){
			if(planMoneysLength-1 != index){
				var inputmoney = $(ele).val();
				if($(ele).val() == ''){
					inputmoney = 0;
				}
				money = money - inputmoney;
			}
		});
		console.log(money);
		var newTemplateLength = $(".newTemplate").length;
		if($(".newTemplate").eq(length-1).find(".checkedlabel input").is(":checked")){
			$(".newTemplate").eq(length-1).find(".planMoneys").attr("readonly","").val(money.toFixed(2));
		}
	}
}
var dataBoxClickIndex=-1;//时间选择器控制因素
//继续添加模板
function copyTemplate(){
	if(!addSure){
		infoMask('已有结算款，无法添加新模板');
		return;
	}
	
	var addFlag = true;
	$(".newTemplate").each(function(index,ele){
		var dateboxValue = $(ele).find(".planTimes").eq(0).datebox('getValue');
		var planMoneysValue = $(ele).find(".planMoneys").val();
		var labelFlag = false;
		$(ele).find("label input").each(function(){
			if($(this).is(":checked")){
				labelFlag = true;
			}
		})
		if(dateboxValue == '' || planMoneysValue == "" || !labelFlag){
			addFlag = false;
		}
	});
	
	if(!addFlag){
		infoMask('请完善收款计划信息，否则无法添加新模板');
		addFlag = true;
		return false;
	}
	
	var htl = '';
	htl += '<div class="newTemplate" id="newTemplate'+i+'">';
	htl += '<div class="table-wrap">';
	htl +=	'<div  class="table-wrap-a">';
	htl +=		'<span class="wrap-span" name="wrap-span">第'+i+'期：</span>';
	htl +=		'<span><a href="javascript:void(0);" onclick="copyTemplate()" id="addDiv'+i+'">继续添加</a></span>';
	htl +=		'<span  style="margin-left: 20px;"><a href="javascript:void(0);" onclick="removeTemplate('+i+',this)">删除</a></span>'	;
	htl +=	'</div>	';
	htl +=  '</div>';
	htl += '<div class="table-wrap">';
	htl +=	'<div>';
	htl +=		'<span class="wrap-span">款项属性：</span>';
	htl +=		'<span>';
	htl +=			'<label class="radio-icon" ><input type="radio" value="1" onclick="changeType('+1+','+i+',this)" class="planType" name="planType'+i+'"/>进度款</label>';
	htl +=			'<label class="radio-icon checkedlabel" style="margin-left: 20px;"><input type="radio" value="2" onclick="changeType('+2+','+i+',this)"  name="planType'+i+'" class="planType" />结算款</label>';
	htl +=		'</span>	';
	htl +=	'</div>	';
	htl += '</div>	';
	htl += '<div class="table-wrap">';
	htl +=	'<div>';
	htl +=		'<span class="wrap-span">计划收款时间：</span>';
	htl +=		'<span>';
	htl +=		'	<input id="planTime" name="planTime" style="height: 34px;width: 183px;border: 1px solid #ccc;" type="text" class="easyui-datebox planTimes planTimes'+i+'"/>';
	htl +=		'</span>	';
	htl +=	'</div>	';
	htl += '</div>	';
	htl += '<div class="table-wrap">';
	htl += 	'<div>';
	htl +=		'<span  class="wrap-span">计划收款金额：</span>';
	htl +=		'<span>';
	htl +=			'<input id="planMoney'+i+'" onBlur="moneyBlur(this)" style="height: 32px;width: 180px;border: 1px solid #ccc;" class="planMoneys" name="planMoney" type="text"/>';
	htl +=		'</span>	';
	htl +=	'</div>	';
	htl += '</div>	';
	htl += '<div class="table-wrap"  style="padding-bottom: 20px;">';
	htl +=	'<div>';
	htl +=	'	<span  class="wrap-span">备注：</span>';
	htl +=		'<span>';
	htl +=		'	<input  style="height: 32px;width: 180px;border: 1px solid #ccc;" class="remarks" id="remark" name="remark" type="text"/>';
	htl +=		'</span>	';
	htl +=	'</div>	';
	htl +=	'</div>	';
	
	var targetObj = $("#template").append(htl);
	//$.parser.parse(targetObj);
	var selsctI=i-1;
	
	$('.newTemplate .planTimes').last().datebox({
		onSelect : function(beginDate){
        	var nextTime = $(this).parent().parent().parent().parent().next().find(".planTimes");
        	//$(this).parent().parent().parent().parent().nextAll().find('.planTimes').eq(0).datebox('');
        	var nextAll = $(this).parent().parent().parent().parent().nextAll();
        //	console.log(nextTime);
        	
        	nextAll.each(function(index,ele){
        		var num = index
        		$(ele).find(".planTimes").eq(0).datebox('setValue', '');       		       		      		
        	})
        	if(nextTime.length > 0){
        		
        		nextTime.datebox('calendar').calendar({ 
                    validator: function(date){
                        return beginDate<=date;
                    }
                });  
        	}
        	  	
        }
	}); 
	$('.newTemplate .planTimes').last().next().find(".textbox-text,a").click(function(e){
		var thisEleID=$(this);
		if($(this)[0].tagName=="INPUT"){
			$(".newTemplate .table-wrap .datebox .textbox-text").each(function(index,ele){
        		if($(ele).attr("id")==thisEleID.attr("id")){
        			dataBoxClickIndex=index;
        			return false;
        		}     		       		      		
        	})
		}else{
			$(".newTemplate .table-wrap .datebox .textbox-text").each(function(index,ele){
        		if($(ele).attr("id")==thisEleID.parent().next().attr("id")){
        			dataBoxClickIndex=index;
        			return false;
        		}  		       		      		
        	})
		}
	});
	dataBoxClickIndex=-1;
	if($('.newTemplate .planTimes').eq(-2).length > 0){
		if($('.newTemplate .planTimes').eq(-2).datebox('getValue')!=''&&$('.newTemplate .planTimes').eq(-2).datebox('getValue')!=null&&$('.newTemplate .planTimes').eq(-2).datebox('getValue')!='underfide'){
			$('.newTemplate .planTimes').last().datebox('calendar').calendar({
				validator: function(date){
					var now = new Date();
					var indexT=dataBoxClickIndex=='-1'?(-2):(dataBoxClickIndex-1);
					var timeStaI=$('.newTemplate .planTimes').eq(indexT).datebox('getValue');
					var timeStaI2=timeStaI.split(' ')[0].split('-')
		        	var d1 =new Date(timeStaI2[0],timeStaI2[1]-1,timeStaI2[2])
					return d1<=date;
				}
			});
		}
	}	
	/*timeCheck();*/
	i++;	
	/*$(".newTemplate").each(function(index,element){
		var indexo = index+1;
		$(element).find("span[name='wrap-span']").html("第"+indexo+"期：");	
	});*/
	
	$(".newTemplate").each(function(index,element){
		var indexo = index+1;
		$(element).find("span[name='wrap-span']").html("第"+indexo+"期：");	
	});
}


//获取表单数据并封装
function packageFrom(){
	//收款计划信息
	var listLink=[];
	var checkArr = [];
	console.log(checkArr);
	
	$(".newTemplate").each(function(index,element){
		var plan={};		
		type=$(element).find("label input:checked").val();	
		planBackTime =$(element).find('.planTimes').eq(0).datebox('getValue');
		planBackMoney=$(element).find('.planMoneys').val();
		remark       =$(element).find('.remarks').val();
		backMoneyPlanCode = $("#backMoneyPlanCode").val();
		console.log(index);
		id = $("#id"+index).val();
		plan.type=type;
		plan.planBackTime=planBackTime;
		plan.planBackMoney=planBackMoney;
		plan.remark=remark;
		plan.backMoneyPlanCode = backMoneyPlanCode;
		plan.id = id;
		listLink.push(plan);
	});
	console.log(listLink);
	return listLink;
}


function changeType(s,n,e){
console.log(s);
    var money = parseFloat($("#money").val());
	setTimeout(function(){
		$("input[type='radio']").each(function(){
			if($(this).is(':checked') == true){
				$(this).parent().addClass("radio-bg");
			}else{
				$(this).parent().removeClass("radio-bg");
			}
		});
	},200);
	
	if(s=='1'){//进度款
		$("#addDiv"+n).attr("hidden",false);
		/*$("#planMoney"+n).val(money).attr("readonly",false);*/
		addSure = true;
		$(e).parents(".newTemplate").find(".planMoneys").removeAttr("readonly");
		
	}else if(s=='2'){//结算款
		
		
		var newTemplatePrevAll = $(e).parents(".newTemplate").prevAll();
		
		if(newTemplatePrevAll.length >= 1){
			newTemplatePrevAll.each(function(index,element){
				var planBackMoney = parseFloat($(element).find('.planMoneys').val());
				if($(element).find('.planMoneys').val() == ''){
					planBackMoney = 0;
				}
				console.log(planBackMoney)
				console.log(money)
				money = money - planBackMoney;
				
			});
		}

		$(e).parents(".newTemplate").find(".planMoneys").val(money.toFixed(2)).attr("readonly","readonly");
		moneyFlag = money;
		
		$("#addDiv"+n).attr("hidden",true);
		
		var nextAll = $(e).parent().parent().parent().parent().parent().nextAll();
		nextAll.each(function(e,ele){
			$(ele).remove();
		})
		console.log($(e));
		i=parseInt(n)+1;
		addSure = false;
	}
}

//timeCheck();
function timeCheck(){
	$(".newTemplate .planTimes").datebox({
        onSelect : function(beginDate){
        	var nextTime = $(this).parent().parent().parent().parent().next().find(".planTimes");
        	//$(this).parent().parent().parent().parent().nextAll().find('.planTimes').eq(0).datebox('');
        	var nextAll = $(this).parent().parent().parent().parent().nextAll();
        	nextAll.each(function(index,ele){
        		var num = index
        		//$(ele).find(".planTimes").eq(0).datebox('setValue', '');
        		console.log(index);
        		console.log($(ele));
        		
        	})
        
        	nextTime.datebox('calendar').calendar({ 
                validator: function(date){
                    return beginDate<=date;
                }
            });
        }
    });

}
	
function cancel(){
	window.history.go(-1);
}

