<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/7/11
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
<body class="gray-bg">
<div class="wrapper wrapper-content"style="height: 100%;" ><!--页面距离-->

    <div class="row">
        <div class="ibox">
                <div class="ibox-title full-height-scroll">
                    <h3 >信息查询</h3>
                    <div  style=" position: absolute;right:800px;top:35px;">
                        <select id="queryWarnType" data-placeholder="选择模块类型..." class="chosen-select"  style="height: 34px" tabindex="2" >
                            <option value="">全部模块</option>
                            <option value="区域热度图报警">区域热度图报警</option>
                            <option value="首页报警">黑名单报警</option>
                            <%--<option value="待定">待定</option>--%>
                        </select>
                    </div>
                    <div class="input-group-addon" style=" height:34px; width:34px;position: absolute;right:680px;top:35px;"><span class="fa fa-users" ></span>
                    </div>


                <div style=" position: absolute;right: 80px;top:35px;">
                        <input type="text" id="message" placeholder="请输入信息" class="form-control " style="width: 600px" >
                    </div>
  <div  style=" position: absolute;right:25px;top:35px;">
                        <button  type="button" class="btn btn-primary" onclick="searchTrajectory()">搜索</button>
                    </div>

                </div>

                <div class="ibox-content full-height-scroll">

                    <table class="table table-striped table-bordered table-hover dataTables-example " id="pList">
                        <thead>
                        <tr>
                            <th>id</th>
                            <th>报警信息</th>
                            <th>报警时间</th>
                            <th>报警模块</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
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
<script src="${pageContext.request.contextPath}/resources/script/admin/warn.js"></script>

</body>
</html>
