/**
 * Created by 宏炜 on 2017-06-15.
 */
//各种表单验证
//初始化执行
$().ready(function () {
    // validate signup form on keyup and submit
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#addForm").validate({
        rules: {
            orgName:{
                required: true
            },
            trueName: {
                required: true,
                minlength: 2,
                maxlength:20
            },
            phone: {
                required: true,
                isMobile:true
            },
            mac: {
                required: true,
                maxlength:32
            },
        },
        messages: {
            orgName:{
                required: icon + "请选择归属的组织机构"
            },
            trueName: {
                required: icon + "请输入姓名",
                minlength: icon + "姓名必须两个字符以上",
                maxlength: icon + "姓名不能超过20个字符"
            },
            phone: {
                required: icon + "请输入手机号码"
            },
            mac:{
                required: icon + "请输入手机MAC地址"
            }
        }
    });

    $("#editForm").validate({
        rules: {
            orgName:{
                required: true
            },
            trueName: {
                required: true,
                minlength: 2,
                maxlength:20
            },
            phone: {
                required: true,
                isMobile:true
            },
            mac: {
                required: true,
                maxlength:32
            },
        },
        messages: {
            orgName:{
                required: icon + "请选择归属的组织机构"
            },
            trueName: {
                required: icon + "请输入姓名",
                minlength: icon + "姓名必须两个字符以上",
                maxlength: icon + "姓名不能超过20个字符"
            },
            phone: {
                required: icon + "请输入手机号码"
            },
            mac:{
                required: icon + "请输入手机MAC地址"
            }
        }
    });
});
//各种函数
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

$(function(){
    var treeHeight = document.body.offsetHeight - 95;
    $('.full-height-scroll').slimScroll({
        height: treeHeight + 'px'
    });
    $('#timelineContainer').slimScroll({
        height: '390px'
    });


    initTree("");
    iniDataTable();

    $('#userType').chosen({
        width:"100%"
    });
    $('#editUserType').chosen({
        width:"100%"
    });
    $('#queryUserType').chosen({
        width:"100%"
    });

    $("#queryUserType").chosen().on("change", function (evt, params) {

        var orgs = $("#orgTree").jstree().get_selected();
        var orgId;
        if(orgs.length == 0){
            orgId = "";
        }else{
            orgId = orgs[0];
        }
        var params = {
            "orgId":orgId,
            "userType": params.selected
        }
        mainTable.settings()[0].ajax.data = params;
        mainTable.ajax.reload(null,false);
    });

});
var mainTable;
function iniDataTable(){

    mainTable = $('#personList').DataTable({
        sort:false,
        processing:true,
        serverSide:true,
        searching:false,
        autoWidth:true,
        ajax: {
            url: starnetContextPath + "/user/getUserTypeListForPage",
            type: 'POST',
            data:{
                "orgId":"",
                "userType": ""
            }
        },
        columns:[
            {"data":"trueName"},
            {"data":"phone"},
            {"data":"mac"},
            {"data":"roleName"},
            {"data":"orgName"},
            {"data":"id"},
            {"data":"resourceId"},
            {"data":"userType"}
        ],
        "columnDefs": [
            {
                "render": function(data, type, row, meta) {
                    return '<button class="btn btn-warning btn-sm" onclick="editFunction('+meta.row+')">修改</button>' +
                        ' <button class="btn btn-info btn-sm" onclick="trajectoryFunction('+meta.row+')">轨迹</button>' +
                        ' <button class="btn btn-danger btn-sm" onclick="deleteFunction('+meta.row+')">删除</button>';
                },
                "targets": 5
            },
            {
                "targets": [6, 7],
                "visible": false
            },

        ],

        language:dataTableLanguage

    });
}

$(function () {
    $('#addBtn').click(function(){
        if (! $("#addForm").valid()) {
            return;
        }
        var params = $("#addForm").serializeObject();
        ajaxReuqest(starnetContextPath + "/person/addPerson","post",params,function (res) {
            if(res.result == 0){
                $("#addModal").modal("hide");
                mainTable.ajax.reload(null,false);
                successfulPrompt("添加成功","");
               document.getElementById('addForm').reset();
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
        ajaxReuqest(starnetContextPath + "/person/updatePerson","post",params,function (res) {
            if(res.result == 0){
                $("#editModal").modal("hide");
                mainTable.ajax.reload(null,false);
                successfulPrompt("修改成功","");
            }else{
                errorPrompt("修改失败",res.msg);
            }
        });
    });
});

function editFunction(rowIndex){
    var editData = mainTable.row(rowIndex).data();
    $("#editForm").populateForm(editData);
    $("#editUserType").trigger("chosen:updated");
    $("#editModal").modal("show");
}
function deleteFunction(rowIndex){
    var deleteData = mainTable.row(rowIndex).data();
    var params = {
        userId:deleteData.id
    }
    $.post(starnetContextPath + "/person/daletePerson",params,function(res){
        if(res.result == 0){
            mainTable.ajax.reload(null,false);
            successfulPrompt("删除成功","");
        }else{
            errorPrompt("删除失败",res.msg);
        }
    });
}
var userType = "";
function trajectoryFunction(rowIndex) {
    var data = mainTable.row(rowIndex).data();
    userType = data.userType;
    $("#queryMac").val(data.mac);
    $("#trajectoryModal").modal("show");

}

function initCalendar(){
    $('#calendar').fullCalendar( 'destroy' );
    $('#calendar').fullCalendar({
        header: {
            left: 'prev,next',
            center: 'title',
            right: ''
        },
        editable: false,
        droppable: false,
        events: function(start, end, callback) {
            var now = new Date();
            var params = {
                start: now.getTime(),
                end: end.getTime(),
                mac:$("#queryMac").val()
            }

            $.post(starnetContextPath + "/path/getmonthList",params,function(res){
                if(res.result == 0){
                    var events = [];
                    for(var i in res.data){
                        var eventObj = new Object();
                        eventObj.title = "　";
                        eventObj.mac_date = res.data[i].mac_date;
                        eventObj.start = res.data[i].mac_date + " 00:00:00";
                        eventObj.end = res.data[i].mac_date + " 23:00:00";
                        eventObj.mac = res.mac;
                        eventObj.color = "gray";
                        if(res.userType == "1"){
                            eventObj.color= "red";
                        }
                        if(res.userType == "0"){
                            eventObj.color = "green";
                        }
                        if(res.userType == "2"){
                            eventObj.color = "blue";
                        }
                        events.push(eventObj);
                    }
                    callback(events);
                }else{
                    errorPrompt("考勤信息加载失败",res.msg);
                }
            },'json');

        },
        eventClick: function(calEvent, jsEvent, view) {

            var params = {
                month: calEvent.mac_date,
                mac:calEvent.mac
            }
            $.post(starnetContextPath + "/path/getDayPathList",params,function(res){
                if(res.result == 0){

                    $("#timelineContainer").empty();
                    for(var i in res.data){
                        var stime = new Date(res.data[i].stime).Format("hh:mm:ss");
                        var etime = new Date(res.data[i].etime).Format("hh:mm:ss");
                        var item = '<div class="timeline-item">' +
                            '<div class="row"> '+
                            '<div class="col-xs-3 date"> ' +
                            '<i class="fa fa-file-text"></i> 停留 '+ res.data[i].staytime + ' 分钟'+
                            '</div> '+
                            '<div class="col-xs-7 content no-top-border"> ' +
                            '<p class="m-b-xs"> ' +
                            '<strong>'+ res.data[i].place_name + '</strong> ' +
                            '<br> '+
                            '<small class="text-navy">进入时间:'+ stime + '</small> ' +
                            '<br> '+
                            '<small class="text-navy">离开时间:'+ etime + '</small> ' +
                            '</p>'+
                            '</div>'+
                            '</div>'+
                            '</div>';
                        $("#timelineContainer").append(item);
                    }
                }else{
                    errorPrompt("轨迹详情加载失败",res.msg);
                }
            },'json');

        }
    });
}

$('#trajectoryModal').on('shown.bs.modal', function () {
    initCalendar()
})



function uploadResult(){
    var body = $(window.frames['upframe'].document.body);
    var res = body.context.innerHTML + "";
    var obj = JSON.parse(res);
    if(obj.result == 0){
        $('#ImportModal').modal('hide');
        mainTable.ajax.reload(null,false);
    }else{
        alert(obj.msg);
    }
}



