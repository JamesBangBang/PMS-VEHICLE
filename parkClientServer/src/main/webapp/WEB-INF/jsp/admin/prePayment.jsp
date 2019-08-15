
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
        <div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12" style="background-color: #ffffff;padding: 0px;">
            <div style="float: left;width: 22%;margin-right: 2%">
                <div class="input-group m-b">
                    <span class="input-group-addon">车牌号</span>
                    <input type="text" id="orderOrechargeCarno" placeholder="查询" class="form-control">
                </div>
            </div>

            <div style="float: left;width: 22%;margin-right: 2%">
                <div class="input-group m-b">
                    <span class="input-group-addon">车场名称</span>
                    <input type="text" id="orderPrechargeCarparkName" placeholder="查询" class="form-control">
                </div>
            </div>

            <div style="float: left;width: 22%;margin-right: 2%">
                <div class="input-group m-b">
                    <span class="input-group-addon">开始时间</span>
                    <input type="text" id="excelStart" placeholder="年/月/日" class="form-control" readonly>
                </div>
            </div>

            <div style="float: left;width: 22%;margin-right: 2%">
                <div class="input-group m-b">
                    <span class="input-group-addon">结束时间</span>
                    <input type="text" id="excelEnd" placeholder="年/月/日 " class="form-control" readonly>
                </div>
            </div>

        </div>



        <div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12" style="background-color: #ffffff;padding: 0px;margin-bottom: 10px">
            <div   style="float:left;">
                <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>
            </div>

            <div  style="float:left;margin-left: 10px">
                <button type="button" class="btn btn-success" onclick="prePaymentExcel()">导出报表</button>
            </div>

            <div   style="float:left;margin-left: 10px">
                <button type="button" class="btn btn-success2 btn-success" onclick="search()">
                    <i class="fa fa-search"></i>&nbsp;&nbsp;搜索</button>
            </div>
    </div>

        <table class="table table-striped table-bordered " id="List" style="background-color:#ffffff ;width: 100%;">
            <thead>
            <tr style="color: #767676">
                <th>车牌号</th>
                <th>车场名称</th>
                <th>缴费类型</th>
                <th>操作员姓名</th>
                <th>应收金额</th>
                <th>实收金额</th>
                <th>免费金额</th>
                <th>免费原因</th>
                <th>备注</th>
                <th>缴费时间</th>
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
<script src="${pageContext.request.contextPath}/resources/script/admin/prePayment.js"></script>

</body>
</html>
