package com.inspur.agriculture.input.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "AgriInput", description = "农业投入品")
public class AgriInput implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("投入品ID")
    private Long inputId;

    @ApiModelProperty("投入品名称")
    private String inputName;

    @ApiModelProperty("类型(pesticide-农药/fertilizer-化肥/seed-种子/other-其他)")
    private String type;

    @ApiModelProperty("唯一产品标识码/SKU")
    private String inputSku;

    @ApiModelProperty("注册商标")
    private String trademark;

    @ApiModelProperty("登记批号")
    private String registerCode;

    @ApiModelProperty("生产许可证号")
    private String productionLicense;

    @ApiModelProperty("产品标准证号")
    private String productionStandard;

    @ApiModelProperty("生产企业名称")
    private String producerName;

    @ApiModelProperty("生产企业地址")
    private String producerAddress;

    @ApiModelProperty("状态(active-正常/inactive-停用)")
    private String status;

    @ApiModelProperty("创建人")
    private String createPeople;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人")
    private String updatePeople;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除标志(0-正常/2-删除)")
    private String delFlag;

    @TableField(exist = false)
    @ApiModelProperty("农药特性")
    private PesticideProperties pesticideProperties;

    @TableField(exist = false)
    @ApiModelProperty("化肥特性")
    private FertilizerProperties fertilizerProperties;

    @TableField(exist = false)
    @ApiModelProperty("种子特性")
    private SeedProperties seedProperties;
}
