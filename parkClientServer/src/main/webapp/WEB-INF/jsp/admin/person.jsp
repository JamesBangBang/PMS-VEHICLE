<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: fhh
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

    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.print.css" rel="stylesheet">
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="gray-bg">
    <div class="wrapper wrapper-content"><!--页面距离-->

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
                        <h5>人员管理</h5>
                        <div style="position: absolute;top:9px;right:290px;">
                            <select id="queryUserType" data-placeholder="选择人员类型..." class="chosen-select" tabindex="2">
                                <option value="">全部人员</option>
                                <option value="0">保安人员</option>
                                <option value="1">黑名单人员</option>
                                <option value="2">特定人员</option>
                            </select>
                        </div>
                        <div style="position: absolute;top:8px;right:20px;">
                            <button type="button" class="btn btn-primary " data-toggle="modal" data-target="#addModal">新增</button>
                        </div>


                        <div style="position: absolute;top:8px;right:200px;">
                            <a href="${pageContext.request.contextPath}/resources/excle/template.zip"  download=""  class="btn btn-primary ">模板下载</a>
                        </div>
                        <div style="position: absolute;top:8px;right:140px;">
                            <button type="button" class="btn btn-primary " data-toggle="modal" data-target="#ImportModal">导入</button>
                        </div>


                        <div style="position: absolute;top:8px;right:80px;">
                            <a href="${pageContext.request.contextPath}/person/exportClass" type="button" class="btn btn-primary " data-toggle="modal" >导出</a>
                        </div>



                    </div>
                    <div class="ibox-content full-height-scroll">
                        <table class="table table-striped table-bordered table-hover dataTables-example" id="personList">
                            <thead>
                            <tr>
                                <th>姓名</th>
                                <th>手机</th>
                                <th>MAC</th>
                                <th>类型</th>
                                <th>归属</th>
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
        <%----%>
        <div class="modal inmodal fade" id="ImportModal" tabindex="-1" role="dialog" aria-hidden="true">

            <div class="modal-dialog"><!--框框-->
                <div class="modal-content"><!--内容-->
                    <div class="modal-header">
                        <h4 class="modal-title">导入人员</h4>
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                            <!--×-->
                        </button>
                    </div>

                    <div class="modal-body">
                        <form id="formExaminee" method="post" enctype="multipart/form-data" target="upframe" action="${pageContext.request.contextPath}/person/uploadfile">
                            <div style="margin-bottom:10px">
                                <input id="file" type="file" name="file" data-options="buttonText:'选择文件',required:true" label="选择文件" style="width:100%">
                            </div>

                            <br/><br/>
                            <input type="submit" value="上传" />
                        </form>



                    <%--<div class="modal-footer">--%>
                        <%--<button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>--%>
                        <%--<button type="button" class="btn btn-primary" id="aBtn">提交</button>--%>
                    <%--</div>--%>
                </div>
                    <iframe id="upframe" name="upframe" src="" style="display:none;" onload="uploadResult()"></iframe>
            </div>
            </div>
        </div>











    <%----%>
        <div class="modal inmodal fade" id="addModal" tabindex="-1" role="dialog" aria-hidden="true">

            <div class="modal-dialog"><!--框框-->
                <div class="modal-content"><!--内容-->
                    <div class="modal-header">
                        <h4 class="modal-title">新增人员</h4>
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                            <!--×-->
                        </button>
                    </div>

                    <div class="modal-body">
                        <form class="form-horizontal m-t" id="addForm">
                            <input name="resourceId" id="orgId" type="hidden">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">归属机构：</label><!--移动-->
                                <div class="col-sm-8">
                                    <input name="orgName" id="orgName" type="text" class="form-control" readonly="" value="">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">姓名：</label>
                                <div class="col-sm-8">
                                    <input name="trueName" minlength="2" maxlength="20" type="text" class="form-control"
                                           required="" aria-required="true">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">手机号：</label>
                                <div class="col-sm-8">
                                    <input name="phone" maxlength="11" type="text" class="form-control"
                                           required="" aria-required="true">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">MAC：</label>
                                <div class="col-sm-8">
                                    <input name="mac" type="text" class="form-control"
                                           required="" aria-required="true">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">人员类型：</label>
                                <div class="col-sm-8">
                                    <div class="input-group" style="width: 100%">
                                        <select id="userType" name="userType" data-placeholder="选择人员类型..." class="chosen-select" tabindex="2">
                                            <option value="0">保安人员</option>
                                            <option value="1">黑名单人员</option>
                                            <option value="2">特定人员</option>
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
            <div class="modal-dialog"><!--框框-->
                <div class="modal-content"><!--内容-->
                    <div class="modal-header">
                        <h4 class="modal-title">人员信息修改</h4>
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                            <!--×-->
                        </button>
                    </div>

                    <div class="modal-body">
                        <form class="form-horizontal m-t" id="editForm">
                            <input type="hidden" name="id">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">归属机构：</label><!--移动-->
                                <div class="col-sm-8">
                                    <input name="orgName" type="text" class="form-control" readonly=""
                                           value="">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">姓名：</label>
                                <div class="col-sm-8">
                                    <input name="trueName" minlength="2" maxlength="20" type="text" class="form-control"
                                           required="" aria-required="true">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">手机号：</label>
                                <div class="col-sm-8">
                                    <input name="phone" minlength="2" maxlength="20" type="text" class="form-control"
                                           required="" aria-required="true">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">MAC：</label>
                                <div class="col-sm-8">
                                    <input name="mac" type="text" class="form-control"
                                           required="" aria-required="true">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">人员类型：</label>
                                <div class="col-sm-8">
                                    <div class="input-group" style="width: 100%">
                                        <select id="editUserType" name="userType" data-placeholder="选择人员类型..." class="chosen-select" tabindex="2">
                                            <option value="0">保安人员</option>
                                            <option value="1">黑名单人员</option>
                                            <option value="2">特定人员</option>
                                        </select>
                                    </div>
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

        <div class="modal inmodal fade" id="trajectoryModal" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog modal-lg"><!--框框-->
                <div class="modal-content" ><!--内容-->
                    <div class="modal-header">
                        <h4 class="modal-title">轨迹信息</h4>
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                            <!--×-->
                        </button>
                    </div>
                    <div class="modal-body" >
                        <div class="row">
                            <div class="col-xs-7">
                                 <input id="queryMac" type="hidden">
                                <div id="calendar"></div>
                            </div>
                            <div class="col-xs-5">
                                <div class="ibox-content timeline" id="timelineContainer">

                                </div>
                            </div>
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

    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/fullcalendar/fullcalendar.min.js"></script>

    <script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
    <script src="${pageContext.request.contextPath}/resources/script/admin/person.js"></script>
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
