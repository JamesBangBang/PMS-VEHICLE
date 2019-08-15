/**
 * Created by 宏炜 on 2017-06-19.
 */
$().ready(function () {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#addForm").validate({
        rules: {
            orgName: "required",
            orgNodeName: "required",
            orgNodeAddress: "required",
            nodeLongitude: "required",
            nodeLatitude: "required",
            devId:"required",
            channelSelect:"required",
            orgName: {
                required: true
            },
            nodeName: {
                required: true
            },
            orgNodeAddress:{
                required: true
            },
            nodeLongitude:{
                required: true,
                number:true,
                min:0,
                max:180
            },
            nodeLatitude:{
                required: true,
                number:true,
                min:0,
                max:90
            },
            devId:{
                required: true
            },
            channelSelect:{
                required: true
            }
        },
        messages: {
            orgName: {
                required: icon + "归属位置不能为空"
            },
            orgNodeName: {
                required: icon + "请输入位置节点名称"
            },
            orgNodeAddress: {
                required: icon + "请输入位置节点地址"
            },
            nodeLongitude: {
                required: icon + "请输入位置节点经度"
            },
            nodeLatitude: {
                required: icon + "请输入位置节点纬度"
            },
            devId: {
                required: icon + "请选择绑定设备"
            },
            channelSelect:{
                required: icon + "请选择数字通道"
            }
        }
    });

});

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
                var params = {
                    orgId:data.id
                }
                mainTable.settings()[0].ajax.data = params;
                mainTable.ajax.reload(null,false);
            });
        }else{
            warningPrompt("获取组织机构树信息失败","");
        }
    });
}
function initDevSelect(){
    $.post(starnetContextPath + "/node/getDeviceOnline",function(res){
        if(res.result == 0){
            for(var i in res.devices){
                var item = '<option value="' + res.devices[i].dev_id + '" devSn="' + res.devices[i].dev_sn + '">' + res.devices[i].dev_name + '</option>';
                $('#devId').append(item);
                // $('#editDevId').append(item);
            }
            // $('#editDevId').chosen({
            //     width:"100%"
            // });
            $('#devId').chosen({
                width:"100%"
            });
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });
}

$(function(){

    var treeHeight = document.body.offsetHeight - 95;
    $('.full-height-scroll').slimScroll({
        height: treeHeight + 'px'
    });
    initTree("");

    initDevSelect();
    initNvrSelect();
    iniDataTable();


    $('#addModal').on('shown.bs.modal', function () {

        initMap()
    })

});

$(function () {
    $('#addBtn').click(function(){
        if($("#channelSelect").val() == null){
            alert("请选择数字通道");
            return;
        }
        if (! $("#addForm").valid()) {
            return;
        }

        var params = $("#addForm").serializeObject();
        params.devSn = $('#devId option:selected').attr("devSn");
        // console.log(params)
        ajaxReuqest(starnetContextPath + "/node/addNode","post",params,function (res) {
            if(res.result == 0){
                mainTable.ajax.reload(null,false);
                successfulPrompt("添加成功","");
                $("#addModal").modal("hide");
            }else{
                errorPrompt("添加失败",res.msg);
            }
        });
    });
    $('#editBtn').click(function(){
        if (! $("#editForm").valid()) {
            return;
        }
        var params = $("#editForm").serializeObject();
        ajaxReuqest(starnetContextPath + "/node/updateNode","post",params,function (res) {
            if(res.result == 0){
                mainTable.ajax.reload(null,false);
                successfulPrompt("修改成功","");
                $("#editModal").modal("hide");
            }else{
                errorPrompt("修改失败",res.msg);
            }
        });
    });
});

var mainTable;
function iniDataTable(){
    mainTable = $('#autoTable').DataTable({
        sort:false,
        processing:true,
        serverSide:true,
        searching:false,
        autoWidth:true,
        ajax: {
            url: starnetContextPath + "/node/list",
            type: 'POST',
            data:{
                orgId:""
            }
        },
        columns:[
            {"data":"orgNodeName"},
            {"data":"orgNodeAddress"},
            {"data":"orgName"},
            {"data":"nodeLongitude"},
            {"data":"nodeLatitude"},
            {"data":"devName"},
            {"data":"limitNumber"},
            {"data":"id"},
            {"data":"orgId"},
            {"data":"devSn"},
            {"data":"devId"},
            {"data":"channelName"}
        ],
        "columnDefs": [
            {
                "render": function(data, type, row, meta) {
                    return '<button class="btn btn-warning btn-sm" onclick="editFunction('+meta.row+')">修改</button>' +
                        ' <button class="btn btn-danger btn-sm" onclick="deleteFunction('+meta.row+')">删除</button>';
                },
                "targets": 7
            },
            {
                "targets": [ 8, 9, 10, 11 ],
                "visible": false
            },
        ],
        language:dataTableLanguage
    });
}

function editFunction(rowIndex){
    var editData = mainTable.row(rowIndex).data();
    $("#editForm").populateForm(editData);
    $("#editModal").modal("show");
}

function deleteFunction(rowIndex){
    var deleteData = mainTable.row(rowIndex).data();
    var params = {
        id:deleteData.id
    }
    $.post(starnetContextPath + "/node/deleteNode",params,function(res){
        if(res.result == 0){
            mainTable.ajax.reload(null,false);
            successfulPrompt("删除成功","");
        }else{
            errorPrompt("删除失败",res.msg);
        }
    });
}


function initMap(){
    var map = new BMap.Map("allmap");    // 创建Map实例
    map.centerAndZoom(new BMap.Point(parseFloat(sysLon),parseFloat(sysLat)), 20);
    // 初始化地图,设置中心点坐标和地图级别
    map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
    map.setCurrentCity("福州");          // 设置地图显示的城市 此项是必须设置的
    map.enableScrollWheelZoom(true);

    var point = new BMap.Point(parseFloat(sysLon),parseFloat(sysLat));
    var marker = new BMap.Marker(point);// 创建标注
    map.addOverlay(marker);             // 将标注添加到地图中
    marker.enableDragging();           // 可拖拽


    marker.addEventListener("dragend", function showInfo(){
        var cp = map.getCenter();
        // alert(cp.lng + "," + cp.lat);
        $('#nodeLongitude').val(cp.lng)
        $('#nodeLatitude').val(cp.lat)
    });


}

function initNvrSelect(){
    $.post(starnetContextPath + "/node/getNvrSelect",function(res){
        if(res.result == 0){
            for(var i in res.nvrs){
                if(i == 0){
                    initChannelSelect(res.nvrs[i].id);
                }
                var item = '<option value="' + res.nvrs[i].id + '" >' + res.nvrs[i].text + '</option>';
                $('#nvrSelect').append(item);

            }
            $('#nvrSelect').chosen({
                width:"100%"
            }).on("change", function (evt, params) {
                initChannelSelect(params.selected);
            });
        }else{
            warningPrompt("获取数字录像机信息失败","");
        }
    });
}
function initChannelSelect(parentId){
    var params = {
        "parentId":parentId
    };
    $.post(starnetContextPath + "/node/getChannelSelect",params,function(res){
        if(res.result == 0){
            $('#channelSelect').empty();
            for(var i in res.channels){
                var item = '<option value="' + res.channels[i].id + '" >' + res.channels[i].text + '</option>';
                $('#channelSelect').append(item);

            }
            $('#channelSelect').chosen({
                width:"100%"
            }).trigger("chosen:updated");

        }else{
            warningPrompt("获取数字录像机信息失败","");
        }
    });
}