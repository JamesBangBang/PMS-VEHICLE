
var index=0;
var index1=0;
var mainTable;
var monthPay;
var countPay;
var carNoCount=1;
var parkingLotCount=1;
var eidtId;
var driverFileId;
var payCarCount=1;
var packageType="0";
var isTag=0;
var editParkId="";

$(function(){
    initSelect();//条件：其他收搜条件
    iniDataTable();//表格初始化，不绑定单位，显示所有车场数据
    initSearchCondition();//条件：收费类型
    creatOwner();//添加会员时信息初始化
    init();//月份加减 套餐下拉框 有效车位数功能
    timeSlect();
    formvalidate();
    importExcel();
    excelSave();
    exportExcel();
});



function iniDataTable(){
    mainTable = $('#ownerFile').DataTable( {
        ordering: false,
        processing:true,
        searching:false,
        autoWidth:true,
        serverSide:true,
        paging:true,
        ajax: {
            url: starnetContextPath + "/whitelist/getWhiteList",
            type: 'POST',
            data:{
                "carNo":"",
                "chargeTypeName":'',
                "driverName":"",
                "driverTelephoneNumber":"",
                "driverInfo":"",
                "driverFileId":"",
                "carparkId":"",
                "depId":getDepId(getCurrentCarparkId())
            }
        },
        columns:[
            {"data":"carNo"},
            {"data":"carparkName"},
            {"data":"driverName"},
            {"data":"driverTelephoneNumber"},
            {"data":"driverInfo"},
            {"data":"chargeTypeName"},
            {"data":"validCount"},
            {"data":"memberInfo"},
            {"data":"carparkId"},
            {"data":"depId"},
            {"data":"id"},
            {"data":"driver_file_id"}
        ],
        "columnDefs": [
            {
                "targets":[9,10,11],
                "visible":false
            },
            {
                "render": function(data, type, row, meta) {
                    return '<button class="btn btn-info btn-sm btn-primary"  onclick="editFunction('+meta.row+')">编辑</button> '
                        + '<button class="btn btn-info btn-sm btn-danger" onclick="delOwner('+meta.row+')">删除</button>';


                },
                "targets": 8
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

function searchDriverInfo() {
    var carparkId = $("#carparkIdSearch").val();
    var chargeTypeName = $("#chargeTypeSearch").val();
    if (carparkId == "all") carparkId = "";
    if (chargeTypeName == "all") chargeTypeName = "";
    var params = {
        "carNo":$("#carNoSearch").val(),
        "driverName":$("#driverNameSearch").val(),
        "driverTelephoneNumber":$("#telephoneSearch").val(),
        "driverInfo":$("#driverInfoSearch").val(),
        "carparkId":carparkId,
        "depId":getDepId(getCurrentCarparkId()),
        "chargeTypeName":$("#chargeTypeSearch").val(),
        "overdueTime" : $("#overdueTime").val()
    };
    mainTable.settings()[0].ajax.data = params;
    mainTable.ajax.reload();
}
//初始化搜索条件
function initSelect() {
    $("[name = carparkIdSearch]").empty();
    $("[name = chargeTypeSearch]").empty();
    $("#carNoSearch").val("");
    $("#driverNameSearch").val("");
    $("#telephoneSearch").val("");
    $("#driverInfoSearch").val("");

    var overdueTime = {
        elem: '#overdueTime',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };

    laydate(overdueTime);
}

function updateSelect() {
    $('[name =carparkIdSearch]').chosen({
        width:"100%"
    });
    $('[name =chargeTypeSearch]').chosen({
        width:"100%"
    });
    /*使新赋的值能够刷新成功*/
    $("select").on('chosen:ready', function(e, params) {
        $("select").val("true");//设置值  
    });
    $("select").trigger('chosen:updated');
}

function getDepId(id) {
    var params = {};
    var depId="";
    if (id == "" || id == null){
        return "1";
        //params.carparkId = getCurrentCarparkId();
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
                if(res.data[0].departmentId)
                {
                    depId = res.data[0].departmentId;
                }else {
                    depId = "1";//没有所属单位的车场 ，单位设置为1
                }


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

//加载车场信息和收费类型
function initSearchCondition() {
    $.post(starnetContextPath + "/carpark/getPark", function (res) {
        if (res.result == 0) {
            $("[name = carparkIdSearch]").empty();
            $("[name = carparkIdSearch]").append("<option value='all'>全部车场</option>");
            for(var i in res.data){
                var item = "<option value='" + res.data[i].carparkId + "'>" +
                    res.data[i].carparkName +
                    "</option>";
                $("[name = carparkIdSearch]").append(item);
            }
            updateSelect();
            var html = "";
            for (var j = 0; j < res.data.length; j++) {
                html += "<option value='" + res.data[j].carparkId + "'>" + res.data[j].carparkName +"</option>";
            }

            $("#carParkNameSelect").append(html);
            $("#carParkNameSelect").selectpicker('refresh');
            $("#carParkNameSelect").selectpicker('render');
        }
    });

    var chargeTypeParams ={'depId': getDepId(getCurrentCarparkId())};
    $.post(starnetContextPath + "/report/getChargeTypeInfo", chargeTypeParams, function (res) {
        if (res.result == 0) {
            $("[name = chargeTypeSearch]").empty();
            $("[name = chargeTypeSearch]").append("<option value=''>全部类型</option>");
            for(var i in res.data){
                var item = "<option value='" + res.data[i].kindName + "'>" +
                    res.data[i].kindName +
                    "</option>";
                $("[name = chargeTypeSearch]").append(item);
            }
            updateSelect();
        }else{
            errorPrompt("初始化收费类型失败！");
        }
    });
}

function creatOwner() {

    $("#creatOwner").unbind("click").bind("click",function () {//添加会员信息

        $("#timesBalanceText").hide();
        $("#amountBalanceText").hide();
        $("#timesBalance").text("0");
        $("#amountBalance").text("0");


        $("#balanceText").hide();
        $("#monthChange").val(0);
        $("#countPackage").val(0);
        $("#ownerModal").modal("show");//界面初始化
        $("#crePark").show();
        $(".selectPay").hide();
        $("#submitBtn").show();
        $("#parkingLotContent").hide();
        $("#submitBtn").hide();
        $("#editBtn").hide();

        $("#payTotal").text(0);//缴费金额标题初始化
        $("#daysRemaining").text(0);//剩余天数标题初始化
        $("#title").text("会员信息添加")

        editParkId="";
        eidtId="";


        //还原车牌号数量
        var count=carNoCount;
        for(var i=1;i< count;i++)
        {
            $(".delCarNo").click();

        }
        $("#carNo0").val("")
        $("#carNo0").attr("disabled",false)
        //车主姓名
        $("#driverName").val("")
        //电话
        $("#driverTelephoneNumber").val("")
        //车主信息
        $("#driverInfo").val("")
        //总缴费
        $("#payTotal").text(0);
        //月份初始化
        $("#monthChange").val(0)
        //时间初始化
        $("#startTime").val(new Date(new Date().getTime()).Format("yyyy-MM-dd"));
        $("#endTime").val(new Date(new Date().getTime()).Format("yyyy-MM-dd"));
        //还原车位数量
        count = parkingLotCount;
        for(var i=1;i< count;i++){
            $(".delParkingLot").click();
        }
        $("#parkingLot0").val("")
        //有效车位数
        payCarCount = 1;
        //其他套餐缴费
        $("#otherPackagePay").val(0)
        carNoChange();//添加车位按钮功能
        parkingLotChange();//添加车位按钮功能
        $(".addCarNo").show();
        $(".delCarNo").show();

    })

    //车场选择界面显示初始化
    $("#crePark").unbind("click").bind("click",function () {
        $("#crePark").hide();
        $(".selectPay").show();

    });

    //关闭
    $("#closeCarPark").unbind("click").bind("click",function () {
        $("#crePark").show();
        $(".selectPay").hide();
    });

    //车场确定按钮
    $("#clickCarPark").unbind("click").bind("click",function () {
        var carparkNameForLable = "";
        $("#carParkNameSelect option:selected").each(function () {
            carparkNameForLable = carparkNameForLable + $(this).text() + " ";
        });
        if (carparkNameForLable === "" || carparkNameForLable === null){
            errorPrompt("请选择车场!");
            return;
        }

        $(".selectPay").hide();
        $("#parkingLotContent").show();
        $("#submitBtn").show();

        /*$("#carParkNameLable").text($("#carParkNameSelect option:selected").text());*/
        $("#carParkNameLable").text(carparkNameForLable);
        var carparkIdArr = $("#carParkNameSelect").val();
        carparkIdArr = carparkIdArr.join();
        var params = {
            carparkId : carparkIdArr
        };
        $.post(starnetContextPath + "/whitelist/getAllPackage", params,function (res) {
            if(res.result==0) {
                $("#PackageSelect").empty();
                for(var i in res.data) {
                    var item='<option value="'+res.data[i].id+'" packageKind="'+res.data[i].packageKind+'" monthPackage="'+res.data[i].monthPackage+'" countPackage="'+res.data[i].countPackage+'">'+res.data[i].name+'</option>';
                    $("#PackageSelect").append(item);
                }
            }
            $("#PackageSelect").trigger("change");

        });
    });

    //提交
    $("#submitBtn").unbind("click").bind("click",function () {
        if (!$("#ownerForm").valid()) {
            return;
        }
        if (!$("#payForm").valid()) {
            return;
        }

        $("#submitBtn").attr("disabled","disabled");
        /*if($("#carNumber").val()!= parkingLotCount) {
            errorPrompt("车位数跟有效车位数不同","");
            return;
        }*/
        saveOwnerInfo();
    });


}


function carNoChange() {

    var item;
    var indexTag;
    $(".addCarNo").unbind("click").bind("click",function () {
        //添加行
        index += 1;
        item='<div class="form-group "  id="carNoInput'+index+'">\n' +
            ' <label class="col-sm-3 control-label">  </label>\n' +
            ' <div class="col-sm-8">\n' +
            ' <input type="text"  class="form-control" id="carNo'+index+'" name="carNo">\n' +
            ' </div>\n' +
            ' <label class="col-sm-1 control-label glyphicon-nopaddingLeft lable">\n' +
            ' <span class="glyphicon glyphicon-plus-sign fa-lg gray-color pull-left addCarNo"></span>\n' +
            ' </label>\n' +
            ' </div>';

        //去除按钮
        if(index>1)
        {
            indexTag=index-2;
            $("#carNoInput"+indexTag).find(".lable").remove();
        }
        $("#addCarNoInput").append(item);

        //添加删除按钮
        indexTag = index-1;
        $("#carNoInput"+indexTag).find(".lable").remove();
        item = ' <label class="col-sm-1 control-label glyphicon-nopaddingLeft lable">\n' +
            ' <span class="glyphicon glyphicon-minus-sign fa-lg gray-color pull-left delCarNo"></span>\n' +
            ' </label>\n';
        $("#carNoInput"+indexTag).append(item);

        carNoChange();

        //计数
        carNoCount += 1;
        //增加有效车数
        item = '  <option value="'+carNoCount+'">'+carNoCount+'</option>';
        $("#carNumber").append(item);
    });

    $(".delCarNo").unbind("click").bind("click",function () {

        if(index>0)
        {
            //删除行
            $("#carNoInput"+index).remove();
            index-=1;

            //删除按钮变更添加按钮
            $("#carNoInput"+index).find(".lable").remove();
            item= '<label class="col-sm-1 control-label glyphicon-nopaddingLeft lable">\n' +
                ' <span class="glyphicon glyphicon-plus-sign fa-lg gray-color pull-left addCarNo"></span>\n' +
                ' </label>\n'
            $("#carNoInput"+index).append(item);

            //添加删除按钮
            indexTag=index-1;
            item=' <label class="col-sm-1 control-label glyphicon-nopaddingLeft lable">\n' +
                ' <span class="glyphicon glyphicon-minus-sign fa-lg gray-color pull-left delCarNo"></span>\n' +
                ' </label>\n';
            $("#carNoInput"+indexTag).append(item);

            carNoChange();

            //去除有效车位数
            $("#carNumber option").each(function (i,el) {
                if (parseInt($(el).text()) == carNoCount) {
                    $(this).remove();
                }
            });
            $("#carNumber").append(item);
                //计数
            carNoCount-=1;

        }

    });


}


function parkingLotChange() {

    var item;
    var indexTag;
    $(".addParkingLot").unbind("click").bind("click",function () {
        //添加行
        index1+=1;
        item='<div class="form-group "  id="carNumberInput'+index1+'">\n' +
            ' <div class="col-sm-2 no-right-padding">  </div>\n' +
            ' <div class="col-sm-8  no-left-padding">\n' +
            ' <input type="text"  class="form-control" id="parkingLot'+index1+'" name="parkingLot">\n' +
            ' </div>\n' +
            ' <label class="col-sm-1 control-label glyphicon-nopaddingLeft lable">\n' +
            ' <span class="glyphicon glyphicon-plus-sign fa-lg gray-color pull-left addParkingLot"></span>\n' +
            ' </label>\n' +
            ' </div>';

        //去除按钮
        if(index1>1)
        {
            indexTag=index1-2;
            $("#carNumberInput"+indexTag).find(".lable").remove();
        }
        $("#addCarNumber").append(item);

        //添加删除按钮
        indexTag=index1-1;
        $("#carNumberInput"+indexTag).find(".lable").remove();
        item=' <label class="col-sm-1 control-label glyphicon-nopaddingLeft lable">\n' +
            ' <span class="glyphicon glyphicon-minus-sign fa-lg gray-color pull-left delParkingLot"></span>\n' +
            ' </label>\n';
        $("#carNumberInput"+indexTag).append(item);

        parkingLotChange();
        parkingLotCount+=1;

    });

    $(".delParkingLot").unbind("click").bind("click",function () {

        if(index1>0)
        {
            //删除行
            $("#carNumberInput"+index1).remove();
            index1 -= 1;

            //删除按钮变更添加按钮
            $("#carNumberInput"+index1).find(".lable").remove();
            item = '<label class="col-sm-1 control-label glyphicon-nopaddingLeft lable">\n' +
                ' <span class="glyphicon glyphicon-plus-sign fa-lg gray-color pull-left addParkingLot"></span>\n' +
                ' </label>\n'
            $("#carNumberInput"+index1).append(item);

            //添加删除按钮
            indexTag = index1-1;
            item =' <label class="col-sm-1 control-label glyphicon-nopaddingLeft lable">\n' +
                ' <span class="glyphicon glyphicon-minus-sign fa-lg gray-color pull-left delParkingLot"></span>\n' +
                ' </label>\n';
            $("#carNumberInput"+indexTag).append(item);

            parkingLotChange();
            parkingLotCount -= 1;
        }

    });


}

function timeSlect() {

    var start= {
        elem: '#startTime',
        format: 'YYYY-MM-DD',
        max: '2099-06-16 23:59:59', //最大日期
        istime: true,
        istoday: false,
        start: new Date(new Date().getTime()).Format("yyyy-MM-dd")
        // choose: function (datas) {
        //     end.min = datas; //开始日选好后，重置结束日的最小日期
        //     end.start = datas //将结束日的初始值设定为开始日
        //     monthDisparity();
        //
        // }
    };
    var end = {
        elem: '#endTime',
        format: 'YYYY-MM-DD',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false
        // choose: function (datas) {
        //     start.max = datas; //结束日选好后，重置开始日的最大日期
        //     monthDisparity();
        // }
    };

    laydate(start);
    laydate(end);


    //初始化填充
    var myDate = new Date();
    var timeTemp = myDate.getTime();
    var beginTime = new Date(timeTemp).Format("yyyy-MM-dd");
    // timeTemp+=30 * 24 * 60 * 60 * 1000;
    // var endTime = new Date(timeTemp).Format("yyyy-MM-dd");
    $("#startTime").val(beginTime);
    $("#endTime").val(beginTime);

}


//时间月份差
function  monthDisparity() {
    var strTime = ($("#startTime").val() + " 00:00:00").replace(/-/g, "/");
    strTime = new Date(strTime).getTime();

    var etrTime = ($("#endTime").val() + " 23:59:59").replace(/-/g, "/");
    etrTime = new Date(etrTime).getTime();
    var date = etrTime - strTime;

    var month = Math.ceil(date/(30 * 24 * 60 * 60 * 1000));
    var days = 0;
    days += 30 * month;
     $("#daysRemaining").text(days);

    etrTime = strTime+month*(30 * 24 * 60 * 60 * 1000);
    $("#endTime").val(new Date(etrTime).Format("yyyy-MM-dd"));

    //缴费数
    // var payTotal=parseInt($("#payTotal").text());
    month= Math.floor(date/(30 * 24 * 60 * 60 * 1000));
    if(month==0){
        month = 1;
    }
    var payTotal = 0;
    payTotal += monthPay * month * payCarCount;
    $("#payTotal").text(payTotal);

}

//保存会员信息
function  saveOwnerInfo() {

    var carNo= "";
    for(var i = 0;i < carNoCount; i++) {
        var s;
        s = $("#carNo"+i).val();
        if (s !== ""){
            if(i<carNoCount-1) {
                s = s.replace(/\s+/g, "")+","
            }
            else {
                s = s.replace(/\s+/g, "")
            }
            carNo += s;
        }

    }

    var carParkId = $("#carParkNameSelect").val();
    var carParkName = $("#carParkNameSelect option:selected").text();

    var driverName;
    driverName = $("#driverName").val();

    var driverTelephoneNumber;
    driverTelephoneNumber = $("#driverTelephoneNumber").val();

    var driverInfo;
    driverInfo = $("#driverInfo").val();

    var PackageSelect;
    PackageSelect = $("#PackageSelect").val();

    var carNumber;
    carNumber = $("#carNumber").val();

    var parkingLot = "";
    for(var i=0;i<parkingLotCount;i++)
    {
        var s;
        s= $("#parkingLot"+i).val();
        if(i<parkingLotCount-1)
        {
            s=s.replace(/\s+/g, "")+","
        }
        else {
            s=s.replace(/\s+/g, "")
        }
        parkingLot+=s;

    }

    var params={
        carNo:carNo,
        carParkId:carParkId,
        carParkName:carParkName,
        driverName:driverName,
        driverTelephoneNumber:driverTelephoneNumber,
        driverInfo:driverInfo,
        PackageSelect:PackageSelect,
        carNumber:carNumber,
        parkingLot:parkingLot,
        startTime:$("#startTime").val(),
        endTime:$("#endTime").val(),
        surplusAmount:$("#payTotal").text(),
        surplusNumber:$("#countPackage").val(),
        memberKindId:$("#PackageSelect").val()
    };
    ajaxReuqest(starnetContextPath + "/whitelist/saveOwner","post",params,function (res) {
        if(res.result==0) {

            successfulPrompt("保存成功","");
            $("#submitBtn").removeAttr("disabled");
            $("#ownerModal").modal("hide");
            mainTable.ajax.reload();
        }else{
            errorPrompt("操作失败",res.msg);
            $("#submitBtn").removeAttr("disabled");
        }

    });

}

function delOwner(rowIndex) {
    if (!confirm("删除后将永久丢失数据，确认要删除？")) {
        return;
    }
    var data=mainTable.row(rowIndex).data();
    var params = {
        id:data.id
    };

    ajaxReuqest(starnetContextPath + "/whitelist/delOwner","post",params,function (res) {
        if(res.result==0) {
            successfulPrompt("删除成功","");
            mainTable.ajax.reload();
        }else{
            errorPrompt("操作失败",res.msg);
        }

    });

}

//编辑
function editFunction(rowIndex) {

    $("#otherPackagePay").val(0)
    $("#countPackage").val(0);
    $("#monthChange").val(0);
    var data = mainTable.row(rowIndex).data();
    eidtId = data.id;
    driverFileId = data.driver_file_id;
    var params = {
        id:data.id
    };
    var packageKind;
    var effectiveStartTime;
    var effectiveEndTime;
    var surplusAmount;
    var surplusNumber;

    $("#ownerModal").modal("show");
    $("#submitBtn").hide();
    $("#editBtn").show();
    $("#crePark").hide();
    $(".selectPay").hide();
    $("#closePrkingLot").hide();
    $("#parkingLotContent").show();
    $("#title").text("会员信息编辑")
    carNoChange();
    parkingLotChange();


    ajaxReuqest(starnetContextPath + "/whitelist/editOwner","post",params,function (res) {
        if(res.result==0) {

            if(res.data[0].pack_kind_id == "1"){
                $("#timesBalanceText").show();
                $("#amountBalanceText").hide();
                $("#timesBalance").text(res.data[0].surplus_number);
            }else if(res.data[0].pack_kind_id == "2"){
                $("#timesBalanceText").hide();
                $("#amountBalanceText").show();
                $("#amountBalance").text(res.data[0].surplus_amount);
            }else{
                $("#timesBalanceText").hide();
                $("#amountBalanceText").hide();
            }
            //车牌
            var str;
            var strs

            if(res.data[0].menber_no) {
                 str=res.data[0].menber_no;
                 strs=str.split(",");
                for(var i=1;i< carNoCount;i++) {
                    $(".delCarNo").click();

                }

                for(var i in strs) {
                    if(i>0) {

                        $(".addCarNo").click();
                    }
                    $("#carNo"+i).val(strs[i]);
                    $("#carNo"+i).attr("disabled",true);
                }
            }


            if(res.data[0].parking_lot_id) {
                //车位
                str=res.data[0].parking_lot_id;
                if(str != ''){
                    strs=str.split(",");
                    for(var i=1;i< parkingLotCount;i++)
                    {
                        $(".delParkingLot").click();
                    }

                    for(var i in strs)
                    {
                        if(i>0)
                        {
                            $(".addParkingLot").click();
                        }
                        $("#parkingLot"+i).val(strs[i]);
                        // $("#parkingLot"+i).attr("disabled",true);
                    }
                }

            }


            //车主姓名
            $("#driverName").val(res.data[0].driver_name);
            //联系电话
            $("#driverTelephoneNumber").val(res.data[0].driver_telephone_number);
            //车主信息
            $("#driverInfo").val(res.data[0].driver_info);

            //有效车数
            payCarCount=parseInt(res.data[0].valid_menber_count)
            $("#carNumber").val(payCarCount);


            //车场
            $("#carParkNameSelect").val(res.data[0].carpark_id);
            $("#carParkNameLable").text(res.data[0].carpark_name);


            //车场确定按钮
            packageKind = res.data[0].pack_kind_id;
            var memberKindId = res.data[0].menber_type_id;
            var params = {
                carparkId:res.data[0].carpark_id
            };
            $.post(starnetContextPath + "/whitelist/getAllPackage",params, function (res) {
                if(res.result==0) {

                    $("#PackageSelect").empty();

                    for(var i in res.data) {
                        var item='<option value="'+res.data[i].id+'" packageKind="'+res.data[i].packageKind+'" monthPackage="'+res.data[i].monthPackage+'" countPackage="'+res.data[i].countPackage+'">'+res.data[i].name+'</option>';
                        $("#PackageSelect").append(item);
                    }
                    $("#PackageSelect").val(memberKindId);
                    $("#PackageSelect").trigger("change");

                }
            });

            surplusAmount=res.data[0].surplus_amount;
            surplusNumber=res.data[0].surplus_number;

            //缴费 时间 初始化
            if(res.data[0].pack_kind_id==0 || res.data[0].pack_kind_id==-3)
            {
                effectiveStartTime=new Date(res.data[0].effective_start_time).Format("yyyy-MM-dd");
                effectiveEndTime=new Date(res.data[0].effective_end_time).Format("yyyy-MM-dd");
                $("#payTotal").text(0);
                $("#daysRemaining").text(0);
                monthPay = parseFloat(res.data[0].month_package);
                //包月时间
                $("#startTime").val(effectiveStartTime);
                $("#endTime").val(effectiveEndTime);
                $("#payTotal").text(res.data[0].surplus_amount);


            }else {
                $("#payTotal").text(res.data[0].surplus_amount);
                $("#daysRemaining").text(0);
            }
        }
        $(".addCarNo").hide();
        $(".delCarNo").hide();
    });

}

//表单验证
function  formvalidate() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#ownerForm").validate({
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
                required: true,
                isMobile:true
            }
            /*parkingLot: {
                required: true
            }*/
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

            driverTelephoneNumber: {
                required: icon + "请输入电话号码"
            }
            /*parkingLot:{
                required: icon + "请输入车位"
            }*/

        }
    });

    $("#payForm").validate({
        rules: {
            /*parkingLot: {
                required: true,

            },*/
            otherPackagePay:{
                required: true,
                number:true,
                min:0
            }
        },
        messages: {
            /*parkingLot:{
                required: icon + "请输入车位"
            },*/
            otherPackagePay:{
                required: icon + "请输入缴费数"
            }

        }

    });
}

function init() {
    //套餐下拉框变更
    $("#PackageSelect").change(function() {
        $("#payTotal").text(0)
        $("#daysRemaining").text(0);
        var packageKind = $("#PackageSelect option:selected").attr("packageKind");
        if(packageKind == "0"){
            $(".freePackage").hide();
            $(".otherPackage").hide();
            $(".countPackage").hide();
            $(".monthPackage").show();
            monthPay = parseFloat($("#PackageSelect option:selected").attr("monthPackage"));
            $("#monthPackage").trigger("change");
        }else if(packageKind == "1"){
            $(".monthPackage").hide();
            $(".freePackage").hide();
            $(".otherPackage").hide();
            $(".countPackage").show();
            countPay = parseFloat($("#PackageSelect option:selected").attr("countPackage"));
            $("#countPackage").trigger("change");
        } else if(packageKind == "-3"){
            $(".monthPackage").hide();
            $(".countPackage").hide();
            $(".otherPackage").hide();
            $(".freePackage").show();
        }else{
            $(".monthPackage").hide();
            $(".freePackage").hide();
            $(".countPackage").hide();
            $(".otherPackage").show();
        }

    });

    //有效车位数变更
    $("#carNumber").change(function() {
        payCarCount = parseInt($("#carNumber").val());
    });

    $("#closePrkingLot").unbind("click").bind("click",function () {
        $(".selectPay").show();
        $("#parkingLotContent").hide();
    });

    //月份加
    $("#increase").unbind("click").bind("click",function () {
        var count = parseInt($("#monthChange").val());
        var days = parseInt($("#daysRemaining").text());
        var payTotal = parseInt($("#payTotal").text());
        var day30 = 30 * 24 * 60 * 60 * 1000;
        var currentTime = $("#endTime").val().replace(/-/g, '/');//ie支持
        currentTime = new Date(currentTime).getTime();
        currentTime += day30;
        $("#endTime").val(new Date(currentTime).Format("yyyy-MM-dd"));

        days += 30;
        count += 1;
        payTotal += monthPay * payCarCount;
        $("#monthChange").val(count);
        $("#daysRemaining").text(days);
        $("#payTotal").text(payTotal)
    });

    //月份减
    $("#reduce").unbind("click").bind("click",function () {
        var count =parseInt($("#monthChange").val());
        var days=parseInt($("#daysRemaining").text());
        var payTotal=parseInt($("#payTotal").text());

        if(count==0){
            return false;
        }
        var day30 = 30 * 24 * 60 * 60 * 1000;
        var currentTime=$("#endTime").val().replace(/-/g, '/');
        currentTime=new Date(currentTime).getTime()
        currentTime-=day30;
        $("#endTime").val(new Date(currentTime).Format("yyyy-MM-dd"));

        count -= 1;
        days -= 30;

        payTotal -= monthPay * payCarCount;
        if(payTotal < 0){
            payTotal = 0;
        }
        $("#daysRemaining").text(days);
        $("#monthChange").val(count);
        $("#payTotal").text(payTotal)
    });

    //其他套餐缴费
    $("#otherPackagePay").bind('input propertychange', function() {
        if($("#otherPackagePay").val() == ''){
            $("#payTotal").text(0);
        }else{
            $("#payTotal").text( $("#otherPackagePay").val());
        }

    });
}

//次数加
$("#increaseCount").click(function () {
    var count = parseInt($("#countPackage").val());

    count += 1;
    $("#countPackage").val(count);
    $("#countPackage").trigger("change");
});

//次数减
$("#reduceCount").click(function () {
    var count = parseInt($("#countPackage").val());
    if(count == 0) {
        return false;
    }
    count -= 1;
    $("#countPackage").val(count);
    $("#countPackage").trigger("change");
});

$("#countPackage").change(function(){
    var tmp = $("#countPackage").val();
    if(tmp == ''){
        tmp = "0";
    }
    var count = parseInt(tmp);
    var payTotal = (countPay * payCarCount * count);

    $("#payTotal").text(payTotal)
});


$("#countPackage").keyup(function(){
    var tmp = $("#countPackage").val();
    if(tmp == ''){
        tmp = "0";
        $("#countPackage").val("0")
    }
    var count = parseInt(tmp);
    var payTotal = (countPay * payCarCount * count);

    $("#payTotal").text(payTotal);
});

/**
 * 修改续费
 */
function  updateOwnerInfo() {

    var carNo= "";
    for(var i = 0;i < carNoCount; i++) {
        var s;
        s = $("#carNo"+i).val();
        if(i<carNoCount-1) {
            s = s.replace(/\s+/g, "")+","
        }
        else {
            s = s.replace(/\s+/g, "")
        }
        carNo += s;

    }

    var carParkId = $("#carParkNameSelect").val();
    var carParkName = $("#carParkNameSelect option:selected").text();

    var driverName;
    driverName = $("#driverName").val();

    var driverTelephoneNumber;
    driverTelephoneNumber = $("#driverTelephoneNumber").val();

    var driverInfo;
    driverInfo = $("#driverInfo").val();

    var PackageSelect;
    PackageSelect = $("#PackageSelect").val();

    var carNumber;
    carNumber = $("#carNumber").val();

    var parkingLot = "";
    for(var i=0;i<parkingLotCount;i++)
    {
        var s;
        s= $("#parkingLot"+i).val();
        if(i<parkingLotCount-1)
        {
            s=s.replace(/\s+/g, "")+","
        }
        else {
            s=s.replace(/\s+/g, "")
        }
        parkingLot+=s;

    }

    var params = {
        driverName:driverName,
        driverTelephoneNumber:driverTelephoneNumber,
        driverInfo:driverInfo,
        PackageSelect:PackageSelect,
        carNumber:carNumber,
        parkingLot:parkingLot,
        startTime:$("#startTime").val(),
        endTime:$("#endTime").val(),
        surplusAmount:$("#payTotal").text(),
        surplusNumber:$("#countPackage").val(),
        memberKindId:$("#PackageSelect").val(),
        driverFileId:driverFileId,
        id:eidtId
    };
    ajaxReuqest(starnetContextPath + "/whitelist/updateOwner","post",params,function (res) {
        if(res.result==0) {
            successfulPrompt("操作成功","");
            $("#ownerModal").modal("hide");
            mainTable.ajax.reload();
        }else{
            errorPrompt("操作失败",res.msg);
        }

    });

}

$("#editBtn").click(function () {
    if (!$("#ownerForm").valid()) {
        return;
    }
    if (!$("#payForm").valid()) {
        return;
    }

    /*if($("#carNumber").val()!= parkingLotCount) {
        errorPrompt("车位数跟有效车位数不同","");
        return;
    }*/
    updateOwnerInfo();
});





function importExcel(){
    $("#importExcel").unbind("click").click(function () {
        $("#importExcelModal").modal("show")
    });

}


function exportExcel(){

    $("#exportExcel").unbind("click").click(function () {

        var carparkId = $("#carparkIdSearch").val();
        var chargeTypeName = $("#chargeTypeSearch").val();
        if (carparkId == "all") carparkId = "";
        if (chargeTypeName == "all") chargeTypeName = "";
        var carNo=$("#carNoSearch").val();
        var driverName=$("#driverNameSearch").val();
        var driverTelephoneNumber=$("#telephoneSearch").val();
        var driverInfo=$("#driverInfoSearch").val();
        var carparkId=carparkId;
        var depId=getDepId(getCurrentCarparkId());
        var packageKind=$("#chargeTypeSearch").val();
     // alert(carNo);
        location.href = starnetContextPath + "/member/kind/exportExcel?"+
            "carNo="+  encodeURI(carNo)+""+
            "&chargeTypeName="+ encodeURI(chargeTypeName)+""+
            "&driverName="+ encodeURI(driverName)+""+
            "&driverTelephoneNumber="+ encodeURI(driverTelephoneNumber)+""+
            "&driverInfo="+ encodeURI(driverInfo)+""+
            "&carparkId="+ encodeURI(carparkId)+""+
            "&depId="+ encodeURI(depId)+""+
            "&packageKind="+ encodeURI(packageKind)+"";

    });

}


function uploadExcel(){
    var body = $(window.frames['importExcelFrame'].document.body);
    var res = body.context.innerHTML + "";
    var obj = JSON.parse(res);
    if(obj.result == 0){
        if(obj.issueRet==="1"){
            errorPrompt(obj.msg);
        }else {
            $('#importExcelModal').modal('hide');
            successfulPrompt("导入白名单成功成功");
            mainTable.ajax.reload();
        }
        $("#excleLoadingGraph").hide();

    }else{
        errorPrompt(obj.msg);
    }
}


function  excelSave() {
        $("#importExcelModalForm").attr("action",starnetContextPath+"/member/kind/uploadExcel");//action初始化
        $("#importMode").change(function () {
            type=$("#importMode").val();
            $("#importExcelModalForm").attr("action",starnetContextPath+"/person/uploadExcel?importType="+type+"" +
                "&operatorId="+window.parent.userId+"&operatorName="+window.parent.userName+"");
        })


}

function getContextPath() {
    var pathName = document.location;
    var str=""+pathName;
    var ff=str.split("/");
    starnetContextPath=ff[0]+"//"+ff[1]+ff[2]+"/";
    var result = starnetContextPath;

    return result;
}

// 刷新表格数据
function refreshBtnClick() {
    mainTable.ajax.reload(null,true);
}