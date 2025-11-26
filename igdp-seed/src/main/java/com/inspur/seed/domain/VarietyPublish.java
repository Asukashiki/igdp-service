package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 品种发布记录实体类
 *
 * @author system
 */
@TableName("variety_publish")
@Setter
@Getter
public class VarietyPublish extends BaseEntity {

    /**
     * 发布记录唯一标识（主键，系统生成）
     */
    @TableId
    private String publishId;

    /**
     * 发布编号（格式：PUB+年月日+000001）
     */
    private String publishNo;

    /**
     * 关联登记申请唯一标识
     */
    private String registrationId;

    /**
     * 品种名称（冗余）
     */
    private String varietyName;

    /**
     * 作物类型（冗余）
     */
    private String cropType;

    /**
     * 发布日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    /**
     * 发布主管部门
     */
    private String publishDept;

    /**
     * 决策说明
     */
    private String decisionExplanation;

    /**
     * 公开描述
     */
    private String publicDescription;

    /**
     * 推荐地区
     */
    private String recommendedRegion;

    /**
     * 播种指南
     */
    private String sowingGuide;

    /**
     * 公示状态（1-公示中/2-已下架）
     */
    private Integer publishStatus;

    /**
     * 发布人
     */
    private String publisher;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
}
