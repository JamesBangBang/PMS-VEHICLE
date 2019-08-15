

$(function(){
    iniDataTable();

});


//表格数据
function iniDataTable(){
    $('#releaseDetails').dataTable( {
        ordering: true,
        sort:false,
        processing:true,
        searching:false,
        autoWidth:true,
        lengthChange:false,

        data: [
            ["是","闽A835CB","","月租车","月租车","","","2017年9月15日 上午8:10:19","601A"],
            ["是","闽AD9913","","月租车","月租车","","","2017年9月15日 上午8:09:29","601A"],
            ["是","闽AD9913","","月租车","月租车","","","2017年9月15日 上午8:07:29","601A"],
            ["是","闽AS8196","","月租车","月租车","","","2017年9月15日 上午8:01:29","601A"],
            ["是","闽ADD399","","月租车","月租车","","","2017年9月15日 上午8:02:29","601A"],
            ["是","闽A00U13","","月租车","月租车","","","2017年9月15日 上午8:15:29","601A"],
            ["是","闽A835CB","","月租车","月租车","","","2017年9月15日 上午8:17:29","601A"],
            ["是","闽A835CB","","月租车","月租车","","","2017年9月15日 上午8:18:29","601A"]
        ],
        columnDefs: [
            {
                "render": function(data, type, row, meta) {
                    return '<span style="color: #767676">'+data+'</span>'
                },
                "targets": [0,1, 2,3,4,5,6,7,8],
            },
        ],
        language:dataTableLanguage
    } );
}











