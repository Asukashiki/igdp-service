package com.inspur.data.treating.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.data.treating.domain.AssetsApplicationData;
import com.inspur.data.treating.domain.payload.ApplicationDataPayload;

import java.util.List;
import java.util.Map;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName IApplicationDataService
 * @date 2024/7/17 14:57
 */
public interface IAssetsApplicationDataService extends IService<AssetsApplicationData> {
    /**
     * 新增或者更新应用系统的数据
     * @param payload 应用系统数据信息
     * @return 结果
     * */
    AjaxResult saveApplicationData(ApplicationDataPayload payload);

    /**
     * 校验数据是否合理
     * @param payload 提交数据
     * @return 校验结果
     * */
    AjaxResult checkPayload(ApplicationDataPayload payload);

    /**
     * 用户访问量
     * @param
     * @return
     */
    List<AssetsApplicationData> getList(AssetsApplicationData payload);

    /**
     * 获取应用系统运行明细
     * @param payload
     * @return
     */
    List<AssetsApplicationData> getApplicationList(AssetsApplicationData payload);
    /**
     * 应用监控详情
     * @param
     * @return
     */
    Map<String, Object> getMonitoringDetails(AssetsApplicationData payload);
}
