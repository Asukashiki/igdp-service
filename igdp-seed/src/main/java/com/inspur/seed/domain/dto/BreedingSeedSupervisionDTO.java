package com.inspur.seed.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 繁殖种子监管信息DTO
 *
 * @author igdp
 * @date 2025-11-29
 */
@Data
public class BreedingSeedSupervisionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 繁育批次ID(必填)
     */
    private String breedingBatchId;

    /**
     * 认证ID(必填)
     */
    private String authId;

    /**
     * 批准编号(必填)
     */
    private String approvalNumber;

    /**
     * 批准机构(必填)
     */
    private String approvalOrganization;

    /**
     * 批准日期(必填)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date approvalDate;

    /**
     * 认证文件(文件路径,必填)
     */
    private String certificationDocument;
}
