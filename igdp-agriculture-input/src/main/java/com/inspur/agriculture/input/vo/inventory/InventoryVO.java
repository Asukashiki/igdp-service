package com.inspur.agriculture.input.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 库存 VO
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
public class InventoryVO {

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

    /** 库存状态 */
    private String stockStatus;

    /** 库存状态描述 */
    private String stockStatusDesc;

    /** 距离过期天数 */
    private Integer daysToExpire;

    /** 创建人 */
    private String createPeople;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改人 */
    private String updatePeople;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
