package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 农事记录实体类
 *
 * @author inspur
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("farming_record")
public class FarmingRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 农事记录ID(主键) */
    @TableId(value = "farming_id", type = IdType.ASSIGN_UUID)
    private String farmingId;

    /** 农事记录编号 */
    @TableField("farming_record_id")
    private String farmingRecordId;

    /** 地块ID */
    @TableField("plot_id")
    private String plotId;

    /** 试验ID */
    @TableField("trial_id")
    private String trialId;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 活动日期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("activity_date")
    private Date activityDate;

    /** 活动类型 */
    @TableField("activity_type")
    private String activityType;

    /** 投入品名称 */
    @TableField("input_name")
    private String inputName;

    /** 数量 */
    @TableField("quantity")
    private BigDecimal quantity;

    /** 单位 */
    @TableField("unit")
    private String unit;

    /** 操作员ID */
    @TableField("operator_id")
    private String operatorId;

    /** 操作描述 */
    @TableField("operation_desc")
    private String operationDesc;

    /** 工作流状态 */
    @TableField("workflow_status")
    private String workflowStatus;

    /** 审核人 */
    @TableField("audit_by")
    private String auditBy;

    /** 审核意见 */
    @TableField("audit_remark")
    private String auditRemark;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("audit_time")
    private Date auditTime;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /** 排除的状态列表（用于查询时排除特定状态，非数据库字段） */
    @TableField(exist = false)
    private String excludeStatuses;


    /**
     * 兼容前端字段：Creator/Modifier（首字母大写）
     * 直接映射 BaseEntity 的 createBy/updateBy
     */
    @com.fasterxml.jackson.annotation.JsonProperty("Creator")
    public String getCreatorAlias() {
        return this.getCreateBy();
    }

    @com.fasterxml.jackson.annotation.JsonProperty("Modifier")
    public String getModifierAlias() {
        return this.getUpdateBy();
    }

    // 兼容小写字段
    @com.fasterxml.jackson.annotation.JsonProperty("creator")
    public String getCreatorAliasLower() {
        return this.getCreateBy();
    }

    @com.fasterxml.jackson.annotation.JsonProperty("modifier")
    public String getModifierAliasLower() {
        return this.getUpdateBy();
    }
}
