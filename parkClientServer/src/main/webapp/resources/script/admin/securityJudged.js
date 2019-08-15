/**
 * Created by 宏炜 on 2017-06-23.
 */
$(function(){
    initTree("");
    $("#devId").chosen({
        width:"100%"
    });


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
    $('#start').val(laydate.now(-1, 'YYYY-MM-DD hh:mm:ss'));
    $('#end').val(laydate.now(0, 'YYYY-MM-DD hh:mm:ss'));
    var treeHeight = document.body.offsetHeight - 95;
    $('.full-height-scroll').slimScroll({
        height: treeHeight + 'px'
    });
    // iniDataTable();
});

var mainTable = null;
function iniDataTable(){
    var macsType = "";

    if($('#systemUser')[0].checked && !$('#otherUser')[0].checked){
        macsType = "sysUser";
    }
    if(!$('#systemUser')[0].checked && $('#otherUser')[0].checked){
        macsType = "umacs";
    }

    mainTable = $('#autoTable').DataTable({
        sort:false,
        processing:true,
        serverSide:true,
        searching:false,
        autoWidth:true,
        data:[],
        ajax: {
            url: starnetContextPath + "/count/getCountMacListSafe",
            type: 'POST',
            data:{
                "orgId": $('#devId').val(),
                "startMonth": $("#start").val(),
                "stopMonth": $("#end").val(),
                "macsType":macsType
            }
        },
        columns:[
            {"data":"macs_name"},
            {"data":"macs_id"},
            {"data":"org_node_name"},
            {"data":"tamp1"},
            {"data":"dev_id"}
        ],
        "columnDefs": [
            {
                "render": function(data, type, row, meta) {
                    return '<button class="btn btn-success btn-sm" onclick="editFunction('+meta.row+')">详情</button>'
                },
                "targets": 4,

            }
        ],

        language:dataTableLanguage

    });
}



function initTree(orgId){
    $.post(starnetContextPath + "/org/orgTree",function(res){
        if(res.result == 0){
            $('#orgTree').jstree('destroy');
            $('#orgTree').jstree({
                'core': {
                    'multiple':false,
                    'data':res.data
                }
            });

            $("#orgTree").on("loaded.jstree",function(){
                if(orgId != "") {
                    $("#orgTree").jstree().select_node(orgId, false, false);
                }
            });
            $("#orgTree").on("select_node.jstree",function(event,node){
                var data = node.node;
                $('#orgName').val(data.text);
                $('#orgId').val(data.id);
                initDevSelect(data.id);
                var params = {
                    "orgId":data.id,
                    "userType":$("#queryUserType").val()
                }
                mainTable.settings()[0].ajax.data = params;
                mainTable.ajax.reload(null,false);
            });
        }else{
            warningPrompt("获取组织机构树信息失败","");
        }
    });
}

function initDevSelect(orgid){
    $.post(starnetContextPath + "/node/getOrgNode?orgId="+orgid,function(res){
        if(res.result == 0){
            $('#devId').empty();
            for(var i in res.devices){
                var item = '<option value="' + res.devices[i].id + '" >' + res.devices[i].org_node_name + '</option>';
                $('#devId').append(item);
            }

            $('#devId').chosen({
                width:"100%"
            });
            $('#devId').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}
  $("#searchBtn").click(function(){
    if(mainTable == null){
        var startTime=new Date($("#start").val()).getTime();
        var endTime=new Date($("#end").val()).getTime()
        if(startTime>=endTime)
        {
            alert("时间信息筛选错误");
            return;
        }

        if($('#devId').val()==null)
        {
            alert("请选择节点");
            return;
        }
        iniDataTable();
        //

    }else{
        var macsType = "";

        if($('#systemUser')[0].checked && !$('#otherUser')[0].checked){
            macsType = "sysUser";
        }
        if(!$('#systemUser')[0].checked && $('#otherUser')[0].checked){
            macsType = "umacs";
        }

        var params = {
            "orgId": $('#devId').val(),
            "startMonth": $("#start").val(),
            "stopMonth": $("#end").val(),
            "macsType":macsType
        }
        mainTable.settings()[0].ajax.data = params;
        mainTable.ajax.reload(null,true);
    }

});


  function editFunction(rowIndex){
    var editData = mainTable.row(rowIndex).data();
    var chartParams = {
        mac: editData.macs_id,
        orgId: $('#devId').val(),
        startMonth:$("#start").val(),
        stopMonth:$("#end").val()
    };

//改这里
    $.post(starnetContextPath + "/path/getTimePath",chartParams,function(res){
        if(res.result == 0) {

            $('#message').html(res.message);
        }

    });


    $("#descModal").modal("show");
    if(secTable == null){
        initSecTable(editData.macs_id, $('#devId').val(),$("#start").val(),$("#end").val());
    }else{
        var params = {
            "mac": editData.macs_id,
            "startMonth": $("#start").val(),
            "orgId": $('#devId').val(),
            "stopMonth": $("#end").val()
        }
        secTable.settings()[0].ajax.data = params;
        secTable.ajax.reload(null,true);
    }

}
var secTable = null;
function initSecTable(mac,startMonth,stopMonth){
    secTable = $('#secTable').DataTable({
        sort:false,
        processing:true,
        serverSide:true,
        searching:false,
        autoWidth:true,
        data:[],
        ajax: {
            url: starnetContextPath + "/path/getTimePathList",
            type: 'POST',
            data:{
                "mac": mac,
                "orgId": $('#devId').val(),
                "startMonth": $("#start").val(),
                "stopMonth": $("#end").val()
            }
        },
        columns:[
            {"data":"name"},
            {"data":"mac"},
            {"data":"org_node_name"},
            {"data":"stime"},
            {"data":"etime"},
            {"data":"tamp"}
        ],
        "columnDefs": [
            {
                "render": function(data, type, row, meta) {
                    return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                },
                "targets": 3
            },
            {
                "render": function(data, type, row, meta) {
                    return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                },
                "targets": 4
            },
            {
                "render": function(data, type, row, meta) {
                    // onclick="showNextLevel(\''+data.child[i].id+'\',\''+data.child[i].type+'\',1)"
                    // return '<button class="btn btn-success btn-sm" onclick="editFunction('+meta.row+')">详情</button>'

                    return '<button class="btn btn-success btn-sm" onclick="screenFunction('+meta.row+')">回放</button>'
                },
                "targets": 6

            }


        ],

        language:dataTableLanguage

    });
}



function screenFunction(rowIndex){
    var editData = secTable.row(rowIndex).data();
    // alert($('#devId').val())
    var devId =$('#devId').val()
    var start=new Date(editData.stime).Format("yyyy-MM-dd hh:mm:ss");
    var  end=new Date(editData.etime).Format("yyyy-MM-dd hh:mm:ss");
    var params = {
        devId:devId
    };

    $.post(starnetContextPath + "/screen/nowScreen",params,function(res){
        if(res.result == 0){
            $("#videoModal").modal("show");
            SnCloud_StartPlayback(res.data.serverIp,res.data.serverPort,res.data.username,res.data.password,res.data.channelId, start,end);

        }else{
            errorPrompt("回放视频信息加载失败",res.msg);
        }
    });
}

