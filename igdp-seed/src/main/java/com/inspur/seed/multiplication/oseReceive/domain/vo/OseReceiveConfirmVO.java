package com.inspur.seed.multiplication.oseReceive.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * OSE接收确认VO
 *
 * @author igdp
 */
@Data
public class OseReceiveConfirmVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 接收确认ID
     */
    private String receiveConfirmId;

    /**
     * 分发ID
     */
    private String distributeId;

    /**
     * OSE ID
     */
    private String oseId;

    /**
     * OSE名称
     */
    private String oseName;

    /**
     * 确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;

    /**
     * 确认操作人姓名
     */
    private String confirmPeople;

    /**
     * 接收状态
     */
    private String receiveStatus;

    /**
     * 补充说明
     */
    private String remark;

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
     * 分发明细
     */
    private DistributeDetailWrapper distributeDetail;

    @Data
    public static class DistributeDetailWrapper implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 分发总数量
         */
        private BigDecimal totalDistributeQuantity;

        /**
         * 明细列表
         */
        private List<DetailItem> detailList;
    }

    @Data
    public static class DetailItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 生产批次ID (种子ID)
         */
        private String breedSeedProduceBatchId;

        /**
         * 亲本种子来源
         */
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
         * 种子类型 (如：原原种、原种)
         */
        private String seedType;

        /**
         * 分发数量
         */
        private BigDecimal distributeQuantity;

        /**
         * 生产批次名称
         */
        private String produceBatchName;
    }
}
