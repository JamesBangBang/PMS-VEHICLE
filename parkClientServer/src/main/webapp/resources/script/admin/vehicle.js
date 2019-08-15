var chargeInfoTable;
function iniDataTable(){
    chargeInfoTable = $('#List').DataTable( {
        ordering: false,
        processing:true,
        searching:false,
        sScrollY:"350px",
        autoWidth:true,
        serverSide:true,
        paging:true,
        /*ordering: true,
        sort:false,
        processing:true,
        searching:false,
        autoWidth:true,
        lengthChange:false,*/
        ajax: {
            url: starnetContextPath + "/report/getAllChargeReport",
            type: 'POST',
            data:{
                "beginTime":$("#start").val(),
                "endTime": $("#end").val(),
                "chargeReceiveCarNo":null,
                "depName":null,
                "carparkName":null,
                "chargeReceiveSubType":null,
                "chargeReceiveAmountMin":null,
                "chargeReceiveAmountMax":null,
                "chargeDiscountInfo":null,
                "chargeReceivePostName":null,
                "memo":null,
                "operatorName":null,
                "chargeReceiveId":null,
                "depId":null,
                "chargeReceivePost":null,
                "chargeReceiveOperatorId":null,
                "chargeReceiveCarpark":null
            }
        },
        /*dom: 'Bfrtip',
        buttons: [
            'copy', 'csv', 'excel', 'pdf', 'print'
        ],*/
        /*dom: 'Bfrtip',
        "buttons": [
            {
                'extend': 'excelHtml5',
                'text': '导出excel',//定义导出excel按钮的文字
                'exportOptions': {
                    'modifier': {
                        'page': 'current'
                    }
                }
                /!*customize: function( xlsx ) {
                 var sheet = xlsx.xl.worksheets['sheet1.xml'];

                 $('row c[r^="C"]', sheet).attr( 's', '2' );
                 }*!/
            }
        ],*/

        columns:[
            {"data":"chargeReceiveCarNo"},
            {"data":"depName"},
            {"data":"carparkName"},
            {"data":"chargeReceiveSubType"},
            {"data":"chargeRealAmount"},
            {"data":"chargeDiscountInfo"},
            {"data":"chargeReceivePostName"},
            {"data":"chargeReceiveTime"},
            {"data":"memo"},
            {"data":"operatorName"},
            {"data":"chargeReceiveId"},
            {"data":"carparkId"},
            {"data":"depId"},
            {"data":"chargeReceiveost"},
            {"data":"chargeReceiveOperatorId"}
        ],
        "columnDefs": [
            /*{
                "render": function(data, type, row, meta) {
                    return '<span style="color: #767676">' + "测试" +  '</span>'
                    /!*return '<button class="btn btn-warning btn-sm">修改</button>' +
                     ' <button class="btn btn-info btn-sm">轨迹</button>' +
                     ' <button class="btn btn-danger btn-sm">删除</button>';*!/
                },
                "targets": 10
            },*/
            {
                "targets": [10, 11, 12, 13, 14],
                "visible": false
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
}

function searchChargeInfo() {
    var params = {
        "beginTime": $("#start").val(),
        "endTime": $("#end").val()
    }
    chargeInfoTable.settings()[0].ajax.data = params;
    chargeInfoTable.ajax.reload(null,true);
}

function outPortExcel() {

    location.href = starnetContextPath + "/report/getVehicleExcel?sheetName=收费情况统计报表&beginTime="
        + $("#start").val() + "&endTime="+$("#end").val();


}


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












