var chargingRules;
$(function(){
    chargingRules = {
        editData:null,
        mainTable:null,
        formTemplate1EmptyData:{
            bigAddFee:"2",
            bigAddFeeEx:"2",
            bigFeeLimit:"5",
            bigFeeLimitEx:"10",
            bigFirstTime:"2",
            bigFirstTimeEx:"2",
            bigFreeDuration:"15",
            bigT112MaxFeeCheck:"on",
            bigT124MaxFeeCheck:"on",
            otherAddFee:"2",
            otherAddFeeEx:"2",
            otherFeeLimit:"5",
            otherFeeLimitEx:"10",
            otherFirstTime:"2",
            otherFirstTimeEx:"2",
            otherFreeDuration:"15",
            otherT112MaxFeeCheck:"on",
            otherT124MaxFeeCheck:"on",
            smallAddFee:"2",
            smallAddFeeEx:"2",
            smallFeeLimit:"5",
            smallFeeLimitEx:"10",
            smallFirstTime:"2",
            smallFirstTimeEx:"2",
            smallFreeDuration:"15",
            smallT112MaxFeeCheck:"on",
            smallT124MaxFeeCheck:"on"
        },
        formTemplate2EmptyData:{
            "smallFreeDuration":"15",
            "bigFreeDuration":"15",
            "otherFreeDuration":"15",
            "smallFirstTime":"2",
            "smallFirstTimeFee":"5",
            "bigFirstTime":"2",
            "bigFirstTimeFee":"5",
            "otherFirstTime":"2",
            "otherFirstTimeFee":"5",
            "smallSecondTimeAfterGapFee":"10",
            "bigSecondTimeAfterGapFee":"10",
            "otherSecondTimeAfterGapFee":"10",
            "smallIsFeeSection":"off",
            "bigIsFeeSection":"off",
            "otherIsFeeSection":"off",
            "smallAddFee":"20",
            "bigAddFee":"20",
            "otherAddFee":"20"
        },
        formTemplate3EmptyData:{
            "smallFreeDuration":"15",
            "bigFreeDuration":"15",
            "otherFreeDuration":"15",
            "smallFirstTime":"2",
            "smallFirstTimeFee":"5",
            "bigFirstTime":"2",
            "bigFirstTimeFee":"5",
            "otherFirstTime":"2",
            "otherFirstTimeFee":"5",
            "smallSecondTimeAfterGapFee":"10",
            "bigSecondTimeAfterGapFee":"10",
            "otherSecondTimeAfterGapFee":"10",
            "smallT3MaxFeeCheck":"on",
            "smallFeeLimit":"50",
            "bigT3MaxFeeCheck":"on",
            "bigFeeLimit":"50",
            "otherT3MaxFeeCheck":"on",
            "otherFeeLimit":"50",
            "smallT3AdditionalCheck":"on",
            "smallFeeStartTime":"01:00",
            "smallAddFee":"20",
            "bigT3AdditionalCheck":"on",
            "bigFeeStartTime":"01:00",
            "bigAddFee":"20",
            "otherT3AdditionalCheck":"on",
            "otherFeeStartTime":"01:00",
            "otherAddFee":"20"
        },
        formTemplate4EmptyData: {
            "smallFreeDuration":"15",
            "smallFreeDurationEx":"15",
            "bigFreeDuration":"15",
            "bigFreeDurationEx":"15",
            "otherFreeDuration":"15",
            "otherFreeDurationEx":"15",
            "smallFirstTime":"2",
            "smallFirstTimeFee":"5",
            "smallFirstTimeEx":"2",
            "smallFirstTimeFeeEx":"5",
            "bigFirstTime":"2",
            "bigFirstTimeFee":"5",
            "bigFirstTimeEx":"2",
            "bigFirstTimeFeeEx":"5",
            "otherFirstTime":"2",
            "otherFirstTimeFee":"5",
            "otherFirstTimeEx":"2",
            "otherFirstTimeFeeEx":"5",
            "smallSecondTimeAfterGapFee":"10",
            "smallSecondTimeAfterGapFeeEx":"10",
            "bigSecondTimeAfterGapFee":"10",
            "bigSecondTimeAfterGapFeeEx":"10",
            "otherSecondTimeAfterGapFee":"10",
            "otherSecondTimeAfterGapFeeEx":"10",
            "smallT4MaxFeeCheckOne":"on",
            "smallFeeLimit":"50",
            "bigT4MaxFeeCheckOne":"on",
            "bigFeeLimit":"50",
            "otherT4MaxFeeCheckOne":"on",
            "otherFeeLimit":"50",
            "smallT4MaxFeeCheckTwo":"on",
            "smallFeeLimitEx":"50",
            "bigT4MaxFeeCheckTwo":"on",
            "bigFeeLimitEx":"50",
            "otherT4MaxFeeCheckTwo":"on",
            "otherFeeLimitEx":"50",
            "feeStartTime":"07:00",
            "feeEndTime":"19:00",
            "isFeeSection":"0",
            "smallTotalFeeLimit":"0",
            "bigTotalFeeLimit":"0",
            "otherTotalFeeLimit":"0",
            "smallFeeStartTime":"07:00:00",
            "bigFeeStartTime":"07:00:00",
            "otherFeeStartTime":"07:00:00",
            "smallFeeEndTime":"19:00:00",
            "bigFeeEndTime":"19:00:00",
            "otherFeeEndTime":"19:00:00",
            "smallIsFeeSection":"0",
            "bigIsFeeSection":"0",
            "otherIsFeeSection":"0",
            "bigTotalFeeLimit":"0",
            "T4DayBigMaxFeeCheck":"off",
            "otherTotalFeeLimit":"0",
            "T4DayOtherMaxFeeCheck":"off",
            "smallTotalFeeLimit":"0",
            "T4DaySmallMaxFeeCheck":"off"
        },
        formTemplate5EmptyData:{
            "smallFreeDuration":"15",
            "smallFreeDurationEx":"15",
            "bigFreeDuration":"15",
            "bigFreeDurationEx":"15",
            "otherFreeDuration":"15",
            "otherFreeDurationEx":"15",
            "smallFirstTimeFee":"5",
            "smallFirstTimeFeeEx":"5",
            "bigFirstTimeFee":"5",
            "bigFirstTimeFeeEx":"5",
            "otherFirstTimeFee":"5",
            "otherFirstTimeFeeEx":"5",
            "smallT5MaxFeeCheck":"off",
            "smallTotalFeeLimit":"5",
            "bigT5MaxFeeCheck":"off",
            "bigTotalFeeLimit":"5",
            "otherT5MaxFeeCheck":"off",
            "otherTotalFeeLimit":"5",
            "feeStartTime":"01:00",
            "feeEndTime":"19:00",
            "isFeeSection":"off",
            "smallFeeStartTime":"01:00:00",
            "bigFeeStartTime":"01:00:00",
            "otherFeeStartTime":"01:00:00",
            "smallFeeEndTime":"19:00:00",
            "bigFeeEndTime":"19:00:00",
            "otherFeeEndTime":"19:00:00",
            "smallIsFeeSection":"0",
            "bigIsFeeSection":"0",
            "otherIsFeeSection":"0"
        },
        formTemplate6EmptyData:{
            "smallFreeDuration":"15",
            "smallFreeDurationEx":"15",
            "bigFreeDuration":"15",
            "bigFreeDurationEx":"15",
            "otherFreeDuration":"15",
            "otherFreeDurationEx":"15",
            "smallFirstTimeFee":"5",
            "smallSecondTimeAfterGapFee":"10",
            "smallAddFee":"20",
            "smallFirstTimeFeeEx":"5",
            "smallSecondTimeAfterGapFeeEx":"10",
            "smallAddFeeEx":"20",
            "bigFirstTimeFee":"5",
            "bigSecondTimeAfterGapFee":"10",
            "bigAddFee":"20",
            "bigFirstTimeFeeEx":"5",
            "bigSecondTimeAfterGapFeeEx":"10",
            "bigAddFeeEx":"20",
            "otherFirstTimeFee":"5",
            "otherSecondTimeAfterGapFee":"10",
            "otherAddFee":"20",
            "otherFirstTimeFeeEx":"5",
            "otherSecondTimeAfterGapFeeEx":"10",
            "otherAddFeeEx":"20",
            "smallT6MaxFeeCheck":"off",
            "smallTotalFeeLimit":"5",
            "bigT6MaxFeeCheck":"off",
            "bigTotalFeeLimit":"5",
            "otherT6MaxFeeCheck":"off",
            "otherTotalFeeLimit":"5",
            "feeStartTime":"01:00",
            "feeEndTime":"19:00",
            "smallFeeStartTime":"01:00:00",
            "bigFeeStartTime":"01:00:00",
            "otherFeeStartTime":"01:00:00",
            "smallFeeEndTime":"19:00:00",
            "bigFeeEndTime":"19:00:00",
            "otherFeeEndTime":"19:00:00"
        },
        editFunction:function(rowIndex){
            this.editData = this.mainTable.row(rowIndex).data();
            var params = {
                "memberKindId":this.editData.id
            };
            if (this.editData.isDelete == 1){
                $("#packageKind").empty();
                $("#packageKind").append("<option value='2'>临时计费</option>");
                $("#packageKind").append("<option value='-3'>免费</option>");
            }else {
                $("#packageKind").empty();
                $("#packageKind").append("<option value='0'>包月收费</option>");
                $("#packageKind").append("<option value='1'>包次收费</option>");
                $("#packageKind").append("<option value='2'>临时计费</option>");
                $("#packageKind").append("<option value='-3'>免费</option>");
            }
            var that = this;
            $('#smallT3SpanTime').timepicker('setTime', "01:00");
            $('#bigT3SpanTime').timepicker('setTime', "01:00");
            $('#otherT3SpanTime').timepicker('setTime', "01:00");
            $('#T4DayTimeStart').timepicker('setTime', "07:00");
            $('#T4DayTimeEnd').timepicker('setTime', "19:00");
            $('#T5DayTimeStart').timepicker('setTime', "01:00");
            $('#T5DayTimeEnd').timepicker('setTime', "19:00");
            $('#T6DayTimeStart').timepicker('setTime', "01:00");
            $('#T6DayTimeEnd').timepicker('setTime', "19:00");
            var i = 1;
            while(i < 7){
                $("#formTemplate" + i).populateForm(that["formTemplate" + i + "EmptyData"]);
                if(i == 1){
                    $('#smallT2duration1').trigger("change");
                    $('#bigT2duration1').trigger("change");
                    $('#otherT2duration1').trigger("change");
                }else if(i == 2){
                    $('#smallT2duration1').trigger("change");
                    $('#bigT2duration1').trigger("change");
                    $('#otherT2duration1').trigger("change");
                }else if(i == 3){
                    $('#smallT3duration1Time1').trigger("change");
                    $('#smallT3duration1Time2').trigger("change");
                    $('#bigT3duration1Time1').trigger("change");
                    $('#bigT3duration1Time2').trigger("change");
                    $('#otherT3duration1Time1').trigger("change");
                    $('#otherT3duration1Time2').trigger("change");
                    $('#T3DayTimeStart').trigger("change");
                    $('#T3DayTimeEnd').trigger("change");
                }else if(i == 4){
                    $('#T4DayTimeStart').trigger("change");
                    $('#T4DayTimeEnd').trigger("change");
                }else if(i == 5){
                    $('#T5DayTimeStart').trigger("change");
                    $('#T5DayTimeEnd').trigger("change");
                }else if(i == 6){
                    $('#T6DayTimeStart').trigger("change");
                    $('#T6DayTimeEnd').trigger("change");
                }
                i++;
            }
            $("#kindName").val(this.editData["kindName"]);
            $("#memo").val(this.editData["memo"]);
            $("#monthPackageValue").val(this.editData["monthPackage"]);
            $("#countPackageValue").val(this.editData["countPackage"]);
            $("#packageKind").val(this.editData["packageKind"]);
            $("#ruleTemplateSelect").val(this.editData["packageChildKind"]);
            $("#isStatistic").val(this.editData["isStatistic"]);
            $("#packageKind").trigger("change");

            $.post(starnetContextPath + '/member/kind/get',params,function (res) {
                if(res.result == 0){

                    if(that.editData["packageKind"] == 2){
                        var index = parseInt(that.editData["packageChildKind"]) + 1;
                        var feeSetDetail = res["data"]["feeSetDetail"];
                        feeSetDetail["isFeeSection"] = feeSetDetail["smallIsFeeSection"];
                        if (index == 4){
                            feeSetDetail["bigFeeLimit"] = feeSetDetail["smallFeeLimit"];
                            feeSetDetail["bigFeeLimitEx"] = feeSetDetail["smallFeeLimitEx"];
                            feeSetDetail["otherFeeLimit"] = feeSetDetail["smallFeeLimit"];
                            feeSetDetail["otherFeeLimitEx"] = feeSetDetail["smallFeeLimitEx"];
                        }
                        $("#formTemplate" + index).populateForm(feeSetDetail);

                        if(index == 1){
                            if(feeSetDetail["smallFeeLimit"] == 0){
                                document.getElementById("smallT112MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("smallT112MaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["bigFeeLimit"] == 0){
                                document.getElementById("bigT112MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("bigT112MaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["otherFeeLimit"] == 0){
                                document.getElementById("otherT112MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("otherT112MaxFeeCheck").checked = true;
                            }

                            if(feeSetDetail["smallFeeLimitEx"] == 0){
                                document.getElementById("smallT124MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("smallT124MaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["bigFeeLimitEx"] == 0){
                                document.getElementById("bigT124MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("bigT124MaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["otherFeeLimitEx"] == 0){
                                document.getElementById("otherT124MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("otherT124MaxFeeCheck").checked = true;
                            }
                        }else if(index == 2){
                            if(feeSetDetail["smallIsFeeSection"] == 0){
                                document.getElementById("T2smallIsFeeSection").checked = false;
                            }else{
                                document.getElementById("T2smallIsFeeSection").checked = true;
                            }
                            if(feeSetDetail["bigIsFeeSection"] == 0){
                                document.getElementById("T2bigIsFeeSection").checked = false;
                            }else{
                                document.getElementById("T2bigIsFeeSection").checked = true;
                            }
                            if(feeSetDetail["otherIsFeeSection"] == 0){
                                document.getElementById("T2otherIsFeeSection").checked = false;
                            }else{
                                document.getElementById("T2otherIsFeeSection").checked = true;
                            }
                            $("#smallT2duration1_show").html(feeSetDetail["smallFirstTime"]);
                            $("#bigT2duration1_show").html(feeSetDetail["bigFirstTime"]);
                            $("#otherT2duration1_show").html(feeSetDetail["otherFirstTime"]);
                        }else if(index == 3){

                            if(feeSetDetail["smallAddFee"] == 0){
                                document.getElementById("smallT3AdditionalCheck").checked = false;
                            }else{
                                document.getElementById("smallT3AdditionalCheck").checked = true;
                            }
                            if(feeSetDetail["bigAddFee"] == 0){
                                document.getElementById("bigT3AdditionalCheck").checked = false;
                            }else{
                                document.getElementById("bigT3AdditionalCheck").checked = true;
                            }
                            if(feeSetDetail["otherAddFee"] == 0){
                                document.getElementById("otherT3AdditionalCheck").checked = false;
                            }else{
                                document.getElementById("otherT3AdditionalCheck").checked = true;
                            }

                            if(feeSetDetail["smallFeeLimit"] == 0){
                                document.getElementById("smallT3MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("smallT3MaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["bigFeeLimit"] == 0){
                                document.getElementById("bigT3MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("bigT3MaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["otherFeeLimit"] == 0){
                                document.getElementById("otherT3MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("otherT3MaxFeeCheck").checked = true;
                            }

                            $('#smallT3SpanTime').timepicker('setTime',feeSetDetail["smallFeeStartTime"].substring(0,5));
                            $('#bigT3SpanTime').timepicker('setTime', feeSetDetail["bigFeeStartTime"].substring(0,5));
                            $('#otherT3SpanTime').timepicker('setTime', feeSetDetail["otherFeeStartTime"].substring(0,5));

                            $("#smallT3duration1_show").html(feeSetDetail["smallFirstTime"]);
                            $("#bigT3duration1_show").html(feeSetDetail["bigFirstTime"]);
                            $("#otherT3duration1_show").html(feeSetDetail["otherFirstTime"]);
                        }else if(index == 4){
                            if(feeSetDetail["smallFeeLimit"] == 0){
                                document.getElementById("smallT4MaxFeeCheckOne").checked = false;
                            }else{
                                document.getElementById("smallT4MaxFeeCheckOne").checked = true;
                            }
                            if(feeSetDetail["smallFeeLimit"] == 0){
                                document.getElementById("bigT4MaxFeeCheckOne").checked = false;
                            }else{
                                document.getElementById("bigT4MaxFeeCheckOne").checked = true;
                            }
                            if(feeSetDetail["smallFeeLimit"] == 0){
                                document.getElementById("otherT4MaxFeeCheckOne").checked = false;
                            }else{
                                document.getElementById("otherT4MaxFeeCheckOne").checked = true;
                            }
                            if(feeSetDetail["smallFeeLimitEx"] == 0){
                                document.getElementById("smallT4MaxFeeCheckTwo").checked = false;
                            }else{
                                document.getElementById("smallT4MaxFeeCheckTwo").checked = true;
                            }
                            if(feeSetDetail["smallFeeLimitEx"] == 0){
                                document.getElementById("bigT4MaxFeeCheckTwo").checked = false;
                            }else{
                                document.getElementById("bigT4MaxFeeCheckTwo").checked = true;
                            }
                            if(feeSetDetail["smallFeeLimitEx"] == 0){
                                document.getElementById("otherT4MaxFeeCheckTwo").checked = false;
                            }else{
                                document.getElementById("otherT4MaxFeeCheckTwo").checked = true;
                            }

                            if(feeSetDetail["smallTotalFeeLimit"] == 0){
                                document.getElementById("T4DaySmallMaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("T4DaySmallMaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["bigTotalFeeLimit"] == 0){
                                document.getElementById("T4DayBigMaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("T4DayBigMaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["otherTotalFeeLimit"] == 0){
                                document.getElementById("T4DayOtherMaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("T4DayOtherMaxFeeCheck").checked = true;
                            }

                            $("#smallT4duration1Time1_show").html($("#smallT4duration1Time1").val());
                            $("#smallT4duration1Time2_show").html($("#smallT4duration1Time2").val());
                            $("#bigT4duration1Time1_show").html($("#bigT4duration1Time1").val());
                            $("#bigT4duration1Time2_show").html($("#bigT4duration1Time2").val());
                            $("#otherT4duration1Time1_show").html($("#otherT4duration1Time1").val());
                            $("#otherT4duration1Time2_show").html($("#otherT4duration1Time2").val());

                            var feeStartTime = feeSetDetail["smallFeeStartTime"].substring(0,5);
                            var feeEndTime = feeSetDetail["smallFeeEndTime"].substring(0,5);

                            $('#T4DayTimeStart').timepicker('setTime', feeStartTime);
                            $('#T4DayTimeEnd').timepicker('setTime', feeEndTime);

                        }else if(index == 5){

                            if(feeSetDetail["smallTotalFeeLimit"] == 0){
                                document.getElementById("smallT5MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("smallT5MaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["bigTotalFeeLimit"] == 0){
                                document.getElementById("bigT5MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("bigT5MaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["otherTotalFeeLimit"] == 0){
                                document.getElementById("otherT5MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("otherT5MaxFeeCheck").checked = true;
                            }

                            if(feeSetDetail["isFeeSection"] == 0){
                                var topfeeLable1 = document.getElementById("topFee5");
                                topfeeLable1.innerHTML = "一天收费封顶";
                                document.getElementById("T5isFeeSection").checked = false;
                            }else{
                                var topfeeLable2 = document.getElementById("topFee5");
                                topfeeLable2.innerHTML = "一次收费封顶";
                                document.getElementById("T5isFeeSection").checked = true;
                            }

                            var feeStartTime = feeSetDetail["smallFeeStartTime"].substring(0,5);
                            var feeEndTime = feeSetDetail["smallFeeEndTime"].substring(0,5);
                            $('#T5DayTimeStart').timepicker('setTime', feeStartTime);
                            $('#T5DayTimeEnd').timepicker('setTime', feeEndTime);
                        }else if(index == 6){

                            if(feeSetDetail["smallTotalFeeLimit"] == 0){
                                document.getElementById("smallT6MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("smallT6MaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["bigTotalFeeLimit"] == 0){
                                document.getElementById("bigT6MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("bigT6MaxFeeCheck").checked = true;
                            }
                            if(feeSetDetail["otherTotalFeeLimit"] == 0){
                                document.getElementById("otherT6MaxFeeCheck").checked = false;
                            }else{
                                document.getElementById("otherT6MaxFeeCheck").checked = true;
                            }

                            var feeStartTime = feeSetDetail["smallFeeStartTime"].substring(0,5);
                            var feeEndTime = feeSetDetail["smallFeeEndTime"].substring(0,5);
                            $('#T6DayTimeStart').timepicker('setTime', feeStartTime);
                            $('#T6DayTimeEnd').timepicker('setTime', feeEndTime);
                        }
                    }

                }else{
                    errorPrompt("操作失败",res.msg);
                }
            })

            $("#editModal").modal("show");
        },
        render:function () {
            this.iniDataTable();
            this.onPackageKindchange();
            this.onRuleTemplateChange();
            this.onSmallT2duration1Change();
            this.onBigT2duration1Change();
            this.onOtherT2duration1Change();
            this.onSmallT3duration1Change();
            this.onBigT3duration1Change();
            this.onOtherT3duration1Change();

            this.onSmallT4duration1Time1Change();
            this.onSmallT4duration1Time2Change();
            this.onBigT4duration1Time1Change();
            this.onBigT4duration1Time2Change();
            this.onOtherT4duration1Time1Change();
            this.onOtherT4duration1Time2Change();
            this.onT4DayTimeStartChange();
            this.onT4DayTimeEndChange();

            this.onT5DayTimeStartChange();
            this.onT5DayTimeEndChange();
            this.onT6DayTimeStartChange();
            this.onT6DayTimeEndChange();
            this.bindaddRuleClick();
            this.bindSubmitBtnClick();

            $(".timepicker").timepicker({
                minuteStep: 5,
                showInputs: false,
                disableFocus: true,
                showMeridian:false
            });
            $('#smallT3SpanTime').timepicker('setTime', "01:00");
            $('#bigT3SpanTime').timepicker('setTime', "01:00");
            $('#otherT3SpanTime').timepicker('setTime', "01:00");
            $('#T4DayTimeStart').timepicker('setTime', "07:00");
            $('#T4DayTimeEnd').timepicker('setTime', "19:00");
            $('#T5DayTimeStart').timepicker('setTime', "01:00");
            $('#T5DayTimeEnd').timepicker('setTime', "19:00");
            $('#T6DayTimeStart').timepicker('setTime', "01:00");
            $('#T6DayTimeEnd').timepicker('setTime', "19:00");

            this.onT4DaySmallMaxFeeCheckChange();
            this.onT4DayBigMaxFeeCheckChange();
            this.onT4DayOtherMaxFeeCheckChange();

            this.onSmallT4MaxFeeCheck1Change();
            this.onBigT4MaxFeeCheck1Change();
            this.onOtherT4MaxFeeCheck1Change();

            this.onSmallT4MaxFeeCheck2Change();
            this.onBigT4MaxFeeCheck2Change();
            this.onOtherT4MaxFeeCheck2Change();
        },
        iniDataTable:function() {

            this.mainTable = $('#chargingRules').DataTable({
                sort:false,
                processing:true,
                searching:false,
                autoWidth:true,
                lengthChange:false,
                serverSide:true,
                paging:true,
                ajax: {
                    url: starnetContextPath + "/charge/getChargeRules",
                    type: 'POST'
                },
                columns:[
                    {"data":"kindName"},
                    {"data":"packageKind"},
                    {"data":"parkName"},
                    {"data":"id"},
                    {"data":"isDelete"},
                    {"data":"countPackage"},
                    {"data":"monthPackage"},
                    {"data":"isStatistic"},
                    {"data":"memo"},
                    {"data":"packageChildKind"}
                ],
                columnDefs: [
                    {
                        "render": function(data, type, row, meta) {
                           if(data == 0){
                                return "包月车";
                           }else if(data == 1) {
                               return "按次车";
                           }else if(data == 2){
                               return "收费车";
                           }else{
                               return "免费车";
                           }
                        },
                        "targets":1
                    },
                    {
                        "render": function(data, type, row, meta) {
                            if(row.isDelete == 0){
                                return '<button class="btn btn-info btn-sm" onclick="chargingRules.editFunction('+meta.row+')">编辑配置</button>' +
                                    '<button class="btn btn-danger btn-sm" onclick="chargingRules.delFunction('+meta.row+')">删除</button>';
                            }else{
                                return '<button class="btn btn-info btn-sm" onclick="chargingRules.editFunction('+meta.row+')">编辑配置</button>';
                            }

                        },
                        "targets":3
                    },
                    {
                        "visible": false,
                        "targets": [4,5,6,7,8,9]
                    }

                ],

                language:dataTableLanguage
            } );
        },
        bindaddRuleClick:function () {
            var that = this;
            $('#addRule').unbind("click").bind("click", function () {
                that.editData = null;
                $("#packageKind").empty();
                $("#packageKind").append("<option value='0'>包月收费</option>");
                $("#packageKind").append("<option value='1'>包次收费</option>");
                $("#packageKind").append("<option value='2'>临时计费</option>");
                $("#packageKind").append("<option value='-3'>免费</option>");
                $('#smallT3SpanTime').timepicker('setTime', "01:00");
                $('#bigT3SpanTime').timepicker('setTime', "01:00");
                $('#otherT3SpanTime').timepicker('setTime', "01:00");
                $('#T4DayTimeStart').timepicker('setTime', "07:00");
                $('#T4DayTimeEnd').timepicker('setTime', "19:00");
                $('#T5DayTimeStart').timepicker('setTime', "01:00");
                $('#T5DayTimeEnd').timepicker('setTime', "19:00");
                $('#T6DayTimeStart').timepicker('setTime', "01:00");
                $('#T6DayTimeEnd').timepicker('setTime', "19:00");
                var i = 1;
                while(i < 7){
                    $("#formTemplate" + i).populateForm(that["formTemplate" + i + "EmptyData"]);
                    if(i == 1){
                        $('#smallT2duration1').trigger("change");
                        $('#bigT2duration1').trigger("change");
                        $('#otherT2duration1').trigger("change");
                    }else if(i == 2){
                        $('#smallT2duration1').trigger("change");
                        $('#bigT2duration1').trigger("change");
                        $('#otherT2duration1').trigger("change");
                    }else if(i == 3){
                        $('#smallT3duration1Time1').trigger("change");
                        $('#smallT3duration1Time2').trigger("change");
                        $('#bigT3duration1Time1').trigger("change");
                        $('#bigT3duration1Time2').trigger("change");
                        $('#otherT3duration1Time1').trigger("change");
                        $('#otherT3duration1Time2').trigger("change");
                        $('#T3DayTimeStart').trigger("change");
                        $('#T3DayTimeEnd').trigger("change");
                    }else if(i == 4){
                        $('#T4DayTimeStart').trigger("change");
                        $('#T4DayTimeEnd').trigger("change");
                    }else if(i == 5){
                        $('#T5DayTimeStart').trigger("change");
                        $('#T5DayTimeEnd').trigger("change");
                    }else if(i == 6){
                        $('#T6DayTimeStart').trigger("change");
                        $('#T6DayTimeEnd').trigger("change");
                    }
                    i++;
                }
                $("#kindName").val("");
                $("#memo").val("");
                $("#monthPackageValue").val("");
                $("#countPackageValue").val("");
                $("#packageKind").val("0");
                $("#ruleTemplateSelect").val("0");
                $("#isStatistic").val("1");
                $("#packageKind").trigger("change");
                $("#editModal").modal("show");
            });
        },
        onPackageKindchange:function () {
            $("#packageKind").change(function(){
                var value = $(this).val();
                if(value == "0"){
                    $("#monthPackage").show();
                    $("#countPackage").hide();
                    $("#ruleTemplate").hide();
                    $("#ruleContainer").hide();
                }else if(value == "1"){
                    $("#monthPackage").hide();
                    $("#countPackage").show();
                    $("#ruleTemplate").hide();
                    $("#ruleContainer").hide();
                }else if(value == "2"){
                    $("#monthPackage").hide();
                    $("#countPackage").hide();
                    $("#ruleTemplate").show();
                    $("#ruleContainer").show();
                }else{
                    $("#monthPackage").hide();
                    $("#countPackage").hide();
                    $("#ruleTemplate").hide();
                    $("#ruleContainer").hide();
                }
                $("#ruleTemplateSelect").trigger("change");
            });
        },
        onRuleTemplateChange:function () {
            $("#ruleTemplateSelect").change(function(){
                var value = parseInt($(this).val()) + 1;

                $(".colHidden").hide();
                $(".template" + value).show();
            });
        },
        onSmallT2duration1Change:function(){
            $("#smallT2duration1").keyup(function(){
                var value = $(this).val();
                $("#smallT2duration1_show").html(value);
            });
            $("#smallT2duration1").click(function(){
                var value = $(this).val();
                $("#smallT2duration1_show").html(value);
            });
            $("#smallT2duration1").change(function(){
                var value = $(this).val();
                $("#smallT2duration1_show").html(value);
            });
        },
        onBigT2duration1Change:function(){
            $("#bigT2duration1").keyup(function(){
                var value = $(this).val();
                $("#bigT2duration1_show").html(value);
            });
            $("#bigT2duration1").click(function(){
                var value = $(this).val();
                $("#bigT2duration1_show").html(value);
            });
            $("#bigT2duration1").change(function(){
                var value = $(this).val();
                $("#bigT2duration1_show").html(value);
            });
        },
        onOtherT2duration1Change:function(){
            $("#otherT2duration1").keyup(function(){
                var value = $(this).val();
                $("#otherT2duration1_show").html(value);
            });
            $("#otherT2duration1").click(function(){
                var value = $(this).val();
                $("#otherT2duration1_show").html(value);
            });
            $("#otherT2duration1").change(function(){
                var value = $(this).val();
                $("#otherT2duration1_show").html(value);
            });
        },
        onSmallT3duration1Change:function(){
            $("#smallT3duration1").keyup(function(){
                var value = $(this).val();
                $("#smallT3duration1_show").html(value);
            });
            $("#smallT3duration1").click(function(){
                var value = $(this).val();
                $("#smallT3duration1_show").html(value);
            });
            $("#smallT3duration1").change(function(){
                var value = $(this).val();
                $("#smallT3duration1_show").html(value);
            });
        },
        onBigT3duration1Change:function(){
            $("#bigT3duration1").keyup(function(){
                var value = $(this).val();
                $("#bigT3duration1_show").html(value);
            });
            $("#bigT3duration1").click(function(){
                var value = $(this).val();
                $("#bigT3duration1_show").html(value);
            });
            $("#bigT3duration1").change(function(){
                var value = $(this).val();
                $("#bigT3duration1_show").html(value);
            });
        },
        onOtherT3duration1Change:function(){
            $("#otherT3duration1").keyup(function(){
                var value = $(this).val();
                $("#otherT3duration1_show").html(value);
            });
            $("#otherT3duration1").click(function(){
                var value = $(this).val();
                $("#otherT3duration1_show").html(value);
            });
            $("#otherT3duration1").change(function(){
                var value = $(this).val();
                $("#otherT3duration1_show").html(value);
            });
        },
        onSmallT4duration1Time1Change:function(){
            $("#smallT4duration1Time1").keyup(function(){
                var value = $(this).val();
                $("#smallT4duration1Time1_show").html(value);
            });
            $("#smallT4duration1Time1").click(function(){
                var value = $(this).val();
                $("#smallT4duration1Time1_show").html(value);
            });
            $("#smallT4duration1Time1").change(function(){
                var value = $(this).val();
                $("#smallT4duration1Time1_show").html(value);
            });
        },
        onSmallT4duration1Time2Change:function(){
            $("#smallT4duration1Time2").keyup(function(){
                var value = $(this).val();
                $("#smallT4duration1Time2_show").html(value);
            });
            $("#smallT4duration1Time2").click(function(){
                var value = $(this).val();
                $("#smallT4duration1Time2_show").html(value);
            });
            $("#smallT4duration1Time2").change(function(){
                var value = $(this).val();
                $("#smallT4duration1Time2_show").html(value);
            });
        },
        onBigT4duration1Time1Change:function(){
            $("#bigT4duration1Time1").keyup(function(){
                var value = $(this).val();
                $("#bigT4duration1Time1_show").html(value);
            });
            $("#bigT4duration1Time1").click(function(){
                var value = $(this).val();
                $("#bigT4duration1Time1_show").html(value);
            });
            $("#bigT4duration1Time1").change(function(){
                var value = $(this).val();
                $("#bigT4duration1Time1_show").html(value);
            });
        },
        onBigT4duration1Time2Change:function(){
            $("#bigT4duration1Time2").keyup(function(){
                var value = $(this).val();
                $("#bigT4duration1Time2_show").html(value);
            });
            $("#bigT4duration1Time2").click(function(){
                var value = $(this).val();
                $("#bigT4duration1Time2_show").html(value);
            });
            $("#bigT4duration1Time2").change(function(){
                var value = $(this).val();
                $("#bigT4duration1Time2_show").html(value);
            });
        },
        onOtherT4duration1Time1Change:function(){
            $("#otherT4duration1Time1").keyup(function(){
                var value = $(this).val();
                $("#otherT4duration1Time1_show").html(value);
            });
            $("#otherT4duration1Time1").click(function(){
                var value = $(this).val();
                $("#otherT4duration1Time1_show").html(value);
            });
            $("#otherT4duration1Time1").change(function(){
                var value = $(this).val();
                $("#otherT4duration1Time1_show").html(value);
            });
        },
        onOtherT4duration1Time2Change:function(){
            $("#otherT4duration1Time2").keyup(function(){
                var value = $(this).val();
                $("#otherT4duration1Time2_show").html(value);
            });
            $("#otherT4duration1Time2").click(function(){
                var value = $(this).val();
                $("#otherT4duration1Time2_show").html(value);
            });
            $("#otherT4duration1Time2").change(function(){
                var value = $(this).val();
                $("#otherT4duration1Time2_show").html(value);
            });
        },
        onT4DayTimeStartChange:function () {
            $("#T4DayTimeStart").change(function () {
                var value = $(this).val();
                $(".T4dayTimeStart").html(value);
                $("#T4nightEnd").html(value);
            });
        },
        onT4DayTimeEndChange:function () {
            $("#T4DayTimeEnd").change(function () {

                var value = $(this).val();
                $(".T4NightStart").html(value);
                $("#T4nightStart").html(value);
            });
        },
        onT5DayTimeStartChange:function () {
            $("#T5DayTimeStart").change(function () {

                var value = $(this).val();
                $(".T5dayTimeStart").html(value);
                $("#T5nightEnd").html(value);
            });
        },
        onT5DayTimeEndChange:function () {
            $("#T5DayTimeEnd").change(function () {

                var value = $(this).val();
                $(".T5NightStart").html(value);
                $("#T5nightStart").html(value);
            });
        },
        onT6DayTimeStartChange:function () {
            $("#T6DayTimeStart").change(function () {

                var value = $(this).val();
                $(".T6dayTimeStart").html(value)
            });
        },
        onT6DayTimeEndChange:function () {
            $("#T6DayTimeEnd").change(function () {

                var value = $(this).val();
                $(".T6NightStart").html(value)
            });
        },
        bindSubmitBtnClick:function(){
            var that = this;
            $("#submitBtn").click(function(){
                if(that.editData != null && that.editData["isDelete"] == 1 ){
                    if($("#packageKind").val() == "0" || $("#packageKind").val() == "1"){
                        errorPrompt("错误信息","默认计费规则只能为免费或临时计费");
                        return;
                    }
                }
                var memberKindInfo = $("#memberForm").serializeObject();
                if(memberKindInfo["kindName"] == ""){
                    errorPrompt("错误信息","规则名称不能为空");
                    return;
                }
                /*if(memberKindInfo["memo"] == ""){
                    errorPrompt("错误信息","车辆属性不能为空");
                    return;
                }*/
                if($("#packageKind").val() == "2"){
                    if(!$("#ruleTemplateSelect").val()){
                        errorPrompt("错误信息","请选择计费模板");
                        return;
                    }
                    var templateIndex = parseInt($("#ruleTemplateSelect").val()) + 1;
                    var feeSetDetail = $("#formTemplate" + templateIndex).serializeObject();
                    memberKindInfo["packageKind"] = $("#packageKind").val();
                    memberKindInfo["packageChildKind"] = $("#ruleTemplateSelect").val();

                    if(templateIndex == 1){
                        if(feeSetDetail["smallT112MaxFeeCheck"] != "on"){
                            feeSetDetail["smallFeeLimit"] = "0";
                        }
                        if(feeSetDetail["smallT124MaxFeeCheck"] != "on"){
                            feeSetDetail["smallFeeLimitEx"] = "0";
                        }

                        if(feeSetDetail["bigT112MaxFeeCheck"] != "on"){
                            feeSetDetail["bigFeeLimit"] = "0";
                        }
                        if(feeSetDetail["bigT124MaxFeeCheck"] != "on"){
                            feeSetDetail["bigFeeLimitEx"] = "0";
                        }

                        if(feeSetDetail["otherT112MaxFeeCheck"] != "on"){
                            feeSetDetail["otherFeeLimit"] = "0";
                        }
                        if(feeSetDetail["otherT124MaxFeeCheck"] != "on"){
                            feeSetDetail["otherFeeLimitEx"] = "0";
                        }
                    }else if(templateIndex == 2){
                        if(feeSetDetail["smallIsFeeSection"] != "on"){
                            feeSetDetail["smallIsFeeSection"] = "0";
                        }else{
                            feeSetDetail["smallIsFeeSection"] = "1";
                        }
                        if(feeSetDetail["bigIsFeeSection"] != "on"){
                            feeSetDetail["bigIsFeeSection"] = "0";
                        }else{
                            feeSetDetail["bigIsFeeSection"] = "1";
                        }
                        if(feeSetDetail["otherIsFeeSection"] != "on"){
                            feeSetDetail["otherIsFeeSection"] = "0";
                        }else{
                            feeSetDetail["otherIsFeeSection"] = "1";
                        }

                    }else if(templateIndex == 3){
                        if(feeSetDetail["smallT3MaxFeeCheck"] != "on"){
                            feeSetDetail["smallFeeLimit"] = "0";
                        }
                        if(feeSetDetail["bigT3MaxFeeCheck"] != "on"){
                            feeSetDetail["bigFeeLimit"] = "0";
                        }
                        if(feeSetDetail["otherT3MaxFeeCheck"] != "on"){
                            feeSetDetail["otherFeeLimit"] = "0";
                        }
                        if(feeSetDetail["smallT3AdditionalCheck"] != "on"){
                            feeSetDetail["smallAddFee"] = "0";
                        }
                        if(feeSetDetail["bigT3AdditionalCheck"] != "on"){
                            feeSetDetail["bigAddFee"] = "0";
                        }
                        if(feeSetDetail["otherT3AdditionalCheck"] != "on"){
                            feeSetDetail["otherAddFee"] = "0";
                        }
                        feeSetDetail["smallFeeStartTime"] = feeSetDetail["smallFeeStartTime"] + ":00";
                        feeSetDetail["bigFeeStartTime"] = feeSetDetail["bigFeeStartTime"] + ":00";
                        feeSetDetail["otherFeeStartTime"] = feeSetDetail["otherFeeStartTime"] + ":00";

                    }else if(templateIndex == 4){
                        feeSetDetail["smallFeeStartTime"] = feeSetDetail["feeStartTime"] + ":00";
                        feeSetDetail["bigFeeStartTime"] = feeSetDetail["feeStartTime"] + ":00";
                        feeSetDetail["otherFeeStartTime"] = feeSetDetail["feeStartTime"] + ":00";

                        feeSetDetail["smallFeeEndTime"] = feeSetDetail["feeEndTime"] + ":00";
                        feeSetDetail["bigFeeEndTime"] = feeSetDetail["feeEndTime"] + ":00";
                        feeSetDetail["otherFeeEndTime"] = feeSetDetail["feeEndTime"] + ":00";

                        feeSetDetail["smallIsFeeSection"] = feeSetDetail["isFeeSection"];
                        feeSetDetail["bigIsFeeSection"] = feeSetDetail["isFeeSection"];
                        feeSetDetail["otherIsFeeSection"] = feeSetDetail["isFeeSection"];

                        if(feeSetDetail["smallT4MaxFeeCheckOne"]!= "on"){
                            feeSetDetail["smallFeeLimit"] = "0";
                        }
                        feeSetDetail["bigFeeLimit"] = feeSetDetail["smallFeeLimit"];
                        feeSetDetail["otherFeeLimit"] = feeSetDetail["smallFeeLimit"];

                        feeSetDetail["bigFeeLimitEx"] = feeSetDetail["smallFeeLimitEx"];
                        feeSetDetail["otherFeeLimitEx"] = feeSetDetail["smallFeeLimitEx"];

                        if(feeSetDetail["T4DaySmallMaxFeeCheck"] != "on"){
                            feeSetDetail["smallTotalFeeLimit"] = "0";
                        }
                        if(feeSetDetail["T4DayBigMaxFeeCheck"] != "on"){
                            feeSetDetail["bigTotalFeeLimit"] = "0";
                        }
                        if(feeSetDetail["T4DayOtherMaxFeeCheck"] != "on"){
                            feeSetDetail["otherTotalFeeLimit"] = "0";
                        }
                    }else if(templateIndex == 5){
                        //设置两个免费时长相等
                        feeSetDetail["smallFreeDurationEx"] = feeSetDetail["smallFreeDuration"];
                        feeSetDetail["bigFreeDurationEx"] = feeSetDetail["bigFreeDuration"];
                        feeSetDetail["otherFreeDurationEx"] = feeSetDetail["otherFreeDuration"];
                        feeSetDetail["smallFeeStartTime"] = feeSetDetail["feeStartTime"] + ":00";
                        feeSetDetail["bigFeeStartTime"] = feeSetDetail["feeStartTime"] + ":00";
                        feeSetDetail["otherFeeStartTime"] = feeSetDetail["feeStartTime"] + ":00";

                        feeSetDetail["smallFeeEndTime"] = feeSetDetail["feeEndTime"] + ":00";
                        feeSetDetail["bigFeeEndTime"] = feeSetDetail["feeEndTime"] + ":00";
                        feeSetDetail["otherFeeEndTime"] = feeSetDetail["feeEndTime"] + ":00";

                        if(feeSetDetail["isFeeSection"] != "on"){
                            feeSetDetail["smallIsFeeSection"] = "0";
                            feeSetDetail["bigIsFeeSection"] = "0";
                            feeSetDetail["otherIsFeeSection"] = "0";
                        }else{
                            feeSetDetail["smallIsFeeSection"] = "1";
                            feeSetDetail["bigIsFeeSection"] = "1";
                            feeSetDetail["otherIsFeeSection"] = "1";
                        }

                        if(feeSetDetail["smallT5MaxFeeCheck"] != "on"){
                            feeSetDetail["smallTotalFeeLimit"] = "0";
                        }
                        if(feeSetDetail["bigT5MaxFeeCheck"] != "on"){
                            feeSetDetail["bigTotalFeeLimit"] = "0";
                        }
                        if(feeSetDetail["otherT5MaxFeeCheck"] != "on"){
                            feeSetDetail["otherTotalFeeLimit"] = "0";
                        }

                    }else if(templateIndex == 6){
                        //设置两个免费时长相等
                        feeSetDetail["smallFreeDurationEx"] = feeSetDetail["smallFreeDuration"];
                        feeSetDetail["bigFreeDurationEx"] = feeSetDetail["bigFreeDuration"];
                        feeSetDetail["otherFreeDurationEx"] = feeSetDetail["otherFreeDuration"];

                        feeSetDetail["smallFeeStartTime"] = feeSetDetail["feeStartTime"] + ":00";
                        feeSetDetail["bigFeeStartTime"] = feeSetDetail["feeStartTime"] + ":00";
                        feeSetDetail["otherFeeStartTime"] = feeSetDetail["feeStartTime"] + ":00";

                        feeSetDetail["smallFeeEndTime"] = feeSetDetail["feeEndTime"] + ":00";
                        feeSetDetail["bigFeeEndTime"] = feeSetDetail["feeEndTime"] + ":00";
                        feeSetDetail["otherFeeEndTime"] = feeSetDetail["feeEndTime"] + ":00";

                        if(feeSetDetail["smallT6MaxFeeCheck"] != "on"){
                            feeSetDetail["smallTotalFeeLimit"] = "0";
                        }
                        if(feeSetDetail["bigT6MaxFeeCheck"] != "on"){
                            feeSetDetail["bigTotalFeeLimit"] = "0";
                        }
                        if(feeSetDetail["otherT6MaxFeeCheck"] != "on"){
                            feeSetDetail["otherTotalFeeLimit"] = "0";
                        }

                    }
                    var params = {
                        feeSetDetail:feeSetDetail,
                        memberKindInfo:memberKindInfo
                    };
                    if(that.editData == null){
                        ajaxReuqest(starnetContextPath + '/member/kind/add','post',params,function(res){
                            if(res.result == 0){
                                $("#editModal").modal("hide");
                                chargingRules.mainTable.ajax.reload();
                                successfulPrompt("操作成功","");
                            }else{
                                errorPrompt("操作失败",res.msg);
                            }
                        });
                    }else{

                        params["memberKindInfo"]["id"] = that.editData.id;
                        ajaxReuqest(starnetContextPath + '/member/kind/update','post',params,function(res){
                            if(res.result == 0){
                                $("#editModal").modal("hide");
                                chargingRules.mainTable.ajax.reload(null,false);
                                successfulPrompt("操作成功","");
                            }else{
                                errorPrompt("操作失败",res.msg);
                            }
                        });
                    }


                }else{
                    memberKindInfo["packageKind"] = $("#packageKind").val();

                    if(memberKindInfo["packageKind"] == 0){
                        var value = $("#monthPackageValue").val();
                        if(value.trim() == ""){
                            errorPrompt("错误信息","每月金额不能为空");
                            return;
                        }
                    }else if(memberKindInfo["packageKind"] == 1){
                        var value = $("#countPackageValue").val();
                        if(value.trim() == ""){
                            errorPrompt("错误信息","每次金额不能为空");
                            return;
                        }
                    }
                    memberKindInfo["monthPackage"] = $("#monthPackageValue").val();
                    memberKindInfo["countPackage"] = $("#countPackageValue").val();
                    var params = {
                        feeSetDetail:null,
                        memberKindInfo:memberKindInfo
                    };
                    if(that.editData == null){
                        ajaxReuqest(starnetContextPath + '/member/kind/add','post',params,function(res){
                            if(res.result == 0){
                                $("#editModal").modal("hide");
                                chargingRules.mainTable.ajax.reload();
                                successfulPrompt("操作成功","");
                            }else{
                                errorPrompt("操作失败",res.msg);
                            }
                        });

                    }else{
                        params["memberKindInfo"]["id"] = that.editData.id;
                        ajaxReuqest(starnetContextPath + '/member/kind/update','post',params,function(res){
                            if(res.result == 0){
                                $("#editModal").modal("hide");
                                chargingRules.mainTable.ajax.reload(null,false);
                                successfulPrompt("操作成功","");
                            }else{
                                errorPrompt("操作失败",res.msg);
                            }
                        });
                    }

                }
            });
        },
        onT4DaySmallMaxFeeCheckChange:function () {
            $("#T4DaySmallMaxFeeCheck").change(function () {
                var value = document.getElementById("T4DaySmallMaxFeeCheck").checked;
                if(value){
                    document.getElementById("smallT4MaxFeeCheck1").checked = false;
                    $("#smallT4FeeLimit1").val(0);
                    document.getElementById("smallT4MaxFeeCheck2").checked = false;
                    $("#smallT4FeeLimit2").val(0);
                }
            });
        },
        onT4DayBigMaxFeeCheckChange:function () {
            $("#T4DayBigMaxFeeCheck").change(function () {
                var value = document.getElementById("T4DayBigMaxFeeCheck").checked;
                if(value){
                    document.getElementById("bigT4MaxFeeCheck1").checked = false;
                    $("#bigT4FeeLimit1").val(0);
                    document.getElementById("bigT4MaxFeeCheck2").checked = false;
                    $("#bigT4FeeLimit2").val(0);
                }
            });
        },
        onT4DayOtherMaxFeeCheckChange:function () {
            $("#T4DayOtherMaxFeeCheck").change(function () {
                var value = document.getElementById("T4DayOtherMaxFeeCheck").checked;
                if(value){
                    document.getElementById("otherT4MaxFeeCheck1").checked = false;
                    $("#otherT4FeeLimit1").val(0);
                    document.getElementById("otherT4MaxFeeCheck2").checked = false;
                    $("#otherT4FeeLimit2").val(0);
                }
            });
        },

        onSmallT4MaxFeeCheck1Change:function(){
            $("#smallT4MaxFeeCheck1").change(function () {
                var value = document.getElementById("smallT4MaxFeeCheck1").checked;
                if(value){
                    document.getElementById("T4DaySmallMaxFeeCheck").checked = false;
                    $("#smallT4TotalFeeLimit").val(0);
                }
            });
        },
        onBigT4MaxFeeCheck1Change:function(){
            $("#bigT4MaxFeeCheck1").change(function () {
                var value = document.getElementById("bigT4MaxFeeCheck1").checked;
                if(value){
                    document.getElementById("T4DayBigMaxFeeCheck").checked = false;
                    $("#bigT4TotalFeeLimit").val(0);
                }
            });
        },
        onOtherT4MaxFeeCheck1Change:function(){
            $("#otherT4MaxFeeCheck1").change(function () {
                var value = document.getElementById("otherT4MaxFeeCheck1").checked;
                if(value){
                    document.getElementById("T4DayOtherMaxFeeCheck").checked = false;
                    $("#otherT4TotalFeeLimit").val(0);
                }
            });
        },
        onSmallT4MaxFeeCheck2Change:function(){
            $("#smallT4MaxFeeCheck2").change(function () {
                var value = document.getElementById("smallT4MaxFeeCheck2").checked;
                if(value){
                    document.getElementById("T4DaySmallMaxFeeCheck").checked = false;
                    $("#smallT4TotalFeeLimit").val(0);
                }
            });
        },
        onBigT4MaxFeeCheck2Change:function(){
            $("#bigT4MaxFeeCheck2").change(function () {
                var value = document.getElementById("bigT4MaxFeeCheck2").checked;
                if(value){
                    document.getElementById("T4DayBigMaxFeeCheck").checked = false;
                    $("#bigT4TotalFeeLimit").val(0);
                }
            });
        },
        onOtherT4MaxFeeCheck2Change:function(){
            $("#otherT4MaxFeeCheck2").change(function () {
                var value = document.getElementById("otherT4MaxFeeCheck2").checked;
                if(value){
                    document.getElementById("T4DayOtherMaxFeeCheck").checked = false;
                    $("#otherT4TotalFeeLimit").val(0);
                }
            });
        },

        delFunction:function(rowIndex){
            if (!confirm("删除后将永久丢失数据，确认要删除？")) {
                return;
            }
            var data = this.mainTable.row(rowIndex).data();
            var params = {
                "memberKindId":data.id
            }
            $.post(starnetContextPath + '/member/kind/delete',params,function (res) {
                if(res.result == 0){
                    chargingRules.mainTable.ajax.reload(null,false);
                    successfulPrompt("操作成功","");
                }else{
                    errorPrompt("操作失败",res.msg);
                }
            })
        }
    };
    chargingRules.render();
});

// 刷新表格数据
function refreshBtnClick() {
    chargingRules.mainTable.ajax.reload(null,true);
}
