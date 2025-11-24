package com.inspur.farmland.management.bean.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 土地信息DTO
 * 
 * @author inspur
 */
@Data
public class LandInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String landName;

    private String landOwnership;

    private BigDecimal landArea;

    private String landLocation;

    private String landUse;

    private String landNature;

    private Date contractStartDate;

    private Date contractEndDate;

    private Integer transferStatus;

    private BigDecimal transferPrice;

    private String transferee;

    private Date createTime;

    private Date updateTime;

    private String remark;
}