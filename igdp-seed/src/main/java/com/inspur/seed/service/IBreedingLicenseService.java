package com.inspur.seed.service;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.BreedingLicenseDTO;
import com.inspur.seed.domain.dto.BreedingLicenseQueryDTO;

/**
 * 育种许可Service接口
 *
 * @author system
 * @since 2025-01-30
 */
public interface IBreedingLicenseService {

    /**
     * 获取许可列表(分页)
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    AjaxResult getLicenseList(BreedingLicenseQueryDTO queryDTO);

    /**
     * 根据ID获取许可详情(包含物种特性)
     *
     * @param id 许可ID
     * @return 许可详情
     */
    AjaxResult getLicenseById(String id);

    /**
     * 根据批次ID获取许可详情
     *
     * @param batchId 批次ID
     * @return 许可详情
     */
    AjaxResult getLicenseByBatchId(String batchId);

    /**
     * 新增许可(包含物种特性)
     *
     * @param dto 许可信息
     * @return 操作结果
     */
    AjaxResult addLicense(BreedingLicenseDTO dto);

    /**
     * 修改许可(包含物种特性)
     *
     * @param dto 许可信息
     * @return 操作结果
     */
    AjaxResult updateLicense(BreedingLicenseDTO dto);

    /**
     * 删除许可
     *
     * @param ids 许可ID数组
     * @return 操作结果
     */
    AjaxResult deleteLicense(String[] ids);
}
