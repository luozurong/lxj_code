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
			data:['访问深度(次)']
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
			    data : ['1页','2页','3页','4页','5页','6-10页','11-50页','50页以上']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'访问深度(次)',
			    type:'bar',
			    data:[162, 135, 77, 25, 20, 7, 5, 2],
			    markPoint : {
				data : [
				    {type : 'max', name: '最大值'},
				    {type : 'min', name: '最小值'}
				]
			    },
			    markLine : {
				data : [
				    {type : 'average', name: '平均值'}
				]
			    }
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
			data:['使用频率(人)']
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
			    data : ['1-2次','3-5次','6-9次','10-19次','20-49次','50次以上']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'使用频率(人)',
			    type:'bar',
			    data:[162, 135, 77, 7, 5, 2],
			    markPoint : {
				data : [
				    {type : 'max', name: '最大值'},
				    {type : 'min', name: '最小值'}
				]
			    },
			    markLine : {
				data : [
				    {type : 'average', name: '平均值'}
				]
			    }
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
			data:['使用时长(次)']
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
			    data : ['0-3秒','4-9秒','10-29秒','30-59秒','1-3分','3-10分','10-30分','30分以上']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'使用时长(次)',
			    type:'bar',
			    data:[162, 135, 77, 25, 20, 7, 5, 2],
			    markPoint : {
				data : [
				    {type : 'max', name: '最大值'},
				    {type : 'min', name: '最小值'}
				]
			    },
			    markLine : {
				data : [
				    {type : 'average', name: '平均值'}
				]
			    }
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
			data:['使用间隔(次)']
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
			    data : ['首次启动','24小时以下','1天','2天','3天','4天','5天','6天','1周','1周-1月','1月以上']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'使用间隔(次)',
			    type:'bar',
			    data:[362,235,162,135,77,50,30,15,7,5,2],
			    markPoint : {
				data : [
				    {type : 'max', name: '最大值'},
				    {type : 'min', name: '最小值'}
				]
			    },
			    markLine : {
				data : [
				    {type : 'average', name: '平均值'}
				]
			    }
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
