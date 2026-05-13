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
 * Union接收确认表实体类
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@TableName("t_input_receive_union")
@Setter
@Getter
public class InputReceiveUnion extends BaseEntity {

    /**
     * 主键（UUID）
     */
    @TableId
    private String id;

    /**
     * 关联分发单编号
     */
    private String releaseId;

    /**
     * 分发单名称
     */
    private String releaseName;

    /**
     * 分发对象ID（UnionID）
     */
    private String targetId;

    /**
     * 分发对象地址
     */
    private String targetAddress;

    /**
     * 联系电话
     */
    private String targetPhone;

    /**
     * 确认人
     */
    private String confirmBy;

    /**
     * 确认机构（Union）
     */
    private String confirmOrg;

    /**
     * 确认时间（空表示未确认）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmTime;

    /**
     * 接收状态（待确认/已确认）
     */
    private String receiveStatus;

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
     * 分发机构（OSE）
     */
    private String releaseOrg;

    /**
     * 数据标识（0/1）
     */
    private String flag;

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
