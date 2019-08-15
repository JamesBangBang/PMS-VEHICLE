package com.starnetsecurity.common.model;

/**
 * Created by Administrator on 2015/4/29.
 */
public class WXErrorMsgModel {
    private String errorMsg;
    private Integer errorCode;

    public WXErrorMsgModel(String errorMsg, Integer errorCode) {
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public WXErrorMsgModel() {
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "API接口请求错误：" + errorMsg + '；' +
                "错误代码：" + errorCode     ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WXErrorMsgModel that = (WXErrorMsgModel) o;

        if (errorCode != null ? !errorCode.equals(that.errorCode) : that.errorCode != null) return false;
        if (errorMsg != null ? !errorMsg.equals(that.errorMsg) : that.errorMsg != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = errorMsg != null ? errorMsg.hashCode() : 0;
        result = 31 * result + (errorCode != null ? errorCode.hashCode() : 0);
        return result;
    }
}
