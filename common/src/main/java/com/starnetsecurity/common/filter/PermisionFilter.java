package com.starnetsecurity.common.filter;

import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 类名： fm.filter.PermisionFilter
 * 创建人： CM
 * 创建时间： 2016/3/6.
 */
public class PermisionFilter extends AuthorizationFilter {
    private static Logger LOGGER = LoggerFactory.getLogger(PermisionFilter.class);

    @Autowired
    HttpSession session;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) throws Exception {
        return isPermitted();
    }


    private boolean isPermitted() {
        Object user = session.getAttribute("currentUser");
        if (user == null) {
            LOGGER.warn("当前用户未授权,申请认证授权！");
        }
        return user == null ? false : true;
    }
}
