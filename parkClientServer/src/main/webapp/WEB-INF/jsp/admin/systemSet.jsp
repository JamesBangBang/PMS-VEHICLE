<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2018-01-22
  Time: 10:38
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
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/jsTree/style.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/bootstrap-switch/css/bootstrap3/bootstrap-switch.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/jasny/jasny-bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/timepicker/bootstrap-timepicker.min.css" rel="stylesheet">
    <title></title>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body>
    <div class="container">

            <div class="row">
                <div class="col-xs-12" style="line-height: 80px;height: 80px;">
                    <h1>系统配置信息</h1>
                </div>
            </div>
        <form id="sysForm">
            <hr>
        <form id="sysFormSet">
            <div class="row">
                <div class="col-xs-4" style="text-align: right;line-height: 35px;">
                    <label>系统优先区域</label>
                </div>
                <div class="col-xs-3">
                    <select id="priorityArea" name="priorityArea" class="form-control" value="${sysInfo.priorityArea}" >
                        <option value="京" >北京</option>
                        <option value="沪" >上海</option>
                        <option value="津" >天津</option>
                        <option value="渝" >重庆</option>
                        <option value="黑" >黑龙江省</option>
                        <option value="吉" >吉林省</option>
                        <option value="辽" >辽宁省</option>
                        <option value="蒙" >内蒙古自治区</option>
                        <option value="冀" >河北省</option>
                        <option value="鲁" >山东省</option>
                        <option value="晋" >山西省</option>
                        <option value="皖" >安徽省</option>
                        <option value="苏" >江苏省</option>
                        <option value="浙" >浙江省</option>
                        <option value="闽" >福建省</option>
                        <option value="粤" >广东省</option>
                        <option value="豫" >河南省</option>
                        <option value="赣" >江西省</option>
                        <option value="湘" >湖南省</option>
                        <option value="鄂" >湖北省</option>
                        <option value="桂" >广西壮族自治区</option>
                        <option value="琼" >海南省</option>
                        <option value="云" >云南省</option>
                        <option value="贵" >贵州省</option>
                        <option value="川" >四川省</option>
                        <option value="陕" >陕西省</option>
                        <option value="甘" >甘肃省</option>
                        <option value="宁" >宁夏回族自治区</option>
                        <option value="青" >青海省</option>
                        <option value="藏" >西藏自治区</option>
                        <option value="新" >新疆维吾尔自治区</option>
                        <option value="台" >台湾省</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4" style="text-align: right;line-height: 35px;">
                    <label>默认收费类型</label>
                </div>
                <div class="col-xs-3">
                    <select id="defaultMemberId" name="defaultMemberId" class="form-control" value="${sysInfo.defaultMemberId}" >
                    </select>
                </div>
            </div>
        </form>
            <hr>
        </form>
            <div class="row">
                <div class="col-xs-7" style="text-align: right;">
                    <button id="submitBtn" class="btn btn-success">提 交</button>
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
<script>
    $(function () {
        $("#priorityArea").val($("#priorityArea").attr("value"));
        //获取收费类型
        var params = {};
        ajaxReuqest(starnetContextPath + '/system/info/getMemberInfo','post',params,function (res) {
            if (res.result == 0){
                $("defaultMemberId").empty();
                $("#defaultMemberId").append("<option value=''>无</option>");
                for (var i in res.data){
                    var item = "<option value='" + res.data[i].memberId + "'>" +
                            res.data[i].memberName +
                            "</option>";
                    $("#defaultMemberId").append(item);
                    $("#defaultMemberId").val($("#defaultMemberId").attr("value"));
                }
            }else{
                errorPrompt("获取套餐信息失败！");
            }
        },'json');



    })


    $("#submitBtn").click(function () {
        var params = $("#sysForm").serializeObject();
        ajaxReuqest(starnetContextPath + '/system/info/update','post',params,function (res) {
            if(res.result == 0){
                successfulPrompt("操作成功","");
            }else{
                errorPrompt("操作失败",res.msg);
            }
        },'json');
    });
</script>
</html>
