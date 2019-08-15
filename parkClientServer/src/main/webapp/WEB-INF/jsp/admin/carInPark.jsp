<%--
  Created by IntelliJ IDEA.
  User: JAMESBANG
  Date: 2019/6/27
  Time: 15:24
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
    <div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12" style="margin-top: 0px;background-color:#ffffff;">

        <div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12" style="background-color: #ffffff;padding: 0px;">
            <div style="float: left;width: 23%;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">车牌号</span>
                    <input type="text" id="carNo" placeholder="模糊查询" class="form-control">
                </div>
            </div>
            <div style="float: left;width: 23%;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">车辆属性</span>
                    <select id="carNoAttribute" data-placeholder="选择车辆属性" class="form-control"  >
                        <option value="">空</option>
                        <option value="临时车">临时车</option>
                        <option value="包月车">包月车</option>
                        <option value="免费车">免费车</option>
                        <option value="包次车">包次车</option>
                        <option value="黑名单">黑名单</option>
                        <option value="预约车">预约车</option>
                    </select>
                </div>
            </div>
            <div style="float: left;width: 23%;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">车场名称</span>
                    <input type="text" id="carparkName" placeholder="模糊查询" class="form-control">
                </div>
            </div>
            <div style="float: left;width: 23%;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">入场车道</span>
                    <select id="inCarRoadName" data-placeholder="选择车辆属性" class="form-control"  >
                    </select>
                    <%--<input type="text" id="inCarRoadName" placeholder="查询" class="form-control">--%>
                </div>
            </div>

        </div>

        <div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12" style="background-color: #ffffff;padding: 0px;">
            <div style="float: left;width: 23%;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">停车时长</span>
                    <input type="text" id="parkOvertime" placeholder="小时" class="form-control">
                </div>
            </div>

            <div style="float: left;width: 23%;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon" id="beginTime">入场开始时间</span>
                    <input type="text" id="excelStart" placeholder="开始时间" class="form-control" readonly>
                </div>
            </div>

            <div style="float: left;width: 23%;;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon" id="endTime">入场结束时间</span>
                    <input type="text" id="excelEnd" placeholder="结束时间" class="form-control" readonly>
                </div>
            </div>

            <div  style="float:left;">
                <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>
            </div>

            <div  style="float:left;margin-left: 10px">
                <button type="button" class="btn btn-success" onclick="carInParkExcel()">导出报表</button>

            </div>

            <div   style="float:left;margin-left: 10px">
                <button type="button" class="btn btn-primary" onclick="search()">
                    <i class="fa fa-search"></i>&nbsp;&nbsp;搜索</button>
            </div>

        </div>

        <table class="table table-striped table-bordered table-hover dataTables-example" id="paymentList" style="background-color:#ffffff ">
            <thead>
            <tr style="color: #767676">
                <th>车牌号</th>
                <th>车场名称</th>
                <th>入场时间</th>
                <th>入场车道</th>
                <th>停车时长</th>
                <th>车辆属性</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>

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
                    <p><strong>如果删除，场内记录将消失，请谨慎操作</strong></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                    <button type="button"  id="deleteBtn" onclick="deleteCarInPark()" class="btn btn-primary">确认删除</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal inmodal fade" id="descModal" tabindex="-1" role="dialog" aria-hidden="true">

    <div class="modal-dialog"><!--框框-->
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
                        <div class="col-xs-12">
                            <img id="descImg" src="" style="width: 100%;">
                        </div>
                    </div>
                </form>
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
<script src="${pageContext.request.contextPath}/resources/script/admin/carInPark.js"></script>

</body>
</html>
