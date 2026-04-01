package com.inspur.seed.dto.invested;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 鎶曞叆鍝佸垎鍙戞槑缁咲TO
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Setter
@Getter
public class BoaZoneReleaseDetailDTO {

    /**
     * 涓婚敭锛堢紪杈戞椂蹇呭～锛?
     */
    private String id;

    /**
     * 浣滅墿绉嶇被
     */
    private String cropType;

    /**
     * 鍝佺
     */
    private String variety;

    /**
     * 鎶曞叆鍝両D
     */
    private String inputId;

    /**
     * 鎶曞叆鍝佺被鍨?
     */
    private String inputType;

    /**
     * 鎶曞叆鍝佺被鍒?
     */
    private String inputCategory;

    /**
     * 闇€姹傛暟閲?
     */
    private BigDecimal required;

    /**
     * 鍒嗗彂鏁伴噺
     */
    private BigDecimal quantity;

    /**
     * 璁￠噺鍗曚綅
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
     * 鍗曚环
     */
    private BigDecimal unitPrice;

    /**
     * 鍒嗗彂鏃堕棿
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseTime;
}

