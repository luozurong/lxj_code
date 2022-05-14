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
           var options ={
		    title : {
			text: '用户地域分布',
			subtext: '（人）',
			x:'center'
		    },
		    tooltip : {
			trigger: 'item'
		    },
		    legend: {
			orient: 'vertical',
			x:'left',
			data:['用户']
		    },
		    dataRange: {
			min: 0,
			max: 80000,
			x: 'left',
			y: 'bottom',
			text:['高','低'],           // 文本，默认为数值文本
			calculable : true
		    },
		    toolbox: {
			show: false,
			orient : 'vertical',
			x: 'right',
			y: 'center',
			feature : {
			    mark : {show: true},
			    dataView : {show: true, readOnly: false},
			    restore : {show: true},
			    saveAsImage : {show: true}
			}
		    },
		    roamController: {
			show: true,
			x: 'right',
			mapTypeControl: {
			    '广东': true
			}
		    },
		    series : [
			{
			    name: '用户',
			    type: 'map',
			    mapType: '广东',
			    roam: false,
			    itemStyle:{
				normal:{label:{show:true}},
				emphasis:{label:{show:true}}
			    },
			    data:[
				{name: '广州市',value: 80000},
				{name: '佛山市',value: 40000}
			    ]
			}
		    ]
		};
            myChart.setOption(options); //先把可选项注入myChart中  
            myChart.hideLoading();  
          //  getChartData();//aja后台交互   9/07/2016 - 09/14/2016
        }  
        

$(document).ready(function(){ 
        var data = $.parseJSON(region);    
        $('#region').datagrid('loadData', data);
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
              'echarts/chart/map'  
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
