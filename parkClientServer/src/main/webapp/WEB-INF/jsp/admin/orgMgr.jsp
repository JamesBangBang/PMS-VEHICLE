<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2017-06-15
  Time: 16:17
  To change this template use File | Settings | File Templates.
--%>
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
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/switchery/switchery.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">


</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-xs-3">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>组织机构树</h5>
                </div>
                <div class="ibox-content full-height-scroll">
                    <div id="orgTree"></div>
                </div>
            </div>
        </div>
        <div class="col-xs-9">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>机构管理</h5>

                            <%--<div style="position: absolute;top:8px;right:20px;">--%>
                                <%--<button type="button" class="btn btn-primary " data-toggle="modal" data-target="#addModal">新增</button>--%>
                            <%--</div>--%>


                            <%--<div style="position: absolute;top:8px;right:200px;">--%>
                                <%--<a href="/resources/excle/template.zip" download="" class="btn btn-primary ">模板下载</a>--%>
                            <%--</div>--%>
                            <%--<div style="position: absolute;top:8px;right:140px;">--%>
                                <%--<button type="button" class="btn btn-primary " data-toggle="modal" data-target="#ImportModal">导入</button>--%>
                            <%--</div>--%>


                            <%--<div style="position: absolute;top:8px;right:80px;">--%>
                                <%--<a href="/person/exportClass" type="button" class="btn btn-primary " data-toggle="modal">导出</a>--%>
                            <%--</div>--%>

                </div>
                <div class="ibox-content full-height-scroll">
                    <div class="row" style="position: absolute;top: -40px;">
                        <div class="col-xs-5" style="position: relative;right: -27%;">
                            <div class="input-group" style="width: 300%">
                                <input type="text" class="form-control" id="test_data">
                                <div class="input-group-btn" style="width: 0px;">
                                         <span class="input-group-btn">

                                    </span>
                                    <ul class="dropdown-menu dropdown-menu-right" role="menu " style="margin-top: 34px;">
                                    </ul>
                                </div>
                                <!-- /btn-group -->
                            </div>
                        </div>

                        <div class="col-xs-1" style="position: relative;right: -90.8%;">
                            <button type="button" class="btn btn-success" id="test_select">搜索</button>

                        </div>
                        <div class="col-xs-6" style="position: relative;right: -171%;">

                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addModal"style="margin-left: 40px;">新增</button>
                            <button type="button" class="btn btn-danger" id="delBtn" >删除</button>
                        </div>
                    </div>
                        <div class="row">
                            <div class="col-xs-5">
                                <form class="form-horizontal m-t" id="editForm">
                                    <input type="hidden" name="id" id="editId">
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">上级：</label>
                                        <div class="col-sm-8">
                                            <input id="higherLevel" name="higherLevel" type="text" class="form-control" readonly="" value="">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">组织机构名称：</label>
                                        <div class="col-sm-8">
                                            <input id="orgName" name="name" minlength="2" maxlength="20" type="text" class="form-control" required="" aria-required="true">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">优先级：</label>
                                        <div class="col-sm-8">
                                            <input id="priority" type="number" class="form-control" name="priority" required="" aria-required="true">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">容量设置：</label>
                                        <div class="col-sm-8">
                                            <input id="limitNumber" type="number" class="form-control" name="limitNumber" required="" aria-required="true">
                                        </div>
                                    </div>


                                </form>
                                <div class="form-group">
                                    <div class="col-sm-4 col-sm-offset-3">
                                        <button class="btn btn-primary" type="submit" id="editBtn">提交</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal inmodal fade" id="addModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">新增组织结构</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal m-t" id="addForm">
                        <input name="parentId" id="parentId" type="hidden">
                        <div class="form-group">
                            <label class="col-sm-3 control-label">上级：</label>
                            <div class="col-sm-8">
                                <input name="higherLevel" id="addHigherLevel" type="text" class="form-control" readonly="" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">组织机构名称：</label>
                            <div class="col-sm-8">
                                <input name="name" minlength="2" maxlength="20" type="text" class="form-control" required="" aria-required="true">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">优先级：</label>
                            <div class="col-sm-8">
                                <input type="number" class="form-control" name="priority" required="" aria-required="true">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">容量设置：</label>
                            <div class="col-sm-8">
                                <input id="limitNumber1" type="number" class="form-control" name="limitNumber1" required="" aria-required="true">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">根节点：</label>
                            <div class="col-sm-8">
                                <div class="switch" style="padding-top:5px;">
                                    <div class="onoffswitch">
                                        <input type="checkbox" class="onoffswitch-checkbox" name="isRoot" id="isRoot">
                                        <label class="onoffswitch-label" for="isRoot">
                                            <span class="onoffswitch-inner"></span>
                                            <span class="onoffswitch-switch"></span>
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="addBtn">提交</button>
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

<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>
<!-- Switchery -->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/switchery/switchery.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/suggest/bootstrap-suggest.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/orgMgr.js"></script>

<style>
    .jstree-open > .jstree-anchor > .fa-folder:before {
        content: "\f07c";
    }

    .jstree-default .jstree-icon.none {
        width: 0;
    }
</style>
</body>
</html>
