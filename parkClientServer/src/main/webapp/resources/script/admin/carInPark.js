/**
 * Created by JAMESBANG on 2019/6/28.
 */

var id=window.parent.parkID;
var excelParams={
    carNo:"",
    carNoAttribute:"",
    carparkName:"",
    inCarRoadName:"",
    parkOvertime:"",
    inStartTime:"",
    inEndTime:""
};
var mainTable;
var deleteData;
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
            url: starnetContextPath + "/real/carInPark",
            type: 'POST',
        },
        columns:[
            {"data":"car_no"},
            {"data":"carpark_name"},
            {"data":"in_time"},
            {"data":"in_car_road_name"},
            {"data":"stayTime"},
            {"data":"car_type"}
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
                "targets": 4
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
                "targets":2
            },
            {
                "render": function(data, type, row, meta) {
                    return '<button class="btn  btn-sm btn-danger" onclick="deleteFunction('+meta.row+')">删除</button> '+
                           '<button class="btn btn-primary" onclick="showDesc(' + meta.row + ')">查看</button>' +
                        ' ';
                },
                "targets": 6
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
    excelParams.parkOvertime = $("#parkOvertime").val();
    excelParams.inStartTime = $("#excelStart").val();
    excelParams.inEndTime = $("#excelEnd").val();

    var params = {
        "carNo":$("#carNo").val(),
        "carNoAttribute":$("#carNoAttribute").val(),
        "carparkName":$("#carparkName").val(),
        "inCarRoadName":$("#inCarRoadName").val(),
        "parkOvertime":$("#parkOvertime").val(),
        "inStartTime":excelParams.inStartTime,
        "inEndTime":excelParams.inEndTime
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
    beginTime = new Date(timeTemp).Format("yyyy-MM-dd") + " 00:00:00";
    endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
    $("#excelStart").val(beginTime);
    $("#excelEnd").val(endTime);

}

//导出报表
function carInParkExcel() {
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
    excelParams.parkOvertime = $("#parkOvertime").val();
    excelParams.inStartTime = $("#excelStart").val();
    excelParams.inEndTime = $("#excelEnd").val();

    location.href = starnetContextPath + "/real/carInParkExcel?"
        + "carNo=" + excelParams.carNo + "&"
        + "carNoAttribute=" + excelParams.carNoAttribute + "&"
        + "carparkName=" + excelParams.carparkName + "&"
        + "inCarRoadName=" + excelParams.inCarRoadName + "&"
        + "parkOvertime=" + excelParams.parkOvertime + "&"
        + "inStartTime=" + excelParams.inStartTime + "&"
        + "inEndTime=" + excelParams.inEndTime + "";

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

            for(var i in data.roadData){
                item = "<option value='" + data.roadData[i].id  + "'>" + data.roadData[i].name  +
                    "</option>";
                if(data.roadData[i].type == 0) {
                    $("#inCarRoadName").append(item);

                }
            }
        }else{
            errorPrompt("初始化车道失败！");
        }
    });
}


// 刷新表格数据
function refreshBtnClick() {
    mainTable.ajax.reload(null,true);
}

function deleteFunction(rowIndex) {
    var data = mainTable.row(rowIndex).data();
    deleteData = data;
    $("#deleteModal").modal("show");
}

function deleteCarInPark() {
    var params = {
        carNo : deleteData.car_no,
        carparkName:deleteData.carpark_name
    };
    $.post(starnetContextPath + "/real/deleteCarInPark",params,function(res){
        if(res.result == 0){
            $("#deleteModal").modal("hide");
            successfulPrompt("场内车辆删除成功");
            mainTable.ajax.reload(null,true);
        }else{
            errorPrompt("场内车辆删除失败",res.msg);
        }
    });
}

function showDesc(rowIndex){
    var data = mainTable.row(rowIndex).data();
    $("#descForm").populateForm(data);
    $("#descImg").attr("src",data["in_picture_name"]);
    $("#descModal").modal("show");
}

