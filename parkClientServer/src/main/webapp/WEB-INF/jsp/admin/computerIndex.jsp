<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2017-12-28
  Time: 10:03
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
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/bootstrap-switch/css/bootstrap3/bootstrap-switch.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/jasny/jasny-bootstrap.min.css" rel="stylesheet">
    <title></title>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-xs-12">
                <button id="addcomputerBtn" class="btn btn-primary">新建岗亭</button>
                <button id="refreshBtn" class="btn btn-default">刷新表格</button>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <table class="table table-striped table-bordered table-hover dataTables-example" id="autoTable" style="background-color:#ffffff;width:100%;">
                    <thead>
                    <tr style="color: #767676">
                        <th>岗亭名称</th>
                        <th>岗亭IP</th>
                        <th>操作人</th>
                        <th>创建时间</th>
                        <th>岗亭状态</th>
                        <th>无人值守</th>
                        <th>语音播报</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>

        <div class="modal inmodal fade" id="editModal" tabindex="-1" role="dialog" aria-hidden="true">

            <div class="modal-dialog"><!--框框-->
                <div class="modal-content"><!--内容-->
                    <div class="modal-header">
                        <h4 class="modal-title">岗亭信息</h4>
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                            <!--×-->
                        </button>
                    </div>

                    <div class="modal-body">
                        <form id="editForm">
                            <input type="hidden" name="postComputerId" value="">
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="form-group">
                                        <label>岗亭名称</label>
                                        <input class="form-control" name="postComputerName">
                                    </div>
                                    <div class="form-group">
                                        <label>岗亭IP</label>
                                        <input type="text" class="form-control" name="postComputerIp">
                                    </div>
                                    <div class="form-group">
                                        <label>操作人</label>
                                        <input class="form-control" name="operationName" disabled>
                                    </div>
                                    <div class="form-group">
                                        <label>创建时间</label>
                                        <input id="addTime" name="addTime" class="form-control" disabled>
                                    </div>
                                    <div class="form-group">
                                        <label>状态</label>
                                        <input id="status" name="status" class="form-control" disabled>
                                    </div>
                                    <div class="form-group" >
                                        <label>无人值守</label>
                                        <input id="isAutoDeal" name="isAutoDeal" type="checkbox" data-on-color="primary" data-off-color="danger" data-size="mini"/>
                                    </div>
                                    <div class="form-group" style="visibility: hidden">
                                        <label>语音播报</label>
                                        <input id="isVoicePlay" name="isVoicePlay" type="checkbox" data-on-color="primary" data-off-color="danger" data-size="mini"/>
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

</body>

<!-- 全局js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/jquery.min.js?v=2.1.4"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- 自定义js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/content.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/jsTree/jstree.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/chosen/chosen.jquery.js"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/resources/bootstrap-switch/js/bootstrap-switch.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/jasny/jasny-bootstrap.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/computerIndex.js"></script>
</html>
