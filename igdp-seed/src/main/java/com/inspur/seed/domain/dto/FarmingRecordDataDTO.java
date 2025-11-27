package com.inspur.seed.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 农事记录数据采集DTO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class FarmingRecordDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 管理措施(必填)
     */
    private String managementPractice;

    /**
     * 肥料类型
     */
    private String fertilizerType;

    /**
     * 肥料施用量(公斤)
     */
    private BigDecimal fertilizerRateKg;

    /**
     * 尿素施用量(公斤)
     */
    private BigDecimal ureaRateKg;

    /**
     * 农药类型
     */
    private String pesticideType;

    /**
     * 灌溉类型
     */
    private String irrigationType;

    /**
     * 灌溉频率
     */
    private Integer irrigationFrequency;

    /**
     * 除草日期
     */
    private Date weedingDate;

    /**
     * 除草剂使用
     */
    private String herbicideUsed;

    /**
     * 种子来源
     */
    private String seedSource;
}
