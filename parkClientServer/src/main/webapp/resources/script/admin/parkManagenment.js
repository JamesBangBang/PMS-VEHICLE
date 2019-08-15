var editData;
var autoTable;
$(function(){
    iniDataTable();
    //信息选择
    InformationSelect();
});
//表格数据
function iniDataTable(){
    //清空表格
    if(autoTable){
        autoTable.destroy();
    }
    autoTable = $('#autoTable').DataTable( {
        sort:false,
        processing:true,
        serverSide:true,
        searching:false,
        autoWidth:true,
        ajax: {
            url: starnetContextPath + "/carpark/getCarParksInfoList",
            type: 'POST'
        },
        columns:[
            {"data":"carpark_name"},
            {"data":"total_car_space"},
            {"data":"is_close"},
            {"data":"parentCarparkName"},
            {"data":"carpark_id"},
            {"data":"department_id"}
        ],
        "columnDefs": [
            {
                "render": function(data, type, row, meta) {
                    return '<button class="btn btn-info btn-sm btn-info"  onclick="editFunction('+meta.row+')">编辑</button>' +
                        '<button class="btn  btn-sm btn-danger" onclick="deleteFunction('+meta.row+')">删除</button> '+
                        ' ';
                },
                "targets": 4
            },
            {
                "render": function(data, type, row, meta) {
                    if(data == 1){
                        return '是';
                    }else{
                        return '否';
                    }
                },
                "targets": 2
            },
            {
                "targets": [ 5 ],
                "visible": false
            }

        ],

        language:dataTableLanguage

    });
}


//加载车场详情信息
 function detailsFunction(rowIndex){

    //去除提交按钮
     $("#addForm").hide();
    var data = autoTable.row(rowIndex).data();
    var params = {
        carParkId:data.carpark_id
    };
    $.post(starnetContextPath + "/carpark/getCarParksInfo",params,function(res){
        if(res.result == 0){
            $("#parkForm").populateForm(res.data);
            $("#descModal").modal("show");
            if (($("#ownCarparkName").val() == "") || ($("#ownCarparkName").val() == null)){
                document.getElementById("ifIncludeCaculate").disabled = true;
            }else{
                document.getElementById("ifIncludeCaculate").disabled = false;
            }
            if ($("#isClose").val() == "yes"){
                document.getElementById("closeType").disabled = false;
                document.getElementById("criticalValue").disabled = false;
            }else {
                document.getElementById("closeType").disabled = true;
                document.getElementById("criticalValue").disabled = true;
            }
        }else{
            errorPrompt("车场信息获取失败",res.msg);
        }
    });
}

//修改车场信息
function editFunction(rowIndex){

     //显示按钮
    $("#addForm").show();

    var data = autoTable.row(rowIndex).data();
    editData=data
    // ("infoForm").reset();
    var params = {
        carParkId:data.carpark_id
    };

    $.post(starnetContextPath + "/carpark/getCarParksInfo",params,function(res){
        if(res.result == 0){
            $("#parkForm").populateForm(res.data);
            $("#descModal").modal("show");
            if (($("#ownCarparkName").val() == "") || ($("#ownCarparkName").val() == null)){
                document.getElementById("ifIncludeCaculate").disabled = true;
            }else{
                document.getElementById("ifIncludeCaculate").disabled = false;
            }
            if ($("#isClose").val() === "yes"){
                document.getElementById("closeType").disabled = false;
                document.getElementById("criticalValue").disabled = false;
            }else {
                document.getElementById("closeType").disabled = true;
                document.getElementById("criticalValue").disabled = true;

            }
            if(!res.data.lat){
                $("#editLat").val("");
            }
            if(!res.data.lon){
                $("#editLon").val("");
            }
            if(!res.data.criticalValue){
                $("#criticalValue").val("");
            }
        }else{
            errorPrompt("车场信息获取失败",res.msg);
        }
    });
}



//删除车场信息
function deleteFunction(rowIndex) {
    var data = autoTable.row(rowIndex).data();
    editData = data;
    $("#deleteModal").modal("show");
}

function deleteCarparkInfo() {
    var params = {
        carParkId:editData.carpark_id
    };
    $.post(starnetContextPath + "/carpark/deleteCarParksInfo",params,function(res){
        if(res.result == 0){
            $("#deleteModal").modal("hide");
            successfulPrompt("车场删除成功");
            autoTable.ajax.reload(null,false);
        }else{
            errorPrompt("车场删除失败",res.msg);
        }
    });
}

//创建车场初始化车场创建界面
function addFunction(){
    $("#addCarparkModal").modal("show");
    //加载所属车场
    var ownCarparkParam = {};
    $.post(starnetContextPath + "/carpark/getOwnCarparkInfo",ownCarparkParam,function (res) {
        if (res.result == 0){
            $("#addOwnCarparkName").empty();
            $("#addOwnCarparkName").append("<option value=''>无</option>");
            for (var i in res.data){
                var item = "<option value='" + res.data[i].carparkId + "'>" +
                    res.data[i].carparkName +
                    "</option>";
                $("#addOwnCarparkName").append(item);
            }
        }else{
            errorPrompt("初始化所属车场失败！");
        }
    })
    $("#addCarparkName").val("");
    $("#addLat").val("");
    $("#addLon").val("");
    $("#addCriticalValue").val("0");

    document.getElementById("addTotalCarSpace").value = 1000;
    document.getElementById("addAvailableCarSpace").value = 1000;
    document.getElementById("addPassTimeWhenBig").value = 10;

    document.getElementById("addIfIncludeCalculate").disabled = true;
    document.getElementById("addIsClose").value = "no";
    document.getElementById("addCloseType").disabled = true;
    document.getElementById("addCriticalValue").disabled = true;
    document.getElementById("addLedMemberCriticalValue").value = 30;

}

function isCloseCarpark() {
    if($("#isClose").val()==="yes"){
        document.getElementById("closeType").disabled = false;
        document.getElementById("criticalValue").disabled = false;
    }else {
        document.getElementById("closeType").disabled = true;
        document.getElementById("criticalValue").disabled = true;

    }
}

function addIsCloseCarpark() {
    if($("#addIsClose").val() == "yes"){
        document.getElementById("addCloseType").disabled = false;
        document.getElementById("addCriticalValue").disabled = false;
    }else {
        document.getElementById("addCloseType").disabled = true;
        document.getElementById("addCriticalValue").disabled = true;
    }
}

function getCarparkInfoByDepId(depId) {
    var carparkParam = {
        "departmentId" : depId
    };
    $.post(starnetContextPath + "/carpark/getUserDepartmentsParks",carparkParam,function (res) {
        if (res.result == 0){
            $("#addOwnCarparkName").empty();
            $("#addOwnCarparkName").append("<option value=''>无</option>");
            for (var i in res.data){
                var item = "<option value='" + res.data[i].caparkId + "'>" +
                    res.data[i].carparkName +
                    "</option>";
                $("#addOwnCarparkName").append(item);
            }
        }else{
            errorPrompt("初始化所属车场失败！");
        }
    })
}

//修改车场信息
function changeFunction(){

    if ($("#carparkName").val() == ""){
        alert("车场名称不能为空");
        return false;
    }
    if ($("#totalCarSpace").val() == ""){
        alert("总车位数不能为空");
        return false;
    }
    if ($("#availableCarSpace").val() == ""){
        alert("剩余车位数不能为空");
        return false;
    }
    var availableCarSpace = parseInt($("#availableCarSpace").val());
    var totalCarSpace = parseInt($("#totalCarSpace").val());
    if (availableCarSpace > totalCarSpace){
        alert("剩余车位数不能大于车位总数");
        return false;
    }
    if ($("#isClose").val() == "yes"){
        if ($("#criticalValue").val() == ""){
            alert("封闭车场临界值不能为空");
            return false;
        }
    }
    if ($("#passTimeWhenBig").val() == ""){
        alert("过路时间不能为空");
        return false;
    }
    if ($("#ledMemberCriticalValue").val() == ""){
        alert("LED开始提示剩余天数不能为空");
        return false;
    }

    if($("#editLon").val()>180)
    {
     alert("经度值过大");
        return false;
    }

    else if($("#editLat").val()>90)
    {
        alert("纬度值过大");
        return false;
    }

    //判断同一管理单位下车场名称不一致
    var isRepeatParams = {
        "controlMode" : "edit",
        "carparkId" : editData.carpark_id,
        "carparkName" : $("#carparkName").val()
    };

    $.ajax({
        url: starnetContextPath + "/carpark/isCarparkNameRepeat",
        type: "post",
        async: false,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(isRepeatParams),
        beforeSend: function (request) {
            request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        },
        success: function (res) {
            if(res == true){
                errorPrompt("车场名称已存在！");
                return;
            }},
        error:function(){
            errorPrompt("无法访问网络","")
        }
    });


    //保存修改的信息
    var params = {
        "controlMode" : "edit",
        "carparkId" : editData.carpark_id,
        "carparkName" : $("#carparkName").val(),
        "totalCarSpace" : $("#totalCarSpace").val(),
        "availableCarSpace" : $("#availableCarSpace").val(),
        "isAutoOpen" : $("#isAutoOpen").val(),
        "isOverdueAutoOpen":$("#isOverdueAutoOpen").val(),
        "ifIncludeCaculate" : $("#ifIncludeCaculate").val(),
        "isTestRunning":$("#isTestRunning").val(),
        "isClose" : $("#isClose").val(),
        "closeType" : $("#closeType").val(),
        "criticalValue" : $("#criticalValue").val(),
        "passTimeWhenBig" : $("#passTimeWhenBig").val(),
        "isOverdueAutoOpen" : $("#isOverdueAutoOpen").val(),
        "ledMemberCriticalValue" : $("#ledMemberCriticalValue").val(),
        "lon" : $("#editLon").val(),
        "lat" : $("#editLat").val()
    };
    ajaxReuqest(starnetContextPath + "/carpark/updateCarparkInfo","post",params,function (res) {
        if(res.result == 0){
            $("#descModal").modal("hide");
            iniDataTable();
            successfulPrompt("操作成功","");
        }else{
            errorPrompt("操作失败",res.msg);
        }
    });

}
function saveParkInfo() {
    if ($("#addCarparkName").val() == ""){
        alert("车场名称不能为空");
        return false;
    }
    if ($("#addtotalcarspace").val() == ""){
        alert("总车位数不能为空");
        return false;
    }
    if ($("#addAvailableCarSpace").val() == ""){
        alert("剩余车位数不能为空");
        return false;
    }
    var availableCarSpace = parseInt($("#addAvailableCarSpace").val());
    var totalCarSpace = parseInt($("#addTotalCarSpace").val());
    if (availableCarSpace > totalCarSpace){
        alert("剩余车位数不能大于车位总数");
        return false;
    }
    if ($("#addPassTimeWhenBig").val() == ""){
        alert("过路时间不能为空");
        return false;
    }
    if ($("#addLedMemberCriticalValue").val() == ""){
        alert("LED开始提示剩余天数不能为空");
        return false;
    }
    if($("#addLon").val()>180)
    {
        alert("经度值过大");
        return false;
    }

    if($("#addLat").val()>90)
    {
        alert("纬度值过大");
        return false;
    }

    //判断同一管理单位下车场名称不一致
    var isRepeatParams = {
        "controlMode" : "add",
        "carparkId" : "",
        "carparkName" : $("#addCarparkName").val()
    };

    $.ajax({
        url: starnetContextPath + "/carpark/isCarparkNameRepeat",
        type: "post",
        async: false,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(isRepeatParams),
        beforeSend: function (request) {
            request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        },
        success: function (res) {
            if(res == true){
                errorPrompt("同一单位下两个车场名称不能相同！");
                return;
            }else {
                //保存车场信息
                var addParams = {
                    "controlMode" : "add",
                    "carparkId" : "",
                    "carparkName" : $("#addCarparkName").val(),
                    "addTotalCarSpace" : $("#addTotalCarSpace").val(),
                    "addIsTestRunningg" : $("#addIsTestRunningg").val(),
                    "addPassTimeWhenBig" : $("#addPassTimeWhenBig").val(),
                    "addLat" : $("#addLat").val(),
                    "addAvailableCarSpace" : $("#addAvailableCarSpace").val(),
                    "addIsClose" : $("#addIsClose").val(),
                    "addIsOverdueAutoOpen" : $("#addIsOverdueAutoOpen").val(),
                    "addOwnCarparkName" : $("#addOwnCarparkName").val(),
                    "isAutoOpen" : $("#isAutoOpen").val(),
                    "addCloseType" : $("#addCloseType").val(),
                    "addLedMemberCriticalValue" : $("#addLedMemberCriticalValue").val(),
                    "addPostComputerName" : $("#addPostComputerName").val(),
                    "ifIncludeCalculate" : $("#ifIncludeCalculate").val(),
                    "addCriticalValue" : $("#addCriticalValue").val(),
                    "addLon" : $("#addLon").val(),
                    "addLat" : $("#addLat").val()
                };
                $.ajax({
                    url: starnetContextPath + "/carpark/saveCarparkInfo",
                    type: "post",
                    async: false,
                    dataType: 'json',
                    contentType: 'application/json',
                    data: JSON.stringify(addParams),
                    beforeSend: function (request) {
                        request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                    },
                    success: function (res) {
                        if(res.result == 0){
                            autoTable.ajax.reload(null,false);
                            successfulPrompt("添加成功","");
                            $("#addCarparkModal").modal("hide");
                        }else{
                            errorPrompt("添加失败",res.msg);
                        }},
                    error:function(){
                        errorPrompt("无法访问网络","")
                    }
                });
            }
            },
        error:function(){
            errorPrompt("无法访问网络","")
        }
    });
}

//信息选择
function InformationSelect() {
    //车场信息
    $("#parkInformation").bind("click",function() {
        //表格
        $("#parkTable").show();
        $("#carRoadTable").hide();
        //按钮显示
        $("#creatPark").show();
        //隐藏无关按钮
        $("#creatRoad").hide();
    });

    //车道信息
    $("#carRoadInformation").bind("click",function() {

        //表格
        $("#carRoadTable").show();
        $("#parkTable").hide();
        //按钮显示
        $("#creatPark").hide();
        //隐藏无关按钮
        $("#creatRoad").show();
    });

}

// 刷新表格数据
function refreshBtnClick() {
    autoTable.ajax.reload(null,true);
}



