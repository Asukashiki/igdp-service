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
 * 鎶曞叆鍝佸垎鍙戞槑缁嗚〃瀹炰綋绫?
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@TableName("t_boa_zone_release_detail")
@Setter
@Getter
public class BoaZoneReleaseDetail extends BaseEntity {

    /**
     * 涓婚敭锛圲UID锛?
     */
    @TableId
    private String id;

    /**
     * 鍒嗗彂鏄庣粏缂栧彿锛堢郴缁熺敓鎴愶級
     */
    private String releaseDetailId;

    /**
     * 鍏宠仈鍒嗗彂鍗曠紪鍙?
     */
    private String releaseId;

    /**
     * 浣滅墿绉嶇被
     */
    private String cropType;

    /**
     * 鍝佺
     */
    private String variety;

    /**
     * 鎶曞叆鍝両D (宸插純鐢紝璇蜂娇鐢╥nputType鍜宨nputCategory杩涜鍖归厤)
     */
    private Long inputId;

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

    /**
     * 閫昏緫鍒犻櫎鏍囪瘑(0=鏈垹闄?1=宸插垹闄?
     */
    @TableLogic
    private Integer isDeleted;

//    private String season;
}

