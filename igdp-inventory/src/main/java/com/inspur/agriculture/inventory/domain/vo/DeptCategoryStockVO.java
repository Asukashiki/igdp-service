package com.inspur.agriculture.inventory.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeptCategoryStockVO {
    private String mainCategory;
    private String subCategory;
    private BigDecimal availableQty;
}
