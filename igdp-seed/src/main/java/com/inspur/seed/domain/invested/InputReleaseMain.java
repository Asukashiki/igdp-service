package com.inspur.seed.domain.invested;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 投入品分发主表实体类
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@TableName("t_input_release_main")
@Setter
@Getter
public class InputReleaseMain extends BaseEntity {

    /**
     * 主键（UUID）
     */
    @TableId
    private String id;

    /**
     * 分发单编号（系统生成）
     */
    private String releaseId;

    /**
     * 分发单名称
     */
    private String releaseName;

    /**
     * zoneId
     */
    private String zoneId;

    /**
     * 分发对象ID（UnionID）
     */
    private String targetId;

    /**
     * 分发对象收获地址
     */
    private String targetAddress;

    /**
     * 分发对象联系人
     */
    private String targetContact;

    /**
     * 分发对象联系电话
     */
    private String targetPhone;

    /**
     * 分发年度
     */
    private String releaseYear;

    /**
     * 分发日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    /**
     * 分发人
     */
    private String releaseBy;

    /**
     * 审核日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditDate;

    /**
     * 审核人
     */
    private String auditBy;

    /**
     * 分发机构（OSE）
     */
    private String releaseOrg;

    /**
     * 分发类型（OSE_TO_UNION: OSE分发到Union, UNION_TO_WOREDA: Union分发到Woreda）
     */
    private String releaseType;

    /**
     * 分发单状态：已分发distributed、已完成completed、未出库notDelivery
     */
    private String status;

    /**
     * 操作人
     */
    private String operateBy;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

    /**
     * 逻辑删除标识(0=未删除,1=已删除)
     */
    @TableLogic
    private Integer isDeleted;
}
