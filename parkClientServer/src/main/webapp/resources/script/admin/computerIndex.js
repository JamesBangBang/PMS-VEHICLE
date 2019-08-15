/**
 * Created by 宏炜 on 2017-12-28.
 */
var computerManage;
$(function () {

    computerManage = {
        autoTable:null,
        editData:null,
        emptyData:{
            "postComputerId":"",
            "postComputerName":"",
            "postComputerIp":"",
            "operationName":"",
            "addTime":"",
            "status":""
        },
        render:function () {
            this.initTable();
            this.initCheckBtn();
            this.editFormValidate();
            this.bindSubmitClick();
            this.bindAddcomputerBtnClick();
            this.bindRefreshBtnClick();
        },
        initTable:function () {
            this.autoTable = $('#autoTable').DataTable( {
                sort:false,
                processing:true,
                serverSide:true,
                searching:false,
                autoWidth:true,
                ajax: {
                    url: starnetContextPath + "/post/computer/page/list",
                    type: 'POST'
                },
                columns:[
                    {"data":"postComputerName"},
                    {"data":"postComputerIp"},
                    {"data":"operationName"},
                    {"data":"addTime"},
                    {"data":"status"},
                    {"data":"isAutoDeal"},
                    {"data":"isVoicePlay"},
                    {"data":"postComputerId"}
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
                            if(data == 1){
                                return '<span style="color: #00A400;">在线</span>'
                            }else{
                                return '<span class="text-danger">离线</span>'
                            }
                        },
                        "targets": 4
                    },
                    {
                        "render": function(data, type, row, meta) {
                            if(data == 0){
                                return '是'
                            }else{
                                return '否'
                            }
                        },
                        "targets": 5
                    },
                    {
                        "render": function(data, type, row, meta) {
                            if(data == 0){
                                return '是'
                            }else{
                                return '否'
                            }
                        },
                        "targets": 6
                    },
                    {
                        "render": function(data, type, row, meta) {
                            return '<button class="btn btn-info btn-sm" onclick="computerManage.editFunc('+meta.row+')">修改</button>'
                                + '<button class="btn btn-danger btn-sm" onclick="computerManage.delFunc('+meta.row+')">删除</button>';
                        },
                        "targets": 7
                    },
                    {
                        "targets": [ 6 ],
                        "visible": false
                    }
                ],
                language:dataTableLanguage

            });
        },

        editFunc:function (rowIndex) {
            this.editData = this.autoTable.row(rowIndex).data();
            $("#editForm").populateForm(this.editData);
            $("#addTime").val(new Date(this.editData.addTime).Format("yyyy-MM-dd hh:mm:ss"));
            if(this.editData.status == 1){
                $("#status").val("在线");
            }else{
                $("#status").val("离线");
            }
            if(this.editData.isAutoDeal == 0){
                $("#isAutoDeal").bootstrapSwitch("state",true);
            }else{
                $("#isAutoDeal").bootstrapSwitch("state",false);
            }
            if(this.editData.isVoicePlay == 0){
                $("#isVoicePlay").bootstrapSwitch("state",true);
            }else{
                $("#isVoicePlay").bootstrapSwitch("state",false);
            }
            $("#editModal").modal("show");
            setTimeout('$("#editForm").valid()',300);
        },
        delFunc:function (rowIndex) {
            if (!confirm("删除后将永久丢失数据，确认要删除？")) {
                return;
            }
            var delData = this.autoTable.row(rowIndex).data();
            var params = {
                "id":delData["postComputerId"]
            };
            $.post(starnetContextPath + '/post/computer/delete',params,function(res){
                if(res.result == 0){
                    successfulPrompt("操作成功","");
                    computerManage.autoTable.ajax.reload(null,true);

                }else{
                    errorPrompt("操作失败",res.msg);
                }
            },'json');
        },
        bindSubmitClick: function () {
            $('#submitBtn').unbind("click").bind("click", function () {
                if (! $("#editForm").valid()) {
                    return;
                }


                var params = $("#editForm").serializeObject();

                if (params["postComputerId"] != ""){
                    //修改状态下
                    if (!confirm("修改操作请确认客户端处于离线状态，是否继续？")) {
                        return;
                    }
                }

                ajaxReuqest(starnetContextPath + "/post/computer/merge",'post',params,function(res){
                    if(res.result == 0){
                        $("#editModal").modal("hide");
                        computerManage.autoTable.ajax.reload(null,true);
                        successfulPrompt("操作成功","");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                });
            });
        },
        initCheckBtn:function () {
            $("#isAutoDeal").bootstrapSwitch({
                onText:' 是 ',
                offText:' 否 '
            });
            $("#isVoicePlay").bootstrapSwitch({
                onText:' 是 ',
                offText:' 否 '
            });

        },
        bindAddcomputerBtnClick:function () {
            $('#addcomputerBtn').unbind("click").bind("click", function () {
                $("#editForm").populateForm(computerManage.emptyData);
                $("#isAutoDeal").bootstrapSwitch("state",true);
                $("#isVoicePlay").bootstrapSwitch("state",true);
                $("#editModal").modal("show");

            });
        },
        bindRefreshBtnClick:function () {

            $('#refreshBtn').unbind("click").bind("click", function () {
                computerManage.autoTable.ajax.reload(null,true);
            });
        },
        editFormValidate:function(){
            var icon = "<i class='fa fa-times-circle'></i> ";
            $("#editForm").validate({
                rules: {
                    postComputerName: {
                        required: true
                    },
                    postComputerIp:{
                        required: true,
                        isIPV4:true
                    }
                },
                messages: {
                    postComputerName: {
                        required: icon + "岗亭名称不能为空"
                    },
                    postComputerIp:{
                        required: icon + "岗亭IP不能为空"
                    }
                }
            });
        }
    };
    computerManage.render();
});
