package com.inspur.agriculture.inventory.domain.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StockCheckReviewReq {
    @NotBlank(message = "审核意见不能为空")
    private String reviewOpinion;
}
