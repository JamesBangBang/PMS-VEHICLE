<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/9/18
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
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/dataTables/buttons.dataTables.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.print.css" rel="stylesheet">
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body style="background-color: #082F4E;padding: 10px;">
<div class=" wrapper-content"style="height:100%;background-color: #FFFFFF" >
    <div class="col-xs-12 " style="padding-top: 0px">
        <div class="col-sm-3">
            <div class="input-group m-b">
                <span class="input-group-addon input-group-background">停车时长</span>
                <select class="form-control m-b" id="parkingLengthTime">
                    <option>0-30分钟</option>
                    <option>30-60小时</option>
                    <option>1-3小时</option>
                    <option>3-12小时</option>
                    <option>12-24小时</option>
                    <option>24小时以上</option>
                </select>
            </div>
        </div>

        <div class="col-sm-3">
            <div class="input-group m-b">
                <span class="input-group-addon input-group-background">统计时间类型</span>
                <select class="form-control m-b" name="account" id="time-type" onchange="timeTypeChange()">
                    <option>日报</option>
                    <option>周报</option>
                    <option>月报</option>
                    <option>年报</option>
                </select>
            </div>
        </div>

        <div class="col-sm-3" >
            <div class="input-group m-b">
                <span class="input-group-addon input-group-background">开始时间</span>
                <input placeholder="年/月/日" class="form-control layer-date" id="start">
            </div>
        </div>

        <div class="col-sm-3" >
            <div class="input-group m-b">
                <span class="input-group-addon input-group-background">结束时间</span>
                <input placeholder="年/月/日" class="form-control layer-date"  style="border-left: none" id="end">
            </div>
        </div>
    </div>
    <div class="col-xs-12" style="padding-top:0px;border-bottom: solid 1px #EFEFEF">
        <div class="col-sm-1">
            <div class="input-group m-b">
                <a href="javascript:void(0)" type="button" class="btn btn-success btn-success2"  data-toggle="modal" onclick="searchParkingLength()"><i class="fa fa-search"></i>&nbsp;&nbsp;搜索</a>
            </div>
        </div>
        <div class="col-sm-1">
            <div class="input-group m-b">
                <a href="javascript:void(0)" type="button" class="btn btn-success " data-toggle="modal" onclick="exportParkingLength()">导出报表</a>
            </div>
        </div>
    </div>
    <div class="col-xs-12" style="margin-top: 20px;background-color:#ffffff;">
        <table class="table table-striped table-bordered table-hover dataTables-example " id="parkingLength">
            <thead>
            <tr style="color: #767676;">
            <tr ><td rowspan=2>停车时长</td><td rowspan=2>时间</td><td colspan=3 style="background-color:#ffffff ">车辆进出</td><td colspan=4 style="background-color:#ffffff ">停车时长</td></tr>
            <tr><td>进场次数</td><td>出场次数</td><td>进出车辆数</td>
                <td>合计(分钟)</td><td>最大值(分钟)</td><td>最小值(分钟)</td><td>平均值(分钟)</td>
            </tr>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
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
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/buttons.html5.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/dataTables.buttons.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/jszip.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/fullcalendar/fullcalendar.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/parkingLength.js"></script>
<!-- layerDate plugin javascript时间表格 -->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/layer/laydate/laydate.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/CSSP_OCX.js"></script>
</body>
</html>
