package com.inspur.seed.domain.invested;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Woreda接收确认实体
 * 对应表：t_input_receive_woreda
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@TableName("t_input_receive_woreda")
@Setter
@Getter
public class InputReceiveWoreda extends BaseEntity {

    /**
     * 主键UUID
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
     * 分发对象ID（WoredaID）
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
     * 确认机构（Woreda）
     */
    private String confirmOrg;

    /**
     * 确认时间
     */
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
    private LocalDate releaseDate;

    /**
     * 分发人
     */
    private String releaseBy;

    /**
     * 分发机构（Union）
     */
    private String releaseOrg;

    /**
     * 操作人
     */
    private String operateBy;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 逻辑删除标识(0=未删除,1=已删除)
     */
    @TableLogic
    private Integer isDeleted;
}
