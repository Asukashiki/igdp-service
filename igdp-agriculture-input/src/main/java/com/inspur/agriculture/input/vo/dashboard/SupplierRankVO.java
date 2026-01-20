package com.inspur.agriculture.input.vo.dashboard;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 供应商排行VO
 *
 * @author igdp
 */
@Data
public class SupplierRankVO {

    /** 供应商用户ID */
    private String userId;

    /** 供应商名称 */
    private String orgName;

    /** 认证状态: 0-待审核, 1-审核中, 2-已认证 */
    private Integer status;

    /** 认证状态描述 */
    private String statusDesc;

    /** 分发总量 */
    private BigDecimal totalDistributed;

    /** 分发单数 */
    private Long distributedCount;

    /** 产品数量 */
    private Long productCount;

    /** 联系人 */
    private String contactName;

    /** 联系电话 */
    private String contactPhone;

    /** 排名 */
    private Integer rank;
}
