<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2017-06-23
  Time: 17:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title></title>
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/jsTree/style.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/chosen/chosen.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">

</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="gray-bg" >

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-xs-3">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>组织机构树</h5>
                </div>
                <div class="ibox-content full-height-scroll">
                    <div id="orgTree"></div>
                </div>
            </div>
        </div>
        <div class="col-xs-9">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>人流量统计情况</h5>


                    <div style="position: absolute;top:9px;right:240px; ">
                        <select style=" margin-right: 6px;height: 34px;width: 80px; text-align: center" id="interval" data-placeholder="选择频率..."  class="chosen-select"  tabindex="2">
                            <option value="45">统计频率</option>
                            <option value="45">45分钟</option>
                            <option value="60">1h</option>
                            <option value="120">2h</option>
                            <option value="180">3h</option>
                        </select>
                    </div>



                    <div style="position: absolute;top:9px;right:120px;">
                        <select id="nodeSelect" data-placeholder="选择位置..." class="chosen-select" tabindex="2">
                            <option value="">全部位置</option>
                        </select>
                    </div>
                    <div style="position: absolute;top:9px;right:20px;">
                        <select id="sectionSelect" data-placeholder="选择区间..." class="chosen-select" tabindex="2">
                            <%--<option value="1">当日</option>--%>
                            <option value="2">近7天</option>
                            <option value="3">近30天</option>
                            <option value="4">自定义</option>
                        </select>
                    </div>
                </div>



                <div class="ibox-content" >
                    <%--//sssss--%>

                    <div style="display:none" id="search" >
                        <div class="col-xs-6">
                            <div class="form-group" id="data_5">
                                <input name="resourceId" id="orgId" type="hidden">
                                <label class="font-noraml">范围选择</label>
                                <div class="input-daterange input-group" id="datepicker">
                                    <input type="text" class="input-sm form-control" name="start"id="start" >
                                    <span class="input-group-addon">到</span>
                                    <input type="text" class="input-sm form-control" name="end" id="end" >
                                </div>
                            </div>

                        </div>
                        <div class="col-xs-4" style="margin-top: 18px;right: 4%" >
                            <button type="button" class="btn btn-primary " id="searchBtn"  >搜索</button>
                        </div>


                    </div>



                    <%--ssss--%>
                    <div class="echarts" id="echarts-line-chart"  >
                    </div>

                    <div class="alert alert-info" style="margin-top: 15%;" id="message" >

                    </div>

                    <div class="echarts"  id="echarts-line-Today" style="display: none">
                    </div>

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

<!-- Data picker -->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/datapicker/bootstrap-datepicker.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/content.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/jsTree/jstree.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/chosen/chosen.jquery.js"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/echarts/echarts-all.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/visitorsFlowrate.js"></script>
</body>

</html>




