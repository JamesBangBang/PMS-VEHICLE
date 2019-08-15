<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2017-06-26
  Time: 17:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%--<meta http-equiv="refresh" content="45">--%>
    <title></title>

    <link rel="shortcut icon" href="favicon.ico">
    <link href="${pageContext.request.contextPath}/resources/admin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/chosen/chosen.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=n4dbtj9LyddAtuAgYbHQHGffpz1KRcct"></script>

    <title></title>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="gray-bg">

<div class="wrapper wrapper-content" style="overflow: hidden">
    <div style="position: fixed;left:0px;top:0px;height:25px;">
        <div style="background-color:#EE4647;float: left;color:#FFFFFF;width: 100px;text-align:center; ">密集</div>
        <div style="background-color:#0091F8;float: left;color:#FFFFFF;width: 100px;text-align:center; ">普通</div>
        <div style="background-color:#00A400;float: left;color:#FFFFFF;width: 100px;text-align:center; ">稀疏</div>
    </div>
    <div class="row">

        <div id="allmap" style="height: 96%;width: 100%;"></div>
        <div id="r-result">
            <input type="button"  onclick="openHeatmap();" value="显示热力图"/><input type="button"  onclick="closeHeatmap();" value="关闭热力图"/>
        </div>
    </div>

    <div class="modal inmodal fade" id="videoModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-lg"><!--框框-->
            <div class="modal-content" ><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title">实时视频</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>
                <div class="modal-body" >
                    <object
                            id="SNMSCloudOcx"
                            classid="clsid:DEEE91FD-B7EE-451A-BF54-F7A289BC422D"
                            width=100%
                            height=450
                            align=top
                            hspace=0
                            vspace=0
                    ></object>
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
<script src="${pageContext.request.contextPath}/resources/script/Heatmap_min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/content.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/CSSP_OCX.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/GeoUtils_min.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/sectionMap.js"></script>
</body>
</html>
