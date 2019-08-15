

$(function(){
    iniDataTable();

});


//表格数据
function iniDataTable(){
    $('#memberRecharge').dataTable( {
        ordering: true,
        sort:false,
        processing:true,
        searching:false,
        autoWidth:true,
        lengthChange:false,
        data: [
        ],
        columnDefs: [
            {
                "render": function(data, type, row, meta) {
                    return '<span style="color: #767676">'+data+'</span>'
                },
                "targets": [0,1, 2,3,4],
            },
        ],
        language:dataTableLanguage
    } );
}











