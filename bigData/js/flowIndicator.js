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
			text: '访问路径',
                        subtext: 'PV（%）'
		    },
		    toolbox: {
			show : false,
			feature : {
			    mark : {show: true},
			    dataView : {show: true, readOnly: false},
			    restore : {show: true},
			    saveAsImage : {show: true}
			}
		    },
                    
		    tooltip : {
			trigger: 'item',
			formatter: "{b}"
		    },
		    series : [
			{
			    name:'树图',
			    type:'tree',
			    orient: 'horizontal',  // vertical horizontal
			    rootLocation: {x: 100,y: 230}, // 根节点位置  {x: 100, y: 'center'}
			    nodePadding: 8,
			    layerPadding: 200,
			    hoverable: false,
			    roam: true,
			    symbolSize: 6,
			    itemStyle: {
				normal: {
				    color: '#4883b4',
				    label: {
				        show: true,
				        position: 'right',
				        formatter: "{b}: {c}",
				        textStyle: {
				            color: '#000',
				            fontSize: 12
				        }
				    },
				    lineStyle: {
				        color: '#ccc',
				        type: 'curve' // 'curve'|'broken'|'solid'|'dotted'|'dashed'

				    }
				},
				emphasis: {
				    color: '#4883b4',
				    label: {
				        show: true
				    },
				    borderWidth: 0
				}
			    },
			    
			     data: [
                {
                     name: '生活首页：51％',
                                    value: 2,
                                    symbol: 'circle',
                                    symbolSize: 20,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                position: 'right'
                                            },
                                            color: '#fa6900',
                                            brushType: 'stroke',
                                            borderWidth: 1,
                                            borderColor: '#999966',
                                        },
                                        emphasis: {
                                            borderWidth: 0
                                        }
                                    },
                    children: [
                        {
                             name: '送水到家：37.82％',
                                    value: 2,
                                    symbol: 'circle',
                                    symbolSize: 20,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                position: 'right'
                                            },
                                            color: '#fa6900',
                                            brushType: 'stroke',
                                            borderWidth: 1,
                                            borderColor: '#999966',
                                        },
                                        emphasis: {
                                            borderWidth: 0
                                        }
                                    },
                            children: [
                                {
                                    name: '列表页：15.26％',
                                    value: 2,
                                    symbol: 'circle',
                                    symbolSize: 20,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                position: 'right'
                                            },
                                            color: '#fa6900',
                                            brushType: 'stroke',
                                            borderWidth: 1,
                                            borderColor: '#999966',
                                        },
                                        emphasis: {
                                            borderWidth: 0
                                        }
                                    }
                                },
                                {
                                    name: '详情页：8.85％',
                                    value: 2,
                                    symbol: 'circle',
                                    symbolSize: 20,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                position: 'right'
                                            },
                                            color: '#fa6900',
                                            brushType: 'stroke',
                                            borderWidth: 1,
                                            borderColor: '#999966',
                                        },
                                        emphasis: {
                                            borderWidth: 0
                                        }
                                    }
                                },
                                {
                                    name: '支付页：2.97%',
                                    value: 2,
                                    symbol: 'circle',
                                    symbolSize: 20,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                position: 'right'
                                            },
                                            color: '#fa6900',
                                            brushType: 'stroke',
                                            borderWidth: 1,
                                            borderColor: '#999966',
                                        },
                                        emphasis: {
                                            borderWidth: 0
                                        }
                                    }
                                },
                                {
                                    name: '离开应用：16.97%',
                                    value: 2,
                                    symbol: 'circle',
                                    symbolSize: 20,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                position: 'right'
                                            },
                                            color: '#fa6900',
                                            brushType: 'stroke',
                                            borderWidth: 1,
                                            borderColor: '#999966',
                                        },
                                        emphasis: {
                                            borderWidth: 0
                                        }
                                    }
                                }
                            ]
                        },
                        {
                             name: '上门维修：12.45％',
                                    value: 2,
                                    symbol: 'circle',
                                    symbolSize: 20,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                position: 'right'
                                            },
                                            color: '#fa6900',
                                            brushType: 'stroke',
                                            borderWidth: 1,
                                            borderColor: '#999966',
                                        },
                                        emphasis: {
                                            borderWidth: 0
                                        }
                                    }
                        },
                        {
                             name: '钟点工：8.23％',
                                    value: 2,
                                    symbol: 'circle',
                                    symbolSize: 20,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                position: 'right'
                                            },
                                            color: '#fa6900',
                                            brushType: 'stroke',
                                            borderWidth: 1,
                                            borderColor: '#999966',
                                        },
                                        emphasis: {
                                            borderWidth: 0
                                        }
                                    }
                        },
                        {
                             name: '生活超市：10.25％',
                                    value: 2,
                                    symbol: 'circle',
                                    symbolSize: 20,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                position: 'right'
                                            },
                                            color: '#fa6900',
                                            brushType: 'stroke',
                                            borderWidth: 1,
                                            borderColor: '#999966',
                                        },
                                        emphasis: {
                                            borderWidth: 0
                                        }
                                    }
                        },
                        {
                             name: '促销专场：2.25％',
                                    value: 2,
                                    symbol: 'circle',
                                    symbolSize: 20,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                position: 'right'
                                            },
                                            color: '#fa6900',
                                            brushType: 'stroke',
                                            borderWidth: 1,
                                            borderColor: '#999966',
                                        },
                                        emphasis: {
                                            borderWidth: 0
                                        }
                                    }
                        },
                        {
                             name: '离开应用：20.25％',
                                    value: 2,
                                    symbol: 'circle',
                                    symbolSize: 20,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                position: 'right'
                                            },
                                            color: '#fa6900',
                                            brushType: 'stroke',
                                            borderWidth: 1,
                                            borderColor: '#999966',
                                        },
                                        emphasis: {
                                            borderWidth: 0
                                        }
                                    }
                        }
                         
                    ]
                  }
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
        $('#datagrid').datagrid('loadData', data);
		data = $.parseJSON(jsonstr1);    
        $('#datagrid1').datagrid('loadData', data);
		$('#datagrid2').datagrid('loadData', data);
		$('#datagrid3').datagrid('loadData', data);
		$('#datagrid4').datagrid('loadData', data);
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
              'echarts/chart/tree'  
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
