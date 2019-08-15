/**
 * Created by 宏炜 on 2017-06-23.
 */
$(function(){
    initCalendar("","");

    $("#mapTrajectory").click(function(){
        $("#trajectoryModal").modal("show");
    });
});

var mapArray = new Array();
function initCalendar(trueName,macAddress,crowdType){
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



            if(crowdType!=null)
            {
                var params = {
                    start: now.getTime(),
                    end: end.getTime(),
                    type: crowdType
                }

                $.post(starnetContextPath + "/path/getCrowdList", params, function (res) {
                    if(res.result == 0){
                        var events = [];
                        for(var i in res.data){
                            var eventObj = new Object();
                            eventObj.title = "　";
                            eventObj.mac_date = res.data[i].mac_date;
                            // alert(res.data[i].mac)
                            eventObj.start = res.data[i].mac_date + " 00:00:00";
                            eventObj.end = res.data[i].mac_date + " 23:00:00";
                            eventObj.macs = res.data[i].macs;
                            // alert(eventObj.macs)
                            // eventObj.macs_array=res.

                            eventObj.color = "gray";
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
                }, 'json');
            }
else{




//改这里

            var params = {
                start: now.getTime(),
                end: end.getTime(),
                mac:macAddress,
                name:trueName

            };

            $.post(starnetContextPath + "/path/getmonthList",params,function(res){
                if(res.result == 0){
                    var events = [];
                    for(var i in res.data){
                        var eventObj = new Object();
                        eventObj.title = "　";
                        eventObj.mac_date = res.data[i].mac_date;
                        eventObj.start = res.data[i].mac_date + " 00:00:00";
                        eventObj.end = res.data[i].mac_date + " 23:00:00";
                        eventObj.mac = res.mac;

                        eventObj.color = "gray";
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
            }
        },
        eventClick: function(calEvent, jsEvent, view) {

            var params = {
                month: calEvent.mac_date,
                mac:calEvent.mac,
                macs:calEvent.macs
            }
            $.post(starnetContextPath + "/path/getDayPathList",params,function(res){
                $(macContainer).empty()
                if(res.result == 0){
                    $("#timelineContainer").empty();
                    if(res.macflag=="true"){
                        var width = res.data.length * 365;
                        $("#macContainer").css("width",width + "px");

                        mapArray = new Array();
                        for(var i in res.data){
                            var timeLineContainerId = "timelineContainer" + i;
                            var containerItem = '<div style="width:360px;float:left;overflow:auto;"> ' +
                                '<div class="timeline" id="' + timeLineContainerId + '">' +
                                '    <div style="margin-left:4%" >'+res.data[i].name+'</div> '   +
                                '</div></div>';
                            $("#macContainer").append(containerItem);
                            // var pointArray = new  Array();
                            var object = new  Object();
                             object.pointArray = new  Array();
                            object.name=res.data[i].name;
                            for(var j in res.data[i].list){
                                var stime = new Date(res.data[i].list[j].stime).Format("hh:mm:ss");
                                var etime = new Date(res.data[i].list[j].etime).Format("hh:mm:ss");
                                var item = '<div class="timeline-item">' +
                                    '<div class="row"> '+
                                    '<div class="col-xs-3 date"> ' +
                                    '<i class="fa fa-file-text"></i> 停留 '+ res.data[i].list[j].staytime + ' 分钟'+
                                    '</div> '+
                                    '<div class="col-xs-7 content no-top-border"> ' +
                                    '<p class="m-b-xs"> ' +
                                    '<strong>'+  res.data[i].list[j].place_name + '</strong> ' +
                                    '<br> '+
                                    '<small class="text-navy">进入时间:'+ stime + '</small> ' +
                                    '<br> '+
                                    '<small class="text-navy">离开时间:'+ etime + '</small> ' +
                                    '</p>'+
                                    '</div>'+
                                    '</div>'+
                                    '</div>';

                                try {
                                    var point = new BMap.Point( res.data[i].list[j].node_longitude, res.data[i].list[j].node_latitude);
                                    object.pointArray.push(point);
                                }catch(e){
                                    return;
                                }
                                $("#"+timeLineContainerId).append(item);
                            }

                            mapArray.push(object);

                        }

                        var divHeight = '<div style="clear: both;margin-bottom: 10px;"></div>'
                        $("#macContainer").append(divHeight);

                    }
                    if(res.macflag=="false"){
                        var width = res.data.length * 365;
                        $("#macContainer").css("width",width + "px");
                        var timeLineContainerId = "timelineContainer" + i;
                        var containerItem = '<div style="width:360px;float:left;overflow:auto;"> ' +
                            '<div class="timeline" id="' + timeLineContainerId + '">' +
                            '    <div style="margin-left:4%" >'+res.name+'</div> '   +
                            '</div></div>';
                        $("#macContainer").append(containerItem);
                        var object = new  Object();
                        object.pointArray = new  Array();
                        object.name=res.name;
                        mapArray = new Array();
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
                                    '<strong>'+  res.data[i].place_name + '</strong> ' +
                                    '<br> '+
                                    '<small class="text-navy">进入时间:'+ stime + '</small> ' +
                                    '<br> '+
                                    '<small class="text-navy">离开时间:'+ etime + '</small> ' +
                                    '</p>'+
                                    '</div>'+
                                    '</div>'+
                                    '</div>';
                             try {
                                 var point = new BMap.Point( res.data[i].node_longitude, res.data[i].node_latitude);
                                 object.pointArray.push(point);
                             }catch(e){
                                 return;
                             }

                                $("#"+timeLineContainerId).append(item);



                        }
                        mapArray.push(object);

                        var divHeight = '<div style="clear: both;margin-bottom: 10px;"></div>'
                        $("#macContainer").append(divHeight);
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
function searchTrajectory(type){
    if(type==0)
    {
        initCalendar(null,null,$("#list-select").val());
    }


    else if(type == 1){
        initCalendar($("#queryName").val(),null,null,null);
    }else{
        initCalendar(null,$("#queryMac").val(),null,null);
    }


}

  function initMap(){
      // 百度地图API功能
      var map = new BMap.Map("allmap");
      map.centerAndZoom(new BMap.Point(parseFloat(sysLon),parseFloat(sysLat)), 20);
      map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
      // var walking = new BMap.WalkingRoute(map, { renderOptions: { map: map, autoViewport: true} });
      map.setCurrentCity("福州");          // 设置地图显示的城市 此项是必须设置的
      map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

      // var myP1 = new BMap.Point(119.221534,26.042809);    //起点
      // var myP2 = new BMap.Point(119.213534,26.042809);    //终点
      // var myP3=new BMap.Point(119.211534,26.042809)
      // var myP4=new BMap.Point(119.201434,26.042809)
      // pointArray.push(myP1);
      // pointArray.push(myP2);
      // pointArray.push(myP3);
      // pointArray.push(myP4);

      //用来存放途经点的坐标



      var myIcon = new BMap.Icon("http://developer.baidu.com/map/jsdemo/img/Mario.png", new BMap.Size(32, 70), {    //小车图片
          //offset: new BMap.Size(0, -5),    //相当于CSS精灵
          imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
      });
// console.log(mapArray)
      for(var i in mapArray){
          // console.log(mapArray[i].pointArray)
          var viaRouteData = [];
          for(var j=1;j< mapArray[i].pointArray.length-1;j++){

              viaRouteData.push( mapArray[i].pointArray[j]);
          }
          var driving2 = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true}});    //驾车实例
          driving2.search(  mapArray[i].pointArray[0],  mapArray[i].pointArray[mapArray[i].pointArray.length-1], {waypoints:viaRouteData});    //显示一条公交线路
          var marker = new BMap.Marker(mapArray[i].pointArray[0]);  // 创建标注
          var lableName = '<a >'+mapArray[i].name+'</a>';
          var label = new BMap.Label(lableName,{offset:new BMap.Size(20,20)});
          label.setStyle({
              "border":"none"
          });
          marker.setLabel(label);
          map.addOverlay(marker);
      }


      window.run = function (){
          var driving = new BMap.DrivingRoute(map);    //驾车实例

          for(var i in mapArray){

              var viaRouteData = [];
              for(var j=1;j< mapArray[i].pointArray.length-1;j++){

                  viaRouteData.push( mapArray[i].pointArray[j]);
              }
              var driving2 = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true}});    //驾车实例
              driving2.search(  mapArray[i].pointArray[0],  mapArray[i].pointArray[mapArray[i].pointArray.length-1], {waypoints:viaRouteData});    //显示一条公交线路
              var marker = new BMap.Marker(mapArray[i].pointArray[0]);  // 创建标注
              var lableName = '<a >'+mapArray[i].name+'</a>';
              var label = new BMap.Label(lableName,{offset:new BMap.Size(20,20)});
              label.setStyle({
                  "border":"none"
              });
              marker.setLabel(label);
              map.addOverlay(marker);
          }
          driving.setSearchCompleteCallback(function(){
              var pts = driving.getResults().getPlan(0).getRoute(0).getPath();    //通过驾车实例，获得一系列点的数组
              var paths = pts.length;    //获得有几个点
              var carMk = new BMap.Marker(pts[0],{icon:myIcon});
              map.addOverlay(carMk);
              i=0;
              function resetMkPoint(i){
                  carMk.setPosition(pts[i]);
                  if(i < paths){
                      setTimeout(function(){
                          i++;
                          resetMkPoint(i);
                      },100);
                  }
              }
              setTimeout(function(){
                  resetMkPoint(5);
              },100)
          });
      }

      setTimeout(function(){
          run();
      },1500);
}
$('#trajectoryModal').on('shown.bs.modal', function () {
    initMap();
})