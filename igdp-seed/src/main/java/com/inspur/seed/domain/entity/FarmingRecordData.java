package com.inspur.seed.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 农事记录数据采集实体类
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("seed_farming_record_data")
public class FarmingRecordData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID(主键)
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String dataId;

    /**
     * 管理措施
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

    /**
     * 删除标志(0正常 2删除)
     */
    private String delFlag;
}
