package com.inspur.seed.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 繁殖种子监管信息VO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingSeedSupervisionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 繁育批次ID
     */
    private String breedingBatchId;

    /**
     * 认证ID
     */
    private String authId;

    /**
     * 批准编号
     */
    private String approvalNumber;

    /**
     * 批准机构
     */
    private String approvalOrganization;

    /**
     * 批准日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date approvalDate;

    /**
     * 认证文件
     */
    private String certificationDocument;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;
}
