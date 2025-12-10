package com.inspur.agriculture.input.domain.institution.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 机构注册申请分页查询DTO
 *
 * @author system
 */
@Data
public class InputEnterprisePageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 机构名称(模糊查询)
     */
    private String enterpriseName;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 申请状态
     */
    private String applicationStatus;

    /**
     * 投入品类型
     */
    private String inputTypes;

    /**
     * Woreda
     */
    private String woreda;

    /**
     * Zone
     */
    private String zone;

    /**
     * 创建时间开始
     */
    private LocalDateTime createdTimeStart;

    /**
     * 创建时间结束
     */
    private LocalDateTime createdTimeEnd;
}
