package com.inspur.ucif.service;

import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.spring.SpringUtils;

/**
 * 部门以及用户信息同步功能
 * @author liyunlong
 * @version 1.0
 * @ClassName IDeptUserAsyncStrategy
 * @date 2024/5/30 9:35
 */
public interface IAccountSyncStrategy {
    String BASE_NAME = "AccountStrategy";

    /**
     * 根据类型获取授权实现类
     * @param grantType 类型
     * @return IAuthStrategy
     * */
    default IAccountStrategy getInstance(String grantType){
        String beanName = grantType + BASE_NAME;
        if (!SpringUtils.containsBean(beanName)) {
            throw new ServiceException("授权类型不正确!");
        }
        return SpringUtils.getBean(beanName);
    }

    /**
     * 同步部门信息
     * */
    void asyncDept();

    /**
     * 同步用户信息
     * */
    void asyncUser();

    /**
     * 同步单个用户信息
     * @param username 用户名
     * */
    void syncUserByUsername(String username);
    /**
     * 同步单个部门信息
     * @param deptId 部门id
     * */
    void syncDeptByDeptId(String deptId);
}
