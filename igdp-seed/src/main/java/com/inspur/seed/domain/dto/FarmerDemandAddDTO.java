package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * Farmer Demand Add DTO
 *
 * @author igdp
 * @date 2025-12-04
 */
@Data
public class FarmerDemandAddDTO {

    /**
     * Batch ID (自动由后端根据年份设置)
     */
    private String batchId;

    /**
     * Farmer ID
     */
    @NotBlank(message = "Farmer ID cannot be empty")
    private String farmerId;

    /**
     * Farmer Name
     */
    @NotBlank(message = "Farmer name cannot be empty")
    private String farmerName;

    /**
     * Farmer ID Number
     */
    @NotBlank(message = "Farmer ID number cannot be empty")
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
    @NotBlank(message = "Woreda cannot be empty")
    private String woreda;

    /**
     * Kebele
     */
    @NotBlank(message = "Kebele cannot be empty")
    private String kebele;

    /**
     * Village
     */
    @NotBlank(message = "Village cannot be empty")
    private String village;

    /**
     * Land Area (hectare)
     */
    private BigDecimal landArea;

    /**
     * Remark
     */
    private String remark;

    /**
     * Input Items
     */
    @NotEmpty(message = "Input items cannot be empty")
    private List<InputItemDTO> inputItems;

    @Data
    public static class InputItemDTO {
        /**
         * Input Category (seed/fertilizer/pesticide)
         */
        @NotBlank(message = "Input category cannot be empty")
        private String inputCategory;

        /**
         * Input Type
         */
        @NotBlank(message = "Input type cannot be empty")
        private String inputType;

        /**
         * Variety
         */
        @NotBlank(message = "Variety cannot be empty")
        private String variety;

        /**
         * Specification
         */
        private String specification;

        /**
         * Unit
         */
        @NotBlank(message = "Unit cannot be empty")
        private String unit;

        /**
         * Quantity
         */
        @NotNull(message = "Quantity cannot be empty")
        private BigDecimal quantity;
    }
}
