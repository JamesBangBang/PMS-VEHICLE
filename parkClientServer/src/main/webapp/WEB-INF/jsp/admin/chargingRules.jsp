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
    <link href="${pageContext.request.contextPath}/resources/timepicker/bootstrap-timepicker.min.css" rel="stylesheet">
    <style>
        .tinyInput{
            margin-left: 2px;
            margin-right: 2px;
            width:36px;
            height: 23px;
            line-height:23px;
        }
        .content{
            text-align: center;
        }
        .miniInput{
            margin-left: 3px;
            margin-right: 3px;
            width:48px;
            height: 20px;
        }
        .colLine{
            height: 38px;
            line-height: 38px;
            border: 1px solid #E5E5E5;
            padding-left: 5px;
            padding-right: 5px;
        }
        .colLine2{
            height: 76px;
            line-height: 76px;
            border: 1px solid #E5E5E5;
            padding-left: 0px;
            padding-right: 0px;
        }
        .ruleContainer{
            border:1px solid #E5E5E5;
            padding: 0px;
        }
        .ruleContainer .row{
            padding-top: 0px;
            padding-bottom: 0px;
        }
        .nav>li>a:focus, .nav>li>a:hover{
            color:#E5E5E5;
        }
        .colHidden{
            display: none;
        }
        .timepick{
            height: 23px;
            width:45px;
        }
        .timeDuration{
            height:37px;
            line-height: 37px;
        }
        .timeDurationTop{
            border-bottom: 1px solid #E5E5E5;
        }
    </style>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body>
<div class=" wrapper-content"style="height:100%;background-color: #FFFFFF;padding:5px" >
    <div class="row">
        <div class="col-xs-12">
            <button id="addRule" class="btn btn-primary">添加收费规则</button>
            <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;">
            <table class="table table-striped table-bordered table-hover dataTables-example" id="chargingRules" style="background-color:#ffffff;">
                <thead>
                <tr style="color: #767676">
                    <th>规则名称</th>
                    <th>计费方式</th>
                    <th>车场</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="modal inmodal fade" id="editModal" tabindex="-1" role="dialog" aria-hidden="true">

        <div class="modal-dialog " style="width: 1200px;"><!--框框-->
            <div class="modal-content"><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title">规则配置</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>

                <div class="modal-body">

                    <input name="id" type="hidden" value="">
                    <div class="tabs-container">
                        <ul class="nav nav-tabs">
                            <li class="active">
                                <a data-toggle="tab" href="#tab-1" aria-expanded="false">规则信息</a>
                            </li>
                            <li class="">
                                <a data-toggle="tab" href="#tab-2" aria-expanded="true">收费信息</a>
                            </li>
                        </ul>
                        <div class="tab-content">

                            <div id="tab-1" class="tab-pane active">
                                <form id="memberForm">
                                <div class="panel-body">
                                    <div class="row">
                                        <div class="col-xs-6">
                                            <div class="form-group">
                                                <label>规则名称</label>
                                                <input id="kindName" name="kindName" class="form-control">
                                            </div>
                                            <div class="form-group">
                                                <label>备注信息</label>
                                                <input id="memo" name="memo" class="form-control">
                                            </div>
                                        </div>
                                        <div class="col-xs-6">
                                            <div class="form-group">
                                                <label>是否纳入车位统计</label>
                                                <select id="isStatistic" name="isStatistic" class="form-control">
                                                    <option value="1" selected>是</option>
                                                    <option value="0">否</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                </form>
                            </div>
                            <div id="tab-2" class="tab-pane">
                                <div class="panel-body">
                                    <div class="row">

                                        <div class="col-xs-6">
                                            <div class="form-group">
                                                <label>收费方式</label>
                                                <select id="packageKind" name="packageKind" class="form-control">
                                                    <%--<option value="0">包月收费</option>
                                                    <option value="1">包次收费</option>
                                                    <option value="2">临时计费</option>
                                                    <option value="-3">免费</option>--%>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-xs-6">
                                            <div id="countPackage" class="form-group" style="display: none;">
                                                <label>每次</label>
                                                <input type="number" id="countPackageValue" name="countPackage" class="form-control">
                                            </div>
                                            <div id="monthPackage" class="form-group" style="display: none;">
                                                <label>每月</label>
                                                <input id="monthPackageValue" name="monthPackage" class="form-control">
                                            </div>
                                            <div id="ruleTemplate" class="form-group" style="display: none;">
                                                <label>计费模板</label>
                                                <select id="ruleTemplateSelect" name="packageChildKind" class="form-control">
                                                    <option value="0">按时长（相对时段）计费</option>
                                                    <option value="1">按次（相对时段）计费</option>
                                                    <option value="2">按时长（全天）计费</option>
                                                    <option value="3">按时长（绝对时段）计费</option>
                                                    <option value="4">按次（绝对时段）计费</option>
                                                    <option value="5">按次（绝对时段）计费2</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="ruleContainer" class="row ruleContainer">
                                        <%--模板1开始--%>
                                        <form id="formTemplate1">
                                            <div class="col-xs-2 colHidden template1">
                                                <div class="row">
                                                    <div class="col-xs-12 content colLine">
                                                        <label>车型</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <label>小型车</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <label>大型车</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <label>其他</label>
                                                    </div>
                                                </div>
                                                </div>
                                                <div class="col-xs-2 colHidden template1">
                                                    <div class="row">
                                                        <div class="col-xs-12 content colLine">
                                                            <label>免费时长</label>
                                                        </div>
                                                        <div class="col-xs-12 content colLine">
                                                            <input type="number" class="tinyInput" name="smallFreeDuration" value="15">分钟
                                                        </div>
                                                        <div class="col-xs-12 content colLine">
                                                            <input type="number" class="tinyInput" name="bigFreeDuration" value="15">分钟
                                                        </div>
                                                        <div class="col-xs-12 content colLine">
                                                            <input type="number" class="tinyInput" name="otherFreeDuration" value="15">分钟
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-xs-2 colHidden template1">
                                                    <div class="row">
                                                    <div class="col-xs-12 content colLine">
                                                        <label>时长1</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="smallT112MaxFeeCheck" type="checkbox" name="smallT112MaxFeeCheck">
                                                        12小时内不超过<input type="number" class="tinyInput" name="smallFeeLimit" value="5">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="bigT112MaxFeeCheck" type="checkbox" name="bigT112MaxFeeCheck">
                                                        12小时内不超过<input type="number" class="tinyInput" name="bigFeeLimit" value="5">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="otherT112MaxFeeCheck" type="checkbox" name="otherT112MaxFeeCheck">
                                                        12小时内不超过<input type="number" class="tinyInput" name="otherFeeLimit" value="5">元
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-2 colHidden template1">
                                                <div class="row">
                                                    <div class="col-xs-12 content colLine">
                                                        <label>时长2</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="smallT124MaxFeeCheck" type="checkbox" name="smallT124MaxFeeCheck">
                                                        24小时内不超过<input type="number" class="tinyInput" name="smallFeeLimitEx" value="10">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="bigT124MaxFeeCheck" type="checkbox" name="bigT124MaxFeeCheck">
                                                        24小时内不超过<input type="number" class="tinyInput" name="bigFeeLimitEx" value="10">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="otherT124MaxFeeCheck" type="checkbox" name="otherT124MaxFeeCheck">
                                                        24小时内不超过<input type="number" class="tinyInput" name="otherFeeLimitEx" value="10">元
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4 colHidden template1">
                                                <div class="row">
                                                    <div class="col-xs-12 content colLine">
                                                        <label>收费标准</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        前<input type="number" class="tinyInput" name="smallFirstTime" value="2">小时
                                                        <input type="number" class="tinyInput" name="smallAddFee" value="2">元,之后每
                                                        <input type="number" class="tinyInput" name="smallFirstTimeEx" value="2">小时
                                                        <input type="number" class="tinyInput"name="smallAddFeeEx" value="2">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        前<input type="number" class="tinyInput" name="bigFirstTime" value="2">小时
                                                        <input type="number" class="tinyInput" name="bigAddFee" value="2">元,之后每
                                                        <input type="number" class="tinyInput" name="bigFirstTimeEx" value="2">小时
                                                        <input type="number" class="tinyInput" name="bigAddFeeEx" value="2">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        前<input type="number" class="tinyInput" name="otherFirstTime" value="2">小时
                                                        <input type="number" class="tinyInput" name="otherAddFee" value="2">元,之后每
                                                        <input type="number" class="tinyInput" name="otherFirstTimeEx" value="2">小时
                                                        <input type="number" class="tinyInput" name="otherAddFeeEx" value="2">元
                                                    </div>
                                                </div>
                                            </div>
                                            </form>
                                        <%--模板1结束--%>
                                        <div class="col-xs-1 colHidden template2 template3">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>车型</label>
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    <label>小型车</label>
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    <label>大型车</label>
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    <label>其他</label>
                                                </div>
                                            </div>
                                        </div>
                                        <%--模板2开始--%>
                                         <form id="formTemplate2">
                                        <div class="col-xs-1 colHidden template2">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>免费时长</label>
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    <input type="number" class="tinyInput" name="smallFreeDuration" value="15">分钟
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    <input type="number" class="tinyInput" name="bigFreeDuration" value="15">分钟
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    <input type="number" class="tinyInput" name="otherFreeDuration" value="15">分钟
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-3 colHidden template2">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>时段1</label>
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    前<input id="smallT2duration1" type="number" class="tinyInput" name="smallFirstTime" value="2">小时,
                                                    收费<input type="number" class="tinyInput" name="smallFirstTimeFee" value="5">元
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    前<input id="bigT2duration1" type="number" class="tinyInput" name="bigFirstTime" value="2">小时,
                                                    收费<input type="number" class="tinyInput" name="bigFirstTimeFee" value="5">元
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    前<input id="otherT2duration1" type="number" class="tinyInput" name="otherFirstTime" value="2">小时,
                                                    收费<input type="number" class="tinyInput" name="otherFirstTimeFee" value="5">元
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-3 colHidden template2">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>时段2</label>
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    超过<span id="smallT2duration1_show">2</span>小时,
                                                    收费<input type="number" class="tinyInput" name="smallSecondTimeAfterGapFee" value="10">元
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    超过<span id="bigT2duration1_show">2</span>小时,
                                                    收费<input type="number" class="tinyInput" name="bigSecondTimeAfterGapFee" value="10">元
                                                </div>
                                                <div class="col-xs-12 content colLine">
                                                    超过<span id="otherT2duration1_show">2</span>小时,
                                                    收费<input type="number" class="tinyInput" name="otherSecondTimeAfterGapFee" value="10">元
                                                </div>
                                            </div>
                                        </div>
                                            <div class="col-xs-2 colHidden template2">
                                                <div class="row">
                                                    <div class="col-xs-12 content colLine">
                                                        <label>24小时内限收一次</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="T2smallIsFeeSection" type="checkbox" name="smallIsFeeSection">
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="T2bigIsFeeSection" type="checkbox" name="bigIsFeeSection">
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="T2otherIsFeeSection" type="checkbox" name="otherIsFeeSection">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-2 colHidden template2">
                                                <div class="row">
                                                    <div class="col-xs-12 content colLine">
                                                        <label>时段3</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        每超过24小时,
                                                        增收<input type="number" class="tinyInput" name="smallAddFee" value="20">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        每超过24小时,
                                                        增收<input type="number" class="tinyInput" name="bigAddFee" value="20">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        每超过24小时,
                                                        增收<input type="number" class="tinyInput" name="otherAddFee" value="20">元
                                                    </div>
                                                </div>
                                            </div>
                                         </form>
                                        <%--模板2结束--%>
                                        <%--模板3开始--%>
                                        <form id="formTemplate3">
                                            <div class="col-xs-1 colHidden template3">
                                                <div class="row">
                                                    <div class="col-xs-12 content colLine">
                                                        <label>免费时长</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input type="number" class="tinyInput" name="smallFreeDuration" value="15">分钟
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input type="number" class="tinyInput" name="bigFreeDuration" value="15">分钟
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input type="number" class="tinyInput" name="otherFreeDuration" value="15">分钟
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-xs-3 colHidden template3">
                                                <div class="row">
                                                    <div class="col-xs-12 content colLine">
                                                        <label>时段1</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        前<input id="smallT3duration1" type="number" class="tinyInput" name="smallFirstTime" value="2">小时,
                                                        每小时<input type="number" class="tinyInput" name="smallFirstTimeFee" value="5">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        前<input id="bigT3duration1" type="number" class="tinyInput" name="bigFirstTime" value="2">小时,
                                                        每小时<input type="number" class="tinyInput" name="bigFirstTimeFee" value="5">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        前<input id="otherT3duration1" type="number" class="tinyInput" name="otherFirstTime" value="2">小时,
                                                        每小时<input type="number" class="tinyInput" name="otherFirstTimeFee" value="5">元
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-3 colHidden template3">
                                                <div class="row">
                                                    <div class="col-xs-12 content colLine">
                                                        <label>时段2</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        超过<span id="smallT3duration1_show">2</span>小时,
                                                        每小时<input type="number" class="tinyInput" name="smallSecondTimeAfterGapFee" value="10">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        超过<span id="bigT3duration1_show">2</span>小时,
                                                        每小时<input type="number" class="tinyInput" name="bigSecondTimeAfterGapFee" value="10">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        超过<span id="otherT3duration1_show">2</span>小时,
                                                        每小时<input type="number" class="tinyInput" name="otherSecondTimeAfterGapFee" value="10">元
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-1 colHidden template3">
                                                <div class="row">
                                                    <div class="col-xs-12 content colLine">
                                                        <label>封顶限额</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="smallT3MaxFeeCheck" type="checkbox" name="smallT3MaxFeeCheck">
                                                        <input type="number" class="tinyInput" name="smallFeeLimit" value="50">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="bigT3MaxFeeCheck" type="checkbox" name="bigT3MaxFeeCheck">
                                                        <input type="number" class="tinyInput" name="bigFeeLimit" value="50">元
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <input id="otherT3MaxFeeCheck" type="checkbox" name="otherT3MaxFeeCheck">
                                                        <input type="number" class="tinyInput" name="otherFeeLimit" value="50">元
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-3 colHidden template3">
                                                <div class="row">
                                                    <div class="col-xs-12 content colLine">
                                                        <label>跨段收费</label>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">

                                                        <div style="float: left;width: 50px;">
                                                            <input id="smallT3AdditionalCheck" type="checkbox" name="smallT3AdditionalCheck"> 超过
                                                        </div>
                                                        <div class="bootstrap-timepicker" style="width: 48px;height: 25px;line-height: 25px;float: left;margin-top: 5px;">
                                                            <div class="input-group">
                                                                <input type="text" id="smallT3SpanTime" name="smallFeeStartTime" class="timepicker timepick" >
                                                            </div>
                                                        </div>
                                                        <div style="float: left;width: 90px;">
                                                            加收 <input type="number" class="tinyInput" name="smallAddFee" value="20">元
                                                        </div>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <div style="float: left;width: 50px;">
                                                            <input id="bigT3AdditionalCheck" type="checkbox" name="bigT3AdditionalCheck"> 超过
                                                        </div>
                                                        <div class="bootstrap-timepicker" style="width: 48px;height: 25px;line-height: 25px;float: left;margin-top: 5px;">
                                                            <div class="input-group">
                                                                <input type="text" id="bigT3SpanTime" name="bigFeeStartTime" class="timepicker timepick" >
                                                            </div>
                                                        </div>
                                                        <div style="float: left;width: 90px;">
                                                            加收 <input type="number" class="tinyInput" name="bigAddFee" value="20">元
                                                        </div>
                                                    </div>
                                                    <div class="col-xs-12 content colLine">
                                                        <div style="float: left;width: 50px;">
                                                            <input id="otherT3AdditionalCheck" type="checkbox" name="otherT3AdditionalCheck"> 超过
                                                        </div>
                                                        <div class="bootstrap-timepicker" style="width: 48px;height: 25px;line-height: 25px;float: left;margin-top: 5px;">
                                                            <div class="input-group">
                                                                <input type="text" id="otherT3SpanTime" name="otherFeeStartTime" class="timepicker timepick" >
                                                            </div>
                                                        </div>
                                                        <div style="float: left;width: 90px;">
                                                            加收 <input type="number" class="tinyInput" name="otherAddFee" value="20">元
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            </form>
                                        <%--模板3结束--%>
                                        <%--模板4开始--%>
                                        <form id="formTemplate4">
                                        <div class="col-xs-2 colHidden template4">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>车型</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <label>小型车</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <label>大型车</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <label>其他</label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-2 colHidden template4">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>时间段</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class="T4daytime timeDuration timeDurationTop">
                                                        <span class="T4dayTimeStart">07:00</span> ~ <span class="T4NightStart">19:00</span>
                                                    </div>
                                                    <div class="T4night timeDuration">
                                                        <span class="T4NightStart">19:00</span> ~ <span class="T4dayTimeStart">01:00</span>
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class="T4daytime timeDuration timeDurationTop">
                                                        <span class="T4dayTimeStart">07:00</span> ~ <span class="T4NightStart">19:00</span>
                                                    </div>
                                                    <div class="T4night timeDuration">
                                                        <span class="T4NightStart">19:00</span> ~ <span class="T4dayTimeStart">01:00</span>
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class="T4daytime timeDuration timeDurationTop">
                                                        <span class="T4dayTimeStart">07:00</span> ~ <span class="T4NightStart">19:00</span>
                                                    </div>
                                                    <div class="T4night timeDuration">
                                                        <span class="T4NightStart">19:00</span> ~ <span class="T4dayTimeStart">01:00</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-xs-2 colHidden template4">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>免费时长</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="smallFreeDuration" value="15">分钟
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="smallFreeDurationEx" value="15">分钟
                                                    </div>

                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="bigFreeDuration" value="15">分钟
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="bigFreeDurationEx" value="15">分钟
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="otherFreeDuration" value="15">分钟
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="otherFreeDurationEx" value="15">分钟
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-xs-2 colHidden template4">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>时段1</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        前<input id="smallT4duration1Time1" type="number" class="tinyInput" name="smallFirstTime" value="2">小时,
                                                        每小时<input type="number" class="tinyInput" name="smallFirstTimeFee" value="5">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        前<input id="smallT4duration1Time2" type="number" class="tinyInput" name="smallFirstTimeEx" value="2">小时,
                                                        每小时<input type="number" class="tinyInput" name="smallFirstTimeFeeEx" value="5">元
                                                    </div>

                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        前<input id="bigT4duration1Time1" type="number" class="tinyInput" name="bigFirstTime" value="2">小时,
                                                        每小时<input type="number" class="tinyInput" name="bigFirstTimeFee" value="5">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        前<input id="bigT4duration1Time2" type="number" class="tinyInput" name="bigFirstTimeEx" value="2">小时,
                                                        每小时<input type="number" class="tinyInput" name="bigFirstTimeFeeEx" value="5">元
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        前<input id="otherT4duration1Time1" type="number" class="tinyInput" name="otherFirstTime" value="2">小时,
                                                        每小时<input type="number" class="tinyInput" name="otherFirstTimeFee" value="5">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        前<input id="otherT4duration1Time2" type="number" class="tinyInput" name="otherFirstTimeEx" value="2">小时,
                                                        每小时<input type="number" class="tinyInput" name="otherFirstTimeFeeEx" value="5">元
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-2 colHidden template4">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>时段2</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        超过<span id="smallT4duration1Time1_show">2</span>小时,
                                                        每小时<input type="number" class="tinyInput" name="smallSecondTimeAfterGapFee" value="10">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        超过<span id="smallT4duration1Time2_show">2</span>小时,
                                                        每小时<input type="number" class="tinyInput" name="smallSecondTimeAfterGapFeeEx" value="10">元
                                                    </div>

                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        超过<span id="bigT4duration1Time1_show">2</span>小时,
                                                        每小时<input type="number" class="tinyInput" name="bigSecondTimeAfterGapFee" value="10">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        超过<span id="bigT4duration1Time2_show">2</span>小时,
                                                        每小时<input type="number" class="tinyInput" name="bigSecondTimeAfterGapFeeEx" value="10">元
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        超过<span id="otherT4duration1Time1_show">2</span>小时,
                                                        每小时<input type="number" class="tinyInput" name="otherSecondTimeAfterGapFee" value="10">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        超过<span id="otherT4duration1Time2_show">2</span>小时,
                                                        每小时<input type="number" class="tinyInput" name="otherSecondTimeAfterGapFeeEx" value="10">元
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-2 colHidden template4">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>封顶限额（以小型车为准）</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        <input id="smallT4MaxFeeCheckOne" type="checkbox" name="smallT4MaxFeeCheckOne">
                                                        <input id="smallT4FeeLimit1" type="number" class="tinyInput" name="smallFeeLimit" value="50">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input id="smallT4MaxFeeCheckTwo" type="checkbox" name="smallT4MaxFeeCheckTwo">
                                                        <input id="smallT4FeeLimit2" type="number" class="tinyInput" name="smallFeeLimitEx" value="50">元
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        <input id="bigT4MaxFeeCheckOne" type="checkbox" name="bigT4MaxFeeCheckOne">
                                                        <input id="bigT4FeeLimit1" type="number" class="tinyInput" name="bigFeeLimit" value="50">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input id="bigT4MaxFeeCheckTwo" type="checkbox" name="bigT4MaxFeeCheckTwo">
                                                        <input id="bigT4FeeLimit2" type="number" class="tinyInput" name="bigFeeLimitEx" value="50">元
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        <input id="otherT4MaxFeeCheckOne" type="checkbox" name="otherT4MaxFeeCheckOne">
                                                        <input id="otherT4FeeLimit1" type="number" class="tinyInput" name="otherFeeLimit" value="50">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input id="otherT4MaxFeeCheckTwo" type="checkbox" name="otherT4MaxFeeCheckTwo">
                                                        <input id="otherT4FeeLimit2" type="number" class="tinyInput" name="otherFeeLimitEx" value="50">元
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12 colHidden template4">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <div style="float: left;width: 50px;">白天:</div>
                                                    <div class="bootstrap-timepicker" style="width: 48px;height: 25px;line-height: 25px;float: left;margin-top: 5px;">
                                                        <div class="input-group">
                                                            <input type="text" id="T4DayTimeStart" name="feeStartTime" class="timepicker timepick" >
                                                        </div>
                                                    </div>
                                                    <div style="float: left;width: 20px;"> ~ </div>
                                                    <div class="bootstrap-timepicker" style="width: 48px;height: 25px;line-height: 25px;float: left;margin-top: 5px;">
                                                        <div class="input-group">
                                                            <input type="text" id="T4DayTimeEnd" name="feeEndTime" class="timepicker timepick" >
                                                        </div>
                                                    </div>
                                                    <div style="float: left;width: 210px;text-align: left;">
                                                        夜间:
                                                        <span id="T4nightStart">19:00</span>
                                                        ~ 次日
                                                        <span id="T4nightEnd">07:00</span>
                                                    </div>
                                                    <div style="float: left;width: 250px;">
                                                        <input type="radio" name="isFeeSection" value="1">分段计费方式
                                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                                        <input type="radio" name="isFeeSection" value="0">以出场时间计费方式
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                        <div class="col-xs-12 colHidden template4">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine" style="text-align: left">
                                                    24小时封顶限额:
                                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="checkbox" id="T4DaySmallMaxFeeCheck" name="T4DaySmallMaxFeeCheck">小型车收费限额<input type="text" id="smallT4TotalFeeLimit" class="tinyInput" name="smallTotalFeeLimit" value="0">元
                                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="checkbox" id="T4DayBigMaxFeeCheck" name="T4DayBigMaxFeeCheck">大型车收费限额<input type="text" id="bigT4TotalFeeLimit" class="tinyInput" name="bigTotalFeeLimit" value="0">元
                                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="checkbox" id="T4DayOtherMaxFeeCheck" name="T4DayOtherMaxFeeCheck">其他车收费限额<input type="text" id="otherT4TotalFeeLimit" class="tinyInput" name="otherTotalFeeLimit" value="0">元
                                                </div>

                                            </div>
                                        </div>
                                        </form>
                                        <%--模板4结束--%>

                                        <%--模板5开始--%>
                                        <form id="formTemplate5">
                                        <div class="col-xs-2 colHidden template5">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>车型</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <label>小型车</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <label>大型车</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <label>其他</label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-3 colHidden template5">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>时间段</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class="T5daytime timeDuration timeDurationTop">
                                                        <span class="T5dayTimeStart">01:00</span> ~ <span class="T5NightStart">19:00</span>
                                                    </div>
                                                    <div class="T5night timeDuration">
                                                        <span class="T5NightStart">19:00</span> ~ <span class="T5dayTimeStart">01:00</span>
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class="T5daytime timeDuration timeDurationTop">
                                                        <span class="T5dayTimeStart">01:00</span> ~ <span class="T5NightStart">19:00</span>
                                                    </div>
                                                    <div class="T5night timeDuration">
                                                        <span class="T5NightStart">19:00</span> ~ <span class="T5dayTimeStart">01:00</span>
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class="T5daytime timeDuration timeDurationTop">
                                                        <span class="T5dayTimeStart">01:00</span> ~ <span class="T5NightStart">19:00</span>
                                                    </div>
                                                    <div class="T5night timeDuration">
                                                        <span class="T5NightStart">19:00</span> ~ <span class="T5dayTimeStart">01:00</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-xs-3 colHidden template5">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>免费时长</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input type="number" class="tinyInput" name="smallFreeDuration" value="15">分钟
                                                    <%--<div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="smallFreeDuration" value="15">分钟
                                                    </div>--%>
                                                    <%--<div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="smallFreeDurationEx" value="15">分钟
                                                    </div>--%>

                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input type="number" class="tinyInput" name="bigFreeDuration" value="15">分钟
                                                    <%--<div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="bigFreeDuration" value="15">分钟
                                                    </div>--%>
                                                    <%--<div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="bigFreeDurationEx" value="15">分钟
                                                    </div>--%>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input type="number" class="tinyInput" name="otherFreeDuration" value="15">分钟
                                                    <%--<div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="otherFreeDuration" value="15">分钟
                                                    </div>--%>
                                                    <%--<div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="otherFreeDurationEx" value="15">分钟
                                                    </div>--%>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-xs-2 colHidden template5">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>收费金额</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="smallFirstTimeFee" value="5">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="smallFirstTimeFeeEx" value="5">元
                                                    </div>

                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="bigFirstTimeFee" value="5">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="bigFirstTimeFeeEx" value="5">元
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="otherFirstTimeFee" value="5">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="otherFirstTimeFeeEx" value="5">元
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-2 colHidden template5">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label id="topFee5">一天收费封顶</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input id="smallT5MaxFeeCheck" type="checkbox" name="smallT5MaxFeeCheck">
                                                    <input type="number" class="tinyInput" name="smallTotalFeeLimit" value="5">元
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input id="bigT5MaxFeeCheck" type="checkbox" name="bigT5MaxFeeCheck">
                                                    <input type="number" class="tinyInput" name="bigTotalFeeLimit" value="5">元
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input id="otherT5MaxFeeCheck" type="checkbox" name="otherT5MaxFeeCheck">
                                                    <input type="number" class="tinyInput" name="otherTotalFeeLimit" value="5">元
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12 colHidden template5">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <div style="float: left;width: 50px;">白天:</div>
                                                    <div class="bootstrap-timepicker" style="width: 48px;height: 25px;line-height: 25px;float: left;margin-top: 5px;">
                                                        <div class="input-group">
                                                            <input type="text" id="T5DayTimeStart" name="feeStartTime" class="timepicker timepick" >
                                                        </div>
                                                    </div>
                                                    <div style="float: left;width: 20px;"> ~ </div>
                                                    <div class="bootstrap-timepicker" style="width: 58px;height: 25px;line-height: 25px;float: left;margin-top: 5px;">
                                                        <div class="input-group">
                                                            <input type="text" id="T5DayTimeEnd" name="feeEndTime" class="timepicker timepick" >
                                                        </div>
                                                    </div>
                                                    <div style="float: left;width: 210px;text-align: left;">
                                                        夜间:
                                                        <span id="T5nightStart">19:00</span>
                                                        ~ 次日
                                                        <span id="T5nightEnd">01:00</span>
                                                    </div>
                                                    <div style="float: left;width: 350px;">
                                                        <input id="T5isFeeSection" type="checkbox" name="isFeeSection">
                                                        <span class="T5dayTimeStart">01:00</span>至次日<span class="T5dayTimeStart">01:00</span>在时间段内进出按一次计费
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                        </form>
                                        <%--模板5结束--%>
                                        <%--模板6开始--%>
                                        <form id="formTemplate6">
                                        <div class="col-xs-2 colHidden template6">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>车型</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <label>小型车</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <label>大型车</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <label>其他</label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-2 colHidden template6">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>时间段</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class="T6daytime timeDuration timeDurationTop">
                                                        <span class="T6dayTimeStart">07:00</span> ~ <span class="T6NightStart">19:00</span>
                                                    </div>
                                                    <div class="T6night timeDuration">
                                                        <span class="T6NightStart">19:00</span> ~ <span class="T6dayTimeStart">07:00</span>
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class="T6daytime timeDuration timeDurationTop">
                                                        <span class="T6dayTimeStart">07:00</span> ~ <span class="T6NightStart">19:00</span>
                                                    </div>
                                                    <div class="T6night timeDuration">
                                                        <span class="T6NightStart">19:00</span> ~ <span class="T6dayTimeStart">07:00</span>
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class="T6daytime timeDuration timeDurationTop">
                                                        <span class="T6dayTimeStart">07:00</span> ~ <span class="T6NightStart">19:00</span>
                                                    </div>
                                                    <div class="T6night timeDuration">
                                                        <span class="T6NightStart">19:00</span> ~ <span class="T6dayTimeStart">07:00</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-xs-2 colHidden template6">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>免费时长</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input type="number" class="tinyInput" name="smallFreeDuration" value="15">分钟
                                                    <%--<div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="smallFreeDuration" value="15">分钟
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="smallFreeDurationEx" value="15">分钟
                                                    </div>--%>

                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input type="number" class="tinyInput" name="bigFreeDuration" value="15">分钟
                                                    <%--<div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="bigFreeDuration" value="15">分钟
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="bigFreeDurationEx" value="15">分钟
                                                    </div>--%>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input type="number" class="tinyInput" name="otherFreeDuration" value="15">分钟
                                                    <%--<div class=" timeDuration timeDurationTop">
                                                        <input type="number" class="tinyInput" name="otherFreeDuration" value="15">分钟
                                                    </div>
                                                    <div class=" timeDuration">
                                                        <input type="number" class="tinyInput" name="otherFreeDurationEx" value="15">分钟
                                                    </div>--%>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-xs-4 colHidden template6">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>收费金额</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        第一次收费<input type="number" class="tinyInput" name="smallFirstTimeFee" value="5">元,
                                                        第二次收费<input type="number" class="tinyInput" name="smallSecondTimeAfterGapFee" value="10">元,
                                                        之后收费<input type="number" class="tinyInput" name="smallAddFee" value="20">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        第一次收费<input type="number" class="tinyInput" name="smallFirstTimeFeeEx" value="5">元,
                                                        第二次收费<input type="number" class="tinyInput" name="smallSecondTimeAfterGapFeeEx" value="10">元,
                                                        之后收费<input type="number" class="tinyInput" name="smallAddFeeEx" value="20">元
                                                    </div>

                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        第一次收费<input type="number" class="tinyInput" name="bigFirstTimeFee" value="5">元,
                                                        第二次收费<input type="number" class="tinyInput" name="bigSecondTimeAfterGapFee" value="10">元,
                                                        之后收费<input type="number" class="tinyInput" name="bigAddFee" value="20">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        第一次收费<input type="number" class="tinyInput" name="bigFirstTimeFeeEx" value="5">元,
                                                        第二次收费<input type="number" class="tinyInput" name="bigSecondTimeAfterGapFeeEx" value="10">元,
                                                        之后收费<input type="number" class="tinyInput" name="bigAddFeeEx" value="20">元
                                                    </div>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <div class=" timeDuration timeDurationTop">
                                                        第一次收费<input type="number" class="tinyInput" name="otherFirstTimeFee" value="5">元,
                                                        第二次收费<input type="number" class="tinyInput" name="otherSecondTimeAfterGapFee" value="10">元,
                                                        之后收费<input type="number" class="tinyInput" name="otherAddFee" value="20">元
                                                    </div>
                                                    <div class=" timeDuration">
                                                        第一次收费<input type="number" class="tinyInput" name="otherFirstTimeFeeEx" value="5">元,
                                                        第二次收费<input type="number" class="tinyInput" name="otherSecondTimeAfterGapFeeEx" value="10">元,
                                                        之后收费<input type="number" class="tinyInput" name="otherAddFeeEx" value="20">元
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-2 colHidden template6">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <label>一天收费封顶</label>
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input id="smallT6MaxFeeCheck" type="checkbox" name="smallT6MaxFeeCheck">
                                                    <input type="number" class="tinyInput" name="smallTotalFeeLimit" value="5">元
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input id="bigT6MaxFeeCheck" type="checkbox" name="bigT6MaxFeeCheck">
                                                    <input type="number" class="tinyInput" name="bigTotalFeeLimit" value="5">元
                                                </div>
                                                <div class="col-xs-12 content colLine2">
                                                    <input id="otherT6MaxFeeCheck" type="checkbox" name="otherT6MaxFeeCheck">
                                                    <input type="number" class="tinyInput" name="otherTotalFeeLimit" value="5">元
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12 colHidden template6">
                                            <div class="row">
                                                <div class="col-xs-12 content colLine">
                                                    <div style="float: left;width: 50px;">白天:</div>
                                                    <div class="bootstrap-timepicker" style="width: 48px;height: 26px;line-height: 26px;float: left;margin-top: 6px;">
                                                        <div class="input-group">
                                                            <input type="text" id="T6DayTimeStart" name="feeStartTime" class="timepicker timepick" >
                                                        </div>
                                                    </div>
                                                    <div style="float: left;width: 20px;"> ~ </div>
                                                    <div class="bootstrap-timepicker" style="width: 58px;height: 26px;line-height: 26px;float: left;margin-top: 6px;">
                                                        <div class="input-group">
                                                            <input type="text" id="T6DayTimeEnd" name="feeEndTime" class="timepicker timepick" >
                                                        </div>
                                                    </div>
                                                    <div style="float: left;width: 210px;text-align: left;">
                                                        夜间:
                                                        <span id="T6nightStart" class="T6NightStart">19:00</span>
                                                        ~ 次日
                                                        <span id="T6nightEnd" class="T6dayTimeStart">01:00</span>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                        </form>
                                        <%--模板6结束--%>
                                    </div>

                                </div>
                            </div>

                        </div>
                    </div>
           
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="submitBtn">提交</button>
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
<script src="${pageContext.request.contextPath}/resources/admin/js/content.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/jsTree/jstree.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/chosen/chosen.jquery.js"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/resources/timepicker/bootstrap-timepicker.min.js" ></script>

<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/chargingRules.js"></script>

</body>
</html>
