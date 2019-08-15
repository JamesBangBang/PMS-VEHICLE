/**
 * Created by   on 2017-09-15.
 */
$(function(){
    $.post(starnetContextPath + '/carpark/getParksMapInfoList',function (res) {
        if(res.result == 0){
            initMap(res.data)
        }else{
            alert(res.msg);
        }
    },'json');
});

  function initMap(infoObj){
      // 百度地图API功能
      var map = new BMap.Map("allmap");    // 创建Map实例
      map.centerAndZoom(new BMap.Point(112.221534,28.042809), 6);  // 初始化地图,设置中心点坐标和地图级别
      map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
      map.setCurrentCity("福州");          // 设置地图显示的城市 此项是必须设置的
      map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

      map.setMapStyle({style:'midnight'});


      var i = 0;
      var points = [];
      for (var item in infoObj) {
         console.log(infoObj);
          (function (x) {
              //创建标注
              var point = new BMap.Point(infoObj[item].lon,infoObj[item].lat);
              points[i] = point;
              var marker = new BMap.Marker(point);  // 创建标注
              var label = new BMap.Label('<span style="color:#fff;background-color: rgba(0,0,0,0.3)">' + infoObj[item].carparkName + '</span>',{offset:new BMap.Size(20,-10)});
              label.setStyle({
                  "border-color": "#12406A"
              });
              marker.setLabel(label);
              map.addOverlay(marker);              // 将标注添加到地图中
              i++;
          })(i);
      }

}
