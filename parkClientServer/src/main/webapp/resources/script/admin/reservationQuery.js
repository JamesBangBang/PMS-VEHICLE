

$(function(){
    iniDataTable();

});


//表格数据
function iniDataTable(){
    $('#reservationQuery').dataTable( {
        ordering: true,
        sort:false,
        processing:true,
        searching:false,
        autoWidth:true,
        lengthChange:false,
        data: [["","闽A835CB","LZ","13559143688","vip会员","20170503","20170929"],
            ["","闽A835CB","LZ","13559143688","vip会员","20170503","20170929"],
            ["","闽A835CB","LZ","13559143688","vip会员","20170503","20170929"],
            ["","闽A835CB","LZ","13559143688","vip会员","20170503","20170929"],
            ["","闽A835CB","LZ","13559143688","vip会员","20170503","20170929"],
            ["","闽A835CB","LZ","13559143688","vip会员","20170503","20170929"],
            ["","闽A835CB","LZ","13559143688","vip会员","20170503","20170929"]
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











