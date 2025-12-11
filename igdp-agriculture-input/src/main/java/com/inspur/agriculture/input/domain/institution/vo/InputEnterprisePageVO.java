package com.inspur.agriculture.input.domain.institution.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 机构注册申请分页VO
 *
 * @author system
 */
@Data
public class InputEnterprisePageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机构ID
     */
    private String id;

    /**
     * 机构名称
     */
    private String enterpriseName;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 机构类型名称
     */
    private String orgTypeName;

    /**
     * 种子企业许可证号
     */
    private String seedEnterpriseLicenseNumber;

    /**
     * 投入品类型
     */
    private String inputTypes;

    /**
     * 申请状态
     */
    private String applicationStatus;

    /**
     * 申请状态名称
     */
    private String applicationStatusName;

    /**
     * Woreda
     */
    private String woreda;

    /**
     * Zone
     */
    private String zone;

    /**
     * 版本号(乐观锁)
     */
    private Integer version;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
}
