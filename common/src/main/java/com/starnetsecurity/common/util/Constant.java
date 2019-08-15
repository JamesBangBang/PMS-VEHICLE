package com.starnetsecurity.common.util;

import java.util.Properties;

public class Constant {
    //应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
    public static final String SERVER_NAME = "http://www.s-findme.com/wx_mul/";
    public static final String WX_REQUEST_SCOPE_BASE = "snsapi_base";
    public static final String WX_REQUEST_SCOPE_USER_INFO = "snsapi_userinfo";
    //网络授权token
    public static final String REQUEST_SESSION_CURRENT_USER_TOKEN_NAME = "$_TOKEN";
    public static final String SESSION_BASE_TOKEN = "$_BASE_TOKEN";

    public static final String SESSION_CURRENT_USER_INFO_NAME = "$_WeChat_User";
    public static final String SESSION_LOGIN_USER = "$_LoginUser";
    public static final String SESSION_LOGIN_USER_TOKEN = "FRONT_USER_TOKEN";
    public static final String SESSION_PUBLIC_NUMBER = "$_PublicNumber";
    public static final String REQUEST_SESSION_BASE_TOKEN = "$_BASE_TOKEN";

    public static final String SESSION_FRONT_REQUEST_URI = "$_FRONT_REQUEST_URI";

    public static final String LANGUAGE_LANG_TYPE_ZH_CN = "zh_CN";
    public static final String LANGUAGE_LANG_TYPE_ZH_TW = "zh_TW";
    public static final String LANGUAGE_LANG_TYPE_EN = "en";

    public static final String GRANT_TYPE_CLIENT_CREDENTIAL = "client_credential";

    //素材类型 图片 视频 语音 图文
    public static final String CLIPS_TYPE_IMAGE = "image";
    public static final String CLIPS_TYPE_VIDEO = "video";
    public static final String CLIPS_TYPE_VOICE = "voice";
    public static final String CLIPS_TYPE_NEWS = "news";


    public static final int FUNCTION_TYPE_WITHOUT_FUNC = 0;
    public static final int FUNCTION_TYPE_CRYSTAL_SHOES = 1;


    public static final String FUNCTION_URL_CRYSTAL_SHOES = "/mobile/function/sjx";

    public static final String JS_API_TICKET = "jsApiTicket";

    public static final String DEFAULT_FILTER_PROCESSES_URL = "/register/autho2";
    public static final String DEFAULT_SERVER_REGISTER_URL = "/register/base";


    public static final String ERROR_VIEW_UNLOGIN = "/admin/login";
    public static final String ERROR_VIEW_BIZ = "/error/500.html";
    public static final String ERROR_VIEW_UNAUTHC = "/err/unauthorizated.html";
    public static final String ERROR_VIEW_404 = "/error/404.html";
    public static final String ERROR_VIEW_500 = "/error/500.html";


    private static Properties properties = null;

    static {
        properties = new PropertiesLoader("classpath:/config.properties").getProperties();
    }

    /**
     * 获取属性文件中的属性
     *
     * @param propertiesKey
     * @return
     */
    public static String getPropertie(String propertiesKey) {
        return properties.getProperty(propertiesKey);
    }


    public static Object setPropertie(String propertyKey, String propertyValue){
        return (Object)properties.setProperty(propertyKey, propertyValue);
    }
}
