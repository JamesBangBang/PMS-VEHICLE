<%--
  Created by IntelliJ IDEA.
  User:
  Date: 2017-09-15
  Time: 14:45
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
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/chosen/chosen.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=n4dbtj9LyddAtuAgYbHQHGffpz1KRcct"></script>
    <style>
        .BMapLabel{
            border:none !important;
            background-color: rgba(0,0,0,0) !important;
        }
    </style>
    <title></title>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="gray-bg">

<div style="overflow: hidden;padding:5px;position: relative;height: 100%;width:100%;background-color:#003C65;">
    <div id="allmap" style="height: 115%;width: 100%;"></div>
    <div style="background-color:#003C65;height: 5px;width: 100%;position: absolute;bottom: 0;"></div>
</div>

<!-- 全局js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/jquery.min.js?v=2.1.4"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/bootstrap.min.js?v=3.3.6"></script>

<!-- ECharts -->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/echarts/echarts-all.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/chosen/chosen.jquery.js"></script>

<!-- 自定义js -->
<script src="${pageContext.request.contextPath}/resources/script/Heatmap_min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/content.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/GeoUtils_min.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/totalMap.js"></script>
</body>
</html>