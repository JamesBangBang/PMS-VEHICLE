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
<div class=" wrapper-content"style="height:100%;background-color: #FFFFFF" >

        <div class="col-xs-12" style="background-color: #ffffff;padding-right: 0px;">

                <div class="col-md-8" style="padding: 0px">
                    <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>
                <%--<h3  style="color:#767676"><i class="fa fa-bars" style="color:#767676"></i>&nbsp;&nbsp;员工交班明细</h3>--%>
                </div>
                <div class="col-md-4">
                    <div class="input-group m-b">
                        <span class="input-group-addon"><i class="fa fa-bank"></i>&nbsp;&nbsp;岗亭名称</span>
                        <input type="text" id="boxName" placeholder="查询" class="form-control">
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-success" onclick="search()">查询</button>
                        </span>
                    </div>
                </div>

        </div>

        <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;border-top: solid 2px #EFEFEF;">
            <%--<div style="margin-top: 15px;margin-bottom: 15px;padding:0px;float: left" >--%>
                <%--<button class="btn btn-success " type="button"><span class="bold">导出报表</span>--%>
                <%--</button>--%>
            <%--</div>--%>
        </div>

            <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;">
                <table class="table table-striped table-bordered table-hover dataTables-example" id="shiftDetails" style="background-color:#ffffff ">
                    <thead>
                    <tr style="color: #767676">
                        <th>岗亭名称</th>
                        <th>交班人姓名</th>
                        <th>交班时间</th>
                        <th>接班人姓名</th>
                        <th>交接班时间</th>
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
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/fullcalendar/fullcalendar.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/shiftDetails.js"></script>
<!-- layerDate plugin javascript时间表格 -->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/layer/laydate/laydate.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/CSSP_OCX.js"></script>

</body>
</html>
