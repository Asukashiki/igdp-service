package com.inspur.agriculture.input.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 入库单 VO
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
public class StockInVO {

    /** 入库单号 */
    private String stockInId;

    /** 入库仓库ID */
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 批次号 */
    private String batchNo;

    /** 经办人 */
    private String operator;

    /** 供应商ID */
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 供应商联系人 */
    private String supplierContact;

    /** 供应商电话 */
    private String supplierPhone;

    /** 入库类型 */
    private String type;

    /** 类型描述 */
    private String typeDesc;

    /** 状态 */
    private String status;

    /** 状态描述 */
    private String statusDesc;

    /** 总数量 */
    private Integer totalQuantity;

    /** 二维码 */
    private String qrCode;

    /** 备注 */
    private String remarks;

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

    /** 入库商品明细列表 */
    private List<StockInItemVO> items;

    /**
     * 入库商品明细 VO
     */
    @Data
    public static class StockInItemVO {
        /** 入库商品明细ID */
        private String stockInItemId;

        /** 入库单号 */
        private String stockInId;

        /** 投入品ID */
        private Long inputId;

        /** 投入品名称 */
        private String inputName;

        /** 投入品SKU */
        private String inputSku;

        /** 仓库ID */
        private Long warehouseId;

        /** 仓库名称 */
        private String warehouseName;

        /** 入库数量 */
        private Integer quantity;

        /** 过期日期 */
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date expiryDate;

        /** 备注 */
        private String remarks;

        /** 生产批次 */
        private String productionBatchNo;

        /** 投入品品类 */
        private String agriculturalInputType;
    }
}
