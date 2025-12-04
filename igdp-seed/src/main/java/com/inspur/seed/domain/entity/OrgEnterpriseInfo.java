package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 机构基础信息实体类
 *
 * @author system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("org_enterprise_info")
public class OrgEnterpriseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键UUID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 机构名称
     */
    private String enterpriseName;

    /**
     * 企业注册号
     */
    private String enterpriseRegistrationId;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 种子企业许可证号
     */
    private String seedEnterpriseLicenseNumber;

    /**
     * 许可证有效期开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseValidityStart;

    /**
     * 许可证有效期结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseValidityEnd;

    /**
     * 企业类型
     */
    private String enterpriseType;

    /**
     * 机构类型(union/cooperative)
     */
    private String orgType;

    /**
     * 投入品类型(多选,逗号分隔:seed,fertilizer,pesticide)
     */
    private String inputTypes;

    /**
     * 销售区域(组织机构代码,多选,逗号分隔)
     */
    private String salesRegions;

    /**
     * 申请状态(draft/pending/approved/rejected)
     */
    private String applicationStatus;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    /**
     * 是否删除(0:否 1:是)
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号(乐观锁)
     */
    @Version
    private Integer version;

    /**
     * 备注
     */
    private String remark;
}
