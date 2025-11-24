package com.inspur.farmland.management.bean.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 农民认证实体类
 * 
 * @author inspur
 */
@Data
@TableName("farmer_certification")
public class FarmerCertification implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 认证ID */
    @TableId(value = "CERT_ID", type = IdType.AUTO)
    private Long certId;

    /** 用户ID */
    @TableField("USER_ID")
    private Long userId;

    /** 真实姓名 */
    @TableField("REAL_NAME")
    private String realName;

    /** 身份证号 */
    @TableField("ID_CARD")
    private String idCard;

    /** 行政区划代码 */
    @TableField("AD_CODE")
    private String adCode;

    /** 种植类型 */
    @TableField("FARM_TYPE")
    private String farmType;

    /** 证明文件存储路径 */
    @TableField("CERT_DOC_PATH")
    private String certDocPath;

    /** 详细住址 */
    @TableField("DETAIL_ADDRESS")
    private String detailAddress;

    /** 申请时间 */
    @TableField("APPLY_TIME")
    private Date applyTime;

    /** 认证状态 (1:审核中, 2:已通过, 0:未通过) */
    @TableField("STATUS")
    private Integer status;

    /** 审批人ID */
    @TableField("APPROVER_ID")
    private Long approverId;

    /** 审批时间 */
    @TableField("APPROVE_TIME")
    private Date approveTime;

    /** 驳回原因 */
    @TableField("REJECT_REASON")
    private String rejectReason;
}