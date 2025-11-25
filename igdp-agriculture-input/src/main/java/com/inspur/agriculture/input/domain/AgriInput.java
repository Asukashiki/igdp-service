package com.inspur.agriculture.input.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 农业投入品对象 agri_input
 *
 * @author igdp
 */
@Data
@TableName("agri_input")
public class AgriInput implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 投入品ID */
    @TableId(type = IdType.AUTO)
    private Long inputId;

    /** 投入品名称 */
    private String inputName;

    /** 类型(pesticide-农药/fertilizer-化肥/seed-种子/other-其他) */
    private String type;

    /** 唯一产品标识码/SKU */
    private String inputSku;

    /** 注册商标 */
    private String trademark;

    /** 登记批号 */
    private String registerCode;

    /** 生产许可证号 */
    private String productionLicense;

    /** 产品标准证号 */
    private String productionStandard;

    /** 生产企业名称 */
    private String producerName;

    /** 生产企业地址 */
    private String producerAddress;

    /** 状态(active-正常/inactive-停用) */
    private String status;

    /** 创建人 */
    private String createPeople;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    private String updatePeople;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 删除标志(0-正常/2-删除) */
    private String delFlag;

    /** 农药特性 */
    @TableField(exist = false)
    private PesticideProperties pesticideProperties;

    /** 化肥特性 */
    @TableField(exist = false)
    private FertilizerProperties fertilizerProperties;

    /** 种子特性 */
    @TableField(exist = false)
    private SeedProperties seedProperties;
}
