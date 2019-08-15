<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/9/15
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<style>
    body{
        moz-user-select: -moz-none;
        -moz-user-select: none;
        -o-user-select:none;
        -khtml-user-select:none;
        -webkit-user-select:none;
        -ms-user-select:none;
        user-select:none;
    }
    .tabTopRow{
        width:100%;
        height:43px;
        line-height:43px;
        border-bottom:1px solid #CCCCCC;
    }
    .tabBottomRow{
        width:741px;
        height:48px;
        line-height:48px;
        border-left:1px solid #CCCCCC;
        border-bottom:1px solid #CCCCCC;
        border-right:1px solid #CCCCCC;
    }
    .tabBottomRow:hover{
        background-color:RGB(200,239,255);
        cursor:hand;
        cursor: pointer;
    }
    .tabLeftCol{
        width:111px;
        float:left;
        height:100%;
        text-align:center;
        border-left:1px solid #CCCCCC;
        border-right:1px solid #CCCCCC;
    }
    .tabCol{
        float:left;
        width:144px;
        height:100%;
        text-align:center;
        border-right:1px solid #CCCCCC;
    }
    .tabCol4{
        float:left;
        width:194px;
        height:100%;
        text-align:center;
        border-right:1px solid #CCCCCC;
    }
    .secRow{
        height: 40px;
        float: left;
    }

    .secRow:hover{
        background-color:RGB(200,239,255);
        cursor:hand;
        cursor: pointer;
    }

    .tabRow{
        position:relative;
        width:100%;
        height:40px;
        line-height:40px;
        border-bottom:1px solid #CCCCCC;
    }
    .secCol{
        float:left;
        height:40px;
        width:144px;
        line-height:40px;
        border-right:1px solid #CCCCCC;
        text-align:center;
        font-size: 12px !important;
    }
    .secCol4{
        float:left;
        height:40px;
        width:194px;
        line-height:40px;
        border-right:1px solid #CCCCCC;
        text-align:center;
        font-size: 12px !important;
    }
    .tableDiv{
        width:743px;
        border-top:1px solid #CCCCCC;
    }
    .describe{
        padding-left:40px;
    }
    .container{
        width:743px;
        padding:8px;
        border-radius:4px;
        box-shadow: 0px 0px 3px 3px #CCCCCC;
        margin-left:10px;
        margin-top:40px;
    }
    body, ul, li, h1, h2, h3, h4, h5, h6, p {
        font-size: 14px;
        font-weight: normal;
        margin: 0px;
        padding: 0px;
        font-family: "微软雅黑";
    }
    ul { list-style: none; }
    img { border-style: none; }
</style>
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

</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="tableCss1">
<div class=" wrapper-content"style="height:100%;background-color: #FFFFFF" >
    <div class="col-xs-12">
        <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>
        <select id="selectRole" onchange="search(event)" class="form-control" style=" display: inline-block;width: 200px;position: relative;left: 65%;background-color: #fff;color: #000;border-color: rgba(0,151,255,0.6);">
            <option value="1">平台用户</option>
            <option value="0">未配置用户</option>
        </select>
        <button class="btn btn-success pull-right" type="button" id="addUserBtn">
            <i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;<span class="bold">创建用户</span>
        </button>
    </div>
    <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;">
        <table class="table table-striped table-bordered table-hover dataTables-example" id="autoTable" style="background-color:#ffffff;width:100%;">
            <thead>
            <tr style="color: #767676">
                <th>用户名</th>
                <th>真实姓名</th>
                <th>邮箱</th>
                <th>手机号</th>
                <th>性别</th>
                <th>qq号</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
    <div class="modal inmodal fade" id="editModal" tabindex="-1" role="dialog" aria-hidden="true">

        <div class="modal-dialog modal-lg"><!--框框-->
            <div class="modal-content"><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title">用户信息</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>

                <div class="modal-body">
                    <form id="userForm">
                        <input type="hidden" name="id" value="">
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label>用户名</label>
                                    <input name="userName" class="form-control" disabled id="userName">
                                </div>
                                <div class="form-group">
                                    <label>真实姓名</label>
                                    <input name="trueName" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label>邮箱</label>
                                    <input name="email" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label>手机号</label>
                                    <input name="telephone" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label>性别</label>
                                    <select name="sex" class="chosen" id="sex">
                                        <option value="MALE">男</option>
                                        <option value="FEMALE">女</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>qq号</label>
                                    <input name="qq" class="form-control">
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="form-group noRoot">
                                    <label>角色</label>
                                    <select name="roleItems" id="roleItems"></select>
                                </div>
                                <div class="form-group" style="display: none">
                                    <label>单位</label>
                                    <select class="" name="departmentItems" id="departmentItems" multiple="multiple"></select>
                                </div>
                                <div class="form-group noRoot" >
                                    <label>车场</label>
                                    <select class="chosen" name="parkItems" id="parkItems" multiple="multiple"></select>
                                </div>
                            </div>
                        </div>


                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" id="submitBtn">提交</button>
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
<script src="${pageContext.request.contextPath}/resources/script/admin/userManage.js"></script>

</body>
</html>
