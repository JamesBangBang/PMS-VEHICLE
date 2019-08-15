//车辆属性车流统计
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

});
var mainTable;
var reportType = 0;
function iniDataTable(){
    mainTable = $('#vehicleAttributes').DataTable( {
        ordering: false,
        processing:true,
        searching:false,
        sScrollY:"300px",
        sScrollX:false,
        autoWidth:true,
        serverSide:true,
        paging:true,
        ajax: {
            url: starnetContextPath + "/report/getVehicleAttributeReport",
            type: 'POST',
            data:{
                "beginTime":"",
                "endTime": "",
                "depId":null,
                "depName":null,
                "carparkId":null,
                "carparkName":null,
                "carType":null,
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
                "reportType":0
            }
        },


        columns:[
            {"data":"carType"},
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

function searchVehicleAttribute() {
    var carAttribute = $("#carAttributeVel").val();
    var params = {
        "beginTime": $("#start").val(),
        "endTime": $("#end").val(),
        "carType":carAttribute,
        "reportType":reportType
    }
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload(null,true);
}

function exportVehicleAttribute() {
    var carAttribute = $("#carAttributeVel").val();
    location.href = starnetContextPath + "/report/getVehicleAttributeExcel?sheetName=车辆属性车流统计表&beginTime="
        + $("#start").val() + "&endTime="+$("#end").val() + "&carType=" + carAttribute + "&reportType=" + reportType;
}









