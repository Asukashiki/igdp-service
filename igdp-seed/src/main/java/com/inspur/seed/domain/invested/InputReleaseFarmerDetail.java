package com.inspur.seed.domain.invested;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 农民分发明细表实体
 * 对应表：t_input_release_farmer_detail
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@TableName("t_input_release_farmer_detail")
@Setter
@Getter
public class InputReleaseFarmerDetail extends BaseEntity {

    /**
     * 主键（UUID）
     */
    @TableId
    private String id;

    /**
     * 明细编号（系统生成，格式：FDT+yyyyMMdd+6位随机码）
     */
    private String releaseDetailId;

    /**
     * 关联分发单编号
     */
    private String releaseId;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 品种
     */
    private String variety;

    /**
     * 投入品类型
     */
    private String inputType;

    /**
     * 投入品类别
     */
    private String inputCategory;

    /**
     * 投入品ID
     */
    private String inputId;

    /**
     * 仓库ID
     */
    private String warehouseId;

    /**
     * 投入品批次ID
     */
    private String batchId;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 分发数量
     */
    private BigDecimal quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 分发时间
     */
    private LocalDateTime releaseTime;

    /**
     * 逻辑删除标识(0=未删除,1=已删除)
     */
    @TableLogic
    private Integer isDeleted;
}
