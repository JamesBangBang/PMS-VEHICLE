<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2017-06-23
  Time: 16:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${pageContext.request.contextPath}/resources/admin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/font-awesome.css?v=4.4.0" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.print.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <style>
        .timeline{
            overflow-x: hidden;
        }
    </style>
    <title></title>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="gray-bg">

<div class="wrapper wrapper-content">
    <div class="row " style="
    height: 100%;
">
        <div class="col-md-6 col-lg-6">
            <div class="ibox">
                <div class="ibox-content">
                    <div class="clients-list">
                        <ul class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#tab-1"><i class="fa fa-lg fa-user"></i> 姓名</a>
                            </li>
                            <li class=""><a data-toggle="tab" href="#tab-2"><i class="fa fa-lg fa-wifi"></i> MAC</a>
                            </li>
                            <li class=""><a data-toggle="tab" href="#tab-3"><i class="fa fa-lg fa-users"></i> 人员类别查询</a>
                            </li>

                        </ul>
                        <div class="tab-content">
                            <div id="tab-1" class="tab-pane active" style="height:40px;">
                                <div style="margin-bottom:15px;"></div>
                                <div class="input-group m-b">
                                    <span class="input-group-addon"><i class="fa fa-user"></i></span>
                                    <input type="text" id="queryName" placeholder="请输入姓名" class="form-control">
                                    <span class="input-group-btn">
                                        <button type="button" class="btn btn-success" onclick='searchTrajectory(1)'>搜索</button>
                                    </span>
                                </div>
                            </div>
                            <div id="tab-2" class="tab-pane" style="height:40px;">
                                <div style="margin-bottom:15px;"></div>
                                <div class="input-group m-b">
                                    <span class="input-group-addon"><i class="fa fa-wifi"></i></span>
                                    <input type="text" id="queryMac" placeholder="请输入MAC地址" class="form-control">
                                    <span class="input-group-btn">
                                        <button type="button" class="btn btn-success" onclick='searchTrajectory(2)'>搜索</button>
                                    </span>
                                </div>
                            </div>


                            <div id="tab-3" class="tab-pane"style="height:40px;" >
                                <div style="margin-bottom:15px;"></div>
                                <div class="input-group m-b">
                                    <span class="input-group-addon"><i class="fa fa-users"></i></span>


                                    <select  class="form-control  " id="list-select" style="width: 100%;" >
                                        <option value="0">
                                            保安人员
                                        </option>
                                        <option value="1">
                                            黑名单人员
                                        </option>
                                        <option value="2">
                                            特定人员
                                        </option>

                                    </select>


                                    <span class="input-group-btn">
                                        <button type="button" class="btn btn-success" onclick='searchTrajectory(0)'>搜索</button>
                                    </span>
                                </div>
                            </div>



                        </div>
                    </div>
                </div>
            </div>
            <div class="ibox">
                <div class="ibox-title">
                    <h5><i class="fa fa-calendar"></i> 近30天出勤</h5>
                </div>
                <div class="ibox-content">
                    <div id="calendar"></div>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-lg-6">
            <div class="ibox">
                <div class="ibox-title">
                    <h5><i class="fa fa-puzzle-piece"></i> 轨迹详情</h5>
                    <div style="position: absolute;top:9px;right:20px;">
                        <button class="btn btn-success" id="mapTrajectory">地图轨迹</button>
                    </div>
                </div>
                <div class="ibox-content" style="overflow-x: auto;">
                    <div id="macContainer">

                    </div>

                </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal inmodal fade" id="trajectoryModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-lg"><!--框框-->
            <div class="modal-content" ><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title">地图轨迹</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>
                <div class="modal-body" >
                    <div id="allmap" style="height: 500px;width: 100%;"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 全局js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/jquery.min.js?v=2.1.4"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/bootstrap.min.js?v=3.3.6"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<!-- Full Calendar -->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/fullcalendar/fullcalendar.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=n4dbtj9LyddAtuAgYbHQHGffpz1KRcct"></script>

<!-- 自定义js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/content.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/trajectory.js"></script>

</body>

</html>