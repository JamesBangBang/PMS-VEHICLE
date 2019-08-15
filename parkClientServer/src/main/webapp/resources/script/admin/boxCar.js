//岗亭车流统计报表
$(function(){
    //初始化开始时间和结束时间
    var myDate = new Date();
    var timeTemp = myDate.getTime();
    document.getElementById("start").value = new Date(timeTemp).Format("yyyy-MM-dd") + " 00:00:00";
    document.getElementById("end").value = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");

    iniDataTable();
    //时间表格
    var start = {
        elem: '#start',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59', //最大日期
        istime: true,
        istoday: false,
        start: '2017-6-15 23:00:00',
        choose: function (datas) {
            end.min = datas; //开始日选好后，重置结束日的最小日期
            end.start = datas //将结束日的初始值设定为开始日
        }
    };
    var end = {
        elem: '#end',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    laydate(start);
    laydate(end);

    initSearchCondition();

});
var mainTable;
var reportType = 0;
function iniDataTable(){
    mainTable = $('#boxCar').DataTable( {
        ordering: false,
        processing:true,
        searching:false,
        sScrollY:"300px",
        sScrollX:false,
        autoWidth:true,
        serverSide:true,
        paging:true,
        ajax: {
            url: starnetContextPath + "/report/getBoothVehicleFlowReport",
            type: 'POST',
            data:{
                "beginTime":"",
                "endTime": "",
                "depId":null,
                "depName":null,
                "carparkId":null,
                "carparkName":null,
                "boothId":null,
                "boothName":null,
                "inCountMin":null,
                "inCountMax":null,
                "outCountMin":null,
                "outCountMax":null,
                "carCountMin":null,
                "carCountMax":null,
                "sumMinuteMin":null,
                "sumMinuteMax":null,
                "maxMinuteMin":null,
                "maxMinuteMax":null,
                "minMinuteMin":null,
                "minMinuteMax":null,
                "avgMinuteMin":null,
                "avgMinuteMax":null,
                /*0-日报  1-周报 2-月报 3-年报*/
                "reportType":0,
                /*0-不需要停车时长， 1-需要停车时长*/
                "isExistDuration":"1"
            }
        },

        columns:[
            {"data":"boothName"},
            {"data":"statisticsTime"},
            {"data":"inCount"},
            {"data":"outCount"},
            {"data":"carCount"},
            {"data":"sumMinute"},
            {"data":"maxMinute"},
            {"data":"minMinute"},
            {"data":"avgMinute"}
        ],
        "columnDefs": [
            {
                /*给每个字段设置默认值，当返回data中数据为null不会报错*/
                "defaultContent": "",
                "targets": "_all"
            }

        ],
        language:dataTableLanguage
    } );
}

function timeTypeChange() {
    var index = document.getElementById("time-type").selectedIndex;
    var myDate = new Date();

    var timeTemp = myDate.getTime();
    var beginTime;
    var endTime;
    if(index == 0){
        //日报
        beginTime = new Date(timeTemp).Format("yyyy-MM-dd") + " 00:00:00";
        endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
    }
    else if (index == 1){
        //周报
        var dayTemp = new Date(myDate.getFullYear(),myDate.getMonth(),myDate.getDate() + 1 - (myDate.getDay() || 7)).Format("yyyy-MM-dd");
        beginTime = dayTemp + " 00:00:00";
        endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
    }
    else if (index == 2){
        //月报
        var weekTemp = myDate.getMonth() + 1;
        if (weekTemp >= 1 && weekTemp <= 9){
            weekTemp = "0" + weekTemp;
        }
        beginTime = myDate.getFullYear() + "-" + weekTemp + "-01 00:00:00";
        endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
    }
    else{
        //年报
        var yearTemp = myDate.getFullYear() + "-01-01 00:00:00";
        beginTime = yearTemp;
        endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
    }
    document.getElementById("start").value = beginTime ;
    document.getElementById("end").value = endTime;
    reportType = index;
}

function searchBoxCar() {
    var boothId = $("#boothIdSearch").val();
    if (boothId == "all") boothId = "";
    var params = {
        "beginTime": $("#start").val(),
        "endTime": $("#end").val(),
        "boothId":boothId,
        "reportType":reportType
    }
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload(null,true);
}

function exportBoxCar() {
    var boothId = $("#boothIdSearch").val();
    if (boothId == "all") boothId = "";
    location.href = starnetContextPath + "/report/getBoxCarExcel?sheetName=岗亭车流统计报表&beginTime="
        + $("#start").val() + "&endTime="+$("#end").val() + "&boothId=" + boothId + "&reportType=" + reportType;
}

function getCurrentCarparkId() {
    var carparkId =  window.parent.parkID;
    return carparkId;
}

function getDepId(id) {
    var params = {};
    var depId="";
    if (id == "" || id == null){
        return "";
    }else{
        params.carparkId = id;
    }
    $.ajax({
        url: starnetContextPath + "/report/getDepInfo",
        type: "POST",
        dataType: 'json',
        async: false,
        contentType: 'application/json',
        data: JSON.stringify(params),
        beforeSend: function (request) {
            request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        },
        success: function (res) {
            if(res.result == 0){
                depId = res.data[0].departmentId;
            }else{
                errorPrompt("获取单位信息失败！");
                return "";
            }
        },
        error:function(){
            errorPrompt("无法访问网络","")
        }
    });
    return depId;
}

function initSearchCondition() {
    var params ={'depId': getDepId(getCurrentCarparkId())};
    $.post(starnetContextPath + "/report/getBoothList", params, function (res) {
        if (res.result == 0) {
            $("[name = boothIdSearch]").empty();
            $("[name = boothIdSearch]").append("<option value='all'>全部岗亭</option>");
            for(var i in res.data){
                var item = "<option value='" + res.data[i].boothId + "'>" +
                    res.data[i].boothName +
                    "</option>";
                $("[name = boothIdSearch]").append(item);
            }
            updateSelect();
        }else{
            errorPrompt("初始化岗亭信息失败！");
        }
    });
}

function updateSelect() {
    $('[name =boothIdSearch]').chosen({
        width:"100%"
    });
    /*使新赋的值能够刷新成功*/
    $("select").on('chosen:ready', function(e, params) {
        $("select").val("true");//设置值  
    });
    $("select").trigger('chosen:updated');
}








