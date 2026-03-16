package com.inspur.seed.dto.invested;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 投入品分发明细DTO
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Setter
@Getter
public class InputReleaseDetailDTO {

    /**
     * 主键（编辑时必填）
     */
    private String id;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 品种
     */
    private String variety;

    /**
     * 投入品ID
     */
    private String inputId;

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
}
