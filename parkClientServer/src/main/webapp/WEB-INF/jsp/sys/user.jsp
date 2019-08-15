<%--
  Created by IntelliJ IDEA.
  User: lubin
  Date: 2017/1/13
  Time: 18:02
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
        <table id="userManage" title="用户管理" class="easyui-datagrid"
               url="${pageContext.request.contextPath}/user/getUserListForPage"
               toolbar="#toolbar"
               fitColumns="true" pagination="true" singleSelect="true" fit="true">
            <thead>
                <tr>
                    <th field="userName" width="50">用户名</th>
                    <th field="trueName" width="50">真实姓名</th>
                    <th field="email" width="50">邮箱</th>
                    <th field="phone" width="50">手机</th>
                    <th field="id" width="10"  data-options="formatter:formatOperation">操作</th>
                </tr>
            </thead>
        </table>
        <div id="toolbar">
            <a href="javascript:void (0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">新增用户</a>
        </div>

        <div id="newUserWin" class="easyui-window" closed="true" title="新增用户" style="width: 420px;height: 370px;overflow: hidden;">
            <iframe id="newUserFrame" frameborder="0" style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/user/newUser"></iframe>
        </div>

        <div id="editUserWin" class="easyui-window" closed="true" title="编辑用户" style="width: 420px;height: 370px;overflow: hidden;">
            <iframe id="editUserFrame" frameborder="0" style="width: 100%;height: 100%;" src="${pageContext.request.contextPath}/user/newUser"></iframe>
        </div>
    </div>
</body>
<script>
    $('#userManage').datagrid({
        onDblClickRow: function (rowIndex, rowData) {
            $('#editUserFrame').attr('src', $('#editUserFrame').attr('src').split('?')[0] + '?userId='+rowData.id + '&pagetime='+ new Date().getTime());
            $('#editUserWin').window({
                left:50,
                top:50,
                minimizable:false,
                maximizable:false
            });
            $('#editUserWin').window('open');
        }
    });
    function newUser(){
        $('#newUserFrame').attr('src', $('#newUserFrame').attr('src'));
        $('#newUserWin').window({
            left:50,
            top:50,
            minimizable:false,
            maximizable:false
        });
        $('#newUserWin').window('open');
    }

    function deleteUser(userId){

        if (!confirm("确认要删除？")) {
            return;
        }
        var params = {
            "userId":userId
        }
        $.post("${pageContext.request.contextPath}/user/daleteUser",params,function(res){
            if(res.result == 0){
                $('#userManage').datagrid("reload");
            }else{
                alert(res.msg);
            }
        },"json");

    }
    function formatOperation(val,row){
        return '<a href="javascript:deleteUser(\''+val+'\')">删除</a>';
    }
</script>
</html>
