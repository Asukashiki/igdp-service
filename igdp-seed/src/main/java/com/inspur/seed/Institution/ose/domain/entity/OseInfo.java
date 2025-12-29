package com.inspur.seed.Institution.ose.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * OSE基础信息表
 *
 * @author igdp
 */
@Data
@TableName("ose_info")
public class OseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * OSE唯一编码
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String oseId;

    /**
     * OSE行政编码
     */
    private String oseCode;

    /**
     * OSE名称
     */
    private String oseName;

    /**
     * 详细地址
     */
    private String location;

    /**
     * 行政区划编码
     */
    private String regionCode;

    /**
     * 行政区划名称(自动带出)
     */
    private String regionName;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactNumber;

    /**
     * 状态(ENABLED/DISABLED)
     */
    private String oseStatus;

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
