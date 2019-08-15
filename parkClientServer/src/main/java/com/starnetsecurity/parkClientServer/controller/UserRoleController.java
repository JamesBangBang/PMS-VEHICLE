package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSON;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.entity.Operator;
import com.starnetsecurity.parkClientServer.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-10-24.
 */
@Controller
@RequestMapping("/auth")
public class UserRoleController extends BaseController {


    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @Autowired
    RoleService roleService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    CarparkService carparkService;

    @Autowired
    ResourceService resourceService;


    @Autowired
    OperatorService operatorService;

    @ResponseBody
    @RequestMapping(value = "/user/list", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map userList(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                             @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                             @RequestParam(value="draw", required=false) Integer draw,
                             @RequestParam(value="isShow", required=false,defaultValue = "1") Integer show){
        Map<String,Object> res = new HashedMap();
        try{
            boolean isRoleExist = false;
            if (show == 1){
                isRoleExist = true;
            }
            List list = userService.getAdminUserListForPage(length,start,isRoleExist);
            Long total = userService.countAdminUser();
            res.put("data",list);
            res.put("draw",draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/role/list", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map roleList(@RequestParam(value="start", required=false,defaultValue = "1") Integer start,
                             @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                             @RequestParam(value="draw", required=false) Integer draw){
        Map<String,Object> res = new HashedMap();
        try{
            List data = roleService.getAdminRoleForPage(start,length);
            Long total = roleService.countAdminRole();
            res.put("data",data);
            res.put("draw",draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/role/merge", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map mergeRole(@RequestBody Map params){
        Map<String,Object> res = new HashedMap();
        try{
            roleService.mergeAdminRole(params);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/role/delete", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map deleteRole(@RequestParam(value = "id",required = true) String id){
        Map<String,Object> res = new HashedMap();
        try{
            roleService.deleteAdminRole(id);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/menu/tree", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public String menuTree(@RequestParam(value = "id",required = false,defaultValue = "#") String id){

        List list = resourceService.getMenuTree(id);
        return JSON.toJSONString(list);

    }

    @ResponseBody
    @RequestMapping(value = "/role/select", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map roleSelect(){
        Map<String,Object> res = new HashedMap();
        try{
            List data = roleService.getAdminRoleSelect();
            res.put("data",data);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/user/merge", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map mergeUser(@RequestBody Map params){
        Map<String,Object> res = new HashedMap();
        try{
            userService.mergeUser(params);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/user/delete", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map deleteUser(@RequestParam(value = "id",required = true) String id){
        Map<String,Object> res = new HashedMap();
        try{

            AdminUser adminUser = (AdminUser) getUser();
            if(adminUser.getId().equals(id)){
                throw new BizException("无法删除自身");
            }
            userService.deleteAdminUser(id);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/department/select", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map departmentSelect(){
        Map<String,Object> res = new HashedMap();
        try{
            List data = departmentService.getDepartmentInfoSelect();
            res.put("data",data);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/park/select", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map parSelect(@RequestBody Map params){
        Map<String,Object> res = new HashedMap();
        try{
            List data = carparkService.getCarParksInfoSelect((List<String>)params.get("departmentIds"));
            res.put("data",data);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @RequestMapping("user/manage")
    public String  userManage(){
        return  "admin/userManage";

    }


    @RequestMapping("operator/setting")
    public String  userSetting(){
        return  "admin/operatorSetting";

    }

    @ResponseBody
    @RequestMapping(value = "/operator/list", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map operatorList(@RequestParam(value="start", required=false,defaultValue = "1") Integer start,
                            @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                            @RequestParam(value="draw", required=true) Integer draw){
        Map<String,Object> res = new HashedMap();
        try{
            List list = operatorService.getOperatorList(start,length);
            Long total = operatorService.countOperatorList();
            res.put("data", list);
            res.put("draw", draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/operator/isPwdRepeat",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public int isPwdRepeat(@RequestBody Map params){
        int res=0 ;
        res=operatorService.isPwdRepeat(params);
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/operator/save",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map save(@RequestBody Map params){
        Map res=new HashMap();
        try {
            String pwd= new SimpleHash("md5",params.get("pwd"),null,1).toString();
            params.put("pwd",pwd);
            operatorService.operatorSave(params);
            this.success(res);
        }catch (BizException e)
        { this.failed(res, e);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/operator/del",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map del(@RequestParam(value = "operatorId",required = true) String id){
        Map<String,Object> res = new HashMap<>();
        try {
            operatorService.deleteOperator(id);
            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/operator/edit",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map edit(@RequestBody Map params){
        Map res=new HashMap();
        Map isOperatorRepeat=new HashMap();
        Operator operator =operatorService.getRemainOperator((String)params.get("opertorId"));
//        Operator operator=new Operator();
        int type=0 ;
        try {
            //判断姓名存在不
            if(!params.get("operatorLoginName").equals(params.get("repeatoperatorLoginName")))
            {
                isOperatorRepeat.put("operatorLoginName",params.get("operatorLoginName"));
            }

            if(!params.get("operatorName").equals(params.get("repeatoperatorName")))
            {
                isOperatorRepeat.put("operatorName",params.get("operatorName"));
            }
            type=operatorService.isPwdRepeat(isOperatorRepeat);

            if(type==1){
                throw new BizException("登录用户名不能相同");
            }else if(type==2)
            {
                throw new BizException("操作员姓名不能相同");
            }
            operator.setOperatorUserName((String)params.get("operatorLoginName"));
            operator.setOperatorName((String)params.get("operatorName"));
            //判断是否修改密码
            if((Integer)params.get("pwdTag")!=0)
            {
                String newPwd=(String) params.get("newPwd");
                String checkNewPwd=(String) params.get("checkNewPwd");
                String oldPwd= new SimpleHash("md5",params.get("oldPwd"),null,1).toString();
                if(!oldPwd.equals(params.get("checkOldPwd")))
                {
                    throw new BizException("旧密码错误");
                }
                if(!newPwd.equals(checkNewPwd))
                {
                    throw new BizException("两次输入的密码不同");
                }
                newPwd=new SimpleHash("md5",newPwd,null,1).toString();
                operator.setOperatorUserPwd(newPwd);
            }
            operator.setOperatorId((String)params.get("opertorId"));
            operatorService.editOperator(operator);
            this.success(res);
        }catch (BizException e)
        { this.failed(res, e);
        }
        return res;
    }





    @RequestMapping("role/manage")
    public String  paymentDetailsIndex(){
        return  "admin/roleManage";

    }

    @ResponseBody
    @RequestMapping(value = "/permission/info", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map permissionInfo(String userId){
        Map<String,Object> res = new HashedMap();
        try{
            List parkData = carparkService.getUserCarParksIdList(userId);
            String roleData = roleService.getUserRoleId(userId);
            List departData = departmentService.getUserDepartmentIds(userId);
            res.put("parkData",parkData);
            res.put("roleData",roleData);
            res.put("departData",departData);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/role/permission/info", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map rolePermissionInfo(String roleId){
        Map<String,Object> res = new HashedMap();
        try{
            List data = resourceService.getRoleMenuIdResources(roleId);
            List selectData = resourceService.getRoleRootMenuResources(roleId);

            res.put("selectData",selectData);
            res.put("data",data);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }
}
