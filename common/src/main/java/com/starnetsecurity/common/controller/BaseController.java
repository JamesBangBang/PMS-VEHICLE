package com.starnetsecurity.common.controller;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.Constant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @修改人：CM
 * @修改时间： 2015/4/9 11:29.
 */
public class BaseController {


    /**
     * 成功
     *
     * @param modelMap
     */
    public void success(Map modelMap) {
        modelMap.put("result", "0");
    }

    /**
     * 不可预期的错误
     *
     * @param modelMap
     */
    public void failed(Map modelMap) {
        modelMap.put("result", "-1");
        modelMap.put("msg", "服务器发生未知错误，请求失败!");
    }

    /**
     * 业务异常
     *
     * @param modelMap
     * @param e
     */
    public void failed(Map modelMap, BizException e) {
        modelMap.put("result", "-3");
        modelMap.put("msg", e.getMessage());
    }

    /**
     * 已知异常
     *
     * @param modelMap
     * @param errorMsg
     */
    public void failed(Map modelMap, String errorMsg) {
        modelMap.put("result", "-2");
        modelMap.put("msg", errorMsg);
    }


    public Object getUser(){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isRemembered() || subject.isAuthenticated()){
            return subject.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
        }else{
            return null;
        }
    }
}
