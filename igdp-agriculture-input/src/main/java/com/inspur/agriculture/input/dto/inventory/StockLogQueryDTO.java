package com.inspur.agriculture.input.dto.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 库存日志查询 DTO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class StockLogQueryDTO {

    /** 仓库ID */
    private String warehouseId;

    /** 投入品ID */
    private String materialId;

    /** 操作类型：inbound-入库/outbound-出库 */
    private String operationType;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /** 页码 */
    private Integer page;

    /** 每页数量 */
    private Integer pageSize;
}
