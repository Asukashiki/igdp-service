package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 育种数据集审核查询DTO
 *
 * @author system
 * @since 2025-01-30
 */
@Data
public class BreedingDatasetAuditQueryDTO implements Serializable {

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
     * 搜索关键词(数据集编号、批次名称、品种名称)
     */
    private String keyword;

    /**
     * 审核状态:pending/approved/rejected
     */
    private String auditStatus;

    /**
     * 数据集状态:submitted/reviewing
     */
    private String datasetStatus;

    /**
     * 批次ID
     */
    private String batchId;

    /**
     * 提交开始时间
     */
    private String submitTimeStart;

    /**
     * 提交结束时间
     */
    private String submitTimeEnd;
}
