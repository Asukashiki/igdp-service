package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 农事记录数据采集VO
 *
 * @author igdp
 * @date 2025-11-26
 */
@Data
public class FarmingRecordDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 活动日期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activityDatetime;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
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
     * 审核状态
     */
    private String auditStatus;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDatetime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDatetime;

    // 兼容前端可能使用的首字母大写字段名
    @com.fasterxml.jackson.annotation.JsonProperty("Creator")
    public String getCreatorAlias() {
        return this.creator;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("Modifier")
    public String getModifierAlias() {
        return this.modifier;
    }

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditedDatetime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;
}
