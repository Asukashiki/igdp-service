package com.inspur.seed.multiplication.c1Seed.service;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.multiplication.c1Seed.domain.dto.AvailableBasicSeedQueryDTO;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1SeedPropagationDTO;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1SeedPropagationQueryDTO;

/**
 * C1种子繁殖申请Service接口
 *
 * @author system
 * @since 2025-12-08
 */
public interface IC1SeedPropagationService {

    /**
     * 获取申请列表（分页）
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    AjaxResult getList(C1SeedPropagationQueryDTO queryDTO);

    /**
     * 根据ID获取申请详情
     *
     * @param id 申请ID
     * @return 申请详情
     */
    AjaxResult getById(String id);

    /**
     * 新增申请
     *
     * @param dto 申请信息
     * @return 操作结果
     */
    AjaxResult add(C1SeedPropagationDTO dto);

    /**
     * 修改申请
     *
     * @param dto 申请信息
     * @return 操作结果
     */
    AjaxResult update(C1SeedPropagationDTO dto);

    /**
     * 删除申请
     *
     * @param ids 申请ID数组
     * @return 操作结果
     */
    AjaxResult delete(String[] ids);

    /**
     * 提交审核
     *
     * @param dto 审核信息
     * @return 操作结果
     */
    AjaxResult audit(C1SeedPropagationDTO dto);

    /**
     * 获取待审核列表（分页）
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    AjaxResult getPendingList(C1SeedPropagationQueryDTO queryDTO);

    /**
     * 获取已审核通过且未使用的申请列表（供批次采集选择）
     *
     * @return 列表结果
     */
    AjaxResult getApprovedList();

    /**
     * 获取可用的Basic种子列表
     * 聚合OSE接收确认和批次采集两个数据源
     *
     * @param queryDTO 查询条件
     * @return 可用种子列表
     */
    AjaxResult getAvailableBasicSeeds(AvailableBasicSeedQueryDTO queryDTO);

    /**
     * 获取指定批次的可用数量
     *
     * @param batchId 批次ID
     * @param sourceType 数据来源类型 (OSE_RECEIVE/OSE_BATCH_COLLECTION)
     * @return 可用数量信息
     */
    AjaxResult getAvailableQuantity(String batchId, String sourceType);
}
