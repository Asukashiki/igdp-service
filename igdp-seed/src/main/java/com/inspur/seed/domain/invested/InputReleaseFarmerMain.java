package com.inspur.seed.domain.invested;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 农民分发主表实体
 * 对应表：t_input_release_farmer_main
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@TableName("t_input_release_farmer_main")
@Setter
@Getter
public class InputReleaseFarmerMain extends BaseEntity {

    /**
     * 主键（UUID）
     */
    @TableId
    private String id;

    /**
     * 分发单编号（系统生成，格式：FRL+yyyyMMdd+6位随机码）
     */
    private String releaseId;

    /**
     * 农民ID
     */
    private String farmerId;

    /**
     * 农民姓名
     */
    private String farmerName;

    /**
     * 联系电话
     */
    private String farmerPhone;

    /**
     * 农民地址
     */
    private String farmerAddress;

    /**
     * 领用状态（未领用/已领用）
     */
    private String receiveStatus;

    /**
     * 分发年度
     */
    private Integer releaseYear;

    /**
     * 分发日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseDate;

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
     * 分发机构（Woreda）
     */
    private String releaseOrg;

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
