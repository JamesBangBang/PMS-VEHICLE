/**
 * Created by 刘阳弘 on 2017-10-21.
 */
var myChart;
$(function () {
    var indexHeight = $(window).height();
    var contentHeight = indexHeight - 301;
    $("#content-main").height(contentHeight);
    // 每5秒，刷新一次图;
    //var timer1=window.setInterval('InitializationMenu()',100);
    //车场模态窗
    init();
    parkData();
});


//停车场表格选择样式和点击事件
function init(){
    $("#table td").bind("click",function(){
        $("#loadingGraph").show();
        var label=document.getElementById("parktext");
        var input=document.getElementById("parkId");
        //清除首页菜单
        $('.J_mainContent .J_iframe').each(function () {
            if ($(this).data('id') !== "/home/index") {
                $(this).remove();
            }
            else {
                $(this).show();
            }
        });
        $('#pageTabs .J_menuTab').each(function () {
            if ($(this).data('id') !== "/home/index") {
                $(this).remove();
            }
        });
        input.value=this.id;
        //ID赋值
        parkID=this.id;

        $("#parktext").html(this.innerText.replace(/\s/g, ""));
        //图表数据
        parkData();
        window.frames['iframe0'].initIpcSelect();
        window.frames['iframe0'].getChargeRecords();
        window.frames['iframe0'].getInoutRecords();
    });

    $("#table td").mouseover(function(){
        this.style.backgroundColor="#0096FF";

    });
    $("#table td").mouseout(function(){
        this.style.backgroundColor="#FFFFFF";
    });

}

//动态创建停车场表格
function createTable() {
    var data = new Array();
    $.ajax({
        type : 'post',
        async : true,
        dataType:'json',
        url :starnetContextPath + "/carpark/getPark",
        success : function(res) {
            data.push('<table class="table tdBorder" ><tbody id="table">');
            var k=0;
            for (var i = 0; i < ((res.data.length/3)+1); i++) {
                data.push('<tr>');
                for (var j = 0; j < 3; j++,k++) {
                    if(k>=res.data.length){
                        break;
                    }
                    data.push('<td id='+res.data[k].carparkId+'>' + '<i class="fa fa-taxi">&nbsp;&nbsp;</i>'+res.data[k].carparkName+'</td>');
                }
                data.push('</tr>');
            }
            data.push('</tbody><table>');
            document.getElementById('parkTable').innerHTML = data.join('');
            init()
        }
    });
}


//车场图表数据
 function parkData() {
     //今日停车剩余
     var total;
     var totalData;
     $.ajax({
         type : 'post',
         async : true,
         data:{
             parkId:parkID
         },
         dataType:'json',
         url :starnetContextPath + "/carpark/get7DayInCount",
         success : function(res) {
             if(res.result == 0)
             {
                 var parkData1=document.getElementById("parkData1");
                 parkData1.innerText = res.data;
             }

             $("#loadingGraph").hide();
             $("#parkModel").modal("hide");
         }
     });

     //近七日停车剩余
     $.ajax({
         type : 'post',
         async : true,
         data:{
             parkId:parkID
         },
         dataType:'json',
         url :starnetContextPath + "/carpark/getparkRecords",
         success : function(res) {
             parkingLot(res);
         }
     });

     //今日停车数量
     $.ajax({
         type : 'post',
         async : true,
         data:{
             parkId:parkID
         },
         dataType:'json',
         url :starnetContextPath + "/inout/getInoutRecordsToday",
         success : function(res) {
             if(res.result == 0)
             {
                 var parkData2=document.getElementById("parkData2");
                 parkData2.innerText=res.data;
             }
         }
     });

//近七日停车数量
     $.ajax({
         type : 'post',
         async : true,
         data:{
             parkId:parkID
         },
         dataType:'json',
         url :starnetContextPath + "/inout/getInoutRecords",
         success : function(res) {
             Parking_quantity(res);
         }
     });

     //今日金额
     $.ajax({
         type : 'post',
         async : true,
         data:{
             parkId:parkID
         },
         dataType:'json',
         url :starnetContextPath + "/charge/getChargeRecordsToday",
         success : function(res) {
             if(res.result == 0){
                 var parkData3=document.getElementById("parkData3");
                 parkData3.innerText=res.data;
             }


         }
     });

     //近七日金额
     $.ajax({
         type : 'post',
         async : true,
         data:{
             parkId:parkID
         },
         dataType:'json',
         url :starnetContextPath + "/charge/getChargeRecords",
         success : function(res) {

             charge_record(res)

         }
     });

     //今日系统异常
     $.ajax({
         type : 'post',
         async : true,
         data:{
             parkId:parkID
         },
         dataType:'json',
         url :starnetContextPath + "/unusual/getUnusualRecordsToday",
         success : function(res) {
             if(res.result == 0) {
                 var parkData4 = document.getElementById("parkData4");
                 parkData4.innerText = res.data;
             }
         }
     });

     //近七日系统异常
     $.ajax({
         type : 'post',
         async : true,
         data:{
             parkId:parkID
         },
         dataType:'json',
         url :starnetContextPath + "/unusual/getUnusualRecords",
         success : function(res) {

             systemWarn(res);

         }
     });


 }

 //近七日停车数量表
function Parking_quantity(res) {
    if( !$("#Parking_quantity").length){
        return
    }
    var dataX = new Array();
    dataX=getThisWeekDate();
    var dataY = new Array();
    dataY=[0, 0, 0, 0, 0, 0, 0];
    if(parkID)
    {
        dataX=[];
        dataY=[];
        for(var i in res.data)
        {
           for(var j in res.data[i])
           {
               if(j==0)
               {
                   dataX.push(res.data[i][j]);
               }
               else if(j==1)
               {
                   dataY.push(res.data[i][j])
               }
           }
        }

    }

    Highcharts.setOptions({
        colors: ['#0199EB',  '', '#FFFFFF']
    });
    $('#Parking_quantity').highcharts({
        chart: {
            backgroundColor:'',
            type: 'area',
        },
        title: {
            text: ' '
        },
        legend: {
            enabled: false
        },
        exporting:{
            enabled:false,
        },
        xAxis: {
            categories:dataX ,
            tickWidth:0,
            lineWidth:3,
            labels:{
                enabled:false
            },
            lineColor:'#0186E4'
        },
        yAxis: {
            gridLineWidth:0,
            title: {
                text: ''
            },
            labels:false,
        },
        tooltip: {
            formatter: function () {
                // return '<b>' + this.series.name + '</b><br/>' +
                return this.x + ': ' + this.y;
            }
        },
        plotOptions: {
            area: {
                fillColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [0, Highcharts.getOptions().colors[0]],
                        [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                    ]
                },
                marker: {
                    enabled: false,
                    symbol: 'circle',
                    states: {
                        hover: {
                            enabled: true
                        }
                    }
                },
                lineWidth: 1,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null
            }
        },

        credits: {
            enabled: false
        },
        series: [{
            name: '',
            data: dataY
        }]
    });
}


//近七日金额数量表
 function charge_record(res) {
    if( !$("#charge_record").length){
        return
    }
    var dataX = new Array();
    dataX=getThisWeekDate();
    var dataY = new Array();
    dataY=[0, 0, 0, 0, 0, 0, 0];
    if(parkID)
    {
        dataX=[];
        dataY=[];
        for(var i in res.data)
        {
            for(var j in res.data[i])
            {
                if(j==0)
                {
                    dataX.push(res.data[i][j]);
                }
                else if(j==1)
                {
                    dataY.push(res.data[i][j])

                }
            }
        }

    }
    //今日金额
    Highcharts.setOptions({
        colors: ['#FF9600']
    });
    $('#charge_record').highcharts({
        chart: {
            backgroundColor:'',
            type: 'column',
            // spacingBottom: 65
        },
        title: {
            text: ''
        },
        exporting:{
            enabled:false,
        },
        legend: {
            enabled: false
        },
        credits:{
            enabled:false,
        },
        xAxis: {
            categories:dataX ,
            crosshair: false,
            tickWidth:0,
            lineWidth:3,
            lineColor:'#FF9600',
            labels:false
        },
        yAxis: {
            labels:false,
            gridLineWidth:0,
            min: 0,
            title: {
                text: ''
            }
        },
        tooltip: {
            formatter: function () {
                return this.x + ': ' + this.y;
            }
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [{
            name: '',
            data: dataY
        } ]
    });

}

//近七日系统异常
  function systemWarn(res) {
    if( !$("#system").length){
        return
    }
    var dataX = new Array();
    dataX= getThisWeekDate();
    var dataY = new Array();
    dataY=[0, 0, 0, 0, 0, 0, 0];
    if(parkID)
    {
        dataX=[];
        dataY=[];
        for(var i in res.data)
        {
            for(var j in res.data[i])
            {
                if(j==0)
                {
                    dataX.push(res.data[i][j]);
                }
                else if(j==1)
                {
                    dataY.push(res.data[i][j])

                }
            }
        }

    }
    Highcharts.setOptions({
        colors: ['#D63B3D',  '#143150', '#FFFFFF']
    });
    $('#system').highcharts({
        chart: {
            backgroundColor:'',
            type: 'areaspline',
        },
        title: {
            text: ' '
        },
        legend: {
            enabled: false
        },
        exporting:{
            enabled:false,
        },
        xAxis: {
            categories: dataX,
            tickWidth:0,
            lineWidth:3,
            lineColor:'#D63B3D',
            labels:false
        },
        yAxis: {
            gridLineWidth:0,
            title: {
                text: ''
            },
            labels:false,
        },
        tooltip: {
            formatter: function () {
                return this.x + ': ' + this.y;
            }
        },
        plotOptions: {
            area: {
                fillColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [0, Highcharts.getOptions().colors[0]],
                        [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                    ]
                },

                lineWidth: 1,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null
            }
        },

        credits: {
            enabled: false
        },
        series: [{
            name: '',
            data: dataY,
            marker: {
                enabled: false,
            },
        },
        ],
    });

}

//近七日停车剩余位数表
function parkingLot(res) {

    if( !$("#parkingLot").length){
        return
    }
    var dataX = new Array();
    dataX=getThisWeekDate();
    var dataY = new Array();
    dataY=[0, 0, 0, 0, 0, 0, 0];
    if(parkID)
    {
        dataX=[];
        dataY=[];
        for(var i in res.data)
        {
            for(var j in res.data[i])
            {
                if(j==0)
                {
                    dataX.push(res.data[i][j]);
                }
                else if(j == 1)
                {
                    dataY.push(res.data[i][j])

                }

            }
        }

    }

    Highcharts.setOptions({
        colors: ['#00BF9A']
    });
    $('#parkingLot').highcharts({
        chart: {
            backgroundColor:'',
            type: 'column'
        },
        title: {
            text: ''
        },
        //官方链接显示
        credits:{
            enabled:false,
        },
        //导出按钮显示
        exporting:{
            enabled:false,
        },
        //x轴样式设置
        xAxis: {
            tickWidth:0,
            categories: dataX,
            lineWidth:3,
            lineColor:'#0186E4',
            labels:false
        },
        //y轴样式设置
        yAxis: {
            labels:false,
            gridLineWidth:0,
            min: 0,
            title: {
                text: ''
            }
        },
        //图例显示
        legend: {
            enabled: false
        },
        tooltip: {
            formatter: function () {
                return this.x + ': ' + this.y;
            }
        },
        plotOptions: {
            column: {
                stacking: 'normal',
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        //数据列
        series: [
            {
                name: '',
                data: dataY
            }]
    });
}

function getThisWeekDate() {
    var date = new Date();
    var sepStr = "-";
    var strYear = date.getFullYear();
    var strMonth = date.getMonth() + 1;
    var lastMonthDay = 31;
    if (strMonth == 4 || strMonth == 6 || strMonth == 9 || strMonth == 11)
        lastMonthDay = 30;
    else if (strMonth == 2)
        lastMonthDay = 28;
    var strDate = date.getDate();
    var day1 = "";
    var day2 = "";
    var day3 = "";
    var day4 = "";
    var day5 = "";
    var day6 = "";
    var day7 = "";
    switch (strDate){
        case 6:
            day7 = strYear + sepStr + strMonth + sepStr + "6";
            day6 = strYear + sepStr + strMonth + sepStr + "5";
            day5 = strYear + sepStr + strMonth + sepStr + "4";
            day4 = strYear + sepStr + strMonth + sepStr + "3";
            day3 = strYear + sepStr + strMonth + sepStr + "2";
            day2 = strYear + sepStr + strMonth + sepStr + "1";
            day1 = strYear + sepStr + strMonth + sepStr + lastMonthDay;
            break;
        case 5:
            day7 = strYear + sepStr + strMonth + sepStr + "5";
            day6 = strYear + sepStr + strMonth + sepStr + "4";
            day5 = strYear + sepStr + strMonth + sepStr + "3";
            day4 = strYear + sepStr + strMonth + sepStr + "2";
            day3 = strYear + sepStr + strMonth + sepStr + "1";
            day2 = strYear + sepStr + strMonth + sepStr + lastMonthDay;
            day1 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-1);
            break;
        case 4:
            day7 = strYear + sepStr + strMonth + sepStr + "4";
            day6 = strYear + sepStr + strMonth + sepStr + "3";
            day5 = strYear + sepStr + strMonth + sepStr + "2";
            day4 = strYear + sepStr + strMonth + sepStr + "1";
            day3 = strYear + sepStr + strMonth + sepStr + lastMonthDay;
            day2 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-1);
            day1 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-2);
            break;
        case 3:
            day7 = strYear + sepStr + strMonth + sepStr + "3";
            day6 = strYear + sepStr + strMonth + sepStr + "2";
            day5 = strYear + sepStr + strMonth + sepStr + "1";
            day4 = strYear + sepStr + strMonth + sepStr + lastMonthDay;
            day3 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-1);
            day2 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-2);
            day1 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-3);
            break;
        case 2:
            day7 = strYear + sepStr + strMonth + sepStr + "2";
            day6 = strYear + sepStr + strMonth + sepStr + "1";
            day5 = strYear + sepStr + strMonth + sepStr + lastMonthDay;
            day4 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-1);
            day3 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-2);
            day2 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-3);
            day1 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-4);
            break;
        case 1:
            day7 = strYear + sepStr + strMonth + sepStr + "1";
            day6 = strYear + sepStr + strMonth + sepStr + lastMonthDay;
            day5 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-1);
            day4 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-2);
            day3 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-3);
            day2 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-4);
            day1 = strYear + sepStr + strMonth + sepStr + (lastMonthDay-5);
            break;
        default:
            day7 = strYear + sepStr + strMonth + sepStr + strDate;
            day6 = strYear + sepStr + strMonth + sepStr + (strDate-1);
            day5 = strYear + sepStr + strMonth + sepStr + (strDate-2);
            day4 = strYear + sepStr + strMonth + sepStr + (strDate-3);
            day3 = strYear + sepStr + strMonth + sepStr + (strDate-4);
            day2 = strYear + sepStr + strMonth + sepStr + (strDate-5);
            day1 = strYear + sepStr + strMonth + sepStr + (strDate-6);
            break;
    }

    var resArr = new Array(day1,day2,day3,day4,day5,day6,day7);
    return resArr;
}


//首页图表点击事件
function parkingSpace() {

    $("#subBtn2").trigger("click");
}

function parkingQuantity() {

    $("#subBtn2").trigger("click");
}

function chargeRecord() {

    $("#subBtn3").trigger("click");
}



function InitializationMenu() {
    if(document.readyState == "complete") {
        var h3List = document.getElementsByClassName("secondary")[0].children;
        for (var i = 0; i < h3List.length; i++) {
            var href = h3List[i].children[0].href;
            // var str = href.split('?');
            var cont = document.getElementById("loginUserName");
            var newHref = href+"&userName=" + cont.innerText;
            h3List[i].children[0].href = newHref;
        }
        window.clearInterval(timer1);
    }
}