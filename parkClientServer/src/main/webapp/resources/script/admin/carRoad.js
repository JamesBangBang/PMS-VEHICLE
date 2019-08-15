/**
 * Created by 刘阳弘 on 2017-12-27.
 */
var carRoad={
    params:null,
    roadTable:null,
    res:0,
    data:null,
    //判断同一个车场下车道名是否相同
    isCarRoadNameRepeat:function () {
        this.params={
            carRoadParkId:  $("#carRoadParkId").val(),
            carRoadName:  $("#carRoadName").val()
        };
        $.ajax({
            url: starnetContextPath + "/carpark/isCarRoadNameRepeat",
            type: "post",
            async: false,
            data: JSON.stringify(this.params),
            dataType: 'json',
            contentType: 'application/json',
            beforeSend: function (request) {
                request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            },
            success: function (res) {
                if(res){
                    errorPrompt("同一车场下两个车道名称不能相同！");
                    //设置标志
                    carRoad.res=1;
                }},
            error:function(){
                errorPrompt("无法访问网络","");
            }
        });


    },
    //车道表格初始化
    carRoadTabel:function () {
        //清空表格
        if(this.roadTable){
            this.roadTable.destroy();
        }
        this.roadTable = $('#roadTable').DataTable( {
            sort:false,
            processing:true,
            serverSide:true,
            searching:false,
            autoWidth:true,
            ajax: {
                url: starnetContextPath + "/carpark/getCarRoadInfoList",
                type: 'POST'
            },
            columns:[
                {"data":"car_road_name"},
                {"data":"car_road_type"},
                {"data":"carpark_name"},
                {"data":"post_computer_name"},
                {"data":"car_road_id"},
                {"data":"own_carpark_no"}
            ],
            "columnDefs": [
                {
                    "render": function(data, type, row, meta) {
                        return '<button class="btn btn-info btn-sm "  onclick="carRoad.editFunction('+meta.row+')">编辑</button>' +
                            '<button class="btn btn-danger btn-sm" onclick="carRoad.deleteCarRoadConfirm('+meta.row+')">删除</button> '+
                            '<button class="btn btn-success btn-sm" onclick="carRoad.openFleetMode('+meta.row+')">打开车队模式</button> '+
                            '<button class="btn btn-success btn-sm" onclick="carRoad.closeFleetMode('+meta.row+')">关闭车队模式</button> '+
                            ' ';
                    },
                    "targets": 4
                },
                {
                    "render": function(data, type, row, meta) {
                        if(data === "1"){
                            return '出口';
                        }else{
                            return '入口';
                        }
                    },
                    "targets": 1
                },
                {
                    "targets": [ 5 ],
                    "visible": false
                }

            ],

            language:dataTableLanguage

        });
    },

    //添加车道表单初始化
    getAllPark:function () {
        $.ajax({
            url: starnetContextPath + "/carpark/getAllCarParksInfo",
            type: "post",
            async: false,
            dataType: 'json',
            contentType: 'application/json',
            beforeSend: function (request) {
                request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            },
            success: function (res) {
                if(res.result==0){
                    if(res.data)
                    {
                        $("#carRoadParkId").empty();
                       /* $("#carRoadParkId").append("<option value=''>无</option>");*/
                        for(var i in res.data)
                        {
                            $("#carRoadParkId").append("<option value="+res.data[i].carpark_id+">"+res.data[i].carpark_name+"</option>");
                        }
                    }
                }},
            error:function(){
                errorPrompt("无法访问网络","")
            }
        });

    },

    //初始化所有岗亭信息
    getAllPost:function () {
        $.ajax({
            url: starnetContextPath + "/carpark/getAllPost",
            type: "post",
            async: false,
            dataType: 'json',
            contentType: 'application/json',
            beforeSend: function (request) {
                request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            },
            success: function (res) {
                if(res.result==0){
                    if(res.data)
                    {
                        $("#carRoadPostId").append("<option value=''>无</option>");
                        for(var i in res.data)
                        {
                            $("#carRoadPostId").append("<option value="+res.data[i].post_computer_id+">"+res.data[i].post_computer_name+"</option>");
                        }
                    }
                }},
            error:function(){
                errorPrompt("无法访问网络","")
            }
        });
    },

    button:function () {
        //表单内容初始化
        $("#creatRoad").bind("click",function () {
            carRoad.getAllPark();
            $("#carRoadName").val("");
            $("#carRoadParkName").val("");
            $("#carRoadType").val("0");
            //按钮显示
            $("#saveCarRoad").show();
            $("#eidtCarRoad").hide();
            //表单权限
            $('#carRoadName').attr("disabled",false);
            $('#carRoadParkId').attr("disabled",false);
            $('#carRoadType').attr("disabled",false);
            $('#carRoadPostId').attr("disabled",false);
            // $("#carRoadForm").reset();
        });



        //保存智能纠错信息
        $('#savepipei').unbind("click").click(function () {
            var one=$('#one').val();
            var two=$('#two').val();
            var item =
                '<tr>'+
                '<td  class="one" >'+one+'</td>'+
                '<td >'+'----->'+'</td>'+
                '<td  class="two">'+two+'</td>'+
                '</tr>';
            $("#whieteTable").append(item);
        });

        //保存车牌纠正信息
        $('#saveAllCarno').unbind("click").click(function () {
            var one=$('#oneAllCarno').val();
            var two=$('#twoAllCarno').val();
            var item =
                '<tr>'+
                '<td  class="one" >'+one+'</td>'+
                '<td >'+'----->'+'</td>'+
                '<td  class="two">'+two+'</td>'+
                '</tr>';
            $("#whieteTableAllCarno").append(item);
        });








        //保存车道信息
        $("#saveCarRoad").bind("click",function () {

            if ($("#carRoadName").val() == ""){
                alert("车道名称不能为空");
                return false;
            }
            if ($("#carRoadType").val() == ""){
                alert("请选择车道类型");
                return false;
            }
            if ($("#carRoadParkId").val() == ''){
                alert("请选择所属车场");
                return false;
            }
            if ($("#carRoadPostId").val() == ''){
                alert("请选择所属岗亭");
                return false;
            }


            carRoad.isCarRoadNameRepeat();
            if(carRoad.res==1)
            {   carRoad.res=0;
                return;
            }


            //模糊匹配信息保存
            //入口白名单
            var white = [];
            if($("#whiteInput").is(":checked")) {
                if(!$("#min").is(":checked")){
                    white.push(1+';')
                }
                if(!$("#a").is(":checked")){
                    white.push(2+';')
                }
                if(!$("#1").is(":checked")){
                    white.push(3+';')
                }
                if(!$("#2").is(":checked")){
                    white.push(4+';')
                }
                if(!$("#3").is(":checked")){
                    white.push(5+';')
                }
                if(!$("#4").is(":checked")){
                    white.push(6+';')
                }
                if(!$("#5").is(":checked")){
                    white.push(7+';')
                }

               var  minwhite=$("#minwhite").val();

                var whitetd = [];
                var trList = $("#whieteTable").children("tr");
                for (var i=0;i<trList.length;i++) {
                    var tdArr = trList.eq(i).children("td");
                    for(var j=0;j<tdArr.length;j++){
                        // alert(tdArr[j].innerText);
                        if(tdArr[j].innerText=='----->'){
                            whitetd.push(",")
                        }else {
                            whitetd.push(tdArr[j].innerText);
                        }

                    }
                }
                var result1 = [];
                for(var i=0,len=whitetd.length;i<len;i+=3){
                    result1.push(whitetd.slice(i,i+3));
                }

                var allCarnoTd = [];
                var trListAllCarno = $("#whieteTableAllCarno").children("tr");
                for (var i=0;i<trListAllCarno.length;i++) {
                    var tdArrAllCarno = trListAllCarno.eq(i).children("td");
                    for(var j=0;j<tdArrAllCarno.length;j++){
                        if(tdArrAllCarno[j].innerText=='----->'){
                            allCarnoTd.push(",")
                        }else {
                            allCarnoTd.push(tdArrAllCarno[j].innerText);
                        }

                    }
                }
                var resultAllCarno = [];
                for(var i=0,len=allCarnoTd.length;i<len;i+=3){
                    resultAllCarno.push(allCarnoTd.slice(i,i+3));
                }



            }

            //模糊匹配信息保存
            //出口临时车
            var temp = [];
            if($("#tempInput").is(":checked")) {
                if(!$("#min_temp").is(":checked")){
                    temp.push(1+';')
                }
                if(!$("#a_temp").is(":checked")){
                    temp.push(2+';')
                }
                if(!$("#1_temp").is(":checked")){
                    temp.push(3+';')
                }
                if(!$("#2_temp").is(":checked")){
                    temp.push(4+';')
                }
                if(!$("#3_temp").is(":checked")){
                    temp.push(5+';')
                }
                if(!$("#4_temp").is(":checked")){
                    temp.push(6+';')
                }
                if(!$("#5_temp").is(":checked")){
                    temp.push(7+';')
                }

                var  mintemp=$("#mintemp").val();

                var whitetd = [];
                var trList = $("#whieteTable").children("tr");
                for (var i=0;i<trList.length;i++) {
                    var tdArr = trList.eq(i).children("td");
                    for(var j=0;j<tdArr.length;j++){
                        // alert(tdArr[j].innerText);
                        if(tdArr[j].innerText=='----->'){
                            whitetd.push(",")
                        }else {
                            whitetd.push(tdArr[j].innerText);
                        }

                    }
                }
                var result2 = [];
                for(var i=0,len=whitetd.length;i<len;i+=3){
                    result2.push(whitetd.slice(i,i+3));
                }
                // console.log(result)

            }

             var result;
            if(($("#flagadd").val())==1){
                result=result1;
            }
            if(($("#flagadd").val())==2){
                result=result2;
            }


            //保存的信息
            this.params={
                carRoadName: $("#carRoadName").val(),
                carRoadParkId:$("#carRoadParkId").val(),
                carRoadType:$("#carRoadType").val(),
                carRoadPostId:$("#carRoadPostId").val(),
                result:result,
                resultAllCarno:resultAllCarno,
                minwhite:minwhite,
                white:white,
                temp:temp,
                mintemp:mintemp

            };



            var carloadType=$("#carRoadType").val();
            if(carloadType==0){
                if($("#whiteInput").is(":checked")) {
                    delete this.params.temp;
                    delete this.params.mintemp;

                }


            }




            ajaxReuqest(starnetContextPath + "/carpark/saveCarRoad","post",this.params,function (res) {
                if(res.result == 0){
                    $("#addCarRoadModal").modal("hide");
                    carRoad.roadTable.ajax.reload(null,false);
                    // carRoad.carRoadTabel();
                    successfulPrompt("操作成功","");
                }else{
                    errorPrompt("操作失败",res.msg);
                }
            });

        })

    },
    //删除
    deleteFu1nction:function()
    {
        this.params={
            carRoadId:this.data.car_road_id
        }
            $.post(starnetContextPath + "/carpark/deleteCarRoadInfo",this.params,function(res){
                if(res.result == 0){
                    successfulPrompt("车道删除成功");
                    carRoad.roadTable.ajax.reload(null,false);
                }else{
                    errorPrompt("车道删除失败",res.msg);
                }
            });
    },
    //删除确认
    deleteCarRoadConfirm:function (rowIndex) {
        this.data= this.roadTable.row(rowIndex).data();
        $("#deleteCarRoadModal").modal("show");
        $("#deleteCarRaodBtn").bind("click",function () {
            carRoad.deleteFu1nction();
            $("#deleteCarRoadModal").modal("hide");
            //移除点击事件
            $('#deleteCarRaodBtn').unbind("click");

        });

    },

    //打开车队模式
    openFleetMode:function (rowIndex) {
        this.data = this.roadTable.row(rowIndex).data();
        this.params = {
            carRoadId : this.data.car_road_id
        };
        ajaxReuqest(starnetContextPath + "/carpark/openFleetMode","post",this.params,function(res){
            if(res.result == 0){
                successfulPrompt("打开车队模式成功","");
            }else{
                errorPrompt("打开车队模式失败",res.msg);
            }
        });
    },
    //关闭车队模式
    closeFleetMode:function (rowIndex) {
        this.data = this.roadTable.row(rowIndex).data();
        this.params = {
            carRoadId : this.data.car_road_id
        };
        ajaxReuqest(starnetContextPath + "/carpark/closeFleetMode","post",this.params,function(res){
            if(res.result == 0){
                successfulPrompt("关闭车队模式成功","");
            }else{
                errorPrompt("关闭车队模式失败",res.msg);
            }
        });
    },

    //详情信息
    detailsCarRoad:function (rowIndex) {
        //显示表单
        $("#addCarRoadModal").modal("show");
        //标题更改
        $("#carRoadTitle")[0] .innerText="车道信息";
        //按钮显示
        $("#saveCarRoad").hide();
        $("#eidtCarRoad").hide();
        //表单权限
        $('#carRoadName').attr("disabled",true);
        $('#carRoadParkId').attr("disabled",true);
        $('#carRoadType').attr("disabled",true);
        $('#carRoadPostId').attr("disabled",true);


        this.data= this.roadTable.row(rowIndex).data();
        this.params={
            carRoadId:this.data.car_road_id,
            carParkId:this.data.own_carpark_no

        };
        ajaxReuqest(starnetContextPath + "/carpark/detailsCarRoadInfo","post",this.params,function(res){
            if(res.result == 0){
                $("#carRoadName").val(res.data.car_road_name);
                $("#carRoadParkId").val(res.data.own_carpark_no);
                $("#carRoadType").val(res.data.car_road_type);
                $("#carRoadPostId").val(res.data.manage_computer_id);
                carRoad.data=res.data;
                //模糊查询
                if(carRoad.data.is_auto_match_car_no==1){
                     console.log(carRoad.data)
                    $('.tag1').siblings("ins").click();
                    $("#minwhite").val(carRoad.data.auto_match_least_bite);
                    //白名单

                    var autoWhite= carRoad.data.auto_match_car_no_pos;
                    var brr=[];
                    if(autoWhite){
                        brr= autoWhite.split(";");
                    }

                    var arr=[1,2,3,4,5,6,7];
                    for(var i=0;i<brr.length;i++){
                        for(var j=0;j<arr.length;j++){
                            if(arr[j]==brr[i]){
                                arr.splice(j,1);
                                j--;
                            }
                        }
                    }

                    for(var i in arr){
                        if(arr[i]==1){
                            $('.min').siblings("ins").click();
                        }
                        if(arr[i]==2){
                            $('.a').siblings("ins").click();
                        }
                        if(arr[i]===3){
                            $('.white1').siblings("ins").click();
                        }
                        if(arr[i]===4){
                            $('.white2').siblings("ins").click();
                        }
                        if(arr[i]===5){
                            $('.white3').siblings("ins").click();
                        }
                        if(arr[i]==6){
                            $('.white4').siblings("ins").click();
                        }
                        if(arr[i]==7){
                            $('.white5').siblings("ins").click();
                        }
                    }


                }
//临时车初始化
                /*if(carRoad.data.is_auto_match_tmp_car_no==1){
                    $('.tag2').siblings("ins").click();
                    $("#mintemp").val(carRoad.data.match_tmp_least_bite)
                    //临时车

                    var autoWhite= carRoad.data.match_tmp_car_no_pos;
                    var brr=[];
                    if(autoWhite){
                        brr= autoWhite.split(";");
                    }

                    var arr=[1,2,3,4,5,6,7];
                    for(var i=0;i<brr.length;i++){
                        for(var j=0;j<arr.length;j++){
                            if(arr[j]==brr[i]){
                                arr.splice(j,1);
                                j--;
                            }
                        }
                    }
                    for(var i in arr){
                        if(arr[i]==1){
                            $('.min_temp').siblings("ins").click();
                        }
                        if(arr[i]==2){
                            $('.a_temp').siblings("ins").click();
                        }
                        if(arr[i]==3){
                            $('.temp1').siblings("ins").click();
                        }
                        if(arr[i]==4){
                            $('.temp2').siblings("ins").click();
                        }
                        if(i==5){
                            $('.temp3').siblings("ins").click();
                        }
                        if(arr[i]==6){
                            $('.temp4').siblings("ins").click();
                        }
                        if(arr[i]==7){
                            $('.temp5').siblings("ins").click();
                        }
                    }
                }*/







            }else{
                errorPrompt("车场信息获取失败",res.msg);
            }
        });
    },
    //修改车道信息
    editFunction:function (rowIndex) {
        document.getElementById("carRoadForm").reset();

        //获取车场信息
        carRoad.detailsCarRoad(rowIndex);
        carRoad.data = carRoad.roadTable.row(rowIndex).data();
        console.log(carRoad.data);
        //标题更改
        $("#carRoadTitle")[0].innerText = "编辑车道";
        //按钮显示
        $("#eidtCarRoad").show();
        //表单权限
        $('#carRoadName').attr("disabled", false);
        $('#carRoadParkId').attr("disabled", false);
        $('#carRoadType').attr("disabled", false);
        $('#carRoadPostId').attr("disabled", false);

        /*回写智能纠错*/

        if (carRoad.data.white_intelligent_correction == 1) {
            $("#whieteTable").empty();
            var array = carRoad.data.white_intelligent_correction;
            if (array.indexOf(";")!=-1) {
                var correction = array.split(";")

                for (var i  in correction) {

                    var correctionNew=[];
                    correctionNew = correction[i].split(",");
                    var one = correctionNew[0];

                    var two = correctionNew[1];
                    var item =
                        '<tr>' +
                        '<td  class="one" >' + one + '</td>' +
                        '<td >' + '----->' + '</td>' +
                        '<td  class="two">' + two + '</td>' +
                        '</tr>';
                    $("#whieteTable").append(item);
                }
            } else {
                var correction = carRoad.data.white_intelligent_correction.split(",")
                var one = correction[0];
                var two = correction[1];
                var item =
                    '<tr>' +
                    '<td  class="one" >' + one + '</td>' +
                    '<td >' + '----->' + '</td>' +
                    '<td  class="two">' + two + '</td>' +
                    '</tr>';
                $("#whieteTable").append(item);
            }


        }

       /* if (carRoad.data.intelligent_correction) {
            $("#whieteTableAllCarno").empty();
            var array = carRoad.data.intelligent_correction;
            if (array.indexOf(";")!=-1) {
                var correction = array.split(";")

                for (var i  in correction) {

                    var correctionNew=[];
                    correctionNew = correction[i].split(",");
                    var one = correctionNew[0];

                    var two = correctionNew[1];
                    var item =
                        '<tr>' +
                        '<td  class="one" >' + one + '</td>' +
                        '<td >' + '----->' + '</td>' +
                        '<td  class="two">' + two + '</td>' +
                        '</tr>';
                    $("#whieteTableAllCarno").append(item);
                }
            } else {
                var correction = carRoad.data.intelligent_correction.split(",")
                var one = correction[0];
                var two = correction[1];
                var item =
                    '<tr>' +
                    '<td  class="one" >' + one + '</td>' +
                    '<td >' + '----->' + '</td>' +
                    '<td  class="two">' + two + '</td>' +
                    '</tr>';
                $("#whieteTableAllCarno").append(item);
            }


        }*/

       //模糊查询
        if(carRoad.data.is_auto_match_car_no==0){
            document.getElementById("whiteInput").checked = false;
        }
        if(carRoad.data.is_auto_match_car_no==1){
            document.getElementById("whiteInput").checked = true;
            $('.tag1').siblings("ins").click();
            $("#minwhite").val(carRoad.data.auto_match_least_bite);
            //白名单

            var autoWhite= carRoad.data.auto_match_car_no_pos;
            var brr=[];
            if(autoWhite){
                brr= autoWhite.split(";");

            }

            var arr=[1,2,3,4,5,6,7];
            for(var i=0;i<brr.length;i++){
                for(var j=0;j<arr.length;j++){
                    if(arr[j]==brr[i]){
                        arr.splice(j,1);
                        j--;
                    }
                }
            }

            for(var i in arr){
                if(arr[i]==1){
                    // $('.min').siblings("ins").click();
                    $("#min").prop("checked",false);

                }
                if(arr[i]==2){
                    $("#a").prop("checked",false);
                }
                if(arr[i]==3){
                    // $('.white1').siblings("ins").click();
                    $("#1").prop("checked",false);
                }
                if(arr[i]==4){
                    // $('.white2').siblings("ins").click();
                    $("#2").prop("checked",false);
                }
                if(arr[i]==5){
                    $("#3").prop("checked",false);
                    // $('.white3').siblings("ins").click();
                }
                if(arr[i]==6){
                    $("#4").prop("checked",false);
                    // $('.white4').siblings("ins").click();
                }
                if(arr[i]==7){
                    $("#5").prop("checked",false);
                    // $('.white5').siblings("ins").click();
                }
            }


        }


//临时车初始化

        /*if(carRoad.data.is_auto_match_tmp_car_no==0){
            $("#tempInput").prop("checked",false);
            // $('.tag2').siblings("ins").click();
        }
        if(carRoad.data.is_auto_match_tmp_car_no==1){
            $("#tempInput").prop("checked",true);
            $('.tag2').siblings("ins").click();
            $("#mintemp").val(carRoad.data.match_tmp_least_bite)
            //临时车

            var autoWhite= carRoad.data.match_tmp_car_no_pos;
            var brr=[];
            if(autoWhite){
                brr= autoWhite.split(";");
            }

            var arr=[1,2,3,4,5,6,7];
            for(var i=0;i<brr.length;i++){
                for(var j=0;j<arr.length;j++){
                    if(arr[j]==brr[i]){
                        arr.splice(j,1);
                        j--;
                    }
                }
            }
            for(var i in arr){
                if(arr[i]==1){
                    $("#min_temp").prop("checked",false);
                    // $('.min_temp').siblings("ins").click();
                }
                if(arr[i]==2){
                    // $('.a_temp').siblings("ins").click();
                    $("#a_temp").prop("checked",false);
                }
                if(arr[i]==3){
                    $("#1_temp").prop("checked",false);
                    // $('.temp1').siblings("ins").click();
                }
                if(arr[i]==4){
                    $("#2_temp").prop("checked",false);
                    // $('.temp2').siblings("ins").click();
                }
                if(arr[i]==5){
                    $("#3_temp").prop("checked",false);
                    // $('.temp3').siblings("ins").click();
                }
                if(arr[i]==6){
                    $("#4_temp").prop("checked",false);
                    // $('.temp4').siblings("ins").click();
                }
                if(arr[i]==7){
                    $("#4_temp").prop("checked",false);
                    // $('.temp5').siblings("ins").click();
                }
            }
        }*/




        //修改车道信息
        $("#eidtCarRoad").bind("click",function () {
            var isAutoMatch;
            if($("#whiteInput").is(":checked")){
                isAutoMatch=true;
            }

            if(!$("#whiteInput").is(":checked")){
                isAutoMatch=false;
            }
            var isAutoMatchTemp;
            if($("#tempInput").is(":checked")){
                isAutoMatchTemp=true;
            }
            if(!$("#tempInput").is(":checked")){

                isAutoMatchTemp=false;
            }


            //模糊匹配信息保存
            //入口白名单

            var white = [];
            if($("#whiteInput").is(":checked")) {
                if(!$("#min").is(":checked")){
                    white.push(1+';')

                }
                if(!$("#a").is(":checked")){
                    white.push(2+';')
                }
                if(!$("#1").is(":checked")){
                    white.push(3+';')
                }
                if(!$("#2").is(":checked")){
                    white.push(4+';')
                }
                if(!$("#3").is(":checked")){
                    white.push(5+';')
                }
                if(!$("#4").is(":checked")){
                    white.push(6+';')
                }
                if(!$("#5").is(":checked")){
                    white.push(7+';')
                }

                var  minwhite=$("#minwhite").val();

                //获取智能纠错字符
                var whitetd = [];
                var trList = $("#whieteTable").children("tr");
                for (var i=0;i<trList.length;i++) {
                    var tdArr = trList.eq(i).children("td");
                    for(var j=0;j<tdArr.length;j++){
                        // alert(tdArr[j].innerText);
                        if(tdArr[j].innerText=='----->'){
                            whitetd.push(",")
                        }else {
                            whitetd.push(tdArr[j].innerText);
                        }

                    }
                }
                var result = [];
                for(var i=0,len=whitetd.length;i<len;i+=3){
                    result.push(whitetd.slice(i,i+3));
                }

                //获取车牌智能纠错字符
                var allCarnoTd = [];
                var trListAllCarno = $("#whieteTableAllCarno").children("tr");
                for (var i=0;i<trListAllCarno.length;i++) {
                    var tdArrAllCarno = trListAllCarno.eq(i).children("td");
                    for(var j=0;j<tdArrAllCarno.length;j++){
                        if(tdArrAllCarno[j].innerText=='----->'){
                            allCarnoTd.push(",")
                        }else {
                            allCarnoTd.push(tdArrAllCarno[j].innerText);
                        }

                    }
                }
                var resultAllCarno = [];
                for(var i=0,len=allCarnoTd.length;i<len;i+=3){
                    resultAllCarno.push(allCarnoTd.slice(i,i+3));
                }


            }

            //模糊匹配信息保存
            //出口临时车
            var temp = [];
            if($("#tempInput").is(":checked")) {
                if(!$("#min_temp").is(":checked")){
                    temp.push(1+';')
                }
                if(!$("#a_temp").is(":checked")){
                    temp.push(2+';')
                }
                if(!$("#1_temp").is(":checked")){
                    temp.push(3+';')
                }
                if(!$("#2_temp").is(":checked")){
                    temp.push(4+';')
                }
                if(!$("#3_temp").is(":checked")){
                    temp.push(5+';')
                }
                if(!$("#4_temp").is(":checked")){
                    temp.push(6+';')
                }
                if(!$("#5_temp").is(":checked")){
                    temp.push(7+';')
                }

                var  mintemp=$("#mintemp").val();

                var whitetd = [];
                var trList = $("#whieteTable").children("tr");
                for (var i=0;i<trList.length;i++) {
                    var tdArr = trList.eq(i).children("td");
                    for(var j=0;j<tdArr.length;j++){
                        // alert(tdArr[j].innerText);
                        if(tdArr[j].innerText=='----->'){
                            whitetd.push(",")
                        }else {
                            whitetd.push(tdArr[j].innerText);
                        }

                    }
                }
                var result = [];
                for(var i=0,len=whitetd.length;i<len;i+=3){
                    result.push(whitetd.slice(i,i+3));
                }

            }

            //保存的信息
            this.params={
                carRoadName: $('#carRoadName').val(),
                carRoadId:carRoad.data.car_road_id,
                carRoadParkId:  $('#carRoadParkId').val(),
                carRoadType:$('#carRoadType').val(),
                carRoadPostId: $('#carRoadPostId').val(),
                result:result,
                resultAllCarno : resultAllCarno,
                minwhite:minwhite,
                white:white,
                temp:temp,
                mintemp:mintemp,
                isAutoMatch:isAutoMatch,
                isAutoMatchTemp:isAutoMatchTemp
            };

            var carloadType=$("#carRoadType").val();
            if(carloadType==0){
                if($("#whiteInput").is(":checked")) {
                    delete this.params.temp;
                    delete this.params.mintemp;

                }


            }



            if(minwhite>7){
                alert("白名单最小匹配位数不能大于7");
                return false;
            }

            if(mintemp>7){
                alert("临时车最小匹配位数不能大于7");
                return false;
            }

            ajaxReuqest(starnetContextPath + "/carpark/updateCarRoadInfo","post",this.params,function(res){
                if(res.result == 0){
                    successfulPrompt("车道修改成功");
                    carRoad.roadTable.ajax.reload(null,false);
                    $('#addCarRoadModal').modal("hide");
                }else{
                    errorPrompt("车场信息获取失败",res.msg);
                }
            });
            //移除点击事件
            $('#eidtCarRoad').unbind("click");
        })
    }

};

$(function () {
    carRoad.getAllPark();
    carRoad.getAllPost();
    carRoad.button();
    carRoad.carRoadTabel();

    $('#tmatchReurn').unbind("click").click(function () {//匹配 返回按钮
        $('#taddForm').show();
        $('#tmatchForm').hide();
        $("#whieteTable").empty();
    });

    $('#tmatchReurnAllCarno').unbind("click").click(function () {//匹配 返回按钮
        $('#taddFormAllCarno').show();
        $('#tmatchFormAllCarno').hide();
        $("#whieteTableAllCarno").empty();
    });
});

