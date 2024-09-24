package com.inspur.ucif.service;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.spring.SpringUtils;

/**
 * @author liyunlong
 * @date 2024/4/2
 */
public interface IAppStrategy {

    String BASE_NAME = "AppStrategy";

    /**
     * 根据类型获取授权实现类
     * @param grantType 类型
     * @return IAuthStrategy
     * */
    default IAppStrategy getInstance(String grantType){
        String beanName = grantType + BASE_NAME;
        if (!SpringUtils.containsBean(beanName)) {
            throw new ServiceException("授权类型不正确!");
        }
        return SpringUtils.getBean(beanName);
    }

    /**
     * 根据AppId获取信息
     * @param appId appId
     * @return 结果内容
     * */
    AjaxResult getAppByAppId(String appId);
}
