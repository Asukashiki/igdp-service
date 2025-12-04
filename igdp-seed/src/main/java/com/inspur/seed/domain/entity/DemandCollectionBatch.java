package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Demand Collection Batch Entity
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
@TableName("demand_collection_batch")
public class DemandCollectionBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Primary Key UUID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Batch Number
     */
    private String batchNo;

    /**
     * Batch Name
     */
    private String batchName;

    /**
     * Year
     */
    private Integer year;

    /**
     * Start Date
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /**
     * End Date
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * Status (collecting/reviewing/completed/locked)
     */
    private String status;

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

    /**
     * Version (Optimistic Lock)
     */
    @Version
    private Integer version;

    /**
     * Remark
     */
    private String remark;
}
