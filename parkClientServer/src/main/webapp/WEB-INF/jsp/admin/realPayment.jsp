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
<div class=" wrapper-content"style="height:100%;background-color: #FFFFFF;min-width:828px;" >

    <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;">

        <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>

        <table class="table table-striped table-bordered table-hover dataTables-example" id="paymentList" style="background-color:#ffffff ">
            <thead>
            <tr style="color: #767676">
                <th>车牌号</th>
                <th>车场名称</th>
                <th>入场时间</th>
                <th>入场车道</th>
                <th>出场时间</th>
                <th>出场车道</th>
                <th>停车时长</th>
                <th>应收金额</th>
                <th>实收金额</th>
                <th>提前缴费</th>
                <th>免费金额</th>
                <th>放行方式</th>
                <th>放行原因</th>
                <th>车辆属性</th>
                <th>收费岗亭</th>
                <th>收费时间</th>
                <th>详情</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>

    </div>

    <div class="modal inmodal fade" id="descModal" tabindex="-1" role="dialog" aria-hidden="true">

        <div class="modal-dialog modal-lg"><!--框框-->
            <div class="modal-content"><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title">详情</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>
                <div class="modal-body">
                    <form id="descForm">
                        <div class="row">
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>车牌号</label>
                                    <input class="form-control" name="car_no" readonly>
                                </div>

                                <div class="form-group">
                                    <label>入场时间</label>
                                    <input class="form-control" name="in_time" readonly>
                                </div>
                                <div class="form-group">
                                    <label>入场车道</label>
                                    <input class="form-control" name="in_car_road_name" readonly>
                                </div>
                                <div class="form-group">
                                    <label>停车时长</label>
                                    <input class="form-control" name="stay_time" readonly>
                                </div>
                            </div>
                            <div class="col-xs-3">

                                <div class="form-group">
                                    <label>应收金额</label>
                                    <input class="form-control" name="charge_receivable_amount" readonly>
                                </div>
                                <div class="form-group">
                                    <label>实收金额</label>
                                    <input class="form-control" name="charge_actual_amount" readonly>
                                </div>
                                <div class="form-group">
                                    <label>提前缴费</label>
                                    <input class="form-control" name="charge_pre_amount" readonly>
                                </div>

                                <div class="form-group">
                                    <label>免费金额</label>
                                    <input class="form-control" name="charge_free_amount" readonly>
                                </div>


                            </div>
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>出场时间</label>
                                    <input class="form-control" name="out_time" readonly>
                                </div>
                                <div class="form-group">
                                    <label>出场车道</label>
                                    <input class="form-control" name="out_car_road_name" readonly>
                                </div>
                                <div class="form-group">
                                    <label>放行方式</label>
                                    <input class="form-control" name="release_type" readonly>
                                </div>
                                <div class="form-group">
                                    <label>放行原因</label>
                                    <input class="form-control" name="release_reason" readonly>
                                </div>
                            </div>
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>车辆属性</label>
                                    <input class="form-control" name="car_type" readonly>
                                </div>
                                <div class="form-group">
                                    <label>收费岗亭</label>
                                    <input class="form-control" name="charge_post_name" readonly>
                                </div>
                                <div class="form-group">
                                    <label>收费时间</label>
                                    <input class="form-control" name="update_time" readonly>
                                </div>
                                <div class="form-group">
                                    <label>车场名称</label>
                                    <input class="form-control" name="carpark_name" readonly>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-6">
                                <label>入场图片</label>
                                <div>
                                    <img id="inImg" src="" style="width: 100%;">
                                </div>

                            </div>
                            <div class="col-xs-6">
                                <label>出场图片</label>
                                <div>
                                    <img id="outImg" src="" style="width: 100%;">
                                </div>

                            </div>
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
<%--<!-- layerDate plugin javascript时间表格 -->--%>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/layer/laydate/laydate.js"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/dataTables.bootstrap.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/fullcalendar/fullcalendar.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/realPayment.js"></script>

</body>
</html>
