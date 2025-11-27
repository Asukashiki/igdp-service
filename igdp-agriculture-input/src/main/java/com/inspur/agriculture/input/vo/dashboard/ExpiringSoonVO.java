package com.inspur.agriculture.input.vo.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 即将过期VO
 *
 * @author igdp
 */
@Data
public class ExpiringSoonVO {

    /** 库存记录ID */
    private String inventoryId;

    /** 投入品ID */
    private Long inputId;

    /** 投入品名称 */
    private String inputName;

    /** 投入品SKU */
    private String inputSku;

    /** 投入品类型 */
    private String inputType;

    /** 投入品类型描述 */
    private String inputTypeDesc;

    /** 批次号 */
    private String batchNo;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 当前库存数量 */
    private Integer currentQuantity;

    /** 入库日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inDate;

    /** 过期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiredDate;

    /** 距离过期天数 */
    private Integer daysToExpire;

    /** 库存状态: 0-正常/1-临期/2-过期 */
    private String stockStatus;

    /** 库存状态描述 */
    private String stockStatusDesc;

    /** 预警级别: 1-低/2-中/3-高 */
    private Integer warningLevel;

    /** 预警级别描述 */
    private String warningLevelDesc;
}
