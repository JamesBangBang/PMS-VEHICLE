/**
 * Created by 宏炜 on 2017-06-26.
 */
$(function(){
    initNodeInfo();
    //initMapExample();

});
function initNodeInfo(){
    var now = new Date();
    var start = new Date(now.getTime() - (15 * 60 * 1000)).Format("yyyy-MM-dd hh:mm:ss");
    var end = now.Format("yyyy-MM-dd hh:mm:ss");

    var params = {
        type: 3,
        timeType: 1,
        startMonth:start,
        stopMonth:end
    };
    var nodeValue = new Array();
    $.post(starnetContextPath + "/count/getCountMacList",params,function(res){
        if(res.result == 0){
            var data = res.data;
            for(var i in data){
                var node = new Object();
                node.value = data[i].scount + data[i].tcount + data[i].ucount;
                node.name = data[i].org_node_name;
                node.lon = data[i].node_longitude;
                node.lat = data[i].node_latitude;
                node.dev = data[i].dev_id;
                nodeValue.push(node);
            }
            initMap(nodeValue);
            setInterval(showWarning,10000);
        }else{
            errorPrompt("区域热度信息加载失败",res.msg);
        }
    },'json');

}
function initMap(infoObj){
    var map = new BMap.Map("allmap");    // 创建Map实例
    map.centerAndZoom(new BMap.Point(parseFloat(sysLon),parseFloat(sysLat)), 20);  // 初始化地图,设置中心点坐标和地图级别
    map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
    map.setCurrentCity("重庆");          // 设置地图显示的城市 此项是必须设置的
    map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
    var i = 0;
    var points = [];
    var markers = [];
    var j =0;
    for (var item in infoObj) {
        var point =  {"lng":infoObj[item].lon,"lat":infoObj[item].lat,"count":infoObj[item].value};
        // at
        //     :
        //     29.460389
        // lng
        //     :
        //     106.540542\


        // 29.457067
        // lng
        //     :
        //     106.533018
        //     var p1 = {"lng":106.534442,"lat":29.458289,"count":100};
        //     var p2 = {"lng":106.534441,"lat":29.459598,"count":100};
        // var p3 = {"lng":106.554441,"lat":29.459598,"count":100};
        points.push(point);
        //     points.push(p1);
        //     points.push(p2);
        // points.push(p3);
        var marker = new BMap.Marker(point);
        //     var marker = new BMap.Marker(p1);
        //     var marker2 = new BMap.Marker(p2);
        // var marker3 = new BMap.Marker(p3);
        var lableName = '<a href="javascript:openVideo(\''+infoObj[item].dev+'\')">'+infoObj[item].name+'</a>';
        var label = new BMap.Label(lableName,{offset:new BMap.Size(20,20)});
        label.setStyle({
            "border":"none"
        });
        //     var label = new BMap.Label('ddddddd',{offset:new BMap.Size(20,20)});
        //     var labe2 = new BMap.Label('ttttttt',{offset:new BMap.Size(20,20)});
        // var labe3= new BMap.Label('ttttttt',{offset:new BMap.Size(20,20)});
        marker.setLabel(label);
        //     marker2.setLabel(labe2);
        // marker3.setLabel(labe3);
        markers.push(marker)
        // for(var i=0;i<200;i++){
        //     markers.push(marker)
        //     markers.push(marker2)
        //     markers.push(marker3)
        // }
        // alert(markers.length)

    }
//对地图级别变化、移动结束和图块加载完毕后，进行添加marker的操作
    map.addEventListener("tilesloaded", function(){
        for(i=0;i<markers.length;i++){
            // 获取经纬度范围参数
            var bs = map.getBounds();   //获取可视区域
            var bssw = bs.getSouthWest();   //可视区域左下角
            var bsne = bs.getNorthEast();   //可视区域右上角

            var topLat = bsne.lat;
            var bottomLat = bssw.lat;
            var leftLng = bssw.lng;
            var rightLng = bsne.lng;
            if(markers[i].point.lat<topLat&&markers[i].point.lat>bottomLat&&markers[i].point.lng>leftLng&&markers[i].point.lng<rightLng)
            {
                map.addOverlay(markers[i]);
            }

            // var result = BMapLib.GeoUtils.isPointInRect(markers[i].point, map.getBounds());

            // if(result == true){
            //     map.addOverlay(markers[i]);
            // }
            // else{
            //     map.removeOverlay(markers[i]);
            // }
        }

    });
    map.addEventListener("zoomend",  function(){

        for(i=0;i<markers.length;i++){
            // 获取经纬度范围参数
            var bs = map.getBounds();   //获取可视区域
            var bssw = bs.getSouthWest();   //可视区域左下角
            var bsne = bs.getNorthEast();   //可视区域右上角
            var topLat = bsne.lat;
            var bottomLat = bssw.lat;
            var leftLng = bssw.lng;
            var rightLng = bsne.lng;
            if(markers[i].point.lat<topLat&&markers[i].point.lat>bottomLat&&markers[i].point.lng>leftLng&&markers[i].point.lng<rightLng)
            {
                map.addOverlay(markers[i]);
            }

            // var result = BMapLib.GeoUtils.isPointInRect(markers[i].point, map.getBounds());

            // if(result == true){
            //     map.addOverlay(markers[i]);
            // }
            // else{
            //     map.removeOverlay(markers[i]);
            // }
        }

    });
    map.addEventListener("moveend", function(){
        for(i=0;i<markers.length;i++){
            // 获取经纬度范围参数
            var bs = map.getBounds();   //获取可视区域
            var bssw = bs.getSouthWest();   //可视区域左下角
            var bsne = bs.getNorthEast();   //可视区域右上角

            var topLat = bsne.lat;
            var bottomLat = bssw.lat;
            var leftLng = bssw.lng;
            var rightLng = bsne.lng;
            if(markers[i].point.lat<topLat&&markers[i].point.lat>bottomLat&&markers[i].point.lng>leftLng&&markers[i].point.lng<rightLng)
            {
                map.addOverlay(markers[i]);
            }

            // var result = BMapLib.GeoUtils.isPointInRect(markers[i].point, map.getBounds());

            // if(result == true){
            //     map.addOverlay(markers[i]);
            // }
            // else{
            //     map.removeOverlay(markers[i]);
            // }
        }

    });
    if(!isSupportCanvas()){
        alert('热力图目前只支持有canvas支持的浏览器,您所使用的浏览器不能使用热力图功能~')
    }
//详细的参数,可以查看heatmap.js的文档 https://github.com/pa7/heatmap.js/blob/master/README.md
//参数说明如下:
    /* visible 热力图是否显示,默认为true
     * opacity 热力的透明度,1-100
     * radius 势力图的每个点的半径大小
     * gradient  {JSON} 热力图的渐变区间 . gradient如下所示
     *	{
     .2:'rgb(0, 255, 255)',
     .5:'rgb(0, 110, 255)',
     .8:'rgb(100, 0, 255)'
     }
     其中 key 表示插值的位置, 0~1.
     value 为颜色值.
     */
    heatmapOverlay = new BMapLib.HeatmapOverlay({"radius":20});
    map.addOverlay(heatmapOverlay);
    // console.log(points);
    heatmapOverlay.setDataSet({data:points,max:100});
//是否显示热力图
    function openHeatmap(){
        heatmapOverlay.show();
    }
    function closeHeatmap(){
        heatmapOverlay.hide();
    }
    openHeatmap();
    function setGradient(){
        /*格式如下所示:
         {
         0:'rgb(102, 255, 0)',
         .5:'rgb(255, 170, 0)',
         1:'rgb(255, 0, 0)'
         }*/
        var gradient = {};
        var colors = document.querySelectorAll("input[type='color']");
        colors = [].slice.call(colors,0);
        colors.forEach(function(ele){
            gradient[ele.getAttribute("data-key")] = ele.value;
        });
        heatmapOverlay.setOptions({"gradient":gradient});
    }
//判断浏览区是否支持canvas
    function isSupportCanvas(){
        var elem = document.createElement('canvas');
        return !!(elem.getContext && elem.getContext('2d'));
    }
}
function initMapExample(){
    var map = new BMap.Map("allmap");
    var point = new BMap.Point(116.404, 39.915);
    map.centerAndZoom(point, 11);

    //创建小狐狸
    var pt = new BMap.Point(116.417, 39.909);

    var myIcon = new BMap.Icon("http://xy3.star-netsecurity.com/resources/images/wifi-48.png", new BMap.Size(300,157));
    var marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
    map.addOverlay(marker2);
}
function showWarning(){
    var now = new Date();
    var start = new Date(now.getTime() - (15 * 60 * 1000)).Format("yyyy-MM-dd hh:mm:ss");
    var end = now.Format("yyyy-MM-dd hh:mm:ss");

    var params = {
        type: 3,
        timeType: 1,
        startMonth:start,
        stopMonth:end
    };
    $.post(starnetContextPath + "/count/getCountMacList",params,function(res){
        if(res.result == 0){
            var data = res.data;
            for(var i in data){
                var node = new Object();
                node.value = data[i].scount + data[i].tcount + data[i].ucount;
                node.name = data[i].org_node_name;
                if(node.value > maxNum){
                    setTimeout(errorPrompt('人数超限预警','位置：[' + node.name + '] 当前人数' + node.value + '，超过 ' + maxNum + '人上限值'),1000);

                }
            }
        }else{
            errorPrompt("区域热度信息加载失败",res.msg);
        }
    },'json');
}

function openVideo(devId){
    var params = {
        devId:devId
    };


    $.post(starnetContextPath + "/screen/nowScreen",params,function(res){
        if(res.result == 0){
            $("#videoModal").modal("show");
            SnCloud_StartVideo(res.data.serverIp,res.data.serverPort,res.data.username,res.data.password,res.data.channelId);
        }else{
            errorPrompt("实时视频信息加载失败",res.msg);
        }
    });
}
$('#videoModal').on('hide.bs.modal', function () {
    SnCloud_StopVideo();
})
function addMymarkers(markers,map){

    for(i=0;i<markers.length;i++){
        // 获取经纬度范围参数
        var bs = map.getBounds();   //获取可视区域
        var bssw = bs.getSouthWest();   //可视区域左下角
        var bsne = bs.getNorthEast();   //可视区域右上角

        var topLat = bsne.lat;
        var bottomLat = bssw.lat;
        var leftLng = bssw.lng;
        var rightLng = bsne.lng;
        if(markers[i].point.lat<topLat&&markers[i].point.lat>bottomLat&&markers[i].point.lng>leftLng&&markers[i].point.lng<rightLng)
        {
            map.addOverlay(markers[i]);
        }

        // var result = BMapLib.GeoUtils.isPointInRect(markers[i].point, map.getBounds());

        // if(result == true){
        //     map.addOverlay(markers[i]);
        // }
        else{
            map.removeOverlay(markers[i]);
        }
    }
}
