var mainTable;
var id=window.parent.parkID;
$(function(){
    var dateStart = new Date().Format("yyyy-MM-dd 00:00:00");
    var dateEnd = new Date().Format("yyyy-MM-dd 23:59:59");
    var start = {
        elem: '#start',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59', //最大日期
        istime: true,
        istoday: false,
        start: dateStart,
        value:dateStart,
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
        value:dateEnd,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    $("#end").val(dateEnd);
    $("#start").val(dateStart);
    laydate(start);
    laydate(end);
    iniDataTable();

});


//表格数据
function iniDataTable(){
        mainTable=$('#entryDetails').DataTable( {
        ordering: true,
        sort:false,
        processing:true,
        searching:false,
        autoWidth:true,
        lengthChange:false,
        serverSide:true,
        ajax: {
            url: starnetContextPath + "/inout/getCarparkInOutHistory",
            type: 'POST',
            data:{
                parkId: id
            }
        },
        columns:[
            {"data":"0"},
            {"data":"1"},
            {"data":"2"},
            {"data":"3"},
            {"data":"4"}
        ],
        columnDefs: [
            {

                "render": function(data, type, row, meta) {
                    return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                },
                "targets": 1
            },
            {

                "render": function(data, type, row, meta) {
                    if(data==1)
                    {
                        data='出'
                    }
                    else if(data==0)
                    {
                        data='进'
                    }
                    return data;
                },
                "targets": 2,
            },
            {
                /*给每个字段设置默认值，当返回data中数据为null不会报错*/
                "defaultContent": "",
                "targets": "_all"
            }

        ],
        data:"data",
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
        "startMonth":$("#start").val(),
        "stopMonth":$("#end").val(),
        "parkId": id,
        "carName":$("#carName").val(),
    };
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload(null,false);

}

// 刷新表格数据
function refreshBtnClick() {
    mainTable.ajax.reload(null,true);
}









