package com.inspur.agriculture.input.dto.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 入库单创建 DTO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class InboundOrderDTO {

    /** 入库类型：0-生产入库/1-采购入库/2-调拨入库 */
    @NotNull(message = "入库类型不能为空")
    private Integer inboundType;

    /** 入库仓库ID */
    @NotBlank(message = "入库仓库ID不能为空")
    private String warehouseId;

    /** 关联单号 */
    private String relatedOrderNo;

    /** 供应商类型 */
    private String supplierType;

    /** 供应商ID */
    private String supplierId;

    /** 入库员 */
    private String inboundUser;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /** 经办人 */
    @NotBlank(message = "经办人不能为空")
    private String operator;

    /** 备注 */
    private String remark;

    /** 入库明细列表 */
    @NotEmpty(message = "入库明细不能为空")
    @Valid
    private List<InboundDetail> details;

    /**
     * 入库明细静态内部类
     */
    @Data
    public static class InboundDetail {

        /** 投入品名称 */
        private String materialName;

        /** 投入品ID */
        @NotBlank(message = "投入品ID不能为空")
        private String materialId;

        /** 投入品批次ID */
        private String materialBatchId;

        /** 投入品类型 */
        @NotBlank(message = "投入品类型不能为空")
        private String materialType;

        /** 数量 */
        @NotNull(message = "数量不能为空")
        private BigDecimal quantity;

        /** 规格型号 */
        private String specModel;

        /** 计量单位 */
        private String unitOfMeasure;

        /** 过期日期 */
        @NotNull(message = "过期日期不能为空")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date expiryDate;

        private String qrCode;
    }
}
