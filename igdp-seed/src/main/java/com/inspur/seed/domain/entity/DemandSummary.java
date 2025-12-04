package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Demand Summary Entity
 * 需求汇总表
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
@TableName("demand_summary")
public class DemandSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键UUID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 批次ID(关联demand_collection_batch)
     */
    private String batchId;

    /**
     * 行政层级(kebele/woreda/zone/region/state)
     */
    private String adminLevel;

    /**
     * 行政区划代码
     */
    private String adminCode;

    /**
     * 行政区划名称
     */
    private String adminName;

    /**
     * 上级行政区划代码
     */
    private String parentAdminCode;

    /**
     * 投入品大类(seed/fertilizer/pesticide)
     */
    private String inputCategory;

    /**
     * 农资类型
     */
    private String inputType;

    /**
     * 品种
     */
    private String variety;

    /**
     * 汇总数量
     */
    private BigDecimal totalQuantity;

    /**
     * 农民数量
     */
    private Integer farmerCount;

    /**
     * 状态(pending/submitted/approved/rejected)
     */
    private String status;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人ID
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 是否删除(0:否 1:是)
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号(乐观锁)
     */
    @Version
    private Integer version;
}
