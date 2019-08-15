
var id=window.parent.parkID;
var excelParams={
    carName:"",
    carType:"",
    carparkName:"",
    carRoadName:"",
    inoutFlag:"",
    startTime:"",
    endTime:"",
    excelStart:"",
    excelEnd:""
};
var mainTable;
$(function(){
    iniDataTable();
    editData();

});
var inoutFlag;

//表格数据
function iniDataTable(){
    mainTable=$('#List').DataTable( {
        sort:false,
        processing:true,
        serverSide:true,
        searching:false,
        autoWidth:true,
        ajax: {
            url: starnetContextPath + "/real/inout",
            type: 'POST',
        },

        columns:[
            {"data":"car_no"},
            {"data":"car_no_color"},
            {"data":"car_type"},
            {"data":"inout_flag"},
            {"data":"carpark_name"},
            {"data":"car_road_name"},
            {"data":"inout_status"},
            {"data":"inout_time"},
            {"data":"post_name"},
            {"data":"operator_name"},
            {"data":"remark"},
            {"data":"inout_record_id"},
            {"data":"photo_capture_pic_name"}
        ],
        columnDefs: [
            {
                "render": function(data, type, row, meta) {
                   if(data==0)
                   {
                       data='未知'
                   }else if(data==1) {
                       data='蓝底白字'
                   }
                   else if(data==2) {
                       data='黄底黑字'
                   }
                   else if(data==3) {
                       data='白底黑字'
                   }
                   else if(data==4) {
                       data='黑底白字'
                   }
                   else if(data==5) {
                       data='绿底白字'
                   }else if(data==6) {
                       data='新能源车'
                   }
                    return data;
                },
                "targets": 1
            },
            {
                "render": function(data, type, row, meta) {
                    if(data==0)
                    {
                        data='进场';
                        inoutFlag = 0;
                    }else if(data==1) {
                        data='出场';
                        inoutFlag = 1;
                    }
                    return data;
                },
                "targets": 3
            },
            {
                "render": function(data, type, row, meta) {
                    if(data==0)
                    {
                        data='自动放行'
                    }else if(data==1) {
                        data='手动放行'
                    }
                    else if(data==2) {
                        if (inoutFlag == 0){
                            data='禁止入场';
                        }else {
                            data='禁止出场';
                        }
                    }
                    else if(data==3) {
                        data='离线数据'
                    }
                    return data;
                },
                "targets": 6
            },
            {
                /*给每个字段设置默认值，当返回data中数据为null不会报错*/
                "defaultContent": "",
                "targets": "_all"
            },
            {
                "render": function(data, type, row, meta) {

                    data = new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                    return data;
                },
                "targets":7
            },
            {
                "render": function(data, type, row, meta) {
                    return '<button class="btn btn-primary" onclick="showDesc(' + meta.row + ')">查看</button>';
                },
                "targets":10
            },
            {
                "visible": false,
                "targets": [8,11,12]
            }
        ],
        language:dataTableLanguage
    } );
}


function search() {
    if ($("#start").val() == "" && $("#end").val() == ""){

    }else if ($("#start").val() != "" && $("#end").val() != ""){
        var beginTime = new Date(Date.parse($("#start").val()));
        var overTime = new Date(Date.parse($("#end").val()));
        if (beginTime > overTime){
            errorPrompt("开始时间不得大于结束时间");
            return;
        }
    }else {
        errorPrompt("开始和结束时间必须同时为空或同时有值");
        return;
    }

    var params = {
        "carName":$("#carName").val(),
        "driverName":$("#driverName").val(),
        "carparkName":$("#carparkName").val(),
        "carRoadName":$("#carRoadName").val(),
        "inoutFlag":$("#inoutFlag").val(),
        "startTime":$("#start").val(),
        "endTime":$("#end").val()
    };
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload(null,false);

    //记录搜索条件（报表条件）
    excelParams.carName=$("#carName").val();
    excelParams.driverName=$("#driverName").val();
    excelParams.carparkName=$("#carparkName").val();
    excelParams.carRoadName=$("#carRoadName").val();
    excelParams.inoutFlag=$("#inoutFlag").val();
    excelParams.startTime=$("#start").val();
    excelParams.endTime=$("#end").val();
    excelParams.excelStart=$("#start").val();
    excelParams.excelEnd=$("#end").val();

}

//导出报表
function realTimeExcel() {
    location.href = starnetContextPath + "/real/inoutExcel?carName="+excelParams.carName+"&" +
        "driverName="+excelParams.driverName+"&carparkName="+excelParams.carparkName+"&carRoadName="+excelParams.carRoadName+"&" +
        "inoutFlag="+excelParams.inoutFlag+"&startTime="+excelParams.startTime+"&endTime="+excelParams.endTime+"&" +
        "excelStart="+excelParams.excelStart+"&excelEnd="+excelParams.excelEnd+"";
}




function editData() {
    var date = new Date().Format("yyyy-MM-dd")
    var start = {
        elem: '#start',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59', //最大日期
        istime: true,
        istoday: false,
        start: date + ' 00:00:00',
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

    $("#start").val(date + " 00:00:00");
    $("#end").val(date + " 23:59:59");

//报表时间
    var myDate = new Date();
    var timeTemp = myDate.getTime();
    var beginTime;
    var endTime;
    //初始化
    /*if($("#excelType").val()==="0")
    {
        //日报
        beginTime = new Date(timeTemp).Format("yyyy-MM-dd") + " 00:00:00";
        endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
        $("#excelStart").val(beginTime);
        $("#excelEnd").val(endTime);
    }

    $("#excelType").change(function() {
        if($("#excelType").val()==="0")
        {
            //日报
            beginTime = new Date(timeTemp).Format("yyyy-MM-dd") + " 00:00:00";
            endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
        }
        else if($("#excelType").val()==="1"){
            //周报
            var dayTemp = new Date(myDate.getFullYear(),myDate.getMonth(),myDate.getDate() + 1 - (myDate.getDay() || 7)).Format("yyyy-MM-dd");
            beginTime = dayTemp + " 00:00:00";
            endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
        }
        else if($("#excelType").val()==="2"){
            //月报
            var weekTemp = myDate.getMonth() + 1;
            if (weekTemp >= 1 && weekTemp <= 9){
                weekTemp = "0" + weekTemp;
            }
            beginTime = myDate.getFullYear() + "-" + weekTemp + "-01 00:00:00";
            endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
        }
        else if($("#excelType").val()==="3"){
            //年报
            var yearTemp = myDate.getFullYear() + "-01-01 00:00:00";
            beginTime = yearTemp;
            endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
        }
        else {
            beginTime="";
            endTime="";
        }
        $("#start").val(beginTime);
        $("#end").val(endTime);
    });*/
}

function showDesc(rowIndex){
    var data = mainTable.row(rowIndex).data();
    if(data["car_no_color"] == 0)
    {
        data["car_no_color"]='未知'
    }else if(data["car_no_color"]==1) {
        data["car_no_color"]='蓝底白字'
    }
    else if(data["car_no_color"]==2) {
        data["car_no_color"]='黄底黑字'
    }
    else if(data["car_no_color"]==3) {
        data["car_no_color"]='白底黑字'
    }
    else if(data["car_no_color"]==4) {
        data["car_no_color"]='黑底白字'
    }
    else if(data["car_no_color"]==5) {
        data["car_no_color"]='绿底白字'
    }else if(data["car_no_color"]==6) {
        data["car_no_color"]='新能源车'
    }

    if(data["inout_flag"]==0)
    {
        data["inout_flag"]='进场'
    }else if(data["inout_flag"]==1) {
        data["inout_flag"]='出场'
    }

    if(data["inout_status"]==0)
    {
        data["inout_status"]='自动放行'
    }else if(data["inout_status"]==1) {
        data["inout_status"]='手动放行'
    }
    else if(data["inout_status"]==2) {
        data["inout_status"]='禁止入场'
    }
    else if(data["inout_status"]==3) {
        data["inout_status"]='离线数据'
    }
    data["inout_time"] = new Date(data["inout_time"]).Format("yyyy-MM-dd hh:mm:ss");
    $("#descForm").populateForm(data);
    $("#descImg").attr("src",data["photo_capture_pic_name"]);
    $("#descModal").modal("show");
}


// 刷新表格数据
function refreshBtnClick() {
    mainTable.ajax.reload(null,true);
}




