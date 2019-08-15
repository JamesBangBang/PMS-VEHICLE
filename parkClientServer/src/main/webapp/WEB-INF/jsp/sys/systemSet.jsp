<!DOCTYPE html>
<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2016-12-26
  Time: 15:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%-- easyui css --%>
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${pageContext.request.contextPath}/resources/admin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/font-awesome.css?v=4.4.0" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/jsTree/style.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/switchery/switchery.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <%-- easyui js --%>

    <title></title>
    <style>

        .input-focus:focus {
            outline:none;
            border: 1px solid #0096FF;
            /*border: 2px solid #18a689;*/
        }

        /*.input-focus{*/
            /*!*border: 2px solid #18a689;*!*/
        /*}*/

        .input-rounded{
            border-radius: 0px;
            border:solid #18a689 2px;
        }
        .input-text{
            padding-left:50px;
        }

        .fa-border-set{
            background-color: #fff;
            border: 2px solid #18a689;
            width: 50px;
            height: 34px;
            border-radius: 7px;
            color: inherit;
            font-size: 17px;
            font-weight: 400;
            line-height: 1;
            padding: 6px 12px;
            text-align: center;
        }

        .div-inline{
            display:inline;
        }

        .mid-icon {
            font-size: 160px;
            color: #e5e6e7;
        }
    </style>

</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="gray-bg">



<div  class=" slimScrollDiv full-height wrapper-content" style=" margin: auto; width: 100% ">


    <div class="ibox float-e-margins ">

//


        <%--<div class="row">--%>
            <%--<div class="col-xs-5">--%>
                <%--<div class="input-group m-b">--%>
                    <%--<span class="input-group-addon"><i class="fa fa-sitemap"></i></span>--%>
                    <%--<input type="text" placeholder="请输入组织机构名称" class="form-control">--%>
                    <%--<span class="input-group-btn">--%>
                                        <%--<button type="button" class="btn btn-success">搜索</button>--%>
                                    <%--</span>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div class="col-xs-7">--%>
                <%--<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addModal">新增</button>--%>
                <%--<button type="button" class="btn btn-danger" id="delBtn">删除</button>--%>
            <%--</div>--%>
        <%--</div>--%>

        //
        <div class="ibox-title">

            <div class="row">

                <div class="col-xs-5">
                    <h2>信息设置</h2>
                </div>

                <div style="position:absolute;right:5%;" >
                    <button class="btn btn-danger " type="button" id="reduction" onclick=""><i class="fa fa-repeat"></i>&nbsp;还原</button>
                </div>

            </div>

        </div>

        <div class="ibox-content " style=" position: relative; height: 739px;">

            <div style="position: absolute; right:0px;top:0px ">
                <span class="fa fa-gears mid-icon" ></span>
            </div>



            <form id="from"  class="form-horizontal" target="postTo"  >

                <div class="row">
                    <form class="form-horizontal" >
                        <div class="form-group" style="margin-top: 30px;">
                            <label class="col-sm-3 control-label">系统标题:</label>

                            <div class="col-sm-4">
                                <input  id="systemTitle"  name="systemTitle" value="${formData.systemTitle}"  data-options="required:true,prompt:'请输入版权所有信息'" missingMessage="请输入版权所有信息" class=" easyui-textbox form-control ">
                            </div>
                        </div>
                        <div class="form-group" style="margin-top: 30px;">
                            <label class="col-sm-3 control-label">版权所有:</label>

                            <div class="col-sm-4">
                                <input   id="copyRight"  name="copyRight" value="${formData.copyRight}"  data-options="required:true,prompt:'请输入版权所有信息'" missingMessage="请输入版权所有信息" class=" easyui-textbox form-control  ">
                            </div>
                        </div>
                        <div class="form-group" style="margin-top: 30px;">
                            <label class="col-sm-3 control-label">经度：</label>

                            <div class="col-sm-4">
                                <input    id="systemLon" name="systemLon" value="${formData.systemLon}"  data-options="required:true,prompt:'请输入经度所有信息'" missingMessage="请输入经度所有信息" class=" easyui-textbox form-control">
                            </div>
                        </div>
                        <div class="form-group" style="margin-top: 30px;">
                            <label class="col-sm-3 control-label">纬度：</label>

                            <div class="col-sm-4">
                                <input   id="systemLat"   name="systemLat" value="${formData.systemLat}"  data-options="required:true,prompt:'请输入纬度所有信息'" missingMessage="请输入纬度所有信息" class="easyui-textbox form-control ">
                            </div>
                        </div>
                        <div class="form-group" style="margin-top: 30px;">
                            <label class="col-sm-3 control-label">普通：</label>

                            <div class="col-sm-4">
                                <input id="middleNum" name="middleNum" value="${formData.middleNum}"  data-options="required:true,prompt:'请输入普通所有信息'" missingMessage="请输入普通所有信息" class=" easyui-textbox form-control ">
                            </div>
                        </div>
                        <div class="form-group" style="margin-top: 30px;">
                            <label class="col-sm-3 control-label">密集：</label>

                            <div class="col-sm-4">
                                <input   id="maxNum"  name="maxNum" value="${formData.maxNum}"  data-options="required:true,prompt:'请输入密集所有信息'" missingMessage="请输入密集所有信息" class=" easyui-textbox form-control  input-focus">
                            </div>
                        </div>

                        <div class="form-group" style="margin-top: 30px;">
                            <div class="col-sm-offset-3 col-sm-8">
                                <a href="javascript:void(0)" id="systemSetBtn" class="easyui-linkbutton btn btn-primary input-rounded">提交</a>
                            </div>
                        </div>
                    </form>
                </div>


                <%--<div class="form-group">--%>
                    <%--<label class="col-sm-3 control-label" style="position: relative; bottom: -35px;  " >系统标题:</label>--%>

                    <%--<div class="col-sm-5" style="position: relative;">--%>
                        <%--<input   style="position: relative; bottom: -34px;  " id="systemTitle"  name="systemTitle" value="${formData.systemTitle}"  data-options="required:true,prompt:'请输入版权所有信息'" missingMessage="请输入版权所有信息" class=" easyui-textbox form-control  input-rounded input-focus input-text">--%>
                        <%--<div class="fa-border-set " style="position: relative; ">  <i class="fa fa-lg fa-gear " ></i></div>--%>
                    <%--</div>--%>

                <%--</div>--%>


                <%--<div class="form-group" >--%>
                    <%--<label class="col-sm-3 control-label" style="position: relative; bottom: -15px;  " >版权所有:</label>--%>

                    <%--<div class="col-sm-5" style="position: relative; bottom:20px;">--%>
                        <%--<input   style="position: relative; bottom: -34px; "  id="copyRight"  name="copyRight" value="${formData.copyRight}"  data-options="required:true,prompt:'请输入版权所有信息'" missingMessage="请输入版权所有信息" class=" easyui-textbox form-control  input-rounded input-focus input-text">--%>
                        <%--<div class="fa-border-set " style="position: relative; ">  <span class="fa fa-lg fa-gear "></span></div>--%>
                    <%--</div>--%>

                <%--</div>--%>

                <%--<div class="form-group">--%>
                    <%--<label class="col-sm-3 control-label" style="position: relative; bottom: 5px;  " >经度：</label>--%>

                    <%--<div class="col-sm-5" style="position: relative; bottom:40px;"  >--%>
                        <%--<input   style="position: relative; bottom: -34px; "  id="systemLon" name="systemLon" value="${formData.systemLon}"  data-options="required:true,prompt:'请输入经度所有信息'" missingMessage="请输入经度所有信息" class=" easyui-textbox form-control  input-rounded input-focus input-text">--%>

                        <%--<div class="fa-border-set " style="position: relative; ">  <span class="fa fa-lg fa-gear "></span></div>--%>
                    <%--</div>--%>
                <%--</div>--%>


                <%--<div class="form-group" >--%>
                    <%--<label class="col-sm-3 control-label" style="position: relative; bottom:25px;" >纬度：</label>--%>

                    <%--<div class="col-sm-5" style="position: relative; bottom:60px; ">--%>
                        <%--<input     style="position: relative; bottom: -34px;" id="systemLat"  name="systemLat" value="${formData.systemLat}"  data-options="required:true,prompt:'请输入纬度所有信息'" missingMessage="请输入纬度所有信息" class="form-control  input-rounded input-focus input-text">--%>

                        <%--<div class="fa-border-set " style="position: relative; ">  <span class="fa fa-lg fa-gear "></span></div>--%>
                    <%--</div>--%>

                <%--</div>--%>


                <%--<div class="form-group" >--%>
                    <%--<label class="col-sm-3 control-label" style="position: relative; bottom: 45px; ">普通：</label>--%>

                    <%--<div class="col-sm-5" style="position: relative; bottom:80px; ">--%>
                        <%--<input style="position: relative; bottom: -34px; " id="middleNum" name="middleNum" value="${formData.middleNum}"  data-options="required:true,prompt:'请输入普通所有信息'" missingMessage="请输入普通所有信息" class=" easyui-textbox form-control  input-rounded input-focus input-text">--%>

                        <%--<div class="fa-border-set " style="position: relative; ">  <span class="fa fa-lg fa-gear "></span></div>--%>
                    <%--</div>--%>

                <%--</div>--%>

                <%--<div class="form-group"  >--%>
                    <%--<label class="col-sm-3 control-label"  style="position: relative; bottom: 65px; ">密集：</label>--%>

                    <%--<div class="col-sm-5" style="position: relative; bottom:100px; ">--%>
                        <%--<input  style="position: relative; bottom: -34px; "  id="maxNum"  name="maxNum" value="${formData.maxNum}"  data-options="required:true,prompt:'请输入密集所有信息'" missingMessage="请输入密集所有信息" class=" easyui-textbox form-control  input-rounded input-focus input-text">--%>

                        <%--<div class="fa-border-set " style="position: relative; ">  <span class="fa fa-lg fa-gear "></span></div>--%>
                    <%--</div>--%>

                <%--</div>--%>

                <%--<div class="form-group">--%>
                    <%--<div class="col-sm-offset-3 col-sm-8"style="position: relative; bottom:80px; " >--%>
                        <%--<a href="javascript:void(0)" id="systemSetBtn" class="easyui-linkbutton btn btn-primary input-rounded">提交</a>--%>
                    <%--</div>--%>
                <%--</div>--%>

            </form>
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

<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/jsTree/jstree.min.js"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/toastr/toastr.min.js"></script>
<!-- Switchery -->
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/switchery/switchery.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/system.js"></script>

</html>