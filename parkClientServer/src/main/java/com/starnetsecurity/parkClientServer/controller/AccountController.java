package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.AesUtil;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.service.AccountService;
import com.starnetsecurity.parkClientServer.service.UserService;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by 宏炜 on 2016-12-12.
 */
@Controller
@RequestMapping("account")
public class AccountController extends BaseController{

    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    private String getUserTokenString(String username,String password){
        AdminUser adminUser = accountService.getAdminUserByUsername(username);
        Map<String,String> res = new HashedMap();
        res.put("loginTime",String.valueOf(new Date().getTime()));
        res.put("username",username);
        res.put("password",adminUser.getUserPwd());
        res.put("userTag","AdminUser");
        String deToken = JSON.toJSONString(res, SerializerFeature.WriteMapNullValue);
        AesCipherService aesCipherService = new AesCipherService();
        String encodeToken = aesCipherService.encrypt(deToken.getBytes(), Hex.decode(AesUtil.aesKey)).toBase64().replaceAll("\\+", "-").replaceAll("/", "*");
        return encodeToken;
    }

    @RequestMapping(value = "login")
    public String login(ModelMap modelMap){
        if (Constant.getPropertie("isUseBasicPlatform").equals("1")){
            return "redirect:http://" + Constant.getPropertie("SYNC.BASIC_SERVER_IP") + ":" + Constant.getPropertie("SYNC.BASIC_SERVER_PORT");
        }else{
            return "login";
        }
    }

    @RequestMapping(value = "/ticketLogin")
    public String ticketLogin(@RequestParam(value = "ticket") String ticket) throws IOException {
        String res = "";
        if (!CommonUtils.isEmpty(ticket)){
            String responseStr = HttpRequestUtils.httpGetRequests("http://192.168.65.150:9000/api/v1/user/detail","ticket=" + ticket);
            JSONObject resData = JSON.parseObject(responseStr);
            if ("0".equals(resData.getString("code"))){
                res =  "redirect:http://" + Constant.getPropertie("SYNC.SYS_IP") + ":" + Constant.getPropertie("SYNC.SYS_PORT") + "/system/index?token=" + getUserTokenString("root", "123456");
            } else {
                res =  "login";
            }
        }
        return  res;
    }



    @ResponseBody
    @RequestMapping(value = "/user/data", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map commitUserData(String trueName, String sex, String email, String telephone, String qq){
        Map<String,Object> res = new HashedMap();
        try{
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession();
            AdminUser user = (AdminUser) session.getAttribute(Constant.SESSION_LOGIN_USER);

            user.setTrueName(trueName);
            user.setSex(sex);
            user.setTelephone(telephone);
            user.setEmail(email);
            user.setQq(qq);
            userService.updateAdminUser(user);
            session.setAttribute(Constant.SESSION_LOGIN_USER,user);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/user/pwd", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map commitUserPwd(@RequestParam(value = "oldPwd",required = true) String oldPwd,
                             @RequestParam(value = "newPwd",required = true) String newPwd,
                             @RequestParam(value = "checkPwd",required = true) String checkPwd){
        Map<String,Object> res = new HashedMap();
        try{


            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession();
            AdminUser user = (AdminUser) session.getAttribute(Constant.SESSION_LOGIN_USER);
            if(!new SimpleHash("md5",oldPwd,null,1).toString().equals(user.getUserPwd())){
                throw new BizException("旧密码错误");
            }
            if(!newPwd.equals(checkPwd)){
                throw new BizException("两次输入的密码不同");
            }
            user.setUserPwd(new SimpleHash("md5",newPwd,null,1).toString());
            userService.updateAdminUser(user);
            session.setAttribute(Constant.SESSION_LOGIN_USER,user);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    public static void main(String[] args) throws InterruptedException {
        int j = 0;
        for (int i = 0;i < 10;i++){
            if (i == 3){
                break;
            }else {
                Thread.sleep(500 * (i+1));
            }
            j = i;
        }
        System.out.printf(j + "");

    }
}
