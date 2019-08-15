package com.starnetsecurity.parkClientServer.shiro.realm;

import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.common.util.HuanXinSdkUtil;
import com.starnetsecurity.parkClientServer.service.AccountService;
import com.starnetsecurity.parkClientServer.service.UserService;
import com.starnetsecurity.parkClientServer.shiro.token.MultipleAuthenticationToken;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Created by 宏炜 on 2016-10-17.
 */
public class LoginRealm extends AuthorizingRealm {

    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //从数据库中获取当前登录用户的详细信息
        String currentUsername = (String) principalCollection.getPrimaryPrincipal();
        AdminUser user = accountService.getAdminUserByUsername(currentUsername);
        if(CommonUtils.isEmpty(user)){
            throw new AuthorizationException();
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(accountService.getRolesByUsername(currentUsername));
        authorizationInfo.addStringPermissions(accountService.getResourcesByUsername(currentUsername));
        return authorizationInfo;
    }
    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        MultipleAuthenticationToken multipleAuthenticationToken = (MultipleAuthenticationToken)authenticationToken;
        String username = multipleAuthenticationToken.getUsername();
        AdminUser user = accountService.getAdminUserByUsername(username);
        if(CommonUtils.isEmpty(user)){
            throw new UnknownAccountException();
        }

        SimpleAuthenticationInfo authenticationInfo;
        if(StringUtils.isEmpty(multipleAuthenticationToken.getTokenStr())){
            authenticationInfo = new SimpleAuthenticationInfo(
                    user.getUserName(),
                    user.getUserPwd(),
                    getName()
            );
        }else{
            String password = new SimpleHash("md5",user.getUserPwd(),null,1).toString();
            authenticationInfo = new SimpleAuthenticationInfo(
                    user.getUserName(),
                    password,
                    getName()
            );
        }

        this.setSession(Constant.SESSION_LOGIN_USER,user);
        return authenticationInfo;
    }

    private void setSession(Object key, Object value){
        Subject currentUser = SecurityUtils.getSubject();
        if(!CommonUtils.isEmpty(currentUser)){
            Session session = currentUser.getSession();
            if(!CommonUtils.isEmpty(session)){
                session.setAttribute(key, value);
            }
        }
    }
}
