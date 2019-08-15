/**
 * Created by 宏炜 on 2017-06-15.
 */
//各种表单验证


//初始化执行
$().ready(function () {
    // validate signup form on keyup and submit
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#from").validate({
        rules: {
            systemTitle:{
                required: true,
                minlength: 2,
                maxlength:20
            },
            copyRight: {
                required: true,
                minlength: 2,
                maxlength:20
            },
            systemLon: {
                required: true,
                number:true

            },
            systemLat: {
                required: true,
                number:true

            },
            middleNum: {
                required: true,
                digits:true,
                min:0,
                max:99999

            },
            maxNum: {
                required: true,
                digits:true,
                min:0,
                max:99999

            }
        },
        messages: {
            systemTitle:{
                required: icon + "请输入系统标题信息",
                minlength: icon + "必须两个字符以上",
                maxlength: icon + "不能超过20个字符"
            },
            copyRight: {
                required: icon + "请输入版权信息",
                minlength: icon + "必须两个字符以上",
                maxlength: icon + "不能超过20个字符"
            },
            systemLon: {
                required: icon + "请输入经度信息"
            },
            systemLat:{
                required: icon + "请输入纬度信息"
            },

            middleNum: {
                required:  icon + "请输入普通信息"

            },
            maxNum: {
                required: icon + "请输入密集信息"

            }


        }
    });

});

jQuery.extend(jQuery.validator.messages, {
    digits: "请输入整数",

});




$(function () {

    //刷新加载数据
        $.post(starnetContextPath + "/system/SystemForm",function(res){
            if(res.result == 0){

                // if(res.systemForm==1)
                // {

                    $('#systemTitle').val(res.systemTitle);
                    $('#copyRight').val(res.copyRight);
                    $('#systemLon').val(res.systemLon);
                    $('#systemLat').val(res.systemLat);
                    $('#middleNum').val(res.middleNum);
                    $('#maxNum').val(res.maxNum);
                // }

            }else{
                errorPrompt("数据库信息错误",res.msg);
            }
        });

    //初始化按钮
    $.post(starnetContextPath + "/system/SystemSet",function(res){
        if(res.result == 0){
            if(res.value==0)
            {
                $("#reduction").hide();
            }
            else {
                $("#reduction").show();
            }

        }else{
            errorPrompt("数据库信息错误",res.msg);
        }
    });


    $("#systemSetBtn").click(function(){
        //验证
        if (! $("#from").valid()) {
                  alert("信息错误")
                    return;
                }
        var params = $("#from").serializeObject();
        ajaxReuqest(starnetContextPath +"/system/mergeSystemSet","POST",params,
            function(res){
                if(res.result == 0){
                    successfulPrompt("提交成功","");
                    //设置表单状态
                    // $.post(starnetContextPath + "/system/formState",function(res){
                    //     if(res.result == 0){
                    //
                    //     }else{
                    //     }
                    // });
                }else{
                    errorPrompt("提交失败",res.msg);
                }
            });
    });


});

//还原按钮
$("#reduction").click(function(){
    ajaxReuqest(starnetContextPath +"/node/initNode","POST",
        function(res){
            if(res.result == 0){
                // alert("还原成功");
            }else{
                // alert(res.msg);
            }
        }
        );

    //还原按钮变更
    $.post(starnetContextPath + "/system/ButtonValue",function(res){
        if(res.result == 0){
            $("#reduction").hide();
            successfulPrompt("还原成功","");

        }else{
            errorPrompt("还原失败",res.msg);
        }
    });

});










