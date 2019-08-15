package com.starnetsecurity.common.exception;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.model.WXErrorMsgModel;
import com.starnetsecurity.common.util.ErrorMessageUtils;

/**
 * Created by Administrator on 2015/4/22.
 * 业务异常类 不进入Exception
 */
public class BizException extends RuntimeException {
    public BizException() {
        super();
    }


    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    protected BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public BizException(WXErrorMsgModel model) {
        super(model.toString());
    }

    /**
     * 根据微信错误消息代码获取对应的错误消息
     *
     * @param obj
     */
    public BizException(JSONObject obj) {
        super(ErrorMessageUtils.getErrorMessageContent(obj.getInteger("errcode")).toString());
    }

}
