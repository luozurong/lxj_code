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
			//text: '某站点用户访问来源',
			//subtext: '纯属虚构',
			x:'center'
		    },
		    tooltip : {
			trigger: 'item',
			formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
			orient : 'vertical',
			x : 'left',
			data:['业主','业主家属','租客']
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
				        max: 1548
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
			    name:'关联住房方式',
			    type:'pie',
			    radius : '55%',
			    center: ['50%', '60%'],
			    data:[
				{value:1548, name:'业主'},
				{value:310, name:'业主家属'},
				{value:135, name:'租客'}
			    ]
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
              'echarts/chart/pie'  
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
