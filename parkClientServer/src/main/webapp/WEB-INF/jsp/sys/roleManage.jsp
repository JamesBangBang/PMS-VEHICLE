<%--
  Created by IntelliJ IDEA.
  User: 宏炜
  Date: 2017-02-15
  Time: 9:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%-- easyui css --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/jquery-easyui-1.5.1/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/jquery-easyui-1.5.1/themes/mobile.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/jquery-easyui-1.5.1/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/easyui-common.css">
    <%-- easyui js --%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery-easyui-1.5.1/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery-easyui-1.5.1/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery-easyui-1.5.1/jquery.easyui.mobile.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery-easyui-1.5.1/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/commonValue.js"></script>
    <title></title>
</head>
<body>
    <div style="width: 100%;height: 100%;">
        <table id="roleManage" class="easyui-datagrid"
               url="${pageContext.request.contextPath}/user/getRoleListForPage"
               fitColumns="true" pagination="true" singleSelect="true" fit="true" toolbar="#toolbar">
            <thead>
                <tr>
                    <th field="roleName" width="50">角色名称</th>
                    <th field="description" width="80">角色描述</th>
                    <th field="id" width="20" data-options="formatter:formatOperation">操作</th>
                </tr>
            </thead>
        </table>
        <div id="toolbar">
            <a href="javascript:void (0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newRole()">新增角色</a>
        </div>
    </div>

    <div id="newRoleWin" class="easyui-window" closed="true" title="角色编辑" style="width: 390px;height: 340px;overflow: hidden;">
        <div class="easyui-navpanel" style="position:relative;padding:20px">
            <form id="roleForm">
                <input id="roleId" type="hidden" name="roleId">
                <div style="margin-bottom:10px">
                    <input id="roleName" name="roleName" class="easyui-textbox" label="角色名称" style="width:100%" data-options="required:true,validType:['length[0,100]']">
                </div>
                <div style="margin-bottom:10px">
                    <input id="description" name="description" class="easyui-textbox" label="角色描述" style="width:100%" data-options="required:true,validType:['length[0,200]']">
                </div>
                <div style="margin-bottom:10px">
                    <input id="menuResources" class="easyui-combotree" data-options="required:true,multiple:true" label="菜单权限" style="width: 100%;">
                </div>
                <div style="margin-bottom:10px">
                    <input id="areaResources" class="easyui-combotree" data-options="multiple:true" label="区域权限" style="width: 100%;">
                </div>
            </form>
            <div style="margin-bottom:10px;text-align: center;">
                <a id="submitBtn" type="button" class="easyui-linkbutton" style="width: 120px;height: 25px;">提交</a>
            </div>
        </div>
    </div>
</body>
<script>
    $(function(){
        $('#menuResources').combotree({
            "url":"${pageContext.request.contextPath}/user/getMenuResourceTreeList",
            cascadeCheck: false,
            onCheck: function (node, checked) {
                if (checked) {
                    var menuResources = $('#menuResources').combotree('tree');
                    var parentNode = menuResources.tree('getParent', node.target);

                    if (parentNode != null) {
                        menuResources.tree('check', parentNode.target);
                    }
                } else {
                    var menuResources = $('#menuResources').combotree('tree');
                    var childNode = menuResources.tree('getChildren', node.target);
                    if (childNode.length > 0) {
                        for (var i = 0; i < childNode.length; i++) {
                            menuResources.tree('uncheck', childNode[i].target);
                        }
                    }
                    var parentNode = menuResources.tree('getParent', node.target);
                    if(parentNode != null){
                        childNode = menuResources.tree('getChildren', parentNode.target);
                        for (var i = 0; i < childNode.length; i++) {
                            if(childNode[i]._checked == true){
                                return;
                            }
                        }
                        menuResources.tree('uncheck', parentNode.target);
                    }

                }
            }
        });

        $('#areaResources').combotree({
            "url":"${pageContext.request.contextPath}/user/getAreaResourceTreeList",
            cascadeCheck: false,
            onCheck: function (node, checked) {
                if (checked) {
                    var areaResources = $('#areaResources').combotree('tree');
                    var parentNode = areaResources.tree('getParent', node.target);

                    if (parentNode != null) {
                        areaResources.tree('check', parentNode.target);
                    }
                } else {
                    var areaResources = $('#areaResources').combotree('tree');
                    var childNode = areaResources.tree('getChildren', node.target);
                    if (childNode.length > 0) {
                        for (var i = 0; i < childNode.length; i++) {
                            areaResources.tree('uncheck', childNode[i].target);
                        }
                    }
                    var parentNode = areaResources.tree('getParent', node.target);
                    if(parentNode != null){
                        childNode = areaResources.tree('getChildren', parentNode.target);
                        for (var i = 0; i < childNode.length; i++) {
                            if(childNode[i]._checked == true){
                                return;
                            }
                        }
                        areaResources.tree('uncheck', parentNode.target);
                    }
                }
            }
        });

        $('#roleManage').datagrid({
            onDblClickRow: function (rowIndex, rowData) {
                $("#roleId").val(rowData.id);
                $("#roleName").textbox("setValue",rowData.roleName);
                $("#description").textbox("setValue",rowData.description);

                $('#areaResources').combotree('setValues', rowData.resourceIds.split(','));
                $('#menuResources').combotree('setValues', rowData.resourceIds.split(','));

                var areaResources = $('#areaResources').combotree('tree');
                var selected = areaResources.tree("getChecked");
                $('#areaResources').combotree('setValues', selected);

                var menuResources = $('#menuResources').combotree('tree');
                var selected2 = menuResources.tree("getChecked");
                $('#menuResources').combotree('setValues', selected2);

                $('#newRoleWin').window({
                    left:50,
                    top:50,
                    minimizable:false,
                    maximizable:false
                });
                $('#newRoleWin').window('open');
            }
        });

        $("#submitBtn").click(function(){
            if($('#roleForm').form('validate') == false) return;
            var params = {
                "roleId":$("#roleId").val(),
                "roleName":$("#roleName").textbox("getValue"),
                "description":$("#description").textbox("getValue"),
                "resourceIds":$("#menuResources").val()
            };
            $.post("${pageContext.request.contextPath}/user/mergeRole",params,function(res){
                if(res.result == 0){
                    $('#roleManage').datagrid("reload");
                    $('#newRoleWin').window('close');
                }else{
                    alert(res.msg);
                }
            },"json");

        });
    });






    function newRole(){
        $("#roleId").val("");
        $("#roleName").textbox("setValue","");
        $("#description").textbox("setValue","");

        $('#areaResources').combotree('setValues', []);
        $('#menuResources').combotree('setValues', []);
        $('#newRoleWin').window({
            left:50,
            top:50,
            minimizable:false,
            maximizable:false
        });
        $('#newRoleWin').window('open');
    }

    function formatOperation(val,row){
        return '<a href="javascript:deleteRole(\''+val+'\')">删除</a>';
    }

    function deleteRole(id){
        if (!confirm("确认要删除？")) {
            return;
        }
        var params = {
            "roleId":id
        }

        $.post("${pageContext.request.contextPath}/user/daleteRole",params,function(res){
            if(res.result == 0){
                $('#roleManage').datagrid("reload");
            }else{
                alert(res.msg);
            }
        },"json");
    }

</script>
</html>
