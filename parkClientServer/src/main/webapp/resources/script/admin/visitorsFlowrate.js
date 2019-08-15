/**
 * Created by 宏炜 on 2017-06-23.
 */
$(function(){

    // Todaecharts();


    $('#data_5 .input-daterange').datepicker({
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true
    });
    var dayBegin = new Date();
    var start =new Date(dayBegin.setDate(dayBegin.getDate() - (8))).Format("yyyy-MM-dd hh:mm:ss");
    var now = new Date().Format("yyyy-MM-dd hh:mm:ss");
    // var start = new Date().Format("yyyy-MM-dd 00:00:00");
    // var now = new Date().Format("yyyy-MM-dd hh:mm:ss");
    var params = {
        type: 3,
        timeType: 2,
        startMonth:start,
        stopMonth:now
    };
    initBuildingLineChart("全区走势",params);


    $('#nodeSelect').chosen({
        width:"100%"
    });
    $('#sectionSelect').chosen({
        width:"100%"
    });

    $("#sectionSelect").chosen().on("change", function (evt, params) {

        $("#echarts-line-Today").hide("slow");
//只改这
       if($("#sectionSelect").val()==4)
       { $("#search").toggle();
       }
       else {
           $("#search").hide();

       }

//只改这
        var orgs = $("#orgTree").jstree().get_selected();
        var lineName = "";
        var orgId;
        if(orgs.length == 0){
            orgId = "";
        }else{
            orgId = orgs[0];
        }
        if(orgId != ""){
            var selectedNode = $("#orgTree").jstree().get_node(orgId);
            if(selectedNode){
                lineName = selectedNode.text;
            }else{
                lineName = "全区走势"
            }
        }else{
            lineName = "全区走势"
        }

        if(params.selected == 1){
            var start = new Date().Format("yyyy-MM-dd 00:00:00");
            var now = new Date().Format("yyyy-MM-dd hh:mm:ss");
            var chartParams = {
                type: 3,
                timeType: 2,
                startMonth:start,
                stopMonth:now,
                orgId:orgId,
                nodeId:$("#nodeSelect").val()
            };
            initBuildingLineChart(lineName,chartParams);
        }else if(params.selected == 2){
            var dayBegin = new Date();
            var start =new Date(dayBegin.setDate(dayBegin.getDate() - (8))).Format("yyyy-MM-dd hh:mm:ss");
            var now = new Date().Format("yyyy-MM-dd hh:mm:ss");
            var chartParams = {
                type: 3,
                timeType: 2,
                startMonth:start,
                stopMonth:now,
                orgId:orgId,
                nodeId:$("#nodeSelect").val()
            };
            initBuildingLineChart(lineName,chartParams);
        }else if(params.selected == 3){
            var dayBegin = new Date();
            var start =new Date(dayBegin.setDate(dayBegin.getDate() - (31))).Format("yyyy-MM-dd hh:mm:ss");
            var now = new Date().Format("yyyy-MM-dd hh:mm:ss");
            var chartParams = {
                type: 3,
                timeType: 2,
                startMonth:start,
                stopMonth:now,
                orgId:orgId,
                nodeId:$("#nodeSelect").val()
            };
            initBuildingLineChart(lineName,chartParams);
        }

    });

    $("#nodeSelect").chosen().on("change", function (evt, params) {
        var orgs = $("#orgTree").jstree().get_selected();
        var lineName = $("#nodeSelect").find("option:selected").text();
        var orgId;
        if(orgs.length == 0){
            orgId = "";
        }else{
            orgId = orgs[0];
        }


        if($("#sectionSelect").val() == 1){
            var start = new Date().Format("yyyy-MM-dd 00:00:00");
            var now = new Date().Format("yyyy-MM-dd hh:mm:ss");
            var chartParams = {
                type: 3,
                timeType: 2,
                startMonth:start,
                stopMonth:now,
                orgId:orgId,
                nodeId:params.selected
            };
            initBuildingLineChart(lineName,chartParams);
        }else if($("#sectionSelect").val() == 2){
            var dayBegin = new Date().Format("yyyy-MM-dd 00:00:00");
            var start = new Date(dayBegin.getTime() - (8 * 24 * 60 * 60 * 1000)).Format("yyyy-MM-dd hh:mm:ss");
            var now = new Date().Format("yyyy-MM-dd hh:mm:ss");
            var chartParams = {
                type: 3,
                timeType: 2,
                startMonth:start,
                stopMonth:now,
                orgId:orgId,
                nodeId:params.selected
            };
            initBuildingLineChart(lineName,chartParams);
        }else if($("#sectionSelect").val() == 3){
            var dayBegin = new Date().Format("yyyy-MM-dd 00:00:00");
            var start = new Date(dayBegin.getTime() - (31 * 24 * 60 * 60 * 1000)).Format("yyyy-MM-dd hh:mm:ss");
            var now = new Date().Format("yyyy-MM-dd hh:mm:ss");
            var chartParams = {
                type: 3,
                timeType: 2,
                startMonth:start,
                stopMonth:now,
                orgId:orgId,
                nodeId:params.selected
            };
            initBuildingLineChart(lineName,chartParams);
        }

    });

    initTree("");
    var treeHeight = document.body.offsetHeight - 95;
    $('.full-height-scroll').slimScroll({
        height: treeHeight + 'px'
    });


    $("#searchBtn").click(function(){
        // alert($("#orgId").val())
        var chartParamsSearchBtn = {
            type: 3,
            timeType: 2,
            start: $("#start").val(),
            end: $("#end").val(),
            orgId: $("#orgId").val()
        };
        var lineName = $("#nodeSelect").find("option:selected").text();
        initBuildingLineChart(lineName,chartParamsSearchBtn);
        $(".message").toggle();


    });

});



var s;
var d;
var c;


function initTree(orgId){
    $.post(starnetContextPath + "/org/orgTree",function(res){
        if(res.result == 0){
            $('#orgTree').jstree('destroy');
            $('#orgTree').jstree({
                'core': {
                    'multiple':false,
                    'data':res.data
                }
            });

            $("#orgTree").on("loaded.jstree",function(){
                if(orgId != "") {
                    $("#orgTree").jstree().select_node(orgId, false, false);
                }
            });
            $("#orgTree").on("select_node.jstree",function(event,node){
                var data = node.node;
                s=data;
                d=data.id;
                c=data.text;

                $('#orgId').val(data.id);
                initDevSelect(data.id);
                if($("#sectionSelect").val() == 1){
                    var start = new Date().Format("yyyy-MM-dd 00:00:00");
                    var now = new Date().Format("yyyy-MM-dd hh:mm:ss");
                    var chartParams = {
                        type: 3,
                        timeType: 2,
                        startMonth:start,
                        stopMonth:now,
                        orgId:data.id
                    };
                    initBuildingLineChart(data.text,chartParams);
                }else if($("#sectionSelect").val() == 2){
                    var dayBegin = new Date().Format("yyyy-MM-dd 00:00:00");
                    var start = new Date(dayBegin.getTime() - (8 * 24 * 60 * 60 * 1000)).Format("yyyy-MM-dd hh:mm:ss");
                    var now = new Date().Format("yyyy-MM-dd hh:mm:ss");
                    var chartParams = {
                        type: 3,
                        timeType: 2,
                        startMonth:start,
                        stopMonth:now,
                        orgId:data.id
                    };
                    initBuildingLineChart(data.text,chartParams);
                }else if($("#sectionSelect").val() == 3){
                    var dayBegin = new Date().Format("yyyy-MM-dd 00:00:00");
                    var start = new Date(dayBegin.getTime() - (31 * 24 * 60 * 60 * 1000)).Format("yyyy-MM-dd hh:mm:ss");
                    var now = new Date().Format("yyyy-MM-dd hh:mm:ss");
                    var chartParams = {
                        type: 3,
                        timeType: 2,
                        startMonth:start,
                        stopMonth:now,
                        orgId:data.id
                    };
                    initBuildingLineChart(data.text,chartParams);
                }

            });
        }else{
            warningPrompt("获取组织机构树信息失败","");
        }
    });
}

function initBuildingLineChart(lineName,params){

    var nodeName = [lineName];
    var nodeValue = new Array();
    var xTimeLine = new Array();

    $.post(starnetContextPath + "/count/getCountMacList",params,function(res){
        if(res.result == 0) {
            // alert(res.message)
            $('#message').html(res.message);
            // alert($("#message").val())
            var data = res.data;

            var placesName = res.placesName;
            for(var i in placesName){
                var j = data.length - 1;
                var nodeData = new Array();
                while(j >= 0){
                    if(data[j].org_node_name == placesName[i]) {
                        var countValue = data[j].scount + data[j].tcount + data[j].ucount;
                        nodeData.push(countValue);
                        if(i == 0){
                            if(params.timeType == 1){
                                xTimeLine.push(new Date(data[j].reccount_time).Format("hh:mm"));
                            }else{
                                xTimeLine.push(new Date(data[j].reccount_time).Format("yyyy-MM-dd"));
                            }

                        }
                    }
                    j--;
                }
                var nodeValueObject = new Object();
                nodeValueObject.type = 'line';
                nodeValueObject.name = lineName;
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

            var lineChart = echarts.init(document.getElementById("echarts-line-chart"));


            var ecConfig = echarts.config;
            lineChart.on(ecConfig.EVENT.CLICK, eConsole);
            // 添加点击事件

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

                clickable: true,

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
            errorPrompt("园区人数走势信息加载失败",res.msg);
        }
    },'json');

}

function initDevSelect(orgid){

    $.post(starnetContextPath + "/node/getOrgNode?orgId="+orgid,function(res){
        if(res.result == 0){
            $('#nodeSelect').empty();
            var defaultItem = '<option value="">全部位置</option>';
            $('#nodeSelect').append(defaultItem);
            for(var i in res.devices){
                var item = '<option value="' + res.devices[i].id + '" >' + res.devices[i].org_node_name + '</option>';
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

//点击触发的事件
function eConsole(param) {

    if (typeof param.seriesIndex == 'undefined') {
        return;
    }
    if (param.type == 'click') {

    }

    Today(param.name)

    $("#echarts-line-Today").show('slow');




}



//设置数据
function Today(todaydata){


                    var data = s;

                    var start = new Date().Format(todaydata+"  00:00:00");
                    var now = new Date().Format(todaydata+" 24:00:00");
                     var interval=$("#interval").val();
                     var  nodeId =$("#nodeSelect").val();


                    var chartParams = {
                        type: 3,
                        timeType: 1,
                        startMonth:start,
                        stopMonth:now,
                        orgId:d,
                        interval:interval,
                        nodeId:nodeId
                    };

                    TodayChart(c,chartParams);
}


//图表显示
function TodayChart(lineName,params){
    var nodeName = [lineName];
    var nodeValue = new Array();
    var xTimeLine = new Array();

    $.post(starnetContextPath + "/count/getCountMactodayList",params,function(res){
        if(res.result == 0) {
            var data = res.data;

                var nodeData = new Array();
            for(var i in data){
                        var countValue = data[i].ucount
                        nodeData.push(countValue);
                            if(params.timeType == 1){
                                var str =data[i].reccount_time;
                                str = str.replace(/-/g,"/");
                                var date = new Date(str );
                                xTimeLine.push(date.Format("hh:mm"));
                            }else{
                                xTimeLine.push(new Date(data[i].reccount_time).Format("yyyy-MM-dd"));
                            }


                }
                var nodeValueObject = new Object();
                nodeValueObject.type = 'line';
                nodeValueObject.name = lineName;
                nodeValueObject.data = nodeData;




            nodeValue.push(nodeValueObject);
            var lineChart = echarts.init(document.getElementById("echarts-line-Today"));
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

                clickable: true,

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
            errorPrompt("园区人数走势信息加载失败",res.msg);
        }
    },'json');

}

