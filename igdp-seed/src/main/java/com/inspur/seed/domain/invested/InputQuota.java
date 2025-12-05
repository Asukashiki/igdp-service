package com.inspur.seed.domain.invested;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 投入品配额实体类
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@TableName("t_input_quota")
@Setter
@Getter
public class InputQuota extends BaseEntity {

    /**
     * 主键（UUID）
     */
    @TableId
    private String id;

    /**
     * 年度
     */
    private Integer year;

    /**
     * 行政机构
     */
    private String adminOrg;

    /**
     * 区域
     */
    private String zone;

    /**
     * 投入品类别（种子/化肥/农药）
     */
    private String inputType;

    /**
     * 投入品总量
     */
    private BigDecimal totalQuota;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 农民ID（单个农民查询时填充）
     */
    private String farmerId;

    /**
     * 农民姓名
     */
    private String farmerName;

    /**
     * 领用状态（未领用/部分领用/已领用）
     */
    private String receiveStatus;

    /**
     * 逻辑删除标识(0=未删除,1=已删除)
     */
    @TableLogic
    private Integer isDeleted;
}
