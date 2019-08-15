<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2017-07-04
  Time: 15:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>首页</title>
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${pageContext.request.contextPath}/resources/admin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/radialindicator.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/home.css" rel="stylesheet">
    <style>
        .sx1 {
            position: absolute;
            width: 200px;
            height: 200px;
            transform: rotate(-180deg);
            clip: rect(0px, 100px, 200px, 0px);
            border-radius: 100px;
            background-color: #015F96;
        }
        .sx2 {
            position: absolute;
            width: 200px;
            height: 200px;
            transform: rotate(-50deg);
            clip: rect(0px, 100px, 200px, 0px);
            border-radius: 100px;
            background-color: #015F96;
        }
        body{
            overflow:scroll;
            overflow-y:hidden;
            overflow-x:hidden;
        }
        td{
            border: solid 1px rgba(0,151,255,0.3);
            text-align: center;
            color: #0096FF;
        }

        #rowContainer tr:hover{
            background-color: #003C6F;
        }
        #rowContainer tr{
            height:50px;
            max-height: 50px;
        }

        table{
            border: solid 1px rgba(0,151,255,0.3);
            border-right:solid 2px #082F4E;
            border-left:solid 2px #082F4E;
            width: 100%;
            height: 100%;

        }
        .tdColor td{
            color: #FF9600;
        }
        .mapBackBtn{
            background-color: #082F4E;position: absolute;left: 10px;top: 10px;color: #0096FF;z-index: 999999;
            font-size: 12px;display: none;
        }
        .mapBackBtn:hover{
            color: #FFFFFF;
            background-color: #003C6F;
        }
    </style>
    <%--<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=n4dbtj9LyddAtuAgYbHQHGffpz1KRcct"></script>--%>
    <script>
        var homeText;
    </script>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body style="background-color: #082F4E;padding-top: 5px;overflow-y:auto" id="bodyContainer">
<!--右侧边栏开始-->
<div class="col-xs-2" style="height: 73%">
    <div style="border: solid 1px rgba(0,151,255,0.6);background: #03375c;height: 100%;">
        <div class="ibox-title1 " style="border-bottom:solid 1px #0066c2;" >
            <h4 class="nav-label">今日</h4>
        </div>
        <div class="col-xs-12" id="echarts-pie-chart" style="height: 40%;margin-top: 3%" >

        </div>
        <div class="col-xs-12" id="personnelCompositionPieChart" style="height: 40%;margin-top: 3%"  >

        </div>
    </div>
</div>
<div class="col-xs-5"style="height: 73%;position:relative;"  >
    <div id="videoPanel" style="height: 100%;width: 100%">
        <div style="margin-top: 3px">
           <select id="deviceInfosSelect" class="form-control" style="width: 200px;background-color: #082F4E;color: #FFFFFF;border-color: rgba(0,151,255,0.6);">

           </select>
        </div>
        <div style="margin-top: 6px">
            <OBJECT
                    id="iparkOcx"
                    classid="clsid:7C5943E3-E9EF-410B-BC54-DE68C2B5627A"
                    height="300"
                    width="100%"
                    align="center"
                    hspace="0"
                    vspace="0"
                    wHttpPort="554"
            >
            </OBJECT>
        </div>
    </div>
    <%--<button id="mapBackBtn" class="btn btn-success mapBackBtn" onclick="mapBack()">--%>
        <%--<i class="fa fa-reply" ></i>&nbsp;返回--%>
    <%--</button>--%>
    <%--<div id="echarts-map-chart" style="height: 100%;width: 100%">--%>

    <%--</div>--%>

</div>
<div class="col-xs-5"style="height: 73%;overflow-y: hidden"  >
    <div  style="background-color: #082F4E;border-top: solid 1px rgba(0,151,255,0.6)" >
        <table>
            <tbody id="rowContainer">
            </tbody>
        </table>
    </div>
</div>
<div class="col-xs-12" style="border-top: solid 1px rgba(0,151,255,0.6);margin-top: 5px;" >
    <div  style="background-color: #ffffff;height: 100%;width: 100%" id="container" value="ddd" >

    </div>
</div>

<!-- 全局js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/jquery.min.js?v=2.1.4"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<!-- Morris -->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/morris/raphael-2.1.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/morris/morris.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/echarts/echarts-all.js"></script>
<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>
<!-- 自定义js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/content.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/radialIndicator.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/run_line.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/highstock.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/highcharts-zh_CN.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/sockJS/sockjs.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/ipark_ocx.js"></script>
<script>
    var mapZindex = 0;
    var mapProvince = "";
    var trendArray = new Array();

    $(function() {
        initWebSocket();
        homeInit();
        initOCX();
        setTimeout("autoOcxHeight()",450);
    });

    function autoOcxHeight() {
        var height = $("#videoPanel").height() - 42;
        document.getElementById("iparkOcx").height = height;
    }

    function initWebSocket(){
        //初始化websocket
        var websocket;
        // 首先判断是否 支持 WebSocket
        if('WebSocket' in window) {
            websocket = new WebSocket("ws://" + window.location.host + "/websocket");
        } else if('MozWebSocket' in window) {
            websocket = new MozWebSocket("ws://" + window.location.host + "/websocket");
        } else {
            websocket = new SockJS("http://" + window.location.host + "/sockjs/websocket");
        }

        // 打开时
        websocket.onopen = function(evnt) {
            var msg = {
                userId:"${$_LoginUser.id}"
            }
            websocket.send(JSON.stringify(msg));
        };
        // 处理消息时
        websocket.onmessage = function(evnt) {
            var current = window.parent.parkID;
            var msg = JSON.parse(evnt.data);
            if(current != msg["parkId"]){
                return;
            }

            var item = '<tr> ' +
                    '<td>' + msg["carNo"] + '</td> ' +
                    '<td>' + msg["time"] + '</td>' +
                    '<td>' + msg["port"] + '</td>' +
                    '<td>' + msg["info"] + '</td> ' +
                    '</tr>';
            $("#rowContainer").prepend(item);
            trendArray.push(msg);
            //重新加载首页数据
           window.parent.parkData();

            getChargeRecords();
            getInoutRecords();

//            realTrendChart();

        };
        websocket.onerror = function(evnt) {

        };
        websocket.onclose = function(evnt) {
            setTimeout("initWebSocket()",20000);

        };
    }

    function homeInit(){
//        renderMap();
        initIpcSelect();
        trendChart();
        getChargeRecords();
        getInoutRecords();
        initRowContainer();

    }

    //渲染地图
    function renderMap(){
//        $.post(starnetContextPath + '/carpark/getParksMapInfoList',function (res) {
//            if(res.result == 0){
//                initMap(res.data)
//            }else{
//                alert(res.msg);
//            }
//        },'json');
    };

    //初始化实时进出信息
    function initRowContainer(){
        $("#rowContainer").empty();
        var params = {
            "carparkId":window.parent.parkID
        };

        $.post(starnetContextPath + "/carpark/last/inout/record",params,function (res) {
            if(res.result == 0){
                for(var i in res.data){
                    var item = '<tr> ' +
                            '<td>' + res.data[i]["in_out_car_no"] + '</td> ' +
                            '<td>' + res.data[i]["in_out_time"] + '</td>' +
                            '<td>' + res.data[i]["road_name"] + '</td>' +
                            '<td>' + res.data[i]["real_amount"] + '</td> ' +
                            '</tr>';
                    $("#rowContainer").prepend(item);
                }
            }
        },'json');
    }

    //地图返回
    function mapBack(){
        if(mapZindex > 0){
            if(mapZindex == 2){
                var option  = myChart.getOption();
                option.series[0].mapType = mapProvince;
                myChart.setOption(option, true);
            }else{
                $("#mapBackBtn").hide();
                var option  = myChart.getOption();
                option.series[0].mapType = 'china';
                option.series[0].roam = false;
                myChart.setOption(option, true);
            }
            mapZindex--;
        }
    }
    //初始化拓扑地图
    function initMap(data) {
        var parks = new Array();
        var geoCoord ={};
        for(var i in data){
            var item = {
                name:data[i].park_name,
                value:0
            }
            geoCoord[data[i].park_name] = [
                data[i].lon,
                data[i].lat
            ];
            parks.push(item);
        }
        var mapType = [];
        for (var city in cityMap) { //加载14市的县级地图数据
            mapType.push(city);//将14个市加入到params.js文件中
            // 自定义扩展图表类型
            //   mapGeoData.params[city] = { //回调，加载14个市对应的县级数据
            echarts.util.mapData.params.params[city] = { //回调，加载14个市对应的县级数据
                getGeoJson: (function (c) {
                    var geoJsonName = cityMap[c];
                    return function (callback) {
                        $.getJSON(starnetContextPath + '/resources/js/china-main-city/' + geoJsonName + '.json', callback);
                    }
                })(city)
            }
        }
        myChart = echarts.init(document.getElementById("echarts-map-chart"));
        var option = {
            title: {},
            tooltip: {
                trigger: 'item',
                formatter: '{b}'
            },
            series: [
                {
                    name: '停车场',
                    type: 'map',
                    mapType: 'china',
                    selectedMode: 'single',
                    itemStyle: {
                        normal: {
                            borderWidth: 1,
                            borderColor: 'rgba(0,151,255,0.3)',
                            color: '#03375C',
                            label: {
                                show: true,
                                textStyle: {
                                    color: '#fff'
                                }
                            }
                        },
                        emphasis: {                 // 也是选中样式
                            borderWidth: 1,
                            borderColor: '#003C65',
                            color: '#3897C5',
                            label: {
                                show: true,
                                textStyle: {
                                    color: '#fff'
                                }
                            }
                        }
                    },
                    mapType: 'china',
                    hoverable: true,
                    roam: true,
                    data: [],
                    markPoint: {
                        symbolSize: 5,       // 标注大小，半宽（半径）参数，当图形为方向或菱形则总宽度为symbolSize * 2
                        itemStyle: {
                            normal: {
                                borderColor: '#87cefa',
                                color: '#3897C5',
                                borderWidth: 1,            // 标注边线线宽，单位px，默认为1
                                label: {
                                    show: false
                                }
                            },
                            emphasis: {
                                borderColor: '#1e90ff',
                                color: '#3897C5',
                                borderWidth: 2,
                                label: {
                                    show: false
                                }
                            }
                        },
                        data: parks
                    },
                    geoCoord: geoCoord
                }
            ]
        };
        myChart.on(echarts.config.EVENT.MAP_SELECTED, function (param) {
            if (mapZindex < 2) {
                option.series[0].mapType = param.target;
                option.series[0].roam = true;
                myChart.setOption(option, true);
                mapZindex++;
                if (mapZindex == 1) {
                    mapProvince = param.target;
                    $("#mapBackBtn").show();
                }
            }
        });
        myChart.setOption(option);
        $(window).resize( myChart.resize);
    }


    function activeLastPointToolip(chart) {
        var points = chart.series[0].points;
        //chart.tooltip.refresh(points[points.length -1]);
    }

    //车辆趋势动态图
    function trendChart()
    {

        if( !$("#container").length){
            return
        }
        Highcharts.setOptions({
            global: {
                useUTC: false
            }
        });
        Highcharts.setOptions({
            colors: ['#0178BB',  '#FFFFFF']
        });

        // 创建表
        var x1=(new Date()).getTime()-(16* 60 * 1000);
        var i=0;
        $('#container').highcharts('StockChart', {
            chart: {
                height:146,
                type:'area',
                backgroundColor: '#082E4E',
                events: {
                    load: function () {
                        var series = this.series[0],
                                chart = this;
                        setInterval(function () {

                            var x = (new Date()).getTime(), // current time
                                    y;
                            y = trendArray.length;
                            var i = 0;
                            while(i < y){
                                i++;
                                trendArray.pop();
                            }
                            series.addPoint([x, y], true, true);
                            activeLastPointToolip(chart);
                        }, 2000);

                    }
                }
            },
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 100,
                tickColor:'#FFFFFF',

            },
            navigator:{
                height:2,
                handles:{
                    backgroundColor: '#0096FF',

                },
                outlineColor:'#0096FF',
            },
            tooltip: {
                formatter: function () {
                    return new Date(this.x).Format("yyyy-MM-dd hh:mm:00") + ': '+ this.y+'辆';
                }
            },
            scrollbar: {
                height:2,
                barBackgroundColor: '#064371',
                barBorderRadius: 0,
                barBorderWidth: 0,
                buttonBackgroundColor: '#064371',
                buttonBorderWidth: 0,
                buttonArrowColor: 'white',
                buttonBorderRadius: 0,
                // rifleColor: '#064371',
                trackBackgroundColor: '#064371',
                trackBorderWidth: 1,
                trackBorderColor: '#064371',
                trackBorderRadius: 7,
                handleColor:'#064371',

            },
            yAxis: {
                gridLineColor:'#20435F',
                tickColor:'#FFFFFF',
                min:0,
                labels: {
                    formatter: function() {
                        return '<span style="fill: #BDC1D1;">' + this.value + '</span>';

                    }
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
                        radius: 2
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

            rangeSelector: {
                buttonTheme: { // 按钮样式
                    fill: '#062239',
                    stroke: 'none',
                    'stroke-width': 0,
                    style: {
                        color: '#D2E0CB',
                        fontWeight: 'bold'
                    },
                    states: {
                        hover: {
                            fill: '#015F96',
                        },
                        select: {
                            fill: '#015F96',
                            style: {
                                color: 'white'
                            }
                        }
                    }
                },
                labelStyle: {
                    color: '#D2E0CB',
                    fontWeight: 'bold'
                },

                buttons: [{
                    //     count: 1,
                    //     type: 'minute',
                    //     text: '1M',
                    //
                    //
                    // }, {
                    count: 5,
                    type: 'minute',
                    text: '5M'
                }, {

                    type: 'all',
                    text: 'All'
                }],
                inputEnabled: false,
                enabled: false,
                // selected: 2
            },
            title: {
                text: '车辆趋势',
                style: {
                    fontSize:"17px",
                    color: "#87CEFA",
                }
            },
            exporting: {
                enabled: false
            },
            series : [{
                data : (function () {
                    var data = [],
                            time = (new Date()).getTime(),
                            i;
                    for (i = -19; i <= 0; i += 1) {
                        data.push({
                            x: time + i * 1000,
                            y: 0
                        });
                    }
                    return data;
                }())
            }]
        });
    }


    //金额饼图
    function getChargeRecords() {

        if( !$("#echarts-pie-chart").length){
            return
        }
//        var money = 0, moneyPart = [{value:11, name: '测试'}],res2;

        if((window.parent.parkID) != 0){
            //今日总收费记录
//            $.ajax({
//                type : 'post',
//                async : false,
//                data:{
//                    "parkId":window.parent.parkID
//                },
//                dataType:'json',
//                url :starnetContextPath + "/charge/getChargeRecordsToday",
//                success : function(res) {
//                    if(res.data.length==0)
//                    {
//                        money=0;
//                    }
//                    else {
//                        money=res.data[0][1];
//                    }
//                }
//            });

            //今日总收费记录组成
            $.ajax({
                type : 'post',
                async : true,
                data:{
                    parkId:window.parent.parkID
                },
                dataType:'json',
                url :starnetContextPath + "/charge/getChargeRecordsTodayDetail",
                success : function(res) {
                    var moneyPart = [{}];
                    var money = 0
                    for(var i in res.data)
                    {
                        var obj = {};
                        obj.value = res.data[i]["fee_value"];
                        obj.name = res.data[i]["pay_type"];
                        moneyPart.push(obj);
                        money += res.data[i]["fee_value"];
                    }

                    var pieChart = echarts.init(document.getElementById("echarts-pie-chart"));
                    var pieoption = {
                        title: {
                            text: money+'元' + "\n" +
                            '总金额',
                            x: 'center',
                            y: 'center',
                            textStyle: {
                                fontSize: 15,
                                // fontFamily:'Microsoft YaHei',
                                color: '#0091FA',
                                fontWeight: '500'
                            }
                        },

                        color: ['rgb(0,150,255)', 'rgb(255,74,77)', 'rgb(255,157,60)', 'rgb(0,221,159)', 'rgb(0,104,199)'],
                        tooltip: {
                            textStyle: {
                                align: 'center'
                            },
                            trigger: 'item',
                            formatter: "{a} <br/>{b}: {c} 元 ({d}%)"
                        },
                        calculable: true,
                        formatter: function (val) {
                            return val.split("-").join("\n");
                        },

                        series: [
                            {
                                itemStyle: {
                                    normal: {
                                        formatter: function (val) {   //让series 中的文字进行换行
                                            return val.name.split("-").join("\n");
                                        },
                                        label: {
                                            show: false,
                                            formatter: '{d}%'
                                        },
                                        labelLine: {
                                            show: false
                                        }
                                    },
                                    emphasis: {
                                        label: {
                                            show: false
                                        },
                                        labelLine: {
                                            show: false
                                        }
                                    }
                                },
                                allowPointSelect: true,
                                name: '详情：',
                                type: 'pie',
                                radius: ['60%', '80%'],
                                avoidLabelOverlap: false,
                                data:moneyPart
                            }
                        ]
                    };

                    pieChart.setOption(pieoption);
                    $(window).resize(pieChart.resize);

                }
            });

        }

    }


    //今日停车流量饼图
    function getInoutRecords() {
        if( !$("#personnelCompositionPieChart").length){
            return
        }
//        var car=0,carPart=[{value:151, name: '测试'}],res4;
//        if((window.parent.parkID)!=0) {
//            //今日停车场流量
//            $.ajax({
//                type : 'post',
//                async : false,
//                data:{
//                    parkId:window.parent.parkID
//                },
//                dataType:'json',
//                url :starnetContextPath + "/inout/getInoutRecordsToday",
//                success : function(res) {
//                    if(res.data.length == 0)
//                    {
//                        car=0;
//                    }
//                    else {
//                        car=res.data[0][1];
//                    }
//                }
//            });

        //今日停车场记录组成
        $.ajax({
            type : 'post',
            async : true,
            data:{
                parkId:window.parent.parkID
            },
            dataType:'json',
            url :starnetContextPath + "/inout/getInoutRecordsTodayDetail",
            success : function(res) {

                var carPart = [{}];
                var carSum = 0;
                for(var i in res.data)
                {
                    var obj = {};
                    obj.value = res.data[i]["car_count"];
                    obj.name = res.data[i]["car_type"];
                    carPart.push(obj);
                    carSum += res.data[i]["car_count"];
                }
                var personnelCompositionPieChart = echarts.init(document.getElementById("personnelCompositionPieChart"));
                var pieoption = {
                    title: {
                        text: carSum + "\n" +
                        '车流量',
                        x: 'center',
                        y: 'center',
                        textStyle: {
                            fontSize: 15,
                            color: '#0091FA',
                            fontWeight: '500'
                        }
                    },
                    tooltip: {
                        textStyle: {
                            align: 'center'
                        },
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
                    color: ['rgb(0,150,255)', 'rgb(255,74,77)', 'rgb(255,157,60)', 'rgb(0,221,159)', 'rgb(0,104,199)'],
                    calculable: true,
                    series: [

                        {

                            itemStyle: {
                                normal: {
                                    formatter: function (val) {   //让series 中的文字进行换行
                                        return val.name.split("-").join("\n");
                                    },
                                    label: {
                                        show: false,
                                        formatter: '{d}%'
                                    },
                                    labelLine: {
                                        show: false
                                    }
                                },
                                emphasis: {
                                    label: {
                                        show: false
                                    },
                                    labelLine: {
                                        show: false
                                    }
                                }
                            },
                            allowPointSelect: true,
                            name: '详情：',
                            type: 'pie',
                            radius: ['60%', '80%'],
                            avoidLabelOverlap: false,
                            data:carPart
                        }
                    ]
                };
                personnelCompositionPieChart.setOption(pieoption);
                $(window).resize(personnelCompositionPieChart.resize);
            }
        });

    }


    var ocxObject;
    function initOCX(){
        ocxObject = new ocxFuncObject();

        var obj = $('#deviceInfosSelect option:selected');
        var ip = $(obj).attr("ip");
        var username = $(obj).attr("username");
        var pwd = $(obj).attr("pwd");
        var params = {
            ip:ip,
            username:username,
            pwd:pwd
        }
        playOcx(params);
    }

    function playOcx(params){
        ocxObject.init({
            "url":params["ip"],
            "wServerPort":"8081",
            "username":params["username"],
            "password":params["pwd"],
            "channel":"1",
            "trantype":"Tcp",
            "streamType":"1",
            "httpport":"554",
            "ocxObjectId":"iparkOcx",
            "drawLine":false,
            "drawRectange":false,
            "drawRegionX":"0",
            "drawRegionY":"0",
            "drawRegionW":"0",
            "drawRegionH":"0",
            "drawPX1":"0",
            "drawPY1":"0",
            "drawPX2":"0",
            "drawPY2":"0",
            "drawPX3":"0",
            "drawPY3":"0",
            "drawPX4":"0",
            "drawPY4":"0",
            "drawPX5":"0",
            "drawPY5":"0"
        });
        var height = $("#videoPanel").height() - 42;
        document.getElementById("iparkOcx").height = height;
        ocxObject.startView();
    }

    $("#deviceInfosSelect").change(function () {
        var obj = $('#deviceInfosSelect option:selected');
        var ip = $(obj).attr("ip");
        var username = $(obj).attr("username");
        var pwd = $(obj).attr("pwd");
        var params = {
            ip:ip,
            username:username,
            pwd:pwd
        }
        playOcx(params);
    });

    window.onbeforeunload = onclose;
    function onclose()
    {
        ocxObject.stopView();
    }
    function initIpcSelect(){
        var params = {
            "parkId":window.parent.parkID
        };

        $.post(starnetContextPath + "/device/manage/park/device/list",params,function (res) {
            if(res.result == 0){
                $("#deviceInfosSelect").empty();
                for(var i in res.data){
                    if(i == 0){
                        var item = '<option ip="'+ res.data[i]['deviceIp'] +'" username="'+ res.data[i]['deviceUsername'] +'" pwd="'+ res.data[i]['devicePwd'] +'" value="'+ res.data[i]['deviceId'] +'" selected>'+ res.data[i]['deviceName'] +'</option>';
                        $("#deviceInfosSelect").append(item);
                    }else{
                        var item = '<option ip="'+ res.data[i]['deviceIp'] +'" username="'+ res.data[i]['deviceUsername'] +'" pwd="'+ res.data[i]['devicePwd'] +'" value="'+ res.data[i]['deviceId'] +'">'+ res.data[i]['deviceName'] +'</option>';
                        $("#deviceInfosSelect").append(item);
                    }

                }

                var obj = $('#deviceInfosSelect option:selected');
                var ip = $(obj).attr("ip");
                var username = $(obj).attr("username");
                var pwd = $(obj).attr("pwd");
                var params = {
                    ip:ip,
                    username:username,
                    pwd:pwd
                }
                playOcx(params);
            }
        },'json');
    }
</script>


</body>
</html>
