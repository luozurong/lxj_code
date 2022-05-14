var myChart;  
        var eCharts;  
  
        
  
        //创建ECharts图表方法  closed="true"
        function DrawEChart(ec) {  
            eCharts = ec;  
            myChart = eCharts.init(document.getElementById('main'));  
            myChart.showLoading({  
                text : "图表数据正在努力加载..."  
            });  
            //定义图表options  
           var options = {
              title : {
		text: '新用户',	
               　subtext: '（人）'	
	    },
	    tooltip : {
		trigger: 'axis'
	    },
	    legend: {
		data:['苹果AppStore','豌豆夹','360手机助手','腾讯应用宝','百度应用','安卓应用市场']
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
		    type : 'category',
		    boundaryGap : false,
		    data : ['2016/08/01','2016/08/02','2016/08/03','2016/08/04','2016/08/05','2016/08/06','2016/08/07','2016/08/08','2016/08/09','2016/08/10']
		}
	    ],
	    yAxis : [
		{
		    type : 'value'
		}
	    ],
	    series : [
		{
		    name:'苹果AppStore',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[400, 390, 380, 410, 350, 300, 280,350,320,350]
		},
                {
		    name:'豌豆夹',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[500, 550, 600, 800, 700, 750, 600,500,450,400]
		},
                {
		    name:'360手机助手',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[50, 80, 100, 90, 60, 40, 75,85,95,60]
		},
                {
		    name:'腾讯应用宝',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[150, 180, 200, 190, 160, 140, 175,185,195,160]
		},
                {
		    name:'百度应用',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[250, 280, 300, 290, 260, 240, 275,285,295,260]
		},
                {
		    name:'安卓应用市场',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[350, 380, 400, 390, 360, 340, 375,385,395,360]
		}
	    ]
           }; 
            myChart.setOption(options); //先把可选项注入myChart中  
            myChart.hideLoading();  
          //  getChartData();//aja后台交互   9/07/2016 - 09/14/2016
        }  
        

$(document).ready(function(){   
        var data = $.parseJSON(jsonstr);    
        $('.easyui-datagrid').datagrid('loadData', data);
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
              'echarts/chart/line'  
            ], DrawEChart //异步加载的回调函数绘制图表  
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
