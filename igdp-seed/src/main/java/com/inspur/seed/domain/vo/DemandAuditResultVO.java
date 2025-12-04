package com.inspur.seed.domain.vo;

import lombok.Data;

/**
 * Demand Audit Result VO
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class DemandAuditResultVO {

    /**
     * Success Count
     */
    private Integer successCount;

    /**
     * Fail Count
     */
    private Integer failCount;
}
