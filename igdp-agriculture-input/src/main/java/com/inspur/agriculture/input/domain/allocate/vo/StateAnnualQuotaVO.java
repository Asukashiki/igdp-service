package com.inspur.agriculture.input.domain.allocate.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * State Annual Quota VO
 * 州级年度配额VO
 */
@Data
public class StateAnnualQuotaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Quota ID
     */
    private String quotaId;

    /**
     * Quota name
     */
    private String quotaName;

    /**
     * Year
     */
    private Integer year;

    /**
     * Category ID
     */
    private String categoryId;

    /**
     * Category name
     */
    private String categoryName;

    /**
     * Total quota
     */
    private BigDecimal totalQuota;

    /**
     * Operator ID
     */
    private String operatorId;

    /**
     * Operator name
     */
    private String operatorName;

    /**
     * Operator division ID
     */
    private String operatorDivisionId;

    /**
     * Operator division name
     */
    private String operatorDivisionName;

    /**
     * Modifier ID
     */
    private String modifierId;

    /**
     * Modifier name
     */
    private String modifierName;

    /**
     * Modifier division name
     */
    private String modifierDivisionName;

    /**
     * Operate time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

    /**
     * Create time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * Update time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
