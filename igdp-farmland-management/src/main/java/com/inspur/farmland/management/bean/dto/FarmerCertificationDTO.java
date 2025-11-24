package com.inspur.farmland.management.bean.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 农民认证DTO
 * 
 * @author inspur
 */
@Data
public class FarmerCertificationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String idCardNumber;

    private String realName;

    private String phoneNumber;

    private String address;

    private String idCardFrontUrl;

    private String idCardBackUrl;

    private String idCardHandheldUrl;

    private String landProofUrl;

    private String otherProofUrl;

    private Integer status;

    private Long approverId;

    private String rejectReason;

    private Date createTime;

    private Date updateTime;
}