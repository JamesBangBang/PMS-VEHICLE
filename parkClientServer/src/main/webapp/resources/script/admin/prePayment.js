var id=window.parent.parkID;
var excelParams={
    orderOrechargeCarno:"",
    chargeMemberKindName:"",
    orderPrechargeCarparkName:"",
    startTime:"",
    endTime:"",
    excelStart:"",
    excelEnd:""
};
var mainTable;
$(function(){
    iniDataTable();
    editData();
    type();

});

//表格数据
function iniDataTable(){
    mainTable=$('#List').DataTable( {
        sort:false,
        processing:true,
        serverSide:true,
        searching:false,
        autoWidth:true,
        ajax: {
            url: starnetContextPath + "/report/prePayment",
            type: 'POST'
        },

        columns:[
            {"data":"order_precharge_carno"},
            {"data":"order_precharge_carpark_name"},
            {"data":"charge_member_kind_name"},
            {"data":"order_precharge_operator_name"},
            {"data":"order_precharge_receivable_amount"},
            {"data":"order_precharge_actual_amount"},
            {"data":"order_precharge_free_amount"},
            {"data":"free_reason"},
            {"data":"memo"},
            {"data":"order_precharge_time"},
            {"data":"order_precharge_id"}
        ],
        columnDefs: [
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
                "targets":9
            },
            {
                "render": function(data, type, row, meta) {
                    return '<button class="btn btn-primary" onclick="showDesc(' + meta.row + ')">详情</button>';
                },
                "targets":10
            },
            {
                "visible": false,
                "targets": [10]
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
            errorPrompt("缴费开始时间不得大于缴费结束时间");
            return;
        }
    }else {
        errorPrompt("缴费时间必须同时为空或同时有值");
        return;
    }
    //记录搜索条件（报表条件）
    excelParams.orderOrechargeCarno = $("#orderOrechargeCarno").val();
    excelParams.chargeMemberKindName = "";
    excelParams.orderPrechargeCarparkName = $("#orderPrechargeCarparkName").val();
    excelParams.excelStart= $("#excelStart").val();
    excelParams.excelEnd= $("#excelEnd").val();
    excelParams.startTime= "";
    excelParams.endTime= "";
    var params = {
        "orderOrechargeCarno":$("#orderOrechargeCarno").val(),
        "chargeMemberKindName":"",
        "orderPrechargeCarparkName":$("#orderPrechargeCarparkName").val(),
        "excelStart":$("#excelStart").val(),
        "excelEnd":$("#excelEnd").val(),
        "startTime":"",
        "endTime":""
    };
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload(null,false);
}

//导出报表
function prePaymentExcel() {
    if ($("#excelStart").val() == "" && $("#excelEnd").val() == ""){

    }else if ($("#excelStart").val() != "" && $("#excelEnd").val() != ""){
        var beginTime = new Date(Date.parse($("#excelStart").val()));
        var overTime = new Date(Date.parse($("#excelEnd").val()));
        if (beginTime > overTime){
            errorPrompt("缴费开始时间不得大于缴费结束时间");
            return;
        }
    }else {
        errorPrompt("缴费时间必须同时为空或同时有值");
        return;
    }
    //记录搜索条件（报表条件）
    excelParams.orderOrechargeCarno = $("#orderOrechargeCarno").val();
    excelParams.chargeMemberKindName = "";
    excelParams.orderPrechargeCarparkName = $("#orderPrechargeCarparkName").val();
    excelParams.excelStart= $("#excelStart").val();
    excelParams.excelEnd= $("#excelEnd").val();
    excelParams.startTime= "";
    excelParams.endTime= "";

    location.href = starnetContextPath + "/report/prePaymentExcel?orderOrechargeCarno="+ excelParams.orderOrechargeCarno+"&" +
        "chargeMemberKindName="+  excelParams.chargeMemberKindName+"&orderPrechargeCarparkName="+ excelParams.orderPrechargeCarparkName+"&excelStart="+excelParams.excelStart+"&" +
        "excelEnd="+excelParams.excelEnd+"&startTime="+excelParams.startTime+"&endTime="+excelParams.endTime+"";
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
    beginTime = new Date(timeTemp).Format("yyyy-MM-dd") + " 00:00:00";
    endTime = new Date(timeTemp).Format("yyyy-MM-dd hh:mm:ss");
    $("#excelStart").val(beginTime);
    $("#excelEnd").val(endTime);
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






