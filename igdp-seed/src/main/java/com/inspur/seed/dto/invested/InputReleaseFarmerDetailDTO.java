package com.inspur.seed.dto.invested;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 农民分发明细DTO
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Setter
@Getter
public class InputReleaseFarmerDetailDTO {

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
     * 仓库ID
     */
    private String warehouseId;

    /**
     * 投入品批次ID
     */
    private String batchId;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 分发数量
     */
    private BigDecimal quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 分发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseTime;
}
