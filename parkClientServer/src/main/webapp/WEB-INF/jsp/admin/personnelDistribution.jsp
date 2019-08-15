<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2017-06-23
  Time: 16:42
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

</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="gray-bg">

<div class="wrapper wrapper-content" style="
    height: 100%;
">
    <div class="row">
        <div class="col-md-6">
            <div  class="row">
                <div class="col-xs-12">
                    <div class="ibox">
                        <div class="ibox-title">
                            <h5>当前楼栋人员分布</h5>
                        </div>
                        <div class="ibox-content">
                            <div class="row"style="width: 100%" >
                                <div class="col-xs-7">
                                    <div class="echarts" id="echarts-pie-chart"></div>
                                </div>
                                <div class="col-xs-5">
                                    <div class="echarts" id="personnelCompositionPieChart"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="ibox-title">
                <h5>当前区域人员构成</h5>
            </div>
            <div class="ibox-content">
                <div class="echarts" id="echarts-bar-chart"></div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>园区人数走势</h5>
                </div>
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="echarts" id="building-line-chart"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>区域人数走势</h5>
                    <div style="position: absolute;top:9px;right:355px;">
                        <select id="nodeSelect" data-placeholder="选择位置..." class="chosen-select" tabindex="2" style="width: 120px;">

                        </select>
                    </div>
                    <div style="position: absolute;top:9px;right:225px;">
                        <select id="queryUserType" data-placeholder="选择人员类型..." class="chosen-select" tabindex="2" style="width: 120px;">
                            <option value="">全部人员</option>
                            <option value="0">保安人员</option>
                            <option value="1">黑名单人员</option>
                            <option value="2">特定人员</option>
                            <option value="3">外来人员</option>
                        </select>
                    </div>
                    <div style="position: absolute;top:9px;right:20px;">
                        <input type="hidden" id="timeSection">
                        <button class="btn btn-success" id="lastDay">前一天</button>
                        <button class="btn btn-primary" id="today">今天</button>
                        <button class="btn btn-success" id="nextDay">后一天</button>
                    </div>
                </div>
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="echarts" id="echarts-line-chart"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
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
<script src="${pageContext.request.contextPath}/resources/admin/js/content.js"></script>

<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/personnelDistribution.js"></script>

</body>

</html>
