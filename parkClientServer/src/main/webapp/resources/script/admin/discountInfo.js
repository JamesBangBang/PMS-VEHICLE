/**
 * Created by JAMESBANG on 2018/3/20.
 */
var editData;
var autoTable;
var disCountType;
var discountIdForSave;
$(function(){
    iniDataTable();
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
        searching:false,
        autoWidth:true,
        lengthChange:false,
        serverSide:true,
        paging:true,
        ajax: {
            url: starnetContextPath + "/discount/getDiscountInfo",
            type: 'POST'
        },
        columns:[
            {"data":"discount_type_name"},
            {"data":"discount_type"},
            {"data":"discount_info"},
            {"data":"discount_company_name"},
            {"data":"discount_type_id"},
            {"data":"discount_type_duration"},
            {"data":"discount"},
            {"data":"discount_fee"},
            {"data":"operation_source"},
            {"data":"add_time"}
        ],
        "columnDefs": [
            {
                "render": function(data, type, row, meta) {
                    return '<button class="btn btn-info btn-sm"  onclick="editFunction('+meta.row+')">编辑</button>  ' +
                        '<button class="btn btn-info btn-sm btn-danger"  onclick="deleteFunction('+meta.row+')">删除</button> ';
                },
                "targets": 4
            },
            {
                "render": function(data, type, row, meta) {
                    if(data == "0"){
                        return '时间优惠';
                    }else if(data == "1"){
                        return "金额优惠";
                    }
                    else if(data == "2"){
                        return "折扣优惠";
                    }else{
                        return "完全免费";
                    }
                },
                "targets": 1
            },
            {
                "visible": false,
                "targets": [5,6,7,8,9]
            }

        ],

        language:dataTableLanguage

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
        discountId:data.discount_type_id
    };
    discountIdForSave = data.discount_type_id;
    $.post(starnetContextPath + "/discount/getDetailDiscountInfo",params,function(res){
        if(res.result == 0){
            $("#discountForm").populateForm(res.data);
            if (res.data.editDiscountType == "0")
                $("#editDiscountType").val("edtTimeFree");
            else if (res.data.editDiscountType == "1")
                $("#editDiscountType").val("edtAmountFree");
            else if (res.data.editDiscountType == "2")
                $("#editDiscountType").val("edtDiscountFree");
            else
                $("#editDiscountType").val("editTotalFree");
            $("#descModal").modal("show");

        }else{
            errorPrompt("优惠券信息获取失败",res.msg);
        }
    });
}



//删除车场信息
function deleteFunction(rowIndex) {
    var data = autoTable.row(rowIndex).data();
    editData = data;
    $("#deleteModal").modal("show");
}

function deleteDiscountInfo() {
    var params = {
        discountTypeId:editData.discount_type_id
    };
    $.post(starnetContextPath + "/discount/deleteDiscountInfo",params,function(res){
        if(res.result == 0){
            $("#deleteModal").modal("hide");
            successfulPrompt("优惠券删除成功");
            autoTable.ajax.reload(null,false);
        }else{
            errorPrompt("优惠券删除失败",res.msg);
        }
    });
}

function addFunction(){
    $("#addDiscountModal").modal("show");

    var discountTypeSelect = document.getElementById("addDiscountType");
    discountTypeSelect.options[0].selected = true;
    $("#addDiscountTypeLabel").html("优惠时间（单位：分钟）");
    document.getElementById("addDiscountName").value = '';
    document.getElementById("addDiscountInfo").value = '';
    disCountType = '0';
}

function onDiscountTypeChange() {
    if ($("#addDiscountType").val() == "timeFree") {
        $("#addDiscountTypeLabel").html("优惠时间（单位：分钟）");
        disCountType = '0';
    }
    else if($("#addDiscountType").val() == "amountFree") {
        $("#addDiscountTypeLabel").html("优惠金额（单位：元）");
        disCountType = '1';
    }
    else if($("#addDiscountType").val() == "discountFree") {
        $("#addDiscountTypeLabel").html("折扣优惠（单位：%）");
        disCountType = '2';
    }
    else {
        $("#addDiscountTypeLabel").html("完全免费,无需填写");
        disCountType = '3';
    }
}

function changeFunction(){

    if ($("#editDiscountName").val() == ""){
        alert("优惠名称不能为空");
        return false;
    }
    if ($("#editDiscountType").val() != "totalFree"){
        if ($("#editDiscountInfo").val() == "") {
            alert("优惠信息不能为空");
            return false;
        }
    }

    var discountName = $("#editDiscountName").val();

    //判断优惠名称是否重复
    var isRepeatParams = {
        "controlMode" : "update",
        "discountName" : discountName,
        "discountId" : discountIdForSave
    };

    $.ajax({
        url: starnetContextPath + "/discount/isDiscountNameRepeat",
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
                errorPrompt("优惠名称一致，请重新修改！");
                return;
            }else {
                //保存车场信息
                var addParams = {
                    "controlMode" : "update",
                    "discountId" : discountIdForSave,
                    "discountName" : $("#editDiscountName").val(),
                    "discountType" :disCountType,
                    "discountDuration" : $("#editDiscountInfo").val(),
                    "discountCompanyName" : $("#edtDiscountCompanyName").val()
                };
                $.ajax({
                    url: starnetContextPath + "/discount/saveDiscountInfo",
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
                            successfulPrompt("修改信息成功","");
                            $("#descModal").modal("hide");
                        }else{
                            errorPrompt("修改信息失败",res.msg);
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
function saveDiscountInfo() {
    if ($("#addDiscountName").val() == ""){
        alert("优惠名称不能为空");
        return false;
    }
    if ($("#addDiscountType").val() != "totalFree"){
        if ($("#addDiscountInfo").val() == "") {
            alert("优惠信息不能为空");
            return false;
        }
    }

    var discountName = $("#addDiscountName").val();

    //判断优惠名称是否重复
    var isRepeatParams = {
        "controlMode" : "add",
        "discountName" : discountName,
        "discountId" : ""
    };

    $.ajax({
        url: starnetContextPath + "/discount/isDiscountNameRepeat",
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
                errorPrompt("优惠名称一致，请重新修改！");
                return;
            }else {
                //保存车场信息
                var addParams = {
                    "controlMode" : "add",
                    "discountId" : "",
                    "discountName" : $("#addDiscountName").val(),
                    "discountType" :disCountType,
                    "discountDuration" : $("#addDiscountInfo").val(),
                    "discountCompanyName" : $("#addCompanyName").val()
                };
                $.ajax({
                    url: starnetContextPath + "/discount/saveDiscountInfo",
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
                            $("#addDiscountModal").modal("hide");
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

// 刷新表格数据
function refreshBtnClick() {
    autoTable.ajax.reload(null,true);
}







