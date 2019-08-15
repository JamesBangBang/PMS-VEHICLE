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
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/fullcalendar/fullcalendar.print.css" rel="stylesheet">
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="tableCss1">
<div class=" wrapper-content"style="height:100%;min-width:828px;background-color: #FFFFFF;" >

    <div class="col-xs-12" style="margin-top: 0px;background-color:#ffffff;">
        <div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12" style="background-color: #ffffff;padding: 0px;">
            <div style="float: left;width: 23%;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">车牌号</span>
                    <input type="text" id="carName" placeholder="查询" class="form-control">
                </div>
            </div>
            <div style="float: left;width: 23%;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">车主姓名</span>
                    <input type="text" id="driverName" placeholder="查询" class="form-control">
                </div>
            </div>
            <div style="float: left;width: 23%;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">车场名称</span>
                    <input type="text" id="carparkName" placeholder="查询" class="form-control">
                </div>
            </div>
            <div style="float: left;width: 23%;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">车道名称</span>
                    <input type="text" id="carRoadName" placeholder="查询" class="form-control">
                </div>
            </div>

        </div>

        <div class="col-xs-12 col-xs-12 col-sm-12 col-md-12 col-lg-12" style="background-color: #ffffff;padding: 0px;">
            <div style="float: left;width: 23%;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">进出类型</span>
                    <select id="inoutFlag" data-placeholder="选择进出场类型" class="form-control">
                        <option value="">无</option>
                        <option value="0">进</option>
                        <option value="1">出</option>
                    </select>
                </div>
            </div>
            <div style="float: left;width: 23%;;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">开始时间</span>
                    <input type="text" id="start" placeholder="开始时间" class="form-control" readonly>
                </div>
            </div>

            <div style="float: left;width: 23%;;margin-right: 10px">
                <div class="input-group m-b">
                    <span class="input-group-addon">结束时间</span>
                    <input type="text" id="end" class="form-control" placeholder="结束时间" readonly >
                </div>
            </div>

            <div  style="float:left;">
                <button id="refreshBtn" onclick="refreshBtnClick()" class="btn btn-default">刷新表格</button>
            </div>

            <div  style="float:left;margin-left: 10px">
                <button type="button" class="btn btn-success" onclick="realTimeExcel()">导出报表</button>
            </div>

            <div   style="float:left;margin-left: 10px">
                <button type="button" class="btn btn-primary" onclick="search()">
                    <i class="fa fa-search"></i>&nbsp;&nbsp;搜索</button>
            </div>
        </div>

        <table class="table table-striped table-bordered " id="List" style="background-color:#ffffff ;width: 100%;">
            <thead>
            <tr style="color: #767676">
                <th>车牌号</th>
                <th>车牌颜色</th>
                <th>车辆属性</th>
                <th>进出类型</th>
                <th>车场名称</th>
                <th>车道名称</th>
                <th>进出场状态</th>
                <th>进出场时间</th>
                <th>岗亭名称</th>
                <th>操作员名称</th>
                <th>图片</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
        
</div>


    <div class="modal inmodal fade" id="descModal" tabindex="-1" role="dialog" aria-hidden="true">

        <div class="modal-dialog modal-lg"><!--框框-->
            <div class="modal-content"><!--内容-->
                <div class="modal-header">
                    <h4 class="modal-title">详情</h4>
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                        <!--×-->
                    </button>
                </div>
                <div class="modal-body">
                    <form id="descForm">
                        <div class="row">
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>车牌号</label>
                                    <input class="form-control" name="car_no" readonly>
                                </div>
                                <div class="form-group">
                                    <label>车场名称</label>
                                    <input class="form-control" name="carpark_name" readonly>
                                </div>
                                <div class="form-group">
                                    <label>岗亭名称</label>
                                    <input class="form-control" name="post_name" readonly>
                                </div>
                                <div class="form-group">
                                    <label>进出类型</label>
                                    <input class="form-control" name="inout_flag" readonly>
                                </div>
                                <div class="form-group">
                                    <label>进出场时间</label>
                                    <input class="form-control" name="inout_time" readonly>
                                </div>
                                <div class="form-group">
                                    <label>进出场状态</label>
                                    <input class="form-control" name="inout_status" readonly>
                                </div>
                            </div>
                            <div class="col-xs-3">
                                <div class="form-group">
                                    <label>车牌颜色</label>
                                    <input class="form-control" name="car_no_color" readonly>
                                </div>
                                <div class="form-group">
                                    <label>车道名称</label>
                                    <input class="form-control" name="car_road_name" readonly>
                                </div>
                                <div class="form-group">
                                    <label>操作员名称</label>
                                    <input class="form-control" name="operator_name" readonly>
                                </div>
                                <div class="form-group">
                                    <label>车辆类型</label>
                                    <input class="form-control" name="car_type" readonly>
                                </div>

                                <div class="form-group">
                                    <label>备注</label>
                                    <input class="form-control" name="remark" readonly>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <img id="descImg" src="" style="width: 100%;">
                            </div>
                            <%--<div class="col-xs-3">--%>

                            <%--</div>--%>
                        </div>
                    </form>
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

<!-- layerDate plugin javascript时间表格 -->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/layer/laydate/laydate.js"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/dataTables.bootstrap.js"></script>

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/fullcalendar/fullcalendar.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/realTimeReport.js"></script>

</body>
</html>
