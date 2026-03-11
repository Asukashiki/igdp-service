package com.inspur.agriculture.inventory.domain.req;

import lombok.Data;

@Data
public class StockCheckListQuery {
    /** 筛选仓库 */
    private String warehouseId;

    /** 状态枚举，多个用逗号分隔 */
    private String checkStatus;

    /** 盘点日期起 YYYY-MM-DD */
    private String startDate;

    /** 盘点日期止 YYYY-MM-DD */
    private String endDate;

    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
