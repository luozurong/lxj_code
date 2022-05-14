//var localhost = 'http://192.168.51.4:8080/';//本地调试
var localhost = '/';//正式
var selNum = 0;//小区选中数量
var total = 0;//小区总数
var radioCommunityPageSize=10;
var pageNumber=1;
var pageDataUrl="/grms/areaUserDataController/getAreaInfoPages"
	
	function resetSearch(){
	$("#province_selt").combobox('setValue','');
	$("#city_selt").combobox('setValue','');
	$("#area_selt").combobox('setValue','');
	$("#town_selt").combobox('setValue','');
	$("#areaType").combobox('setValue','');
	$("#keyword").searchbox('setValue','');
	$("input[name='translateStatus']").attr("checked", false);
	$("#areaSingleCheckboxInfos").datagrid('unselectAll');
}

function initPageInfos(){
	$('#areaInfospp').pagination({
	    layout:['list','first','prev','links','next','last','manual'],
	    emptyMsg: '<span>无记录</span>',
	    showRefresh:true,
	    displayMsg:' ',
	    pageList:[10,20,30],
	    onSelectPage:function (pageNo, pageSize) {
	    	pageNumber = pageNo;
	    	pageSize = pageSize;
	    	searchPageData(pageNo,pageSize);
	    }
	});
}

function searchData(){
	searchPageData(1, radioCommunityPageSize);
}
/**
 * 数据封装并请求响应数据
 * @param pageNo
 * @param pageSize
 */
function searchPageData(pageNo, pageSize){
	var provinceVal = $('#province_selt').val()=='请选择'?"": $('#province_selt').val();
	var cityVal = $('#city_selt').val()=='请选择'?"":$('#city_selt').val() ;
	var areaVal = $('#area_selt').val()=='请选择'?"":$('#area_selt').val();
	var areaCategory =$('#areaType').combobox('getValue');
	var townVal =$('#town_selt').val()=='请选择'?"":$('#town_selt').val();
	var keyword = $('#keyword').val();
	var translate = new Array($("input:checked").size());
	$("input[name='translateStatus']:checked").each(function(index,element){
		translate[index]=$(element).val();
	});
	var areaQueryBean = new Object();
	areaQueryBean.pageNumber = pageNumber;
	areaQueryBean.pageSize = pageSize;
	areaQueryBean.province =provinceVal;
	areaQueryBean.city=cityVal;
	areaQueryBean.country=areaVal;
	areaQueryBean.translateStatus=translate;
	areaQueryBean.areaName=keyword;
	areaQueryBean.areaCategory=areaCategory;
	areaQueryBean.town=townVal;
   $("#areaSingleCheckboxInfos").datagrid("reload", {
	   "province":areaQueryBean.province,
	   "city":areaQueryBean.city,
	   "country":areaQueryBean.country,
	   "town":areaQueryBean.town,
	   "translateStatus[0]":areaQueryBean.translateStatus[0]?areaQueryBean.translateStatus[0]:null,
		"translateStatus[1]":areaQueryBean.translateStatus[1]?areaQueryBean.translateStatus[1]:null,			
		"translateStatus[2]":areaQueryBean.translateStatus[2]?areaQueryBean.translateStatus[2]:null,						
	   "areaName":areaQueryBean.areaName,
	   "areaCategory":areaQueryBean.areaCategory,
	   "pageNumber":areaQueryBean.pageNumber,
	   "pageSize":areaQueryBean.pageSize
	});   	
}

function initDataGrid(){
	var windowheight = $(window).height();
	var tableheight = 0;
	if(windowheight<500){
		tableheight = 255;
	}else{
		tableheight = 355;
	}
	var areaQueryBean = new Object();
	areaQueryBean.pageNumber = 1;
	areaQueryBean.pageSize = radioCommunityPageSize;
	areaQueryBean.province ="";
	areaQueryBean.city="";
	areaQueryBean.country="";
	areaQueryBean.town="";
	areaQueryBean.translateStatus=[];
	areaQueryBean.areaName="";
	areaQueryBean.areaCategory="";
	
	//表格初始化
	$('#areaSingleCheckboxInfos').datagrid({
		singleSelect:true,
		url:pageDataUrl,
		   queryParams: {
			   "province":areaQueryBean.province,
			   "city":areaQueryBean.city,
			   "country":areaQueryBean.country,
			   "town":areaQueryBean.town,
			   "translateStatus":areaQueryBean.translateStatus,
			   "areaName":areaQueryBean.areaName,
			   "areaCategory":areaQueryBean.areaCategory,
			   "pageNumber":areaQueryBean.pageNumber,
			   "pageSize":areaQueryBean.pageSize
			},	
		columns:areaInfosCol,
		onLoadSuccess: function(data){
	    	pageNumber=data.pageNumber;
	    	radioCommunityPageSize=data.pageSize;
	    	$('#areaSingleCheckboxInfos').datagrid('resize');
	    	$('#areaInfospp').pagination('refresh', {
				total: data.total,
				pageNumber: data.pageNumber
			});	
	    	$(".pagination-page-list").prev().remove();
	    	$(".pagination-page-list").next().remove();
			$(".pagination-page-list").parent().append("<span>条</span>");
			$(".pagination-page-list").parent().prepend("<span>共计"+data.total+"条,每页显示： </span>");
		},
	});	
}

function initProvinceSelect(){
	var allCommunitysUrl = '';
	//获取所有小区
	var citys = bd.getIds('.arealist .delPic');
	var allCommunitysUrl = localhost+'grms/common/communitys?province=&citys='+citys+'&country=&keyword=';

	$.ajax({
		type: "get",
		url: allCommunitysUrl,
		dataType: 'json',
		contentType: 'application/json;charset=UTF-8',
		async: false,
		success: function(data) {
			if(data.code == 0) {
				total = data.total;
				$('#total').text(total);
				$('#communityDg').datagrid('loadData', data.data);//填入表格
			}
		},
		error:function(xhr,txt,e){
			bd.alert('提示','服务器响应异常！');
		}
	});	
}

//  初始化下拉框
function initSelt() {
	var cityAreaTownInitValue = [{//市、区默认值
		"id": "",
		"text": "请选择",
		"selected": true
	}];
	$('#city_selt,#area_selt,#town_selt').combobox({
		height: 34,
		width: 120,
		limitToList: true, //输入的值只能是列表框中的内容
		data: cityAreaTownInitValue,
		valueField: 'id',
		textField: 'text',
		editable: false,
		panelHeight: "auto",
		value: '请选择'
	});

	$('#province_selt').combobox({
		height: 34,
		width: 120,
		editable: false,
		valueField: 'id',
		textField: 'text',
		//  		data:comboData,
		value: '请选择'

	})
	var comboData = [{
		text:'请选择',
		id:''
	}]; //省
	var myProvinces = [];
	myProvinces = provinces
		//		comboData = provinces;
//	console.log(provinces)
//	console.log('111' + comboData)
	if(myProvinces.length == 0) { //没有选市，省空
		var provincesUrl = localhost+'grms/common/districts/provinces';
//		获取全部省
		$.ajax({
			type: "get",
			url: provincesUrl,
			dataType: 'json',
			contentType: 'application/json;charset=UTF-8',
			async: false,
			success: function(data) {
//				console.log(data)
				if(data.code == 0) {
					var shengdata = data.data;
					for(var i = 0; i < shengdata.length; i++) {//处理数据格式
						comboData.push({
							id: shengdata[i].code,
							text: shengdata[i].name
						})
					};
					if(comboData.length<=10){$('#province_selt').combobox({panelHeight: 'auto'})}
					$('#province_selt').combobox({
						data: comboData,//省数据填入下拉框
						onChange: function(newValue, oldValue) {
							var comboCitysData = [{
								text:'请选择',
								id:''
							}]; //市
							$('#city_selt').combobox('select','');
//							获取市
							if(newValue!=''){
								var citysUrl = localhost+'grms/common/districts/' + newValue + '/citys';
								$.ajax({
									type: "get",
									url: citysUrl,
									dataType: 'json',
									contentType: 'application/json;charset=UTF-8',
									async: false,
									success: function(data) {
	//									console.log(data)
										if(data.code == 0) {
											var shidata = data.data;
											for(var i = 0; i < shidata.length; i++) {//处理数据格式
												comboCitysData.push({
													id: shidata[i].code,
													text: shidata[i].name
												})
											}
											$('#city_selt').combobox({
												data: comboCitysData,//市数据填入下拉框
												onChange: function(newValue, oldValue) {
													var comboCountrysData = [{
														text:'请选择',
														id:''
													}]; //区
													if(newValue!=''){
														var countrysUrl = localhost+'grms/common/districts/' + newValue + '/countrys';
		//												console.log(newValue)
		//												获取区
														$.ajax({
															type: "get",
															url: countrysUrl,
						//									url: "../dialog/tree_data1.json",
															dataType: 'json',
															contentType: 'application/json;charset=UTF-8',
															async: false,
															success: function(data) {
		//														console.log(data)
																if(data.code == 0) {
																	var data = data.data;
																	for(var i = 0; i < data.length; i++) {//处理数据格式
																		comboCountrysData.push({
																			id: data[i].code,
																			text: data[i].name
																		})
																	}
		//															console.log(comboCountrysData);
																		
																	$('#area_selt').combobox({
																		data: comboCountrysData,//填入区数据
																		onChange: function(newValue, oldValue){
																				var comboTownsData = [{
																					text:'请选择',
																					id:''
																				}];
																			if(newValue!=''){
																				var townsUrl = localhost+'grms/common/districts/'+newValue+'/towns';
																				$.ajax({
																					type: "get",
																					url: townsUrl,
																					dataType: 'json',
																					contentType: 'application/json;charset=UTF-8',
																					async: false,
																					success: function(data){
																						var comboTownsData = [{
																							text:'请选择',
																							id:''
																						}];
																						if(data.code == 0) {
																							var data = data.data;
																							for(var i = 0; i < data.length; i++) {//处理数据格式
																								comboTownsData.push({
																									id: data[i].code,
																									text: data[i].name
																								})
																							}
																						}
																						$('#town_selt').combobox({
																							data: comboTownsData,//市数据填入下拉框
																						})
																					}
																				});
																			}	
																		}
																	
																	})

																}
															},
															error:function(xhr,txt,e){
																bd.alert('提示','服务器响应异常！');
															}
														});
													}else{
														var nullData = [{//联动下拉框默认空数据
															text:'请选择',
															id:''
														}];
														$('#area_selt').combobox({data:nullData});
													}
	
												}
											})
										}
									},
									error:function(xhr,txt,e){
										bd.alert('提示','服务器响应异常！');
									}
							
								})
							}else{
								var nullData = [{//联动下拉框默认空数据
									text:'请选择',
									id:''
								}];
								$('#city_selt').combobox({data:nullData});
								$('#area_selt').combobox({data:nullData});
							}
						}
					})
				}
			},
			error:function(xhr,txt,e){
				bd.alert('提示','服务器响应异常！');
			}
		});
	} else {//如果有选城市，即省非空
		comboData = provinces;
		if(comboData[0].id!=''){
			comboData.unshift({
				text:'请选择',
				id:''
			})
		}
//		console.log(comboData);
		$('#province_selt').combobox({
			data: comboData,//填入省
			onChange: function(newValue, oldValue) {
//				console.log(newValue+'---'+oldValue)
				var comboCitysData = []; //市
				$('#city_selt').combobox('select','');
				if(newValue!=''){
					for(var i = 0; i < comboData.length; i++) {
						if(newValue == comboData[i].id) {//获取选中省对应的城市
							comboCitysData = comboData[i].citys;
							break;
						}
					}
				}
//				console.log(comboCitysData);
				if(comboCitysData.length != 0) {//市非空
					
					if(comboCitysData[0].id!=''){
						comboCitysData.unshift({
					    	text:'请选择',
					 		id:''
						});
					}
				}else{
					comboCitysData = [{
						text:'请选择',
					 	id:''
					}]
				}
				$('#city_selt').combobox({
					data: comboCitysData,//填入市数据
					onChange: function(newValue, oldValue) {
						var comboCountrysData = [{
							text:'请选择',
							id:''
						}]; //区
						if(newValue!=''){
							var countrysUrl = localhost+'grms/common/districts/' + newValue + '/countrys';
//							console.log(newValue)
//							获取对应区
							$.ajax({
								type: "get",
								url: countrysUrl,
								dataType: 'json',
								contentType: 'application/json;charset=UTF-8',
								async: false,
								success: function(data) {
//										console.log(data)
									if(data.code == 0) {
										var data = data.data;
										for(var i = 0; i < data.length; i++) {//处理区数据格式
											comboCountrysData.push({
												id: data[i].code,
												text: data[i].name
											})
										}
	//										console.log(comboCountrysData);
	
										$('#area_selt').combobox({
											data: comboCountrysData,//填入区数据
											onChange: function(newValue, oldValue){
												var comboTownsData = [{
													text:'请选择',
													id:''
												}];
											if(newValue!=''){
												var townsUrl = localhost+'grms/common/districts/'+newValue+'/towns';
												$.ajax({
													type: "get",
													url: townsUrl,
													dataType: 'json',
													contentType: 'application/json;charset=UTF-8',
													async: false,
													success: function(data){
														var comboTownsData = [{
															text:'请选择',
															id:''
														}];
														if(data.code == 0) {
															var data = data.data;
															for(var i = 0; i < data.length; i++) {//处理数据格式
																comboTownsData.push({
																	id: data[i].code,
																	text: data[i].name
																})
															}
														}
														$('#town_selt').combobox({
															data: comboTownsData,//市数据填入下拉框
														})
													}
												});
											}	
										}
											
										})
									}
								},
								error:function(xhr,txt,e){
									bd.alert('提示','服务器响应异常！');
								}
							});
						}else{
							var nullData = [{//联动下拉框默认空数据
								text:'请选择',
								id:''
							}];
							$('#area_selt').combobox({data:nullData});
						}
					}
				})

			}
		})
	}
}
//表格加载成功
/*function onLoadSuccess() {
	var allCommunitys = $('#communityDg').datagrid('getRows'); //所有小区
	$('#communityDg').datagrid('uncheckAll');
	for(var i = 0; i < communitys.length; i++) { //循环选中的小区
		for(var j = 0; j < allCommunitys.length; j++) { //循环所有小区
			if(allCommunitys[j].organizationSeq == communitys[i]) { //选中的小区==所有小区中的某个
				var index = $('#communityDg').datagrid('getRowIndex', allCommunitys[j]); //只能这样拿index
				$('#communityDg').datagrid('selectRow', index); //勾选
			}
		}
	}
	
//另一线程延迟加载复选框样式
	setTimeout(function() {
		bd.loadSpan();
		bd.boxCheck();
		bd.boxUnCheck();
	//	全选按钮勾上
		var total = $('#total').text();
		var selNum = $('#selNum').text();
		if(total!=0&&total == selNum){
			$('#communityDg').datagrid('checkAll');
		}
	}, 0)
}*/

//	选中小区确定写到一级页面
function communitySure() {
	var communityHtml = ''; //一级页面小区标签
	communitys = []; //选中的小区organizationSeq
	var selectNodes = $('#areaSingleCheckboxInfos').datagrid('getSelections');
	if(selectNodes.length == 0) {
		bd.alert('提示', '请选择小区');
	} else {
		for(var i = 0; i < selectNodes.length; i++) {
			communityHtml += '<li class="clearfix fl" organizationSeq="' + selectNodes[i].organizationSeq + '""><span class="community fl">' +
			selectNodes[i].areaName + '</span><span class="delPic fl" id="' + selectNodes[i].organizationSeq + '" onclick="communityDelSelf(this)"></span></li>';
			communitys.push(selectNodes[i].organizationSeq); //重新填入选中的小区organizationSeq
		}
		$('.communitylist').html('');
		$('.communitylist').append(communityHtml);
		$('#communityRadioPageWindow').window('close');
	}

}

//删除小区
function communityDelSelf(e) {
	var id = $(e).attr('id');
	for(var i = 0; i < communitys.length; i++) {
		if(id == communitys[i]) {
			communitys.splice(i, 1); //数组删除
		}
	}
	$(e).parent('li').remove(); //页面删除
}
//窗口大小改变
$(window).resize(function() {
	$('#communityRadioPageWindow').window('center'); //弹窗居中
})

var areaInfosCol=[[
                   {
                	   field:'organizationSeq',
                	   checkbox:true,
                	   width:80,
                   },
		             {
		                 field:'areaName',
		                 title:'小区名称',
		                 width:150,
		                 align:'center'
		             },
		             {
		                 field:'areaCategory',
		                 title:'小区类型',
		                 width:150,
		                 align:'center',
			 	        	formatter: function(value,row,index){
				        		switch(value){
					        		case '0':return "测试小区";break;
					        		case '1':return "云对讲项目";break;
					        		case '2':return "工程项目";break;
					        		case '3':return "样板演示小区";break;
					        		default:return"/";
				        		}
							}		                 
		             },
		             {
		                 field:'translateStatus',
		                 title:'转化情况',
		                 width:150,
		                 align:'center',
		 	        	formatter: function(value,row,index){
			        		switch(value){
				        		case 0:return "未转化";break;
				        		case 1:return "已转化";break;
				        		case 2:return "部分转化";break;
				        		default:return"/";
			        		}
						}		                 
		             },
		             {
		                 field:'address',
		                 title:'地址',
		                 width:420,
		                 align:'center'
		             }
]];

function initAreaTypeSelect(){
	$('#areaType').combobox({    
	    data:[
				{
					label: '请选择',
					value: ''
				},	          
				{
					label: '云对讲项目',
					value: '1'
				},
				{
					label: '工程项目',
					value: '2'
				},
				{
					label: '样板演示小区',
					value: '3'
				},
				{
					label: '测试小区',
					value: '0'
				}	 				
	    ],
	    valueField:'value',    
	    textField:'label',
	    select:'',
		editable: false,
		panelHeight: "auto"
	});  
}
	initDataGrid();
	initPageInfos();
	initSelt();//初始化下拉框
	initProvinceSelect();
	initAreaTypeSelect();
