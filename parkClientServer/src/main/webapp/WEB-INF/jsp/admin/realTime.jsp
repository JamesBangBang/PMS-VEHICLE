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
<div class=" wrapper-content"style="height:100%;min-width:828px;background-color: #FFFFFF;" >

    <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;">

        <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>

    <%--<div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12" style="background-color: #ffffff;padding: 0px;">--%>
            <%--<div style="float: left;width: 30%;;margin-right: 2%">--%>
                <%--<div class="input-group m-b">--%>
                    <%--<span class="input-group-addon">车牌号</span>--%>
                    <%--<input type="text" id="carName" placeholder="查询" class="form-control">--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div style="float: left;width: 30%;;margin-right: 2%">--%>
                <%--<div class="input-group m-b">--%>
                    <%--<span class="input-group-addon">车辆属性</span>--%>
                    <%--<input type="text" id="carType" placeholder="查询" class="form-control">--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div style="float: left;width: 30%;;margin-right: 2%">--%>
                <%--<div class="input-group m-b">--%>
                    <%--<span class="input-group-addon">车场名称</span>--%>
                    <%--<input type="text" id="carparkName" placeholder="查询" class="form-control">--%>
                <%--</div>--%>
            <%--</div>--%>

        <%--</div>--%>

        <%--<div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12" style="background-color: #ffffff;padding: 0px;">--%>

                <%--<div style="float: left;width: 30%;;margin-right: 2%">--%>
                    <%--<div class="input-group m-b">--%>
                        <%--<span class="input-group-addon">车道名称</span>--%>
                        <%--<input type="text" id="carRoadName" placeholder="查询" class="form-control">--%>
                    <%--</div>--%>
                <%--</div>--%>


                <%--<div style="float: left;width: 30%;;margin-right: 2%">--%>
                    <%--<div class="input-group m-b">--%>
                        <%--<span class="input-group-addon">进出场状态</span>--%>
                        <%--<select id="inoutFlag" data-placeholder="选择进出场状态" class="form-control"  >--%>
                            <%--<option value="">空</option>--%>
                            <%--<option value="0">进</option>--%>
                            <%--<option value="1">出</option>--%>
                        <%--</select>--%>
                    <%--</div>--%>
                <%--</div>--%>

                <%--<div style="float: left;width: 15%;;">--%>
                    <%--<div class="input-group m-b">--%>
                        <%--<span class="input-group-addon">进出场时间范围</span>--%>
                        <%--<input type="text" id="start" placeholder="年/月/日" class="form-control">--%>
                    <%--</div>--%>
                <%--</div>--%>

                <%--<div style="float: left;width: 15%;;margin-right: 2%">--%>
                    <%--<div class="input-group m-b">--%>
                        <%--&lt;%&ndash;<span class="input-group-addon">出场时间</span>&ndash;%&gt;--%>
                        <%--<input type="text" id="end"placeholder="年/月/日 " class="form-control">--%>
                    <%--</div>--%>
                <%--</div>--%>

            <%--</div>--%>

        <%--<div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12" style="background-color: #ffffff;padding: 0px;">--%>
            <%--<div   style="float:right;margin-right: 6%">--%>
                <%--<button type="button" class="btn btn-success" onclick="search()">查询</button>--%>
            <%--</div>--%>

        <%--</div>--%>



        <table class="table table-striped table-bordered " id="List" style="background-color:#ffffff ;width: 100%;">
            <thead>
            <tr style="color: #767676">
                <th>车牌号</th>
                <th>车牌颜色</th>
                <th>车辆类型</th>
                <th>进出类型</th>
                <th>车场名称</th>
                <th>车道名称</th>
                <th>进出场状态</th>
                <th>进出场时间</th>
                <th>岗亭名称</th>
                <th>操作员名称</th>
                <th>备注</th>
                <th>操作</th>
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
                                    <label>车场名称</label>
                                    <input class="form-control" name="carpark_name" readonly>
                                </div>
                                <div class="form-group">
                                    <label>岗亭名称</label>
                                    <input class="form-control" name="post_name" readonly>
                                </div>
                                <div class="form-group">
                                    <label>进出类型</label>
                                    <input class="form-control" name="inout_flag" readonly>
                                </div>
                                <div class="form-group">
                                    <label>进出场时间</label>
                                    <input class="form-control" name="inout_time" readonly>
                                </div>
                                <div class="form-group">
                                    <label>进出场状态</label>
                                    <input class="form-control" name="inout_status" readonly>
                                </div>
                            </div>
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>车牌颜色</label>
                                    <input class="form-control" name="car_no_color" readonly>
                                </div>
                                <div class="form-group">
                                    <label>车道名称</label>
                                    <input class="form-control" name="car_road_name" readonly>
                                </div>
                                <div class="form-group">
                                    <label>操作员名称</label>
                                    <input class="form-control" name="operator_name" readonly>
                                </div>
                                <div class="form-group">
                                    <label>车辆类型</label>
                                    <input class="form-control" name="car_type" readonly>
                                </div>

                                <div class="form-group">
                                    <label>备注</label>
                                    <input class="form-control" name="remark" readonly>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <img id="descImg" src="" style="width: 100%;">
                            </div>
                            <%--<div class="col-xs-3">--%>

                            <%--</div>--%>
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

<!-- layerDate plugin javascript时间表格 -->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/layer/laydate/laydate.js"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/dataTables.bootstrap.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/fullcalendar/fullcalendar.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/realTime.js"></script>

</body>
</html>
