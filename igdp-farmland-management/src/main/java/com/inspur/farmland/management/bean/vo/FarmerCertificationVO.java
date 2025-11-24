package com.inspur.farmland.management.bean.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 农民认证VO
 * 
 * @author inspur
 */
@Data
public class FarmerCertificationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String idCardNumber;

    private String realName;

    private String phoneNumber;

    private String address;

    private String statusDesc;

    private String rejectReason;

    private Date createTime;

    private Date updateTime;
}