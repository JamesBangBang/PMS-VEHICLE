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
    <style>
        .input-background::-webkit-input-placeholder{
            color: #000000;
        }
        label{
            font-size: 15px;
        }
    </style>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body style="background-color: #082F4E;padding: 20px;">
<div class=" wrapper-content"style="height:75.5%;background-color: #FFFFFF" >
    <div class="col-xs-12">
        <h3  style="color:#767676"><i class="fa fa-bars" style="color:#767676"></i>&nbsp;&nbsp;参数设置</h3>
    </div>
    <div class="col-xs-12" style="border-top: solid 2px #EFEFEF;margin: 0px;">
            <div class="modal-body">
                <form class="form-horizontal m-t" >
                    <div class="form-group"style="border-bottom: solid 1px #EFEFEF">
                        <div class="col-sm-2">
                            <label>固定车位总数：</label>
                        </div>
                        <div class="col-sm-5">
                            <input class="input-background"  placeholder="981" type="text" class="form-control"   style="border: none;color: #000000;width:70%;">
                        </div>
                    </div>
                    <div class="form-group" style="border-bottom: solid 1px #EFEFEF">
                        <div class="col-sm-2">
                            <label>临时车位总数:</label>
                        </div>
                        <div class="col-sm-5">
                            <input  class="input-background"  placeholder="19" type="text" class="form-control" style="border: none;color: #000000;width:70%;">
                        </div>
                    </div>
                    <div class="form-group"style="border-bottom: solid 1px #EFEFEF">
                        <div class="col-sm-2">
                            <label>车位总数：</label>
                        </div>
                        <div class="col-sm-5">
                            <input class="input-background" type="number"placeholder="1000" class="form-control"  style="border: none;color: #000000;width:70%;">
                        </div>
                    </div>
                    <div class="form-group"style="border-bottom: solid 1px #EFEFEF">
                        <div class="col-sm-2">
                            <label>入口自动放行规则：</label>
                        </div>
                        <div class="col-sm-5">
                            <input  class="input-background"type="number" placeholder="无"class="form-control" style="border: none;color: #000000;width:70%;">
                        </div>
                    </div>
                    <div class="form-group"style="border-bottom: solid 1px #EFEFEF">
                        <div class="col-sm-2">
                            <label>出口自动放行规则：</label>
                        </div>
                        <div class="col-sm-5">
                            <input class="input-background" type="number"placeholder="无" class="form-control" style="border: none;color: #000000;width:70%;">
                        </div>
                    </div>
                    <div class="form-group"style="border-bottom: solid 1px #EFEFEF">
                        <div class="col-sm-2">
                            <label>是否收费0元自动放行：</label>
                        </div>
                        <div class="col-sm-5">
                            <input class="input-background" type="number"placeholder="是" class="form-control" style="border: none;color: #000000;width:70%;">
                        </div>
                    </div>
                    <div class="form-group"style="border-bottom: solid 1px #EFEFEF">
                        <div class="col-sm-2">
                            <label>扩展设置：</label>
                        </div>
                        <div class="col-sm-5">
                            <input class="input-background" type="number"placeholder="" class="form-control" style="border: none;color: #000000;width:70%;">
                        </div>
                    </div>
                    <div class="form-group"style="border-bottom: solid 1px #EFEFEF">
                        <div class="col-sm-2">
                            <label>创建人：</label>
                        </div>
                        <div class="col-sm-5">
                            <input class="input-background" type="number"placeholder="rx000006" class="form-control" style="border: none;color: #000000;width:70%;">
                        </div>
                    </div>
                    <div class="form-group"style="border-bottom: solid 1px #EFEFEF">
                        <div class="col-sm-2">
                            <label>创建时间：</label>
                        </div>
                        <div class="col-sm-5">
                            <input class="input-background" type="number"placeholder="2017年4月28日 上午9:42:23" class="form-control" style="border: none;color: #000000;width:70%;">
                        </div>
                    </div>
                    <div class="form-group"style="border-bottom: solid 1px #EFEFEF">
                        <div class="col-sm-2">
                            <label>最后修改人</label>
                        </div>
                        <div class="col-sm-5">
                            <input class="input-background" type="number"placeholder="rx000006" class="form-control" style="border: none;;color: #000000;width:70%;">
                        </div>
                    </div>
                    <div class="form-group"style="border-bottom: solid 1px #EFEFEF">
                        <div class="col-sm-2">
                            <label>最后修改时间</label>
                        </div>
                        <div class="col-sm-5">
                            <input class="input-background" type="number"placeholder="2017年8月28日 上午19:42:23" class="form-control" style="border: none;;color: #000000;width:70%;">
                        </div>
                    </div>
                </form>
                <div class="form-group">
                    <%--<div class="col-sm-4" >--%>
                        <button class="btn btn-success" type="submit" id="editBtn"style="float: left">提交</button>
                    <%--</div>--%>
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
<script src="${pageContext.request.contextPath}/resources/script/admin/accountSetting.js"></script>

</body>
</html>
