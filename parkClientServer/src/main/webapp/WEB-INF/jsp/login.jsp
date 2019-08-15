<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录</title>
    <link href="${pageContext.request.contextPath}/resources/admin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/jsTree/style.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <script>if(window.top !== window.self){ window.top.location = window.location;}</script>
<style>

    .input-background::-webkit-input-placeholder{
        color: rgba(255,255,255,0.4);
    }

    .input-background{
        border: 1px solid rgba(0,150,255,0.9);
        background-color:rgba(255,255,255,0);


    }

    .input-background:focus{
        outline:none;

        background-color:rgba(0,150,255,0.1);
        font-color: rgba(0,0,0,1);
    }



</style>


</head>
<jsp:include page="commonValue.jsp"></jsp:include>
<body style=" background:url(${pageContext.request.contextPath}/resources/images/background.jpg);overflow: auto;">
<div  style="background:url(${pageContext.request.contextPath}/resources/images/login.png);width:902px;height:632px;margin-left: -451px;margin-top:-316px;position:absolute;top: 50%;left: 50%">
    <div class="middle-box text-center loginscreen  animated fadeInDown" style="position:relative;top:22%;right: 8%">
        <form class="m-t" id="loginForm">
            <div class="form-group" style="text-align: right;padding-bottom: 2px;">
                <input class="input-background" style="  padding-left:17px;font-size: 18px;height: 65px;width: 429px;  color: rgba(255,255,255,0.9); "type="text" id="username" name="username" class="form-control" placeholder="用户名" required="" value="">
            </div>
            <div class="form-group" style="text-align: right;font-size: 14px;">
                <input  class="input-background" style=" padding-left:17px;font-size: 18px;height: 65px;width: 429px;color: rgba(255,255,255,0.9) " type="password" id="password" name="password" class="form-control" placeholder="密码" required="" value="">
            </div>
            <div class="form-group" style="text-align: right;">
                <div class="checkbox">
                    <span id="errorTips" class="pull-left" style="color:#a94442;">

                    </span>
                    <label class="i-checks" style="padding-right: 21px;">
                        <input type="checkbox" id="rememberMe" name="rememberMe"><span style="color:rgba(255,255,255,0.7);margin-left: 23px;font-size: 18px;">记住我</span>
                    </label>
                </div>
            </div>
        </form>
           <div style="  width:429px;">
           <button style="height:65px; font-size: 20px;"  type="submit" class="btn btn-success block full-width m-b" id="loginBtn" >登 &nbsp; 录</button>

           </div>
            </p>
    </div>
</div>


<!-- 全局js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/jquery.min.js?v=2.1.4"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/iCheck/icheck.min.js"></script>
<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/login.js"></script>
</body>
</html>
