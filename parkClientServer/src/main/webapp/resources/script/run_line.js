
var Highchart;


/*------------------------------------------------------------------------------
【功能】绘制图表
【参数】无
【返回值】无
------------------------------------------------------------------------------*/
function Highcharts_DrawRun() 
{					
	Highchart = Highcharts.chart('container', 
	
	{
            chart: {
                backgroundColor: '#082e4e',
                zoomType: 'x',
				enabled: false
            },
			credits: {
				enabled: false
			},
			navigation: {
				buttonOptions: {
					enabled: false
				}
			},
            title: {
                text: '人员通行走势图'
            },
            xAxis: {
                type: 'datetime',
                dateTimeLabelFormats: {
                    millisecond: '%H:%M:%S.%L',
                    second: '%H:%M:%S',
                    minute: '%H:%M',
                    hour: '%H:%M',
                    day: '%m-%d',
                    week: '%m-%d',
                    month: '%Y-%m',
                    year: '%Y'
                }
            },
            tooltip: {
                dateTimeLabelFormats: {
                    millisecond: '%H:%M:%S.%L',
                    second: '%H:%M:%S',
                    minute: '%H:%M',
                    hour: '%H:%M',
                    day: '%Y-%m-%d',
                    week: '%m-%d',
                    month: '%Y-%m',
                    year: '%Y'
                }
            },
            yAxis: {
                title: {
                    text: '条数'
                }
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                area: {
                    //fillColor: null
                }
            },
            series: [{
                type: 'line',
                name: '入口数据',
                data: [
                        [Date.UTC(2013,5,2),4.7695],
                        [Date.UTC(2013,5,3),6.1648],
                        [Date.UTC(2013,5,4),5.7645],]
            }, {
                type: 'line',
                name: '出口数据',
                color: '#cca635',
                data: [
                        [Date.UTC(2013,5,2),3.7695],
                        [Date.UTC(2013,5,3),6.1648],
                        [Date.UTC(2013,5,4),7.7645],]
            }]
        }
	
	);
}

function Highcharts_SetBroadSize(width, height)
{	
	$('#container').width(width);
	$('#container').height(height);
	Highchart.reflow();
} 
