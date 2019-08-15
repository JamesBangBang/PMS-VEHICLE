
var operatorSetting;
operatorSetting={
    autoTable:null,
    pwd:null,
    checkPwd:null,
    data:null,
    repeatTag:0,
    pwdTag:0,
    //车道表格初始化
    operatorTabel:function () {
        //清空表格
        if(this.autoTable){
            this.autoTable.destroy();
        }
        this.autoTable = $('#operatorTable').DataTable( {
            sort:false,
            processing:true,
            serverSide:true,
            searching:false,
            autoWidth:true,
            ajax: {
                url: starnetContextPath + "/auth/operator/list",
                type: 'POST'
            },
            columns:[
                {"data":"operator_user_name"},
                {"data":"operator_name"},
                {"data":"operator_id"},
                {"data":"operator_user_pwd"}
            ],
            "columnDefs": [
                {
                    "render": function(data, type, row, meta) {
                        return '<button class="btn btn-info btn-sm "  onclick="operatorSetting.editOperator('+meta.row+')">编辑</button> ' +
                            '<button class="btn btn-sm btn-danger"  onclick="operatorSetting.delConfirm('+meta.row+')">删除</button> ';
                    },
                    "targets": 2
                },
                {
                    "targets": [ 3 ],
                    "visible": false
                }

            ],

            language:dataTableLanguage

        });
    },
    //添加操作人员
    addOperator:function () {
        $("#addOperator").unbind("click").bind("click",function () {
            $("#loginName").val('');
            $("#operatorName").val('');
            $("#pwd").val('');
            $("#checkPwd").val('');
            $("#addOperatorModal").modal("show");
        })

        $("#opeSubmitBtn").unbind("click").bind("click",function () {

            // $('#pwd').keyup(function(e) {
            //     alert(12)
            //     var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
            //     var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
            //     var enoughRegex = new RegExp("(?=.{6,}).*", "g");
            //     if (false == enoughRegex.test($(this).val())) {
            //         $('#passstrength').html('More Characters');
            //     } else if (strongRegex.test($(this).val())) {
            //         $('#passstrength').className = 'ok';
            //         $('#passstrength').html('Strong!');
            //     } else if (mediumRegex.test($(this).val())) {
            //         $('#passstrength').className = 'alert';
            //         $('#passstrength').html('Medium!');
            //     } else {
            //         $('#passstrength').className = 'error';
            //         $('#passstrength').html('Weak!');
            //     }
            //     return true;
            // });

            //初始化
            operatorSetting.repeatTag==0;
            //判断密码
            this.pwd=$("#pwd").val();
            this.checkPwd=$("#checkPwd").val();
            if(strIsEmpty($("#loginName").val())){
                warningPrompt("请输入用户名");
                return;
            }
            var un = /^[a-zA-Z][a-zA-Z0-9_]{4,15}$/i;
            var isUsername = un.test($("#loginName").val());
            if(!isUsername){
                warningPrompt("用户名为字母开头，允许5-16字符，允许字母数字下划线");
                return;
            }
            if(strIsEmpty($("#operatorName").val())){
                warningPrompt("请输入操作人员姓名");
                return;
            }
            if(strIsEmpty(this.pwd)){
                warningPrompt("请输入密码");
                return;
            }
            if(strIsEmpty(this.pwd)){
                warningPrompt("请输入密码");
                return;
            }
            if(strIsEmpty(this.checkPwd)){
                warningPrompt("请输入确认密码");
                return;
            }
            if(this.checkPwd != this.pwd){
                warningPrompt("两次输入的密码不同");
                return;
            }
            //判断操作员是否存在
            operatorSetting. isPwdRepeat();

            if(operatorSetting.repeatTag==1)
            {
                return;
            }
            //保存数据
            operatorSetting.saveOperator();


        })
    },
    //判断操作员是否相同
    isPwdRepeat:function () {
        this.params={
            operatorLoginName: $("#loginName").val(),
            operatorName: $("#operatorName").val()
        };
        $.ajax({
            url: starnetContextPath + "/auth/operator/isPwdRepeat",
            type: "post",
            async: false,
            data: JSON.stringify(this.params),
            dataType: 'json',
            contentType: 'application/json',
            beforeSend: function (request) {
                request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            },
            success: function (res) {
                if(res!=0)
                {
                    if(res==1){
                        errorPrompt("登录用户名不能相同！");
                    }else if(res==2){
                        errorPrompt("操作员姓名不能相同！");
                    }
                    operatorSetting.repeatTag=1;
                    return false
                }else {
                    operatorSetting.repeatTag=0;
                }
              },
            error:function(){
                errorPrompt("无法访问网络","");
                return false
            }
        });
    },
    //保存数据
    saveOperator:function () {
        this.params={
            operatorLoginName: $("#loginName").val(),
            operatorName: $("#operatorName").val(),
            pwd:$("#pwd").val(),
        };
        ajaxReuqest(starnetContextPath + "/auth/operator/save","post",this.params,function(res){
            if(res.result == 0){
                successfulPrompt("添加成功");
                operatorSetting.autoTable.ajax.reload(null,false);
                $('#addOperatorModal').modal("hide");
            }else{
                errorPrompt("添加失败",res.msg);
            }
        });
        $("#adOperatorForm :input").not(":button, :submit, :reset, :hidden").val("").removeAttr("checked").remove("selected");
    },

    //删除确认
    delConfirm:function (rowIndex) {
        this.data=this.autoTable.row(rowIndex).data();
        $("#deleteOperatorModal").modal("show");
        $("#deleteOperatorBtn").unbind("click").bind("click",function () {
            operatorSetting.deleteOperator();
            $("#deleteOperatorModal").modal("hide");
        });
    },
    //删除
    deleteOperator:function()
    {
        this.params={
            operatorId:this.data.operator_id
        };
        $.post(starnetContextPath + "/auth/operator/del",this.params,function(res){
            if(res.result == 0){
                successfulPrompt("删除成功");
                operatorSetting.autoTable.ajax.reload(null,false);
            }else{
                errorPrompt("删除失败",res.msg);
            }
        });
    },
    //编辑
    editOperator:function (rowIndex) {
        $("#editOperatorModal").modal("show");
        $(".pwd").hide();
        $("#editOperatorModal :input").not(":button, :submit, :reset, :hidden").val("").removeAttr("checked").remove("selected");
        operatorSetting.pwdTag=0;
        this.data=this.autoTable.row(rowIndex).data();
        $("#editLoginName").val(this.data.operator_user_name);
        $("#editOperatorName").val(this.data.operator_name);
        //修改密码的按钮
        $("#modifyPwd").unbind("click").bind("click",function () {
            $(".pwd").show();
            operatorSetting.pwdTag=1;

        });
        //提交数据
        $("#editSubmitBtn").unbind("click").bind("click",function () {
            if(strIsEmpty($("#editOperatorName").val())){
                warningPrompt("请输入操作人员姓名");
                return;
            }
            this.params={
                opertorId:operatorSetting.data.operator_id,
                operatorLoginName:$("#editLoginName").val(),
                pwdTag:operatorSetting.pwdTag,
                operatorName: $("#editOperatorName").val(),
                repeatoperatorLoginName:operatorSetting.data.operator_user_name,
                repeatoperatorName:operatorSetting.data.operator_name,
                oldPwd:$("#oldPwd").val(),
                checkOldPwd:operatorSetting.data.operator_user_pwd,
                newPwd:$("#newPwd").val(),
                checkNewPwd:$("#checkNewPwd").val()
            };
            ajaxReuqest(starnetContextPath + "/auth/operator/edit","post",this.params,function(res){
                if(res.result == 0){
                    successfulPrompt("修改成功");
                    operatorSetting.autoTable.ajax.reload(null,false);
                    $("#editOperatorModal").modal("hide");
                    operatorSetting.pwdTag=0;
                    $("#editOperatorModal :input").not(":button, :submit, :reset, :hidden").val("").removeAttr("checked").remove("selected");
                }else{
                    errorPrompt("修改失败",res.msg);
                }
            });
        });},

    button:function () {
        $("#close").unbind("click").bind("click",function () {
            $(".pwd").hide();
            operatorSetting.pwdTag=0;

        });
    }

}

$(function () {

    operatorSetting.operatorTabel();
    operatorSetting.addOperator();
    operatorSetting.button();

});

// 刷新表格数据
function refreshBtnClick() {
    operatorSetting.autoTable.ajax.reload(null,true);
}
