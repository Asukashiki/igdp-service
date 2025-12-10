package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Farmer Demand Detail VO
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class FarmerDemandDetailVO {

    /**
     * Demand ID
     */
    private String id;

    /**
     * Batch ID
     */
    private String batchId;

    /**
     * Batch Number
     */
    private String batchNo;

    /**
     * Farmer ID
     */
    private String farmerId;

    /**
     * Farmer Name
     */
    private String farmerName;

    /**
     * Farmer ID Number
     */
    private String farmerIdNumber;

    /**
     * Region
     */
    private String region;

    /**
     * Zone
     */
    private String zone;

    /**
     * Woreda
     */
    private String woreda;

    /**
     * Kebele
     */
    private String kebele;

    /**
     * Village
     */
    private String village;

    /**
     * Land Area (hectare)
     */
    private BigDecimal landArea;

    /**
     * Max Seed Quantity (kg)
     */
    private BigDecimal maxSeedQuantity;

    /**
     * Max Fertilizer Quantity (kg)
     */
    private BigDecimal maxFertilizerQuantity;

    /**
     * Status
     */
    private String status;

    /**
     * Status Name
     */
    private String statusName;

    /**
     * Current Audit Level
     */
    private String currentAuditLevel;

    /**
     * Current Audit Level Name
     */
    private String currentAuditLevelName;

    /**
     * DA User Name
     */
    private String daUserName;

    /**
     * Submit Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /**
     * Version (Optimistic Lock)
     */
    private Integer version;

    /**
     * Remark
     */
    private String remark;

    /**
     * Created Time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * Input Items
     */
    private List<InputItemVO> inputItems;

    /**
     * Audit Records
     */
    private List<AuditRecordVO> auditRecords;

    @Data
    public static class InputItemVO {
        /**
         * Item ID
         */
        private String id;

        /**
         * Input Category
         */
        private String inputCategory;

        /**
         * Input Category Name
         */
        private String inputCategoryName;

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
    }

    @Data
    public static class AuditRecordVO {
        /**
         * Audit Record ID
         */
        private String id;

        /**
         * Audit Level
         */
        private String auditLevel;

        /**
         * Audit Level Name
         */
        private String auditLevelName;

        /**
         * Audit User Name
         */
        private String auditUserName;

        /**
         * Audit Time
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date auditTime;

        /**
         * Audit Action
         */
        private String auditAction;

        /**
         * Audit Result
         */
        private String auditResult;

        /**
         * Audit Opinion
         */
        private String auditOpinion;

        private String year;
    }
}
