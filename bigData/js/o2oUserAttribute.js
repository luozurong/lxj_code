  
         
        
        function drawEcharts(ec){
　　        DrawEChart(ec);
　        　DrawEChart1(ec);
          DrawEChart2(ec);
        }
        //创建ECharts图表方法  closed="true"
        function DrawEChart(ec) {  
            var eCharts = ec;  
            var myChart = eCharts.init(document.getElementById('main'));  
            myChart.showLoading({  
                text : "图表数据正在努力加载..."  
            });  
            //定义图表options  
           var options = {
		    title : {
			text: '性别比例',
			subtext: '（人）',
			x:'center'
		    },
		    tooltip : {
			trigger: 'item',
			formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
			orient : 'vertical',
			x : 'left',
			data:['男','女']
		    },
		    toolbox: {
			show : false,
			feature : {
			    mark : {show: true},
			    dataView : {show: true, readOnly: false},
			    magicType : {
				show: true, 
				type: ['pie', 'funnel'],
				option: {
				    funnel: {
				        x: '25%',
				        width: '50%',
				        funnelAlign: 'left',
				        max: 900
				    }
				}
			    },
			    restore : {show: true},
			    saveAsImage : {show: true}
			}
		    },
		    calculable : true,
		    series : [
			{
			    name:'性别比例',
			    type:'pie',
			    radius : '55%',
			    center: ['50%', '60%'],
			    data:[
				{value:900, name:'男'},
				{value:800, name:'女'}
			    ]
			}
		    ]
		};
            myChart.setOption(options); //先把可选项注入myChart中  
            myChart.hideLoading();  
          //  getChartData();//aja后台交互   9/07/2016 - 09/14/2016
        }  

//创建ECharts图表方法  closed="true"
        function DrawEChart1(ec) {  
            var eCharts1 = ec;  
            var myChart1 = eCharts1.init(document.getElementById('main1'));  
            myChart1.showLoading({  
                text : "图表数据正在努力加载..."  
            });  
            //定义图表options  
           var options1 = {
                         title : {
			text: '年龄比例',
			subtext: '（人）',
			x:'center'
		       },
		         tooltip : {
			trigger: 'axis',
			axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			}
		    },
		    legend: {
　　　　　　　　　　　　　　　　　　　　　　　　orient : 'vertical',
　　　　　　　　　　　　　　　　　　　　　　　　x : 'left',
			data:['男', '女']
		    },
		    toolbox: {
			show : false,
			feature : {
			    mark : {show: true},
			    dataView : {show: true, readOnly: false},
			    magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
			    restore : {show: true},
			    saveAsImage : {show: true}
			}
		    },
		    calculable : true,
		    xAxis : [
			{
			    type : 'value',
				    axisLabel : {
					formatter: function(value)
					{
					   return Math.abs(value);
					}
				  }
			}
		    ],
		    yAxis : [
			{
			    type : 'category',
			    data : ['0-3岁','3-6岁','6-18岁','18-24岁','25-34岁','35-44岁','45-54岁','55-64岁','65岁以上']
			}
		    ],
		    series : [
			{
			    name:'男',
			    type:'bar',
			    stack: '总量',
                            data:[-320, -302, -301, -334, -390, -330, -120, -132, -101],
			    itemStyle : { normal: {label : {show: true, position: 'insideLeft',formatter: function(a,b,c)
					{
					   return Math.abs(a.value);
					}}}}
			    
			},
			{
			    name:'女',
			    type:'bar',
			    stack: '总量',
			    itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
			    data:[120, 132, 101, 134, 90, 230, 210, 101, 134, 90]
			}
		    ]
		};
            myChart1.setOption(options1,true); //先把可选项注入myChart中  
            myChart1.hideLoading();  
          //  getChartData();//aja后台交互   9/07/2016 - 09/14/2016
        }
        
//创建ECharts图表方法  closed="true"
        function DrawEChart2(ec) {  
            var eCharts2 = ec;  
            var myChart2 = eCharts2.init(document.getElementById('main2'));  
            myChart2.showLoading({  
                text : "图表数据正在努力加载..."  
            });  
            //定义图表options  
           var options2 = {
		    title : {
　　　　　　　　　　　　　　　　　　　　　　　　x:'center',
			text: '用户收入',
			//subtext: '（人）'
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
　　　　　　　　　　　　　　　　　　　　　　　　x:'left',
			data:['收入（人）']
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
			    data : ['3千以下','3千-5千','5千-1万','1万-1.5万','1.5万-2万','2万-3万','3万-5万','5万以上']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'收入（人）',
			    type:'bar',
			    data:[162, 135, 77, 25, 20, 7, 5, 2]
			}
		    ]
		};
            myChart2.setOption(options2,true); //先把可选项注入myChart中  
            myChart2.hideLoading();  
          //  getChartData();//aja后台交互   9/07/2016 - 09/14/2016
        }

$(document).ready(function(){ 
        var data = $.parseJSON(jsonstr);    
        $('#datagrid').datagrid('loadData', data);  
		data = $.parseJSON(jsonstr1);    
        $('#datagrid1').datagrid('loadData', data); 
		data = $.parseJSON(jsonstr2);    
        $('#datagrid2').datagrid('loadData', data); 
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
              'echarts/chart/pie',
              'echarts/chart/bar'   
            ], drawEcharts //异步加载的回调函数绘制图表  
        ); 
      var input1 = $('<input>').appendTo($('#dateFrame')).attr('id', 'dd').attr('type','text').attr('value','');

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
