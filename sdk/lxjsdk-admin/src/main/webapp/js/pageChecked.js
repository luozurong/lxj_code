/**
 * 分页复选思路：
 *		第一种情况：如果是点击左边菜单目录点击后，弹出的frmright页面中需要分页复选，则需要将左侧栏的body作为容器块存放临时值，
 *		此时会在容器块中添加一个_pageChecked_div_*(*号是要分页的table的id),
 *  	的div,并将复选框的值保存在id=_pageChecked_val_*的input中，选中的文本的值保存在id=_pageChecked_text_*个input中；
 *  	这种情况下每次点击左侧菜单时要清空容器块，在uaas项目的left.jsp绑定了一个点击事件
 *		必传参数：tableId，checkBoxName,isNeedText,flag = 1;
 *		
 *		第二种情况，如果是从frmright页面中点击一个按钮，弹出一个新窗口，新窗口需要分页复选，
 * 		此时容器块是分页窗口的标题div，在容器块中添加一个_pageChecked_div_*(*号是要分页的table的id)的div,
 *		并将复选框的值保存在id=_pageChecked_val_*的input中，选中的文本的值保存在id=_pageChecked_text_*个input中
 *		点击确定，将临时的值赋给frmright的对应输入框中，如果需要显示text,isNeedText传true,且需要传targetTextEleId
 *		接收文本值
 *		必传参数：tableId，currDialogID，parentDialogID = 'frmright'，targetValueEleId，flag = 2
 * 				checkBoxName,isNeedText
 * 
 *		第三种情况：如果是从已弹出的窗口中，在点击一个按钮，弹出一个新窗口，新窗口需要分页复选，
 *		此时的容器块是分页窗口的标题div，在容器块中添加一个_pageChecked_div_*(*号是要分页的table的id)的div,
 *		并将复选框的值保存在id=_pageChecked_val_*的input中，选中的文本的值保存在id=_pageChecked_text_*个input中
 *		点击确定，将临时的值赋给parentDialogID的对应输入框中，如果需要显示text,isNeedText传true,且需要传
 *		targetTextEleId接收文本值
 *		必传参数：tableId，currDialogID，parentDialogID，targetValueEleId，flag = 3
 *				checkBoxName,isNeedText
 * **/

/**
 *  提供给js页面调用的方法
 *  1、	_pageChecked(pageCheckedParam);
 *  	说明    ：  渲染当前页面的分页复选
 *  	参数说明 ：  js对象，有多个参数字段构成，详情见下面方法注释
 *  
 *  
 * 	2、	_setcheckedValue();
 * 		说明    ：  传递临时值到对应的目标输入框内，如果没有选中，返回false,如果选中，将值传递后返回true
 * 		参数说明 ：  无
 * 
 * 
 *  3、	_getCheckedResult();
 * 		说明    ：  获当前已选中的复选框的value和text，返回一个obj对象，{value:"",text:""}
 * 		参数说明 ：  无
 * 
 * 
 *  4、	_clearChecked();
 * 		说明    ：  清楚已选
 * 		参数说明 :   无
 * **/



	var _vesselDiv ;//用来存放临时块的容器元素  以下称 容器块
	var _divEleTemp ;//存放临时数据div,层级为：<div><input/><input/></div>  以下称   临时块 
	var _divEle_id ;//存放临时数据div的id   
	var _valueEleTemp_id ;//存放选中复选框的value 的临时input的id
	var _textEleTemp_id ;//存放选中复选框的value 的临时input的id
	var paramsObj;//参数对象
	
	/**	渲染分页复选
		@author liuqinghui
		@date 2016-12-28
		@param tableId 要分页复选的table 必传
		@param currDialogID 当前分页窗口的dialog的id 必传(第一种情况时是frmright的标题栏的div的id)
		@param parentDialogID 父窗口的dialog的id 如果分页复选窗口没有父窗口，可不传
		@param colNum 勾选复选框后，取第colNum列内的文本内容，从1开始    不需要获取text时可不传 
		@param targetValueEleId 父页面中存放复选框value的元素id	
		@param targetTextEleId 父页面中存放复选框对应文本的元素id	不需要获取text时可不传 
		@param checkBoxName 复选框名称，必传
		@param isNeedText 是否需要text值  boolean型  不需要获取text时可不传为false,否则为true
		@param flag 1,2,3种不同的情况，对应思路中情况描述 必传
	**/
	function _pageChecked(pageCheckedParam){
		paramsObj = pageCheckedParam;
		var tableId = pageCheckedParam.tableId;
		var currDialogID = pageCheckedParam.currDialogID;
		var parentDialogID = pageCheckedParam.parentDialogID;
		var colNum = pageCheckedParam.colNum;
		var targetValueEleId = pageCheckedParam.targetValueEleId;
		var targetTextEleId = pageCheckedParam.targetTextEleId;
		var checkBoxName = pageCheckedParam.checkBoxName;
		var flag = pageCheckedParam.flag;
		var isNeedText = pageCheckedParam.isNeedText;
		
		_divEle_id = "_pageChecked_div_" + tableId;
		_valueEleTemp_id = "_pageChecked_val_" + tableId;
		_textEleTemp_id = "_pageChecked_text_" + tableId;
		
		var isCreate = _createDivEle(tableId,currDialogID,flag);//创建临时块   此时有了临时块
		
		if (isCreate && (flag == 2 || flag == 3)){//如果重新创建了临时块,需要将真实值赋给临时值，保证默认选中
			_initTempValue(parentDialogID,targetValueEleId,targetTextEleId,isNeedText,flag);//此时初始化了临时块的内容
		}
		
		_checkedBindEvent(checkBoxName,isNeedText,colNum);//给复选框绑定事件
		_allCheckedBindEvent(tableId,checkBoxName,isNeedText,colNum);//给全选按钮绑定事件
		
		_initChecked(checkBoxName);//默认选中复选框
		
	}
	
	/**
		创建临时块，如果已有，则不创建，返回false,如果没有，则创建，返回true
		@author liuqinghui
		@date 2016-12-28
	**/
	function _createDivEle(tableId,currDialogID,flag){
		//获得容器块，临时块将append在容器块中，这样分页跳转时不消失，窗口关闭消失
		if (flag == 1){//第一种情况，容器块为左边菜单栏的body
			var cWin = top.document.getElementById("frmleft").contentWindow
			_vesselDiv = cWin.document.body;
			_divEleTemp = cWin.document.getElementById(_divEle_id);
		}
		else if (flag == 2 || flag == 3){//第二、三种情况，容器块为标题栏div
			_vesselDiv = top.document.getElementById("_DialogDiv_" + currDialogID);
			_divEleTemp = top.document.getElementById(_divEle_id);
		}
		
		var _valueEleTemp;//存放临时复选框value的input元素
		var _textEleTemp;//存放临时复选框text的input元素divEle")
		
		if (_divEleTemp == null){//如果此时没有临时块,则新建
			_divEleTemp = document.createElement("div");
			_valueEleTemp = document.createElement("input");
			_textEleTemp = document.createElement("input");
			
			$(_valueEleTemp).attr("type","hidden");
			$(_textEleTemp).attr("type","hidden");
			$(_divEleTemp).attr("id",_divEle_id);
			$(_valueEleTemp).attr("id",_valueEleTemp_id);
			$(_textEleTemp).attr("id",_textEleTemp_id);
			
			$(_divEleTemp).append(_valueEleTemp);
			$(_divEleTemp).append(_textEleTemp);
			
			$(_vesselDiv).append(_divEleTemp);//将临时值保存到分页窗口的容器块中，这样点击下一页时不会刷新
			
			return true;
		}
		return false;
	}
	
	/**
		初始化临时值的时候，需要将父窗口的真实值赋给临时值，目的是为遍历临时值来默认选中
		@author liuqinghui
		@date 2016-12-29
	**/
	function _initTempValue(parentDialogID,targetValueEleId,targetTextEleId,isNeedText,flag){
		var parentDiagWindow ;
		var trueCheckedVal = '';//父页面保存的当前实际值
		var trueCheckedText = '';//父页面保存的当前实际选中值对应的text
		var tempCheckedVal = '';//临时保存的当前已选中值拼接字符串
		var tempCheckedText = '';//父页面临时保存的当前已选中复选框对应text字符串
		if (flag == 2){//如果在第二种情况下创建了临时块，此时父窗口是frmright窗口
			parentDiagWindow = top.document.getElementById(parentDialogID).contentWindow.document
		}
		else if (flag == 3){//如果在第三种情况下创建了临时块，此时父窗口是上一层dialog窗口
			parentDiagWindow = top.document.getElementById("_DialogFrame_" + parentDialogID).contentWindow.document
		}
		
		trueCheckedVal = parentDiagWindow.getElementById(targetValueEleId).value
		tempCheckedVal = trueCheckedVal != '' ? "," + deleteSpace(trueCheckedVal) : '';//父页面临时保存的当前已选中值拼接字符串
		if (isNeedText){
			trueCheckedText = parentDiagWindow.getElementById(targetTextEleId).value;
			tempCheckedText = trueCheckedText != '' ? "," + deleteSpace(trueCheckedText) : '';//父页面临时保存的当前已选中复选框对应text字符串
		}
		
		$(_divEleTemp).find("#"+_valueEleTemp_id).val(tempCheckedVal);//将临时值保存到临时标签中
		$(_divEleTemp).find("#"+_textEleTemp_id).val(tempCheckedText);//将临时text保存到临时标签中
	}
	
	/**
		初始化选中，即如果原来真实选中了第一、三行，则一进入，第一、三需要默认是勾选中的
		@author liuqinghui
		@date 2016-12-29
	**/
	function _initChecked(checkBoxName){
		var tempCheckedVal = $(_divEleTemp).find("#"+_valueEleTemp_id).val();//临时保存的当前已选中值拼接字符串
		
		if (tempCheckedVal != ""){
			tempCheckedVal = tempCheckedVal.substring(1,tempCheckedVal.length).trim();
			tempCheckedVal = deleteSpace(tempCheckedVal);//去空格
			var valArray = tempCheckedVal.split(",");//已选中的数组
			var currPageBox = $("input[name='" + checkBoxName + "']");//当前页的复选框
			
			$(valArray).each(function(i,obj){
				for (var b = 0; b < currPageBox.length; b++){
					if (obj != "" && obj.trim() == $(currPageBox[b]).val()){
						$(currPageBox[b]).attr('checked',true);
						return ;
					}
				}
			})
		}
		
		
	}
	
	/**
		给复选框绑定点击事件,选中后将将值保存在临时值中，勾选后从临时值中去掉
		@author liuqinghui
		@date 2016-12-29
	*/
	function _checkedBindEvent(checkBoxName,isNeedText,colNum){
		$("input[name='" + checkBoxName + "']").bind('click',function(){
			var tempCheckedVal = $(_divEleTemp).find("#"+_valueEleTemp_id).val();//临时保存的当前已选中值拼接字符串
			var tempCheckedText = $(_divEleTemp).find("#"+_textEleTemp_id).val();//临时text保存到临时标签中
			
			if ($(this).attr('checked') == true){
				tempCheckedVal = tempCheckedVal + "," + $(this).val();
				if (isNeedText){
					var currTr = $(this).parents("tr");
					var tds = $(currTr).find("td");
					var tdVal = $(tds[colNum-1]).text();
					tempCheckedText = tempCheckedText + "," + tdVal;
					
				}
			}
			else{
				tempCheckedVal = tempCheckedVal.replace("," + $(this).val(),'');
				if (isNeedText){
					var currTr = $(this).parents("tr");
					var tds = $(currTr).find("td");
					var tdVal = $(tds[colNum-1]).text();
					tempCheckedText = tempCheckedText.replace("," + tdVal,'');
				}
			}
			
			$(_divEleTemp).find("#"+_valueEleTemp_id).val(tempCheckedVal);//将临时值保存到临时标签中
			$(_divEleTemp).find("#"+_textEleTemp_id).val(tempCheckedText);//将临时text保存到临时标签中
		})
	}
	
	/**
		点击全选时，获取当前页所有的区域的序列号,循环去掉或添加到父页面的已选择区域的标签内 
		@author liuqinghui
		@date 2016-12-29
	**/
	function _allCheckedBindEvent(tableId,checkBoxName,isNeedText,colNum){
		$("#" + tableId).find("img[class='hand']").bind('click',function(){
			var tempCheckedVal = $(_divEleTemp).find("#"+_valueEleTemp_id).val();//临时保存的当前已选中值拼接字符串
			var tempCheckedText = $(_divEleTemp).find("#"+_textEleTemp_id).val();//临时text保存到临时标签中
			
			$("input[name='" + checkBoxName + "']").each(function(){
			    if($(this).attr("checked") == true){
			    	if (tempCheckedVal.indexOf(this.value) == -1){//如果未存在
			    		tempCheckedVal = tempCheckedVal + "," + this.value;
			    		
			    		if (isNeedText){
							var currTr = $(this).parents("tr");
							var tds = $(currTr).find("td");
							var tdVal = $(tds[colNum-1]).text();
							tempCheckedText = tempCheckedText + "," + tdVal;
							
						}
			    	}
			    }
			    else{
			    	tempCheckedVal = tempCheckedVal.replace("," + this.value,'');
			    	
			    	if (isNeedText){
						var currTr = $(this).parents("tr");
						var tds = $(currTr).find("td");
						var tdVal = $(tds[colNum-1]).text();
						tempCheckedText = tempCheckedText.replace("," + tdVal,'');
					}
			    }
			});
			
			$(_divEleTemp).find("#"+_valueEleTemp_id).val(tempCheckedVal);//将临时值保存到临时标签中
			$(_divEleTemp).find("#"+_textEleTemp_id).val(tempCheckedText);//将临时text保存到临时标签中
		})
		
	}
	
	/**
		传递临时值到对应的目标输入框内，目标输入框的id在初始渲染时（调用_pageChecked()）时传入的targetValueEleId
		如果没有选中，返回false,如果选中，将值传递后返回true
		@author liuqinghui
		@date 2016-12-29
	**/
	function _setcheckedValue(){
		var parentDialogID = paramsObj.parentDialogID;
		var targetValueEleId = paramsObj.targetValueEleId;
		var targetTextEleId = paramsObj.targetTextEleId;
		var checkBoxName = paramsObj.checkBoxName;
		var flag = paramsObj.flag;
		var isNeedText = paramsObj.isNeedText;
		
		var parentDiagWindow;
		if (flag == 2){//如果在第二种情况，此时父窗口是frmright窗口
			parentDiagWindow = top.document.getElementById(parentDialogID).contentWindow.document
		}
		else if (flag == 3){//如果在第三种情况下，此时父窗口是上一层dialog窗口
			parentDiagWindow = top.document.getElementById("_DialogFrame_" + parentDialogID).contentWindow.document
		}
		
		var tempCheckedVal = $(_divEleTemp).find("#"+_valueEleTemp_id).val();//临时值保存到临时标签中
		var tempCheckedText = $(_divEleTemp).find("#"+_textEleTemp_id).val();//临时text保存到临时标签中
		tempCheckedVal = tempCheckedVal.substring(1,tempCheckedVal.length);//去掉逗号
		tempCheckedText = tempCheckedText.substring(1,tempCheckedText.length);//去掉逗号
		
		if (tempCheckedVal != null && tempCheckedVal != ''){
			parentDiagWindow.getElementById(targetValueEleId).value = tempCheckedVal;//将临时值保存到临时标签中
			if (isNeedText){
				parentDiagWindow.getElementById(targetTextEleId).value = tempCheckedText;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 去掉空格
	 * @author liuqinghui
	 * @param spaceStr
	 */
	function deleteSpace(spaceStr){
		spaceStr = spaceStr.replace(/\s/g,"");//去除文章中间空格
		return spaceStr;
	}
	
	/**
		获取当前已选中的复选框的value和text
		@author liuqinghui 
		@date 2016-12-30
	**/
	function _getCheckedResult(){
		var tempCheckedVal = $(_divEleTemp).find("#"+_valueEleTemp_id).val();//临时值保存到临时标签中
		var tempCheckedText = $(_divEleTemp).find("#"+_textEleTemp_id).val();//临时text保存到临时标签中
		tempCheckedVal = tempCheckedVal.substring(1,tempCheckedVal.length);//去掉逗号
		tempCheckedText = tempCheckedText.substring(1,tempCheckedText.length);//去掉逗号
		
		var checkedResult = null;//已选中的值
		if (tempCheckedVal != null && tempCheckedVal != ''){
			
			checkedResult = {value:tempCheckedVal,text:tempCheckedText}
		}
		return checkedResult;
	}
	
	/**
		清空已选择的复选框
	**/
	function _clearChecked(){
		var checkBoxName = paramsObj.checkBoxName;
		$(_divEleTemp).find("#"+_valueEleTemp_id).val('');//将临时值设为空
		$(_divEleTemp).find("#"+_textEleTemp_id).val('');//临时text设为空
		
		var currPageBox = $("input[name='" + checkBoxName + "']");//当前页的复选框
		for (var b = 0; b < currPageBox.length; b++){
			$(currPageBox[b]).attr('checked',false);
		}
		
	}