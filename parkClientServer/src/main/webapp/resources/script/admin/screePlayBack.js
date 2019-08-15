/**
 * Created by 宏炜 on 2017-06-23.
 */
$(function(){

    initTree("");


    $("#devId").chosen({
        width:"100%"
    });



    var start = {
        elem: '#start',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59', //最大日期
        istime: true,
        istoday: false,
        start: '2017-6-15 23:00:00',
        choose: function (datas) {
            end.min = datas; //开始日选好后，重置结束日的最小日期
            end.start = datas //将结束日的初始值设定为开始日
        }
    };
    var end = {
        elem: '#end',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    laydate(start);
    laydate(end);
    $('#start').val(laydate.now(-1, 'YYYY-MM-DD hh:mm:ss'));
    $('#end').val(laydate.now(0, 'YYYY-MM-DD hh:mm:ss'));
    var treeHeight = document.body.offsetHeight - 95;
    $('.full-height-scroll').slimScroll({
        height: treeHeight + 'px'
    });
    // iniDataTable();

    $("#searchBtn").click(function(){
        var start=$("#start").val();
        var  end=$("#end").val();
        var devId=$('#devId').val();
        openVideo(devId,start,end);
    });

});


function openVideo(devId,start,end){
    var params = {
        devId:devId
    };
    $.post(starnetContextPath + "/screen/nowScreen",params,function(res){
        if(res.result == 0){

            SnCloud_StartPlayback(res.data.serverIp,res.data.serverPort,res.data.username,res.data.password,res.data.channelId, start,end);
        }else{
            errorPrompt("回放视频信息加载失败",res.msg);
        }
    });
}


function initTree(orgId){
    $.post(starnetContextPath + "/org/orgTree",function(res){
        if(res.result == 0){
            $('#orgTree').jstree('destroy');
            $('#orgTree').jstree({
                'core': {
                    'multiple':false,
                    'data':res.data
                }
            });

            $("#orgTree").on("loaded.jstree",function(){
                if(orgId != "") {
                    $("#orgTree").jstree().select_node(orgId, false, false);
                }
            });
            $("#orgTree").on("select_node.jstree",function(event,node){
                var data = node.node;
                $('#orgName').val(data.text);
                $('#orgId').val(data.id);
                initDevSelect(data.id);
                var params = {
                    "orgId":data.id,
                    "userType":$("#queryUserType").val()
                }
                // mainTable.settings()[0].ajax.data = params;
                // mainTable.ajax.reload(null,false);
            });
        }else{
            warningPrompt("获取组织机构树信息失败","");
        }
    });
}

function initDevSelect(orgid){
    $.post(starnetContextPath + "/node/getOrgNode?orgId="+orgid,function(res){
        if(res.result == 0){
            $('#devId').empty();
            for(var i in res.devices){
                var item = '<option value="' + res.devices[i].id + '" >' + res.devices[i].org_node_name + '</option>';
                $('#devId').append(item);
            }

            $('#devId').chosen({
                width:"100%"
            });
            $('#devId').trigger("chosen:updated");
        }else{
            warningPrompt("获取设备信息失败","");
        }
    });

}

$("#start").click(function () {

    // setTimeout("appendIframe()",100);

});

function appendIframe(){
    var item = '<iframe id="iframe1" src="about:blank" frameBorder="0" marginHeight="0" marginWidth="0" style="position:absolute; visibility:inherit; top:0px;left:0px;width:120px; height:120px;z-index:-1; filter:alpha(opacity=0);"></iframe>';
    $('#laydate_box').append(item);
}

