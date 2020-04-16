
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
<div class=" wrapper-content"style="height:100%;background-color: #FFFFFF;min-width:828px;" >
    <div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12" style="margin-top: 0px;background-color:#ffffff;">
        <div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12 no-padding" >

            <div class="col-xs-3 col-xs-3 col-sm-3 col-md-3 col-lg-3 no-padding">
                <div class="input-group m-b">
                    <span class="input-group-addon">车牌号码</span>
                    <input type="text" id="carPlate" placeholder="支持模糊查询" class="form-control">
                </div>
            </div>

            <div class="col-xs-3 col-xs-3 col-sm-3 col-md-3 col-lg-3 ">
                <div class="input-group m-b">
                    <span class="input-group-addon">开始时间</span>
                    <input type="text" id="excelStart" placeholder="年/月/日" class="form-control" readonly>
                </div>
            </div>

            <div class="col-xs-3 col-xs-3 col-sm-3 col-md-3 col-lg-3 ">
                <div class="input-group m-b">
                    <span class="input-group-addon">结束时间</span>
                    <input type="text" id="excelEnd" placeholder="年/月/日" class="form-control" readonly>
                </div>
            </div>

            <div class="col-xs-3 col-xs-3 col-sm-3 col-md-3 col-lg-3 ">
                <div class="input-group m-b">
                    <span class="input-group-addon">支付类型</span>
                    <select id="payType" data-placeholder="选择支付类型" class="form-control"  >
                    </select>
                </div>
            </div>
        </div>

        <div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12 no-padding" >
            <%--<div class="col-xs-3 col-xs-3 col-sm-3 col-md-3 col-lg-3 no-padding">
                <div class="input-group m-b">
                    <span class="input-group-addon">支付状态</span>
                    <select id="payStatus" data-placeholder="选择支付状态" class="form-control"  >

                    </select>
                </div>
            </div>--%>

            <div class="col-xs-3 col-xs-3 col-sm-3 col-md-3 col-lg-3 no-padding">
                <div class="input-group m-b">
                    <span class="input-group-addon">出场情况</span>
                    <select id="markInfo" data-placeholder="" class="form-control"  >
                        <option value="">全部</option>
                        <option value="0">未出场</option>
                        <option value="1">已出场</option>
                    </select>
                </div>
            </div>

            <div class="col-xs-3 col-xs-3 col-sm-3 col-md-3 col-lg-3 ">
                <div class="input-group m-b">
                    <span class="input-group-addon">车场名称</span>
                    <input type="text" id="carparkName" placeholder="支持模糊查询" class="form-control">
                </div>
            </div>

            <div class="col-xs-3 col-xs-3 col-sm-3 col-md-3 col-lg-3 " style="margin-left: -15px;">

                <div class="col-xs-4 col-xs-4 col-sm-4 col-md-4 col-lg-4 ">
                    <button type="button" class="btn btn-success2 btn-success" onclick="search()">
                        <i class="fa fa-search"></i>&nbsp;&nbsp;搜索</button>
                </div>

                <div class="col-xs-4 col-xs-4 col-sm-4 col-md-4 col-lg-4 ">
                    <button type="button" class="btn btn-success" onclick="realPaymentExcel()">导出报表</button>
                </div>

                <div class="col-xs-4 col-xs-4 col-sm-4 col-md-4 col-lg-4 ">
                    <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>
                </div>
            </div>
        </div>

        <div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12 no-padding" >
            <div class="col-xs-3 col-xs-3 col-sm-3 col-md-3 col-lg-3 no-padding">
                <div class="input-group m-b">
                    <span class="input-group-addon">总金额</span>
                    <input type="text" id="totalFee" placeholder="元" class="form-control" readonly>
                </div>
            </div>

            <div class="col-xs-3 col-xs-3 col-sm-3 col-md-3 col-lg-3 ">
                <div class="input-group m-b">
                    <span class="input-group-addon">现金支付</span>
                    <input type="text" id="cashFee" placeholder="元" class="form-control" readonly>
                </div>
            </div>

            <div class="col-xs-3 col-xs-3 col-sm-3 col-md-3 col-lg-3 ">
                <div class="input-group m-b">
                    <span class="input-group-addon">手机支付</span>
                    <input type="text" id="onlineFee" placeholder="元" class="form-control" readonly>
                </div>
            </div>
        </div>

        <table class="table table-striped table-bordered table-hover dataTables-example tableCss2" id="tranSactionFlowList">
                <thead>
                <tr >
                    <th>车牌号码</th>
                    <th>车场名称</th>
                    <th>支付类型</th>
                    <th>支付时间</th>
                    <th>总金额</th>
                    <th>备注</th>
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

<script src="${pageContext.request.contextPath}/resources/script/admin/tranSactionFlow.js"></script>

</body>
</html>
