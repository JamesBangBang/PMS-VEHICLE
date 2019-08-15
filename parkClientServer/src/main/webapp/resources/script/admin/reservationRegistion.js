var mainTable;

$(function(){
    iniDataTable();

    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#reservationForm").validate({
        rules: {
            carNo:{
                required: true
            },
            driverName: {
                required: true,
                minlength: 2,
                maxlength:20
            },
            driverTelephoneNumber: {
                required: false,
                isMobile:true
            },
            carparkId: {
                required: true,
                select:true
            },
            effectiveStartTime: {
                required: true
            },
            effectiveEndtTime: {
                required: true
            }
        },
        messages: {
            carNo:{
                required: icon + "请输入车牌号"
            },
            driverName: {
                required: icon + "请输入姓名",
                minlength: icon + "姓名必须两个字符以上",
                maxlength: icon + "姓名不能超过20个字符"
            },
            carparkId: {
                required: icon + "请选择一个车场"
            },
            driverTelephoneNumber: {
                required: icon + "请输入电话号码"
            },
            effectiveStartTime: {
                required: icon + "时间格式不正确/不可为空"
            },
            effectiveEndtTime: {
                required: icon + "时间格式不正确/不可为空"
            }
        }
    });
    var dateStr = new Date().Format("yyyy-MM-dd");
    var dateStart = new Date().Format("yyyy-MM-dd 00:00:00");
    var dateEnd = new Date().Format("yyyy-MM-dd 23:59:59");

    //时间表格
    var effectiveStartTime = {
        elem: '#effectiveStartTime',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59', //最大日期
        istime: true,
        istoday: false,
        start: dateStr + ' 00:00:00',
        value:dateStart,
        choose: function (datas) {
            effectiveEndTime.min = datas; //开始日选好后，重置结束日的最小日期
            effectiveEndTime.start = datas //将结束日的初始值设定为开始日
        }
    };
    var effectiveEndTime = {
        elem: '#effectiveEndTime',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        value:dateEnd,
        choose: function (datas) {
            effectiveStartTime.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    var startMin = {
        elem: '#startMin',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        choose: function (datas) {
            endMin.min = datas; //开始日选好后，重置结束日的最小日期
            endMin.start = datas //将结束日的初始值设定为开始日
        }
    };
    var startMax = {
        elem: '#startMax',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        choose: function (datas) {
            endMax.min = datas; //开始日选好后，重置结束日的最小日期
            endMax.start = datas //将结束日的初始值设定为开始日
        }
    };
    var endMin = {
        elem: '#endMin',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        choose: function (datas) {
            startMin.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    var endMax = {
        elem: '#endMax',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        choose: function (datas) {
            startMax.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };

    $("#effectiveStartTime").val(dateStr + " 00:00:00");
    $("#startMin").val(dateStr + " 00:00:00");
    $("#endMin").val(dateStr + " 00:00:00");
    $("#effectiveEndTime").val(dateStr + " 23:59:59");
    $("#startMax").val(dateStr + " 23:59:59");
    $("#endMax").val("");


    laydate(effectiveStartTime);
    laydate(effectiveEndTime);
    laydate(startMin);
    laydate(startMax);
    laydate(endMin);
    laydate(endMax);

    $("#addReservation").unbind("click").bind("click", function () {

        initSelect();
        // var params ={'depId':'159e0185ae5911e78ed5a4bf0116715b'};
        if(typeof(getDepId(getCurrentCarparkId()))==="undefined"){

            return

        }
        initSelect();
        updateSelect();
        addRoleFormSet();

    });

    initSearchCondition();
});



//表格数据
function iniDataTable(){

    mainTable = $('#reservationRegistion').DataTable( {
        ordering: true,
        processing:true,
        searching:false,
        autoWidth:true,
        serverSide:true,
        paging:true,
        ajax: {
            url: starnetContextPath + "/reservation/getReservattionList",
            type: 'POST',
            data:{
                "reservationTypeName":"",
                "carNo":'',
                "driverName":"",
                "driverTelephoneNumber":"",
                "driverInfo":"",
                "beginTimeMin":"",
                "beginTimeMax":"",
                "endTimeMin":"",
                "endTimeMax":"",
                "driverFileId":"",
                "carparkId":"",
                "depId":getDepId(getCurrentCarparkId())
            }
        },
        columns:[
            {"data":"carNo"},
            {"data":"reservationTypeName"},
            {"data":"driverName"},
            {"data":"driverTelephoneNumber"},
            {"data":"driverInfo"},
            {"data":"carparkName"},
            {"data":"effectiveStartTime"},
            {"data":"effectiveEndTime"},
            {"data":"carparkId"},
            {"data":"driverFileId"},
            {"data":"memberWalletId"},
            {"data":"depId"}
        ],
        "columnDefs": [
          {
                "render": function(data, type, row, meta) {
                    return '<button class="btn btn-info btn-sm" style="background-color: #FFAB99;border-color:#FFAB99 "  onclick="editFunction(' + meta.row + ')">修改</button>' +
                        ' <button class="btn btn-info btn-sm btn-danger"  onclick="deleteFunction(' + meta.row + ')" >删除</button>';
                      // /!*  ' <button class="btn btn-danger btn-sm" onclick="deleteFunction('+meta.row+')">删除</button>';*!/
                },
                "targets": 8
            },
            {
                "targets":[1,9,10,11],
                "visible":false
            },
            {
                /*给每个字段设置默认值，当返回data中数据为null不会报错*/
                "defaultContent": "",
                "targets": "_all"
            }

        ],
        language:dataTableLanguage
    } );
}

function initSelect() {
    $("#driverFileId").val("");
    $("#memberWalletId").val("");
    $("#controlMode").val("");
    $("#carNo").val("");
    $("#driverName").val("");
    $("#driverTelephoneNumber").val("");
    $("#driverInfo").val("");
    $('[name =reservationTypeName]').val("来宾");
    $("[name = carparkId]").val(parent.parkID);
    $("[name = carparkId]").trigger('chosen:updated');

    var dateStart = new Date().Format("yyyy-MM-dd 00:00:00");
    var dateEnd = new Date().Format("yyyy-MM-dd 23:59:59");

    //时间表格
    var effectiveStartTime = {
        elem: '#effectiveStartTime',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59', //最大日期
        istime: true,
        istoday: false,
        //start: dateStr + ' 00:00:00',
        value:dateStart,
        choose: function (datas) {
            effectiveEndTime.min = datas; //开始日选好后，重置结束日的最小日期
            effectiveEndTime.start = datas //将结束日的初始值设定为开始日
        }
    };
    var effectiveEndTime = {
        elem: '#effectiveEndTime',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        value:dateEnd,
        choose: function (datas) {
            effectiveStartTime.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
}

function updateSelect() {
    $('[name =carparkId]').chosen({
        width:"100%"
    });
    $('[name =carparkIdSearch]').chosen({
        width:"100%"
    });
    /*使新赋的值能够刷新成功*/
    $("select").on('chosen:ready', function(e, params) {
        $("select").val("true");//设置值  
    });
    $("select").trigger('chosen:updated');
}

function selectChecked(nodeId, nodeValue) {
    $("'#" + nodeId + "'").find("option[value = '"+ nodeValue +"']").attr("selected","selected");
}

function editFunction(rowIndex){
    var rowData = mainTable.row(rowIndex).data();

    initSelect();
    $("#reservationForm").populateForm(rowData);
    $("#carparkId").find("option[value = '"+ rowData.carparkId +"']").attr("selected","selected");
    updateSelect();
    edtRoleFormSet();

}

function getDepId(id) {
    var params = {};
    var depId="";
    if (id == "" || id == null){
        params.carparkId = getCurrentCarparkId();
    }else{
        params.carparkId = id;
    }
    $.ajax({
        url: starnetContextPath + "/report/getDeptId",
        type: "POST",
        dataType: 'json',
        async: false,
        contentType: 'application/json',
        data: JSON.stringify(params),
        beforeSend: function (request) {
            request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        },
        success: function (res) {
            if(res.result == 0){
                depId = res.data[0].departmentId;
            }else{
                errorPrompt("获取单位信息失败！");
                return "";
            }
        },
        error:function(){
            errorPrompt("无法访问网络","")
        }
    });
    return depId;
}

function getCurrentCarparkId() {
    var carparkId =  window.parent.parkID;
    return carparkId;
}

function initSearchCondition() {

    if(typeof(getDepId(getCurrentCarparkId()))=="undefined"){

      return

    }
    var ownCarparkParam = {};
    $.post(starnetContextPath + "/carpark/getOwnCarparkInfo",ownCarparkParam,function (res) {
        if (res.result == 0) {
            $("#carparkIdSearch").empty();
            var item = "<option value='all'>全部车场</option>";
            $("#carparkIdSearch").append(item);
            for(var i in res.data){
                var item = "<option value='" + res.data[i].carparkId + "'>" +
                    res.data[i].carparkName +
                    "</option>";
                $("#carparkIdSearch").append(item);
            }
            $("#carparkId").empty();
            for(var i in res.data){
                var item = "<option value='" + res.data[i].carparkId + "'>" +
                    res.data[i].carparkName +
                    "</option>";
                $("#carparkId").append(item);
            }
            updateSelect();
        }else{
            errorPrompt(res.msg);
        }
    });


}

function deleteFunction(rowIndex){
    if (!confirm("删除后将永久丢失数据，确认要删除？")) {
        return;
    }
    $("#deleteModal").modal("show");
    $("#deleteBtn").unbind("click").bind("click",function () {
        var editData = mainTable.row(rowIndex).data();
        var params = {
            "driverFileId": "",
            "memberWalletId": editData.memberWalletId,
            "carNo":editData.carNo
        };
        $.post(starnetContextPath + "/reservation/deleteReservation", params, function (res) {
            if(res.result == 0){
                mainTable.ajax.reload();
                $("#deleteModal").modal("hide");
                successfulPrompt("预约信息", "删除成功！");
            }else{
                errorPrompt(res.msg);
            }
        });
    });

}

function addRoleFormSet() {
    $('#reservationTitle').text( "添加预约");
    $('#controlMode').val('0');
    $("#reservationModal").modal("show");
}
function edtRoleFormSet(params) {
    $('#reservationTitle').text( "修改预约");
    $('#controlMode').val('1');
    $("#reservationModal").modal("show");
}


/*搜索操作*/
function searchChargeInfo() {

    var carparkId = $("#carparkIdSearch").val();
    if (carparkId == "all") carparkId = "";
    var params = {"carNo":$("#carNoSearch").val(), "driverName":$("#driverNameSearch").val(),
                  "beginTimeMin":$("#startMin").val(), "beginTimeMax":$("#startMax").val(),
                  "endTimeMin":$("#endMin").val(), "endTimeMax":$("#endMax").val(),
                  "carparkId":carparkId, "depId":getDepId(carparkId)
                  };
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload();
}

function outPortExcel(){
    alert("单位编号：" + getDepId());
}

$(function () {
   $("#saveBtn").on("click", function () {

       if (! $("#reservationForm").valid()) {
           return;
       }

       var isRepeat = -1;
       var params = $("#reservationForm").serializeObject();
       if(!params["carparkId"]){
           errorPrompt("请选择车场！");
           return;
       }
       //预约添加单位和车场
       if(params.controlMode == "0")
           params.carparkName = $("#carparkId").text();

       $.ajax({
           url: starnetContextPath + "/reservation/isCarparkRepeat",
           type: "POST",
           dataType: 'json',
           async: false,
           contentType: 'application/json',
           data: JSON.stringify(params),
           beforeSend: function (request) {
               request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
           },
           success: function (res) {
               if(res == true){
                   errorPrompt("在该车场已经存在相同车牌的套餐信息！");
                   isRepeat = 0;
               }
           },
           error:function(){
               errorPrompt("无法访问网络","")
           }
       });
       if(isRepeat == 0) return;



       //alert(JSON.stringify(params));
       $.post(starnetContextPath + "/reservation/controllReservationRecord", params, function (res) {
          if(res.result == 0){
              successfulPrompt("保存预约信息", "成功！");
              $("#reservationModal").modal("hide");
              mainTable.ajax.reload();
          } else{
              errorPrompt(res.msg);
          }
       });

   });
});

function create() {

    
}

// 刷新表格数据
function refreshBtnClick() {
    mainTable.ajax.reload(null,true);
}









