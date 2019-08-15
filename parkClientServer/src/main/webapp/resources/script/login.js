/**
 * Created by 宏炜 on 2017-06-15.
 */
$().ready(function () {

    // validate signup form on keyup and submit
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#loginForm").validate({
        rules: {
            username: "required",
            password: "required",
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            username: {
                required: icon + "请输入用户名"
            },
            password: {
                required: icon + "请输入密码"
            }
        }
    });
});
var rememberMeFlag = 0;
$(document).ready(function () {
    $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });
    $('#rememberMe').on('ifChecked', function(event){
        rememberMeFlag = 1;
    });
    $('#rememberMe').on('ifUnchecked', function(event){
        rememberMeFlag = 0;
    });
});
$(document).keydown(function (e) {
    if(e.keyCode == 13){
        loginFunc();
    }
});
$("#loginBtn").click(function () {
    loginFunc();
});
function loginFunc() {
    if (! $("#loginForm").valid()) {
        return;
    }
    var params = {
        "username":$("#username").val(),
        "password":$("#password").val(),
        "rememberMe":rememberMeFlag
    };
    $.post(starnetContextPath + "/account/login",params,function(res){
        if(res.result == 0){
            window.location.href= starnetContextPath + res.data.url;
        }else{
            $("#errorTips").html('<i class="fa fa-times-circle"></i> ' + res.msg);
        }
    },"json");
}