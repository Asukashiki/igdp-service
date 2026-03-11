package com.inspur.agriculture.inventory.domain.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class StockCheckUpdateReq {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;

    private String checkRemark;

    @NotEmpty(message = "盘点明细不能为空")
    @Valid
    private List<DetailUpdateReq> details;

    @Data
    public static class DetailUpdateReq {
        @NotNull(message = "记录ID不能为空")
        private Long id;

        private BigDecimal actualQty;

        private String itemRemark;
    }
}
