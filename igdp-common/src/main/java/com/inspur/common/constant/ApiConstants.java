package com.inspur.common.constant;

/**
 * 内置服务接口常用变量
 * @author liyunlong
 * @version 1.0
 * @ClassName ApiConstants
 * @date 2024/6/10 9:50
 */
public class ApiConstants {


    /**低代码流程发起接口*/
    public final static String ICD_WORKFLOW_SUB_SUBMIT_INSTANCE = "/workflowService/instance/saveDataAndSubmitInstance";

    /*** 系统内置默认用户认证服务相关接口*/
    /**code换取token接口*/
    public final static String SSO_DEFAULT_TOKEN = "/oauth2/token";
    /**校验token接口*/
    public final static String SSO_DEFAULT_CHECK_TOKEN = "/oauth2/checkToken";
    /**刷新token*/
    public final static String SSO_DEFAULT_REFRESH_TOKEN = "/oauth2/refresh";
    /**获取当前登录用户信息接口*/
    public final static String SSO_DEFAULT_CURRENT_USER = "/user/userinfo";
    /**登出接口*/
    public final static String SSO_DEFAULT_LOGOUT = "/oauth2/logout";


    /**
     * 广东政法委相关接口
     * */
    public final static String GDZFW_SSO_TOKEN = "/portal-api/portal/public/sso/auth";
    public final static String GDZFW_SSO_USER_LIST = "/portal-api/portal/public/sso/user";
    public final static String GDZFW_SSO_USER = "/portal-api/portal/public/sso/user";
    public final static String GDZFW_SSO_DEPT = "/portal-api/portal/public/sso/org";
    public final static String GDZFW_SSO_DEPT_LIST = "/portal-api/portal/public/sso/org";

}
