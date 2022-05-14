//客户移交记录数据
var dataList;
function transferInfo(){
	
	var id = $("#detailId").val();
	$.ajax({
 		type : "post",
 		url : "/grms/customerManagement/transferInfo.html",
 		dataType : "json",
 		data : {"id":id},
	 	success : function(result) {
	 		if (result.succ) {
	 			dataList = result.data;
				initDg();
			}else{
				infoMask("获取移交信息列表失败:  服务器响应异常")
			}
	 	}
 	});
}

function initDg(){
	$("#transferInfo").datagrid({
	    border:true,
	    scrollbarSize:0,
	    nowrap:false,//允许换行
	    emptyMsg: '<span>无记录</span>',
	    data:dataList,
	    fitColumns:true,//宽度不自适应
	    checkOnSelect:false,//点击该复选框的时候才会选中或取消
	    emptyMsg:'<span>无记录</span>',
	    singleSelect:false,
	    selectOnCkeck:true,
	    ckeckOnSelect:true,
	    onBeforeLoad:function(param){
			$('#transferInfo').datagrid('resize');	
	    },
	    onLoadSuccess:function(data){
	    	
	    	setTimeout(function(){
	    		$('#transferInfo').datagrid('resize');	
				var dataHeight =  $("#transferInfoBox .datagrid-view").height()-19;
		        $("#transferInfoBox .datagrid-view").css("height",dataHeight );
	    	},0)	
			
	    },
	    columns:[[
	        {
	            field:'time',
	            title:'时间',
	            width:200,
	            align:'center',
	            formatter : function(value){
                    var date = new Date(value);
                    var y = date.getFullYear();
                    var m = date.getMonth() + 1;
                    var d = date.getDate();
                   /* var h = date.getHours(); // hour
                    var M = date.getMinutes(); // minute
                    var s = date.getSeconds(); // second*/ 
                    return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) /*+ ' ' + (h < 10 ? '0' + h : h) + ':' + (M < 10 ? '0' + M : M) + ':' + (s < 10 ? '0' + s : s)*/;
                }
	        },
	        {
	            field:'formerName',
	            title:'移交人员',
	            width:200,
	            align:'center',
	        },
	        {
	        	field:'accepterName',
	        	title:'接收人员',
	        	width:200,
	        	align:'center',
	        },
	        {
	        	field:'createName',
	        	title:'操作人员',
	        	width:200,
	        	align:'center',
	        }
	    ]]
	});
}