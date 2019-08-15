

$(function(){

    iniDataTable();
    $('#queryUserType').chosen({
        width:"100%"
    });
    $("#queryWarnType").chosen().on("change", function () {

        var params = {
            "message": $("#message").val(),
            "warn_module": $("#queryWarnType").val()
        }

        mainTable.settings()[0].ajax.data = params;
        mainTable.ajax.reload(null,false);

    });
});


var mainTable;
function iniDataTable(){
    mainTable = $('#pList').DataTable({
        sort:false,
        processing:true,
        serverSide:true,
        searching:false,
        autoWidth:true,
        ajax: {
            url: starnetContextPath + "/warn/getAllWarn",
            type: 'POST',
            data:{
              message:$("#message").val(),
              warn_module:$("#queryWarnType").val()
            }
        },

        columns:[
            {"data":"id"},
            {"data":"message"},
            {"data":"add_time" },
            {"data":"warn_module"}

        ],
        "columnDefs": [

            {
                "targets": [0],
                "visible": false
            },
            {
                "render": function(data, type, row, meta) {
                    data = new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                    return data;
                },
                "targets": 2
            }

        ],
        language:dataTableLanguage

    });
}
function searchTrajectory(){

    var params = {
        "message":$("#message").val(),
        "warn_module":$("#queryWarnType").val()
    };
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload(null,false);

}







// function searchMoudlejectory(){
//
//     var params = {
//         "warn_module":$("#queryWarnType").val()
//
//
//     }
//     alert(params)
//     console.log(params)
//     mainTable.settings()[0].ajax.data = params;
//     mainTable.ajax.reload(null,false);
//     // iniDataTable();
// }
//





