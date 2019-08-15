/**
 * Created by 宏炜 on 2017-06-23.
 */
$(function(){

    initBuildingPieChart();
    personnelCompositionPieChart();
    initRoomChart();


    var start = new Date(new Date().Format("yyyy/MM/dd 00:00:00"));
    $("#timeSection").val(start.getTime());
    var now = new Date().Format("yyyy/MM/dd hh:mm:00");
    start = start.Format("yyyy/MM/dd hh:mm:00");


    // alert(now)
    // alert(start)
    initPlacesChart(start,now);
    initBuildingLineChart(start,now);
    initDevSelect();
    $('#queryUserType').chosen({
        width:"100%"
    });
    setInterval(inittimeData,60000);
});

function initBuildingPieChart(){
    var now = new Date();
    var start = new Date(now.getTime() - ( 1 * 60 * 1000)).Format("yyyy-MM-dd hh:mm:00");
    var end = now.Format("yyyy-MM-dd hh:mm:00");

    var params = {
        type: 3,
        timeType: 1,
        startMonth:start,
        stopMonth:end
    };
    var nodeName = new Array();
    var nodeValue = new Array();
    $.post(starnetContextPath + "/count/getCountMacList",params,function(res){
        if(res.result == 0){
            var data = res.data;
            if(data.length > 0){
                for(var i in data){
                    nodeName.push(data[i].org_node_name);
                    var node = new Object();
                    node.value = data[i].scount + data[i].tcount + data[i].ucount +  data[i].gcount;

                    node.name = data[i].org_node_name+node.value+"人";

                    nodeValue.push(node);
                }
            }else{
                var node = new Object();
                nodeName.push('默认区域');
                node.value = 0;
                node.name = '默认区域';
                nodeValue.push(node);
            }

            var pieChart = echarts.init(document.getElementById("echarts-pie-chart"));
            var pieoption = {
                title : {
                    text: '楼栋区域分布',
                    x:'center',
                    textStyle:{
                        fontFamily:'微软雅黑',
                        fontWeight:'900'
                    }
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                // legend: {
                //     orient : 'vertical',
                //     x : 'left',
                //     data:nodeName
                // },
                calculable : true,
                series : [
                    {
                        name:'详情：',
                        type:'pie',
                        radius : '40%',
                        center: ['50%', '57%'],

                        data:nodeValue
                    }
                ]
            };
            pieChart.setOption(pieoption);
            $(window).resize(pieChart.resize);
        }else{
            errorPrompt("楼栋区域分布信息加载失败",res.msg);
        }
    },'json');

}
function personnelCompositionPieChart(){
    var now = new Date();
    var start = new Date(now.getTime() - (1 * 60 * 1000)).Format("yyyy-MM-dd hh:mm:00");
    var end = now.Format("yyyy-MM-dd hh:mm:00");

    var params = {
        type: 3,
        timeType: 1,
        startMonth:start,
        stopMonth:end
    };
    var nodeName = new Array();
    var nodeValue = new Array();
    var tcount = 0;
    var gcount = 0;
    var scount = 0;
    var ucount = 0;
    $.post(starnetContextPath + "/count/getCountMacList",params,function(res){
        if(res.result == 0){
            var data = res.data;
            for(var i in data){
                tcount += data[i].tcount;
                gcount += data[i].gcount;
                scount += data[i].scount;
                ucount += data[i].ucount;
            }
            nodeName.push("保安人员");
            nodeName.push("黑名单人员");
            nodeName.push("特定人员");
            nodeName.push("外来人员");
            nodeValue.push({
                value:scount,
                name:"保安人员"+scount+"人"
            });
            nodeValue.push({
                value:tcount,
                name:"黑名单人员"+tcount+"人"
            });
            nodeValue.push({
                value:gcount,
                name:"特定人员"+gcount+"人"
            });
            nodeValue.push({
                value:ucount,
                name:"外来人员"+ucount+"人"
            });
            var personnelCompositionPieChart = echarts.init(document.getElementById("personnelCompositionPieChart"));
            var pieoption = {
                title : {
                    text: '楼栋人员构成',
                    x:'center',
                    textStyle:{
                        fontFamily:'微软雅黑',
                        fontWeight:'900'
                    }
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                // legend: {
                //     orient : 'vertical',
                //     x : 'left',
                //     data:nodeName
                // },
                calculable : true,
                series : [
                    {
                        name:'详情',
                        type:'pie',
                        radius : '40%',
                        center: ['50%', '57%'],
                        data:nodeValue
                    }
                ]
            };
            personnelCompositionPieChart.setOption(pieoption);
            $(window).resize(personnelCompositionPieChart.resize);
        }else{
            errorPrompt("楼栋人员构成信息加载失败",res.msg);
        }
    },'json');
}

function initRoomChart(){

    var now = new Date();
    var start = new Date(now.getTime() - (1 * 60 * 1000)).Format("yyyy-MM-dd hh:mm:00");
    var end = now.Format("yyyy-MM-dd hh:mm:00");

    var params = {
        type: 3,
        timeType: 1,
        startMonth:start,
        stopMonth:end
    };
    var nodeName = new Array();
    var nodeValue = new Array();
    nodeValue.push({
        type:'bar',
        name:"保安人员"
    });
    nodeValue.push({
        type:'bar',
        name:"黑名单人员"
    });
    nodeValue.push({
        type:'bar',
        name:"特定人员"
    });
    nodeValue.push({
        type:'bar',
        name:"外来人员"
    });
    var gcount = new Array();
    var tcount = new Array();
    var scount = new Array();
    var ucount = new Array();
    $.post(starnetContextPath + "/count/getCountMacList",params,function(res){
        if(res.result == 0) {
            var data = res.data;
            if(data.length > 0){
                for(var i in data){
                    nodeName.push(data[i].org_node_name);
                    gcount.push(data[i].gcount);
                    tcount.push(data[i].tcount);
                    scount.push(data[i].scount);
                    ucount.push(data[i].ucount);
                }
                nodeValue[0].data = scount;
                nodeValue[1].data = tcount;
                nodeValue[2].data = gcount;
                nodeValue[3].data = ucount;
            }{
                nodeName.push('默认区域');
                gcount.push(0);
                tcount.push(0);
                scount.push(0);
                ucount.push(0);
                nodeValue[0].data = scount;
                nodeValue[1].data = tcount;
                nodeValue[2].data = gcount;
                nodeValue[3].data = ucount;
            }


            var barChart = echarts.init(document.getElementById("echarts-bar-chart"));
            var baroption = {
                title : {
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:['保安人员', '黑名单人员', '特定人员','外来人员']
                },
                grid:{
                    x:30,
                    x2:40,
                    y2:24
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : nodeName
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : nodeValue
            };
            barChart.setOption(baroption);
            window.onresize = barChart.resize;
        }else{
            errorPrompt("楼栋人员构成信息加载失败",res.msg);
        }
    },'json');

}

function initPlacesChart(startTime,endTime){

    var params = {
        type: 3,
        timeType: 1,
        startMonth:startTime,
        stopMonth:endTime,
        nodeId:$("#nodeSelect").val()
    };

    var nodeName = new Array();
    var nodeValue = new Array();
    var xTimeLine = new Array();
    $.post(starnetContextPath + "/count/getCountMacList",params,function(res){
        if(res.result == 0) {
            var data = res.data;
            var placesName = res.placesName;
            for(var i in placesName){
                nodeName.push(placesName[i]);
                var j = data.length - 1;
                var nodeData = new Array();
                while(j >= 0){
                    if(data[j].org_node_name == placesName[i]) {
                        var countValue = 0;

                        if($("#queryUserType").val() == "0"){
                            countValue = data[j].scount;
                        }
                        if($("#queryUserType").val() == "1"){
                            countValue += data[j].tcount;
                        }
                        if($("#queryUserType").val() == "2"){
                            countValue += data[j].gcount;
                        }
                        if($("#queryUserType").val() == "3"){
                            countValue += data[j].ucount;
                        }
                        if($("#queryUserType").val() == ""){
                            countValue = data[j].scount + data[j].tcount + data[j].ucount + data[j].gcount;
                        }
                        nodeData.push(countValue);
                        if(i == 0){
                            xTimeLine.push(new Date(data[j].reccount_time).Format("hh:mm"));
                        }
                    }

                    j--;
                }
                var nodeValueObject = new Object();
                nodeValueObject.type = 'line';
                nodeValueObject.name = placesName[i];
                nodeValueObject.data = nodeData;
                nodeValue.push(nodeValueObject);
            }


            var lineChart = echarts.init(document.getElementById("echarts-line-chart"));
            var lineoption = {
                title : {
                    text:'',
                    textStyle:{
                        fontFamily:'微软雅黑',
                        fontWeight:'900'
                    }
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:nodeName
                },
                grid:{
                    x:40,
                    x2:40,
                    y2:24
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        boundaryGap : false,
                        data : xTimeLine
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value} 人'
                        }
                    }
                ],
                series : nodeValue
            };
            lineChart.setOption(lineoption);
            $(window).resize(lineChart.resize);
        }else{
            errorPrompt("楼内人数走势信息加载失败",res.msg);
        }
    },'json');

}

function initBuildingLineChart(startTime,endTime){
    var params = {
        type: 3,
        timeType: 1,
        startMonth:startTime,
        stopMonth:endTime
    };
    var nodeName = ['全区走势'];
    var nodeValue = new Array();
    var xTimeLine = new Array();
    $.post(starnetContextPath + "/count/getCountMacList",params,function(res){
        if(res.result == 0) {
            var data = res.data;
            var placesName = res.placesName;
            for(var i in placesName){

                //305

                var j = data.length - 1;
                var nodeData = new Array();
                while(j >= 0){
                    if(data[j].org_node_name == placesName[i]) {
                        var countValue = data[j].scount + data[j].tcount + data[j].ucount;
                        nodeData.push(countValue);
                        if(i == 0){
                            xTimeLine.push(new Date(data[j].reccount_time).Format("hh:mm"));
                        }
                    }
                    j--;
                }

                var nodeValueObject = new Object();
                nodeValueObject.type = 'line';
                nodeValueObject.name = '全区走势';
                nodeValueObject.data = nodeData;
                if(i == 0){
                    nodeValue.push(nodeValueObject);
                }else{
                    var totalNode = nodeValue[0];
                    for(var i in totalNode.data){
                        totalNode.data[i] += nodeData[i];
                    }
                }
            }
            var lineChart = echarts.init(document.getElementById("building-line-chart"));
            var lineoption = {
                title : {
                    text:'',
                    textStyle:{
                        fontFamily:'微软雅黑',
                        fontWeight:'900'
                    }
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:nodeName
                },
                grid:{
                    x:40,
                    x2:40,
                    y2:24
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        boundaryGap : false,
                        data : xTimeLine
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value} 人'
                        }
                    }
                ],

                series:nodeValue
                // series: [
                //     {
                //         name :'全区走势',
                //         type:'line',
                //         itemStyle : {
                //             normal : {
                //                 lineStyle:{
                //                     width:0.001
                //                 }
                //             }
                //         },
                //         data:nodeValue[0].data
                //     }
                // ]
            };
            lineChart.setOption(lineoption);
            $(window).resize(lineChart.resize);
        }else{
            errorPrompt("园区人数走势信息加载失败",res.msg);
        }
    },'json');

}

function initDevSelect(){

    $.post(starnetContextPath + "/node/getAllNode",function(res){
        if(res.result == 0){
            $('#nodeSelect').empty();
            var defaultItem = '<option value="">全部位置</option>';
            $('#nodeSelect').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].orgNodeName + '</option>';
                $('#nodeSelect').append(item);
            }
            $('#nodeSelect').chosen({
                width:"100%"
            });

            $('#nodeSelect').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}
$("#nodeSelect").chosen().on("change", function (evt, params) {
    var start = new Date(parseInt($("#timeSection").val()));
    var end = new Date(start.getTime() + (24 * 60 * 60 * 1000));
    start = start.Format("yyyy-MM-dd hh:mm:00");
    end = end.Format("yyyy-MM-dd hh:mm:00");
    initPlacesChart(start,end);
});
$("#queryUserType").chosen().on("change", function (evt, params) {
    var start = new Date(parseInt($("#timeSection").val()));
    var end = new Date(start.getTime() + (24 * 60 * 60 * 1000));
    start = start.Format("yyyy-MM-dd hh:mm:00");
    end = end.Format("yyyy-MM-dd hh:mm:00");
    initPlacesChart(start,end);
});
$("#lastDay").click(function(){
    var end = new Date(parseInt($("#timeSection").val()));
    var start = new Date(end.getTime() - (24 * 60 * 60 * 1000));
    $("#timeSection").val(start.getTime());
    start = start.Format("yyyy-MM-dd hh:mm:00");
    end = end.Format("yyyy-MM-dd hh:mm:00");
    initPlacesChart(start,end);
    initBuildingLineChart(start,end);

});
$("#today").click(function(){
    var start = new Date(new Date().toLocaleDateString());
    var now = new Date().Format("yyyy-MM-dd hh:mm:00");
    $("#timeSection").val(start.getTime());
    start = start.Format("yyyy-MM-dd hh:mm:00");
    initPlacesChart(start,now);
    initBuildingLineChart(start,now);
});
$("#nextDay").click(function(){
    var start = new Date(parseInt($("#timeSection").val()));
    start = new Date(start.getTime() + (24 * 60 * 60 * 1000));
    var end = new Date(start.getTime() + (24 * 60 * 60 * 1000));
    var now = new Date();
    if(start.getTime() > now.getTime()){
        return;
    }
    $("#timeSection").val(start.getTime());
    start = start.Format("yyyy-MM-dd hh:mm:00");
    end = end.Format("yyyy-MM-dd hh:mm:00");
    initPlacesChart(start,end);
    initBuildingLineChart(start,end);
});

function inittimeData(){
    initBuildingPieChart();
    personnelCompositionPieChart();
    initRoomChart();

    var start = new Date(new Date().Format("yyyy/MM/dd 00:00:00"));
    $("#timeSection").val(start.getTime());
    var now = new Date().Format("yyyy-MM-dd hh:mm:00");
    start = start.Format("yyyy-MM-dd hh:mm:00");
    initPlacesChart(start,now);
    initBuildingLineChart(start,now);
    initDevSelect();
    $('#queryUserType').chosen({
        width:"100%"
    });

}