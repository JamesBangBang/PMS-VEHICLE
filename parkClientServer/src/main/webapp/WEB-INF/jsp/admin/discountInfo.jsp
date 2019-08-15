<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: JAMESBANG
  Date: 2018/3/20
  Time: 10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <style>body,th{font-size:13px !important}</style>
    <style>body,td{font-size:13px !important}</style>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body>
<div class=" wrapper-content"style="height:100%;background-color: #FFFFFF;padding:5px" >
    <div class="row">
        <div class="col-xs-12">
            <button class="btn btn-primary" type="button" onclick="addFunction()" id="createDiscountInfo">创建优惠方式</button>
            <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;">
            <table class="table table-striped table-bordered table-hover dataTables-example" id="autoTable" style="background-color:#ffffff;">
                <thead>
                <tr style="color: #767676">
                    <th>优惠名称</th>
                    <th>优惠类型</th>
                    <th>优惠信息</th>
                    <th>商家名称</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>

    <div class="modal inmodal fade" id="descModal" tabindex="-1" role="dialog" aria-hidden="true">

        <div class="modal-dialog modal-lg"><!--框框-->
            <div class="modal-content"><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title">优惠详情</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>

                <div class="modal-body">
                    <form id="discountForm">
                        <input name="carparkId" type="hidden" value="">
                        <div class="row">
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>优惠名称</label>
                                    <input name="editDiscountName" class="form-control" value="" id="editDiscountName">
                                </div>
                            </div>

                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>优惠类型</label>
                                    <select class="form-control m-b" name="editDiscountType" id="editDiscountType" onchange="onDiscountTypeChange()" disabled>
                                        <option value="edtTimeFree">时间优惠</option>
                                        <option value="edtAmountFree">金额优惠</option>
                                        <option value="edtDiscountFree">折扣优惠</option>
                                        <option value="editTotalFree">完全免费</option>
                                    </select>
                                </div>
                            </div>

                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>优惠信息</label>
                                    <input name="editDiscountInfo" class="form-control" value="" id="editDiscountInfo" disabled>
                                </div>
                            </div>

                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>商家名称</label>
                                    <input name="edtDiscountCompanyName" class="form-control" value="" id="edtDiscountCompanyName">
                                </div>
                            </div>


                        </div>

                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-white" onclick="changeFunction()" id="addForm">提交</button>
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal inmodal fade" id="addDiscountModal" tabindex="-1" role="dialog" aria-hidden="true">

        <div class="modal-dialog modal-lg"><!--框框-->
            <div class="modal-content"><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title">添加优惠信息</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>

                <div class="modal-body" >
                    <form id="addParkForm">
                        <input name="carparkId" type="hidden" value="">
                        <div class="row">
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>优惠名称</label>
                                    <input name="addDiscountName" class="form-control" value="" id="addDiscountName">
                                </div>

                            </div>

                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>优惠类型</label>
                                    <select class="form-control m-b" name="addDiscountType" id="addDiscountType" onchange="onDiscountTypeChange()">
                                        <option value="timeFree">时间优惠</option>
                                        <option value="amountFree">金额优惠</option>
                                        <option value="discountFree">折扣优惠</option>
                                        <option value="totalFree">完全免费</option>
                                    </select>
                                </div>
                            </div>

                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label id="addDiscountTypeLabel">优惠时间（单位：分钟）</label>
                                    <input name="addDiscountInfo" class="form-control"  id="addDiscountInfo" >
                                </div>
                            </div>

                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>商家名称</label>
                                    <input name="addDiscountInfo" class="form-control"  id="addCompanyName" >
                                </div>
                            </div>


                        </div>

                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-white" onclick="saveDiscountInfo()">保存</button>
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal inmodal" id="deleteModal" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content animated fadein">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title">确认删除</h4>
                    <small class="font-bold"></small>
                </div>
                <div class="modal-body">
                    <p><strong>此条优惠规则将被删除，请谨慎操作</strong></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                    <button type="button"  id="deleteBtn" onclick="deleteDiscountInfo()" class="btn btn-primary">确认删除</button>
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
    <script src="${pageContext.request.contextPath}/resources/script/admin/discountInfo.js"></script>
</body>
</html>
