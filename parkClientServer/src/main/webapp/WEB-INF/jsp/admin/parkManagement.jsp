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
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/dataTables/dataTables.bootstrap.css"
          rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.css"
          rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.print.css"
          rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/custom.css" rel="stylesheet">
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="tableCss1">
<div class=" wrapper-content"style="height:100%;background-color: #FFFFFF" >
    <div class="col-xs-12" style="margin-top: 0px">
        <div style="margin-top: 15px;margin-bottom: 15px;padding:0px;float: left" >
            <button class="btn btn-success " type="button" id="parkInformation" ><span class="bold">车场信息</span>

            </button>

            <button class="btn btn-primary " type="button" id="carRoadInformation" ><span class="bold">车道信息</span>

            </button>

            <button class="btn btn-success " type="button" onclick="addFunction()" id="creatPark"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;<span class="bold">创建停车场</span>
            </button>

            <button class="btn btn-info "  data-toggle="modal" data-target="#addCarRoadModal" style="display: none" type="button" id="creatRoad"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;<span class="bold">创建车道</span>
            </button>

            <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>

        </div>
        <%--<div style="margin-top: 15px;margin-bottom: 15px;padding:0px;float: left" >
            <button class="btn btn-info " type="button"><i class="glyphicon glyphicon-pencil"></i>&nbsp;&nbsp;<span class="bold">修改</span>
            </button>
        </div>
        <div style="margin-top: 15px;margin-bottom: 15px;padding:0px;float: left;" >
            <button class="btn btn-info " type="button" style="width: 34px;"><i class="glyphicon glyphicon-chevron-down"></i>&nbsp;&nbsp;<span class="bold"></span>
            </button>
        </div>
        <div style="margin-top: 15px;margin-bottom: 15px;padding:0px;float: left" >
            <button class="btn btn-info " type="button" style="background-color: #FFAB99;border-color:#FFAB99 "><i class="fa fa-close"></i>&nbsp;&nbsp;<span class="bold">删除</span>
            </button>
        </div>--%>
    </div>
    <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;" id="parkTable">
        <table class="table table-striped table-bordered table-hover dataTables-example" id="autoTable" style="background-color:#ffffff;width:100%;">
            <thead>
            <tr style="color: #767676">
                <th>停车场名称</th>
                <th>车位总数</th>
                <th>是否关闭</th>
                <th>所属车场</th>
                <th>操作</th>
                <th>所属单位ID</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>

    <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;display: none" id="carRoadTable">
        <table class="table table-striped table-bordered table-hover dataTables-example" id="roadTable" style="background-color:#ffffff;width:100%;">
            <thead>
            <tr style="color: #767676">
                <th>车道名称</th>
                <th>车道类型</th>
                <th>所属车场</th>
                <th>所属岗亭</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>

    <div class="modal inmodal fade" id="descModal" tabindex="-1" role="dialog" aria-hidden="true">

            <div class="modal-dialog modal-lg"><!--框框-->
                <div class="modal-content"><!--内容-->
                    <div class="modal-header">
                        <h4 class="modal-title">车场详情</h4>
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                            <!--×-->
                        </button>
                    </div>

                    <div class="modal-body">
                        <form id="parkForm">
                            <input name="carparkId" type="hidden" value="">
                            <div class="row">
                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>车场名称</label>
                                        <input name="carparkName" class="form-control" value="" id="carparkName">
                                    </div>
                                    <div class="form-group">
                                        <label>总车位数</label>
                                        <input name="totalCarSpace" class="form-control" value="" id="totalCarSpace" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                                    </div>
                                    <div class="form-group">
                                        <label>车场试运行（未匹配出场收费为0）</label>
                                        <select class="form-control m-b" name="isTestRunning" id="isTestRunning">
                                            <option value="yes">是</option>
                                            <option value="no">否</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>过期车辆是否禁行</label>
                                        <%--<input name="isOverdueAutoOpen" class="form-control" value="" >--%>
                                        <select class="form-control m-b" name="isOverdueAutoOpen" id="isOverdueAutoOpen">
                                            <option value="yes">是</option>
                                            <option value="no">否</option>
                                        </select>
                                    </div>
                                    <%--<div class="form-group">--%>
                                        <%--<label>纬度</label>--%>
                                        <%--<input name="lat" class="form-control" value="" id="lat">--%>
                                    <%--</div>--%>
                                </div>

                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>大车场过路时间</label>
                                        <input name="passTimeWhenBig" class="form-control" value="" id="passTimeWhenBig" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                                    </div>
                                    <div class="form-group">
                                        <label>剩余车位数</label>
                                        <input name="availableCarSpace" class="form-control" value="" id="availableCarSpace" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                                    </div>
                                    <div class="form-group">
                                        <label>是否封闭车场</label>
                                        <%--<input name="isClose" class="form-control" value="" >--%>
                                        <select class="form-control m-b" name="isClose" id="isClose" onchange="isCloseCarpark()">
                                            <option value="yes">是</option>
                                            <option value="no">否</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>纬度</label>
                                        <input name="lat" class="form-control" value="" id="editLat">
                                    </div>
                                </div>

                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>归属车场</label>
                                        <input name="ownCarparkName" class="form-control" value="" id="ownCarparkName" disabled>
                                    </div>
                                    <div class="form-group">
                                        <label>收费0元是否自动放行</label>
                                        <%--<input name="isAutoOpen" class="form-control" value="" >--%>
                                        <select class="form-control m-b" name="isAutoOpen" id="isAutoOpen">
                                            <option value="yes">是</option>
                                            <option value="no">否</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>临界值类型</label>
                                        <select class="form-control m-b" name="closeType" id="closeType">
                                            <option value="yes">剩余总车位临界值</option>
                                            <option value="no">剩余大场车位临界值</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>经度</label>
                                        <input name="lon" class="form-control" value="" id="editLon">
                                    </div>

                                </div>

                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>是否纳入父场计算</label>
                                        <%--<input name="ifIncludeCaculate" class="form-control" value="" >--%>
                                        <select class="form-control m-b" name="ifIncludeCaculate" id="ifIncludeCaculate">
                                            <option value="yes">是</option>
                                            <option value="no">否</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>LED开始提示剩余天数</label>
                                        <input name="ledMemberCriticalValue" class="form-control" value="" id="ledMemberCriticalValue" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                                    </div>
                                    <div class="form-group">
                                        <label>封闭车场临界值</label>
                                        <input name="criticalValue" class="form-control" value="" id="criticalValue" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                                    </div>


                                </div>

                            </div>

                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-white" onclick="changeFunction()" id="addForm">提交</button>
                        <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                    </div>
                </div>
            </div>
        </div>

    <div class="modal inmodal fade" id="addCarparkModal" tabindex="-1" role="dialog" aria-hidden="true">

        <div class="modal-dialog modal-lg"><!--框框-->
            <div class="modal-content"><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title">添加车场</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>

                <div class="modal-body" >
                    <form id="addParkForm">
                        <input name="carparkId" type="hidden" value="">
                        <div class="row">
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>车场名称</label>
                                    <input name="carparkName" class="form-control" value="" id="addCarparkName">
                                </div>
                                <div class="form-group">
                                    <label>总车位数</label>
                                    <input name="totalCarSpace" class="form-control"  id="addTotalCarSpace" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                                </div>
                                <div class="form-group">
                                    <label>车场试运行（未匹配出场收费为0）</label>
                                    <%--<input name="isTestRunning" class="form-control" value="">--%>
                                    <select class="form-control m-b" name="isTestRunning" id="addIsTestRunningg">
                                        <option value="yes">是</option>
                                        <option value="no">否</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>过期车辆是否自动放行</label>
                                    <%--<input name="isOverdueAutoOpen" class="form-control" value="" >--%>
                                    <select class="form-control m-b" name="isOverdueAutoOpen" id="addIsOverdueAutoOpen">
                                        <option value="yes">是</option>
                                        <option value="no">否</option>
                                    </select>
                                </div>

                            </div>

                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>大车场过路时间</label>
                                    <input name="passTimeWhenBig" class="form-control"  id="addPassTimeWhenBig" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                                </div>
                                <div class="form-group">
                                    <label>剩余车位数</label>
                                    <input name="availableCarSpace" class="form-control" value="" id="addAvailableCarSpace" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                                </div>
                                <div class="form-group">
                                    <label>是否封闭车场</label>
                                    <%--<input name="isClose" class="form-control" value="" >--%>
                                    <select class="form-control m-b" name="isClose" id="addIsClose" onchange="addIsCloseCarpark()">
                                        <option value="yes">是</option>
                                        <option value="no">否</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>纬度</label>
                                    <input name="lat" class="form-control" value="" id="addLat">
                                </div>
                            </div>

                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>归属车场</label>
                                    <%--<input name="ownCarparkName" class="form-control" value="" >--%>
                                    <select class="form-control m-b" name="ownCarparkName" id="addOwnCarparkName"></select>
                                </div>
                                <div class="form-group">
                                    <label>收费0元是否自动放行</label>
                                    <%--<input name="isAutoOpen" class="form-control" value="" >--%>
                                    <select class="form-control m-b" name="isAutoOpen" id="addIsAutoOpen">
                                        <option value="yes">是</option>
                                        <option value="no">否</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>临界值类型</label>
                                    <select class="form-control m-b" name="addCloseType" id="addCloseType">
                                        <option value="yes">剩余总车位临时值</option>
                                        <option value="no">剩余大场车位临界值</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>经度</label>
                                    <input name="lon" class="form-control" value="" id="addLon">
                                </div>

                            </div>

                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>LED开始提示剩余天数</label>
                                    <input name="ledMemberCriticalValue" class="form-control" value="" id="addLedMemberCriticalValue" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                                </div>
                                <div class="form-group">
                                    <label>是否纳入父场计算</label>
                                    <%--<input name="ifIncludeCaculate" class="form-control" value="" >--%>
                                    <select class="form-control m-b" name="ifIncludeCalculate" id="addIfIncludeCalculate">
                                        <option value="yes">是</option>
                                        <option value="no">否</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>封闭车场临界值</label>
                                    <input name="criticalValue" class="form-control" value="" id="addCriticalValue" onkeypress="return (/[\d.]/.test(String.fromCharCode(event.keyCode)))">
                                </div>

                            </div>

                        </div>

                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-white" onclick="saveParkInfo()">保存</button>
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal inmodal" id="deleteModal" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content animated fadein">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title">确认删除</h4>
                    <small class="font-bold"></small>
                </div>
                <div class="modal-body">
                    <p><strong>如果删除车场，该车场下属所有车道和设备都会被删除，请谨慎操作</strong></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                    <button type="button"  id="deleteBtn" onclick="deleteCarparkInfo()" class="btn btn-primary">确认删除</button>
                </div>
            </div>
        </div>
    </div>

<%--创建车道--%>
    <div class="modal inmodal fade" id="addCarRoadModal" tabindex="-1" role="dialog" aria-hidden="true"  >

        <div class="modal-dialog modal-lg"><!--框框-->
            <div class="modal-content"><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title" id="carRoadTitle">添加车道</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>

                <div class="modal-body">
                    <%--<input name="carparkId" type="hidden" value="">--%>
                    <form id="carRoadForm">
                        <div class="row">
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>车道名称</label>
                                    <input name="carRoadName" class="form-control" value="" id="carRoadName" disabled>
                                </div>
                            </div>
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>归属车场</label>
                                    <select class="form-control m-b" name="carRoadParkId" id="carRoadParkId"
                                            disabled></select>
                                </div>
                            </div>
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>车道类型</label>
                                    <select class="form-control m-b" name="carRoadType" id="carRoadType" disabled >
                                        <option value="0" selected>入口</option>
                                        <option value="1">出口</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>所属岗亭</label>
                                    <select class="form-control m-b" name="carRoadPostId" id="carRoadPostId"
                                            disabled></select>
                                </div>
                            </div>


                            <div class="col-xs-4" id="white">
                                <div class="c-content">
                                    <div class="w-content">
                                        <div class="checkbox no-margin">
                                            <label class=" i-checks " style="padding-left: 0px;">
                                                <input type="checkbox" class="tag1" id="whiteInput" >
                                            </label>
                                            <span class="icheckFont">白名单车模糊匹配（针对一位多车）</span>
                                        </div>
                                    </div>

                                    <div class="w-content">
                                        <div class="w-3">
                                            <div class="m-h">
                                                <span class="icheckFont"
                                                      style="position: relative;top: 80px;">关心匹配的字符</span>
                                            </div>
                                        </div>
                                        <div class="w-5">
                                            <div class="m-h">
                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">闽</span>
                                                        <label class=" i-checks input-p">
                                                            <input type="checkbox" id="min" class="min">
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">A</span>
                                                        <label class=" i-checks  input-p">
                                                            <input type="checkbox" id="a" class="a">
                                                        </label>

                                                    </div>
                                                </div>

                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">1</span>
                                                        <label class=" i-checks  input-p">
                                                            <input type="checkbox" id="1" class="white1">
                                                        </label>

                                                    </div>
                                                </div>

                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">2</span>
                                                        <label class=" i-checks  input-p">
                                                            <input type="checkbox" id="2" class="white2">
                                                        </label>

                                                    </div>
                                                </div>

                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">3</span>
                                                        <label class=" i-checks  input-p">
                                                            <input type="checkbox" id="3" class="white3">
                                                        </label>

                                                    </div>
                                                </div>

                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">4</span>
                                                        <label class=" i-checks  input-p">
                                                            <input type="checkbox" id="4" class="white4">
                                                        </label>

                                                    </div>
                                                </div>

                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">5</span>
                                                        <label class=" i-checks  input-p">
                                                            <input type="checkbox" id="5" class="white5">
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="w-content" style=" margin-top: 20px;">
                                        <span class="s-t ">最小匹配位数</span>
                                        <div class="col-sm-2">
                                            <input type="text" style="width: 50px;margin-top:0px;height:33px" id="minwhite">
                                        </div>
                                        <div class="col-sm-2 col-sm-offset-1"  id="addt">
                                            <a class="btn btn-success" >智能纠错设置</a>
                                        </div>
                                    </div>
                                    <div class="w-content" style=" margin-top: 20px;margin-left: 142px">
                                        <div class="col-sm-2 col-sm-offset-1"  id="carnoCorrect">
                                            <a class="btn btn-success" >车牌纠错设置</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%--临时车--%>
                            <%--<div class="col-xs-4" id="temporaryCar" style="visibility: hidden" >
                                <div class="c-content">
                                    <div class="w-content">
                                        <div class="checkbox no-margin">
                                            <label class="i-checks " style="padding-left: 0px;">
                                                <input type="checkbox" class="tag2" id="tempInput">
                                            </label>
                                            <span class="icheckFont">临时车无匹配出场模糊匹配</span>
                                        </div>
                                    </div>

                                    <div class="w-content">
                                        <div class="w-3">
                                            <div class="m-h">
                                                <span class="icheckFont"
                                                      style="position: relative;top: 80px;">关心匹配的字符</span>
                                            </div>
                                        </div>
                                        <div class="w-5">
                                            <div class="m-h">
                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">闽</span>
                                                        <label class="  input-p i-checks  ">
                                                            <input type="checkbox" id="min_temp" class="min_temp">
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">A</span>
                                                        <label class="  input-p i-checks  ">
                                                            <input type="checkbox" id="a_temp" class="a_temp">
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">1</span>
                                                        <label class="  input-p i-checks  ">
                                                            <input type="checkbox" id="1_temp" class="temp1">
                                                        </label>
                                                    </div>
                                                </div>

                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">2</span>
                                                        <label class="  input-p i-checks  ">
                                                            <input type="checkbox" id="2_temp" class="temp2">
                                                        </label>

                                                    </div>
                                                </div>

                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">3</span>
                                                        <label class="  input-p i-checks  ">
                                                            <input type="checkbox" id="3_temp" class="temp3">
                                                        </label>

                                                    </div>
                                                </div>

                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">4</span>
                                                        <label class="  input-p i-checks  ">
                                                            <input type="checkbox" id="4_temp" class="temp4">
                                                        </label>

                                                    </div>
                                                </div>

                                                <div class="w-1">
                                                    <div class="checkbox text-center">
                                                        <span class="icheckFont">5</span>
                                                        <label class="  input-p i-checks  ">
                                                            <input type="checkbox" id="5_temp" class="temp5">
                                                        </label>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="w-content" style=" margin-top: 20px;">
                                        <span class="s-t ">最小匹配位数</span>
                                        <div class="col-sm-2">
                                            <input type="text" style="width: 50px;margin-top:0px;height:33px" id="mintemp">
                                        </div>


                                        <div class="col-sm-4">

                                            <div class="col-sm-2 col-sm-offset-3"  id="addt2">
                                                <a class="btn btn-success" >智能纠错设置</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>--%>


                            <%--白名单--%>
                         <div class="col-xs-2"   style="margin-left: 0% !important;" id="smartError">
                            <div class="c-content">
                                <div class="w-content">
                                    <div class="checkbox no-margin">
                                        <span class="icheckFont">智能纠错设置</span>
                                    </div>
                                </div>

                                <div class="w-content">
                                    <form class="form-horizontal " id="taddForm" novalidate="novalidate" >
                                        <input id="flagadd" type="hidden" value="">
                                        <div class="form-group" style="padding: 30px;margin-right: 25px;">
                                            <div class="col-sm-2">
                                                <a class="btn btn-success" id="taddCharacter">添加字符对</a>
                                            </div>
                                            <div class="col-sm-2 col-sm-offset-4"  id="returnbutton">
                                                <a class="btn btn-success" >配置结束并返回</a>
                                            </div>
                                        </div>
                                    </form>
                                    <form class="form-horizontal " id="tmatchForm" novalidate="novalidate" style="display: none">
                                        <div class="form-group">
                                            <div class="col-sm-2 ">
                                                <input type="text" style="width: 50px;margin-top:1px;height:33px" id="one">
                                            </div>
                                            <label class="col-sm-2 control-label" ><h5>匹配成：</h5></label>
                                            <div class="col-sm-2">
                                                <input type="text" style="width: 50px;margin-top:1px;height:33px" id="two">
                                            </div>
                                            <div class="col-sm-2" >
                                                <a class="btn btn-success"  id="savepipei">确定</a>
                                            </div>
                                            <div class="col-sm-2">
                                                <a class="btn btn-success" id="tmatchReurn">清空</a>
                                            </div>

                                        </div>
                                    </form>

                                    <div style="width:300px; height:100px; overflow:scroll;">
                                    <table class="table table-striped table-bordered table-hover "  >
                                        <thead>
                                        <tr>
                                            <th>
                                                字符1
                                            </th>
                                            <th>
                                                匹配
                                            </th>
                                            <th>
                                                字符2
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody id="whieteTable">
                                        </tbody>
                                    </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                         <div class="col-xs-2"   style="margin-left: 195px !important;" id="allCarnoCorrect">
                             <div class="c-content">
                                    <div class="w-content">
                                        <div class="checkbox no-margin">
                                            <span class="icheckFont">车牌纠错设置</span>
                                        </div>
                                    </div>

                                    <div class="w-content">
                                        <form class="form-horizontal " id="taddFormAllCarno" novalidate="novalidate" >
                                            <input id="flagaddAllCarno" type="hidden" value="">
                                            <div class="form-group" style="padding: 30px;margin-right: 25px;">
                                                <div class="col-sm-2">
                                                    <a class="btn btn-success" id="taddCharacterAllCarno">添加车牌</a>
                                                </div>
                                                <div class="col-sm-2 col-sm-offset-4"  id="returnbuttonAllCarno">
                                                    <a class="btn btn-success" >配置结束并返回</a>
                                                </div>
                                            </div>
                                        </form>
                                        <form class="form-horizontal " id="tmatchFormAllCarno" novalidate="novalidate" style="display: none">
                                            <div class="form-group">
                                                <div class="col-sm-2 ">
                                                    <input type="text" style="width: 50px;margin-top:1px;height:33px" id="oneAllCarno">
                                                </div>
                                                <label class="col-sm-2 control-label" ><h5>匹配成：</h5></label>
                                                <div class="col-sm-2">
                                                    <input type="text" style="width: 50px;margin-top:1px;height:33px" id="twoAllCarno">
                                                </div>
                                                <div class="col-sm-2" >
                                                    <a class="btn btn-success"  id="saveAllCarno">确定</a>
                                                </div>
                                                <div class="col-sm-2">
                                                    <a class="btn btn-success" id="tmatchReurnAllCarno">清空</a>
                                                </div>

                                            </div>
                                        </form>

                                        <div style="width:300px; height:100px; overflow:scroll;">
                                            <table class="table table-striped table-bordered table-hover "  >
                                                <thead>
                                                <tr>
                                                    <th>
                                                        车牌1
                                                    </th>
                                                    <th>
                                                        匹配
                                                    </th>
                                                    <th>
                                                        车牌2
                                                    </th>
                                                </tr>
                                                </thead>
                                                <tbody id="whieteTableAllCarno">
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                             </div>
                          </div>



                        </div>

                    </form>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-white" id="saveCarRoad">保存</button>
                        <button type="button" class="btn btn-white" id="eidtCarRoad">提交</button>
                        <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal inmodal" id="deleteCarRoadModal" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content animated fadeIn">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title">确认删除</h4>
                    <small class="font-bold"></small>
                </div>
                <div class="modal-body">
                    <p><strong>如果删除车道，该车道下属所有设备都会被删除，请谨慎操作</strong></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                    <button type="button"  id="deleteCarRaodBtn"  class="btn btn-primary">确认删除</button>
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
    <!-- icheck-->
    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/iCheck/icheck.min.js"></script>
      <script src="${pageContext.request.contextPath}/resources/script/admin/carRoad.js"></script>
    <script src="${pageContext.request.contextPath}/resources/script/admin/parkManagenment.js"></script>

    <script>
        /*$('#carRoadType').change(function(){
            var carloadType=$(this).children('option:selected').val();

            if(carloadType==0){
                //$('#temporaryCar').hide();
                $('#white').show();

            }else {
                $('#white').show();
                //$('#temporaryCar').show();
            }
        });*/
        $('#smartError').hide();
        $('#allCarnoCorrect').hide();

        $('#addt').unbind("click").click(function () {//弹出临时车模态窗
            $("#flagadd").val(1);

            $('#smartError').show();
        });
        $('#carnoCorrect').unbind("click").click(function () {//弹出临时车模态窗
            $("#flagadd").val(2)
            $('#allCarnoCorrect').show();
        });

        $('#returnbutton').unbind("click").click(function () {//弹出临时车模态窗
            $('#smartError').hide();
        });
        $('#returnbuttonAllCarno').unbind("click").click(function () {//弹出临时车模态窗
            $('#allCarnoCorrect').hide();
        });

        //添加字符对
        $('#taddCharacter').unbind("click").click(function () {//显示添加匹配字符功能 按钮
            $('#taddForm').hide();
            $('#tmatchForm').show();

        });

        $('#taddCharacterAllCarno').unbind("click").click(function () {//显示添加匹配字符功能 按钮
            $('#taddFormAllCarno').show();
            $('#tmatchFormAllCarno').show();

        });

        //复选框样式添加
        $('.i-checks').iCheck({
            checkboxClass: 'icheckbox_square-green ',
            radioClass: 'iradio_square-green'
        });

        //白名单弹窗按钮功能
        $('#waddCharacter').unbind("click").click(function () {//显示添加匹配字符功能 按钮
            $('#waddForm').hide();
            $('#wmatchForm').show();

        });

        $('#wmatchReurn').unbind("click").click(function () {//匹配 返回按钮
            $('#waddForm').show();
            $('#wmatchForm').hide();
        });





    </script>

</body>
</html>
