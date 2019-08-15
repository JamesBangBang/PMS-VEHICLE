/**
 * Created by 宏炜 on 2017-10-24.
 */
var userManage;
$(function () {

    userManage = {
        autoTable:null,
        editData:null,
        parkData:null,
        emptyData:{
            email: "",
            id: "",
            parkItems: "",
            qq: "",
            roleItems: "",
            sex: "",
            telephone: "",
            trueName: "",
            userName:""
        },
        render:function () {
            this.initTable();
            this.initRoleSelect();
            this.initDepartment();
            this.bindSubmitClick();
            this.bindAddUserBtnClick();
            this.userFormValidate();
            $(".chosen").chosen({
                disable_search:true,
                width:"100%"
            });
        },
        initTable:function () {
            this.autoTable = $('#autoTable').DataTable( {
                ordering: true,
                sort:false,
                processing:true,
                searching:false,
                autoWidth:true,
                lengthChange:false,
                serverSide:true,
                // sort:false,
                // processing:true,
                // serverSide:true,
                // searching:false,
                // autoWidth:true,
                ajax: {
                    url: starnetContextPath + "/auth/user/list",
                    type: 'POST',
                    data: {
                        isShow: $('#selectRole').val()
                    }
                },
                columns:[
                    {"data":"userName"},
                    {"data":"trueName"},
                    {"data":"email"},
                    {"data":"telephone"},
                    {"data":"sex"},
                    {"data":"qq"},
                    {"data":"id"}
                ],
                "columnDefs": [
                    {
                        "render": function(data, type, row, meta) {
                            return '<button class="btn btn-info btn-sm" onclick="userManage.editFunc('+meta.row+')">修改</button>'
                                + '<button class="btn btn-danger btn-sm" onclick="userManage.delFunc('+meta.row+')">删除</button>';
                        },
                        "targets": 6
                    },
                    {
                        "render": function(data, type, row, meta) {
                            if(data == 'MALE'){
                                return '男';
                            }else if(data == 'FEMALE'){
                                return '女';
                            }else{
                                return '无';
                            }

                        },
                        "targets": 4
                    }
                ],
                language:dataTableLanguage

            });
        },
        initRoleSelect:function(){
            $.post(starnetContextPath + '/auth/role/select',function(res){
                if(res.result == 0){
                    for(var i in res.data){
                        var option = '<option value="'+ res.data[i].id +'">'+ res.data[i].roleName +'</option>'
                        $("#roleItems").append(option);
                    }
                    $("#roleItems").chosen({
                        disable_search:true,
                        width:"100%"
                    })
                }
            },'json');
        },
        initDepartment:function(){
            $.post(starnetContextPath + '/auth/department/select',function(res){
                if(res.result== 0){
                    for(var i in res.data){
                        var option = '<option value="'+ res.data[i].depId +'" selected>'+ res.data[i].depName +'</option>'
                        $("#departmentItems").append(option);
                    }
                    $("#departmentItems").chosen({
                        width:"100%"
                    });
                    $("#departmentItems").chosen().on("change", function (evt, params) {

                        userManage.initParkSelect($("#departmentItems").val(),userManage.parkData);
                    });
                }
            },'json');
        },
        initParkSelect:function (departmentId,parkData) {
            $("#parkItems").empty();
            if(!departmentId){
                $("#parkItems").trigger("chosen:updated");
                return;
            }
            var params = {
                "departmentIds":departmentId
            }

            ajaxReuqest(starnetContextPath + '/auth/park/select','post',params,function(res){
                if(res.result == 0){

                    for(var i in res.data){
                        var option = '<option value="'+ res.data[i].carparkId +'">'+ res.data[i].carparkName +'</option>'
                        $("#parkItems").append(option);

                    }
                    $("#parkItems").chosen({
                        language:"zh-CN",
                        width:"100%"
                    });

                    $("#parkItems").val(parkData);
                    $("#parkItems").trigger("chosen:updated");
                }
            });
            // $.post(starnetContextPath + '/auth/park/select',{departmentId:departmentId},function(res){
            //     if(res.result == 0){
            //
            //         for(var i in res.data){
            //             var option = '<option value="'+ res.data[i].carparkId +'">'+ res.data[i].carparkName +'</option>'
            //             $("#parkItems").append(option);
            //         }
            //         $("#parkItems").chosen({
            //             width:"100%"
            //         });
            //
            //         $("#parkItems").val(parkData);
            //         $("#parkItems").trigger("chosen:updated");
            //     }
            // },'json');
        },
        editFunc:function (rowIndex) {

            this.editData = this.autoTable.row(rowIndex).data();
            if(this.editData.userName == 'root'){
                $(".noRoot").hide();
            }else{
                $(".noRoot").show();
            }
            $("#userName").attr("disabled","disabled");
            $("#userForm").populateForm(this.editData);
            $("#sex").trigger('chosen:updated');
            $.post(starnetContextPath + '/auth/permission/info',{userId:this.editData.id},function(res){
                if(res.result == 0){
                    $("#departmentItems").val(res.departData);
                    $("#roleItems").val(res.roleData);
                    $("#departmentItems").trigger("chosen:updated");
                    $("#roleItems").trigger("chosen:updated");
                    userManage.initParkSelect(res.departData,res.parkData);
                    userManage.parkData = res.parkData;
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
            $.post(starnetContextPath + '/auth/user/delete',params,function(res){
                if(res.result == 0){
                    successfulPrompt("操作成功","");
                    userManage.autoTable.ajax.reload(null,true);

                }else{
                    errorPrompt("操作失败",res.msg);
                }
            },'json');
        },
        bindSubmitClick: function () {
            $('#submitBtn').unbind("click").bind("click", function () {
                if (! $("#userForm").valid()) {
                    return;
                }
                var params = $("#userForm").serializeObject();
                params["departmentItems"] = $("#departmentItems").val();
                ajaxReuqest(starnetContextPath + "/auth/user/merge",'post',params,function(res){
                    if(res.result == 0){
                        $("#editModal").modal("hide");
                        userManage.autoTable.ajax.reload(null,true);
                        successfulPrompt("操作成功","");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                });
            });
        },
        bindAddUserBtnClick:function () {

            $('#addUserBtn').unbind("click").bind("click", function () {
                userManage.parkData = null;
                userManage.editData = null;
                $(".noRoot").show();
                userManage.initParkSelect($("#departmentItems").val(),userManage.parkData);
                $("#userForm").populateForm(userManage.emptyData);
                $("#roleItems").trigger("chosen:updated");
                $("#departmentItems").trigger("chosen:updated");
                $("#parkItems").trigger("chosen:updated");
                $("#sex").trigger("chosen:updated");
                $("#userName").removeAttr("disabled");
                $("#editModal").modal("show");
            });
        },
        userFormValidate:function(){
            var icon = "<i class='fa fa-times-circle'></i> ";
            $("#userForm").validate({
                ignore: ":hidden:not(select)",
                rules: {
                    userName: {
                        required: true,
                        isUsername:true
                    },
                    trueName:{
                        required: true,
                        maxlength:30,
                        minlength:2
                    },
                    email:{
                        required: true,
                        email:true
                    },
                    telephone:{
                        required: true,
                        isMobile:true
                    },
                    sex:{
                        required: true
                    },
                    roleItems:{
                        required: true
                    },
                    parkItems:{
                        required: true
                    }
                },
                messages: {
                    userName: {
                        required: icon + "用户名不能为空"
                    },
                    trueName:{
                        required: icon + "真实姓名不能为空"
                    }

                }
            });
        },
    };

    userManage.render();


});
function search(event) {
    var params = {
        "isShow": $('#selectRole').val()
    };
    userManage.autoTable.settings()[0].ajax.data = params;
    userManage.autoTable.ajax.reload(null,false);
}

// 刷新表格数据
function refreshBtnClick() {
    userManage.autoTable.ajax.reload(null,true);
}

