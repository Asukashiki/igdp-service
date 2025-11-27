package com.inspur.seed.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 育种材料登记表实体类
 *
 * @author system
 */
@TableName("breeding_material")
@Setter
@Getter
public class BreedingMaterial extends BaseEntity {

    /**
     * 材料登记唯一标识
     */
    @TableId
    private String materialId;

    /**
     * 登记编码
     */
    private String registrationCode;

    /**
     * 育种批次ID
     */
    private String batchId;

    /**
     * 入库ID
     */
    private String warehouseInId;

    /**
     * 种子类别
     */
    private String seedType;

    /**
     * 数量(千克)
     */
    private BigDecimal quantity;

    /**
     * 来源实体
     */
    private String sourceEntity;

    /**
     * 接收日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate receiveDate;

    /**
     * 实验室检测报告路径
     */
    private String labTestReportUrl;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作机构
     */
    private String operationOrg;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;


    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
