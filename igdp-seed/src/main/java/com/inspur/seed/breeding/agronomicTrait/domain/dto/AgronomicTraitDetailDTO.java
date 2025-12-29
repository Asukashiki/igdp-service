package com.inspur.seed.breeding.agronomicTrait.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 农艺性状明细DTO
 *
 * @author inspur
 */
@Data
public class AgronomicTraitDetailDTO {

    /** 明细ID - {record_id}-D{序号} */
    private String detailId;

    /** 主记录ID */
    private String recordId;

    /** 性状代码(字典值) */
    private String traitCode;

    /** 性状名称(从字典获取) */
    private String traitName;

    /** 性状值 */
    private BigDecimal traitValue;

    /** 单位(从字典actual_value获取) */
    private String unit;

    /** 排序序号 */
    private Integer sortOrder;
}
