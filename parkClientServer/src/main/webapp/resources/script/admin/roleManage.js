/**
 * Created by 宏炜 on 2017-10-24.
 */
var roleManage;
$(function () {

    roleManage = {
        autoTable:null,
        editData:null,
        editResourceIds:null,
        emptyData:{
            id: "",
            roleName: "",
            roleTag: "",
            description: ""
        },
        render:function () {
            this.initTable();
            this.bindAddRoleBtnClick();
            this.roleFormValidate();
            this.bindSubmitClick();
            this.initMenuTree();
        },
        initTable:function () {
            this.autoTable = $('#autoTable').DataTable( {
                sort:false,
                processing:true,
                serverSide:true,
                searching:false,
                autoWidth:true,
                ajax: {
                    url: starnetContextPath + "/auth/role/list",
                    type: 'POST'
                },
                columns:[
                    {"data":"roleName"},
                    {"data":"roleTag"},
                    {"data":"description"},
                    {"data":"id"}
                ],
                "columnDefs": [
                    {
                        "render": function(data, type, row, meta) {
                            return '<button class="btn btn-info btn-sm" onclick="roleManage.editFunc('+meta.row+')">修改</button>'
                                + '<button class="btn btn-danger btn-sm" onclick="roleManage.delFunc('+meta.row+')">删除</button>';
                        },
                        "targets": 3
                    }
                ],
                language:dataTableLanguage

            });
        },
        initMenuTree:function () {
            $("#menuTree").jstree("destroy");
            $("#menuTree").jstree({
                'core' : {
                    "check_callback" : true,
                    'multiple':true,
                    'data': {
                        'cache':false,
                        'url': function (node) {
                            if(node.id == "#"){
                                return starnetContextPath + "/auth/menu/tree";
                            }
                            return starnetContextPath + "/auth/menu/tree";
                        },
                        'data': function (node) {
                            return {
                                'id': node.id
                            };
                        }
                    }
                },
                "checkbox" : {
                    "keep_selected_style" : true
                },
                "plugins" : [ "checkbox" ]
            });
            $("#menuTree").on("select_node.jstree",function(event,node){
                var parent = $("#menuTree").jstree().get_parent(node.node);
                if(parent != "#"){
                    var parentNode = $("#menuTree").jstree().get_node(parent);
                    parentNode.original.state = {
                        "undetermined":true
                    }
                }
            });
            $("#menuTree").on("load_node.jstree",function(event,node){
                $('#menuTree').jstree().select_node(roleManage.editResourceIds);
            });
            $('#menuTree').jstree(true).get_all_checked = function(full) {
                var tmp=new Array;
                for(var i in this._model.data){
                    if(i!="#") {
                        if (this.is_undetermined(i) || this.is_checked(i)) {
                            tmp.push(full ? this._model.data[i] : i);
                        }
                    }
                }
                return tmp;
            };
        },
        editFunc:function (rowIndex) {
            this.editData = this.autoTable.row(rowIndex).data();
            $("#roleName").attr("disabled","disabled");
            $("#roleForm").populateForm(this.editData);
            $.post(starnetContextPath + '/auth/role/permission/info',{roleId:this.editData.id},function(res){
                if(res.result == 0){
                    roleManage.editResourceIds = res.data;
                    $('#menuTree').jstree().deselect_all();
                    $('#menuTree').jstree().open_node(res.selectData);
                    $('#menuTree').jstree().select_node(res.data);
                }
            },'json');
            $("#editModal").modal("show");
        },
        delFunc:function (rowIndex) {
            if (!confirm("删除后将永久丢失数据，确认要删除？")) {
                return;
            }
            var delData = this.autoTable.row(rowIndex).data();
            var params = {
                "id":delData.id
            };
            $.post(starnetContextPath + '/auth/role/delete',params,function(res){
                if(res.result == 0){
                    successfulPrompt("操作成功","");
                    roleManage.autoTable.ajax.reload(null,true);

                }else{
                    errorPrompt("操作失败",res.msg);
                }
            },'json');
        },
        bindSubmitClick: function () {
            $('#submitBtn').unbind("click").bind("click", function () {
                if (! $("#roleForm").valid()) {
                    return;
                }
                var params = $("#roleForm").serializeObject();
                var checkedNodes = $('#menuTree').jstree("get_all_checked");
                params.resourceIds = checkedNodes;
                ajaxReuqest(starnetContextPath + "/auth/role/merge",'post',params,function(res){
                    if(res.result == 0){
                        $("#editModal").modal("hide");
                        roleManage.autoTable.ajax.reload(null,true);
                        successfulPrompt("操作成功","");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                });
            });
        },
        bindAddRoleBtnClick:function () {

            $('#addRoleBtn').unbind("click").bind("click", function () {
                $("#roleForm").populateForm(roleManage.emptyData);
                $("#roleTag").removeAttr("disabled");
                $('#menuTree').jstree().deselect_all();

                $("#editModal").modal("show");
            });
        },
        roleFormValidate:function(){
            var icon = "<i class='fa fa-times-circle'></i> ";
            $("#roleForm").validate({
                rules: {
                    roleName: {
                        required: true
                    },
                    roleTag:{
                        required: true,
                        stringCheck:true
                    }
                },
                messages: {
                    roleName: {
                        required: icon + "角色名称不能为空"
                    },
                    roleTag:{
                        required: icon + "角色标识不能为空"
                    }
                }
            });
        }
    };
    roleManage.render();
});
// 刷新表格数据
function refreshBtnClick() {
    roleManage.autoTable.ajax.reload(null,true);
}
