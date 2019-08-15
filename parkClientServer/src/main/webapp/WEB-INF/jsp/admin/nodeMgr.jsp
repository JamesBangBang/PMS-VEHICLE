<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/chosen/chosen.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=n4dbtj9LyddAtuAgYbHQHGffpz1KRcct"></script>

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
                    <h5>位置管理</h5>
                    <div style="position: absolute;top:8px;right:20px;">
                        <button type="button" class="btn btn-primary " data-toggle="modal" data-target="#addModal">新增</button>
                    </div>
                </div>

                <div class="ibox-content full-height-scroll">
                    <table id="autoTable" class="table table-striped table-bordered table-hover dataTables-example">
                        <thead>
                        <tr>
                            <th>名称</th>
                            <th>地址</th>
                            <th>归属</th>
                            <th>经度</th>
                            <th>纬度</th>
                            <th>设备</th>
                            <th>容量设置</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>






    <div class="modal inmodal fade" id="addModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">新增位置节点</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal m-t" id="addForm">
                        <input name="orgId" id="orgId" type="hidden">
                        <div class="form-group">
                            <label class="col-sm-3 control-label">归属位置：</label>
                            <div class="col-sm-8">
                                <input name="orgName" id="orgName" type="text" class="form-control" readonly="" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">位置节点名称：</label>
                            <div class="col-sm-8">
                                <input name="orgNodeName" id="orgNodeName" maxlength="20" type="text" class="form-control" required="" aria-required="true">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">位置节点地址：</label>
                            <div class="col-sm-8">
                                <input name="orgNodeAddress" id="orgNodeAddress" maxlength="100" type="text" class="form-control" required="" aria-required="true">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">经度：</label>
                            <div class="col-sm-8">
                                <input name="nodeLongitude" id="nodeLongitude" type="text" class="form-control" value="">
                            </div>
                            <label class="col-sm-3 control-label">纬度：：</label>
                            <div class="col-sm-8">
                                <input name="nodeLatitude" id="nodeLatitude" type="text" class="form-control"  value="">
                            </div>

                            <div id="allmap" style="height: 200px;width: 100%;">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">容量设置：</label>
                            <div class="col-sm-8">
                                <input name="limitNumber" id="limitNumber" type="number" class="form-control" required="" aria-required="true">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">选择感知设备：</label>
                            <div class="col-sm-8">
                                <div class="input-group" style="width: 100%">
                                    <select id="devId" name="devId" data-placeholder="选择感知设备..." class="chosen-select" tabindex="2">
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">选择数字录像机：</label>
                            <div class="col-sm-8">
                                <div class="input-group" style="width: 100%">
                                    <select id="nvrSelect" name="nvrSelect" data-placeholder="选择数字录像机..." class="chosen-select" tabindex="2">
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">选择数字通道：</label>
                            <div class="col-sm-8">
                                <div class="input-group" style="width: 100%">
                                    <select id="channelSelect" name="channelId" data-placeholder="选择数字通道..." class="chosen-select" tabindex="2" >
                                    </select>
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

    <div class="modal inmodal fade" id="editModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">修改位置节点</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal m-t" id="editForm">
                        <input name="id" type="hidden">
                        <input name="orgId" type="hidden">
                        <div class="form-group">
                            <label class="col-sm-3 control-label">归属位置：</label>
                            <div class="col-sm-8">
                                <input name="orgName" type="text" class="form-control" readonly="" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">位置节点名称：</label>
                            <div class="col-sm-8">
                                <input name="orgNodeName" maxlength="20" type="text" class="form-control" required="" aria-required="true">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">位置节点地址：</label>
                            <div class="col-sm-8">
                                <input name="orgNodeAddress" maxlength="100" type="text" class="form-control" required="" aria-required="true">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">经度：</label>
                            <div class="col-sm-8">
                                <input name="nodeLongitude" type="number" class="form-control" required="" aria-required="true">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">纬度：</label>
                            <div class="col-sm-8">
                                <input name="nodeLatitude" type="number" class="form-control" required="" aria-required="true">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">容量设置：</label>
                            <div class="col-sm-8">
                                <input name="limitNumber" id="limitNumber1" type="number" class="form-control" required="" aria-required="true">
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-3 control-label">感知设备：</label>
                            <div class="col-sm-8">
                                <input name="devName" type="text" class="form-control" required="" aria-required="true" readonly>
                                <%--<div class="input-group" style="width: 100%">--%>
                                <%--<select id="editDevId" name="devId" data-placeholder="选择设备..." class="chosen-select" tabindex="2">--%>

                                <%--</select>--%>
                                <%--</div>--%>
                            </div>

                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">数字通道：</label>
                            <div class="col-sm-8">
                                <input name="channelName" type="text" class="form-control" required="" aria-required="true" readonly>
                            </div>

                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="editBtn">提交</button>
                </div>
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


<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/nodeMgr.js"></script>

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
