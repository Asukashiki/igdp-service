package com.inspur.seed.multiplication.c1Seed.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C1种子繁殖申请查询DTO
 *
 * @author system
 * @since 2025-12-08
 */
@Data
public class C1SeedPropagationQueryDTO implements Serializable {

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
     * 搜索关键词（申请机构名称、品种名称）
     */
    private String keyword;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 申请状态：pending-待审核，approved-通过，rejected-拒绝
     */
    private String applyStatus;

    /**
     * 申请机构名称
     */
    private String applicantOrgName;

    /**
     * 查询开始时间
     */
    private String queryDateStart;

    /**
     * 查询结束时间
     */
    private String queryDateEnd;
}
