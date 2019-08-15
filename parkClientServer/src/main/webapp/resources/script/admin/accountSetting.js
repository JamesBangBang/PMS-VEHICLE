

$(function(){
    iniDataTable();

});


//表格数据
function iniDataTable(){
    $('#List').dataTable( {
        ordering: true,
        sort:false,
        processing:true,
        searching:false,
        autoWidth:true,
        lengthChange:false,
        data: [["闽A835CB","出",1,"92d4800224bc4adf96574544711f80e8","601门岗出口","292e2caf69ae4c118f97c102d902f91f","601门岗出口"],
            ["闽AD9913","出",1,"92d4800224bc4adf96574544711f80e8","601门岗出口","292e2caf69ae4c118f97c102d902f91f","601门岗出口"],
            ["闽AD9913","出",1,"92d4800224bc4adf96574544711f80e8","601门岗出口","292e2caf69ae4c118f97c102d902f91f","601门岗出口"],
            ["闽AS8196","出",1,"92d4800224bc4adf96574544711f80e8","601门岗出口","292e2caf69ae4c118f97c102d902f91f","601门岗出口"],
            ["闽ADD399","出",1,"92d4800224bc4adf96574544711f80e8","601门岗出口","292e2caf69ae4c118f97c102d902f91f","601门岗出口"],
            ["闽A00U13","进",11,"92d4800224bc4adf96574544711f80e8","601门岗出口","292e2caf69ae4c118f97c102d902f91f","601门岗出口"],
            ["闽A835CB","出",1,"92d4800224bc4adf96574544711f80e8","601门岗出口","292e2caf69ae4c118f97c102d902f91f","601门岗出口"],
            ["闽A835CB","进出",11,"92d4800224bc4adf96574544711f80e8","601门岗出口","292e2caf69ae4c118f97c102d902f91f","601门岗出口"]
        ],
        columnDefs: [
            {
                "render": function(data, type, row, meta) {
                    return '<span style="color: #767676">'+data+'</span>'
                },
                "targets": [0,1, 2,3,4,5,6],
            },
        ],
        language:dataTableLanguage
    } );
}











