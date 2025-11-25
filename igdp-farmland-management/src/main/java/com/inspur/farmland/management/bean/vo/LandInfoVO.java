package com.inspur.farmland.management.bean.vo;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 土地信息VO
 *
 * @author inspur
 */
@Data
public class LandInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String userId;

    private String landName;

    private String landOwnership;

    private BigDecimal landArea;

    private String landLocation;

    private String landUse;

    private String landNature;

    private Date contractStartDate;

    private Date contractEndDate;

    private String transferStatusDesc;

    private BigDecimal transferPrice;

    private Date createTime;

    private Date updateTime;
}
