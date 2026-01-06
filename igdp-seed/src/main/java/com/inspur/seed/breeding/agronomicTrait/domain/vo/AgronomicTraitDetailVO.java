package com.inspur.seed.breeding.agronomicTrait.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 农艺性状明细VO
 *
 * @author inspur
 */
@Data
public class AgronomicTraitDetailVO {

    /** 明细ID */
    private String detailId;

    /** 主记录ID */
    private String recordId;

    /** 性状代码 */
    private String traitCode;

    /** 性状名称 */
    private String traitName;

    /** 性状值 */
    private BigDecimal traitValue;

    /** 单位 */
    private String unit;

    /** 排序序号 */
    private Integer sortOrder;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
