<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2016-12-22
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>车辆出入口管理端</title>
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${pageContext.request.contextPath}/resources/admin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/jsTree/style.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
    <c:if test="${not empty isUseBasicPlatform}">
        <link rel="stylesheet" href="http://${basicAddress}/resources/public/css/menu.css">
    </c:if>
    <style>
        .tdBorder tbody tr td{
            border: none;
            cursor:pointer;

        }
        .divCSS a:hover{
            background: rgba(22,76,129,1);
            color: rgba(255,255,255,1);
        }


        /*label.label1 {*/

        /*font-size: 1.5em ;*/

        /*}*/


       .nav::-webkit-scrollbar-track

       {    background:#082E4E;

       }

        .nav
        {   scrollbar-highlight-color:#082e4e;
            scrollbar-base-color:#999999;



        }

        /*.navbar-default::-webkit-scrollbar-thumb*/
        /*{     background:#0096FF;*/

        /*}*/

    </style>
<script>
   var pieChart;
   var personnelCompositionPieChart;
   var parkID = '${parkId}';
   var trendTimeCount;
    var token = '${param.token}';
    var basicAddress = '${basicAddress}';
</script>
</head>
<jsp:include page="commonValue.jsp"></jsp:include>
<body class="fixed-sidebar full-height-layout  fixed-nav" style="overflow:hidden">
<div id="wrapper" style="padding-top: 0%;height: 100%;overflow:hidden">
    <div class="row border-bottom " style="height:5em;background-color: #082F4E;position: relative;">

        <div data-toggle="modal" href="#parkModel" style="margin-top:-2.4em;  margin-left:-21em;   height: 4.8em;width:41em;float:left;position:absolute;top:50%;left: 50%; background:url(${pageContext.request.contextPath}/resources/images/title3.png);" onclick="createTable()" >
            <div class="col-xs-12" style="height: 100%;text-align: center">
                <label id="parktext"  style="color: #0096FF; font-size: 2.7em;font-family: '黑体';" >${parkName}</label>
            </div>
        </div>
        <div  style="background-color: #082F4E;height: 5em;float:left;width: 30%;">
            <img  style="width: 33%;height: 100%;float: left"  src="${pageContext.request.contextPath}/resources/images/title.png" >
            <lable style="height: 100%;width:66%;float: left;color: #0096FF;font-size: 2em;line-height: 3em;font-family: '黑体'; font-style:italic;">车场信息中心</lable>
            <%--${systemTitle}--%>
        </div>

        <div class="navli" style="position:relative;top:6%;float: right;height: 3em;width:5em;position: relative;right: 2%;">
            <a  style=" font-size: 1.2em; position: relative;top: 16%;left: 24%; text-align: center;  color: #0097FF;background: none !important;" href="${pageContext.request.contextPath}/account/logout"> 注销</a>
        </div>

        <div class="navli" style="position:relative;top:6%;float: right;height: 3em;width:5em;position: relative;right: 2%;">
            <a  style=" font-size: 1.2em; position: relative;top: 16%;left: 24%; text-align: center;  color: #0097FF;background: none !important;" href="${pageContext.request.contextPath}/resources/ocx/ocxreg.zip" title="点击下载ocx控件"> 下载</a>
        </div>

        <div style="float: right;position: relative;right: 3%;top: 13%">
            <img style="height: 3em;width: 3em;position: relative;right: 16%"  src="${pageContext.request.contextPath}/resources/images/user-1.png" />
            <span id="loginUserName" style="color: #0096FF;font-size: 1em;position: relative;right: 10%;font-size:1.2em " >
            <strong>${$_LoginUser.userName}</strong>
            </span>
        </div>
    </div>

    <div style="height:193px;border-bottom: 4px solid #131A2A;border-top: solid 3px #131A2A; background: #09304F;padding: 2px; ">
        <div class="blockInside1 col-xs-3" style="padding: 1px;" ondblclick="parkingSpace()">
            <div class="blockInside">
                <div class="col-xs-7" style="height: 100%;padding-bottom: 2.3%;padding-top: 1%">
                    <div class="col-xs-12"  id="parkingLot" style="height: 90%;width: 100%" >

                    </div>
                    <div class="col-xs-12 text-center" style="height: 10%;width: 100%">
                        <label  style="color:#0096FF;">近七日进场车辆 </label>
                    </div>
                </div>
                <div class="col-xs-5" style="height: 100%;padding-bottom: 2.3%;padding-top: 1%">
                    <div class="col-xs-12 text-center" style="height: 80%;width: 100%;padding-top: 23%">
                        <label id="parkData1" class="label1" style="color:#0096FF;font-size: 3.5em;font-family:SimSun;width: 100%;height: 50%;">0 </label>
                    </div>
                    <div class="col-xs-12 text-center"  style="height: 10%;width: 100%;margin-top: 14.6%;padding: 0px;">
                        <label  style="color:#0096FF;">今日进场车辆总数 </label>
                    </div>
                </div>
            </div>
        </div>

        <div class="blockInside1 col-xs-3" style="padding: 1px;" ondblclick="parkingQuantity()">
            <div class="blockInside">
                <div class="col-xs-7" style="height: 100%;padding-bottom: 2.3%;padding-top: 1%">
                    <div class="col-xs-12"  id="Parking_quantity" style="height: 90%;width: 100%" >

                    </div>
                    <div class="col-xs-12 text-center" style="width: 100%">
                        <label  style="color:#0096FF;">近七日出场车辆  </label>
                    </div>
                </div>

                <div class="col-xs-5" style="height: 100%;padding-bottom: 2.3%;padding-top: 1%">
                    <div class="col-xs-12 text-center" style="height: 80%;width: 100%;padding-top: 23%">
                        <label id="parkData2" class="label1" style="color:#0096FF;font-size: 3.5em;font-family:SimSun;width: 100%;height: 50%;">0 </label>
                    </div>

                    <div class="col-xs-12 text-center"  style="height: 10%;width: 100%;margin-top: 14.6%;padding: 0px;">
                        <label  style="color:#0096FF;">今日出场车辆总数 </label>
                    </div>
                </div>
            </div>
        </div>

        <div class="blockInside1 col-xs-3" style="padding: 1px;"ondblclick="chargeRecord()">
            <div class="blockInside">
                <div class="col-xs-7" style="height: 100%;padding-bottom: 2.3%;padding-top: 1%">
                    <div class="col-xs-12"  id="charge_record" style="height: 90%;width: 100%" >

                    </div>
                    <div class="col-xs-12 text-center" style="height: 10%;width: 100%">
                        <label  style="color:#0096FF;">近七日收费金额  </label>
                    </div>
                </div>

                <div class="col-xs-5" style="height: 100%;padding-bottom: 4.5%;padding-top: 8%">
                    <div class="col-xs-12 text-center" style="height: 80%;width: 100%">
                        <label id="parkData3" class="label1" style="color: #FF9600;font-size: 3.5em;font-family:SimSun;width: 100%;height: 50%;">0 </label>
                    </div>
                    <div class="col-xs-12" style="height: 10%;width: 100%">
                        <div class="progress progress-striped" style="height: 48%;background-color: rgba(255,255,255,0.1);display: none">
                            <div id="parkWidth3" style="width: 70%;background-color: #FF9600" class="progress-bar progress-bar-danger" >
                                <span class="sr-only"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 text-center"  style="height: 10%;width: 100%;margin-top: 4.6%;padding: 0px;">
                        <label  style="color:#0096FF;">今日收费金额(元)</label>
                    </div>
                </div>
            </div>
        </div>

        <div class="blockInside1 col-xs-3" style="padding: 1px;">
            <div class="blockInside">
                <div class="col-xs-7" style="height: 100%;padding-bottom: 2.3%;padding-top: 1%">
                    <div class="col-xs-12"  id="system" style="height: 90%;width: 100%" >

                    </div>
                    <div class="col-xs-12 text-center" style="width: 100%">
                        <label  style="color:#0096FF;">近七日通行异常  </label>
                    </div>
                </div>

                <div class="col-xs-5" style="height: 100%;padding-bottom: 4.5%;padding-top: 8%">
                    <div class="col-xs-12 text-center" style="height: 80%;width: 100%">
                        <label id="parkData4" class="label1" style="color: #D63B3D;font-size: 3.5em;font-family:SimSun;width: 100%;height: 50%;">0 </label>
                    </div>
                    <div class="col-xs-12" style="height: 10%;width: 100%">
                        <div class="progress progress-striped" style="height: 48%;background-color: rgba(255,255,255,0.1);display: none">
                            <div style="width: 70%;background-color: #D63B3D" class="progress-bar progress-bar-danger" >
                                <span class="sr-only"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 text-center"  style="width: 100%;margin-top: 4.6%;padding: 0px;">
                        <label  style="color:#0096FF;">通行异常</label>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!--左侧导航开始-->
    <nav class="navbar-default navbar-static-side" role="navigation" style="border-right: 3px solid #000000;">
        <div class="sidebar-collapse" style="height: 71%;">
            <ul class="nav " id="side-menu" style="height: 90%;overflow-y: scroll;">
                <div style="height:37px";>
                    <a href="javascript:void(0)" class="navbar-minimalize minimalize-styl-2 siderBarBtn" style="position: relative;top: 24%;left: 7%" >
                        <i class="fa fa-lg fa-bars" style="color:#0097FF"></i>
                    </a>
                </div>
                <c:forEach items="${menuList}" var="menu">
                    <c:if test="${menu.parentId eq '#'}">
                        <li class="navli" >
                            <a href="javascript:void(0)"style="height: 1%">
                                <i class="fa fa-cube"></i>
                                <span class="nav-label">${menu.resourceName}</span>
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-second-level">
                                <c:forEach items="${menuList}" var="menuChild">
                                    <c:if test="${menuChild.parentId eq menu.id}">
                                        <li >
                                            <a class="J_menuItem nav-label" href="${pageContext.request.contextPath}${menuChild.targetLink}"><i class="fa fa-minus"></i>${menuChild.resourceName}  </a>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </div>
        <%--<div class="navli-footer" >--%>
        <%--<div style="margin-top: 10px;" >--%>
        <%--<span class="nav-label"  >${copyRight}</span>--%>
        <%--</div>--%>
        <%--</div>--%>
    </nav>
    <!--左侧导航结束-->
    <!--右侧部分开始-->
    <div id="page-wrapper" class="gray-bg dashbard-1" style="overflow:hidden;">
        <div class="row content-tabs"  >
            <nav class="page-tabs J_menuTabs" >
                <div class="page-tabs-content nav-label divCSS"  id="pageTabs">
                    <a data-id="${pageContext.request.contextPath}/home/index" class="active J_menuTab" style="background-color: #12406A;color: #FFFFFF">首页</a>
                </div>
            </nav>
        </div>

        <div class="row J_mainContent" id="content-main" style="height: 71%">
            <iframe class="J_iframe" id="iframe0" name="iframe0" width="100%" height="100%" src="${pageContext.request.contextPath}/home/index" frameborder="0" data-id="${pageContext.request.contextPath}/home/index" ></iframe>
        </div>
        <%--<div class="footer" >--%>

        <%--&lt;%&ndash;<div class="pull-right">&copy; 2017-2020 <a href="javascript:void(0)" target="_blank">${copyRight}</a>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
        <%--</div>--%>
    </div>
    <!--右侧部分结束-->
</div>

<div>
    <input id="parkId" type="hidden" value="">
</div>

<div class="modal  fade" id="parkModel" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title" style="text-align: center">请选择停车场</h3>
            </div>
            <div class="modal-body" style="position: relative">
                <div class="spiner-example" style="position: absolute;right: 45%;top: -35%;display: none" id="loadingGraph">
                    <div class="sk-spinner sk-spinner-three-bounce">
                        <div class="sk-bounce1"></div>
                        <div class="sk-bounce2"></div>
                        <div class="sk-bounce3"></div>
                    </div>
                </div>
                <div  id="parkTable" style="background-color: #ffffff;height: 100%;" >

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 图表点击事件 -->
<a class="J_menuItem nav-label" href = "${pageContext.request.contextPath}/parkManagement/index" style="display:none">车场管理<button  id="subBtn1" type="button"></button></a>
<a class="J_menuItem nav-label" href = "${pageContext.request.contextPath}/entryDetails/index" style="display:none">车辆进出明细<button  id="subBtn2" type="button"></button></a>
<a class="J_menuItem nav-label" href = "${pageContext.request.contextPath}/costsDetails/index" style="display:none">车辆缴费明细<button  id="subBtn3" type="button"></button></a>
    <!-- 全局js -->
    <script src="${pageContext.request.contextPath}/resources/admin/js/jquery.min.js?v=2.1.4"></script>
    <script src="${pageContext.request.contextPath}/resources/admin/js/bootstrap.min.js?v=3.3.6"></script>
    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/layer/layer.min.js"></script>
    <!-- 自定义js -->
    <script src="${pageContext.request.contextPath}/resources/js/hplus.js?v=4.1.0"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/contabs.js"></script>
    <!-- 第三方插件 -->
    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/pace/pace.min.js"></script>
    <!-- jQuery Validation plugin javascript-->
    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
    <!-- Morris -->
    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/morris/raphael-2.1.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/morris/morris.js"></script>
    <script src="${pageContext.request.contextPath}/resources/admin/js/plugins/echarts/echarts-all.js"></script>
    <%--<script src="${pageContext.request.contextPath}/resources/script/run_colum.js"></script>--%>
    <script src="${pageContext.request.contextPath}/resources/script/run_line.js"></script>
    <script src="${pageContext.request.contextPath}/resources/script/highstock.js"></script>
    <script src="${pageContext.request.contextPath}/resources/script/highcharts-zh_CN.js"></script>
    <script src="${pageContext.request.contextPath}/resources/script/admin/index.js"></script>
    <c:if test="${not empty isUseBasicPlatform}">
        <script src="http://${basicAddress}/resources/public/script/menu.js"></script>
        <%--<script src="http://${basicAddress}/resources/public/script/subsystoolbar.js"></script>--%>
    </c:if>
</body>
<script>


//    //车场图表数据
//    function realParkData() {
//        //今日停车剩余
//        var total;
//        var totalData;
//        $.ajax({
//            type : 'post',
//            async : true,
//            data:{
//                parkId:parkID
//            },
//            dataType:'json',
//            url :starnetContextPath + "/carpark/getRemainPark",
//            success : function(res) {
//                if(res.data.length==0)
//                {
//                    totalData=0;
//                }
//                else {
//                    totalData=res.data.availableCarSpace;
//                }
//                var parkData1=document.getElementById("parkData1");
//                var parkWidth1=document.getElementById("parkWidth1");
//                total=res.data.totalCarSpace;
//                var width=(totalData)/(total)*100;
//                parkWidth1.style.width=width+"%";
//                parkData1.innerText=totalData;
//            }
//        });
//
//        //近七日停车剩余
//        $.ajax({
//            type : 'post',
//            async : true,
//            data:{
//                parkId:parkID
//            },
//            dataType:'json',
//            url :starnetContextPath + "/carpark/getparkRecords",
//            success : function(res) {
//                parkingLot(res);
//            }
//        });
//
//        //今日停车数量
//        $.ajax({
//            type : 'post',
//            async : true,
//            data:{
//                parkId:parkID
//            },
//            dataType:'json',
//            url :starnetContextPath + "/inout/getInoutRecordsToday",
//            success : function(res) {
//                if(res.data.length==0)
//                {
//                    totalData=0;
//                }
//                else {
//                    totalData=res.data[0][1];
//                }
//                var parkData2=document.getElementById("parkData2");
//                var parkWidth2=document.getElementById("parkWidth2");
//                var width=(totalData)/(total)*100;
//                parkWidth2.style.width=width+"%";
//                parkData2.innerText=totalData;
//
//            }
//        });
//
////近七日停车数量
//        $.ajax({
//            type : 'post',
//            async : true,
//            data:{
//                parkId:parkID
//            },
//            dataType:'json',
//            url :starnetContextPath + "/inout/getInoutRecords",
//            success : function(res) {
//                Parking_quantity(res);
//            }
//        });
//
//    }

</script>

</html>
