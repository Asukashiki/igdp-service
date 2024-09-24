package com.inspur.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.system.domain.SysOpenApp;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ISysOpenApp
 * @date 2024/6/14 15:12
 */
public interface ISysOpenAppService extends IService<SysOpenApp> {
    /**
     * 根据id获取app信息
     * @param appId id
     * @return 应用信息
     * */
    SysOpenApp getSysOpenApp(String appId);
    /**
     * 校验appid是否有效
     * @param appid 应用id
     * @return 结果
     * */
    AjaxResult checkByAppid(String appid);

    /**
     * 查询列表
     * @param sysOpenApp 查询条件
     * @return 列表
     * */
    List<SysOpenApp> getList(SysOpenApp sysOpenApp);
    /**
     * 新增
     * @param sysOpenApp 新增内容
     * @return 结果
     * */
    AjaxResult addApp(SysOpenApp sysOpenApp);
    /**
     * 修改
     * @param sysOpenApp 修改内容
     * @return 结果
     * */
    AjaxResult editApp(SysOpenApp sysOpenApp);

    /**
     * 修改状态
     * @param appid 应用id
     * @param status 目标状态
     * @return 结果
     * */
    boolean changeStatus(String appid,String status);

    /**
     * 修改锁定状态
     * @param appid 应用id
     * @param invokeStatus 目标状态
     * @return 结果
     * */
    boolean changeInvokeStatus(String appid,String invokeStatus);
    /**
     * 删除
     * @param appId 应用id
     * @return 结果
     * */
    boolean removeApp(String appId);
}
