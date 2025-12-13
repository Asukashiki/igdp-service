package com.inspur.agriculture.input.dto.inventory;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 出库单创建 DTO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class OutboundOrderDTO {

    /** 出库类型：1-销售出库/2-调拨出库 */
    @NotNull(message = "出库类型不能为空")
    private Integer outboundType;

    /** 出库仓库ID */
    @NotBlank(message = "出库仓库ID不能为空")
    private String warehouseId;

    /** 关联单号 */
    private String relatedOrderNo;

    /** 出库对象ID */
    @NotBlank(message = "出库对象ID不能为空")
    private String outboundObjectId;

    private String outboundObjectName;

    /** 出库员 */
    private String outboundUser;

    /** 出库部门 */
    private String outboundDept;

    /** 经办人 */
    @NotBlank(message = "经办人不能为空")
    private String operator;

    /** 备注 */
    private String remark;

    /** 出库明细列表 */
    @NotEmpty(message = "出库明细不能为空")
    @Valid
    private List<OutboundDetail> details;

    /**
     * 出库明细静态内部类
     */
    @Data
    public static class OutboundDetail {

        /** 投入品ID */
        @NotBlank(message = "投入品ID不能为空")
        private String materialId;

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

        private String materialName;

        private String materialBatchId;

        private String agriculturalInputType;
    }
}
