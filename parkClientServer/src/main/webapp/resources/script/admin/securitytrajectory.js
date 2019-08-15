/**
 * Created by 宏炜 on 2017-06-23.
 */
$(function(){
    iniDataTable();
    initUserSelect();
    initUserSelect1();
    initUserSelect2();
    initUserSelect3();
    initUserSelect4();
    initUserSelect5();
    initUserSelect6();
    initDevSelect();
    initDevSelect1()
    initDevSelect2()
    initDevSelect3()
    initDevSelect4()
    initDevSelect5()
    initDevSelect6()
    initCalendar("","");
    $("#mapTrajectory").click(function(){
        $("#trajectoryModal").modal("show");
    });
    $('#save').click(function(){
        // if (! $("#addForm").valid()) {
        //     return;
        // }

        // var params = $("#addForm").serialize();
        // var params1 = $("#addForm").serializeArray();

        var params = $("#addForm").serializeObject();
        params.classname=$("#classname").val();
        params.onOoff=$("#onOoff").val();

        ajaxReuqest(starnetContextPath + "/security/addSecurity","post",params,function (res) {
            if(res.result == 0){
                $("#myModal").modal("hide");
                // mainTable.ajax.reload(null,false);
                successfulPrompt("添加成功","");
            }else{
                errorPrompt("添加失败",res.msg);
            }
        });
    });
    $('.time-picker').timepicker({
        minuteStep:5,
    }).on('show', function(e){
        $('.time-picker').click(function(event) {
            event.stopPropagation();
        });
    });

});

var pointArray = new Array();
function initCalendar(trueName,macAddress){
    $('#calendar').fullCalendar( 'destroy' );
    $('#calendar').fullCalendar({
        header: {
            left: '',
            center: 'title',
            right: ''
        },
        height:400,
        editable: false,
        droppable: false,
        events: function(start, end, callback) {
            var now = new Date();
            var params = {
                trueName:trueName

            };
            $.post(starnetContextPath + "/security/selectWork",params,function(res){
                if(res.result == 0){
                    var events = [];
                    for(var i in res.data){
                        var eventObj = new Object();
                        eventObj.title = "　";
                        eventObj.mac_date = res.data[i].mac_date;
                        eventObj.start = res.data[i].mac_date + " 00:00:00";
                        eventObj.end = res.data[i].mac_date + " 23:00:00";
                        eventObj.mac = res.mac;
                        eventObj.mac = res.mac;
                        eventObj.color = "green";
                        if(res.userType == "1"){
                            eventObj.color= "red";
                        }
                        if(res.userType == "0"){
                            eventObj.color = "green";
                        }
                        if(res.userType == "2"){
                            eventObj.color = "blue";
                        }
                        events.push(eventObj);
                    }
                    callback(events);
                }else{
                    errorPrompt("考勤信息加载失败",res.msg);
                }
            },'json');

        },
        eventClick: function(calEvent, jsEvent, view) {
            var params = {
                month: calEvent.mac_date,
                mac:calEvent.mac
            }
            $.post(starnetContextPath + "/path/getDayPathList",params,function(res){
                if(res.result == 0){
                    $("#timelineContainer").empty();
                    pointArray = new Array();
                    for(var i in res.data){
                        var stime = new Date(res.data[i].stime).Format("hh:mm:ss");
                        var etime = new Date(res.data[i].etime).Format("hh:mm:ss");
                        var item = '<div class="timeline-item">' +
                            '<div class="row"> '+
                            '<div class="col-xs-3 date"> ' +
                            '<i class="fa fa-file-text"></i> 停留 '+ res.data[i].staytime + ' 分钟'+
                            '</div> '+
                            '<div class="col-xs-7 content no-top-border"> ' +
                            '<p class="m-b-xs"> ' +
                            '<strong>'+ res.data[i].place_name + '</strong> ' +
                            '<br> '+
                            '<small class="text-navy">进入时间:'+ stime + '</small> ' +
                            '<br> '+
                            '<small class="text-navy">离开时间:'+ etime + '</small> ' +
                            '</p>'+
                            '</div>'+
                            '</div>'+
                            '</div>';
                        var point = new BMap.Point(res.data[i].node_longitude,res.data[i].node_latitude);
                        pointArray.push(point);
                        $("#timelineContainer").append(item);
                    }
                }else{
                    errorPrompt("轨迹详情加载失败",res.msg);
                }
            },'json');

        }
        // eventClick: function(calEvent, jsEvent, view) {
        //     var params = {
        //         month: calEvent.mac_date,
        //         mac:calEvent.mac
        //     }
        //
        //     $.post(starnetContextPath + "/path/getDayPathList",params,function(res){
        //         if(res.result == 0){
        //
        //             $("#timelineContainer").empty();
        //             for(var i in res.data){
        //                 var stime = new Date(res.data[i].stime).Format("hh:mm:ss");
        //                 var etime = new Date(res.data[i].etime).Format("hh:mm:ss");
        //                 var item = '<div class="timeline-item">' +
        //                     '<div class="row"> '+
        //                     '<div class="col-xs-3 date"> ' +
        //                     '<i class="fa fa-file-text"></i> 停留 '+ res.data[i].staytime + ' 分钟'+
        //                     '</div> '+
        //                     '<div class="col-xs-7 content no-top-border"> ' +
        //                     '<p class="m-b-xs"> ' +
        //                     '<strong>'+ res.data[i].place_name + '</strong> ' +
        //                     '<br> '+
        //                     '<small class="text-navy">进入时间:'+ stime + '</small> ' +
        //                     '<br> '+
        //                     '<small class="text-navy">离开时间:'+ etime + '</small> ' +
        //                     '</p>'+
        //                     '</div>'+
        //                     '</div>'+
        //                     '</div>';
        //                 $("#timelineContainer").append(item);
        //             }
        //         }else{
        //             errorPrompt("轨迹详情加载失败",res.msg);
        //         }
        //     },'json');
        //
        // }
    });
}


function initMap(){
    var map = new BMap.Map("allmap");    // 创建Map实例
    map.centerAndZoom(new BMap.Point(parseFloat(sysLon),parseFloat(sysLat)), 20);  // 初始化地图,设置中心点坐标和地图级别
    map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
    map.setCurrentCity("福州");          // 设置地图显示的城市 此项是必须设置的
    map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
    // pointArray.push(new BMap.Point(119.221534,26.042809));
    var polyline = new BMap.Polyline(pointArray, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});   //创建折线
    map.addOverlay(polyline);

    var i = 0;
    var points = [];
    for (var item in pointArray) {
        (function (x) {

            //创建标注
            var point = pointArray[item]
            points[i] = point;
            var marker = new BMap.Marker(point);  // 创建标注
            // var label = new BMap.Label(infoObj[item].name,{offset:new BMap.Size(65,65)});
            // label.setStyle({
            //     "border":"none"
            // });
            // marker.setLabel(label);
            map.addOverlay(marker);              // 将标注添加到地图中
            i++;
        })(i);
    }
}
$('#trajectoryModal').on('shown.bs.modal', function () {
    initMap();
})

/**
 * 添加、修改、删除数据
 **/
function newCal(){
    nid=0;

    $("#myModalLabel").text("新增");
    $("#classname").val('新班次');
    //显示第一页，有 bug
    $("#shiftwork_detail").removeClass('active');
    $("#shiftwork_info").addClass('active');
    //默认固定班次。好麻烦！
    $("#option1").attr('checked','checked');
    $("#option1").parent().addClass('active');
    $("#option2").removeAttr('checked');
    $('#fg_expire').hide();

    //默认时间
    for(var i=1;i<=7;i++){
        $("#Week"+i+"_stime").timepicker('setTime',"8:30");
        $("#Week"+i+"_etime").timepicker('setTime',"9:30");

        $("#Week"+i+"_stime2").timepicker('setTime',"16:30");
        $("#Week"+i+"_etime2").timepicker('setTime',"17:30");
    }
    for(var i=1;i<=5;i++){
        $("#Week"+i+"_stime").removeAttr("disabled");
        $("#Week"+i+"_etime").removeAttr("disabled");
        $("#Week"+i+"_stime2").attr("disabled","disabled");
        $("#Week"+i+"_etime2").attr("disabled","disabled");
        $("#Week"+i+"_etime").next().removeAttr("disabled");
        $("#Week"+i+"_etime").next().val('0');
        $("#Week"+i+"_stime").prev().removeAttr("disabled");
        $("#Week"+i+"_stime").prev().prev().children().addClass("btn-info");
    }
    $("#tol").val(10);

}

/**
 *编辑
 **/
function edit() {
    $("#myModalLabel").text("修改");
    //得到当前选中的数据对象，格式就是网络返回的json
    var aData = g_otable.getSelected();

    if (aData){
        //进入编辑模式，影响接口参数的填写
        nid = aData.id;
        /* 开始初始化表格，逐行设置 */
        $("#classname").val(aData.name);
        if(aData.sdate&&aData.edate){
            $("#option1").removeAttr('checked');
            $("#option2").attr('checked','checked');
            $("#option1").removeClass('active');
            $("#option2").addClass('active');
            $("#sdate_temp").val(aData.sdate);
            $("#edate_temp").val(aData.edate);
        }

        for(var i=1;i<=7;i++){
            $("#Week"+i+"_stime").parent().children().attr('disabled','disabled');
            $("#Week"+i+"_stime").prev().prev().children().removeClass("btn-info");
            $("#Week"+i+"_stime").timepicker('setTime',"8:30");
            $("#Week"+i+"_etime").timepicker('setTime',"9:30");

            $("#Week"+i+"_stime2").prev().val('0');
            $("#Week"+i+"_stime2").timepicker('setTime',"16:30");
            $("#Week"+i+"_etime2").timepicker('setTime',"17:30");
        }

        var DaysShort = ["周", "周一", "周二", "周三", "周四", "周五", "周六","周日"];
        var DaysLookup=[];
        var DaysCount=[];
        for(var i in DaysShort)
            DaysLookup[DaysShort[i]] = i;

        var tol=0;
        $.each(aData.worktime,function(i,item){
            if(item.tol>0)
                tol=item.tol;

            var idx = DaysLookup[item.dname];
            if(idx>0 && idx<=7) {
                $("#Week"+idx+"_stime").prev().prev().children().addClass("btn-info");
                $("#Week"+idx+"_stime").parent().children('select').removeAttr('disabled');
                if(DaysCount[item.dname]==null) { //当天第一个时段
                    $("#Week"+idx+"_stime").removeAttr('disabled');
                    $("#Week"+idx+"_etime").removeAttr('disabled');
                    $("#Week"+idx+"_stime").timepicker('setTime',((item.stime-item.stime%100)/100)+':'+(item.stime%100));
                    $("#Week"+idx+"_etime").timepicker('setTime',((item.etime-item.etime%100)/100)+':'+(item.etime%100));
                    DaysCount[item.dname] = 1;
                }else {
                    $("#Week"+idx+"_stime2").removeAttr('disabled');
                    $("#Week"+idx+"_etime2").removeAttr('disabled');
                    $("#Week"+idx+"_stime2").prev().val(item.name);
                    $("#Week"+idx+"_stime2").timepicker('setTime',((item.stime-item.stime%100)/100)+':'+(item.stime%100));
                    $("#Week"+idx+"_etime2").timepicker('setTime',((item.etime-item.etime%100)/100)+':'+(item.etime%100));
                    DaysCount[item.dname] ++;
                }
            }
        });
        $("#tol").val(tol);

        //弹出编辑框
        $("#myModal").modal("show");

    } else {
        userid = 0;
        Hint('请点击选择一条记录后操作!',2000);
    }

}
function del(){
    var aData = g_otable.getSelected();
    if(aData){
        Confirm("确认删除该记录吗？删除后所有关联的部门考试时间将恢复默认。",function(result){
            if(result==true){
                var delJson = {"id": aData.id};
                ajax(delJson);
            }
        });
    }else {
        Hint('请点击选择一条记录后操作!',1000);
    }
}



function initDevSelect(){

    $.post(starnetContextPath + "/node/getAllNode",function(res){
        if(res.result == 0){
            $('#selectplace').empty();
            var defaultItem = '<option value="">全部位置</option>';
            $('#selectplace').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].orgNodeName + '</option>';
                $('#selectplace').append(item);
            }
            $('#selectplace').chosen({
                width:"100%"
            });

            $('#selectplace').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}

function initDevSelect1(){

    $.post(starnetContextPath + "/node/getAllNode",function(res){
        if(res.result == 0){
            $('#selectplace1').empty();
            var defaultItem = '<option value="">全部位置</option>';
            $('#selectplace1').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].orgNodeName + '</option>';
                $('#selectplace1').append(item);
            }
            $('#selectplace1').chosen({
                width:"100%"
            });

            $('#selectplace1').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });
}
    function initDevSelect2(){

        $.post(starnetContextPath + "/node/getAllNode",function(res){
            if(res.result == 0){
                $('#selectplace2').empty();
                var defaultItem = '<option value="">全部位置</option>';
                $('#selectplace2').append(defaultItem);
                for(var i in res.data){
                    var item = '<option value="' + res.data[i].id + '" >' + res.data[i].orgNodeName + '</option>';
                    $('#selectplace2').append(item);
                }
                $('#selectplace2').chosen({
                    width:"100%"
                });

                $('#selectplace2').trigger("chosen:updated");
            }else{
                warningPrompt("获取设备信息失败","");
            }
        });

    }


function initDevSelect3(){

    $.post(starnetContextPath + "/node/getAllNode",function(res){
        if(res.result == 0){
            $('#selectplace3').empty();
            var defaultItem = '<option value="">全部位置</option>';
            $('#selectplace3').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].orgNodeName + '</option>';
                $('#selectplace3').append(item);
            }
            $('#selectplace3').chosen({
                width:"100%"
            });

            $('#selectplace3').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}

function initDevSelect4(){

    $.post(starnetContextPath + "/node/getAllNode",function(res){
        if(res.result == 0){
            $('#selectplace4').empty();
            var defaultItem = '<option value="">全部位置</option>';
            $('#selectplace4').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].orgNodeName + '</option>';
                $('#selectplace4').append(item);
            }
            $('#selectplace4').chosen({
                width:"100%"
            });

            $('#selectplace4').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}

function initDevSelect5(){

    $.post(starnetContextPath + "/node/getAllNode",function(res){
        if(res.result == 0){
            $('#selectplace5').empty();
            var defaultItem = '<option value="">全部位置</option>';
            $('#selectplace5').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].orgNodeName + '</option>';
                $('#selectplace5').append(item);
            }
            $('#selectplace5').chosen({
                width:"100%"
            });

            $('#selectplace5').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}

function initDevSelect6(){

    $.post(starnetContextPath + "/node/getAllNode",function(res){
        if(res.result == 0){
            $('#selectplace6').empty();
            var defaultItem = '<option value="">全部位置</option>';
            $('#selectplace6').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].orgNodeName + '</option>';
                $('#selectplace6').append(item);
            }
            $('#selectplace6').chosen({
                width:"100%"
            });

            $('#selectplace6').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}

function initUserSelect(){

    $.post(starnetContextPath + "/person/getAllPerson",function(res){
        if(res.result == 0){
            $('#selectuser').empty();
            var defaultItem = '<option value="">全部保安人员</option>';
            $('#selectuser').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].true_name+ '</option>';
                $('#selectuser').append(item);
            }
            $('#selectuser').chosen({
                width:"100%"
            });

            $('#selectuser').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}

function initUserSelect1(){

    $.post(starnetContextPath + "/person/getAllPerson",function(res){
        if(res.result == 0){
            $('#selectuser1').empty();
            var defaultItem = '<option value="">全部保安人员</option>';
            $('#selectuser1').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].true_name+ '</option>';
                $('#selectuser1').append(item);
            }
            $('#selectuser1').chosen({
                width:"100%"
            });

            $('#selectuser1').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}

function initUserSelect2(){

    $.post(starnetContextPath + "/person/getAllPerson",function(res){
        if(res.result == 0){
            $('#selectuser2').empty();
            var defaultItem = '<option value="">全部保安人员</option>';
            $('#selectuser2').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].true_name+ '</option>';
                $('#selectuser2').append(item);
            }
            $('#selectuser2').chosen({
                width:"100%"
            });

            $('#selectuser2').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}

function initUserSelect3(){

    $.post(starnetContextPath + "/person/getAllPerson",function(res){
        if(res.result == 0){
            $('#selectuser3').empty();
            var defaultItem = '<option value="">全部保安人员</option>';
            $('#selectuser3').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].true_name+ '</option>';
                $('#selectuser3').append(item);
            }
            $('#selectuser3').chosen({
                width:"100%"
            });

            $('#selectuser3').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}


function initUserSelect4(){

    $.post(starnetContextPath + "/person/getAllPerson",function(res){
        if(res.result == 0){
            $('#selectuser4').empty();
            var defaultItem = '<option value="">全部保安人员</option>';
            $('#selectuser4').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].true_name+ '</option>';
                $('#selectuser4').append(item);
            }
            $('#selectuser4').chosen({
                width:"100%"
            });

            $('#selectuser4').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}


function initUserSelect5(){

    $.post(starnetContextPath + "/person/getAllPerson",function(res){
        if(res.result == 0){
            $('#selectuser5').empty();
            var defaultItem = '<option value="">全部保安人员</option>';
            $('#selectuser5').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].true_name+ '</option>';
                $('#selectuser5').append(item);
            }
            $('#selectuser5').chosen({
                width:"100%"
            });

            $('#selectuser5').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}


function initUserSelect6(){

    $.post(starnetContextPath + "/person/getAllPerson",function(res){
        if(res.result == 0){
            $('#selectuser6').empty();
            var defaultItem = '<option value="">全部保安人员</option>';
            $('#selectuser6').append(defaultItem);
            for(var i in res.data){
                var item = '<option value="' + res.data[i].id + '" >' + res.data[i].true_name+ '</option>';
                $('#selectuser6').append(item);
            }
            $('#selectuser6').chosen({
                width:"100%"
            });

            $('#selectuser6').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}




/*数据删除*/

function deleteWork(rowIndex){
    var deleteData = mainTable.row(rowIndex).data();
    var params = {
        workId:deleteData.id
    }

    $.post(starnetContextPath + "/security/delteWork",params,function(res){
        if(res.result == 0){
            mainTable.ajax.reload(null,false);
            successfulPrompt("删除成功","");
        }else{
            errorPrompt("删除失败",res.msg);
        }
    });
}












/*表格数据查询*/


var mainTable;
function iniDataTable(){
    mainTable = $('#callist').DataTable({
        sort:false,
        processing:true,
        serverSide:true,
        searching:false,
        autoWidth:true,
        ajax: {
            url: starnetContextPath + "/security/getAllWork",
            type: 'POST',
            data:{
            }
        },
        columns:[

            {"data":"id"},
            {"data":"name"},
            {"data":"use_statues" },



        ],
        "columnDefs": [

            {
                "render": function(data, type, row, meta) {
                    if(data==1){
                        data='启用'
                    }
                    else data='未启用'
                    return data
                },
                "targets": 2
            },

            {
                "render": function(data, type, row, meta) {
                    return ' <button class="btn btn-danger btn-sm" onclick="deleteWork('+meta.row+')">删除</button>';
                },
                "targets": 3
            },





            {

                "targets": [0],
                "visible": false
            }

        ],
        language:dataTableLanguage

    });
}

/*人员信息查询*/

function searchPerson(){
    initCalendar($("#searchtext").val(),null);
    // $.ajax({
    //     type: "POST",
    //     url: "/security/selectWork",
    //     data: {
    //         "message":$("#searchtext").val()
    //     }
    //
    // });


}

