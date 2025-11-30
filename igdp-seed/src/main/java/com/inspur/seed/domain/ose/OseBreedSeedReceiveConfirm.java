package com.inspur.seed.domain.ose;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * OSE接收育种家种子确认表
 *
 * @author igdp
 */
@Data
@TableName("ose_breed_seed_receive_confirm")
public class OseBreedSeedReceiveConfirm implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String receiveConfirmId;

    /**
     * 分发主表ID
     */
    private String distributeId;

    /**
     * OSE ID
     */
    private String oseId;

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
     * 接收状态(PENDING/CONFIRMED)
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
}
