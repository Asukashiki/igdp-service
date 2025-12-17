package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Demand Farmer Input Item Entity
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
@TableName("demand_farmer_input_item")
public class DemandFarmerInputItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Primary Key UUID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Demand ID
     */
    private String demandId;

    /**
     * Input Category (seed/fertilizer/pesticide)
     */
    private String inputCategory;

    /**
     * Input Type
     */
    private String inputType;

    /**
     * Variety
     */
    private String variety;

    /**
     * Specification
     */
    private String specification;

    /**
     * Unit
     */
    private String unit;

    /**
     * Quantity
     */
    private BigDecimal quantity;

    /**
     * Created By User ID
     */
    private String createdBy;

    /**
     * Created Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * Updated By User ID
     */
    private String updatedBy;

    /**
     * Updated Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    /**
     * Is Deleted (0: No, 1: Yes)
     */
    @TableLogic
    private Integer isDeleted;

    private String season;

    private String cropLand;

    private String fertilizerAmount;
}
