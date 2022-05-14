 $(document).ready(function(){ 
        var data = $.parseJSON(construction);    
        $('#construction').datagrid('loadData', data);
		var data = $.parseJSON(region);    
        $('#region').datagrid('loadData', data);
		var data = $.parseJSON(activeUser);    
        $('#activeUser').datagrid('loadData', data);
		var data = $.parseJSON(openDoor);    
        $('#openDoor').datagrid('loadData', data);
		var data = $.parseJSON(achievement);    
        $('#achievement').datagrid('loadData', data);
		var data = $.parseJSON(achievement1);    
        $('#achievement1').datagrid('loadData', data);
		$('#achievement2').datagrid('loadData', data);
		$('#achievement3').datagrid('loadData', data);
		$('#achievement4').datagrid('loadData', data);
		$('#achievement5').datagrid('loadData', data);
		var data = $.parseJSON(orderIndicator);    
        $('#orderIndicator').datagrid('loadData', data);
		var data = $.parseJSON(warning);    
        $('#warning').datagrid('loadData', data);
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
              'echarts/chart/line',
              'echarts/chart/bar',
              'echarts/chart/map'  
            ], drawECharts //异步加载的回调函数绘制图表  
        );  
 });
        
        function drawECharts(ec) {
           DrawEChart(ec);
           DrawEChart1(ec);
           DrawEChart2(ec);
           DrawEChart3(ec);
	   DrawEChart4(ec);
           DrawEChart5(ec);
　	　　　DrawEChart6(ec);
        }
        //创建ECharts图表方法  closed="true"
        function DrawEChart(ec) {  
            var eCharts = ec;  
            var myChart = eCharts.init(document.getElementById('main'));  
            myChart.showLoading({  
                text : "图表数据正在努力加载..."  
            });  
            //定义图表options  
           var options =  {
		    title : {
			//text: '访问深度',
			//subtext: '人'
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
			data:['待签','已签','设计中','施工中','已完工']
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
			    data : ['2016/08/01','2016/08/02','2016/08/03','2016/08/04','2016/08/05','2016/08/06','2016/08/07','2016/08/08']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'待签',
			    type:'bar',
			    data:[162, 135, 77, 25, 20, 7, 5, 2]			  
			},
                        {
			    name:'已签',
			    type:'bar',
			    data:[262, 15, 7, 5, 3, 0, 0, 0]			  
			},
                        {
			    name:'设计中',
			    type:'bar',
			    data:[50, 60, 20, 10, 5, 0, 0, 0]			  
			},
                        {
			    name:'施工中',
			    type:'bar',
			    data:[800, 600, 300, 20, 5, 0, 0, 0]			  
			},
                        {
			    name:'已完工',
			    type:'bar',
			    data:[1000, 800, 500, 20, 10, 0, 0, 0]			  
			}
		    ]
		};
                    
            myChart.setOption(options,true); //先把可选项注入myChart中  
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
           var options1 ={
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
		text: '开门次数',	
               　subtext: '（次）'	
	    },
	    tooltip : {
		trigger: 'axis'
	    },
	    legend: {
		data:['刷卡开门次数','IOS开门次数','Android开门次数','对讲开门次数','监控开门次数']
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
		    name:'刷卡开门次数',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[800, 850, 1000, 1100, 1050, 900, 700,850,800,600]
		},
                {
		    name:'IOS开门次数',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[500, 550, 600, 800, 700, 750, 600,500,450,400]
		},
                {
		    name:'Android开门次数',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[50, 80, 100, 90, 60, 40, 75,85,95,60]
		},
                {
		    name:'对讲开门次数',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[10, 50, 40, 30, 20, 10, 50,40,30,10]
		},
                {
		    name:'监控开门次数',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[20, 30, 50, 40, 10, 40, 20,30,30,30]
		}
　　　　　　　　　　　　　　　　
	    ]
           }; 
            myChart2.setOption(options2,true); //先把可选项注入myChart中  
            myChart2.hideLoading();  
          //  getChartData();//aja后台交互   9/07/2016 - 09/14/2016
        }  

//创建ECharts图表方法  closed="true"
        function DrawEChart3(ec) {  
            var eCharts3 = ec;  
            var myChart3 = eCharts3.init(document.getElementById('main3'));  
            myChart3.showLoading({  
                text : "图表数据正在努力加载..."  
            });  
            //定义图表options  
           var options3 = {
              title : {
	       text: '成交总额',	
               subtext: '（元）'	
	    },
	    tooltip : {
		trigger: 'axis'
	    },
	    legend: {
		data:['送水到家','上门维修','钟点工','生活超市','促销专场']
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
		    name:'送水到家',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[800, 850, 1000, 1100, 1050, 900, 700,850,800,600]
		},
                {
		    name:'上门维修',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[500, 550, 600, 800, 700, 750, 600,500,450,400]
		},
                {
		    name:'钟点工',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[200, 350, 400, 450, 420, 350, 300,320,280,250]
		},
                {
		    name:'生活超市',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[400, 550, 600, 650, 620, 550, 500,520,480,450]
		},
                {
		    name:'促销专场',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[100, 250, 300, 350, 320, 250, 200,220,180,150]
		}　　　　　　　　　　　　　　　　
	    ]
           }; 
            myChart3.setOption(options3,true); //先把可选项注入myChart中  
            myChart3.hideLoading();  
          //  getChartData();//aja后台交互   9/07/2016 - 09/14/2016
        }  
        
	 //创建ECharts图表方法  closed="true"
        function DrawEChart4(ec) {  
            var eCharts4 = ec;  
            var myChart4 = eCharts4.init(document.getElementById('main4'));  
            myChart4.showLoading({  
                text : "图表数据正在努力加载..."  
            });  
            //定义图表options  
           var options4 = {
              title : {
		text: '订单数',	
                subtext: '（个）'	
	    },
	    tooltip : {
		trigger: 'axis'
	    },
	    legend: {
		data:['总订单数']
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
		    name:'总订单数',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[800, 850, 1000, 1100, 1050, 900, 700,850,800,600]
		}　　　　　　　　
	    ]
           }; 
            myChart4.setOption(options4,true); //先把可选项注入myChart中  
            myChart4.hideLoading();  
          //  getChartData();//aja后台交互   9/07/2016 - 09/14/2016
        }  

	//创建ECharts图表方法  closed="true"
        function DrawEChart5(ec) {  
            var eCharts5 = ec;  
            var myChart5 = eCharts5.init(document.getElementById('main5'));  
            myChart5.showLoading({  
                text : "图表数据正在努力加载..."  
            });  
            //定义图表options  
           var options5 =  {
		    title : {
			text: '报障情况',
			subtext: '(次)'
		    },
		    tooltip : {
			trigger: 'axis'
		    },
		    legend: {
			data:['平台类故障（自动）','平台类故障（人工）','APP类故障（人工）','门禁设备类故障（自动）','门禁设备类故障（人工）']
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
			    data : ['2016/08/01','2016/08/02','2016/08/03','2016/08/04','2016/08/05','2016/08/06','2016/08/07','2016/08/08']
			}
		    ],
		    yAxis : [
			{
			    type : 'value'
			}
		    ],
		    series : [
			{
			    name:'平台类故障（自动）',
			    type:'bar',
			    data:[0, 0, 0, 0, 0, 0, 0, 0]			  
			},
                        {
			    name:'平台类故障（人工）',
			    type:'bar',
			    data:[0, 1, 0, 0, 0, 0, 0, 0]			  
			},
                        {
			    name:'APP类故障（人工）',
			    type:'bar',
			    data:[5, 3, 2, 0, 0, 0, 0, 0]			  
			},
                        {
			    name:'门禁设备类故障（自动）',
			    type:'bar',
			    data:[50, 8, 0, 20, 5, 0, 0, 0]			  
			},
                        {
			    name:'门禁设备类故障（人工）',
			    type:'bar',
			    data:[3, 0, 0, 0, 0, 0, 0, 0]			  
			}
		    ]
		};
                    
            myChart5.setOption(options5,true); //先把可选项注入myChart中  
            myChart5.hideLoading();  
          //  getChartData();//aja后台交互   9/07/2016 - 09/14/2016
        } 

        //创建ECharts图表方法  closed="true"
        function DrawEChart6(ec) {  
            var eCharts6 = ec;  
            var myChart6 = eCharts6.init(document.getElementById('main6'));  
            myChart6.showLoading({  
                text : "图表数据正在努力加载..."  
            });  
            //定义图表options  
           var options6 = {
              title : {
		text: '日活跃度',	
               　subtext: '（人）'	
	    },
	    tooltip : {
		trigger: 'axis'
	    },
	    legend: {
		data:['日活跃度']
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
		    name:'日活跃度',
		    type:'line',
		    smooth:true,
		    itemStyle: {normal: {areaStyle: {type: 'default'}}},
		    data:[0.03, 0.05, 0.15, 0.30, 0.18, 0.25, 0.20,0.17,0.09,0.19]
		}
	    ]
           }; 
            myChart6.setOption(options6,true); //先把可选项注入myChart中  
            myChart6.hideLoading();  
          //  getChartData();//aja后台交互   9/07/2016 - 09/14/2016
        }
