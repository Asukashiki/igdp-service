package com.inspur.seed.breeding.agronomicTrait.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 农艺性状采集主记录DTO
 *
 * @author inspur
 */
@Data
public class AgronomicTraitRecordDTO {

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

    /** 业务状态: draft/submit/approve */
    private String status;

    /** 流程审核状态（字典flow_status） */
    private String workflowStatus;

    /** 审核人 */
    private String auditBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /** 性状明细列表 */
    private List<AgronomicTraitDetailDTO> detailList;
}
