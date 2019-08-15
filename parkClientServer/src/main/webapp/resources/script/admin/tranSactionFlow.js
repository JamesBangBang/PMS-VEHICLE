
var id=window.parent.parkID
var mainTable;
$(function(){
    iniDataTable();
    editData();
    init();

});


//表格数据
function iniDataTable(){
    mainTable=$('#tranSactionFlowList').DataTable( {
        sort:false,
        processing:true,
        serverSide:true,
        searching:false,
        autoWidth:true,
        ajax: {
            url: starnetContextPath + "/report/tranSaction",
            type: 'POST'
        },
        columns:[
            {"data":"car_no"},
            {"data":"carpark_name"},
            {"data":"pay_type"},
            {"data":"pay_time"},
            {"data":"total_fee"},
            {"data":"transaction_mark"}
        ],
        columnDefs: [
            {
                "render": function(data, type, row, meta) {

                    data = new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                    return data;
                },
                "targets":3
            },
            {
                "render": function(data, type, row, meta) {
                    if(data!=null)
                    {
                        data+='元'
                    }
                    return data;
                },
                "targets": 4
            },
            {
                /*给每个字段设置默认值，当返回data中数据为null不会报错*/
                "defaultContent": "",
                "targets": "_all"
            }
        ],
        language:dataTableLanguage
    } );
}



function search() {
    var params = {
        "startTime":$("#excelStart").val(),
        "endTime":$("#excelEnd").val(),
        "payType":$("#payType").val(),
        "payStatus":"",
        "excelType":"",
        "excelStart":$("#excelStart").val(),
        "excelEnd":$("#excelEnd").val(),
        "markInfo":$("#markInfo").val(),
        "carparkName":$("#carparkName").val(),
        "carno":$("#carPlate").val()
    };
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload();

    var url = starnetContextPath + "/report/getTransactionFee?startTime=" + params.startTime
                                                            + "&endTime=" + params.endTime
                                                            + "&payType=" + params.payType
                                                            + "&payStatus=" + params.payStatus
                                                            + "&markInfo=" + params.markInfo
                                                            + "&carparkName=" + params.carparkName
                                                            + "&carno=" + params.carno;
        $.post(url, function (res) {
        if (res.result == 0) {
            $("#totalFee").val(res.totalFee);
            $("#cashFee").val(res.cashFee);
            $("#onlineFee").val(res.onlineFee);
        }else{
            errorPrompt("获取金额信息失败！");
        }
    });
}



function editData() {
    var dateStart = new Date().Format("yyyy-MM-dd 00:00:00");
    var dateEnd = new Date().Format("yyyy-MM-dd 23:59:59");

    var excelSart = {
        elem: '#excelStart',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59', //最大日期
        istime: true,
        istoday: false,
        start: dateStart,
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

//导出报表
function realPaymentExcel() {
    location.href = starnetContextPath + "/report/tranSactionExcel";
}


//初始化
function init() {
    $.post(starnetContextPath + "/report/paySelect", function (res) {
        if (res.result == 0) {
            var data=res.data;
            $("#payType").empty();
            $("#payType").append("<option value='' selected>全部</option>");

            for(var i in data.payTypeSelect){
                var item = "<option value='" + data.payTypeSelect[i].name  + "'>"+ data.payTypeSelect[i].desc  +
                    "</option>";
                $("#payType").append(item);
            }

        }else{
            errorPrompt("初始化信息失败！");
        }
    });

    $.post(starnetContextPath + "/report/getTransactionFee", function (res) {
        if (res.result == 0) {
            $("#totalFee").val(res.totalFee);
            $("#cashFee").val(res.cashFee);
            $("#onlineFee").val(res.onlineFee);
        }else{
            errorPrompt("获取金额信息失败！");
        }
    });
}

// 刷新表格数据
function refreshBtnClick() {
    mainTable.ajax.reload(null,true);
}





