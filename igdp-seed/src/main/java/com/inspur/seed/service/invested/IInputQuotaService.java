package com.inspur.seed.service.invested;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.invested.InputQuota;

import java.util.List;

/**
 * 投入品配额Service接口
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
public interface IInputQuotaService extends IService<InputQuota> {

    /**
     * 查询投入品配额列表
     *
     * @param year 年度
     * @param adminOrg 行政机构
     * @param zone 区域
     * @param inputType 投入品类别
     * @param farmerId 农民ID
     * @param receiveStatus 领用状态
     * @return 配额列表
     */
    List<InputQuota> queryQuotaList(Integer year, String adminOrg, String zone,
                                     String inputType, String farmerId, String receiveStatus);

    /**
     * 根据ID查询配额详情
     *
     * @param id 配额ID
     * @return 配额详情
     */
    InputQuota queryById(String id);
}
