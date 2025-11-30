package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 繁殖检测信息表实体类
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("breeding_test_info")
public class BreedingTestInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 检测编号（系统自动生成）
     */
    @TableField("test_id")
    private String testId;

    /**
     * 关联跟踪编号
     */
    @TableField("tracking_id")
    private String trackingId;

    /**
     * 关联繁殖批次编号
     */
    @TableField("batch_id")
    private String batchId;

    /**
     * 作物种类（枚举值）
     */
    @TableField("crop_type")
    private String cropType;

    /**
     * 发芽率（%）
     */
    @TableField("germination_rate")
    private BigDecimal germinationRate;

    /**
     * 纯度（%）
     */
    @TableField("purity")
    private BigDecimal purity;

    /**
     * 水分含量（%）
     */
    @TableField("moisture_content")
    private BigDecimal moistureContent;

    /**
     * 病虫害检测结果
     */
    @TableField("pest_detection")
    private String pestDetection;

    /**
     * 检测日期
     */
    @TableField("test_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date testDate;

    /**
     * 检测机构
     */
    @TableField("test_org")
    private String testOrg;

    /**
     * 检测人员
     */
    @TableField("test_person")
    private String testPerson;

    /**
     * 检测结论（01-合格 02-不合格 03-待复检）
     */
    @TableField("test_result")
    private String testResult;

    /**
     * 检测报告附件路径
     */
    @TableField("test_report_url")
    private String testReportUrl;

    /**
     * 操作机构ID
     */
    @TableField("org_id")
    private String orgId;

    /**
     * 操作机构名称
     */
    @TableField("org_name")
    private String orgName;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 删除标志（0-正常 2-删除）
     */
    @TableField("del_flag")
    @TableLogic(value = "0", delval = "2")
    private String delFlag;
}
