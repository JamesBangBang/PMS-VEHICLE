
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
            type: 'column'
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
            text: '今日统计'
        },
        xAxis: {
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: '条'
            }
        },
        tooltip: {
            shared: true,
            useHTML: false,
			followPointer:true,
			padding:20,
            hideDelay:100
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [{
            name: '其它',
            color: 'rgb(0,151,196)',
            data: [49]
        }, {
            name: '出口',
            color: 'rgb(255,164,0)',
            data: [83]
        }, {
            name: '入口',
            color: 'rgb(71,210,0)',
            data: [48]
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
