<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2017-12-28
  Time: 10:03
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
    <link href="${pageContext.request.contextPath}/resources/bootstrap-switch/css/bootstrap3/bootstrap-switch.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/jasny/jasny-bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/timepicker/bootstrap-timepicker.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/ionRangeSlider/ion.rangeSlider.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/ionRangeSlider/ion.rangeSlider.skinFlat.css" rel="stylesheet">
    <style>
        .nav>li>a:focus, .nav>li>a:hover{
            color:#000000;
        }
        .irs-slider{
            background-color: #00a2d4;
            background-image: none;
        }
        .irs-from, .irs-to, .irs-single {
            background-color: #00a2d4;
            background-image: none;
        }
        .irs-from::after, .irs-to::after, .irs-single::after {
            position: absolute;
            display: block;
            content: "";
            bottom: -6px;
            left: 50%;
            width: 0px;
            height: 0px;
            margin-left: -3px;
            overflow: hidden;
            border-width: 3px;
            border-style: solid;
            border-color: #00a2d4 transparent transparent;
            border-image: initial;
        }


    </style>
    <title></title>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12">
            <button id="refreshBtn" class="btn btn-default">刷新表格</button>
            <button id="timeFormatBtn" class="btn btn-info">一键校时</button>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <table class="table table-striped table-bordered table-hover dataTables-example" id="autoTable" style="background-color:#ffffff;width:100%;">
                <thead>
                <tr style="color: #767676">
                    <th>设备名称</th>
                    <th>设备IP</th>
                    <th>设备端口</th>
                    <th>设备用户名</th>
                    <th>设备密码</th>
                    <th>设备类型</th>
                    <th>所属车道</th>
                    <th>设备状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>

    <div class="modal inmodal fade" id="editModal" tabindex="-1" role="dialog" aria-hidden="true">

        <div class="modal-dialog modal-lg"><!--框框-->
            <div class="modal-content"><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title">摄像机设备配置</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>

                <div class="modal-body">
                    <div class="tabs-container">
                        <ul class="nav nav-tabs">

                            <%--               <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">NVR信息</a>
                            </li>--%>

                                <li class="active">
                                    <a data-toggle="tab" href="#tab-1" aria-expanded="false">基本配置</a>
                                </li>
                                <li class="">
                                    <a data-toggle="tab" href="#tab-2" aria-expanded="true"> 系统配置</a>
                                </li>
                                <li class="">
                                    <a data-toggle="tab" href="#tab-3" aria-expanded="false">OSD及补光配置</a>
                                </li>
                                <li class="">
                                    <a data-toggle="tab" href="#tab-4" aria-expanded="false">场景配置</a>
                                </li>
                                <li class="">
                                    <a data-toggle="tab" href="#tab-6" aria-expanded="false">显示配置</a>
                                </li>
                                <li class="">
                                    <a data-toggle="tab" href="#tab-7" aria-expanded="false">播报配置</a>
                                </li>
                        </ul>
                        <div class="tab-content">

                            <%--<div id="tab-2" class="tab-pane">--%>
                                <%--<div class="panel-body">--%>
                                    <%--<form id="nvrFrom">--%>
                                        <%--<div class="row">--%>
                                            <%--<div class="col-xs-6">--%>
                                                <%--<div class="form-group">--%>
                                                    <%--<label>IP</label>--%>
                                                    <%--<input name="nvrIp" type="text" class="form-control">--%>
                                                <%--</div>--%>
                                                <%--<div class="form-group">--%>
                                                    <%--<label>端口</label>--%>
                                                    <%--<input name="nvrPort" type="number" class="form-control">--%>
                                                <%--</div>--%>
                                                <%--<div class="form-group">--%>
                                                    <%--<label>通道号</label>--%>
                                                    <%--<input name="channelNo" type="number" class="form-control">--%>
                                                <%--</div>--%>
                                            <%--</div>--%>
                                            <%--<div class="col-xs-6">--%>
                                                <%--<div class="form-group">--%>
                                                    <%--<label>用户名</label>--%>
                                                    <%--<input name="nvrUserName" type="text" class="form-control">--%>
                                                <%--</div>--%>
                                                <%--<div class="form-group">--%>
                                                    <%--<label>密码</label>--%>
                                                    <%--<input name="nvrPwd" type="text" class="form-control">--%>
                                                <%--</div>--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
                                    <%--</form>--%>

                                <%--</div>--%>
                            <%--</div>--%>
                            <div id="tab-1" class="tab-pane active">
                                <div class="panel-body">

                                    <div class="row">
                                        <form id="basicForm">
                                            <div class="col-xs-6">
                                                <input id="basicDevId" type="hidden" name="deviceId" value="">
                                                <input type="hidden" name="addressType">
                                                <input type="hidden" name="dnsAddress1">
                                                <input type="hidden" name="dnsAddress2">
                                                <input type="hidden" name="pppoe.username">
                                                <input type="hidden" name="pppoe.password">
                                                <input type="hidden" name="phonePort">
                                                <input type="hidden" name="rtspCheck">
                                                <input type="hidden" name="rtspPort">

                                                <div class="form-group">
                                                    <label>用户名</label>
                                                    <input name="deviceUsername" type="text" class="form-control" disabled>
                                                </div>
                                                <div class="form-group">
                                                    <label>密码</label>
                                                    <input name="devicePwd" type="password" class="form-control" disabled>
                                                </div>
                                                <div class="form-group">
                                                    <label>设备名称</label>
                                                    <input id="deviceName" name="deviceName" type="text" class="form-control">
                                                </div>
                                                <div class="form-group">
                                                    <label>设备IP</label>
                                                    <input name="ipv4Address" type="text" class="form-control">
                                                </div>
                                            </div>
                                            <div class="col-xs-6">
                                                <div class="form-group">
                                                    <label>子网掩码</label>
                                                    <input name="subnetMask" type="text" class="form-control">
                                                </div>
                                                <div class="form-group">
                                                    <label>网关</label>
                                                    <input name="gatewayAddress" type="text" class="form-control">
                                                </div>
                                                <div class="form-group">
                                                    <label>端口</label>
                                                    <input name="httpPort" type="number" class="form-control" readonly>
                                                </div>

                                            </div>
                                        </form>
                                        <div class="col-xs-6">
                                            <div class="row" >
                                                <iframe id="updateSystemFrame" name="updateSystemFrame" src="" style="display:none;" onload="updateResult()"></iframe>
                                                <form id="updateSystemForm" name="updateSystemForm" action="${pageContext.request.contextPath}/device/manage/update/file/sys" target="updateSystemFrame" enctype="multipart/form-data" method="post">
                                                   <%--<input id="sysDevId" type="hidden" name="sysDevId" value="">--%>
                                                    <div class="col-xs-8">
                                                        <div id="file-pretty">
                                                            <div class="form-group">
                                                                <label>设备固件</label>
                                                                <input type="file" name="devSoftware" class="form-control pull-right" style="margin-top: 5px">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-xs-4">
                                                        <button id="updateDev" class=" btn btn-primary" style="margin-top: 22px;">升级设备</button>
                                                    </div>
                                                </form>

                                                <div class="col-xs-12" style="text-align: right;">
                                                    <button id="formatSdCard" class=" btn btn-primary">格式化SD卡</button>
                                                    <button id="rebootDev" class=" btn btn-primary">重启设备</button>

                                                    <button id="saveDevBasic" class=" btn btn-primary">保存到设备</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                            <div id="tab-2" class="tab-pane">
                                <div class="panel-body">
                                    <form id="sysForm">
                                        <div class="row">
                                            <div class="col-xs-6">
                                                <input id="sysDevId" type="hidden" name="deviceId" value="">
                                                <input type="hidden" name="direction"><%--出入口属性 0： 入口 1： 出口 --%>
                                                <input type="hidden" name="bSlave"><%--主从属性 0： 主 IPC 1： 附属 IPC --%>
                                                <input type="hidden" name="bEnableCenter"><%--启用中心服务器开关--%>
                                                <input type="hidden" name="bEnableSlaveIPC"><%--启用附属 IPC 开关 0： 关闭 1： 启用 --%>
                                                <input type="hidden" name="slaveIPC.addr"><%--附属 IPC 地址--%>
                                                <input type="hidden" name="slaveIPC.port"><%--附属 IPC 端口--%>
                                                <input type="hidden" name="bEnableAssociateIPC"><%--启用单车道双向行驶关联 IPC开关 0： 关闭 1： 启用--%>
                                                <input type="hidden" name="associateIPC.addr"><%--关联 IPC 地址--%>
                                                <input type="hidden" name="associateIPC.port"><%--关联 IPC 端口--%>
                                                <input type="hidden" name="bEnableServer"><%--启用服务开关 0： 关闭 1： 启用--%>
                                                <input type="hidden" name="server.port">

                                                <div class="form-group">
                                                    <label>中心服务器IP地址</label>
                                                    <input name="center.addr" type="text" class="form-control">
                                                </div>
                                                <div class="form-group">
                                                    <label>中心服务器端口</label>
                                                    <input name="center.port" type="text" class="form-control" readonly>
                                                </div>
                                                <div class="form-group">
                                                    <label>自动放行模式</label>
                                                    <select name="openMode" class="form-control">
                                                        <option value="1">白名单车辆</option>
                                                        <option value="2">所有车辆</option>
                                                        <option value="3">所有车辆禁止自动放行</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-xs-6">
                                                <div class="form-group">
                                                    <label>所属车道</label>
                                                    <select name="carRoadId" type="text" class="form-control">
                                                        <option value="">无</option>
                                                        <c:forEach items="${carRoads}" var="carRoad">
                                                            <option value="${carRoad.id}">${carRoad.name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label>附属智能单元</label>
                                                    <select id="sysSubIpcId" name="subIpcId" class="form-control" data-placeholder="请选择">
                                                        <option value="">无</option>
                                                        <c:forEach items="${ipcDevs}" var="ipc">
                                                            <option value="${ipc.id}">${ipc.name}</option>
                                                        </c:forEach>

                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label>关联智能单元</label>
                                                    <select id="sysRelateIpcId" name="relateIpcId" class="form-control" data-placeholder="请选择">
                                                        <option value="">无</option>
                                                        <c:forEach items="${ipcDevs}" var="ipc">
                                                            <option value="${ipc.id}">${ipc.name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label>关联作用时间(1 - 300秒)</label>
                                                    <input name="associateIPC.landtimeout" type="number" class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="row">
                                        <div class="col-xs-12" style="text-align: right;">
                                            <button id="saveDeviceSystem" class="btn btn-primary">保存到设备</button>
                                        </div>

                                    </div>
                                </div>
                            </div>
                            <div id="tab-3" class="tab-pane">
                                    <div class="panel-body">


                                        <div class="row">

                                            <div class="col-xs-12">
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <form id="lightForm">
                                                            <input type="hidden" name="deviceId" value="">
                                                            <input id="offTime_h" type="hidden" name="offTime_h">
                                                            <input id="offTime_m" type="hidden" name="offTime_m">
                                                            <input id="offTime_s" type="hidden" name="offTime_s">
                                                            <input id="onTime_h" type="hidden" name="onTime_h">
                                                            <input id="onTime_m" type="hidden" name="onTime_m">
                                                            <input id="onTime_s" type="hidden" name="onTime_s">

                                                            <div class="row">
                                                                 <div class="col-xs-6">
                                                                     <div class="form-group">
                                                                         <label>显示车牌</label>
                                                                         <select name="chanoverlaydsp5" class="form-control" style="width: 100%;">
                                                                             <option value="0">关闭</option>
                                                                             <option value="1">启用</option>
                                                                         </select>
                                                                     </div>
                                                                 </div>
                                                                 <div class="col-xs-6">
                                                                     <div class="form-group">
                                                                         <label>显示日期时间</label>
                                                                         <select name="chantimedsp" class="form-control" style="width: 100%;">
                                                                             <option value="0">关闭</option>
                                                                             <option value="1">启用</option>
                                                                         </select>
                                                                     </div>
                                                                 </div>
                                                                 <div class="col-xs-6">
                                                                     <div class="form-group">
                                                                         <label>显示车道名称</label>
                                                                         <select name="channamedsp" class="form-control" style="width: 100%;">
                                                                             <option value="0">关闭</option>
                                                                             <option value="1">启用</option>
                                                                         </select>
                                                                     </div>
                                                                 </div>
                                                             </div>
                                                            <div class="row">
                                                                <div class="col-xs-6">
                                                                    <div class="form-group">
                                                                        <label>曝闪灯模式</label>
                                                                        <select name="StrobeLight" class="form-control" style="width: 100%;">
                                                                            <option value="0">关闭</option>
                                                                            <option value="1">与地感联动</option>
                                                                        </select>
                                                                    </div>
                                                                </div>
                                                                <div class="col-xs-6">
                                                                    <div class="form-group">
                                                                        <label>曝闪灯持续时间(50 - 1000 毫秒)</label>
                                                                        <input name="StrobeTime" type="number" class="form-control">
                                                                    </div>
                                                                </div>
                                                                <div class="col-xs-6">
                                                                    <div class="form-group">
                                                                        <label>补光模式</label>
                                                                        <select id="lightMode" name="mode" class="form-control" style="width: 100%;">
                                                                            <option value="0">关闭</option>
                                                                            <option value="1">启动</option>
                                                                            <option value="2">自动</option>
                                                                            <option value="3">定时</option>
                                                                        </select>
                                                                    </div>
                                                                </div>
                                                                <div class="col-xs-6">
                                                                    <div class="row" id="lightDelay" style="display: none;">
                                                                        <div class="col-xs-12">
                                                                            <div class="form-group">
                                                                                <label>关灯延时(0 - 60 秒)</label>
                                                                                <input type="number" name="delay" class="form-control">
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row" id="lightTime" style="display:none;">
                                                                        <div class="col-xs-6">
                                                                            <div class="bootstrap-timepicker">
                                                                                <div class="form-group">
                                                                                    <label>开始时间</label>
                                                                                    <div class="input-group">
                                                                                        <input type="text" id="dayStartTime" class="form-control timepicker">
                                                                                        <div class="input-group-addon">
                                                                                            <i class="fa fa-clock-o"></i>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                        <div class="col-xs-6">
                                                                            <div class="bootstrap-timepicker">
                                                                                <div class="form-group">
                                                                                    <label>结束时间</label>
                                                                                    <div class="input-group">
                                                                                        <input type="text" id="dayEndTime" class="form-control timepicker">
                                                                                        <div class="input-group-addon">
                                                                                            <i class="fa fa-clock-o"></i>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-12" style="text-align: right;">
                                                <button id="saveLightAndOsd" class="btn btn-primary">保存到设备</button>
                                            </div>

                                        </div>

                                    </div>
                                </div>
                            <div id="tab-4" class="tab-pane">
                                    <div class="panel-body">
                                        <div class="row">
                                            <div class="col-xs-6">
                                                <OBJECT
                                                        id="iparkOcx"
                                                        classid="clsid:7C5943E3-E9EF-410B-BC54-DE68C2B5627A"
                                                        height="376"
                                                        width="100%"
                                                        align="center"
                                                        hspace="0"
                                                        vspace="0"
                                                        wHttpPort="554"
                                                >
                                                </OBJECT>
                                            </div>
                                            <form id="secneFrom">
                                                <input type="hidden" name="deviceId" value="">
                                                <input id="regionheight" type="hidden" name="region.height">
                                                <input id="regionwidth" type="hidden" name="region.width">
                                                <input id="regionx" type="hidden" name="region.x">
                                                <input id="regionx1" type="hidden" name="region.x1">
                                                <input id="regionx2" type="hidden" name="region.x2">
                                                <input id="regionx3" type="hidden" name="region.x3">
                                                <input id="regionx4" type="hidden" name="region.x4">
                                                <input id="regionx5" type="hidden" name="region.x5">
                                                <input id="regiony" type="hidden" name="region.y">
                                                <input id="regiony1" type="hidden" name="region.y1">
                                                <input id="regiony2" type="hidden" name="region.y2">
                                                <input id="regiony3" type="hidden" name="region.y3">
                                                <input id="regiony4" type="hidden" name="region.y4">
                                                <input id="regiony5" type="hidden" name="region.y5">
                                                <input type="hidden" name="CarColorOn">
                                                <input type="hidden" name="DayTriggerThresh">
                                                <input type="hidden" name="EnableSamePlateCar">
                                                <input type="hidden" name="MaxPlateWidth">
                                                <input type="hidden" name="MinPlateWidth">
                                                <input type="hidden" name="NightTriggerThresh">
                                                <input type="hidden" name="PlateAngle">
                                                <input type="hidden" name="PlateConfidThrld">
                                                <input type="hidden" name="PlateTypeSp">
                                                <input type="hidden" name="PriorArmyPlate">
                                                <input type="hidden" name="SamePlateCarOutputTime">
                                                <input type="hidden" name="TrigerInterval">
                                                <input type="hidden" name="TriggerSensitivity">
                                                <div class="col-xs-6">
                                                <div class="row">
                                                    <div class="col-xs-6">
                                                        <div class="form-group">
                                                            <label>启用车牌识别</label>
                                                            <select name="enabled" class="form-control" style="width:100%">
                                                                <option value="0">关闭</option>
                                                                <option value="1">启用</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group">
                                                            <label>车检模式</label>
                                                            <select name="detectMode" class="form-control" style="width:100%">
                                                                <option value="0">车头模式</option>
                                                                <option value="1">车尾模式</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group">
                                                            <label>检测方向</label>
                                                            <select name="detectDoubleDirect" class="form-control" style="width:100%">
                                                                <option value="0">单向（车头或车尾）</option>
                                                                <option value="1">双向（车头及车尾）</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group">
                                                            <label>触发模式</label>
                                                            <select name="triggerMode" class="form-control" style="width:100%">
                                                                <option value="0">线圈触发</option>
                                                                <option value="1">视频触发</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="col-xs-6">
                                                        <div class="form-group">
                                                            <label>摄像机安装位置</label>
                                                            <select name="cameraPos" class="form-control" style="width:100%">
                                                                <option value="0">司机同侧（推荐）</option>
                                                                <option value="1">司机异侧</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group">
                                                            <label>无车牌过滤</label>
                                                            <select name="FilterByPlate" class="form-control" style="width:100%">
                                                                <option value="0">关闭</option>
                                                                <option value="1">开启</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group">
                                                            <label>优先省份</label>
                                                            <select name="PriorCityType" class="form-control" style="width:100%">
                                                                <option value="3" >全国</option>
                                                                <option value="4" >北京</option>
                                                                <option value="5" >上海</option>
                                                                <option value="6" >天津</option>
                                                                <option value="7" >重庆</option>
                                                                <option value="8" >黑龙江省</option>
                                                                <option value="9" >吉林省</option>
                                                                <option value="10" >辽宁省</option>
                                                                <option value="11" >内蒙古自治区</option>
                                                                <option value="12" >河北省</option>
                                                                <option value="13" >山东省</option>
                                                                <option value="14" >山西省</option>
                                                                <option value="15" >安徽省</option>
                                                                <option value="16" >江苏省</option>
                                                                <option value="17" >浙江省</option>
                                                                <option value="18" >福建省</option>
                                                                <option value="19" >广东省</option>
                                                                <option value="20" >河南省</option>
                                                                <option value="21" >江西省</option>
                                                                <option value="22" >湖南省</option>
                                                                <option value="23" >湖北省</option>
                                                                <option value="24" >广西壮族自治区</option>
                                                                <option value="25" >海南省</option>
                                                                <option value="26" >云南省</option>
                                                                <option value="27" >贵州省</option>
                                                                <option value="28" >四川省</option>
                                                                <option value="29" >陕西省</option>
                                                                <option value="30" >甘肃省</option>
                                                                <option value="31" >宁夏回族自治区</option>
                                                                <option value="32" >青海省</option>
                                                                <option value="33" >西藏自治区</option>
                                                                <option value="34" >新疆维吾尔自治区</option>
                                                                <option value="35" >台湾省</option>
                                                                <option value="36" >香港特别行政区</option>
                                                                <option value="37" >澳门特别行政区</option>
                                                            </select>
                                                        </div>

                                                    </div>
                                                </div>
                                            </div>
                                            </form>
                                            <div class="col-xs-6">
                                                <button id="drawRectangle" class="btn btn-primary" style="margin-bottom: 8px">绘制矩形识别区域</button>
                                                <button id="drawPolygon" class="btn btn-primary" style="margin-bottom: 8px">绘制多边形识别区域</button>
                                                <button id="exitDraw" class="btn btn-danger" style="margin-bottom: 8px;display: none;">完成识别区域绘制</button>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-xs-12" style="text-align: right;">
                                                <button id="saveSceneInfo" class="btn btn-primary">保存到设备</button>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            <div id="tab-6" class="tab-pane">
                                    <div class="panel-body">
                                        <form id="ledForm">
                                            <input name="deviceId" type="hidden" value="">
                                            <div class="row">
                                                <div class="col-xs-4">
                                                    <div class="form-group">
                                                        <label>显示屏类型</label>
                                                        <select name="deviceLedType" class="form-control" id="deviceLedType">
                                                            <option value="threeRow">三行显示屏</option>
                                                            <option value="fourRow">四行显示屏</option>
                                                            <option value="guideScreen">车位引导屏</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-xs-4">
                                                    <div class="form-group">
                                                        <label>串口号</label>
                                                        <select name="deviceChannelNo" class="form-control">
                                                            <option value="1">1</option>
                                                            <option value="2" selected>2</option>
                                                            <option value="3">3</option>
                                                            <option value="4">4</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-xs-4">
                                                    <div class="form-group">
                                                        <label>声音通道</label>
                                                        <select name="voiceChannel" class="form-control">
                                                            <option value="0">第一通道</option>
                                                            <option value="1">第二通道</option>
                                                            <option value="2">第三通道</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row" id="ledPlayThree" style="display: none">
                                                <div class="col-xs-12">
                                                    <table class="table table-striped table-bordered table-hover dataTables-example" id="ledTable" style="background-color:#ffffff;width:100%;">
                                                        <thead>
                                                        <tr style="color: #767676">
                                                            <th>显示位置</th>
                                                            <th>显示内容</th>
                                                            <th>移动方向</th>
                                                            <th>移动速度</th>
                                                            <th>播报内容</th>
                                                            <th>声音大小</th>
                                                            <th>操作</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody></tbody>
                                                    </table>
                                                </div>

                                            </div>

                                            <div class="row" id="ledPlayFour" style="display: none">
                                                <div class="col-xs-12">
                                                    <table class="table table-striped table-bordered table-hover dataTables-example" id="ledTableFour" style="background-color:#ffffff;width:100%;">
                                                        <thead>
                                                        <tr style="color: #767676">
                                                            <th>显示位置</th>
                                                            <th>显示内容</th>
                                                            <th>移动方向</th>
                                                            <th>移动速度</th>
                                                            <th>播报内容</th>
                                                            <th>声音大小</th>
                                                            <th>操作</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody></tbody>
                                                    </table>
                                                </div>

                                            </div>
                                        </form>
                                        <div class="row">
                                            <div class="col-xs-12" style="text-align: right;">
                                               <button id="saveLedBtn" class="btn btn-primary">保存到设备</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            <div id="tab-7" class="tab-pane">
                                    <div class="panel-body">
                                        <form id="audioForm">
                                            <input name="deviceId" type="hidden" value="">
                                            <div class="row">
                                                <div class="col-xs-6">
                                                    <div class="form-group">
                                                        <label>串口号</label>
                                                        <select name="deviceChannelNo" class="form-control">
                                                            <option value="1">1</option>
                                                            <option value="2">2</option>
                                                            <option value="3" selected>3</option>
                                                            <option value="4">4</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-xs-6">
                                                    <div class="form-group">
                                                        <label>声音通道</label>
                                                        <select name="voiceChannel" class="form-control">
                                                            <option value="0">第一通道</option>
                                                            <option value="1">第二通道</option>
                                                            <option value="2">第三通道</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-xs-12" >
                                                    <table class="table table-striped table-bordered table-hover dataTables-example" id="audioTable" style="background-color:#ffffff;width:100%;">
                                                        <thead>
                                                        <tr style="color: #767676">
                                                            <th>播报模式</th>
                                                            <th>播报内容</th>
                                                            <th>声音大小</th>
                                                            <th>操作</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody></tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </form>
                                        <div class="row">
                                            <div class="col-xs-12" style="text-align: right;">
                                                <button id="saveAudioBtn" class="btn btn-primary">保存到设备</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                        </div>
                    </div>
                </div>


            </div>
        </div>
    </div>
</div>

</body>

<!-- 全局js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/jquery.min.js?v=2.1.4"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- 自定义js -->
<script src="${pageContext.request.contextPath}/resources/admin/js/content.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/jsTree/jstree.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/chosen/chosen.jquery.js"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/resources/bootstrap-switch/js/bootstrap-switch.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/prettyfile/bootstrap-prettyfile.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/ionRangeSlider/ion.rangeSlider.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/timepicker/bootstrap-timepicker.min.js" ></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/ipark_ocx.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/deviceManage.js"></script>
</html>
