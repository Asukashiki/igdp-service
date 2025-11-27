package com.inspur.agriculture.input.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 出库单 VO
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
public class StockOutVO {

    /** 出库单号 */
    private String stockOutId;

    /** 出库仓库ID */
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 批次号 */
    private String batchNo;

    /** 经办人 */
    private String operator;

    /** 出库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outTime;

    /** 客户 */
    private String customer;

    /** 出库类型 */
    private String type;

    /** 类型描述 */
    private String typeDesc;

    /** 状态 */
    private String status;

    /** 状态描述 */
    private String statusDesc;

    /** 总数量 */
    private Integer totalQuantity;

    /** 备注 */
    private String remark;

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

    /** 出库商品明细列表 */
    private List<StockOutItemVO> items;

    /**
     * 出库商品明细 VO
     */
    @Data
    public static class StockOutItemVO {
        /** 出库商品明细ID */
        private String stockOutItemId;

        /** 出库单号 */
        private String stockOutId;

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

        /** 批次号 */
        private String batchNo;

        /** 出库数量 */
        private Integer quantity;

        /** 备注 */
        private String remarks;
    }
}
