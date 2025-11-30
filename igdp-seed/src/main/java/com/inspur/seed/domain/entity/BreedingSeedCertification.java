package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 繁殖种子认证申请实体类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seed_breeding_seed_certification")
public class BreedingSeedCertification extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID(主键)
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String dataId;

    /**
     * 繁育批次ID
     */
    private String breedingBatchId;

    /**
     * 认证ID
     */
    private String authId;

    /**
     * 申请机构名称
     */
    private String applyOrgName;

    /**
     * 申请机构ID
     */
    private String applyOrgId;

    /**
     * 备案日期
     */
    private Date recordDate;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 备案状态
     */
    private String recordStatus;

    /**
     * 审核结果(pending-待审核 approved-已通过 rejected-已驳回)
     */
    private String auditResult;

    /**
     * 审核意见
     */
    private String auditComment;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核机构ID
     */
    private String auditorOrgId;

    /**
     * 审核机构名称
     */
    private String auditorOrgName;

    /**
     * 打印次数
     */
    private Integer printCount;

    /**
     * 最后打印时间
     */
    private Date lastPrintTime;

    /**
     * 删除标志(0正常 2删除)
     */
    private String delFlag;
}
