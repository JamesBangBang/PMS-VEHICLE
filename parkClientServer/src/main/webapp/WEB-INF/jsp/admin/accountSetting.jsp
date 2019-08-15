<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/9/15
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title></title>
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${pageContext.request.contextPath}/resources/admin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/jsTree/style.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.print.css" rel="stylesheet">
    <style>
        .form-control::-webkit-input-value{
            color: #000000;
        }
        label{
            font-size: 15px;
        }
    </style>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="tableCss1">
<div class=" wrapper-content"style="height:100%;background-color: #FFFFFF" >

    <div class="col-xs-12" style="border-top: solid 2px #EFEFEF;margin: 0px;">
            <div class="modal-body">
                <form class="form-horizontal m-t" id="userDataFrom">
                    <div class="row" style="line-height: 34px;padding-bottom: 5px;border-bottom: 1px solid #EFEFEF;margin-bottom: 5px;">
                        <div class="col-sm-2" >
                            <span><b>用户名</b></span>
                        </div>
                        <div class="col-sm-3">
                            <input name="userName" class="form-control"  value="${$_LoginUser.userName}" type="text" disabled>
                        </div>
                    </div>
                    <div class="row" style="line-height: 34px;padding-bottom: 5px;border-bottom: 1px solid #EFEFEF;margin-bottom: 5px;">
                        <div class="col-sm-2">
                            <label>密码</label>
                        </div>
                        <div class="col-sm-3">
                            <a id="changePwdBtn" class="btn btn-primary">修改密码</a>
                        </div>
                    </div>
                    <div class="row" style="line-height: 34px;padding-bottom: 5px;border-bottom: 1px solid #EFEFEF;margin-bottom: 5px;">
                        <div class="col-sm-2">
                            <label>真实姓名</label>
                        </div>
                        <div class="col-sm-3">
                            <input name="trueName" class="form-control" type="text"value="${$_LoginUser.trueName}">
                        </div>
                    </div>
                    <div class="row" style="line-height: 34px;padding-bottom: 5px;border-bottom: 1px solid #EFEFEF;margin-bottom: 5px;">
                        <div class="col-sm-2">
                            <label>邮箱</label>
                        </div>
                        <div class="col-sm-3">
                            <input name="email" class="form-control"type="text" value="${$_LoginUser.email}" >
                        </div>
                    </div>
                    <div class="row" style="line-height: 34px;padding-bottom: 5px;border-bottom: 1px solid #EFEFEF;margin-bottom: 5px;">
                        <div class="col-sm-2">
                            <label>手机号</label>
                        </div>
                        <div class="col-sm-3">
                            <input name="telephone" class="form-control" type="text"value="${$_LoginUser.telephone}"  >
                        </div>
                    </div>
                    <%--<div class="row" style="line-height: 34px;padding-bottom: 5px;border-bottom: 1px solid #EFEFEF;margin-bottom: 5px;">--%>
                        <%--<div class="col-sm-2">--%>
                            <%--<label>创建时间</label>--%>
                        <%--</div>--%>
                        <%--<div class="col-sm-3">--%>
                            <%--<input name="createTime" class="form-control"type="text"value="" >--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <div class="row" style="line-height: 34px;padding-bottom: 5px;border-bottom: 1px solid #EFEFEF;margin-bottom: 5px;">
                        <div class="col-sm-2">
                            <label>性别</label>
                        </div>
                        <div class="col-sm-3">
                            <%--<input name="sex" class="form-control"type="text"value="${$_LoginUser.sex}">--%>
                            <select class="chosen" name="sex">
                                <c:if test="${$_LoginUser.sex eq 'MALE'}">
                                    <option value="MALE" selected>男性</option>
                                    <option value="FEMALE">女性</option>
                                </c:if>
                                <c:if test="${$_LoginUser.sex eq 'FEMALE'}">
                                    <option value="FEMALE" selected>女性</option>
                                    <option value="MALE">男性</option>
                                </c:if>
                                <c:if test="${$_LoginUser.sex eq '' or $_LoginUser.sex eq null}">
                                    <option value="FEMALE">女性</option>
                                    <option value="MALE" >男性</option>
                                </c:if>
                            </select>
                        </div>
                    </div>
                    <div class="row" style="line-height: 34px;padding-bottom: 5px;border-bottom: 1px solid #EFEFEF;margin-bottom: 5px;">
                        <div class="col-sm-2">
                            <label>qq号</label>
                        </div>
                        <div class="col-sm-3">
                            <input name="qq" class="form-control"type="text"value="${$_LoginUser.qq}">
                        </div>
                    </div>
                </form>
                <div class="row">
                    <button class="btn btn-success" type="submit" id="userDataBtn" style="width: 100px;">提交</button>
                </div>
            </div>
      </div>

    <div class="modal inmodal fade" id="pwdModal" tabindex="-1" role="dialog" aria-hidden="true">

        <div class="modal-dialog modal-sm"><!--框框-->
            <div class="modal-content"><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title">修改密码</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>

                <div class="modal-body">
                    <form id="pwdForm">
                        <div class="form-group">
                            <label>旧密码</label>
                            <input id="oldPwd" type="password" name="oldPwd" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>新密码</label>
                            <input id="newPwd" type="password" name="newPwd" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>确认新密码</label>
                            <input id="checkPwd"  type="password" name="checkPwd" class="form-control">
                        </div>
                    </form>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" id="pwdSubmitBtn">提交</button>
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 全局js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/jquery.min.js?v=2.1.4"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- 自定义js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/content.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/jsTree/jstree.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/chosen/chosen.jquery.js"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/dataTables.bootstrap.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/fullcalendar/fullcalendar.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/parameterSetting.js"></script>

</body>
<script>
    $(".chosen").chosen({
        disable_search:true,
        width:"100%"
    });
    $("#userDataBtn").click(function(){
        var params = $("#userDataFrom").serialize();
        $.post(starnetContextPath + "/account/user/data",params,function(res){
            if(res.result == 0){
                successfulPrompt("信息修改成功","");
            }else{
                errorPrompt("信息修改失败",res.msg);
            }
        },'json');
    });
    $("#changePwdBtn").click(function () {
        $("#oldPwd").val('');
        $("#newPwd").val('');
        $("#checkPwd").val('');
        $("#pwdModal").modal("show");
    });

    $("#pwdSubmitBtn").click(function(){
        var oldPwd = $("#oldPwd").val();
        var newPwd = $("#newPwd").val();
        var checkPwd = $("#checkPwd").val();

        if(strIsEmpty(oldPwd)){
            warningPrompt("请输入旧密码");
            return;
        }
        if(strIsEmpty(newPwd)){
            warningPrompt("请输入新密码");
            return;
        }
        if(strIsEmpty(checkPwd)){
            warningPrompt("请确认新密码");
            return;
        }
        if(newPwd != checkPwd){
            warningPrompt("两次输入的密码不同");
            return;
        }
        var params = {
            newPwd:newPwd,
            oldPwd:oldPwd,
            checkPwd:checkPwd
        }
        $.post(starnetContextPath + "/account/user/pwd",params,function(res){
            if(res.result == 0){
                successfulPrompt("密码修改成功","");
                $("#pwdModal").hide();
            }else{
                errorPrompt("密码修改失败",res.msg);
            }
        },'json');


    });
</script>
</html>
