<%--
  Created by IntelliJ IDEA.
  User: lubin
  Date: 2017/1/15
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/json/json2.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/commonValue.js"></script>
    <title></title>
</head>
<body>
    <div style="padding: 10px">
        <form id="userForm">
            <div style="margin-bottom: 10px;">
                <c:if test="${empty user}">
                    <input id="userName" type="text" class="easyui-textbox" data-options="required:true,validType:['length[0,20]','username']" required="true" label="用户名" style="width:100%;">

                </c:if>
                <c:if test="${not empty user}">
                    <div id="userName">用户名:<span style="padding-left: 43px;">${user.userName}</span> </div>
                </c:if>
            </div>
            <div style="margin-bottom: 10px;">
                <input id="trueName" type="text" class="easyui-textbox" data-options="required:true,validType:['length[2,20]']" required="true"  label="真实姓名" value="${user.trueName}" style="width:100%;">
            </div>
            <div style="margin-bottom: 10px;">
                <input id="email" type="text" class="easyui-textbox" data-options="required:true,validType:['length[0,40]','email']"   label="电子邮箱" value="${user.email}" style="width:100%;">
            </div>
            <div style="margin-bottom: 10px;">
                <input id="phone" type="text" class="easyui-textbox" data-options="required:true,validType:['length[0,11]','phoneRex']" required="true"  label="手机号码" value="${user.phone}" style="width:100%;">
            </div>
            <div style="margin-top: 10px">
                <input id="roleIds" class="easyui-combobox" name="roleIds" label="角色" style="width:100%;" value="${user.roleIds}"
                       data-options="valueField:'id',textField:'text',panelHeight:'auto',url:'${pageContext.request.contextPath}/user/getRoleComboboxList'" required="true" editable="false">
            </div>
            <div style="margin-top: 15px;text-align: center;">
                <a href="javascript:void(0)" class="easyui-linkbutton" id="submitBtn" style="width:120px;">提交</a>
            </div>
        </form>
    </div>
</body>
<script>
    var userId=$.getUrlParam("userId");

    $("#submitBtn").click(function(){

        if($('#userForm').form('validate') == false) return;
        if(userId != null && userId != ""){
            var params = {
                "trueName":$("#trueName").val(),
                "email":$("#email").val(),
                "phone":$("#phone").val(),
                "userId":userId,
                "roleIds":$("#roleIds").val()
            };
            doAjaxReuqest("${pageContext.request.contextPath}/user/updateUser",'POST',params,
                    function(res){
                        if(res.result == 0){
                            parent.$('#editUserWin').window('close');
                            parent.$('#userManage').datagrid("reload");
                        }else{
                            alert(res.msg);
                        }
                    },
                    function(res){
                        alert("提交失败");
                    });
        }else{
            var params = {
                "userName":$("#userName").val(),
                "trueName":$("#trueName").val(),
                "email":$("#email").val(),
                "phone":$("#phone").val(),
                "roleIds":$("#roleIds").val()
            };

            doAjaxReuqest("${pageContext.request.contextPath}/user/addUser",'POST',params,
                    function(res){
                        if(res.result == 0){
                            parent.$('#newUserWin').window('close');
                            parent.$('#userManage').datagrid("reload");
                        }else{
                            alert(res.msg);
                        }
                    },
                    function(res){
                        alert("提交失败");
                    });
        }
    });
</script>
</html>
