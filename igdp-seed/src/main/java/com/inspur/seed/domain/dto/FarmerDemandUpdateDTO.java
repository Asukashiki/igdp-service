package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * Farmer Demand Update DTO
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class FarmerDemandUpdateDTO {

    /**
     * Demand ID
     */
    @NotBlank(message = "Demand ID cannot be empty")
    private String id;

    /**
     * Version (Optimistic Lock)
     */
    private Integer version;

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
     * Zone Name
     */
    private String zoneName;

    /**
     * Woreda Name
     */
    private String woredaName;

    /**
     * Kebele Name
     */
    private String kebeleName;

    /**
     * Village
     */
    private String village;

    /**
     * Land Area (hectare)
     */
    private BigDecimal landArea;

    /**
     * Demand Entry Type (WHOLE_DEMAND/BY_FARMERS)
     */
    private String demandEntryType;

    /**
     * Remark
     */
    private String remark;

    /**
     * Year
     */
    private String year;

    private String status;

    /**
     * Input Items
     */
    @NotEmpty(message = "Input items cannot be empty")
    private List<InputItemDTO> inputItems;

    @Data
    public static class InputItemDTO {
        /**
         * Input Category (seed/fertilizer/pesticide)
         *
         *
         */
        private String id;


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
         * Season
         */
        private String season;

        /**
         * Crop Land (hectare)
         */
        private BigDecimal cropLand;

        /**
         * Fertilizer Amount
         */
        private double fertilizerAmount;
    }
}
