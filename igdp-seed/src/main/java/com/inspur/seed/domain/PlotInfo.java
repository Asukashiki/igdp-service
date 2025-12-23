package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.time.LocalDateTime;

/**
 * 地块信息实体类
 *
 * @author inspur
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("plot_info")
public class PlotInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 地块ID(主键) - 格式: {trial_id}-P{replication_no}{row_no}{column_no} */
    @TableId(value = "plot_id", type = IdType.INPUT)
    private String plotId;

    /** 试验ID */
    @TableField("trial_id")
    private String trialId;

    /** 育种批次ID */
    @TableField("batch_id")
    private String batchId;

    /** 重复组编号 */
    @TableField("replication_no")
    private Integer replicationNo;

    /** 行号 */
    @TableField("row_no")
    private Integer rowNo;

    /** 列号 */
    @TableField("column_no")
    private Integer columnNo;

    /** 品种编码 */
    @TableField("variety_code")
    private String varietyCode;

    /** 播种日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("sowing_date")
    private Date sowingDate;

    /** 种子数量(kg) */
    @TableField("seed_quantity")
    private Double seedQuantity;

    /** 播种方式 */
    @TableField("sowing_method")
    private String sowingMethod;

    /** 播种时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("sowing_time")
    private LocalDateTime sowingTime;

    /** 地区 */
    @TableField("region")
    private String region;

    /** 区域 */
    @TableField("zone")
    private String zone;

    /** 县 */
    @TableField("woreda")
    private String woreda;

    /** 乡 */
    @TableField("kebele")
    private String kebele;

    /** 地块面积(平方米) */
    @TableField("plot_area_m2")
    private Double plotAreaM2;

    /** GPS纬度 */
    @TableField("gps_lat")
    private Double gpsLat;

    /** GPS经度 */
    @TableField("gps_long")
    private Double gpsLong;

    /** 审核状态(S0=草稿,S1=待审批,S2=已审批,S3=已退回,S9=已归档,S10=作废) */
    @TableField("workflow_status")
    private String auditStatus;

    /** 审核意见 */
    @TableField("audit_opinion")
    private String auditOpinion;

    /** 创建人ID */
    @TableField("created_by")
    private String createdBy;

    /** 创建人姓名 */
    @TableField("created_name")
    private String createdName;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private LocalDateTime createTime;

    /** 修改人ID */
    @TableField("modified_by")
    private String modifiedBy;

    /** 修改人姓名 */
    @TableField("modified_name")
    private String modifiedName;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private LocalDateTime updateTime;

    /** 审核人ID */
    @TableField("audited_by")
    private String auditedBy;

    /** 审核人姓名 */
    @TableField("audited_name")
    private String auditedName;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("audit_time")
    private LocalDateTime auditTime;

    /** 逻辑删除标识(0=未删除,1=已删除) */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /** 品种名称（关联字段，非数据库字段） */
    @TableField(exist = false)
    private String varietyName;

    /** 审核记录是否被作废（关联字段，非数据库字段） */
    @TableField(exist = false)
    private Integer auditCanceled;
}
