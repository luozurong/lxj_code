var eCharts;
  var myChart;
   //定义图表options  
           var options =  [{
		    title : {
			//text: '访问深度',
			//subtext: '人'
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
			data:['送水到家(人)','上门维修(人)','钟点工(人)','生活超市(人)','促销专场(人)']
		    },
		    toolbox: {
			show : false,
			feature : {
			    mark : {show: true},
			    dataView : {show: true, readOnly: false},
			    magicType : {show: true, type: ['line', 'bar']},
			    restore : {show: true},
			    saveAsImage : {show: true}
			}
		    },
		    calculable : true,
		    xAxis : [
			{
			    type : 'category',
			    data : ['1次','2次','3次','4次','5次','6-10次','11-20次','20次以上']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'送水到家(人)',
			    type:'bar',
			    data:[162, 135, 77, 25, 20, 7, 5, 2]			  
			},
                        {
			    name:'上门维修(人)',
			    type:'bar',
			    data:[262, 15, 7, 5, 3, 0, 0, 0]			  
			},
                        {
			    name:'钟点工(人)',
			    type:'bar',
			    data:[50, 60, 20, 10, 5, 0, 0, 0]			  
			},
                        {
			    name:'生活超市(人)',
			    type:'bar',
			    data:[800, 600, 300, 20, 5, 0, 0, 0]			  
			},
                        {
			    name:'促销专场(人)',
			    type:'bar',
			    data:[1000, 800, 500, 20, 10, 0, 0, 0]			  
			}
		    ]
		},{
		    title : {
			//text: '访问深度',
			//subtext: ''
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
			data:['送水到家(人)','上门维修(人)','钟点工(人)','生活超市(人)','促销专场(人)']
		    },
		    toolbox: {
			show : false,
			feature : {
			    mark : {show: true},
			    dataView : {show: true, readOnly: false},
			    magicType : {show: true, type: ['line', 'bar']},
			    restore : {show: true},
			    saveAsImage : {show: true}
			}
		    },
		    calculable : true,
		    xAxis : [
			{
			    type : 'category',
			    data : ['1次','2次','3次','4次','5次','6-10次','11-20次','20次以上']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'送水到家(人)',
			    type:'bar',
			    data:[162, 135, 77, 25, 20, 7, 5, 2]			  
			},
                        {
			    name:'上门维修(人)',
			    type:'bar',
			    data:[262, 15, 7, 5, 3, 0, 0, 0]			  
			},
                        {
			    name:'钟点工(人)',
			    type:'bar',
			    data:[50, 60, 20, 10, 5, 0, 0, 0]			  
			},
                        {
			    name:'生活超市(人)',
			    type:'bar',
			    data:[800, 600, 300, 20, 5, 0, 0, 0]			  
			},
                        {
			    name:'促销专场(人)',
			    type:'bar',
			    data:[1000, 800, 500, 20, 10, 0, 0, 0]			  
			}
		    ]
		},{
		    title : {
			//text: '访问深度',
			//subtext: ''
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
			data:['送水到家(人)','上门维修(人)','钟点工(人)','生活超市(人)','促销专场(人)']
		    },
		    toolbox: {
			show : false,
			feature : {
			    mark : {show: true},
			    dataView : {show: true, readOnly: false},
			    magicType : {show: true, type: ['line', 'bar']},
			    restore : {show: true},
			    saveAsImage : {show: true}
			}
		    },
		    calculable : true,
		    xAxis : [
			{
			    type : 'category',
			    data : ['1次','2次','3次','4次','5次','6-10次','11-20次','20次以上']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'送水到家(人)',
			    type:'bar',
			    data:[162, 135, 77, 25, 20, 7, 5, 2]			  
			},
                        {
			    name:'上门维修(人)',
			    type:'bar',
			    data:[262, 15, 7, 5, 3, 0, 0, 0]			  
			},
                        {
			    name:'钟点工(人)',
			    type:'bar',
			    data:[50, 60, 20, 10, 5, 0, 0, 0]			  
			},
                        {
			    name:'生活超市(人)',
			    type:'bar',
			    data:[800, 600, 300, 20, 5, 0, 0, 0]			  
			},
                        {
			    name:'促销专场(人)',
			    type:'bar',
			    data:[1000, 800, 500, 20, 10, 0, 0, 0]			  
			}
		    ]
		},{
		    title : {
			//text: '访问深度',
			//subtext: ''
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
			data:['送水到家(人)','上门维修(人)','钟点工(人)','生活超市(人)','促销专场(人)']
		    },
		    toolbox: {
			show : false,
			feature : {
			    mark : {show: true},
			    dataView : {show: true, readOnly: false},
			    magicType : {show: true, type: ['line', 'bar']},
			    restore : {show: true},
			    saveAsImage : {show: true}
			}
		    },
		    calculable : true,
		    xAxis : [
			{
			    type : 'category',
			    data : ['1月1次','1月2次','1月3次','1月4次','1月5次','1月6-10次','1月11-20次','1月20次以上']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'送水到家(人)',
			    type:'bar',
			    data:[162, 135, 77, 25, 20, 7, 5, 2]			  
			},
                        {
			    name:'上门维修(人)',
			    type:'bar',
			    data:[262, 15, 7, 5, 3, 0, 0, 0]			  
			},
                        {
			    name:'钟点工(人)',
			    type:'bar',
			    data:[50, 60, 20, 10, 5, 0, 0, 0]			  
			},
                        {
			    name:'生活超市(人)',
			    type:'bar',
			    data:[800, 600, 300, 20, 5, 0, 0, 0]			  
			},
                        {
			    name:'促销专场(人)',
			    type:'bar',
			    data:[1000, 800, 500, 20, 10, 0, 0, 0]			  
			}
		    ]
		}];     

		function selDrawEcharts(num){
            myChart = eCharts.init(document.getElementById('main'+num));  
            myChart.showLoading({  
                text : "图表数据正在努力加载..."  
            });  
            
                    
            myChart.setOption(options[num],true); //先把可选项注入myChart中  
            myChart.hideLoading(); 
		}
  
        function drawEcharts(ec){
　　        DrawEChart(ec);
        }
        //创建ECharts图表方法  closed="true"
        function DrawEChart(ec) {  
            eCharts = ec;  
            myChart = eCharts.init(document.getElementById('main0'));  
            myChart.showLoading({  
                text : "图表数据正在努力加载..."  
            });  
            
                    
            myChart.setOption(options[0],true); //先把可选项注入myChart中  
            myChart.hideLoading();  
          //  getChartData();//aja后台交互   9/07/2016 - 09/14/2016
        }  
        
 

$(document).ready(function(){
        var data = $.parseJSON(jsonstr);    
        $('#datagrid').datagrid('loadData', data);
		data = $.parseJSON(jsonstr1);    
        $('#datagrid1').datagrid('loadData', data);
		data = $.parseJSON(jsonstr2);    
        $('#datagrid2').datagrid('loadData', data);
		data = $.parseJSON(jsonstr3);    
        $('#datagrid3').datagrid('loadData', data);
	  // 路径配置
        require.config({
            packages: [{
                name: 'echarts',
                location: 'echarts',
                main: 'echarts'
            }]
        });
  
        require(  
            [ 'echarts',   
              'echarts/chart/bar'  
            ], drawEcharts //异步加载的回调函数绘制图表  
        );  

		$('#tt').tabs({
			border:false,
			onSelect:function(title,index){
				selDrawEcharts(index);
			}
		});

      var input1 = $('<input>').appendTo($('#dateFrame')).attr('id', 'dd').attr('type','text').attr('value','').css({"display":"none"});

      easyloader.locale = "zh_CN";
      easyloader.base = "easyui/";
      using('daterangebox', function(){
        $('#dd').daterangebox();
      });

   
	  $("#areaLi input").each(function(){
		  $(this).click(function(){               
              if($(this).is(':checked'))
			  {
                 $("#area_seat").append("<span class=\"sel-box w80\" name=\""+$(this).parent().find('label').text()+"\">"+$(this).parent().find('label').text()+"<a class=\"close\" href=\"javascript:\" onclick=\"$.removeSel(this,&quot;SWZ&quot;,2)\"></a></span>");
			  }
			  else
			  {
                 $("#area_seat").find("span[name='"+$(this).parent().find('label').text()+"']").remove();
			  }
			  
		  });
	  });

         $("#villageLi input").each(function(){
		  $(this).click(function(){               
              if($(this).is(':checked'))
			  {
                 $("#village_seat").append("<span class=\"sel-box w80\" name=\""+$(this).parent().find('label').text()+"\">"+$(this).parent().find('label').text()+"<a class=\"close\" href=\"javascript:\" onclick=\"$.removeSel(this,&quot;SWZ&quot;,2)\"></a></span>");
			  }
			  else
			  {
                 $("#village_seat").find("span[name='"+$(this).parent().find('label').text()+"']").remove();
			  }
			  
		  });
	  });

$('#w').window({
    		collapsible:false,
    		minimizable:false,
    		maximizable:false
    	});
$('#w1').window({
    		collapsible:false,
    		minimizable:false,
    		maximizable:false
    	});
	

    });

function openWin()
        {
            var offset =$("#areaButton").offset();
            
            $('#w').window({left:offset.left+"px", top:offset.top+$("#areaButton").height()+"px"});
            $('#w').window('open');
        }       

       

        function openWin1()
        {
            var offset =$("#villageButton").offset();
            
            $('#w1').window({left:offset.left+"px", top:offset.top+$("#villageButton").height()+"px"});
            $('#w1').window('open');
        } 
