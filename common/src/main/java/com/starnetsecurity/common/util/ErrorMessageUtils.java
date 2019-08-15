package com.starnetsecurity.common.util;

import com.starnetsecurity.common.model.WXErrorMsgModel;

/**
 * Created by Administrator on 2015/4/29.
 */
public class ErrorMessageUtils {


    /**
     * 获取错误信息（全局错误代码对应的信息）
     *
     * @param errorCode 错误代码
     * @return
     */
    public static WXErrorMsgModel getErrorMessageContent(Integer errorCode) {
        WXErrorMsgModel errorMsg = null;
        switch (errorCode) {
            case -1:
                errorMsg = new WXErrorMsgModel("系统繁忙，此时请开发者稍候再试", errorCode);
                break;
            case 0:
                errorMsg = new WXErrorMsgModel("请求成功", errorCode);
                break;
            case 40001:
                errorMsg = new WXErrorMsgModel("获取access_token时AppSecret错误，或者access_token无效。请开发者认真比对AppSecret的正确性，或查看是否正在为恰当的公众号调用接口", errorCode);
                break;
            case 40002:
                errorMsg = new WXErrorMsgModel("不合法的凭证类型", errorCode);
                break;
            case 40003:
                errorMsg = new WXErrorMsgModel("不合法的OpenID，请开发者确认OpenID（该用户）是否已关注公众号，或是否是其他公众号的OpenID", errorCode);
                break;
            case 40004:
                errorMsg = new WXErrorMsgModel("不合法的媒体文件类型", errorCode);
                break;
            case 40005:
                errorMsg = new WXErrorMsgModel("不合法的文件类型", errorCode);
                break;
            case 40006:
                errorMsg = new WXErrorMsgModel("不合法的文件大小", errorCode);
                break;
            case 40007:
                errorMsg = new WXErrorMsgModel("不合法的媒体文件id", errorCode);
                break;
            case 40008:
                errorMsg = new WXErrorMsgModel("不合法的消息类型", errorCode);
                break;
            case 40009:
                errorMsg = new WXErrorMsgModel("不合法的图片文件大小", errorCode);
                break;
            case 40010:
                errorMsg = new WXErrorMsgModel("不合法的语音文件大小", errorCode);
                break;
            case 40011:
                errorMsg = new WXErrorMsgModel("不合法的视频文件大小", errorCode);
                break;
            case 40012:
                errorMsg = new WXErrorMsgModel("不合法的缩略图文件大小", errorCode);
                break;
            case 40013:
                errorMsg = new WXErrorMsgModel("不合法的AppID，请开发者检查AppID的正确性，避免异常字符，注意大小写", errorCode);
                break;
            case 40014:
                errorMsg = new WXErrorMsgModel("不合法的access_token，请开发者认真比对access_token的有效性（如是否过期），或查看是否正在为恰当的公众号调用接口", errorCode);
                break;
            case 40015:
                errorMsg = new WXErrorMsgModel("不合法的菜单类型", errorCode);
                break;
            case 40016:
                errorMsg = new WXErrorMsgModel("不合法的按钮个数", errorCode);
                break;
            case 40017:
                errorMsg = new WXErrorMsgModel("不合法的按钮个数", errorCode);
                break;
            case 40018:
                errorMsg = new WXErrorMsgModel("不合法的按钮名字长度", errorCode);
                break;
            case 40019:
                errorMsg = new WXErrorMsgModel("不合法的按钮KEY长度", errorCode);
                break;
            case 40020:
                errorMsg = new WXErrorMsgModel("不合法的按钮URL长度", errorCode);
                break;
            case 40021:
                errorMsg = new WXErrorMsgModel("不合法的菜单版本号", errorCode);
                break;
            case 40022:
                errorMsg = new WXErrorMsgModel("不合法的子菜单级数", errorCode);
                break;
            case 40023:
                errorMsg = new WXErrorMsgModel("不合法的子菜单按钮个数", errorCode);
                break;
            case 40024:
                errorMsg = new WXErrorMsgModel("不合法的子菜单按钮类型", errorCode);
                break;
            case 40025:
                errorMsg = new WXErrorMsgModel("不合法的子菜单按钮名字长度", errorCode);
                break;
            case 40026:
                errorMsg = new WXErrorMsgModel("不合法的子菜单按钮KEY长度", errorCode);
                break;
            case 40027:
                errorMsg = new WXErrorMsgModel("不合法的子菜单按钮URL长度", errorCode);
                break;
            case 40028:
                errorMsg = new WXErrorMsgModel("不合法的自定义菜单使用用户", errorCode);
                break;
            case 40029:
                errorMsg = new WXErrorMsgModel("不合法的oauth_code", errorCode);
                break;
            case 40030:
                errorMsg = new WXErrorMsgModel("不合法的refresh_token", errorCode);
                break;
            case 40031:
                errorMsg = new WXErrorMsgModel("不合法的openid列表", errorCode);
                break;
            case 40032:
                errorMsg = new WXErrorMsgModel("不合法的openid列表长度", errorCode);
                break;
            case 40033:
                errorMsg = new WXErrorMsgModel("不合法的请求字符，不能包含\\uxxxx格式的字符", errorCode);
                break;
            case 40035:
                errorMsg = new WXErrorMsgModel("不合法的参数", errorCode);
                break;
            case 40038:
                errorMsg = new WXErrorMsgModel("不合法的请求格式", errorCode);
                break;
            case 40039:
                errorMsg = new WXErrorMsgModel("不合法的URL长度", errorCode);
                break;
            case 40050:
                errorMsg = new WXErrorMsgModel("不合法的分组id", errorCode);
                break;
            case 40051:
                errorMsg = new WXErrorMsgModel("分组名字不合法", errorCode);
                break;
            case 40117:
                errorMsg = new WXErrorMsgModel("分组名字不合法", errorCode);
                break;
            case 40118:
                errorMsg = new WXErrorMsgModel("media_id大小不合法", errorCode);
                break;
            case 40119:
                errorMsg = new WXErrorMsgModel("button类型错误", errorCode);
                break;
            case 40120:
                errorMsg = new WXErrorMsgModel("button类型错误", errorCode);
                break;
            case 40121:
                errorMsg = new WXErrorMsgModel("不合法的media_id类型", errorCode);
                break;
            case 41001:
                errorMsg = new WXErrorMsgModel("缺少access_token参数", errorCode);
                break;
            case 41002:
                errorMsg = new WXErrorMsgModel("缺少appid参数", errorCode);
                break;
            case 41003:
                errorMsg = new WXErrorMsgModel("缺少refresh_token参数", errorCode);
                break;
            case 41004:
                errorMsg = new WXErrorMsgModel("缺少secret参数", errorCode);
                break;
            case 41005:
                errorMsg = new WXErrorMsgModel("缺少多媒体文件数据", errorCode);
                break;
            case 41006:
                errorMsg = new WXErrorMsgModel("缺少media_id参数", errorCode);
                break;
            case 41007:
                errorMsg = new WXErrorMsgModel("缺少子菜单数据", errorCode);
                break;
            case 41008:
                errorMsg = new WXErrorMsgModel("缺少oauth code", errorCode);
                break;
            case 41009:
                errorMsg = new WXErrorMsgModel("缺少openid", errorCode);
                break;
            case 42001:
                errorMsg = new WXErrorMsgModel("access_token超时，请检查access_token的有效期，请参考基础支持-获取access_token中，对access_token的详细机制说明", errorCode);
                break;
            case 42002:
                errorMsg = new WXErrorMsgModel("refresh_token超时", errorCode);
                break;
            case 42003:
                errorMsg = new WXErrorMsgModel("oauth_code超时", errorCode);
                break;
            case 43001:
                errorMsg = new WXErrorMsgModel("需要GET请求", errorCode);
                break;
            case 43002:
                errorMsg = new WXErrorMsgModel("需要POST请求", errorCode);
                break;
            case 43003:
                errorMsg = new WXErrorMsgModel("需要HTTPS请求", errorCode);
                break;
            case 43004:
                errorMsg = new WXErrorMsgModel("需要接收者关注", errorCode);
                break;
            case 43005:
                errorMsg = new WXErrorMsgModel("需要好友关系", errorCode);
                break;
            case 44001:
                errorMsg = new WXErrorMsgModel("多媒体文件为空", errorCode);
                break;
            case 44002:
                errorMsg = new WXErrorMsgModel("POST的数据包为空", errorCode);
                break;
            case 44003:
                errorMsg = new WXErrorMsgModel("图文消息内容为空", errorCode);
                break;
            case 44004:
                errorMsg = new WXErrorMsgModel("文本消息内容为空", errorCode);
                break;
            case 45001:
                errorMsg = new WXErrorMsgModel("多媒体文件大小超过限制", errorCode);
                break;
            case 45002:
                errorMsg = new WXErrorMsgModel("消息内容超过限制", errorCode);
                break;
            case 45003:
                errorMsg = new WXErrorMsgModel("标题字段超过限制", errorCode);
                break;
            case 45004:
                errorMsg = new WXErrorMsgModel("描述字段超过限制", errorCode);
                break;
            case 45005:
                errorMsg = new WXErrorMsgModel("链接字段超过限制", errorCode);
                break;
            case 45006:
                errorMsg = new WXErrorMsgModel("图片链接字段超过限制", errorCode);
                break;
            case 45007:
                errorMsg = new WXErrorMsgModel("语音播放时间超过限制", errorCode);
                break;
            case 45008:
                errorMsg = new WXErrorMsgModel("图文消息超过限制", errorCode);
                break;
            case 45009:
                errorMsg = new WXErrorMsgModel("接口调用超过限制", errorCode);
                break;
            case 45010:
                errorMsg = new WXErrorMsgModel("创建菜单个数超过限制", errorCode);
                break;
            case 45015:
                errorMsg = new WXErrorMsgModel("回复时间超过限制", errorCode);
                break;
            case 45016:
                errorMsg = new WXErrorMsgModel("系统分组，不允许修改", errorCode);
                break;
            case 45017:
                errorMsg = new WXErrorMsgModel("分组名字过长", errorCode);
                break;
            case 45018:
                errorMsg = new WXErrorMsgModel("分组数量超过上限", errorCode);
                break;
            case 46001:
                errorMsg = new WXErrorMsgModel("不存在媒体数据", errorCode);
                break;
            case 46002:
                errorMsg = new WXErrorMsgModel("不存在的菜单版本", errorCode);
                break;
            case 46003:
                errorMsg = new WXErrorMsgModel("不存在的菜单数据", errorCode);
                break;
            case 46004:
                errorMsg = new WXErrorMsgModel("不存在的用户", errorCode);
                break;
            case 47001:
                errorMsg = new WXErrorMsgModel("解析JSON/XML内容错误", errorCode);
                break;
            case 48001:
                errorMsg = new WXErrorMsgModel("api功能未授权，请确认公众号已获得该接口，可以在公众平台官网-开发者中心页中查看接口权限", errorCode);
                break;
            case 50001:
                errorMsg = new WXErrorMsgModel("用户未授权该api", errorCode);
                break;
            case 61451:
                errorMsg = new WXErrorMsgModel("参数错误(invalid parameter)", errorCode);
                break;
            case 61452:
                errorMsg = new WXErrorMsgModel("无效客服账号(invalid kf_account)", errorCode);
                break;
            case 61453:
                errorMsg = new WXErrorMsgModel("客服帐号已存在(kf_account exsited)", errorCode);
                break;
            case 61454:
                errorMsg = new WXErrorMsgModel("客服帐号名长度超过限制(仅允许10个英文字符，不包括@及@后的公众号的微信号)(invalid kf_acount length)", errorCode);
                break;
            case 61455:
                errorMsg = new WXErrorMsgModel("客服帐号名包含非法字符(仅允许英文+数字)(illegal character in kf_account)", errorCode);
                break;
            case 61456:
                errorMsg = new WXErrorMsgModel("客服帐号个数超过限制(10个客服账号)(kf_account count exceeded)", errorCode);
                break;
            case 61457:
                errorMsg = new WXErrorMsgModel("无效头像文件类型(invalid file type)", errorCode);
                break;
            case 61450:
                errorMsg = new WXErrorMsgModel("系统错误(system error)", errorCode);
                break;
            case 61500:
                errorMsg = new WXErrorMsgModel("日期格式错误", errorCode);
                break;
            case 61501:
                errorMsg = new WXErrorMsgModel("日期范围错误", errorCode);
                break;
            case 9001001:
                errorMsg = new WXErrorMsgModel("POST数据参数不合法", errorCode);
                break;
            case 9001002:
                errorMsg = new WXErrorMsgModel("远端服务不可用", errorCode);
                break;
            case 9001003:
                errorMsg = new WXErrorMsgModel("Ticket不合法", errorCode);
                break;
            case 9001004:
                errorMsg = new WXErrorMsgModel("获取摇周边用户信息失败", errorCode);
                break;
            case 9001005:
                errorMsg = new WXErrorMsgModel("获取商户信息失败", errorCode);
                break;
            case 9001006:
                errorMsg = new WXErrorMsgModel("获取OpenID失败", errorCode);
                break;
            case 9001007:
                errorMsg = new WXErrorMsgModel("上传文件缺失", errorCode);
                break;
            case 9001008:
                errorMsg = new WXErrorMsgModel("上传素材的文件类型不合法", errorCode);
                break;
            case 9001009:
                errorMsg = new WXErrorMsgModel("上传素材的文件尺寸不合法", errorCode);
                break;
            case 9001010:
                errorMsg = new WXErrorMsgModel("上传失败", errorCode);
                break;
            case 9001020:
                errorMsg = new WXErrorMsgModel("帐号不合法", errorCode);
                break;
            case 9001021:
                errorMsg = new WXErrorMsgModel("已有设备激活率低于50%，不能新增设备", errorCode);
                break;
            case 9001022:
                errorMsg = new WXErrorMsgModel("设备申请数不合法，必须为大于0的数字", errorCode);
                break;
            case 9001023:
                errorMsg = new WXErrorMsgModel("已存在审核中的设备ID申请", errorCode);
                break;
            case 9001024:
                errorMsg = new WXErrorMsgModel("一次查询设备ID数量不能超过50", errorCode);
                break;
            case 9001025:
                errorMsg = new WXErrorMsgModel("设备ID不合法", errorCode);
                break;
            case 9001026:
                errorMsg = new WXErrorMsgModel("页面ID不合法", errorCode);
                break;
            case 9001027:
                errorMsg = new WXErrorMsgModel("页面参数不合法", errorCode);
                break;
            case 9001028:
                errorMsg = new WXErrorMsgModel("一次删除页面ID数量不能超过10", errorCode);
                break;
            case 9001029:
                errorMsg = new WXErrorMsgModel("页面已应用在设备中，请先解除应用关系再删除", errorCode);
                break;
            case 9001030:
                errorMsg = new WXErrorMsgModel("一次查询页面ID数量不能超过50", errorCode);
                break;
            case 9001031:
                errorMsg = new WXErrorMsgModel("时间区间不合法", errorCode);
                break;
            case 9001032:
                errorMsg = new WXErrorMsgModel("保存设备与页面的绑定关系参数错误", errorCode);
                break;
            case 9001033:
                errorMsg = new WXErrorMsgModel("门店ID不合法", errorCode);
                break;
            case 9001034:
                errorMsg = new WXErrorMsgModel("设备备注信息过长", errorCode);
                break;
            case 9001035:
                errorMsg = new WXErrorMsgModel("设备申请参数不合法", errorCode);
                break;
            case 9001036:
                errorMsg = new WXErrorMsgModel("查询起始值begin不合法", errorCode);
                break;
            default:
                errorMsg = new WXErrorMsgModel("未知接口异常", errorCode);
                break;
        }
        return errorMsg;
    }

}
