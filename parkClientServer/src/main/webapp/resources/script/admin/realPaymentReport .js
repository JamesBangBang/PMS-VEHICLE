var id=window.parent.parkID;
var excelParams={
    carNo:"",
    carNoAttribute:"",
    carparkName:"",
    inCarRoadName:"",
    outCarRoadName:"",
    chargePostName:"",
    inStartTime:"",
    inEndTime:"",
    OutStartTime:"",
    OutEndTime:"",
    excelStart:"",
    excelEnd:""
};
var mainTable;
$(function(){
    iniDataTable();
    editData();
    init();

});


//表格数据
function iniDataTable(){
    mainTable=$('#paymentList').DataTable( {
        sort:false,
        processing:true,
        serverSide:true,
        searching:false,
        autoWidth:true,
        ajax: {
            url: starnetContextPath + "/real/pay",
            type: 'POST',
        },
        columns:[
            {"data":"car_no"},
            {"data":"carpark_name"},
            {"data":"in_time"},
            {"data":"in_car_road_name"},
            {"data":"out_time"},
            {"data":"out_car_road_name"},
            {"data":"stay_time"},
            {"data":"charge_receivable_amount"},
            {"data":"charge_actual_amount"},
            {"data":"charge_pre_amount"},
            {"data":"charge_free_amount"},
            {"data":"release_type"},
            {"data":"release_reason"},
            {"data":"car_type"},
            {"data":"charge_post_name"},
            {"data":"charge_time"}
        ],
        columnDefs: [
            {
                "render": function(data, type, row, meta) {
                    if(data){
                        return secondFormat(data);
                    }else{
                        return 0;
                    }
                },
                "targets": 6
            },
            {
                "render": function(data, type, row, meta) {
                    if(data!=null)
                    {
                        data+='元';
                    }else {
                        data='0元';
                    }
                    return data;
                },
                "targets": [7,8,9,10]
            },
            {
                "render": function(data, type, row, meta) {
                    if(data==1)
                    {
                        data='收费'
                    }else if(data==3) {
                        data='免费'
                    }else
                    {
                        data=""
                    }
                    return data;
                },
                "targets": 11
            },
            {
                /*给每个字段设置默认值，当返回data中数据为null不会报错*/
                "defaultContent": "",
                "targets": "_all"
            },
            {
                "render": function(data, type, row, meta) {
                    if(data!=null) {
                        data = new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                    }
                    return data;
                },
                "targets":[2,4]
            },
            {
                "visible": false,
                "targets": [8,10,12,15]
            }
        ],
        language:dataTableLanguage
    } );
}



function search() {
    if ($("#excelStart").val() == "" && $("#excelEnd").val() == ""){

    }else if ($("#excelStart").val() != "" && $("#excelEnd").val() != ""){
        var beginTime = new Date(Date.parse($("#excelStart").val()));
        var overTime = new Date(Date.parse($("#excelEnd").val()));
        if (beginTime > overTime){
            errorPrompt("开始时间不得大于缴费结束时间");
            return;
        }
    }else {
        errorPrompt("开始和结束时间必须同时为空或同时有值");
        return;
    }

    excelParams.carNo = $("#carNo").val();
    excelParams.carNoAttribute = $("#carNoAttribute").val();
    excelParams.carparkName = $("#carparkName").val();
    excelParams.inCarRoadName = $("#inCarRoadName").val();
    excelParams.outCarRoadName = $("#outCarRoadName").val();
    excelParams.chargePostName = $("#chargePostName").val();

    if($("#excelType").val()==="0")
    {
        excelParams.inStartTime = $("#excelStart").val();
        excelParams.inEndTime = $("#excelEnd").val();
        excelParams.OutStartTime = "";
        excelParams.OutEndTime = "";
        excelParams.excelStart = "";
        excelParams.excelEnd = "";
    } else if($("#excelType").val()==="1"){
        excelParams.inStartTime = "";
        excelParams.inEndTime = "";
        excelParams.OutStartTime = $("#excelStart").val();
        excelParams.OutEndTime = $("#excelEnd").val();
        excelParams.excelStart = "";
        excelParams.excelEnd = "";
    } else {
        excelParams.inStartTime = "";
        excelParams.inEndTime = "";
        excelParams.OutStartTime = "";
        excelParams.OutEndTime = "";
        excelParams.excelStart = $("#excelStart").val();
        excelParams.excelEnd = $("#excelEnd").val();
    }

    var params = {
        "carNo":$("#carNo").val(),
        "carNoAttribute":$("#carNoAttribute").val(),
        "carparkName":$("#carparkName").val(),
        "inCarRoadName":$("#inCarRoadName").val(),
        "outCarRoadName":$("#outCarRoadName").val(),
        "chargePostName":$("#chargePostName").val(),
        "inStartTime":excelParams.inStartTime,
        "inEndTime":excelParams.inEndTime,
        "OutStartTime":excelParams.OutStartTime,
        "OutEndTime":excelParams.OutEndTime,
        "excelStart":excelParams.excelStart,
        "excelEnd":excelParams.excelEnd
    };
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload();
}



function editData() {
    var date = new Date().Format("yyyy-MM-dd");
    var excelSart = {
        elem: '#excelStart',
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
    var excelEnd = {
        elem: '#excelEnd',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };

    laydate(excelSart);
    laydate(excelEnd);


    var myDate = new Date();
    var timeTemp = myDate.getTime();
    var beginTime;
    var endTime;
    //初始化
    if($("#excelType").val()==="")
    {
        document.getElementById("beginTime").innerHTML = "进出开始时间";
        document.getElementById("endTime").innerHTML = "进出结束时间";
        //日报
        beginTime = new Date(timeTemp).Format("yyyy-MM-dd") + " 00:00:00";
        endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
        $("#excelStart").val(beginTime);
        $("#excelEnd").val(endTime);
    }

    $("#excelType").change(function() {
        if($("#excelType").val()==="0")
        {
            document.getElementById("beginTime").innerHTML = "入场开始时间";
            document.getElementById("endTime").innerHTML = "入场结束时间";
        }
        else if($("#excelType").val()==="1"){
            document.getElementById("beginTime").innerHTML = "出场开始时间";
            document.getElementById("endTime").innerHTML = "出场结束时间";
        }
        else {
            document.getElementById("beginTime").innerHTML = "进出开始时间";
            document.getElementById("endTime").innerHTML = "进出结束时间";
        }
        $("#excelStart").val(beginTime);
        $("#excelEnd").val(endTime);
    });
}

//导出报表
function realPaymentExcel() {
    location.href = starnetContextPath + "/real/payExcel?carNo=" + excelParams.carNo + "&"
                                       + "carNoAttribute=" + excelParams.carNoAttribute + "&"
                                       + "carparkName=" + excelParams.carparkName + "&"
                                       + "inCarRoadName=" + excelParams.inCarRoadName + "&"
                                       + "outCarRoadName=" + excelParams.outCarRoadName + "&"
                                       + "chargePostName=" + excelParams.chargePostName + "&"
                                       + "inStartTime=" + excelParams.inStartTime + "&"
                                       + "inEndTime=" + excelParams.inEndTime + "&"
                                       + "OutStartTime=" + excelParams.OutStartTime + "&"
                                       + "OutEndTime=" + excelParams.OutEndTime + "&"
                                       + "excelStart=" + excelParams.excelStart + "&"
                                       + "excelEnd=" + excelParams.excelEnd + "";

}


//初始化
function init() {
    var item;
    var params={
        "parkId":id
    };
    $.post(starnetContextPath + "/real/payRoadPostSelect", params, function (res) {
        if (res.result == 0) {
            data = res.data;
            $("#inCarRoadName").empty();
            $("#inCarRoadName").append("<option value='' selected>全部</option>");

            $("#outCarRoadName").empty();
            $("#outCarRoadName").append("<option value='' selected>全部</option>");

            $("#chargePostName").empty();
            $("#chargePostName").append("<option value='' selected>全部</option>");

            for(var i in data.postData){
                item = "<option value='" + data.postData[i].id  + "'>" + data.postData[i].name  +
                    "</option>";
                $("#chargePostName").append(item);
            }

            for(var i in data.roadData){
                item = "<option value='" + data.roadData[i].id  + "'>" + data.roadData[i].name  +
                    "</option>";
                if(data.roadData[i].type == 0) {
                    $("#inCarRoadName").append(item);

                }else {
                    $("#outCarRoadName").append(item);
                }
            }
        }else{
            errorPrompt("初始化缴费信息失败！");
        }
    });
}


// 刷新表格数据
function refreshBtnClick() {
    mainTable.ajax.reload(null,true);
}




