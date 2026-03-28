package com.inspur.seed.domain.invested;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 投入品分发明细表实体类
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@TableName("t_input_release_detail")
@Setter
@Getter
public class InputReleaseDetail extends BaseEntity {

    /**
     * 主键（UUID）
     */
    @TableId
    private String id;

    /**
     * 分发明细编号（系统生成）
     */
    private String releaseDetailId;

    /**
     * 关联分发单编号
     */
    private String releaseId;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 品种
     */
    private String variety;

    /**
     * 投入品ID (已弃用，请使用inputType和inputCategory进行匹配)
     */
    private Long inputId;

    /**
     * 投入品类型
     */
    private String inputType;

    /**
     * 投入品类别
     */
    private String inputCategory;

    /**
     * 需求数量
     */
    private BigDecimal required;

    /**
     * 分发数量
     */
    private BigDecimal quantity;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * Outbound warehouse code
     */
    private String outWarehouseCode;

    /**
     * Outbound warehouse name
     */
    private String outWarehouseName;

    /**
     * Inbound warehouse code
     */
    private String inWarehouseCode;

    /**
     * Inbound warehouse name
     */
    private String inWarehouseName;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 分发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseTime;

    /**
     * 逻辑删除标识(0=未删除,1=已删除)
     */
    @TableLogic
    private Integer isDeleted;

//    private String season;
}
