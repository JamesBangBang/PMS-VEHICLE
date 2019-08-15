
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title></title>
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

    <div class="col-xs-12 " style="padding-top:0px;border-bottom: solid 1px #EFEFEF">
            <div class="col-sm-3" hidden="true">
                <div class="input-group m-b">
                    <span class="input-group-addon input-group-background">有效开始时间最小值</span>
                    <input placeholder="最小值" class="form-control layer-date" id="startMin" readonly>
                </div>
            </div>

            <div class="col-sm-3" hidden="true">
                <div class="input-group m-b">
                    <span class="input-group-addon input-group-background">有效开始时间最大值</span>
                    <input placeholder="最大值" class="form-control layer-date" id="startMax" readonly>
                </div>
            </div>

            <div class="col-sm-3" hidden="true">
                <div class="input-group m-b">
                    <span class="input-group-addon input-group-background">有效结束时间最小值</span>
                    <input placeholder="最小值" class="form-control layer-date"  style="border-left: none" id="endMin" readonly>
                </div>
            </div>

            <div class="col-sm-3" >
                <div class="input-group m-b">
                    <span class="input-group-addon input-group-background">车牌号</span>
                    <input type="text" class="form-control" name="carNo" id="carNoSearch" value="">
                </div>
            </div>

            <div class="col-sm-3">
                <div class="input-group m-b">
                    <span class="input-group-addon input-group-background">车主姓名</span>
                    <input type="text" class="form-control" name="driverName" id="driverNameSearch" value="">
                </div>
            </div>

            <div class="col-sm-3">
                <div class="input-group m-b">
                    <span class="input-group-addon input-group-background">车场选择</span>
                    <select class="form-control m-b" data-placeholder="选择车场..." name="carparkId" id="carparkIdSearch">
                    </select>
                </div>
            </div>

            <div class="col-sm-3" >
                <div class="input-group m-b">
                    <span class="input-group-addon input-group-background">截止时间</span>
                    <input placeholder="截止时间" class="form-control layer-date"  style="border-left: none" id="endMax" readonly>
                </div>
            </div>


            <div class="col-xs-1 col-xs-1 col-sm-1 col-md-1 col-lg-1 ">
                <a href="javascript:void(0)" type="button" class="btn btn-success btn-success2" onclick="searchChargeInfo()"><i class="fa fa-search"></i>&nbsp;&nbsp;搜索</a>
            </div>

            <div class="col-xs-1 col-xs-1 col-sm-1 col-md-1 col-lg-1 ">
                <a href="javascript:void(0)" type="button" class="btn btn-success btn-success2" onclick="addRoleFormSet()" id="addBlacklist"></i>&nbsp;&nbsp;新增</a>
            </div>
            <div class="col-xs-1 col-xs-1 col-sm-1 col-md-1 col-lg-1 ">
                <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>
            </div>

        </div>

    <div class="col-xs-12" style="margin-top: 23px;background-color:#ffffff;">
        <table class="table table-striped table-bordered table-hover dataTables-example" id="blacklist" style="background-color:#ffffff;margin-top: 20px">
            <thead>
            <tr >
                <th>车牌号</th>
                <th>黑名单类型</th>
                <th>车主姓名</th>
                <th>车主电话</th>
                <th>车主信息</th>
                <th>车场名称</th>
                <th>有效开始时间</th>
                <th>有效结束时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>

    <div class="modal inmodal fade" id="blacklistModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog"><!--框框-->
        <div class="modal-content"><!--内容-->
            <div class="modal-header">
                <h4 class="modal-title" id="title">新增黑名单</h4>
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                    <!--×-->
                </button>
            </div>

            <div class="modal-body">
                <form class="form-horizontal m-t" id="addBlacklistForm">
                    <input name="depId" id="depId" type="hidden">
                    <input name="driverFileId" id="driverFileId" type="hidden">
                    <input name="memberWalletId" id="memberWalletId" type="hidden">
                    <input name="controlMode" id="controlMode" type="hidden">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">车牌号：</label><!--移动-->
                        <div class="col-sm-8">
                            <input name="carNo" id="carNo" type="text" class="form-control" value="">
                        </div>
                    </div>
                    <%--<div class="form-group">
                        <label class="col-sm-3 control-label">黑名单类型：</label>
                        <div class="col-sm-8">
                            <div class="input-group" style="width: 100%">
                                <select id="blacklistTypeName" name="blacklistTypeName" data-placeholder="选择黑名单类型..." class="form-control" tabindex="2">
                                    <option value="0" selected="selected">挂失</option>
                                    <option value="1">通缉</option>
                                    <option value="2">其他</option>
                                </select>
                            </div>
                        </div>
                    </div>--%>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">车主姓名：</label>
                        <div class="col-sm-8">
                            <input id="driverName" name="driverName" minlength="2" maxlength="20" type="text" class="form-control"
                                   required="" aria-required="true">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">车主电话：</label>
                        <div class="col-sm-8">
                            <input name="driverTelephoneNumber" id="driverTelephoneNumber" maxlength="11" type="text" class="form-control"
                                   required="" aria-required="true">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">车主信息：</label>
                        <div class="col-sm-8">
                            <input name="driverInfo" id="driverInfo" type="text" class="form-control">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">车场选择：</label>
                        <div class="col-sm-8">
                            <select id="carparkId" name="carparkId" data-placeholder="选择车场..." class="form-control" tabindex="2">
                            </select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-3 control-label">有效开始时间：</label>
                        <div class="col-sm-8">
                            <input name="effectiveStartTime" placeholder="有效开始时间" class="form-control layer-date" id="effectiveStartTime" required="" aria-required="true" readonly>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">有效结束时间：</label>
                        <div class="col-sm-8">
                            <input name="effectiveEndTime" placeholder="有效结束时间" class="form-control layer-date" id="effectiveEndTime" aria-required="true" readonly>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">返回</button>
                <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
            </div>
        </div>
    </div>
</div>

    <div class="modal " id="deleteModal" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content animated fadeIn">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">确认删除</h4>
                <small class="font-bold"></small>
            </div>
            <div class="modal-body">
                <p><strong>是否确认删除该黑名单人员信息?</strong></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                <button type="button"  id="deleteBtn"  class="btn btn-primary">确认删除</button>
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
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/layer/laydate/laydate.js"></script>

<script src="${pageContext.request.contextPath}/resources/script/admin/blacklist.js"></script>

</body>
</html>
