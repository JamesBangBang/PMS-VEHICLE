/**
 * Created by 宏炜 on 2017-12-29.
 */
var deviceManage;
var ledPlayType;
var deleteData;
var mainTable;
$(function () {

    deviceManage = {
        autoTable:null,
        editData:null,
        ledTable:null,
        ledTableFour:null,
        audioTable:null,
        emptyData:{

        },
        ocxObject:null,
        render:function () {
            this.initTable();
            this.initOcx();
            this.bindOnEditModalHide();
            this.bindRefreshBtnClick();
            $( '#file-pretty input[type="file"]' ).prettyFile();
            this.bindDrawLineBtn();
            $(".timepicker").timepicker({
                minuteStep: 5,
                showInputs: false,
                disableFocus: true,
                showMeridian:false
            });
            this.onchangeLightMode();
            this.onChangeLightTime();
            this.bindSubmitBasicForm();
            this.basicFormValidate();
            this.bindSaveDeviceSystem();
            this.sysFormValidate();
            this.onSubIpcIdChange();
            this.onSysRelateIpcIdChange();
            this.bindSaveLightAndOsd();
            this.lightFormValidate();
            this.bindSaveSceneInfo();
            this.bindFormatSdCard();
            this.bindRebootDev();
            this.bindSaveLedBtn();
            this.bindSaveAudioBtn();
            this.bindTimeFormatBtn();
            this.ondeviceLedTypeChange();
            this.delFunc();
        },
        initOcx:function(){
            this.ocxObject = new ocxFuncObject();

        },
        initTable:function () {
            this.autoTable = $('#autoTable').DataTable( {
                sort:false,
                processing:true,
                serverSide:true,
                searching:false,
                autoWidth:true,
                ajax: {
                    url: starnetContextPath + "/device/manage/page/list",
                    type: 'POST'
                },
                columns:[
                    {"data":"deviceName"},
                    {"data":"deviceIp"},
                    {"data":"devicePort"},
                    {"data":"deviceUsername"},
                    {"data":"devicePwd"},
                    {"data":"deviceType"},
                    {"data":"carRoadName"},
                    {"data":"devStatus"},
                    {"data":"deviceId"},
                    {"data":"carRoadId"},
                    {"data":"subIpcId"},
                    {"data":"relateIpcId"},
                    {"data":"relateTime"}
                ],
                "columnDefs": [
                    {
                        "render": function(data, type, row, meta) {
                            return '******';
                        },
                        "targets": 4
                    },
                    {
                        "render": function(data, type, row, meta) {
                            return '<button class="btn btn-info btn-sm" onclick="deviceManage.editFunc('+meta.row+')">配置</button> '
                                + '<button class="btn  btn-sm btn-danger" onclick="deviceManage.deleteFunction('+meta.row+')">删除</button>';
                        },
                        "targets": 8
                    },
                    {
                        "render": function(data, type, row, meta) {
                            if(data == 0){
                                return "摄像机";
                            }else if(data == 2){
                                return "道闸";
                            }else if(data == 3){
                                return "显示屏";
                            }else if(data == 10){
                                return "播报器";
                            }else{
                                return data;
                            }
                        },
                        "targets": 5
                    },
                    {
                        "render": function(data, type, row, meta) {
                            if(data == 1){
                                return '<span style="color: #00A400;">在线</span>'
                            }else{
                                return '<span class="text-danger">离线</span>'
                            }
                        },
                        "targets": 7
                    },
                    {
                        "visible": false,
                        "targets": [9,10,11,12]
                    }
                ],
                language:dataTableLanguage

            });
        },

        editFunc:function (rowIndex) {
            var that = this;
            this.editData = this.autoTable.row(rowIndex).data();
            $("#sysDevId").val(this.editData.deviceId);
            if(this.editData.deviceType == 0){
                $.post(starnetContextPath + "/device/manage/device/info",{"id":this.editData.deviceId},function (res) {
                    if(res.result == 0){

                        $("#sysForm").populateForm(res.data.sysParams);
                        $("#sysForm").populateForm(res.data.gateMode);
                        $("#basicForm").populateForm(res.data.netParams);
                        $("#basicForm").populateForm(res.data.portParams);
                        $("#deviceName").val(res.data.deviceName);
                        $("#lightForm").populateForm(res.data.lightParams);
                        $("#lightForm").populateForm(res.data.osdParams);
                        $("#lightForm").populateForm(res.data.osdoverlayParams);

                        $("#secneFrom").populateForm(res.data.secneInfo);
                        $("#secneFrom").populateForm(res.data.recoSceneInfo);
                        if (res.data.ledDevInfo.deviceType == "3"){
                            ledPlayType = "3";
                            $("#deviceLedType").val("threeRow");
                            $("#ledPlayThree")[0].style.display = "block";
                            $("#ledPlayFour")[0].style.display = "none";
                            deviceManage.ledTable = null;
                            deviceManage.ledTable = deviceManage.initLedTable();
                            /*if(deviceManage.ledTable == null){
                                deviceManage.ledTable = deviceManage.initLedTable();
                            }else{
                                deviceManage.ledTable.ajax.url = starnetContextPath + "/device/manage/led/list?devId=" + res.data.ledDevInfo.deviceId;
                                deviceManage.ledTable.ajax.reload();
                            }*/
                        } else if (res.data.ledDevInfo.deviceType == "11"){
                            ledPlayType = "11";
                            $("#deviceLedType").val("fourRow");
                            $("#ledPlayThree")[0].style.display = "none";
                            $("#ledPlayFour")[0].style.display = "block";
                            if(deviceManage.ledTableFour == null){
                                deviceManage.ledTableFour = deviceManage.initLedTableFour();
                            }else{
                                deviceManage.ledTableFour.ajax.url = starnetContextPath + "/device/manage/led/list?devId=" + $('#basicDevId').val();
                                deviceManage.ledTableFour.ajax.reload();
                            }
                        }else {
                            ledPlayType = "12";
                            $("#deviceLedType").val("guideScreen");
                            $("#ledPlayThree")[0].style.display = "none";
                            $("#ledPlayFour")[0].style.display = "none";
                        }
                        $("#ledForm").populateForm(res.data.ledDevInfo);
                        $("#audioForm").populateForm(res.data.audioDevInfo);

                        if(res.data.lightParams.mode == 2){
                            $('#lightTime').hide();
                            $('#lightDelay').show();

                        }else if(res.data.lightParams.mode == 3){
                            var start = pad(res.data.lightParams.onTime_h,2) + ":" + pad(res.data.lightParams.onTime_m,2);
                            var end = pad(res.data.lightParams.offTime_h,2) + ":" + pad(res.data.lightParams.offTime_m,2);
                            $('#dayStartTime').timepicker('setTime', start);
                            $("#dayEndTime").timepicker('setTime',end);
                            $('#lightTime').show();
                            $('#lightDelay').hide();

                        }else{
                            $('#lightTime').hide();
                            $('#lightDelay').hide();
                        }


                        that.ocxObject.init({
                            "url":that.editData.deviceIp,
                            "wServerPort":"8081",
                            "username":that.editData.deviceUsername,
                            "password":that.editData.devicePwd,
                            "channel":"1",
                            "trantype":"Tcp",
                            "streamType":"1",
                            "httpport":"554",
                            "ocxObjectId":"iparkOcx",
                            "drawLine":false,
                            "drawRectange":false,
                            "drawRegionX":res.data.secneInfo["region.x"],
                            "drawRegionY":res.data.secneInfo["region.y"],
                            "drawRegionW":res.data.secneInfo["region.width"],
                            "drawRegionH":res.data.secneInfo["region.height"],
                            "drawPX1":res.data.secneInfo["region.x1"],
                            "drawPY1":res.data.secneInfo["region.y1"],
                            "drawPX2":res.data.secneInfo["region.x2"],
                            "drawPY2":res.data.secneInfo["region.y2"],
                            "drawPX3":res.data.secneInfo["region.x3"],
                            "drawPY3":res.data.secneInfo["region.y3"],
                            "drawPX4":res.data.secneInfo["region.x4"],
                            "drawPY4":res.data.secneInfo["region.y4"],
                            "drawPX5":res.data.secneInfo["region.x5"],
                            "drawPY5":res.data.secneInfo["region.y5"]
                        });
                        that.ocxObject.startView();
                    }
                });
                $("#sysForm").populateForm(this.editData);
                $("#basicForm").populateForm(this.editData);

                if(deviceManage.audioTable == null){
                    deviceManage.audioTable = deviceManage.initAudioTable();
                }else{
                    deviceManage.audioTable.ajax.url = starnetContextPath + "/device/manage/audio/list?devId=" + $('#basicDevId').val();
                    deviceManage.audioTable.ajax.reload();
                }


                $("#lightForm").populateForm(this.editData);
                $("#secneFrom").populateForm(this.editData);


                $("#editModal").modal("show");
            }else if(this.editData.deviceType == 2){

            }


        },

        bindOnEditModalHide:function () {
            var that = this;
            $('#editModal').on('hidden.bs.modal', function (e) {
                that.ocxObject.stopView();
            });
        },

        deleteFunction: function (rowIndex) {
            var data = this.autoTable.row(rowIndex).data();
            deleteData = data;
            $("#deleteModal").modal("show");
        },

        delFunc:function () {
            $('#deleteBtn').unbind("click").bind("click", function () {
                var params = {
                    "id": deleteData["deviceId"]
                };
                $.post(starnetContextPath + '/device/manage/delete', params, function (res) {
                    if (res.result == 0) {
                        $("#deleteModal").modal("hide");
                        successfulPrompt("设备删除成功", "");
                        deviceManage.autoTable.ajax.reload(null, true);
                    } else {
                        errorPrompt("操作失败", res.msg);
                    }
                }, 'json');
            });
        },
        bindSubmitClick: function () {
            $('#submitBtn').unbind("click").bind("click", function () {
                if (! $("#videoForm").valid()) {
                    return;
                }

                var params = $("#videoForm").serializeObject();

                ajaxReuqest(starnetContextPath + "/device/manage/merge",'post',params,function(res){
                    if(res.result == 0){
                        $("#editModal").modal("hide");
                        deviceManage.autoTable.ajax.reload(null,true);
                        successfulPrompt("操作成功","");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                });
            });
        },
        bindAddDeviceBtnClick:function () {
            $('#addDeviceBtn').unbind("click").bind("click", function () {
                $("#videoForm").populateForm(deviceManage.emptyData);
                $("#isAutoDeal").bootstrapSwitch("state",true);
                $("#isVoicePlay").bootstrapSwitch("state",true);
                $("#editModal").modal("show");

            });
        },
        bindRefreshBtnClick:function () {

            $('#refreshBtn').unbind("click").bind("click", function () {
                deviceManage.autoTable.ajax.reload(null,true);
            });
        },
        basicFormValidate:function(){
            var icon = "<i class='fa fa-times-circle'></i> ";
            $("#basicForm").validate({
                rules: {
                    deviceName: {
                        required: true
                    },
                    ipv4Address:{
                        required: true,
                        isIPV4:true
                    },
                    subnetMask:{
                        required: true,
                        isIPV4:true
                    },
                    httpPort:{
                        required: true
                    },
                    gatewayAddress:{
                        isIPV4:true
                    }
                },
                messages: {
                    deviceName: {
                        required: icon + "设备名称不能为空"
                    },
                    ipv4Address:{
                        required: icon + "设备IP不能为空"

                    },
                    subnetMask:{
                        required: icon + "子网掩码不能为空",
                        isIPV4:icon + "请输入正确的子网掩码"
                    },
                    httpPort:{
                        required: icon + "设备端口不能为空"
                    },
                    gatewayAddress:{
                        isIPV4:icon + "请输入正确的网关"
                    }
                }
            });
        },
        bindDrawLineBtn:function () {
            var that = this;
            $('#drawRectangle').unbind("click").bind("click", function () {
                that.ocxObject.setDrawLine(true);
                that.ocxObject.setDrawRectange(true);
                that.ocxObject.startDrawLine();
                $('#drawRectangle').hide();
                $('#drawPolygon').hide();
                $('#exitDraw').show();
            });

            $('#drawPolygon').unbind("click").bind("click", function () {
                that.ocxObject.setDrawLine(true);
                that.ocxObject.setDrawRectange(false);
                that.ocxObject.startDrawLine();
                $('#drawRectangle').hide();
                $('#drawPolygon').hide();
                $('#exitDraw').show();
            });
            $('#exitDraw').unbind("click").bind("click", function () {
                that.ocxObject.setDrawLine(false);
                that.ocxObject.setDrawRectange(false);
                $('#drawRectangle').show();
                $('#drawPolygon').show();
                $('#exitDraw').hide();

                $("#regionheight").val(that.ocxObject.ocxObject.Draw_RegionH);
                $("#regionwidth").val(that.ocxObject.ocxObject.Draw_RegionW);
                $("#regionx").val(that.ocxObject.ocxObject.Draw_RegionX);
                $("#regionx1").val(that.ocxObject.ocxObject.Draw_PX1);
                $("#regionx2").val(that.ocxObject.ocxObject.Draw_PX2);
                $("#regionx3").val(that.ocxObject.ocxObject.Draw_PX3);
                $("#regionx4").val(that.ocxObject.ocxObject.Draw_PX4);
                $("#regionx5").val(that.ocxObject.ocxObject.Draw_PX5);
                $("#regiony").val(that.ocxObject.ocxObject.Draw_RegionY);
                $("#regiony1").val(that.ocxObject.ocxObject.Draw_PY1);
                $("#regiony2").val(that.ocxObject.ocxObject.Draw_PY2);
                $("#regiony3").val(that.ocxObject.ocxObject.Draw_PY3);
                $("#regiony4").val(that.ocxObject.ocxObject.Draw_PY4);
                $("#regiony5").val(that.ocxObject.ocxObject.Draw_PY5);
            });
        },
        onchangeLightMode:function(){
            $("#lightMode").change(function(){
                var value = $(this).val();
                if(value == 2){
                    $('#lightTime').hide();
                    $('#lightDelay').show();
                }else if(value == 3){
                    $('#lightTime').show();
                    $('#lightDelay').hide();
                }else{
                    $('#lightDelay').hide();
                    $('#lightTime').hide();
                }
            });
        },
        onChangeLightTime:function(){
            $("#dayStartTime").change(function(){
                var times = $(this).val().split(':');
                $('#onTime_h').val(parseInt(times[0]));
                $('#onTime_m').val(parseInt(times[1]));
                $('#onTime_s').val(0);
            });
            $("#dayEndTime").change(function(){
                var times = $(this).val().split(':');
                $('#offTime_h').val(parseInt(times[0]));
                $('#offTime_m').val(parseInt(times[1]));
                $('#offTime_s').val(0);
            });
        },
        bindSubmitBasicForm:function () {
            $('#saveDevBasic').unbind("click").bind("click", function () {
                if (!$("#basicForm").valid()) {
                    return;
                }
                var params = $("#basicForm").serializeObject();

                ajaxReuqest(starnetContextPath + "/device/manage/update/basic",'post',params,function(res){
                    if(res.result == 0){
                        deviceManage.autoTable.ajax.reload(null,true);
                        successfulPrompt("操作成功","");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                });
            });

        },
        sysFormValidate:function(){
            var icon = "<i class='fa fa-times-circle'></i> ";
            $("#sysForm").validate({
                rules: {
                    'center.addr':{
                        required: true,
                        isIPV4:true
                    }
                },
                messages: {
                    'center.addr': {
                        required: icon + "中心服务器地址不能为空"
                    }
                }
            });
        },
        bindSaveDeviceSystem:function () {
            $('#saveDeviceSystem').unbind("click").bind("click", function () {
                if (!$("#sysForm").valid()) {
                    return;
                }
                var params = $("#sysForm").serializeObject();

                ajaxReuqest(starnetContextPath + "/device/manage/update/system",'post',params,function(res){
                    if(res.result == 0){
                        deviceManage.autoTable.ajax.reload(null,true);
                        successfulPrompt("操作成功","");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                });
            });
        },
        onSubIpcIdChange:function(){
            $("#sysSubIpcId").change(function(){
                var params = {
                    id:$('#sysDevId').val(),
                    subId:$('#sysSubIpcId').val()
                }
                $.post(starnetContextPath + "/device/manage/check/subIpc",params,function (res) {
                    if(res.result == 0){

                    }else{
                        errorPrompt("错误",res.msg);
                        $('#sysSubIpcId').val("");
                    }
                },'json');
            });
        },
        onSysRelateIpcIdChange:function(){

            $("#sysRelateIpcId").change(function(){
                var params = {
                    id:$('#sysDevId').val(),
                    relateId:$('#sysRelateIpcId').val()
                }
                $.post(starnetContextPath + "/device/manage/check/relateIpc",params,function (res) {
                    if(res.result == 0){

                    }else{
                        errorPrompt("错误",res.msg);
                        $('#sysRelateIpcId').val("");
                    }
                },'json');
            });
        },
        bindSaveLightAndOsd:function () {

            $('#saveLightAndOsd').unbind("click").bind("click", function () {
                if (!$("#lightForm").valid()) {
                    return;
                }
                var params = $("#lightForm").serializeObject();
                if(params["mode"] == 2){
                    if(params["delay"] == ""){
                        errorPrompt("","请输入关灯延时");
                        return;
                    }
                }

                ajaxReuqest(starnetContextPath + "/device/manage/update/light",'post',params,function(res){
                    if(res.result == 0){
                        deviceManage.autoTable.ajax.reload(null,true);
                        successfulPrompt("操作成功","");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                });
            });
        },
        lightFormValidate:function(){
            var icon = "<i class='fa fa-times-circle'></i> ";
            $("#lightForm").validate({
                rules: {
                    'StrobeTime':{
                        required: true,
                        number:true,
                        min:50,
                        max:1000
                    },
                    'delay':{
                        number:true,
                        min:0,
                        max:60
                    }
                },
                messages: {
                    'StrobeTime': {
                        required: icon + "曝闪灯持续时间不能为空"
                    }
                }
            });
        },
        bindSaveSceneInfo:function () {

            $('#saveSceneInfo').unbind("click").bind("click", function () {

                var params = $("#secneFrom").serializeObject();

                ajaxReuqest(starnetContextPath + "/device/manage/update/scene",'post',params,function(res){
                    if(res.result == 0){
                        successfulPrompt("操作成功","");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                });
            });
        },
        bindFormatSdCard:function(){
            $('#formatSdCard').unbind("click").bind("click", function () {
                var params = {
                    devId:$('#basicDevId').val()
                }
                $.post(starnetContextPath + "/device/manage/sdcard/format",params,function (res) {
                    if(res.result == 0){
                        successfulPrompt("操作成功","");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                },'json');
            });

        },
        bindRebootDev:function(){
            $('#rebootDev').unbind("click").bind("click", function () {
                var params = {
                    devId:$('#basicDevId').val()
                }
                $.post(starnetContextPath + "/device/manage/reboot",params,function (res) {
                    if(res.result == 0){
                        successfulPrompt("操作成功","设备正在重启...");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                },'json');
            });

        },




        initLedTable:function(){
            var obj = $('#ledTable').DataTable( {
                sort:false,
                processing:true,
                serverSide:true,
                searching:false,
                paging:false,
                autoWidth:true,
                retrieve:true,
                ajax: {
                    url: starnetContextPath + "/device/manage/led/list?devId=" + deviceManage.editData.deviceId,
                    type: 'POST'
                },
                columns:[
                    {"data":"scene"},
                    {"data":"topRowContent"},
                    {"data":"movingDirection"},
                    {"data":"movementSpeed"},
                    {"data":"voiceBroadcast"},
                    {"data":"voiceBroadcastVolume"},
                    {"data":"id"},
                    {"data":"sceneNo"},
                    {"data":"middleRowContent"},
                    {"data":"buttomRowContent"},
                    {"data":"devId"}

                ],
                "columnDefs": [
                    {
                        "render": function(data, type, row, meta) {
                            return '<div style="width: 180px;">' +  data + '</div>'
                                + '<div>'+'上行'+'</div>'
                                + '<div>'+'中行'+'</div>'
                                + '<div>'+'下行'+'</div>';
                        },
                        "targets": 0
                    },
                    {
                        "render": function(data, type, row, meta) {
                            return '<div>——</div>'
                                + '<div><input type="text" name="topRowContent' + row.sceneNo +'" value="'+data+'" ></div>'
                                + '<div><input type="text" name="middleRowContent' + row.sceneNo +'" value="'+row.middleRowContent+'" ></div>'
                                + '<div><input type="text" name="buttomRowContent' + row.sceneNo +'" value="'+row.buttomRowContent+'" ></div>';
                        },
                        "targets": 1
                    },
                    /*{
                        "render": function(data, type, row, meta) {
                            var direct1 = data.substring(0,1);
                            var direct2 = data.substring(1,2);
                            var direct3 = data.substring(2,3);

                            return '<div>——</div>'
                                + '<div><select name="topMovingDirection' + row.sceneNo +'">' +
                                deviceManage.getDirectionItems(direct1) + '</select></div>'
                                + '<div><select name="middleMovingDirection' + row.sceneNo +'">' +
                                deviceManage.getDirectionItems(direct2) +'</select></div>'
                                + '<div><select name="bottomMovingDirection' + row.sceneNo +'">' +
                                deviceManage.getDirectionItems(direct3) +'</select></div>';
                        },
                        "targets": 2
                    },*/
                    /*{
                        "render": function(data, type, row, meta) {
                            var speed1 = data.substring(0,1);
                            var speed2 = data.substring(1,2);
                            var speed3 = data.substring(2,3);

                            return '<div>——</div>'
                                + '<div><select name="topMovementSpeed' + row.sceneNo +'">' +
                                deviceManage.getSpeedItems(speed1) + '</select></div>'
                                + '<div><select name="middleMovementSpeed' + row.sceneNo +'">' +
                                deviceManage.getSpeedItems(speed2) +'</select></div>'
                                + '<div><select name="bottomMovementSpeed' + row.sceneNo +'">' +
                                deviceManage.getSpeedItems(speed3) +'</select></div>';
                        },
                        "targets": 3
                    },*/
                    {
                        "render": function(data, type, row, meta) {
                            return '<input name="voiceBroadcast' + row.sceneNo +'" value="'+data+'" type="text">';
                        },
                        "targets": 4
                    },
                    {
                        "render": function(data, type, row, meta) {
                            var i = 0;
                            var item = "";
                            while(i < 8){
                                if(data == i){
                                    item += '<option value="' + i + '" selected>' + i + '</option>';
                                }else{
                                    item += '<option value="' + i + '">' + i + '</option>';
                                }
                                i++;
                            }
                            return '<select name="voiceBroadcastVolume' + row.sceneNo +'">' + item +
                                '</select>';
                        },
                        "targets": 5
                    },
                    {
                        "render": function(data, type, row, meta) {
                            return '<input name="id' + row.sceneNo +'" value="'+data+'" type="hidden">' +
                                '<a class="btn btn-info btn-sm" onclick="deviceManage.ledDisplayTest('+meta.row+')">测试</a>';
                        },
                        "targets": 6
                    },
                    {
                        "visible": false,
                        "targets": [ 2,3,7,8,9,10 ]
                    }
                ],
                language:dataTableLanguage

            });
            obj.ajax.url(starnetContextPath + "/device/manage/led/list?devId=" + deviceManage.editData.deviceId).load();
            return obj;
        },
        initLedTableFour:function () {
            var objFour = $('#ledTableFour').DataTable( {
                sort:false,
                processing:true,
                serverSide:false,
                searching:false,
                paging:false,
                autoWidth:true,
                ajax: {
                    url: starnetContextPath + "/device/manage/led/list?devId=" + $('#basicDevId').val(),
                    type: 'POST'
                },
                columns:[
                    {"data":"scene"},
                    {"data":"topRowContent"},
                    {"data":"movingDirection"},
                    {"data":"movementSpeed"},
                    {"data":"voiceBroadcast"},
                    {"data":"voiceBroadcastVolume"},
                    {"data":"id"},
                    {"data":"sceneNo"},
                    {"data":"middleRowContent"},
                    {"data":"buttomRowContent"},
                    {"data":"devId"},
                    {"data":"fourthRowContent"}

                ],
                "columnDefs": [
                    {
                        "render": function(data, type, row, meta) {
                            return '<div style="width: 180px;">' +  data + '</div>'
                                + '<div>'+'第一行'+'</div>'
                                + '<div>'+'第二行'+'</div>'
                                + '<div>'+'第三行'+'</div>'
                                + '<div>'+'第四行'+'</div>';
                        },
                        "targets": 0
                    },
                    {
                        "render": function(data, type, row, meta) {
                            return '<div>——</div>'
                                + '<div><input type="text" name="topRowContentFour' + row.sceneNo +'" value="'+data+'" ></div>'
                                + '<div><input type="text" name="middleRowContentFour' + row.sceneNo +'" value="'+row.middleRowContent+'" ></div>'
                                + '<div><input type="text" name="buttomRowContentFour' + row.sceneNo +'" value="'+row.buttomRowContent+'" ></div>'
                                + '<div><input type="text" name="fourthRowContentFour' + row.sceneNo +'" value="'+row.fourthRowContent+'" ></div>';
                        },
                        "targets": 1
                    },
                    /*{
                        "render": function(data, type, row, meta) {
                            var direct1 = data.substring(0,1);
                            var direct2 = data.substring(1,2);
                            var direct3 = data.substring(2,3);
                            var direct4 = direct3;
                            return '<div>——</div>'
                                + '<div><select name="topMovingDirectionFour' + row.sceneNo +'">' +
                                deviceManage.getDirectionItems(direct1) + '</select></div>'
                                + '<div><select name="middleMovingDirectionFour' + row.sceneNo +'">' +
                                deviceManage.getDirectionItems(direct2) +'</select></div>'
                                + '<div><select name="bottomMovingDirectionFour' + row.sceneNo +'">' +
                                deviceManage.getDirectionItems(direct3) +'</select></div>'
                                + '<div><select name="fourthMovingDirectionFour' + row.sceneNo +'">' +
                                deviceManage.getDirectionItems(direct4) +'</select></div>';
                        },
                        "targets": 2
                    },
                    {
                        "render": function(data, type, row, meta) {
                            var speed1 = data.substring(0,1);
                            var speed2 = data.substring(1,2);
                            var speed3 = data.substring(2,3);
                            var speed4 = speed3;

                            return '<div>——</div>'
                                + '<div><select name="topMovementSpeedFour' + row.sceneNo +'">' +
                                deviceManage.getSpeedItems(speed1) + '</select></div>'
                                + '<div><select name="middleMovementSpeedFour' + row.sceneNo +'">' +
                                deviceManage.getSpeedItems(speed2) +'</select></div>'
                                + '<div><select name="bottomMovementSpeedFour' + row.sceneNo +'">' +
                                deviceManage.getSpeedItems(speed3) +'</select></div>'
                                + '<div><select name="fourthMovementSpeedFour' + row.sceneNo +'">' +
                                deviceManage.getSpeedItems(speed4) +'</select></div>';
                        },
                        "targets": 3
                    },*/
                    {
                        "render": function(data, type, row, meta) {
                            return '<input name="voiceBroadcastFour' + row.sceneNo +'" value="'+data+'" type="text">';
                        },
                        "targets": 4
                    },
                    /*{
                        "render": function(data, type, row, meta) {
                            var i = 0;
                            var item = "";
                            while(i < 8){
                                if(data == i){
                                    item += '<option value="' + i + '" selected>' + i + '</option>';
                                }else{
                                    item += '<option value="' + i + '">' + i + '</option>';
                                }
                                i++;
                            }
                            return '<select name="voiceBroadcastVolumeFour' + row.sceneNo +'">' + item +
                                '</select>';
                        },
                        "targets": 5
                    },*/
                    {
                        "render": function(data, type, row, meta) {
                            return '<input name="idFour' + row.sceneNo +'" value="'+data+'" type="hidden">' +
                                '<a class="btn btn-info btn-sm" onclick="deviceManage.ledDisplayFourTest('+meta.row+')">测试</a>';
                        },
                        "targets": 6
                    },
                    {
                        "visible": false,
                        "targets": [2,3,5,7,8,9,10,11]
                    }
                ],
                language:dataTableLanguage

            });
            return objFour;
        },
        getDirectionItems:function(direction){
            if(direction == "0"){
                var items = '<option value="0" selected>向左</option>' +
                    '<option value="1">向右</option>';
                return items;
            }else{
                var items = '<option value="0" selected>向左</option>' +
                    '<option value="1" selected>向右</option>';
                return items;
            }

        },
        getSpeedItems:function(speed){
            if(speed == "0"){
                var items = '<option value="0" selected>高</option>' +
                    '<option value="1">中</option>' +
                    '<option value="2">低</option>';
                return items;
            }else if(speed == "1"){
                var items = '<option value="0">高</option>' +
                    '<option value="1" selected>中</option>' +
                    '<option value="2">低</option>';
                return items;
            }else{
                var items = '<option value="0">高</option>' +
                    '<option value="1">中</option>' +
                    '<option value="2" selected>低</option>';
                return items;
            }

        },
        bindSaveLedBtn:function(){
            $('#saveLedBtn').unbind("click").bind("click", function () {
                var params = $("#ledForm").serializeObject();
                var list = new Array();
                var i = 0;
                if (ledPlayType == "3"){
                    while(i < 7){
                        var item = {
                            id:params["id" + i],
                            sceneNo:i,
                            topRowContent:params["topRowContent" + i],
                            middleRowContent:params["middleRowContent" + i],
                            buttomRowContent:params["buttomRowContent" + i],
                            fourthRowContent:params["fourthRowContent" + i],
                            movingDirection:params["topMovingDirection" + i] + params["middleMovingDirection" + i] + params["bottomMovingDirection" + i],
                            movementSpeed:params["topMovementSpeed" + i] + params["middleMovementSpeed" + i] + params["bottomMovementSpeed" + i],
                            voiceBroadcast:params["voiceBroadcast" + i],
                            voiceBroadcastVolume:params["voiceBroadcastVolume" + i]
                        };
                        list.push(item);
                        i++;
                    }
                }else {
                    while(i < 7){
                        var item = {
                            id:params["idFour" + i],
                            sceneNo:i,
                            topRowContent:params["topRowContentFour" + i],
                            middleRowContent:params["middleRowContentFour" + i],
                            buttomRowContent:params["buttomRowContentFour" + i],
                            fourthRowContent:params["fourthRowContentFour" + i],
                            movingDirection:params["topMovingDirectionFour" + i] + params["middleMovingDirectionFour" + i] + params["bottomMovingDirectionFour" + i],
                            movementSpeed:params["topMovementSpeedFour" + i] + params["middleMovementSpeedFour" + i] + params["bottomMovementSpeedFour" + i],
                            voiceBroadcast:params["voiceBroadcastFour" + i],
                            voiceBroadcastVolume:params["voiceBroadcastVolumeFour" + i]
                        };
                        list.push(item);
                        i++;
                    }
                }


                var channelSet = {
                    deviceChannelNo:params.deviceChannelNo,
                    voiceChannel:params.voiceChannel,
                    devId:params.deviceId,
                    ledPlayType : ledPlayType
                }
                var sendParams = {
                    list:list,
                    channelSet:channelSet
                };
                ajaxReuqest(starnetContextPath + "/device/manage/led/update",'post',sendParams,function(res){
                    if(res.result == 0){
                        successfulPrompt("操作成功","");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                });

            });

        },
        ledDisplayTest:function (rowIndex) {
            var data = this.ledTable.row(rowIndex).data();
            var params = $("#ledForm").serializeObject();

            var sendParams = {
                id:data["id"],
                sceneNo:data["sceneNo"],
                devId:params["deviceId"],
                topRowContent:params["topRowContent" + data["sceneNo"]],
                middleRowContent:params["middleRowContent" + data["sceneNo"]],
                buttomRowContent:params["buttomRowContent" + data["sceneNo"]],
                movingDirection:params["topMovingDirection" + data["sceneNo"]] + params["middleMovingDirection" + data["sceneNo"]] + params["bottomMovingDirection" + data["sceneNo"]],
                movementSpeed:params["topMovementSpeed" + data["sceneNo"]] + params["middleMovementSpeed" + data["sceneNo"]] + params["bottomMovementSpeed" + data["sceneNo"]],
                voiceBroadcast:params["voiceBroadcast" + data["sceneNo"]],
                voiceBroadcastVolume:params["voiceBroadcastVolume" + data["sceneNo"]],
                displayMode:data["displayMode"],
                scene:data["scene"],
                ledType : "3"
            };
            console.log(params);
            console.log(sendParams);
            console.log(data["sceneNo"]);
            ajaxReuqest(starnetContextPath + "/device/manage/test/led",'post',sendParams,function(res){
                if(res.result == 0){
                    successfulPrompt("操作成功","");
                }else{
                    errorPrompt("操作失败",res.msg);
                }
            });
            return false;
        },
        ledDisplayFourTest:function (rowIndex) {
            var data = this.ledTableFour.row(rowIndex).data();
            var params = $("#ledForm").serializeObject();

            var sendParams = {
                id:data["id"],
                sceneNo:data["sceneNo"],
                devId:params["deviceId"],
                topRowContent:params["topRowContentFour" + data["sceneNo"]],
                middleRowContent:params["middleRowContentFour" + data["sceneNo"]],
                buttomRowContent:params["buttomRowContentFour" + data["sceneNo"]],
                movingDirection:params["topMovingDirectionFour" + data["sceneNo"]] + params["middleMovingDirectionFour" + data["sceneNo"]] + params["bottomMovingDirectionFour" + data["sceneNo"]],
                movementSpeed:params["topMovementSpeedFour" + data["sceneNo"]] + params["middleMovementSpeedFour" + data["sceneNo"]] + params["bottomMovementSpeedFour" + data["sceneNo"]],
                voiceBroadcast:params["voiceBroadcastFour" + data["sceneNo"]],
                voiceBroadcastVolume:params["voiceBroadcastVolumeFour" + data["sceneNo"]],
                displayMode:data["displayMode"],
                scene:data["scene"],
                ledType : "11"
            };
            console.log(params);
            console.log(sendParams);
            console.log(data["sceneNo"]);
            ajaxReuqest(starnetContextPath + "/device/manage/test/led",'post',sendParams,function(res){
                if(res.result == 0){
                    successfulPrompt("操作成功","");
                }else{
                    errorPrompt("操作失败",res.msg);
                }
            });
            return false;
        },
        audioTest:function(rowIndex){

        },
        initAudioTable:function(){
            var obj = $('#audioTable').DataTable( {
                sort:false,
                processing:true,
                serverSide:false,
                searching:false,
                paging:false,
                autoWidth:true,
                ajax: {
                    url: starnetContextPath + "/device/manage/audio/list?devId=" + $('#basicDevId').val(),
                    type: 'POST'
                },
                columns:[
                    {"data":"scene"},
                    {"data":"voiceBroadcast"},
                    {"data":"voiceBroadcastVolume"},
                    {"data":"id"},
                    {"data":"sceneNo"}

                ],
                "columnDefs": [
                    {
                        "render": function(data, type, row, meta) {
                            return '<div style="width: 180px;">' +  data + '</div>';
                        },
                        "targets": 0
                    },
                    {
                        "render": function(data, type, row, meta) {
                            return '<div><input type="text" name="voiceBroadcast' + row.sceneNo +'" value="'+data+'" ></div>';
                        },
                        "targets": 1
                    },
                    {
                        "render": function(data, type, row, meta) {
                            var i = 0;
                            var item = "";
                            while(i < 17){
                                if(data == i){
                                    item += '<option value="' + i + '" selected>' + i + '</option>';
                                }else{
                                    item += '<option value="' + i + '">' + i + '</option>';
                                }
                                i++;
                            }
                            return '<select name="voiceBroadcastVolume' + row.sceneNo +'">' + item +
                                '</select>';
                        },
                        "targets": 2
                    },
                    {
                        "render": function(data, type, row, meta) {
                            return '<input name="id' + row.sceneNo +'" value="'+data+'" type="hidden">' +
                                '<a class="btn btn-info btn-sm" onclick="deviceManage.audioTest('+meta.row+')">测试</a>';
                        },
                        "targets": 3
                    },
                    {
                        "visible": false,
                        "targets": [ 4 ]
                    }
                ],
                language:dataTableLanguage

            });
            return obj;
        },
        bindSaveAudioBtn:function(){
            $('#saveAudioBtn').unbind("click").bind("click", function () {
                var params = $("#audioForm").serializeObject();
                var list = new Array();
                var i = 1;
                while(i < 7){
                    if(i != 5){
                        var item = {
                            id:params["id" + i],
                            sceneNo:i,
                            voiceBroadcast:params["voiceBroadcast" + i],
                            voiceBroadcastVolume:params["voiceBroadcastVolume" + i]
                        };
                        list.push(item);
                    }
                    i++;
                }
                var channelSet = {
                    deviceChannelNo:params.deviceChannelNo,
                    voiceChannel:params.voiceChannel,
                    devId:params.deviceId
                }
                var sendParams = {
                    list:list,
                    channelSet:channelSet
                };
                ajaxReuqest(starnetContextPath + "/device/manage/audio/update",'post',sendParams,function(res){
                    if(res.result == 0){
                        successfulPrompt("操作成功","");
                    }else{
                        errorPrompt("操作失败",res.msg);
                    }
                });

            });

        },
        bindTimeFormatBtn:function(){

            $('#timeFormatBtn').unbind("click").bind("click", function () {

                $.post(starnetContextPath + "/device/manage/update/time",function (res) {
                    if(res.result == 0){
                        successfulPrompt("校时成功","");
                    }else{
                        errorPrompt("校时失败",res.msg);
                    }
                },'json');
            });
        },
        ondeviceLedTypeChange:function () {
            $("#deviceLedType").change(function () {
                if ($("#deviceLedType").val() == "threeRow"){
                    //alert("显示三行显示屏");
                    $("#ledPlayThree")[0].style.display = "block";
                    $("#ledPlayFour")[0].style.display = "none";
                    ledPlayType = "3";

                    deviceManage.ledTable = deviceManage.initLedTable();
                    if(deviceManage.ledTable == null){
                        deviceManage.ledTable = deviceManage.initLedTable();
                    }else{
                        deviceManage.ledTable.ajax.url = starnetContextPath + "/device/manage/led/list?devId=" + $('#basicDevId').val();
                        deviceManage.ledTable.ajax.reload();
                    }
                }else if ($("#deviceLedType").val() == "fourRow"){
                    //alert("显示四行显示屏");
                    $("#ledPlayThree")[0].style.display = "none";
                    $("#ledPlayFour")[0].style.display = "block";
                    ledPlayType = "11";
                    if(deviceManage.ledTableFour == null){
                        deviceManage.ledTableFour = deviceManage.initLedTableFour();
                    }else{
                        deviceManage.ledTableFour.ajax.url = starnetContextPath + "/device/manage/led/list?devId=" + $('#basicDevId').val();
                        deviceManage.ledTableFour.ajax.reload();
                    }
                }else {
                    $("#ledPlayThree")[0].style.display = "none";
                    $("#ledPlayFour")[0].style.display = "none";
                    ledPlayType = "12";
                }
            })
        }


    };
    deviceManage.render();
});
window.onbeforeunload = onclose;
function onclose()
{
    deviceManage.ocxObject.stopView();
}

function updateResult(){
    var body = $(window.frames['updateSystemFrame'].document.body);
    var res = body.context.innerHTML + "";
    console.log(res)
    var obj = JSON.parse(res);

    if(obj.result == 0){
        successfulPrompt("操作成功","设备升级中......");
    }else{
        errorPrompt("升级失败，请检查升级文件");
    }
}