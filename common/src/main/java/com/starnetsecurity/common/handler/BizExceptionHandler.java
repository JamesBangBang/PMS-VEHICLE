package com.starnetsecurity.common.handler;

import com.alibaba.fastjson.JSON;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.exception.TokenInvalidException;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.exception.TokenInvalidException;
import com.starnetsecurity.common.util.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一异常处理器
 *
 * @修改时间： 2015/2/26 10:41.
 */
public class BizExceptionHandler implements HandlerExceptionResolver {
    private static Logger LOGGER = LoggerFactory.getLogger(BizExceptionHandler.class);
    @Autowired
    HttpSession session;

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        PrintWriter out = null;

        if (isAjax(request)) {

            LOGGER.error("ajax request occur error : {} ,request param : {} ",
                    ex.getMessage(), JSON.toJSONString(request.getParameterMap()));

            try {
                response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                out = response.getWriter();
                Map data = new HashMap();
                String errMsg;

                if (ex instanceof BizException) {
                    data.put("result", "-3");
                    errMsg = ex.getMessage();
                } else if (ex instanceof TokenInvalidException) {
                    data.put("result", "-2");
                    errMsg = "身份验证已经失效，请重新登录!";
                } else {
                    data.put("result", "-1");
                    errMsg = "服务器发生错误，请稍后再试！";
                }
                data.put("msg", errMsg);
                out.write(JSON.toJSONString(data));
            } catch (IOException e) {
                LOGGER.error("get response writer failed!", ex);
            } finally {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            }
            return null;
        } else {
            LOGGER.error("Normal request occur error : {} ,request param : {}",
                    StringUtils.isNotEmpty(ex.getMessage()) ? ex.getMessage() : ex.getClass().getSimpleName()
                    , JSON.toJSONString(request.getParameterMap()));
            String view = getErrorView(ex, request);

            return new ModelAndView(view);
        }
    }

    /**
     * 根据异常类型返回异常视图
     *
     * @param ex
     * @param request
     * @return
     */
    private String getErrorView(Throwable ex, HttpServletRequest request) {
        String viewUri = "forward:";

        if (ex instanceof TokenInvalidException) {

            request.setAttribute("result", "-2");
            request.setAttribute("msg", "身份验证已经超时，请重新登录！");
            LOGGER.error("身份验证已经过期:", ex);
            viewUri += Constant.ERROR_VIEW_500;
        } else if (ex instanceof BizException) {

            request.setAttribute("result", "-3");
            request.setAttribute("msg", ex.getMessage());
            LOGGER.error("发生业务异常:", ex.getMessage());
            viewUri += Constant.ERROR_VIEW_BIZ;

        } else if (ex instanceof ServletException) {

            if (ex.getMessage().contains("No adapter for handler")) {
                request.setAttribute("result", "-1");
                request.setAttribute("msg", "您请求的页面不存在。");
                LOGGER.error("发生不可预期的异常:", ex);
                viewUri += Constant.ERROR_VIEW_404;
            } else {
                request.setAttribute("result", "-1");
                request.setAttribute("msg", "服务器发生了未知的错误，请联系管理人员解决！");
                LOGGER.error("发生不可预期的异常:", ex);
                viewUri += Constant.ERROR_VIEW_500;

            }

        } else {

            request.setAttribute("result", "-1");
            request.setAttribute("msg", "服务器发生错误，请联系管理人员解决！");
            LOGGER.error("发生不可预期的异常:", ex);
            viewUri += Constant.ERROR_VIEW_500;

        }

        LOGGER.info("Error Type : {} , Error View : {}", ex.getClass(), viewUri);
        return viewUri;
    }

    /**
     * 判定是否为ajax请求
     *
     * @param request
     * @return
     */
    boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
    }

}
