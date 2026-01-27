package com.inspur.seed.multiplication.c1Seed.domain.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * C1繁殖批次查询DTO
 * @author system
 * @since 2025-12-08
 */
@Data
public class C1BreedingBatchQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 批次编号（模糊搜索）
     */
    private String batchId;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 批次状态
     */
    private String batchStatus;

    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 开始日期-起始
     */
    private String startDateBegin;

    /**
     * 开始日期-结束
     */
    private String startDateEnd;

    /**
     * 品种名称（模糊搜索）
     */
    private String varietyName;

    /**
     * 审核状态: pending-待审核, approved-已通过, rejected-已驳回
     */
    private String auditStatus;
}
