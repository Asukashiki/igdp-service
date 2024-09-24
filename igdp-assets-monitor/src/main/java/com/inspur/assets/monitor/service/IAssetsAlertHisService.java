package com.inspur.assets.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.assets.monitor.domain.AssetsAlertHis;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName IAssetsAlertHisService
 * @date 2024/7/8 11:25
 */
public interface IAssetsAlertHisService extends IService<AssetsAlertHis>{
    /**
     * 获取历史告警信息列表
     * @param queryParam 查询条件
     * @return 集合
     * */
    List<AssetsAlertHis> getList(AssetsAlertHis queryParam);
    /**
     * 保存历史告警
     * 由活跃告警解除后，转为历史告警
     * @param alertHis 历史告警信息
     * */
    void saveAlertHis(AssetsAlertHis alertHis);
}
