package com.inspur.agriculture.input.domain.allocation.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Zone Allocation VO
 * 区域分配额度VO
 */
@Data
public class AllocationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Allocation ID
     */
    private String id;

    /**
     * Allocation name
     */
    private String allocationName;

    /**
     * Allocation year
     */
    private String year;

    /**
     * Zone code
     */
    private String zone;

    /**
     * Zone name
     */
    private String zoneName;

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
