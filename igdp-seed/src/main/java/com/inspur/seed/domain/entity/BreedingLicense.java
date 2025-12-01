package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 育种许可信息表
 *
 * @author system
 * @since 2025-01-30
 */
@Data
@TableName("breeding_license")
public class BreedingLicense implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 育种批次ID
     */
    @TableField("batch_id")
    private String batchId;

    /**
     * 数据集ID
     */
    @TableField("dataset_id")
    private String datasetId;

    /**
     * 批次名称(冗余字段)
     */
    @TableField("batch_name")
    private String batchName;

    /**
     * 数据集编号(冗余字段)
     */
    @TableField("dataset_code")
    private String datasetCode;

    /**
     * 作物类型(冗余字段)
     */
    @TableField("crop_type")
    private String cropType;

    /**
     * 品种名称(冗余字段)
     */
    @TableField("variety_name")
    private String varietyName;

    /**
     * 许可证号
     */
    @TableField("license_no")
    private String licenseNo;

    /**
     * 审批机构
     */
    @TableField("approval_org")
    private String approvalOrg;

    /**
     * 批准日期
     */
    @TableField("approval_date")
    private LocalDate approvalDate;

    /**
     * 有效期开始日期
     */
    @TableField("valid_start_date")
    private LocalDate validStartDate;

    /**
     * 有效期结束日期
     */
    @TableField("valid_end_date")
    private LocalDate validEndDate;

    /**
     * 认证文件路径
     */
    @TableField("certificate_file")
    private String certificateFile;

    /**
     * 认证文件名称(原始文件名)
     */
    @TableField("certificate_file_name")
    private String certificateFileName;

    /**
     * 许可状态:valid/expired/revoked
     */
    @TableField("license_status")
    private String licenseStatus;

    /**
     * 状态:1有效0无效
     */
    @TableField("status")
    private String status;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建人姓名
     */
    @TableField("created_by_name")
    private String createdByName;

    /**
     * 创建机构代码
     */
    @TableField("created_org_code")
    private String createdOrgCode;

    /**
     * 创建机构名称
     */
    @TableField("created_org_name")
    private String createdOrgName;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 删除标记:0未删除1已删除
     */
    @TableField("deleted")
    private String deleted;
}
