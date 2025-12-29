package com.inspur.seed.breeding.agronomicTrait.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 农艺性状采集主记录VO
 *
 * @author inspur
 */
@Data
public class AgronomicTraitRecordVO {

    /** 记录ID - {plot_id}-TR{序号} */
    private String recordId;

    /** 地块ID */
    private String plotId;

    /** 试验ID */
    private String trialId;

    /** 育种批次ID */
    private String batchId;

    /** 观测日期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date observationDate;

    /** 生长阶段 */
    private String growthStage;

    /** 观测员ID */
    private String observerId;

    /** 照片URL/文件ID */
    private String photoUrl;

    /** 备注 */
    private String remarks;

    /** 业务状态 */
    private String status;

    /** 流程审核状态 */
    private String workflowStatus;

    /** 审核人 */
    private String auditBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /** 性状明细列表 */
    private List<AgronomicTraitDetailVO> detailList;

    /** 性状数量 */
    private Integer traitCount;

    // ===== 用户名显示字段 =====
    /** 创建人名称 */
    private String createByName;

    /** 更新人名称 */
    private String updateByName;

    /** 审核人名称 */
    private String auditByName;

    /** 观测员名称 */
    private String observerName;
}
