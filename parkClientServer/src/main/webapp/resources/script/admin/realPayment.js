
var id=window.parent.parkID
var mainTable;
$(function(){
    iniDataTable();
    editData();

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
            {"data":"update_time"},
            {"data":"charge_info_id"},
            {"data":"in_record_id"},
            {"data":"out_record_id"}

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
                    if(data != null)
                    {
                       return data + '元';
                    }else{
                        return '0元';
                    }
                },
                "targets": [7,8,9,10]
            },
            {
                "render": function(data, type, row, meta) {
                    if(data==1)
                    {
                        data='收费'
                    }else if(data==2) {
                        data='使用优惠券'
                    }else if(data==3)
                    {
                        data='免费'
                    }else{
                        data = "";
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

                    data = new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                    return data;
                },
                "targets":[2,4,15]
            },
            {
                "render": function(data, type, row, meta) {
                    return '<button class="btn btn-primary" onclick="showDesc(' + meta.row + ')">详情</button>';
                },
                "targets":16
            },
            {
                "visible":false,
                "targets":[17,18]
            }
        ],
        language:dataTableLanguage
    } );
}


function search() {
    var params = {
        "carNo":$("#carNo").val(),
        "carNoAttribute":$("#carNoAttribute").val(),
        "carparkName":$("#carparkName").val(),
        "inCarRoadName":$("#inCarRoadName").val(),
        "outCarRoadName":$("#outCarRoadName").val(),
        "chargePostName":$("#chargePostName").val(),
        "inStartTime":$("#inStartTime").val(),
        "inEndTime":$("#inEndTime").val(),
        "OutStartTime":$("#OutStartTime").val(),
        "OutEndTime":$("#OutEndTime").val()
    };
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload(null,false);

}



function editData() {
    var inStartTime = {
        elem: '#inStartTime',
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
    var inEndTime = {
        elem: '#inEndTime',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    var OutStartTime = {
        elem: '#OutStartTime',
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
    var OutEndTime = {
        elem: '#OutEndTime',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    laydate(inStartTime);
    laydate(inEndTime);
    laydate(OutStartTime);
    laydate(OutEndTime);
}

function showDesc(rowIndex){

    var data = mainTable.row(rowIndex).data();
    if(data["charge_receivable_amount"] != null)
    {
        data["charge_receivable_amount"] =  data["charge_receivable_amount"] + '元';
    }else{
        data["charge_receivable_amount"] = '0元';
    }

    if(data["charge_actual_amount"] != null)
    {
        data["charge_actual_amount"] =  data["charge_actual_amount"] + '元';
    }else{
        data["charge_actual_amount"] = '0元';
    }

    if(data["charge_pre_amount"] != null)
    {
        data["charge_pre_amount"] =  data["charge_pre_amount"] + '元';
    }else{
        data["charge_pre_amount"] = '0元';
    }

    if(data["charge_free_amount"] != null)
    {
        data["charge_free_amount"] =  data["charge_free_amount"] + '元';
    }else{
        data["charge_free_amount"] = '0元';
    }

    data["in_time"] = new Date(data["in_time"]).Format("yyyy-MM-dd hh:mm:ss");
    data["out_time"] = new Date(data["out_time"]).Format("yyyy-MM-dd hh:mm:ss");
    data["update_time"] = new Date(data["update_time"]).Format("yyyy-MM-dd hh:mm:ss");


    if(data["release_type"]==1)
    {
        data["release_type"]='收费'
    }else if(data==2) {
        data["release_type"]='使用优惠券'
    }else if(data==3) {
        data["release_type"]='免费'
    }else
    {
        data["release_type"]=""
    }

    if(data["stay_time"]){
        data["stay_time"] = secondFormat(data["stay_time"]);
    }else{
        data["stay_time"] = 0;
    }
    $("#descForm").populateForm(data);
    $("#inImg").attr("src",data["in_picture_name"]);
    $("#outImg").attr("src",data["out_picture_name"]);
    $("#descModal").modal("show");
}

// 刷新表格数据
function refreshBtnClick() {
    mainTable.ajax.reload(null,true);
}

