package com.starnetsecurity.parkClientServer.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.starnetsecurity.common.util.AesUtil;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.AccountService;
import com.starnetsecurity.parkClientServer.shiro.token.MultipleAuthenticationToken;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-10-12.
 */
public class AdminLoginAccessFilter extends AccessControlFilter {

    private String loginSuccessUrl;

    private static Logger log = LoggerFactory.getLogger(AdminLoginAccessFilter.class);

    @Autowired
    AccountService accountService;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;

        if(httpServletRequest.getMethod().equals("GET")){
            if(subject.isAuthenticated() || subject.isRemembered()){
                WebUtils.issueRedirect(servletRequest,servletResponse,getLoginSuccessUrl());
            }else{
                return true;
            }
        }else{
            if(subject.isAuthenticated() || subject.isRemembered()){
                return true;
            }
        }

//        if(ModeSelect.mode.equals(0)){
//            return tokenLogin("root","123456","1",servletRequest,servletResponse);
//        }else{
//
//        }
        return adminLogin(servletRequest,servletResponse);
    }

    private boolean adminLogin(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        String username = servletRequest.getParameter("username");
        String password = servletRequest.getParameter("password");
        String rememberMe = servletRequest.getParameter("rememberMe");

        if(StringUtils.isBlank(username)){
            writeExceptionMsg(servletResponse,"请输入用户名");
            return false;
        }
        if(StringUtils.isBlank(password)){
            writeExceptionMsg(servletResponse,"请输入密码");
            return false;
        }
        return tokenLogin(username,password,rememberMe,servletRequest,servletResponse);

    }

    private boolean tokenLogin(String username,String password,String rememberMe, ServletRequest servletRequest,ServletResponse servletResponse) throws IOException {
        MultipleAuthenticationToken multipleAuthenticationToken = new MultipleAuthenticationToken(username,password);
        multipleAuthenticationToken.setLoginType(0);
        if("1".equals(rememberMe)){
            multipleAuthenticationToken.setRememberMe(true);
        }else{
            multipleAuthenticationToken.setRememberMe(false);
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(multipleAuthenticationToken);
            Map<String, Object> modelMap = new HashedMap();
            modelMap.put("result", 0);
            Map<String, Object> data = new HashedMap();
            data.put("url",getLoginSuccessUrl());
            data.put("token",getUserTokenString(username,password));
            modelMap.put("data", data);
            servletResponse.setContentType("text/html;charset=UTF-8");
            servletResponse.getWriter().write(JSON.toJSONString(modelMap));
            return false;
        } catch (IncorrectCredentialsException ex) {
            writeExceptionMsg(servletResponse,"用户密码错误");
        }catch (UnknownAccountException ex){
            writeExceptionMsg(servletResponse,"用户名不存在");
        }
        multipleAuthenticationToken.clear();
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }


    private void writeExceptionMsg(ServletResponse servletResponse, String msg) throws IOException {
        Map<String, Object> modelMap = new HashedMap();
        modelMap.put("result", -2);
        modelMap.put("msg", msg);
        servletResponse.setContentType("text/html;charset=UTF-8");
        servletResponse.getWriter().write(JSON.toJSONString(modelMap));
    }

    public String getLoginSuccessUrl() {
        return loginSuccessUrl;
    }

    public void setLoginSuccessUrl(String loginSuccessUrl) {
        this.loginSuccessUrl = loginSuccessUrl;
    }

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
}
