package com.inspur.seed.vo.breed;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Breeder Seed 分发数据VO
 *
 * @author igdp
 */
@Data
public class BreedSeedDistributeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分发ID
     */
    private String distributeId;
    /**
     * 分发名称
     */
    private String distributeName;

    /**
     * 来源种子等级
     */
    private String fromSeedLevel;

    /**
     * 目标种子等级
     */
    private String toSeedLevel;

    /**
     * OSE ID
     */
    private String oseId;

    /**
     * OSE名称
     */
    private String oseName;

    /**
     * 分发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 分发操作人姓名
     */
    private String people;

    /**
     * 种子机构名称
     */
    private String organ;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 明细数量总和
     */
    private BigDecimal totalDistributeQuantity;

    /**
     * 分发状态
     */
    private String distributeStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 分发明细列表
     */
    private List<DistributeDetailVO> detailList;

    @Data
    public static class DistributeDetailVO implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 分发明细ID
         */
        private String distributeDetailId;

        /**
         * 生产批次ID
         */
        private String produceBatchId;

        /**
         * 生产批次名称
         */
        private String produceBatchName;

        /**
         * 品种ID
         */
        private String breedBatchName;


        private String parentalSeedSource;

        /**
         * 品种名称
         */
        private String varietyName;

        /**
         * 作物类型
         */
        private String cropType;

        /**
         * 分发数量
         */
        private BigDecimal distributeQuantity;

        /**
         * 生产批次剩余可分发量
         */
        private BigDecimal produceBatchRemaining;

        /**
         * 创建时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    }
}
