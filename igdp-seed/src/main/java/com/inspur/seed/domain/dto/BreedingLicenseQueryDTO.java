package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 育种许可查询DTO
 *
 * @author system
 * @since 2025-01-30
 */
@Data
public class BreedingLicenseQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 搜索关键词(许可证号、批次名称)
     */
    private String keyword;

    /**
     * 许可状态:valid/expired/revoked
     */
    private String licenseStatus;

    /**
     * 批次ID
     */
    private String batchId;

    /**
     * 审批开始时间
     */
    private String approvalDateStart;

    /**
     * 审批结束时间
     */
    private String approvalDateEnd;
}
