package com.starnetsecurity.parkClientServer.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.parkClientServer.service.AccountService;
import com.starnetsecurity.parkClientServer.service.ResourceService;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-10-12.
 */
public class AdminUserFilter extends AccessControlFilter {

    @Autowired
    AccountService accountService;

    @Autowired
    ResourceService resourceService;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        if(httpServletRequest.getMethod().equals("GET")){
            if(subject.isAuthenticated() || subject.isRemembered()){
                if(!relogin(subject,servletResponse)){
                    return false;
                }

                this.configMenu(subject);
                return true;
            }else{
                WebUtils.issueRedirect(servletRequest,servletResponse,getLoginUrl());
                return false;
            }
        }else{
            if(subject.isAuthenticated() || subject.isRemembered()){
                if(!relogin(subject,servletResponse)){
                    return false;
                }
                return true;
            }else{
                writeExceptionMsg(servletResponse,"用户未登录");
                return false;
            }
        }
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

    public boolean relogin(Subject subject,ServletResponse servletResponse) throws IOException {
        Session session = subject.getSession();
        Object user = session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(CommonUtils.isEmpty(user)){
            String username = String.valueOf(subject.getPrincipal());
            if(StringUtils.isBlank(username)){
                writeExceptionMsg(servletResponse,"用户未登录");
                return false;
            }
            AdminUser adminUser = accountService.getAdminUserByUsername(username);
            session.setAttribute(Constant.SESSION_LOGIN_USER,adminUser);
        }
        return true;
    }

    public void configMenu(Subject subject){
        Session session = subject.getSession();
        AdminUser adminUser = (AdminUser) session.getAttribute(Constant.SESSION_LOGIN_USER);
        List menu = resourceService.getUserMenuResource(adminUser.getId());
        session.setAttribute("menuList",menu);
    }
}
