<%--
  Created by IntelliJ IDEA.
  User: fuhh
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
    <link href="${pageContext.request.contextPath}/resources/admin/css/AdminLTE.min.css">
    <link href="${pageContext.request.contextPath}/resources/admin/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/admin/css/bootstrap-timepicker.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/style.css?v=4.1.0" rel="stylesheet">
    <style>
        .bootstrap-timepicker-widget{
            z-index:9999 !important;
        }
    </style>
    <title></title>
</head>
<jsp:include page="../commonValue.jsp"></jsp:include>
<body class="gray-bg">

<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-11 col-lg-11">
            <div class="ibox">
                <div class="ibox-content">
                    <section class="content-header">
                    </section>
                    <section class="content">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="box box-primary box-solid">
                                    <div class="box-body">
                                        <div>
 <span class="input-group" style="width:60%;display:inline-table;">
                                                <span class='admintitle input-group-addon '>人员查询</span>

 <input id="searchtext" placeholder="请输入信息" type="text" class="form-control">
                                                <a href="javascript:void(0)" class="input-group-addon btn-white"  onclick="searchPerson()" > <i class="fa fa-search"></i> </a>

        </span>                                            <span class="input-group pull-right">
                                                <a href="#myModal" id="addFun" class="btn btn-success " data-toggle="modal" onclick="newCal()"> 新增 </a>&nbsp;
                                                <a href="" class="btn btn-primary " id="editFun" data-toggle="modal" onclick="edit()"> 编辑 </a>&nbsp;
                                                <a href="" class="btn btn-danger " id="deleteFun" data-toggle="modal" onclick="del()"> 删除 </a>&nbsp;
                                            </span>
                                        </div>
                                        <table id="callist" class="table table-striped dataTable table-bordered table-hover" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>id</th>
                                                <th>班次名称</th>
                                                <th>状态信息</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>                                        <!-- Modal -->
                                        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
                                            <div class="modal-dialog" style="z-index:1050;">
                                                <div class="modal-content">
                                                    <div class="modal-header text-center bg-purple">
                                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                                                aria-hidden="true" class="text-gray">&times;</span></button>
                                                        <h4 class="modal-title " id="myModalLabel">新增</h4>
                                                    </div>
                                                    <div class="modal-body">
                                                        <ul class="nav nav-tabs" role="tablist" id="shiftwork_Tab">
                                                            <li role="presentation" class="active"><a href="#shiftwork_info" role="tab" data-toggle="tab">基本信息</a></li>
                                                            <li role="presentation"><a href="#shiftwork_detail" role="tab" data-toggle="tab">排班详情</a></li>
                                                        </ul>

                                                        <div class="tab-content">
                                                            <div role="tabpanel" class="tab-pane active" id="shiftwork_info">
                                                                <form class="form-horizontal">
                                                                    <div class="box-body">
                                                                        <div id="fg_shiftwork_name" class="form-group bg-gray">
                                                                            <label  class="col-sm-2 control-label">班次名称 :</label>
                                                                            <div class="col-sm-10 padding-right ">
                                                                                <input type="text" id="classname" name="classname" class="form-control"  placeholder="名称">
                                                                            </div>
                                                                        </div>
                                                                        <div class="row">
                                                                            <div class="col-xs-12">
                                                                                <div class="form-group">
                                                                                    <label class="col-sm-2 control-label" >是否启用：</label>
                                                                                    <div class="col-sm-10">
                                                                                        <div class="switch" style="padding-top:5px;">
                                                                                            <div class="onoffswitch">
                                                                                                <input type="checkbox" class="onoffswitch-checkbox" id="onOoff" name="onOoff">
                                                                                                <label class="onoffswitch-label" for="onOoff">
                                                                                                    <span class="onoffswitch-inner"></span>
                                                                                                    <span class="onoffswitch-switch"></span>
                                                                                                </label>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>

                                                                            </div>
                                                                        </div>
                                                                        <div id="fg_expire" class="form-group bg-gray" hidden="true">
                                                                            <label class="col-sm-2 control-label">有效日期 :</label>
                                                                            <div class="col-sm-10 padding-right ">
                                                                                <div class="input-group">
                                                                                    <div class="input-append date datetimepicker "  data-date="12-02-2012" data-date-format="dd-mm-yyyy" style="display: inline-flex">
                                                                                        <input id="sdate_temp" class="span2 form-control" size="16" type="text" value="">
                                                                                        <span class="add-on"><i class="icon-th"></i></span>
                                                                                    </div>
                                                                                    至
                                                                                    <div class="input-append date datetimepicker inline-flex" data-date="12-02-2012" data-date-format="dd-mm-yyyy" style="display: inline-flex">
                                                                                        <input id="edate_temp" class="span2 form-control" size="16" type="text" value="">
                                                                                        <span class="add-on"><i class="icon-th"></i></span>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div><!-- /.box-body -->
                                                                </form>
                                                            </div>

                                                            <div role="tabpanel" class="tab-pane" id="shiftwork_detail">
                                                                <form class="form-horizontal m-t" id="addForm">
                                                                    <div class="box box-success" style="margin:0px;border:0px;">
                                                                        <label class="text-center" style="width: 13%;">周次</label>
                                                                        <label class="text-center" style="width: 13%;">白/夜班</label>
                                                                        <label class="text-center" style="width: 13%;">上班时间</label>
                                                                        <label class="text-center" style="width: 13%;">下班时间</label>
                                                                        <label class="text-center" style="width: 13%;">地点选择</label>
                                                                        <label class="text-center" style="width: 13%;">值班人员</label>
                                                                        <label class="text-center" style="width: 15%;">排班</label>
                                                                        <div class="input-group" style="width: 100%;">
                                                                            <div class="input-group-btn" style="width: 12%">
                                                                                <button type="button" class="form-control btn btn-xs week_btn">周日</button>
                                                                            </div>
                                                                            <select  name="select" type="text" class="form-control mname" style="width:16%;margin:2px;">
                                                                                <option>白班</option>
                                                                                <option>夜班</option>
                                                                            </select>
                                                                            <input  id="Week7_stime" name="Week7_stime_morning" type="text" class="form-control time-picker" style="width:12%; margin:2px;" placeholder="开始" value="8:30">
                                                                            <input  id="Week7_etime" name="Week7_stime1_aftrnoon" type="text" class="form-control time-picker" style="width:12%;margin:2px;" placeholder="结束" value="9:30">

                                                                            <select  type="text" name="place"   id="selectplace" class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>会议室</option>
                                                                                <option>研发场地</option>
                                                                            </select>


                                                                            <select  type="text" name="users" id="selectuser" class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>付和海</option>
                                                                                <option>郑宏伟</option>
                                                                            </select>
                                                                            <div class="col-sm-3" style="width:10%;margin:2px;">
                                                                                <div class="switch" style="padding-top:5px;">
                                                                                    <div class="onoffswitch">
                                                                                        <input type="checkbox" class="onoffswitch-checkbox" id="mUser" name="mUser">
                                                                                        <label class="onoffswitch-label" for="mUser">
                                                                                            <span class="onoffswitch-inner"></span>
                                                                                            <span class="onoffswitch-switch"></span>
                                                                                        </label>
                                                                                    </div>
                                                                                </div>
                                                                            </div>



                                                                        </div>
                                                                        <div class="input-group" style="width: 100%;">
                                                                            <div class="input-group-btn" style="width: 12%">
                                                                                <button type="button" class="form-control btn btn-xs week_btn">周一</button>
                                                                            </div>
                                                                            <select  name="select1" type="text" class="form-control mname" style="width:16%;margin:2px;">
                                                                                <option>白班</option>
                                                                                <option>夜班</option>
                                                                            </select>
                                                                            <input  id="Week1_stime_morning" name="Week1_stime_morning" type="text" class="form-control time-picker" style="width:12%; margin:2px;" placeholder="开始" value="8:30">
                                                                            <input  id="Week1_etime_afternoon"  name="Week1_etime_afternoon" type="text" class="form-control time-picker" style="width:12%;margin:2px;" placeholder="结束" value="9:30">

                                                                            <select  type="text" name="place1" id="selectplace1"  class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>会议室</option>
                                                                                <option>研发场地</option>
                                                                            </select>


                                                                            <select id="selectuser1" name="users1" type="text" class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>付和海</option>
                                                                                <option>郑宏伟</option>
                                                                            </select>
                                                                            <div class="col-sm-3" style="width:10%;margin:2px;">
                                                                                <div class="switch" style="padding-top:5px;">
                                                                                    <div class="onoffswitch">
                                                                                        <input type="checkbox" class="onoffswitch-checkbox" id="mUser1" name="mUser1">
                                                                                        <label class="onoffswitch-label" for="mUser1">
                                                                                            <span class="onoffswitch-inner"></span>
                                                                                            <span class="onoffswitch-switch"></span>
                                                                                        </label>
                                                                                    </div>
                                                                                </div>
                                                                            </div>



                                                                        </div>
                                                                        <div class="input-group" style="width: 100%;">
                                                                            <div class="input-group-btn" style="width: 12%">
                                                                                <button type="button" class="form-control btn btn-xs week_btn">周二</button>
                                                                            </div>
                                                                            <select  name="select2" type="text" class="form-control mname" style="width:16%;margin:2px;">
                                                                                <option>白班</option>
                                                                                <option>夜班</option>
                                                                            </select>
                                                                            <input  id="Week2_stime" name="Week2_stime_morning" type="text" class="form-control time-picker" style="width:12%; margin:2px;" placeholder="开始" value="8:30">
                                                                            <input  id="Week2_etime" name="Week2_etime_afternoon"type="text" class="form-control time-picker" style="width:12%;margin:2px;" placeholder="结束" value="9:30">

                                                                            <select  type="text"  name="place2"  id="selectplace2"  class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>会议室</option>
                                                                                <option>研发场地</option>
                                                                            </select>


                                                                            <select  id="selectuser2" type="text" name="user2" class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>付和海</option>
                                                                                <option>郑宏伟</option>
                                                                            </select>
                                                                            <div class="col-sm-3" style="width:10%;margin:2px;">
                                                                                <div class="switch" style="padding-top:5px;">
                                                                                    <div class="onoffswitch">
                                                                                        <input type="checkbox" class="onoffswitch-checkbox" id="mUser2" name="mUser2" >
                                                                                        <label class="onoffswitch-label" for="mUser2">
                                                                                            <span class="onoffswitch-inner"></span>
                                                                                            <span class="onoffswitch-switch"></span>
                                                                                        </label>
                                                                                    </div>
                                                                                </div>
                                                                            </div>



                                                                        </div>
                                                                        <div class="input-group" style="width: 100%;">
                                                                            <div class="input-group-btn" style="width: 12%">
                                                                                <button type="button" class="form-control btn btn-xs week_btn">周三</button>
                                                                            </div>
                                                                            <select  type="text" name="select3"  class="form-control mname" style="width:16%;margin:2px;">
                                                                                <option>白班</option>
                                                                                <option>夜班</option>
                                                                            </select>
                                                                            <input  id="Week3_stime"  name="Week3_stime_morning" type="text" class="form-control time-picker" style="width:12%; margin:2px;" placeholder="开始" value="8:30">
                                                                            <input  id="Week3_etime" name="Week3_stime_afternoon"  type="text" class="form-control time-picker" style="width:12%;margin:2px;" placeholder="结束" value="9:30">

                                                                            <select  name="place3" type="text"  id="selectplace3"  class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>会议室</option>
                                                                                <option>研发场地</option>
                                                                            </select>


                                                                            <select id="selectuser3" name="users3" type="text" class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>付和海</option>
                                                                                <option>郑宏伟</option>
                                                                            </select>
                                                                            <div class="col-sm-3" style="width:10%;margin:2px;">
                                                                                <div class="switch" style="padding-top:5px;">
                                                                                    <div class="onoffswitch">
                                                                                        <input type="checkbox" class="onoffswitch-checkbox" id="mUser3" name="mUser3">
                                                                                        <label class="onoffswitch-label" for="mUser3">
                                                                                            <span class="onoffswitch-inner"></span>
                                                                                            <span class="onoffswitch-switch"></span>
                                                                                        </label>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                        <div class="input-group" style="width: 100%;">
                                                                            <div class="input-group-btn" style="width: 12%">
                                                                                <button type="button" class="form-control btn btn-xs week_btn">周四</button>
                                                                            </div>
                                                                            <select  name="select4" type="text" class="form-control mname" style="width:16%;margin:2px;">
                                                                                <option>白班</option>
                                                                                <option>夜班</option>
                                                                            </select>
                                                                            <input  id="Week4_stime" name="Week4_stime_morning" type="text" class="form-control time-picker" style="width:12%; margin:2px;" placeholder="开始" value="8:30">
                                                                            <input  id="Week4_etime" name="Week4_stime_afternoon"  type="text" class="form-control time-picker" style="width:12%;margin:2px;" placeholder="结束" value="9:30">

                                                                            <select  name="place4" type="text"  id="selectplace4" class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>会议室</option>
                                                                                <option>研发场地</option>
                                                                            </select>


                                                                            <select  id="selectuser4" name="users4" type="text" class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>付和海</option>
                                                                                <option>郑宏伟</option>
                                                                            </select>
                                                                            <div class="col-sm-3" style="width:10%;margin:2px;">
                                                                                <div class="switch" style="padding-top:5px;">
                                                                                    <div class="onoffswitch">
                                                                                        <input type="checkbox" class="onoffswitch-checkbox" id="mUser4" name="mUser4">
                                                                                        <label class="onoffswitch-label" for="mUser4">
                                                                                            <span class="onoffswitch-inner"></span>
                                                                                            <span class="onoffswitch-switch"></span>
                                                                                        </label>
                                                                                    </div>
                                                                                </div>
                                                                            </div>



                                                                        </div>
                                                                        <div class="input-group" style="width: 100%;">
                                                                            <div class="input-group-btn" style="width: 12%">
                                                                                <button type="button" class="form-control btn btn-xs week_btn">周五</button>
                                                                            </div>
                                                                            <select  type="text" name="select5" class="form-control mname" style="width:16%;margin:2px;">
                                                                                <option>白班</option>
                                                                                <option>夜班</option>
                                                                            </select>
                                                                            <input  id="Week5_stime" name="Week5_stime_morning" type="text" class="form-control time-picker" style="width:12%; margin:2px;" placeholder="开始" value="8:30">
                                                                            <input  id="Week5_etime" name="Week5_etime_afternoon" type="text" class="form-control time-picker" style="width:12%;margin:2px;" placeholder="结束" value="9:30">

                                                                            <select  type="text" name="place5" id="selectplace5"  class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>会议室</option>
                                                                                <option>研发场地</option>
                                                                            </select>


                                                                            <select  id="selectuser5" type="text" name="users5" class="form-control mname" style="width:19%;margin:2px;">
                                                                                <option value='0'>付和海</option>
                                                                                <option>郑宏伟</option>
                                                                            </select>
                                                                            <div class="col-sm-3" style="width:10%;margin:2px;">
                                                                                <div class="switch" style="padding-top:5px;">
                                                                                    <div class="onoffswitch">
                                                                                        <input type="checkbox" class="onoffswitch-checkbox" id="mUser5" name="mUser5">
                                                                                        <label class="onoffswitch-label" for="mUser5">
                                                                                            <span class="onoffswitch-inner"></span>
                                                                                            <span class="onoffswitch-switch"></span>
                                                                                        </label>
                                                                                    </div>
                                                                                </div>
                                                                            </div>



                                                                        </div>
                                                                        <div class="input-group" style="width: 100%;">
                                                                            <div class="input-group-btn" style="width: 12%">
                                                                                <button type="button" class="form-control btn btn-xs week_btn">周六</button>
                                                                            </div>
                                                                            <select  type="text" name="select6" class="form-control mname" style="width:16%;margin:2px;">
                                                                                <option>白班</option>
                                                                                <option>夜班</option>
                                                                            </select>
                                                                            <input  id="Week6_stime"  name="Week6_stime_morning" type="text" class="form-control time-picker" style="width:12%; margin:2px;" placeholder="开始" value="8:30">
                                                                            <input  id="Week6_etime" name="Week6_etime_afternoon" type="text" class="form-control time-picker" style="width:12%;margin:2px;" placeholder="结束" value="9:30">

                                                                            <select  type="text" class="form-control mname" name="place6" id="selectplace6"  style="width:19%;margin:2px;">
                                                                                <option value='0'>会议室</option>
                                                                                <option>研发场地</option>
                                                                            </select>


                                                                            <select  type="text" class="form-control mname" name="users6" id="selectuser6" style="width:19%;margin:2px;">
                                                                                <option value='0'>付和海</option>
                                                                                <option>郑宏伟</option>
                                                                            </select>
                                                                            <div class="col-sm-3" style="width:10%;margin:2px;">
                                                                                <div class="switch" style="padding-top:5px;">
                                                                                    <div class="onoffswitch">
                                                                                        <input type="checkbox" class="onoffswitch-checkbox" id="mUser6" name="mUser6">
                                                                                        <label class="onoffswitch-label" for="mUser6">
                                                                                            <span class="onoffswitch-inner"></span>
                                                                                            <span class="onoffswitch-switch"></span>
                                                                                        </label>
                                                                                    </div>
                                                                                </div>
                                                                            </div>



                                                                        </div>
                                                                        <div>
                                                                            <label class="text-center" style="width: 30%;">可迟到早退时间:</label>
                                                                            <select id="tol" class="text-center" style="width:20%;margin:2px;border-color: black;"> <option value='0'>无</option><option value='5'>5分钟</option> <option value='10'>10分钟</option><option value='30'>30分种</option></select>
                                                                        </div>
                                                                    </div>
                                                                </form>
                                                            </div>
                                                        </div>
                                                        <div class="box-footer">
                                                            <button type="submit" class="btn btn-default" data-dismiss="modal">关闭</button>
                                                            <a  id="save" class="btn btn-success ">保存</a>
                                                        </div><!-- /.box-footer -->
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div><!-- /.nav-tabs-custom -->
                                </div>
                            </div> </div>
                    </section>
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
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/resources/admin/js/content.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/bootstrap-timepicker.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/admin/securitytrajectory.js"></script>





</body>

</html>