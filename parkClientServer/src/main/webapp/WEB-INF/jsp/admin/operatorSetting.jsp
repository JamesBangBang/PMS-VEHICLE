
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
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="tableCss1">
<div class=" wrapper-content"style="height:100%;background-color: #FFFFFF" >
    <div class="col-xs-12">
        <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>
        <button class="btn btn-success pull-right" type="button" id="addOperator">
            <i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;<span class="bold">创建操作人员</span>
        </button>
    </div>

    <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;">
        <table class="table table-striped table-bordered table-hover dataTables-example" id="operatorTable" style="background-color:#ffffff ">
            <thead>
            <tr style="color: #767676">
                <th>用户名</th>
                <th>操作人员姓名</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
</div>

<div class="modal inmodal fade in" id="addOperatorModal" tabindex="-1" role="dialog" aria-hidden="true">

    <div class="modal-dialog modal-sm"><!--框框-->
        <div class="modal-content"><!--内容-->
            <div class="modal-header">
                <h4 class="modal-title">添加操作人员</h4>
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span>
                    <!--×-->
                </button>
            </div>

            <div class="modal-body">
                <form id="adOperatorForm">
                    <div class="form-group">
                        <label>用户名</label>
                        <input id="loginName" type="text" name="loginName" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>操作人员姓名</label>
                        <input id="operatorName" type="text" name="operatorName" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>设置密码</label>
                        <input id="pwd" type="password" name="pwd" class="form-control">
                        <%--<span id="passstrength" class="form-control"></span>--%>
                    </div>
                    <div class="form-group">
                        <label>确认密码</label>
                        <input id="checkPwd" type="password" name="checkPwd" class="form-control">
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="opeSubmitBtn">提交</button>
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<div class="modal inmodal" id="deleteOperatorModal" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content animated fadeIn">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">确认删除</h4>
                <small class="font-bold"></small>
            </div>
            <div class="modal-body">
                <p><strong>确定删除，该操作人员?</strong></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                <button type="button"  id="deleteOperatorBtn"  class="btn btn-primary">确认删除</button>
            </div>
        </div>
    </div>
</div>

<div class="modal inmodal fade in" id="editOperatorModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-sm"><!--框框-->
        <div class="modal-content"><!--内容-->
            <div class="modal-header">
                <h4 class="modal-title">操作人员编辑</h4>
                <%--<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span>--%>
                    <%--<!--×-->--%>
                <%--</button>--%>
            </div>

            <div class="modal-body">
                <form id="editOperatorForm">
                    <div class="form-group">
                        <label>用户名</label>
                        <input id="editLoginName" type="text" name="editLoginName" class="form-control" disabled>
                    </div>
                    <div class="form-group">
                        <label>操作人员姓名</label>
                        <input id="editOperatorName" type="text" name="editOperatorName" class="form-control">
                    </div>
                    <div class="form-group">
                        <a id="modifyPwd" class="btn btn-primary">设置新密码</a>
                    </div>
                    <div class="form-group pwd">
                        <label style="width: 100%">旧密码    <a id="close" class="pull-right"><i class="fa fa-close" style="color: #cccccc;font-size: 12px"></i></a> </label>
                        <input id="oldPwd" type="password" name="oldPwd" class="form-control">
                    </div>
                    <div class="form-group pwd">
                        <label>新密码</label>
                        <input id="newPwd" type="password" name="newPwd" class="form-control">
                    </div>
                    <div class="form-group pwd">
                        <label>确认新密码</label>
                        <input id="checkNewPwd" type="password" name="checkNewPwd" class="form-control">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="editSubmitBtn">提交</button>
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<%--<div class="modal inmodal fade in" id="pwdModal" tabindex="-1" role="dialog" aria-hidden="true" >--%>
    <%--<div class="modal-dialog modal-sm"><!--框框-->--%>
        <%--<div class="modal-content"><!--内容-->--%>
            <%--<div class="modal-header">--%>
                <%--<h4 class="modal-title">修改密码</h4>--%>
                <%--<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span>--%>
                    <%--<!--×-->--%>
                <%--</button>--%>
            <%--</div>--%>

            <%--<div class="modal-body">--%>
                <%--<form id="pwdForm">--%>
                    <%--<div class="form-group">--%>
                        <%--<label>旧密码</label>--%>
                        <%--<input id="oldPwd" type="password" name="oldPwd" class="form-control">--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                        <%--<label>新密码</label>--%>
                        <%--<input id="newPwd" type="password" name="newPwd" class="form-control">--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                        <%--<label>确认新密码</label>--%>
                        <%--<input id="checkNewPwd" type="password" name="checkNewPwd" class="form-control">--%>
                    <%--</div>--%>
                <%--</form>--%>

            <%--</div>--%>
            <%--<div class="modal-footer">--%>
                <%--<button type="button" class="btn btn-primary" id="pwdSubmitBtn">提交</button>--%>
                <%--<button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>

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
<script src="${pageContext.request.contextPath}/resources/script/admin/operatorSetting.js"></script>

</body>
</html>
