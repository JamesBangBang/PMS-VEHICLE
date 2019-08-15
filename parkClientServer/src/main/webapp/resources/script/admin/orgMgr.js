/**
 * Created by 宏炜 on 2017-06-15.
 */
$().ready(function () {

    // validate signup form on keyup and submit
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#editForm").validate({
        rules: {
            name: "required",
            priority: "required",
            name: {
                required: true,
                minlength: 2,
                maxlength:20
            },
            priority: {
                required: true,
                digits:true,
                min:0,
                max:99999
            },
            limitNumber: {
                required: true,
                digits:true,
                min:0,
                max:99999
            }
        },
        messages: {
            name: {
                required: icon + "请输入组织机构名称",
                minlength: icon + "组织机构名称必须两个字符以上",
                maxlength: icon + "组织机构名称不能超过20个字符"
            },
            priority: {
                required: icon + "请输入优先级"
            },
            limitNumber: {
                required: icon + "请输入容量"
            }
        }
    });

    jQuery.extend(jQuery.validator.messages, {
        digits: "不能选择负数",

    });

    $("#addForm").validate({
        rules: {
            name: "required",
            priority: "required",
            limitNumber:"required",
            name: {
                required: true,
                minlength: 2,
                maxlength:20
            },
            priority: {
                required: true,
                digits:true,
                min:0,
                max:99999
            },
            limitNumber1: {
                required: true,
                digits:true,
                min:0,
                max:99999
            }
        },
        messages: {
            name: {
                required: icon + "请输入组织机构名称",
                minlength: icon + "组织机构名称必须两个字符以上",
                maxlength: icon + "组织机构名称不能超过20个字符"
            },
            priority: {
                required: icon + "请输入优先级"
            },
            limitNumber1: {
                required: icon + "请输入容量"
            }
        }
    });
    jQuery.extend(jQuery.validator.messages, {
        digits: "不能选择负数",

    });

});

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
                $('#editId').val(data.id);
                $('#orgName').val(data.text);
                $('#priority').val(data.original.priority);
                $('#limitNumber').val(data.original.limit_number);
                $('#addHigherLevel').val(data.text);
                $('#parentId').val(data.id);

                var parent = $("#orgTree").jstree().get_parent(node.node);
                if(parent){
                    var parentNode = $("#orgTree").jstree().get_node(parent);
                    $('#higherLevel').val(parentNode.text);
                }else{
                    $('#higherLevel').val("null");
                }
            });
        }else{
            warningPrompt("获取组织机构树信息失败","");
        }
    });
}

$(function(){
    var treeHeight = document.body.offsetHeight - 95;

    $('.full-height-scroll').slimScroll({
        height: treeHeight + 'px'
    });
    initTree("");
    selectMatching();

});

    $('#addBtn').click(function(){
        if (! $("#addForm").valid()) {
            return;
        }
        var params = $("#addForm").serializeObject();
        if($('#isRoot')[0].checked){
            params.parentId = "";
        }
        ajaxReuqest(starnetContextPath + "/org/addOrg","post",params,function (res) {
            if(res.result == 0){
                initTree(res.data);
                selectMatching();
                successfulPrompt("添加成功","");
                location.replace(location.href);
                $("#addModal").modal("hide");

            }else{
                errorPrompt("添加失败",res.msg);
            }
        });
    });
    $('#editBtn').click(function(){

        if (! $("#editForm").valid()) {
            return;
        }

        var params = $("#editForm").serializeObject();

        ajaxReuqest(starnetContextPath + "/org/updateOrg","post",params,function (res) {
            if(res.result == 0){
                initTree(params.id);
                successfulPrompt("修改成功","");

            }else{
                errorPrompt("修改失败",res.msg);
            }
        });
    });

    $("#delBtn").click(function(){

        if(window.confirm("是否删除数据")) {


            //表格是否有数据
            if (($('#orgName').val())) {
                var params = {
                    "orgName": $('#orgName').val()
                }

                //搜索相应ID删除
                $.post(starnetContextPath + "/org/dataDelete", params, function (res) {

                    if (res.result == 0) {
                        if (res.id == null) {
                            infoPrompt("删除失败", "机构不存在");
                            return;

                        }
                        var params = {
                            "id": res.id
                        }

                        var parent = $("#orgTree").jstree().get_parent(res.id);
                        $.post(starnetContextPath + "/org/daleteOrg", params, function (res) {
                            if (res.result == 0) {
                                if (parent != "#") {
                                    initTree(parent);

                                } else {
                                    initTree("");
                                }

                                //重新加载数据
                                selectMatching();
                                successfulPrompt("删除成功", "");
                                location.replace(location.href);

                            } else {
                                errorPrompt("删除失败", res.msg);
                            }
                        });

                    } else {
                        infoPrompt("删除失败", "机构不存在");
                    }

                });
            }
            else {
                infoPrompt("删除失败", "请选择一个机构");
                // //点击节点删除
                // var nodes = $("#orgTree").jstree().get_selected();
                // if (nodes.length == 0) {
                //     infoPrompt("删除失败", "请选择一个机构");
                //     return;
                // }
                // var parent = $("#orgTree").jstree().get_parent(nodes[0]);
                //
                // var params = {
                //     "id": nodes[0]
                // }
                //
                // $.post(starnetContextPath + "/org/daleteOrg",params,function(res){
                //
                //     if(res.result == 0){
                //         if(parent != "#"){
                //             initTree(parent);
                //
                //         }else{
                //             initTree("");
                //             // errorPrompt("不能删除此节点",res.msg);
                //         }
                //         successfulPrompt("删除成功","");
                //         selectMatching();
                //
                //     }else{
                //         errorPrompt("删除失败",res.msg);
                //     }
                // });
            }
        }
    });

    //自动匹配
    function  selectMatching() {

        var list;
        var dataList = {
            value : []
        };

        $.ajax({
            type : 'post',
            async : false,
            url :starnetContextPath + "/org/select",
            success : function(data) {
                list=data;
                for (var i = 0; i < data.data.length; i++) {
                    var obj = {};
                    // obj.id = i;
                    obj.name = data.data[i].name;
                    dataList.value.push(obj);
                }
            }
        });


        var testdataBsSuggest = $("#test_data").bsSuggest({

            indexId: 1, //data.value 的第几个数据，作为input输入框的内容
            // indexKey: 1, //data.value 的第几个数据，作为input输入框的内容
            data : dataList

        });

        $('#test_select').click(function(){
            $('#higherLevel').val("");
            for (var i = 0; i < list.data.length; i++) {

                if($("#test_data").val()==(list.data[i].name))
                {

                      $('#orgName').val(list.data[i].name);
                      $('#priority').val(list.data[i].priority);
                      $('#limitNumber').val(list.data[i].limit_number);
                      var j=list.data[i].parent_id
                     for (var i = 0; i < list.data.length; i++) {
                       if(j==(list.data[i].id) ) {
                           $('#higherLevel').val(list.data[i].name);
                       }
                    }
                    }

                }
        });

    }








