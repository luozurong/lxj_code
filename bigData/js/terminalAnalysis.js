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
			data:['累计启动次数(次)']
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
			    data : ['苹果','三星','华为','小米','OPPO','vivo','其他']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'累计启动次数(次)',
			    type:'bar',
			    data:[76099, 18384, 12970, 10439, 9987, 8853, 7648],
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
			//subtext: '人'
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
			data:['累计启动次数(次)']
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
			    data : ['iPhone6s plus','iPhone6s','iPhone6','华为荣耀８','小米5S','oppoR9 plus','vivo x7']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'累计启动次数(次)',
			    type:'bar',
			    data:[76099, 18384, 12970, 10439, 9987, 8853, 7648],
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
			//subtext: '人'
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
			data:['累计启动次数(次)']
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
			    data : ['ios10','ios9.3','ios8.3','ios7.0','android4.4.3','android4.3.1','android4.2.2']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'累计启动次数(次)',
			    type:'bar',
			    data:[76099, 18384, 12970, 10439, 9987, 8853, 7648],
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
			//subtext: '人'
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
			data:['累计启动次数(次)']
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
			    data : ['640*1136','DVGA(640*960)','HVGA(320*480)','768*1024','1536*2048','552*900']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'累计启动次数(次)',
			    type:'bar',
			    data:[76099, 18384, 12970, 10439, 9987, 8853],
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
			//subtext: '人'
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
			data:['累计启动次数(次)']
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
			    data : ['中国移动','中国联通','中国电信']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'累计启动次数(次)',
			    type:'bar',
			    data:[18384, 12970, 10439],
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
			//subtext: '人'
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
			data:['累计启动次数(次)']
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
			    data : ['WIFI','4G/3G']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'累计启动次数(次)',
			    type:'bar',
			    data:[18384, 12970, 10439],
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
	  data = $.parseJSON(jsonstr4);    
      $('#datagrid4').datagrid('loadData', data); 
	  data = $.parseJSON(jsonstr5);    
      $('#datagrid5').datagrid('loadData', data); 
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
