package com.starnetsecurity.parkClientServer.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.util.AesUtil;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.commonService.service.WxUserService;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.shiro.token.MultipleAuthenticationToken;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-11-02.
 */
public class TokenFilter extends AccessControlFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenFilter.class);


//    @Autowired
//    WxUserService wxUserService;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return true;
        } else {
            String token = servletRequest.getParameter("token");
            if ( token == null || StringUtils.isBlank(token) || "undefined".equals(token)) {
                return true;
            }
            LOGGER.info("用户提交token:{}", token);
            try {
                Timestamp now = CommonUtils.getTimestamp();
                Constant.setPropertie("token",token);
                token = AesUtil.decodeToken(token);
                JSONObject jsonToken = JSON.parseObject(token);
                Timestamp tokenTime = new Timestamp(Long.parseLong(jsonToken.getString("loginTime")));

                /*if((now.getTime() - tokenTime.getTime()) > (2 * 60 * 60 * 1000)){
                    LOGGER.info("token登录超时,用户信息username:{}",jsonToken.getString("username"));
                    return false;
                }*/
                String username = jsonToken.getString("username");
                String password = jsonToken.getString("password");
                MultipleAuthenticationToken multipleAuthenticationToken = new MultipleAuthenticationToken(username,password);
                multipleAuthenticationToken.setTokenStr(token);
                multipleAuthenticationToken.setLoginType(1);
                subject.login(multipleAuthenticationToken);
                LOGGER.info("用户[{}]通过token授权登录",username);

                return true;
            }  catch (IncorrectCredentialsException ex) {
                writeExceptionMsg(servletResponse,"用户密码错误");
                return false;
            }catch (UnknownAccountException ex){
                writeExceptionMsg(servletResponse,"用户名不存在");
                return false;
            }catch (Exception e) {
                LOGGER.error("token 用户信息授权错误,token:{}",token,e);
                return false;
            }
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Map<String, Object> modelMap = new HashedMap();
        modelMap.put("result", -1008);
        modelMap.put("msg", "用户身份验证失效，请重试登录");
        servletResponse.setContentType("text/html;charset=UTF-8");
        servletResponse.getWriter().write(JSON.toJSONString(modelMap));
        return false;
    }

    private void writeExceptionMsg(ServletResponse servletResponse, String msg) throws IOException {
        Map<String, Object> modelMap = new HashedMap();
        modelMap.put("result", -2);
        modelMap.put("msg", msg);
        servletResponse.setContentType("text/html;charset=UTF-8");
        servletResponse.getWriter().write(JSON.toJSONString(modelMap));
    }


}