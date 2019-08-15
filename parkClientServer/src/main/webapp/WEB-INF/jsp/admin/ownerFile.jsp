<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cf
  Date: 2017-11-24
  Time: 16:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
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
    <link href="${pageContext.request.contextPath}/resources/admin/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="/resources/js/bootstrap-select/css/bootstrap-select.css" rel="stylesheet">
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="tableCss1">
<div class=" wrapper-content"style="height:100%;background-color: #FFFFFF" >
    <div class="col-xs-12 " style="padding-top:0px;border-bottom: solid 1px #EFEFEF">
        <div class="col-sm-3">
            <div class="input-group m-b">
                <span class="input-group-addon input-group-background">车场选择</span>
                <select class="form-control m-b" data-placeholder="选择车场类型..." name="carparkIdSearch" id="carparkIdSearch">
                </select>
            </div>
        </div>

        <div class="col-sm-3">
            <div class="input-group m-b">
                <span class="input-group-addon input-group-background">车辆性质</span>
                <select class="form-control m-b" data-placeholder="选择车辆性质..." name="chargeTypeSearch" id="chargeTypeSearch">
                </select>
            </div>
        </div>

        <div class="col-sm-3" >
            <div class="input-group m-b">
                <span class="input-group-addon input-group-background">车牌号码</span>
                <input type="text" class="form-control" id="carNoSearch" value="">
            </div>
        </div>

        <div class="col-sm-3" >
            <div class="input-group m-b">
                <span class="input-group-addon input-group-background">车主姓名</span>
                <input type="text" class="form-control" id="driverNameSearch" value="">
            </div>
        </div>

        <div class="col-sm-3" >
            <div class="input-group m-b">
                <span class="input-group-addon input-group-background">联系方式</span>
                <input type="text" class="form-control" id="telephoneSearch" value="">
            </div>
        </div>

        <div class="col-sm-3" >
            <div class="input-group m-b">
                <span class="input-group-addon input-group-background">车主信息</span>
                <input type="text" class="form-control" id="driverInfoSearch" value="">
            </div>
        </div>

        <div class="col-sm-3" >
            <div class="input-group m-b">
                <span class="input-group-addon">截止时间</span>
                <input type="text" id="overdueTime" placeholder="截止时间" class="form-control" readonly>
            </div>
        </div>

        <div class="col-sm-1">
            <div class="input-group m-b">
                <a href="javascript:void(0)" type="button" class="btn btn-success btn-success2" onclick="searchDriverInfo()"><i class="fa fa-search"></i>&nbsp;&nbsp;搜索</a>
            </div>
        </div>

        <div class="col-sm-2">
            <div class="input-group m-b">
                <button class="btn btn-success " type="button"  id="creatOwner" ><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;<span class="bold" >会员信息添加</span>
                </button>
            </div>
        </div>
            <div class="col-sm-1">
            <div class="input-group m-b">
                <button class="btn btn-success " type="button"  id="importExcel" >&nbsp;&nbsp;<span class="bold" >导入</span>
                </button>
            </div>
            </div>
            <div class="col-sm-1">
                <a class="btn btn-success m-b f-m" id="exportExcel">导出报表</a>
            <%--<div class="input-group m-b">--%>
                <%--<button class="btn btn-success " type="button"  id="exportExcel "  >&nbsp;&nbsp;<span class="bold" >导出</span>--%>
                <%--</button>--%>
            <%--</div>--%>
            </div>
            <div class="col-sm-1">
                <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>
            </div>
    </div>
    <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;border-top: solid 2px #EFEFEF">
        <table class="table table-striped table-bordered table-hover dataTables-example" id="ownerFile" style="background-color:#ffffff ">
            <thead>
            <tr style="color: #767676">
                <th>车牌号码</th>
                <th>车场名称</th>
                <th>车主姓名</th>
                <th>联系方式</th>
                <th>车主信息</th>
                <th>车辆性质</th>
                <th>车辆有效数</th>
                <th>到期时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
</div>
<iframe id="importExcelFrame" name="importExcelFrame" src="" style="display:none;" onload="uploadExcel()"></iframe>
<div class="modal inmodal fade" id="importExcelModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog"><!--框框-->
        <div class="modal-content"><!--内容-->
            <div class="modal-header">
                <h4 class="modal-title">导入人员</h4>
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                    <!--×-->
                </button>
            </div>

            <div class="modal-body" style="overflow: hidden">
                <form id="importExcelModalForm" method="post" enctype="multipart/form-data" target="importExcelFrame" action="动态变化">
                    <div class="col-sm-12">
                        <div class="col-sm-9">
                            <input  id="importExcelFile" type="file" name="file" data-options="buttonText:'选择文件',required:true" label="选择文件" >
                        </div>
                        <label class="col-sm-3 control-label" style="visibility: hidden">选择导入方式：</label>
                        <div class="col-sm-3" style="visibility: hidden" >
                            <select id="importMode" data-placeholder="未选择" >
                                <option value="0">更新导入</option>
                                <option value="1">覆盖导入</option>
                            </select>
                        </div>
                    </div>

                    <div class="col-sm-12" style="margin-top: 20px;">
                        <div class="col-sm-3">
                            <input  class="form-group" type="submit"  onclick="excelLoadpicture()" id="sumbitExcel" value="上传" />
                        </div>
                    </div>
                </form>
                <div class="spiner-example" style="position: absolute;right: 45%;top: -35%;display:none" id="excleLoadingGraph">
                    <div class="sk-spinner sk-spinner-three-bounce">
                        <div class="sk-bounce1"></div>
                        <div class="sk-bounce2"></div>
                        <div class="sk-bounce3"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="modal inmodal fade in" id="ownerModal" tabindex="-1" role="dialog" aria-hidden="true" >

    <div class="modal-dialog modal-lg"><!--框框-->
        <div class="modal-content"><!--内容-->
            <div class="modal-header">
                <h4 class="modal-title" id="title">会员信息添加</h4>
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span>
                    <!--×-->
                </button>
            </div>

            <div class="modal-body">
                <div class="row">
                    <div class="col-sm-6 ">
                        <form id="ownerForm" class="form-horizontal">
                            <div id="addCarNoInput">
                                <div class="form-group "  id="carNoInput0">
                                    <label class="col-sm-3 control-label">车牌/卡号：</label>
                                    <div class="col-sm-8">
                                        <input type="text"  class="form-control" id="carNo0" name="carNo">
                                    </div>
                                    <label class="col-sm-1 control-label glyphicon-nopaddingLeft lable">
                                        <span class="glyphicon glyphicon-plus-sign fa-lg gray-color pull-left addCarNo color-honvor-blue"></span>
                                    </label>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-sm-3 control-label">车主姓名：</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" id="driverName" name="driverName">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">联系电话：</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" id="driverTelephoneNumber" name="driverTelephoneNumber">
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-sm-3 control-label">车主信息：</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" id="driverInfo" >
                                </div>
                            </div>

                          <%--  <div class="form-group">
                                <label class="col-sm-8 control-label" style="color: red">*同一车辆基本信息以首次录入为准</label>
                            </div>--%>
                        </form>
                    </div>

                    <div class="col-sm-6" style="margin-bottom: 10px;">
                        <form class="form-horizontal" id="payForm">
                            <div class="form-group pay">
                                <div class="form-group text-center pay">
                                    <h2 id="amountBalanceText" class="blue-color no-marginTop" >余额:<span id="amountBalance">0</span>元</h2>
                                    <h2 id="timesBalanceText" class="blue-color no-marginTop" >余量:<span id="timesBalance">0</span>次</h2>
                                    <h2 class="blue-color no-marginTop" >缴费:<span id="payTotal">0</span>元</h2>
                                </div>

                                <button class="btn btn-outline btn-default " type="button" id="crePark" style="width: 100%"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;<span class="bold"></span>
                                </button>
                            </div>

                            <div class="form-group selectPay" style="display: none">
                                <div class="col-sm-8" >
                                    <select id="carParkNameSelect"  title="选择所属车场" class="selectpicker form-control "  multiple>
                                    </select>
                                </div>

                                <button class="btn btn-primary pull-right btn-sm " type="button" id="clickCarPark">确定</button>

                                <button type="button" class="close" id="closeCarPark" ><span aria-hidden="true">×</span>
                                    <!--×-->
                                </button>
                            </div>

                            <div class="ibox-content" id="parkingLotContent" style="display: none">
                                <div class="row">
                                    <button type="button" class="close " id="closePrkingLot" style="position: static;top: 0px" ><span aria-hidden="true">×</span>
                                            <!--×-->
                                    </button>

                                    <div style="text-align: center;font-size: 18px;">

                                        <%--<div class=" col-sm-offset-3 col-sm-3 no-padding">--%>
                                            <%--<img class=" form-control  background-color-none border-none pull-right" src="${pageContext.request.contextPath}/resources/images/parking.png"  style="height: 34px;width: 44px">--%>
                                        <%--</div>--%>

                                        <label class=" form-control background-color-none border-none" id="carParkNameLable" >停车场</label>

                                    </div>

                                    <div class="form-group" style="display: none;">
                                        <div class="col-sm-offset-6 col-sm-6 no-padding ">
                                            <label class=" form-control background-color-none border-none text-left no-top-padding" id="1" style="height: 34px;">(充值天数:<span id="daysRemaining">0</span>天)</label>
                                        </div>
                                    </div>

                                    <div class="form-group" >

                                        <div class="col-sm-4" >
                                            <select id="PackageSelect"  data-placeholder="套餐类型" class="form-control "  >
                                            </select>
                                        </div>


                                        <div class="col-sm-4" >
                                            <div class="input-group  ">

                                                <span class="input-group-addon no-marginTop">缴费车位数</span>
                                                <select id="carNumber"  data-placeholder="车位数" class="form-control " >
                                                <option value="1">1</option>
                                                </select>

                                            </div>
                                        </div>

                                        <div class="col-sm-4 otherPackage" >
                                            <div class="form-group">
                                                <div class=" col-sm-4 no-padding">
                                                    <span class="form-control " >缴费</span>
                                                </div>
                                                <div class="col-sm-5 no-padding ">
                                                    <input  id="otherPackagePay" type="text" class="form-control no-marginTop " name="otherPackagePay" value="0">
                                                </div>
                                                <div class=" col-sm-3 no-padding">
                                                    <span class="form-control" >元</span>
                                                </div>
                                            </div>


                                            <%--<div class="input-group  ">--%>
                                                <%--<span class="input-group-addon">缴费</span>--%>
                                                <%--<input  id="otherPackagePay"type="text" class="form-control " name="otherPackagePay" value="0">--%>
                                                <%--<span class="input-group-addon no-borders">元</span>--%>
                                            <%--</div>--%>
                                        </div>

                                        <div class="col-sm-4 countPackage">
                                            <div class="input-group">
                                                <span class="input-group-addon glyphicon glyphicon-plus background-color-honvor-blue" id="increaseCount"></span>
                                                <input  id="countPackage" type="text" class="form-control " value="0" >
                                                <span class="input-group-addon glyphicon glyphicon-minus background-color-honvor-blue" id="reduceCount"></span>
                                                <span class="input-group-addon no-borders ">次</span>
                                            </div>
                                        </div>

                                        <div class="col-sm-4 monthPackage">
                                            <div class="input-group">
                                                <span class="input-group-addon glyphicon glyphicon-plus background-color-honvor-blue" id="increase"></span>
                                                <input  id="monthChange"type="text" class="form-control " value="0" readonly>
                                                <span class="input-group-addon glyphicon glyphicon-minus background-color-honvor-blue" id="reduce"></span>
                                                <span class="input-group-addon no-borders ">月</span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group monthPackage freePackage" >
                                        <div class="col-sm-6 ">
                                            <div class="input-group ">
                                                <span class="input-group-addon">有效开始时间</span>
                                                <input type="text" id="startTime" placeholder="年/月/日 " class="form-control">
                                            </div>
                                        </div>

                                        <div class="col-sm-6 ">
                                            <div class="input-group">
                                                <span class="input-group-addon">有效结束时间</span>
                                                <input type="text" id="endTime" placeholder="年/月/日 " class="form-control">
                                            </div>
                                        </div>
                                    </div>

                                    <div id="addCarNumber">
                                        <div class="form-group "  id="carNumberInput0">

                                            <div class="text-left col-sm-2 no-right-padding">
                                                <label class=" control-label ">车位：</label>
                                            </div>

                                            <div class="col-sm-8 no-left-padding">
                                                <input type="text"  class="form-control" id="parkingLot0" name="parkingLot">
                                            </div>
                                            <label class="col-sm-1 control-label glyphicon-nopaddingLeft lable">
                                                <span class="glyphicon glyphicon-plus-sign fa-lg gray-color pull-left addParkingLot color-honvor-blue"></span>
                                            </label>
                                        </div>
                                    </div>

                                </div>

                            </div>
                        </form>


                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="submitBtn">提交</button>
                <button type="button" class="btn btn-primary" id="editBtn" >提交</button>
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</div>



<!-- 全局js -->

<script src="${pageContext.request.contextPath}/resources/admin/js/jquery.min.js?v=2.1.4"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<%--<!-- layerDate plugin javascript时间表格 -->--%>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/layer/laydate/laydate.js"></script>
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

<script src="${pageContext.request.contextPath}/resources/js/bootstrap-select/js/bootstrap-select.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap-select/js/defaults-zh_CN.js"></script>

<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script   src="${pageContext.request.contextPath}/resources/script/admin/ownerFile.js"></script>
</body>
</html>
