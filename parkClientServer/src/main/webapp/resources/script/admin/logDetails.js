var mainTable;
var id=window.parent.parkID;
$(function(){
    iniDataTable();

});


//表格数据
function iniDataTable(){
    mainTable=$('#logDetails').DataTable( {
        ordering: true,
        sort:false,
        processing:true,
        searching:false,
        autoWidth:true,
        lengthChange:false,
        serverSide:true,
        ajax: {
            url: starnetContextPath + "/operator/getOperator",
            type: 'POST',
            data:{
                parkId: id
            }
        },
        data:"data",
        columnDefs: [
            {
                "render": function(data, type, row, meta) {
                    data = new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                    return data;
                },
                "targets": 1
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
    // alert($("#logName").val())

    var params = {
        "parkId": id,
        "logName":$("#logName").val(),
    };
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload(null,false);

}


// 刷新表格数据
function refreshBtnClick() {
    mainTable.ajax.reload(null,true);
}






