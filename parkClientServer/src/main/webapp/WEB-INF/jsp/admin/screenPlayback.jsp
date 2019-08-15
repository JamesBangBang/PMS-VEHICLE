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
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/switchery/switchery.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">

</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="gray-bg">
<div class="wrapper wrapper-content ">
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
                    <h5>视频回放信息筛选</h5>
                    <div style="position: absolute;top: 10px;right: 20px;">
                        <button type="button" class="btn btn-primary" id="searchBtn">查询</button>
                    </div>
                </div>
                <div class="ibox-content">
                    <form class="form-horizontal m-t" id="addForm">
                        <input name="orgId" id="orgId" type="hidden">
                        <div class="row">
                            <div class="col-xs-12" >
                                <div class="form-group">
                                    <label class="col-sm-2 control-label" >时间筛选：</label>
                                    <div class="col-sm-10">
                                        <input placeholder="开始日期" class="form-control layer-date" id="start">
                                      <input placeholder="结束日期" class="form-control layer-date" id="end">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12" >
                                <div class="form-group">
                                    <label class="col-xs-2 control-label">位置节点：</label>
                                    <div class="col-xs-10">

                                        <div class="input-group"  style="width: 240px;">
                                            <select id="devId" name="devId" data-placeholder="选择位置节点..." class="chosen-select" tabindex="2" >
                                            </select>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                        <div class="row">
                            <object
                                    id="SNMSCloudOcx"
                                    classid="clsid:DEEE91FD-B7EE-451A-BF54-F7A289BC422D"
                                    width=100%
                                    height=450
                                    align=top
                                    hspace=0
                                    vspace=0
                            ></object>

                        </div>
                    </form>


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

<!-- layerDate plugin javascript时间表格 -->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/layer/laydate/laydate.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/CSSP_OCX.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/screePlayBack.js"></script>

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
