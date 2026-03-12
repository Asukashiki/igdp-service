package com.inspur.agriculture.inventory.domain.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class StockCheckCreateReq {

    @NotNull(message = "盘点日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;

    @NotBlank(message = "盘点仓库ID不能为空")
    private String warehouseId;

    private String checkRemark;

    @NotEmpty(message = "盘点明细不能为空")
    @Valid
    private List<DetailReq> details;

    @Data
    public static class DetailReq {
        @NotBlank(message = "商品ID不能为空")
        private String productId;

        @NotBlank(message = "批次号不能为空")
        private String batchNo;

        @NotNull(message = "系统库存不能为空")
        private BigDecimal systemQty;

        // 新建时可为空，提交时业务上再校验
        private BigDecimal actualQty;

        @NotBlank(message = "单位不能为空")
        private String unit;

        private String itemRemark;

        private String mainCategory;

        private String subCategory;
    }
}
